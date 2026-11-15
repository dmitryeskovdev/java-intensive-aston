package com.eskov.service.service;

import com.eskov.service.dao.UserDao;
import com.eskov.service.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

public class UserServiceImpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private static final Pattern NAME_PATTERN = Pattern.compile("");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("");

    private final UserDao userDao;

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public void createUser(String name, String email, int age) {
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return Optional.empty();
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return Optional.empty();
    }

    @Override
    public List<User> getAllUsers() {
        return List.of();
    }

    @Override
    public List<User> findUsersByFilter(String name, Integer age) {
        return List.of();
    }

    @Override
    public void updateUser(Long id, String newName, String newEmail, Integer newAge) {

    }

    @Override
    public void deleteUser(Long id) {

    }

    private void validateName(String name) {

    }

    private void validateEmail(String email) {

    }

    private void validateAge(int age) {

    }
}
