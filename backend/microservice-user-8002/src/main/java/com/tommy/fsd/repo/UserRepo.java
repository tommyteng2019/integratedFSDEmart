package com.tommy.fsd.repo;

import com.tommy.fsd.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface UserRepo extends JpaRepository<User, String> {
    User findByEmail(String email);
    Collection<User> findAllByRole(String role);
    User findByName(String name);
}
