package com.socialMedia.controller;

import com.socialMedia.exception.NoUsersFoundException;
import com.socialMedia.exception.NotValidIdException;
import com.socialMedia.service.ExternalApiService;
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
    private final ExternalApiService externalApiService;

    private final String FORUM_BY_USER_ID = "http://localhost:2222/api/user/{userId}";

    @GetMapping("/")
    public ResponseEntity<?> getAllUsers() throws NoUsersFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(externalApiService.getAllUsersFromExternalApi());
    }

//This method fetches forum messages for a specific user either from a cache or by making a request to an external API,
//caching the result for future requests.
    @GetMapping("/{userId}/optionOne")
    public ResponseEntity<?> fetchForumMessagesByUserId(@PathVariable("userId") final Long userId) {
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

    @GetMapping("/{userId}/optionTwo")
    public ResponseEntity<?> getForumMessagesByUserId(@PathVariable("userId") final Long userId) throws NoUsersFoundException, NotValidIdException {
        return ResponseEntity.status(HttpStatus.OK).body(externalApiService.getAllForumMessagesByUserId(userId, externalApiService.getAllUsersFromExternalApi()));
    }

    @GetMapping("/userWithMostMessages")
    public ResponseEntity<?> getUserWithMostMessages() throws NoUsersFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(externalApiService.getUserWithMostForumMessages(externalApiService.getAllUsersFromExternalApi()));
    }
}