package com.socialMedia.service;

import com.socialMedia.dto.CommentRequestDTO;
import com.socialMedia.dto.CommentResponseDTO;
import com.socialMedia.exception.MandatoryFieldsMissingException;
import com.socialMedia.exception.NoPostFoundException;
import com.socialMedia.exception.NoUsersFoundException;
import com.socialMedia.exception.NotValidIdException;
import com.socialMedia.model.Comment;
import com.socialMedia.model.Post;
import com.socialMedia.model.User;
import com.socialMedia.repository.CommentRepository;
import com.socialMedia.service.mappingService.CommentMappingService;
import com.socialMedia.validator.CommentRequestValidator;
import com.socialMedia.validator.GlobalExceptionValidator;
import com.socialMedia.validator.PostRequestValidator;
import com.socialMedia.validator.UserRequestValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommentService {

    private final GlobalExceptionValidator globalExceptionValidator;
    private final UserRequestValidator userRequestValidator;
    private final PostRequestValidator postRequestValidator;
    private final CommentRequestValidator commentRequestValidator;
    private final CommentRepository commentRepository;
    private final CommentMappingService commentMappingService;

    @CacheEvict(value = {"postCache", "userCache"}, allEntries = true)
    public List<CommentResponseDTO> addComment(final CommentRequestDTO commentRequestDTO) throws NotValidIdException, MandatoryFieldsMissingException, NoPostFoundException, NoUsersFoundException {
        globalExceptionValidator.validateId(commentRequestDTO.getUserId());
        globalExceptionValidator.validateId(commentRequestDTO.getPostId());
        userRequestValidator.validateUserById(commentRequestDTO.getUserId());
        postRequestValidator.validatePostById(commentRequestDTO.getPostId());
        commentRequestValidator.validateCommentRequest(commentRequestDTO);

        var comment = commentMappingService.mapToEntity(commentRequestDTO);
        commentRepository.save(comment);
        log.info("Comment added and saved successfully!");
        return commentMappingService.mapToResponse(commentRepository.findAll());
    }

    @CacheEvict(value = {"postCache", "userCache"}, allEntries = true)
    public List<CommentResponseDTO> likeComment(final Long commentId) throws NotValidIdException, NoUsersFoundException {
        globalExceptionValidator.validateId(commentId);
        commentRequestValidator.validateCommentById(commentId);
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow();

        comment.setLikeCount(comment.getLikeCount() + 1);
        User user = comment.getUser();

        commentRepository.save(comment);
        log.info("Comment liked successfully!");
        return commentMappingService.mapToResponse(user.getComments());
    }

    @CacheEvict(value = {"postCache", "userCache"}, allEntries = true)
    public List<CommentResponseDTO> deleteComment(final Long commentId) throws NotValidIdException, NoUsersFoundException {
        globalExceptionValidator.validateId(commentId);
        commentRequestValidator.validateCommentById(commentId);
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow();

        User user = comment.getUser();

        commentRepository.delete(comment);
        log.info("Comment with id: " + commentId + " deleted successfully!");
        return commentMappingService.mapToResponse(user.getComments());
    }
}
