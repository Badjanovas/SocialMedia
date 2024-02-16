package com.socialMedia.validator;

import com.socialMedia.dto.UserRequestDTO;
import com.socialMedia.exception.MandatoryFieldsMissingException;
import com.socialMedia.exception.NoUsersFoundException;
import com.socialMedia.exception.NotValidAgeException;
import com.socialMedia.exception.NotValidIdException;
import com.socialMedia.model.User;
import com.socialMedia.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserRequestValidator {

    private final UserRepository userRepository;

    public void validateUserRequest(final UserRequestDTO userRequestDto) throws MandatoryFieldsMissingException {
        if (userRequestDto == null) {
            log.error("Request was empty!");
            throw new MandatoryFieldsMissingException("Request was empty!");
        } else if (userRequestDto.getName() == null) {
            log.error("Mandatory name field is missing!");
            throw new MandatoryFieldsMissingException("Mandatory name field is missing!");
        } else if (userRequestDto.getName().isBlank()) {
            log.error("Mandatory name field is empty!");
            throw new MandatoryFieldsMissingException("Mandatory name field is empty!");
        } else if (userRequestDto.getAge() == null) {
            log.error("Mandatory age field is missing!");
            throw new MandatoryFieldsMissingException("Mandatory age field is missing!");
        }
    }

    public void validateUserAgeRequest(final Integer age) throws NotValidAgeException {
        if (age <= 0){
            log.error("User age can't be 0 or less!");
            throw new NotValidAgeException("User age can't be 0 or less!");
        }
    }

    public void validateUserList(final List<User> users) throws NoUsersFoundException {
        if (users.isEmpty()) {
            log.error("No users were found in the DB!");
            throw new NoUsersFoundException("No users were found!");
        }
    }

    public void validateUserById(final Long id) throws NoUsersFoundException {
        if (!userRepository.existsById(id)) {
            log.error("User with id number " + id + " not found!");
            throw new NoUsersFoundException("User with id number " + id + " not found!");
        }
    }

}
