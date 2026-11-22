package com.eskov.service.dao;

import com.eskov.service.entity.User;
import java.util.List;
import java.util.Optional;

public interface UserDao {
    void save(User user);
    Optional<User> findById(Long id);
    Optional<User> findByEmail(String email);
    List<User> findAll();
    List<User> findByNameAndAge(String name, Integer age);
    void update(User user);
    void delete(Long id);
}
