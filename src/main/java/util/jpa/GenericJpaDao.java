package util.jpa;

import com.google.inject.persist.Transactional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;


public abstract class GenericJpaDao<T> {

    protected Class<T> entityClass;
    protected EntityManager entityManager;

    public GenericJpaDao(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    @Inject
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    public void persist(T entity) {
        entityManager.getTransaction().begin();
        entityManager.persist(entity);
        entityManager.getTransaction().commit();
    }

    @Transactional
    public Optional<T> find(Object primaryKey) {
        return Optional.ofNullable(entityManager.find(entityClass, primaryKey));
    }

    @Transactional
    public List<T> findAll() {
        TypedQuery<T> typedQuery = entityManager.createQuery("FROM " + entityClass.getSimpleName(), entityClass);
        return typedQuery.getResultList();
    }

    @Transactional
    public void remove(T entity) {
        entityManager.remove(entity);
    }

    @Transactional
    public void update(T entity) {
        entityManager.merge(entity);
    }

}
