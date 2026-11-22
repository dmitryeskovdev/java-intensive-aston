package com.eskov.service.service;

import com.eskov.service.entity.User;
import java.util.List;
import java.util.Optional;

public interface UserService {
    void createUser(String name, String email, int age);
    Optional<User> getUserById(Long id);
    Optional<User> getUserByEmail(String email);
    List<User> getAllUsers();
    List<User> findUsersByFilter(String name, Integer age);
    void updateUser(Long id, String newName, String newEmail, Integer newAge);
    void deleteUser(Long id);
}
