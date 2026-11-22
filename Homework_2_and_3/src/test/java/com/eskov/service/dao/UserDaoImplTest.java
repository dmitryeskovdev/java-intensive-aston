package com.eskov.service.dao;

import com.eskov.service.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
class UserDaoImplTest {

    @Container
    private static final PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:16-alpine")
            .withDatabaseName("test_db")
            .withUsername("test_user")
            .withPassword("test_pass");

    private static SessionFactory testSessionFactory;
    private UserDao userDao;

    @BeforeAll
    static void beforeAll() {
        Configuration configuration = new Configuration()
                .addAnnotatedClass(User.class)
                .setProperty("hibernate.connection.driver_class", "org.postgresql.Driver")
                .setProperty("hibernate.connection.url", postgres.getJdbcUrl())
                .setProperty("hibernate.connection.username", postgres.getUsername())
                .setProperty("hibernate.connection.password", postgres.getPassword())
                .setProperty("hibernate.hbm2ddl.auto", "create-drop")
                .setProperty("hibernate.show_sql", "true");

        testSessionFactory = configuration.buildSessionFactory();
    }

    @AfterAll
    static void afterAll() {
        if (testSessionFactory != null) {
            testSessionFactory.close();
        }
    }

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl(testSessionFactory);
        try (Session session = testSessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.createNativeMutationQuery("TRUNCATE TABLE users RESTART IDENTITY CASCADE").executeUpdate();
            tx.commit();
        }
    }

    @Test
    void saveAndFindById_Success() {
        User user = new User("Ivan", "ivan@gmail.com", 27);
        userDao.save(user);
        assertNotNull(user.getId());
        assertNotNull(user.getCreatedAt());

        Optional<User> found = userDao.findById(user.getId());
        assertTrue(found.isPresent());
        assertEquals("Ivan", found.get().getName());
        assertEquals("ivan@gmail.com", found.get().getEmail());
        assertEquals(27, found.get().getAge());
    }

    @Test
    void save_DuplicateEmail_ThrowsException() {
        userDao.save(new User("dmitry", "dmitry@mail.com", 20));
        User duplicate = new User("alex", "dmitry@mail.com", 30);
        assertThrows(RuntimeException.class, () -> userDao.save(duplicate));
    }

    @Test
    void findById_NotFound_ReturnsEmpty() {
        Optional<User> found = userDao.findById(10000L);
        assertTrue(found.isEmpty());
    }

    @Test
    void findByEmail_SuccessAndNotFound() {
        userDao.save(new User("Anna", "anna@yandex.ru", 28));
        Optional<User> found = userDao.findByEmail("anna@yandex.ru");
        assertTrue(found.isPresent());
        assertEquals("Anna", found.get().getName());
        assertTrue(userDao.findByEmail("anna998877@yandex.ru").isEmpty());
    }

    @Test
    void findAll_ReturnsOrderedList() {
        userDao.save(new User("User1", "user1@example.com", 20));
        userDao.save(new User("User2", "user2@example.com", 30));
        List<User> users = userDao.findAll();
        assertEquals(2, users.size());
        assertEquals("User1", users.get(0).getName());
        assertEquals("User2", users.get(1).getName());
    }

    @Test
    void findByNameAndAge_FiltersCorrectly() {
        userDao.save(new User("Alex", "alex1@example.com", 25));
        userDao.save(new User("Alex", "alex2@example.com", 35));
        userDao.save(new User("Maria", "maria@example.com", 25));

        List<User> byName = userDao.findByNameAndAge("Alex", null);
        assertEquals(2, byName.size());

        List<User> byAge = userDao.findByNameAndAge(null, 25);
        assertEquals(2, byAge.size());

        List<User> byBoth = userDao.findByNameAndAge("Alex", 35);
        assertEquals(1, byBoth.size());
        assertEquals("alex2@example.com", byBoth.get(0).getEmail());
    }

    @Test
    void update_UpdatesExistingUser() {
        User user = new User("dmitry", "dmitryx@gmail.com", 30);
        userDao.save(user);

        user.setName("dmitriy");
        user.setEmail("dmitryx30@gmail.com");
        user.setAge(31);
        userDao.update(user);

        User updated = userDao.findById(user.getId()).orElseThrow();
        assertEquals("dmitriy", updated.getName());
        assertEquals("dmitryx30@gmail.com", updated.getEmail());
        assertEquals(31, updated.getAge());
    }

    @Test
    void delete_RemovesUser() {
        User user = new User("deleteduser", "deleteduser@ya.com", 19);
        userDao.save(user);
        Long id = user.getId();
        userDao.delete(id);
        assertTrue(userDao.findById(id).isEmpty());
    }
}
