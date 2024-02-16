package com.socialMedia.controller;

import com.socialMedia.dto.CommentRequestDTO;
import com.socialMedia.exception.MandatoryFieldsMissingException;
import com.socialMedia.exception.NoUsersFoundException;
import com.socialMedia.exception.NotValidIdException;
import com.socialMedia.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comment")
@Slf4j
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/")
    public ResponseEntity<?> addComment(@RequestBody final CommentRequestDTO commentRequestDTO)
            throws NotValidIdException, MandatoryFieldsMissingException, NoUsersFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(commentService.addComment(commentRequestDTO));
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity<?> addLike(@PathVariable("commentId") final Long commentId) throws NotValidIdException, NoUsersFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(commentService.likeComment(commentId));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable final Long commentId) throws NotValidIdException, NoUsersFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(commentService.deleteComment(commentId));
    }
}
