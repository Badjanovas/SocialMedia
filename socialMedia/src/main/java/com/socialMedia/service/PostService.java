package com.socialMedia.service;

import com.socialMedia.dto.PostRequestDTO;
import com.socialMedia.dto.PostResponseDTO;
import com.socialMedia.exception.MandatoryFieldsMissingException;
import com.socialMedia.exception.NoPostFoundException;
import com.socialMedia.exception.NoUsersFoundException;
import com.socialMedia.exception.NotValidIdException;
import com.socialMedia.model.Post;
import com.socialMedia.model.User;
import com.socialMedia.repository.PostRepository;
import com.socialMedia.repository.UserRepository;
import com.socialMedia.service.mappingService.PostMappingService;
import com.socialMedia.validator.GlobalExceptionValidator;
import com.socialMedia.validator.PostRequestValidator;
import com.socialMedia.validator.UserRequestValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@Slf4j
public class PostService {

    private final PostMappingService postMappingService;
    private final PostRepository postRepository;
    private final PostRequestValidator postRequestValidator;
    private final UserRequestValidator userRequestValidator;
    private final GlobalExceptionValidator globalExceptionValidator;
    private final UserRepository userRepository;
    private final CacheManager cacheManager;
    private final Cache postCache;

    public PostService(PostMappingService postMappingService,
                       PostRepository postRepository,
                       PostRequestValidator postRequestValidator,
                       UserRequestValidator userRequestValidator,
                       GlobalExceptionValidator globalExceptionValidator,
                       UserRepository userRepository,
                       CacheManager cacheManager) {
        this.postMappingService = postMappingService;
        this.postRepository = postRepository;
        this.postRequestValidator = postRequestValidator;
        this.userRequestValidator = userRequestValidator;
        this.globalExceptionValidator = globalExceptionValidator;
        this.userRepository = userRepository;
        this.cacheManager = cacheManager;
        this.postCache = cacheManager.getCache("postCache");
    }

    @CacheEvict(value = {"postCache", "userCache"}, allEntries = true)
    public List<PostResponseDTO> addPost(final PostRequestDTO postRequestDTO) throws MandatoryFieldsMissingException, NoUsersFoundException, NotValidIdException {
        globalExceptionValidator.validateId(postRequestDTO.getUserId());
        userRequestValidator.validateUserById(postRequestDTO.getUserId());
        postRequestValidator.validatePostRequest(postRequestDTO);
        final Post post = postMappingService.mapToEntity(postRequestDTO);
        postRepository.save(post);
        log.info("New post was created and saved successfully!");

        return postMappingService.mapToResponse(postRepository.findAll());
    }

    @CacheEvict(value = {"postCache", "userCache"}, allEntries = true)
    public List<PostResponseDTO> deletePost(final Long id) throws NotValidIdException, NoPostFoundException {
        globalExceptionValidator.validateId(id);
        postRequestValidator.validatePostById(id);
        postRepository.deleteById(id);

        return postMappingService.mapToResponse(postRepository.findAll());
    }

    public List<PostResponseDTO> getAllPostByUserId(final Long id) throws NotValidIdException, NoUsersFoundException {

        globalExceptionValidator.validateId(id);

        final String cacheKey = "postsOfUser" + id;
        final List<Post> posts;

        if (postCache != null) {
            posts = postCache.get(cacheKey, List.class);
            if (posts != null) {
                log.info("Fetching posts from cache of user with id number: " + id);
                return postMappingService.mapToResponse(posts);
            }
        }

        userRequestValidator.validateUserById(id);
        final var user = userRepository.findById(id);

        if (postCache != null) {
            postCache.put(cacheKey, user.get().getPosts());
        }
        log.info(user.get().getPosts().size() + " posts were found in DB.");
        return postMappingService.mapToResponse(user.get().getPosts());
    }

    @CacheEvict(value = {"postCache", "userCache"}, allEntries = true)
    public List<PostResponseDTO> addLikeToPost(Long postId) throws NotValidIdException, NoPostFoundException {
        globalExceptionValidator.validateId(postId);
        postRequestValidator.validatePostById(postId);
        Post post = postRepository.findById(postId)
                .orElseThrow();

        post.setLikeCount(post.getLikeCount() + 1);
        User user = post.getUser();

        postRepository.save(post);
        log.info("Post liked successfully!");
        return postMappingService.mapToResponse(user.getPosts());
    }

    public Post getMostLikedPost() throws NoPostFoundException {
        final List<Post> allPosts = postRepository.findAll();
        postRequestValidator.validatePostList(allPosts);
        log.info("Most liked post was found successfully.");
        return allPosts.stream()
                .max(Comparator.comparingInt(Post::getLikeCount))
                .orElse(null);
    }
}
