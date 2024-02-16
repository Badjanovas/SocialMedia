package com.socialMedia.validator;

import com.socialMedia.dto.CommentRequestDTO;
import com.socialMedia.exception.MandatoryFieldsMissingException;
import com.socialMedia.exception.NoUsersFoundException;
import com.socialMedia.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommentRequestValidator {

    private final CommentRepository commentRepository;

    public void validateCommentRequest(CommentRequestDTO commentRequestDTO) throws MandatoryFieldsMissingException {
        if(commentRequestDTO == null){
            log.error("Request was empty!");
            throw new MandatoryFieldsMissingException("Request was empty!");
        } else if (commentRequestDTO.getCommentText() == null){
            log.error("Mandatory commentText field is missing!");
            throw new MandatoryFieldsMissingException("Mandatory commentText field is missing!");
        } else if (commentRequestDTO.getCommentText().isBlank()) {
            log.error("Mandatory commentText field is empty!");
            throw new MandatoryFieldsMissingException("Mandatory commentText field is empty!");
        }
    }

    public void validateCommentById(final Long id) throws NoUsersFoundException {
        if (!commentRepository.existsById(id)) {
            log.error("Comment with id number " + id + " not found!");
            throw new NoUsersFoundException("Comment with id number " + id + " not found!");
        }
    }
}
