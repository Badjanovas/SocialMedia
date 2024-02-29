package com.socialMedia.service;

import com.socialMedia.dto.ExternalApiUserDTO;
import com.socialMedia.dto.ExternalApiForumMessageDto;
import com.socialMedia.exception.NoUsersFoundException;
import com.socialMedia.exception.NotValidIdException;
import com.socialMedia.validator.ExternalApiValidator;
import com.socialMedia.validator.GlobalExceptionValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Comparator;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ExternalApiService {

    private final RestTemplate restTemplate;
    private final ExternalApiValidator externalApiValidator;
    private final GlobalExceptionValidator globalExceptionValidator;
    private final CacheManager cacheManager;

    @Cacheable("externalApiCache")
    public List<ExternalApiUserDTO> getAllUsersFromExternalApi() throws NoUsersFoundException {
        /*final String cacheKey = "allUsers";
        final Cache usersCache = cacheManager.getCache("externalApiCache");
        Cache.ValueWrapper cachedResponse = usersCache.get(cacheKey);

        if (cachedResponse != null) {
            log.info("Response from cache.");
            return (List<ExternalApiUserDTO>) cachedResponse.get();
        }*/

        final String url = "http://localhost:2222/api/user/";
        ResponseEntity<ExternalApiUserDTO[]> response = restTemplate.getForEntity(url, ExternalApiUserDTO[].class);
        ExternalApiUserDTO[] users = response.getBody();
        externalApiValidator.validateApiUserList(List.of(users));
        log.info("Response from DB.");
        //usersCache.put(cacheKey, List.of(users));
        return List.of(users);
    }


    public List<ExternalApiForumMessageDto> getAllForumMessagesByUserId(final Long id, final List<ExternalApiUserDTO> usersFromApi) throws NotValidIdException, NoUsersFoundException {
        globalExceptionValidator.validateId(id);
        externalApiValidator.validateApiUserList(usersFromApi);
        for (ExternalApiUserDTO user : usersFromApi) {
            if (user.getId() == id) {
                return user.getForumMessages();
            }
        }
        return null;
    }

    public ExternalApiUserDTO getUserWithMostForumMessages(final List<ExternalApiUserDTO> usersFromApi) throws NoUsersFoundException {
        externalApiValidator.validateApiUserList(usersFromApi);
        log.info("Searching for user with largest amount of forum messages.");
        return usersFromApi.stream()
                .max(Comparator.comparingInt(user -> (user.getForumMessages() != null ? user.getForumMessages().size() : 0)))
                .orElse(null);
    }

}
