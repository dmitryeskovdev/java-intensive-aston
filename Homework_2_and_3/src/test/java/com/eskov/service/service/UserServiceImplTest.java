package com.eskov.service.service;

import com.eskov.service.dao.UserDao;
import com.eskov.service.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserDao userDao;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void createUser_Success() {
        when(userDao.findByEmail("dmitry66@gmail.com")).thenReturn(Optional.empty());
        userService.createUser("Dmitry", "dmitry66@gmail.com", 30);
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userDao).save(userCaptor.capture());
        User saved = userCaptor.getValue();
        assertEquals("Dmitry", saved.getName());
        assertEquals("dmitry66@gmail.com", saved.getEmail());
        assertEquals(30, saved.getAge());
    }

    @Test
    void createUser_DuplicateEmail_ThrowsException() {
        when(userDao.findByEmail("alex99@mail.ru")).thenReturn(Optional.of(new User()));

        assertThrows(IllegalArgumentException.class,
                () -> userService.createUser("Alex", "alex99@mail.ru", 27));

        verify(userDao, never()).save(any());
    }

    @Test
    void createUser_InvalidInputs_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> userService.createUser("", "a@b.com", 25));
        assertThrows(IllegalArgumentException.class, () -> userService.createUser("Alex 12345", "a@b.com", 25));
        assertThrows(IllegalArgumentException.class, () -> userService.createUser("Ivan", "bad_email@mail.com", 25));
        assertThrows(IllegalArgumentException.class, () -> userService.createUser("Dmitry", "Dmitry", 25));
        assertThrows(IllegalArgumentException.class, () -> userService.createUser("Ivan", "badage@bad.com", 17));
        assertThrows(IllegalArgumentException.class, () -> userService.createUser("Bob", "badage@bad.com", 81));
        verifyNoInteractions(userDao);
    }

    @Test
    void getUserById_Success() {
        User user = new User("Alex", "alex@test.com", 30);
        user.setId(1L);
        when(userDao.findById(1L)).thenReturn(Optional.of(user));
        Optional<User> result = userService.getUserById(1L);
        assertTrue(result.isPresent());
        assertEquals("alex@test.com", result.get().getEmail());
    }

    @Test
    void getUserById_InvalidId_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> userService.getUserById(null));
        assertThrows(IllegalArgumentException.class, () -> userService.getUserById(0L));
        assertThrows(IllegalArgumentException.class, () -> userService.getUserById(-5L));
        verifyNoInteractions(userDao);
    }

    @Test
    void getUserByEmail_Success() {
        User user = new User("Olga", "olga@test.com", 24);
        when(userDao.findByEmail("olga@test.com")).thenReturn(Optional.of(user));
        Optional<User> result = userService.getUserByEmail("olga@test.com");
        assertTrue(result.isPresent());
        assertEquals("Olga", result.get().getName());
    }

    @Test
    void getAllUsers_DelegatesToDao() {
        List<User> list = List.of(
                new User("Alex", "alex@net.com", 20),
                new User("Maria", "maria@net.com", 25),
                new User("Ivan", "ivan@net.com", 32)
        );

        when(userDao.findAll()).thenReturn(list);
        List<User> result = userService.getAllUsers();
        assertEquals(3, result.size());
        assertEquals(list, result);
        verify(userDao).findAll();
    }

    @Test
    void findUsersByFilter_Success() {
        List<User> expected = List.of(new User("Max", "max@test.com", 28));
        when(userDao.findByNameAndAge("Max", 28)).thenReturn(expected);
        List<User> actual = userService.findUsersByFilter("Max", 28);
        assertEquals(expected, actual);
    }

    @Test
    void findUsersByFilter_BothNull_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> userService.findUsersByFilter(null, null));
        verifyNoInteractions(userDao);
    }

    @Test
    void updateUser_Success() {
        User existing = new User("alex", "alex99@test.com", 27);
        existing.setId(1L);
        when(userDao.findById(1L)).thenReturn(Optional.of(existing));
        when(userDao.findByEmail("newalex99@test.com")).thenReturn(Optional.empty());
        userService.updateUser(1L, "NewAlex", "newalex99@test.com", 28);
        assertEquals("NewAlex", existing.getName());
        assertEquals("newalex99@test.com", existing.getEmail());
        assertEquals(28, existing.getAge());
        verify(userDao).update(existing);
    }

    @Test
    void updateUser_EmailTakenByAnotherUser_ThrowsException() {
        User targetUser = new User("Alex", "alex99@test.com", 27);
        targetUser.setId(1L);
        User otherUser = new User("Dmitry", "dmitry@test.com", 30);
        otherUser.setId(2L);
        when(userDao.findById(1L)).thenReturn(Optional.of(targetUser));
        when(userDao.findByEmail("dmitry@test.com")).thenReturn(Optional.of(otherUser));

        assertThrows(IllegalArgumentException.class,
                () -> userService.updateUser(1L, null, "dmitry@test.com", null));

        verify(userDao, never()).update(any());
    }

    @Test
    void updateUser_UserNotFound_ThrowsException() {
        when(userDao.findById(99L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> userService.updateUser(99L, "Petr", null, null));

        verify(userDao, never()).update(any());
    }

    @Test
    void updateUser_NoFieldsProvided_ThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> userService.updateUser(1L, null, null, null));

        verifyNoInteractions(userDao);
    }

    @Test
    void deleteUser_Success() {
        User existing = new User("DeletedDmitry", "deldmitry@test.com", 22);
        existing.setId(1L);
        when(userDao.findById(1L)).thenReturn(Optional.of(existing));
        userService.deleteUser(1L);
        verify(userDao).delete(1L);
    }

    @Test
    void deleteUser_NotFound_ThrowsException() {
        when(userDao.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> userService.deleteUser(1L));

        verify(userDao, never()).delete(any());
    }
}
