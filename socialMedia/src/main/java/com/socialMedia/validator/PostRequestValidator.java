package com.socialMedia.validator;

import com.socialMedia.dto.PostRequestDTO;
import com.socialMedia.exception.MandatoryFieldsMissingException;
import com.socialMedia.exception.NoPostFoundException;
import com.socialMedia.exception.NoUsersFoundException;
import com.socialMedia.exception.NotValidIdException;
import com.socialMedia.model.Post;
import com.socialMedia.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public void validatePostById(final Long id) throws NoPostFoundException {
        if (!postRepository.existsById(id)) {
            log.error("Post with id number " + id + " not found!");
            throw new NoPostFoundException("Post with id number " + id + " not found!");
        }
    }

    public void validatePostList(final List<Post> allPosts) throws NoPostFoundException {
        if (allPosts.isEmpty()){
            log.error("No posts were found in the DB!");
            throw new NoPostFoundException("No posts were found!");
        }
    }


}
