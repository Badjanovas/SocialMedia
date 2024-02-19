package com.socialMedia.dto;

import lombok.Data;

import java.util.List;
@Data
public class ExternalApiUserDTO {

    private Long id;
    private String name;
    private List<ExternalApiForumMessageDto> forumMessages;

}
