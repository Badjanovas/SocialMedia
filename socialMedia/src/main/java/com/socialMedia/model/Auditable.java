package com.socialMedia.model;


import java.time.LocalDateTime;
public interface Auditable {


    LocalDateTime getCreatedAt();
    void setCreatedAt(LocalDateTime createdAt);
}
