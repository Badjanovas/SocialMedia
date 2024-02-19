package com.socialMedia.service;

import com.socialMedia.dto.UserRequestDTO;
import com.socialMedia.dto.UserResponseDTO;
import com.socialMedia.exception.MandatoryFieldsMissingException;
import com.socialMedia.exception.NoUsersFoundException;
import com.socialMedia.exception.NotValidAgeException;
import com.socialMedia.exception.NotValidIdException;
import com.socialMedia.model.User;
import com.socialMedia.repository.UserRepository;
import com.socialMedia.service.mappingService.UserMappingService;
import com.socialMedia.validator.GlobalExceptionValidator;
import com.socialMedia.validator.UserRequestValidator;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Data
public class UserService {

    private final UserRepository userRepository;
    private final CacheManager cacheManager;
    private final Cache userCache;
    private final UserMappingService userMappingService;
    private final UserRequestValidator userRequestValidator;
    private final GlobalExceptionValidator globalExceptionValidator;

    public UserService(UserRepository userRepository, CacheManager cacheManager, UserMappingService userMappingService, UserRequestValidator userRequestValidator, GlobalExceptionValidator globalExceptionValidator) {
        this.userRepository = userRepository;
        this.cacheManager = cacheManager;
        this.userCache = cacheManager.getCache("userCache");
        this.userMappingService = userMappingService;
        this.userRequestValidator = userRequestValidator;
        this.globalExceptionValidator = globalExceptionValidator;
    }

    // we can optimise this code by using spring boot annotation @Cacheable(value = "userCache", key = "allUsers")
    // but I am not sure how to handle logging.
    public List<User> getAllUsers() throws NoUsersFoundException {
        final String cacheKey = "allUsersKey";
        List<User> users = userCache != null ? userCache.get(cacheKey, List.class) : null;

        if (users != null) {
            log.info("Fetching users from cache.");
            return users;
        }

        log.info("Looking for users in DB...");
        users = userRepository.findAll();
        userRequestValidator.validateUserList(users);
        userCache.put(cacheKey, users);
        log.info(users.size() + " users were found in the DB. Caching the result.");

        return users;
    }

    @CacheEvict(value = {"postCache", "userCache"}, allEntries = true)
    public List<UserResponseDTO> addUser(final UserRequestDTO userRequestDto) throws MandatoryFieldsMissingException, NotValidAgeException {
        userRequestValidator.validateUserRequest(userRequestDto);
        userRequestValidator.validateUserAgeRequest(userRequestDto.getAge());
        final User user = userMappingService.mapToEntity(userRequestDto);
        userRepository.save(user);
        log.info("New user " + userRequestDto.getName() + " was saved successfully!");

        return userMappingService.mapToResponse(userRepository.findAll());
    }

    @CacheEvict(value = {"postCache", "userCache"}, allEntries = true)
    public List<User> deleteUser(final Long id) throws NoUsersFoundException, NotValidIdException {
        globalExceptionValidator.validateId(id);
        userRequestValidator.validateUserById(id);
        userRepository.deleteById(id);
        log.info("User with id number " + id + " was deleted fromDB successfully.");

        return userRepository.findAll();
    }

    public List<UserResponseDTO> sortByAge() throws NoUsersFoundException {
        final String cacheKey = "sortedUsers";
        final List<User> users = userCache != null ? userCache.get(cacheKey, List.class) : null;

        if (users != null) {
            log.info("Fetching sorted users from cache.");
            return userMappingService.mapToResponse(users);
        }

        final List<User> allUsers = userRepository.findAll();
        userRequestValidator.validateUserList(allUsers);

        final List<User> sortedUsers = allUsers.stream()
                .sorted(Comparator.comparingInt(User::getAge))
                .toList();

        log.info("Fetching sorted users from database.");
        userCache.put(cacheKey, sortedUsers);
        return userMappingService.mapToResponse(sortedUsers);
    }

    public List<UserResponseDTO> getUserWithMostPosts() throws NoUsersFoundException {
        final List<User> users = userRepository.findAll();
        userRequestValidator.validateUserList(users);

        final int maxPosts = users.stream()
                .mapToInt(user -> user.getPosts().size())
                .max()
                .orElse(0);

        List<User> usersWithMostPosts = users.stream()
                .filter(user -> user.getPosts().size() == maxPosts)
                .collect(Collectors.toList());

        return userMappingService.mapToResponse(usersWithMostPosts);
    }

}
