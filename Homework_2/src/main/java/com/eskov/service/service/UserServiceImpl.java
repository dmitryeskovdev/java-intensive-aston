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

    private static final Pattern NAME_PATTERN = Pattern.compile("^[a-zA-Zа-яА-ЯёЁ]{1,64}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9]{1,128}@[a-zA-Z]{1,32}\\.[a-zA-Z]{1,8}$");

    private final UserDao userDao;

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public void createUser(String name, String email, int age) {
        validateName(name);
        validateEmail(email);
        validateAge(age);

        if (userDao.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Пользователь с email " + email + " уже существует");
        }

        User user = new User(name, email, age);
        userDao.save(user);
    }

    @Override
    public Optional<User> getUserById(Long id) {
        validateId(id);
        return userDao.findById(id);
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        validateEmail(email);
        return userDao.findByEmail(email);
    }

    @Override
    public List<User> getAllUsers() {
        return userDao.findAll();
    }

    @Override
    public List<User> findUsersByFilter(String name, Integer age) {
        if (name == null && age == null) {
            throw new IllegalArgumentException("Укажите хотя бы один параметр для поиска (имя или возраст)");
        }
        if (name != null) {
            validateName(name);
        }
        if (age != null) {
            validateAge(age);
        }

        return userDao.findByNameAndAge(name, age);
    }

    @Override
    public void updateUser(Long id, String newName, String newEmail, Integer newAge) {
        validateId(id);

        if (newName == null && newEmail == null && newAge == null) {
            throw new IllegalArgumentException("Не передано ни одного нового значения для обновления");
        }

        User user = userDao.findById(id)
                .orElseThrow(() -> {
                    return new IllegalArgumentException("Пользователь с ID " + id + " не найден");
                });

        if (newName != null) {
            validateName(newName);
            user.setName(newName);
        }

        if (newEmail != null) {
            validateEmail(newEmail);
            Optional<User> existingWithEmail = userDao.findByEmail(newEmail);
            if (existingWithEmail.isPresent() && !existingWithEmail.get().getId().equals(id)) {
                throw new IllegalArgumentException("Email " + newEmail + " уже используется другим пользователем");
            }
            user.setEmail(newEmail);
        }

        if (newAge != null) {
            validateAge(newAge);
            user.setAge(newAge);
        }
        userDao.update(user);
    }

    @Override
    public void deleteUser(Long id) {
        validateId(id);

        if (userDao.findById(id).isEmpty()) {
            throw new IllegalArgumentException("Пользователь с ID " + id + " не найден");
        }

        userDao.delete(id);
    }

    private void validateId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID пользователя должен быть положительным числом");
        }
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Имя пользователя не может быть пустым");
        }
        if (!NAME_PATTERN.matcher(name).matches()) {
            throw new IllegalArgumentException("Имя должно состоять из одного слова (от 1 до 64 русских или английских букв)");
        }
    }

    private void validateEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email не может быть пустым");
        }
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new IllegalArgumentException("Email не соответствует формату (только английские буквы и цифры): example3@link.dev");
        }
    }

    private void validateAge(int age) {
        if (age < 18 || age > 80) {
            throw new IllegalArgumentException("Возраст должен быть от 18 до 80 лет");
        }
    }
}
