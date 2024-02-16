package com.socialMedia.service.mappingService;

import com.socialMedia.dto.PostRequestDTO;
import com.socialMedia.dto.PostResponseDTO;
import com.socialMedia.model.Comment;
import com.socialMedia.model.Post;
import com.socialMedia.model.User;
import com.socialMedia.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostMappingService {

    private final UserRepository userRepository;

    public Post mapToEntity(PostRequestDTO postRequestDTO){
        User user = userRepository.findById(postRequestDTO.getUserId())
                .orElse(null);

        return Post.builder()
                .message(postRequestDTO.getMessage())
                .user(user)
                .createdAt(LocalDateTime.now())
                .likeCount(0)
                .build();
    }

    public List<PostResponseDTO> mapToResponse(List<Post> allPosts){
        List<PostResponseDTO> mappedPosts = new ArrayList<>();
        for (Post post : allPosts) {
            PostResponseDTO dto = new PostResponseDTO();
            dto.setId(post.getId());
            dto.setPostText(post.getMessage());
            dto.setUserId(post.getUser().getId());
            dto.setLikeCount(post.getLikeCount());
            dto.setCommentCount(determineCommentCount(post));
            dto.setCreatedAt(post.getCreatedAt());
            mappedPosts.add(dto);
        }
        return mappedPosts;
    }

    private int determineCommentCount(Post post){
        return post.getComments() != null ? post.getComments().size() : 0;
    }
    //Conditional Operator (? :): The method uses the ternary conditional operator to succinctly express a conditional logic in a single line.
    // This operator is a shorthand for an if-else statement and is used here to decide between two values based on a condition.

}
