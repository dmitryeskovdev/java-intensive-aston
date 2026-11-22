# Домашнее задание №2
## Реализация консольного приложения User Service

## Используется Hibernate для взаимодействия с PostgreSQL
## Поддерживаются базовые операции CRUD над сущностью User(id, name, email, age, created_at)
## Используется Maven для управления зависимостями
## Используется SLF4J + Logback для логирования 

### Результат работы программы:

### Добавление пользователей:
![Добавление пользователей](screenshots/screenshot_create.png)

### Поиск пользователей по фильтрам:
![Поиск пользователей по фильтрам](screenshots/screenshot_search.png)

### Обновление данных пользователей:
![Обновление данных пользователей](screenshots/screenshot_update.png)

### Удаление пользователей:
![Удаление пользователей](screenshots/screenshot_delete.png)

### Логи работы
![Логи работы](screenshots/screenshot_logs.png)


# Домашнее задание №3
## Создание юнит-тестов и интеграционных тестов для User Service

## Используется JUnit 5, Mockito и Testcontainers
## Тесты запускаются изолированно друг от друга

### Результат работы тестов:

### Запуск тестов в режиме Debug. Процесс IDEA
![Запуск тестов в режиме Debug](screenshots/SCREENSHOT_TEST_DEBUG_IDEA.png)

### Запуск тестов в режиме Debug. Процесс Docker Desktop
![Запуск тестов в режиме Debug](screenshots/SCREENSHOT_TEST_DEBUG_DOCKER_RUN.png)

### Запуск тестов. Интеграционные + Unit:
![Запуск тестов All](screenshots/SCREENSHOT_TEST_RUN_TEST_ALL.png)
