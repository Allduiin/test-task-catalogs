package test.task.catalogs.repositories.impl;

import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;
import test.task.catalogs.exceptions.DataProcessingException;
import test.task.catalogs.model.Catalog;
import test.task.catalogs.repositories.CatalogRepository;

@AllArgsConstructor
@Repository
public class CatalogRepositoryImpl implements CatalogRepository {
    private final SessionFactory sessionFactory;

    @Override
    public Catalog add(Catalog catalog) {
        Transaction transaction = null;
        Session session = sessionFactory.openSession();
        try {
            transaction = session.beginTransaction();
            session.save(catalog);
            transaction.commit();
            return catalog;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Error adding catalog", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public Catalog update(Catalog catalog) {
        Transaction transaction = null;
        Session session = sessionFactory.openSession();
        try {
            transaction = session.beginTransaction();
            session.update(catalog);
            transaction.commit();
            return catalog;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Error updating catalog", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public Catalog getById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Catalog.class, id);
        } catch (Exception e) {
            throw new DataProcessingException("Can't find catalog by id", e);
        }
    }

    @Override
    public Catalog delete(Catalog catalog) {
        Transaction transaction = null;
        Session session = sessionFactory.openSession();
        try {
            transaction = session.beginTransaction();
            session.delete(catalog);
            transaction.commit();
            return catalog;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Error deleting catalog", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
