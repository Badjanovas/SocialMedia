package com.socialMedia.repository;

import com.socialMedia.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "SELECT * FROM user_name u WHERE u.age >= 18", nativeQuery = true)
    List<User> findUsersOlderThan18();

}
