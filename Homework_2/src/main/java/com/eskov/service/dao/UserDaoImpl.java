package com.eskov.service.dao;

import com.eskov.service.entity.User;
import com.eskov.service.util.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class UserDaoImpl implements UserDao {
    private static final Logger logger = LoggerFactory.getLogger(UserDaoImpl.class);

    @Override
    public void save(User user) {

    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return Optional.empty();
    }

    @Override
    public List<User> findAll() {
        return List.of();
    }

    @Override
    public List<User> findByNameAndAge(String name, Integer age) {
        return List.of();
    }

    @Override
    public void update(User user) {

    }

    @Override
    public void delete(Long id) {

    }
}
