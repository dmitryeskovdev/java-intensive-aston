package com.eskov.service;

import com.eskov.service.dao.UserDaoImpl;
import com.eskov.service.entity.User;
import com.eskov.service.service.UserService;
import com.eskov.service.service.UserServiceImpl;
import com.eskov.service.util.HibernateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private static final UserService userService = new UserServiceImpl(new UserDaoImpl());
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        boolean running = true;
        System.out.println("Приложение User Service");
        printMenu();

        try {
            while (running) {
                System.out.print("->: ");
                String choice = scanner.nextLine().trim();

                try {
                    switch (choice) {
                        case "1" -> createUser();
                        case "2" -> findById();
                        case "3" -> findByEmail();
                        case "4" -> findByNameAndAge();
                        case "5" -> showAllUsers();
                        case "6" -> updateUser();
                        case "7" -> deleteUser();
                        case "0" -> running = false;
                        case "?" -> printMenu();
                        case "" -> {}
                        default -> System.out.println("Недопустимая операция");
                    }
                } catch (RuntimeException e) {
                    System.out.println("Произошла ошибка: " + e.getMessage());
                }
            }

        } finally {
            HibernateUtil.shutdown();
            System.out.println("Работа завершена");
        }
    }

    private static void printMenu() {
        System.out.println("Список доступных действий:");
        System.out.println("1. Создать пользователя");
        System.out.println("2. Найти пользователя по ID");
        System.out.println("3. Найти пользователя по Email");
        System.out.println("4. Найти пользователей по имени и/или возрасту");
        System.out.println("5. Показать всех пользователей");
        System.out.println("6. Обновить данные пользователя по ID");
        System.out.println("7. Удалить пользователя по ID");
        System.out.println("0. Выход");
        System.out.println("?. Показать список доступных действий");
    }

    private static void createUser() {
        String name = readRequiredString("Введите имя: ");
        int age = readRequiredInt("Введите возраст: ");
        String email = readRequiredString("Введите email: ");
        userService.createUser(name, email, age);
    }

    private static void findById() {
        Long id = readRequiredLong("Введите ID: ");
        userService.getUserById(id).ifPresentOrElse(
                System.out::println,
                () -> System.out.println("Пользователь с ID " + id + " не найден")
        );
    }

    private static void findByEmail() {
        String email = readRequiredString("Введите email: ");
        userService.getUserByEmail(email).ifPresentOrElse(
                System.out::println,
                () -> System.out.println("Пользователь с email " + email + " не найден")
        );
    }

    private static void findByNameAndAge() {
        String name = readOptionalString("Введите имя (Enter - не включать в фильтрацию): ");
        Integer age = readOptionalInt("Введите возраст (Enter - не включать в фильтрацию): ");
        List<User> users = userService.findUsersByFilter(name, age);
        users.forEach(System.out::println);
    }

    private static void showAllUsers() {
        List<User> users = userService.getAllUsers();
        users.forEach(System.out::println);
    }

    private static void updateUser() {
        Long id = readRequiredLong("Введите ID: ");
        String newName = readOptionalString("Введите новое имя (Enter - без изменений): ");
        String newEmail = readOptionalString("Введите новый email (Enter - без изменений): ");
        Integer newAge = readOptionalInt("Введите новый возраст (Enter - без изменений): ");
        userService.updateUser(id, newName, newEmail, newAge);
    }

    private static void deleteUser() {
        Long id = readRequiredLong("Введите ID: ");
        userService.deleteUser(id);
    }

    private static String readRequiredString(String prompt) {
        System.out.print(prompt);
        String input = scanner.nextLine().trim();
        if (input.isEmpty()) {
            throw new IllegalArgumentException("Поле не может быть пустым");
        }
        return input;
    }

    private static String readOptionalString(String prompt) {
        System.out.print(prompt);
        String input = scanner.nextLine().trim();
        return input.isEmpty() ? null : input;
    }

    private static int readRequiredInt(String prompt) {
        String input = readRequiredString(prompt);
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Возраст должен быть целым числом");
        }
    }

    private static Integer readOptionalInt(String prompt) {
        String input = readOptionalString(prompt);
        if (input == null) {
            return null;
        }
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Возраст должен быть целым числом");
        }
    }

    private static Long readRequiredLong(String prompt) {
        String input = readRequiredString(prompt);
        try {
            return Long.parseLong(input);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("ID должен быть целым числом");
        }
    }
}
