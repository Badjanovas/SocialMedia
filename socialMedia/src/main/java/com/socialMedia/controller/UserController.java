package com.socialMedia.controller;

import com.socialMedia.dto.UserRequestDTO;
import com.socialMedia.exception.MandatoryFieldsMissingException;
import com.socialMedia.exception.NoUsersFoundException;
import com.socialMedia.exception.NotValidAgeException;
import com.socialMedia.exception.NotValidIdException;
import com.socialMedia.service.UserService;
import com.socialMedia.validator.UserRequestValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserRequestValidator userRequestValidator;

    @GetMapping("/")
    public ResponseEntity<?> findAll() throws NoUsersFoundException {
            return ResponseEntity.status(HttpStatus.OK).body(userService.getAllUsers());
    }

    @PostMapping("/")
    public ResponseEntity<?> add(@RequestBody final UserRequestDTO userRequestDto) throws MandatoryFieldsMissingException, NotValidAgeException {
            return ResponseEntity.status(HttpStatus.OK).body(userService.addUser(userRequestDto));
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable final Long userId) throws NoUsersFoundException, NotValidIdException {
            return ResponseEntity.status(HttpStatus.OK).body(userService.deleteUser(userId));
    }
}
