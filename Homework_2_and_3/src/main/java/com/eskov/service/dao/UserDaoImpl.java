package com.eskov.service.dao;

import com.eskov.service.entity.User;
import com.eskov.service.util.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class UserDaoImpl implements UserDao {
    private static final Logger logger = LoggerFactory.getLogger(UserDaoImpl.class);
    private final SessionFactory sessionFactory;

    public UserDaoImpl() {
        this(HibernateUtil.getSessionFactory());
    }

    public UserDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void save(User user) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.persist(user);
            transaction.commit();
            logger.info("CREATE: Пользователь сохранен: {}", user);
        } catch (HibernateException e) {
            rollbackTransaction(transaction);
            throw new RuntimeException("Ошибка базы данных при создании пользователя", e);
        }
    }

    @Override
    public Optional<User> findById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            User user = session.find(User.class, id);
            return Optional.ofNullable(user);
        } catch (HibernateException e) {
            throw new RuntimeException("Ошибка базы данных при поиске пользователя по ID", e);
        }
    }

    @Override
    public Optional<User> findByEmail(String email) {
        try (Session session = sessionFactory.openSession()) {
            Query<User> query = session.createQuery("FROM User WHERE email = :email", User.class);
            query.setParameter("email", email);
            Optional<User> result = query.uniqueResultOptional();
            return result;
        } catch (HibernateException e) {
            throw new RuntimeException("Ошибка базы данных при поиске пользователя по email", e);
        }
    }

    @Override
    public List<User> findAll() {
        try (Session session = sessionFactory.openSession()) {
            List<User> users = session.createQuery("FROM User ORDER BY id", User.class).list();
            return users;
        } catch (HibernateException e) {
            throw new RuntimeException("Ошибка базы данных при получении списка пользователей", e);
        }
    }

    @Override
    public List<User> findByNameAndAge(String name, Integer age) {
        try (Session session = sessionFactory.openSession()) {
            StringBuilder hql = new StringBuilder("FROM User WHERE 1=1");
            if (name != null) {
                hql.append(" AND name = :name");
            }
            if (age != null) {
                hql.append(" AND age = :age");
            }
            hql.append(" ORDER BY id");

            Query<User> query = session.createQuery(hql.toString(), User.class);
            if (name != null) {
                query.setParameter("name", name);
            }
            if (age != null) {
                query.setParameter("age", age);
            }

            List<User> users = query.list();
            return users;
        } catch (HibernateException e) {
            throw new RuntimeException("Ошибка базы данных при поиске пользователей по фильтру", e);
        }
    }

    @Override
    public void update(User user) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.merge(user);
            transaction.commit();
            logger.info("UPDATE: Данные пользователя обновлены: {}", user);
        } catch (HibernateException e) {
            rollbackTransaction(transaction);
            throw new RuntimeException("Ошибка базы данных при обновлении пользователя", e);
        }
    }

    @Override
    public void delete(Long id) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            User user = session.find(User.class, id);
            if (user != null) {
                session.remove(user);
            }
            transaction.commit();
            logger.info("DELETE: Пользователь с ID={} удален", id);
        } catch (HibernateException e) {
            rollbackTransaction(transaction);
            throw new RuntimeException("Ошибка базы данных при удалении пользователя", e);
        }
    }

    private void rollbackTransaction(Transaction transaction) {
        if (transaction != null && transaction.isActive()) {
            transaction.rollback();
        }
    }
}
