package com.geovannycode.hibernate.dto;

import java.time.LocalDateTime;

public record TaskDTO(Integer id, Integer userId, String content, boolean completed, LocalDateTime createdAt) {
}
