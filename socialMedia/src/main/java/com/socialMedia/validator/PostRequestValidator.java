package com.socialMedia.validator;

import com.socialMedia.dto.PostRequestDTO;
import com.socialMedia.exception.MandatoryFieldsMissingException;
import com.socialMedia.exception.NoUsersFoundException;
import com.socialMedia.exception.NotValidIdException;
import com.socialMedia.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostRequestValidator {

    private final PostRepository postRepository;

    public void validatePostRequest(final PostRequestDTO postRequestDTO) throws MandatoryFieldsMissingException {
        if (postRequestDTO == null){
            log.error("Request was empty!");
            throw new MandatoryFieldsMissingException("Request was empty!");
        } else if (postRequestDTO.getMessage() == null) {
            log.error("Mandatory message field is missing!");
            throw new MandatoryFieldsMissingException("Mandatory message field is missing!");
        } else if (postRequestDTO.getMessage().isBlank()) {
            log.error("Mandatory message field is empty!");
            throw new MandatoryFieldsMissingException("Mandatory message field is empty!");
        }
    }

    public void validatePostById(final Long id) throws NoUsersFoundException {
        if (!postRepository.existsById(id)) {
            log.error("Post with id number " + id + " not found!");
            throw new NoUsersFoundException("Post with id number " + id + " not found!");
        }
    }


}
