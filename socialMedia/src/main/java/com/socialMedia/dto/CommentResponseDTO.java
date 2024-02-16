package com.socialMedia.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentResponseDTO {

    private Long id;
    private Integer likeCount;
    private String commentMessage;
    private Long postId;
    private Long userId;
    private LocalDateTime createdAt;

}
