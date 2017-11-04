package com.babycare.dao;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public abstract class AbstractJpaDao<T extends Serializable> extends AbstractDao<T> {

	@PersistenceContext
	protected EntityManager em;

	// API

	@Override
	public T findOne(final Object id) {
		return em.find(clazz, id);
	}

	@Override
	public List<T> findAll() {
		final CriteriaBuilder cb = em.getCriteriaBuilder();
		final CriteriaQuery<T> cq = cb.createQuery(clazz);
		final Root<T> rootEntry = cq.from(clazz);
		final CriteriaQuery<T> all = cq.select(rootEntry);
		final TypedQuery<T> allQuery = em.createQuery(all);
		return allQuery.getResultList();
	}

	@Override
	public void create(final T entity) {
		em.persist(entity);
	}

	@Override
	public T update(final T entity) {
		em.merge(entity);
		return entity;
	}

	@Override
	public void delete(final T entity) {
		em.remove(entity);
	}

	@Override
	public void deleteById(final Object entityId) {
		delete(findOne(entityId));
	}

	@Override
	public T createEntity(T entity) {
		em.persist(entity);
		em.flush();
		return entity;
	}

	@Override
	public T updateEntity(final T entity) {
		em.merge(entity);
		em.flush();
		return entity;
	}
}
