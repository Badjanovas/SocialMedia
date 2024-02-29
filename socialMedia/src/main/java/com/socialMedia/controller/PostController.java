package com.socialMedia.controller;

import com.socialMedia.dto.PostRequestDTO;
import com.socialMedia.exception.MandatoryFieldsMissingException;
import com.socialMedia.exception.NoPostFoundException;
import com.socialMedia.exception.NoUsersFoundException;
import com.socialMedia.exception.NotValidIdException;
import com.socialMedia.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/post")
@Slf4j
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/")
    public ResponseEntity<?> addPostToUser(@RequestBody final PostRequestDTO postRequestDTO) throws MandatoryFieldsMissingException, NotValidIdException, NoUsersFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(postService.addPost(postRequestDTO));
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable final Long postId) throws NotValidIdException, NoUsersFoundException, NoPostFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(postService.deletePost(postId));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getPostsByUserId(@PathVariable final Long userId) throws NotValidIdException, NoUsersFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(postService.getAllPostByUserId(userId));
    }

    @PatchMapping("/addLike/{postId}")
    public ResponseEntity<?> addLike(@PathVariable("postId") final Long postId) throws NotValidIdException, NoPostFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(postService.addLikeToPost(postId));
    }

    @GetMapping("/mostLikedPost")
    public ResponseEntity<?> mostLikedPost() throws NoPostFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(postService.getMostLikedPost());
    }
}
