package com.socialMedia.validator;

import com.socialMedia.dto.ExternalApiUserDTO;
import com.socialMedia.exception.NoUsersFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ExternalApiValidator {

    public void validateApiUserList(List<ExternalApiUserDTO> users) throws NoUsersFoundException {
        if (users.isEmpty()){
            log.error("No users were found in the DB!");
            throw new NoUsersFoundException("No users were found!");
        }
    }

}
