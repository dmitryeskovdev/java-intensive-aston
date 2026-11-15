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

    }

    private static void findById() {

    }

    private static void findByEmail() {

    }

    private static void findByNameAndAge() {

    }

    private static void showAllUsers() {

    }

    private static void updateUser() {

    }

    private static void deleteUser() {

    }

}
