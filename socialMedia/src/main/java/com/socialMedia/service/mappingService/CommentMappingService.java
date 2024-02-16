package com.socialMedia.service.mappingService;

import com.socialMedia.dto.CommentRequestDTO;
import com.socialMedia.dto.CommentResponseDTO;
import com.socialMedia.model.Comment;
import com.socialMedia.model.Post;
import com.socialMedia.model.User;
import com.socialMedia.repository.PostRepository;
import com.socialMedia.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentMappingService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public Comment mapToEntity(CommentRequestDTO commentRequestDTO){
        User user = userRepository.findById(commentRequestDTO.getUserId())
                .orElse(null);

        Post post = postRepository.findById(commentRequestDTO.getPostId())
                .orElse(null);

        return Comment.builder()
                .message(commentRequestDTO.getCommentText())
                .user(user)
                .post(post)
                .likeCount(0)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public List<CommentResponseDTO> mapToResponse(List<Comment> allComments){
        List<CommentResponseDTO> mappedComments = new ArrayList<>();
        for (Comment comment : allComments) {
            CommentResponseDTO dto = new CommentResponseDTO();
            dto.setId(comment.getId());
            dto.setLikeCount(comment.getLikeCount());
            dto.setUserId(comment.getUser().getId());
            dto.setPostId(comment.getPost().getId());
            dto.setCommentMessage(comment.getMessage());
            dto.setCreatedAt(comment.getCreatedAt());
            mappedComments.add(dto);
        }
        return mappedComments;
    }
}
