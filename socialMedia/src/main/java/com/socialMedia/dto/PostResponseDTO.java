package com.socialMedia.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostResponseDTO {

    private Long id;
    private String postText;
    private Integer likeCount;
    private Long userId;
    private Integer commentCount;
    private LocalDateTime createdAt;
}
