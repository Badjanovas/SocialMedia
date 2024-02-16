package com.socialMedia.validator;

import com.socialMedia.exception.NotValidIdException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class GlobalExceptionValidator {
    public void validateId(final Long id) throws NotValidIdException {
        if (id == null || id <=0){
            log.error("Invalid id!");
            throw new NotValidIdException("Invalid id!");
        }
    }
}
