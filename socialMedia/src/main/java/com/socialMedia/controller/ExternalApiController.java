package com.socialMedia.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@Slf4j
@RequiredArgsConstructor
public class ExternalApiController {



    private final RestTemplate restTemplate;
    private final CacheManager cacheManager;

    private final String ALL_USERS = "http://localhost:2222/api/user/";
    private final String FORUM_BY_USER_ID = "http://localhost:2222/api/user/{userId}";

    @GetMapping("/")
    public ResponseEntity<?> getAllUsers() {
        ResponseEntity<?> response = restTemplate.getForEntity(ALL_USERS, String.class);
        return ResponseEntity.ok(response.getBody());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getForumsByUserId(@PathVariable("userId") final Long userId) {
        final String cacheKey = "allUserForumPosts" + userId;
        final Cache usersCache = cacheManager.getCache("externalApiCache");
        Cache.ValueWrapper cachedResponse = usersCache.get(cacheKey);

        if (cachedResponse != null) {
            log.info("Response from cache.");
            return ResponseEntity.status(HttpStatus.OK).body(cachedResponse.get());
        } else {
            ResponseEntity<?> response = restTemplate.getForEntity(FORUM_BY_USER_ID, String.class, userId);
            usersCache.put(cacheKey, response.getBody());
            log.info("Response from DB.");
            return ResponseEntity.status(HttpStatus.OK).body(response.getBody());
        }
    }
}