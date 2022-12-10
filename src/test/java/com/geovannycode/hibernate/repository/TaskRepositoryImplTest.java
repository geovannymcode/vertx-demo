package com.geovannycode.hibernate.repository;

import com.geovannycode.hibernate.dto.TaskDTO;
import com.geovannycode.hibernate.model.Project;
import com.geovannycode.hibernate.model.Task;
import com.geovannycode.hibernate.repository.impl.TaskRepositoryImpl;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Vertx;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.hibernate.cfg.Configuration;
import org.hibernate.reactive.provider.ReactiveServiceRegistryBuilder;
import org.hibernate.reactive.stage.Stage;
import org.hibernate.service.ServiceRegistry;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Properties;

@Testcontainers
@ExtendWith(VertxExtension.class)
class TaskRepositoryImplTest {

    private final String DB_NAME = "hibernatedb";
    private final String DB_USER = "user";
    private final String DB_PASSWORD = "secret";

    @Container
    PostgreSQLContainer container = new PostgreSQLContainer("postgres:13-alpine")
            .withDatabaseName(DB_NAME)
            .withUsername(DB_USER)
            .withPassword(DB_PASSWORD);

    TaskRepositoryImpl repository;

    @BeforeEach
    void setup(Vertx vertx, VertxTestContext context){
        Properties hibernateProps = new Properties();
        String url = "jdbc:postgresql://localhost:" + Integer.toString(container.getFirstMappedPort()) + "/" + DB_NAME;
        hibernateProps.put("hibernate.connection.url", url);
        hibernateProps.put("hibernate.connection.username", DB_USER);
        hibernateProps.put("hibernate.connection.password", DB_PASSWORD);
        hibernateProps.put("javax.persistence.schema-generation.database.action", "create");
        hibernateProps.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQL95Dialect");
        hibernateProps.put("hibernate.generate_statistics", true);
        hibernateProps.put("hibernate.show_sql", true);
        hibernateProps.put("hibernate.format_sql", true);
        Configuration hibernateConfiguration = new Configuration();
        hibernateConfiguration.setProperties(hibernateProps);
        hibernateConfiguration.addAnnotatedClass(Task.class);
        hibernateConfiguration.addAnnotatedClass(Project.class);
        ServiceRegistry serviceRegistry = new ReactiveServiceRegistryBuilder()
                .applySettings(hibernateConfiguration.getProperties()).build();
        Stage.SessionFactory sessionFactory = hibernateConfiguration
                .buildSessionFactory(serviceRegistry).unwrap(Stage.SessionFactory.class);
        repository = new TaskRepositoryImpl(sessionFactory);
        context.completeNow();
    }

    @Test
    void createTaskTest(Vertx vertx, VertxTestContext context){
        TaskDTO taskDTO = new TaskDTO(null, 1 ,"My Task", false, LocalDateTime.now(), Optional.empty());
        context.verify(()->{
           repository.createTask(taskDTO)
                   .onFailure(err -> context.failNow(err))
                   .onSuccess(result ->{
                       Assertions.assertNotNull(result);
                       Assertions.assertNotNull(result.id());
                       Assertions.assertEquals(1, result.id());
                       context.completeNow();
                   });
        });
    }

    @Test
    void findTaskByIdDoesNotExistTest(Vertx vertx, VertxTestContext context){
        context.verify(()->
        {
           repository.findTaskById(1)
                   .onSuccess(r->{
                      Assertions.assertTrue(r.isEmpty());
                      context.completeNow();
                   })
                   .onFailure(err -> context.failNow(err));
        });
    }

    @Test
    void findTaskByIdExistsTest(Vertx vertx, VertxTestContext context){
        TaskDTO taskDTO = new TaskDTO(null, 1 ,"My Task", false, LocalDateTime.now(), Optional.empty());
        context.verify(()->{
            repository.createTask(taskDTO)
                    .compose(r-> repository.findTaskById(r.id()))
                    .onFailure(err -> context.failNow(err))
                    .onSuccess(result ->{
                        Assertions.assertTrue(result.isPresent());
                        context.completeNow();
                    });
        });
    }

    @Test
    void removeTaskTest(Vertx vertx, VertxTestContext context){
        TaskDTO taskDTO = new TaskDTO(null, 1 ,"My Task", false, LocalDateTime.now(), Optional.empty());
        context.verify(()->{
           repository.createTask(taskDTO)
                   .compose(r->{
                      Assertions.assertEquals(1,r.id());
                      return repository.removeTask(r.id());
                   }).compose(r-> repository.findTaskById(1))
                   .onFailure(err -> context.failNow(err))
                   .onSuccess(r->{
                      Assertions.assertTrue(r.isEmpty());
                      context.completeNow();
                   });
        });
    }

    @Test
    void updateTaskTest(Vertx vertx, VertxTestContext context){
        TaskDTO taskDTO = new TaskDTO(null, 1 ,"My Task", false, LocalDateTime.now(), Optional.empty());
        context.verify(()->{
            repository.createTask(taskDTO)
                    .compose(r->{
                       Assertions.assertEquals(1, r.id());
                       TaskDTO updateTask = new TaskDTO(1,r.userId(),"Update content", true, r.createdAt(), Optional.empty());
                       return repository.updateTask(updateTask);
                    }).compose(r->{
                        Assertions.assertTrue(r.completed());
                        Assertions.assertEquals("Update content",r.content());
                        return repository.findTaskById(1);
                    }).onFailure(err -> context.failNow(err))
                    .onSuccess(r->{
                       Assertions.assertTrue(r.isPresent());
                       TaskDTO result = r.get();
                       Assertions.assertTrue(result.completed());
                        Assertions.assertEquals("Update content", result.content());
                        context.completeNow();
                    });
                });
    }

    @Test
    void findTaskByUserTest(Vertx vertx, VertxTestContext context){
        TaskDTO task1 = new TaskDTO(null, 1, "My task", false, LocalDateTime.now(), Optional.empty());
        TaskDTO task2 = new TaskDTO(null, 1, "My task", false, LocalDateTime.now(), Optional.empty());
        TaskDTO task3 = new TaskDTO(null, 2, "My task", false, LocalDateTime.now(), Optional.empty());
        CompositeFuture createTask = CompositeFuture.join(repository.createTask(task1), repository.createTask(task2), repository.createTask(task3));
        context.verify(()->{
            createTask.compose(r->{
                Assertions.assertTrue(r.succeeded());
                Assertions.assertTrue(r.isComplete());
                System.out.println(r.list().get(0));
                System.out.println(r.list().get(1));
                System.out.println(r.list().get(2));
                return repository.findTasksByUser(1);
            }).onFailure(err-> context.failNow(err))
                    .onSuccess(r->{
                        Assertions.assertEquals(2, r.tasks().size());
                        context.completeNow();
                    });
        });

    }
}
