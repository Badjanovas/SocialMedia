package com.socialMedia.util;

import com.socialMedia.model.Comment;
import com.socialMedia.model.Post;
import com.socialMedia.model.User;
import com.socialMedia.repository.CommentRepository;
import com.socialMedia.repository.PostRepository;
import com.socialMedia.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class testDataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    @Override
    public void run(String... args) throws Exception {
        log.info("Create test data.");

        User user1 = User.builder()
                .name("John Doe")
                .age(30)
                .build();

        User user2 = User.builder()
                .name("Jane Smith")
                .age(25)
                .build();

        User user3 = User.builder()
                .name("Alex Johnson")
                .age(28)
                .build();


        Post post1 = Post.builder()
                .likeCount(100)
                .message("My first post")
                .user(user1)
                .createdAt(LocalDateTime.now())
                .build();

        Post post2 = Post.builder()
                .likeCount(50)
                .message("Another day, another post")
                .user(user2)
                .createdAt(LocalDateTime.now())
                .build();

        Post post3 = Post.builder()
                .likeCount(10)
                .message("One more extra post.")
                .user(user3)
                .createdAt(LocalDateTime.now())
                .build();

        Post post4 = Post.builder()
                .likeCount(75)
                .message("Exploring new ideas")
                .user(user1)
                .createdAt(LocalDateTime.now())
                .build();

        Post post5 = Post.builder()
                .likeCount(120)
                .message("Thoughts on technology")
                .user(user2)
                .createdAt(LocalDateTime.now())
                .build();
        Post post6 = Post.builder()
                .likeCount(15)
                .message("Looking for book recommendation")
                .user(user2)
                .createdAt(LocalDateTime.now())
                .build();

        user1.setPosts(Arrays.asList(post1, post4));
        user2.setPosts(Arrays.asList(post2, post5, post6));
        user3.setPosts(Arrays.asList(post3));


        Comment comment1 = Comment.builder()
                .likeCount(10)
                .message("Great post!")
                .user(user1)
                .post(post2)
                .createdAt(LocalDateTime.now())
                .build();

        Comment comment2 = Comment.builder()
                .likeCount(5)
                .message("Really enjoyed this.")
                .user(user2)
                .post(post1)
                .createdAt(LocalDateTime.now())
                .build();

        Comment comment3 = Comment.builder()
                .likeCount(3)
                .message("Interesting perspective.")
                .user(user3)
                .post(post3)
                .createdAt(LocalDateTime.now())
                .build();

        Comment comment4 = Comment.builder()
                .likeCount(8)
                .message("Very insightful!")
                .user(user1)
                .post(post3)
                .createdAt(LocalDateTime.now())
                .build();

        Comment comment5 = Comment.builder()
                .likeCount(12)
                .message("Can't wait to see more.")
                .user(user1)
                .post(post2)
                .createdAt(LocalDateTime.now())
                .build();

        user1.setComments(Arrays.asList(comment1, comment4, comment5));
        user2.setComments(Arrays.asList(comment2));
        user3.setComments(Arrays.asList(comment3));

        post1.setComments(Arrays.asList(comment2));
        post2.setComments(Arrays.asList(comment1, comment5));
        post3.setComments(Arrays.asList(comment3, comment4));

        userRepository.saveAll(Arrays.asList(user1, user2, user3));
        postRepository.saveAll(Arrays.asList(post1, post2, post3, post4, post5, post6));
        commentRepository.saveAll(Arrays.asList(comment1, comment2, comment3, comment4, comment5));

        log.info("Data created successfully!");

    }
}
