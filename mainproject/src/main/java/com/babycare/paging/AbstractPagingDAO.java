/*package com.babycare.paging;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import com.google.common.base.Preconditions;

public abstract class AbstractPagingDAO<E extends Serializable, K extends Object> implements JpaRepository<E, K>, IPagingOperations<E, K> {
	@PersistenceContext
	protected EntityManager em;

	protected Class<E> entityClass;
	protected Class<K> keyClass;

	protected final void setEntityClass(final Class<E> entityClass) {
		this.entityClass = Preconditions.checkNotNull(entityClass);
	}

	@Override
	public E findOne(K key) {
		return em.find(entityClass, key);
	}

	@Override
	public List<E> listAll() {
		final CriteriaBuilder cb = em.getCriteriaBuilder();
		final CriteriaQuery<E> cq = cb.createQuery(entityClass);
		final Root<E> rootEntry = cq.from(entityClass);
		final CriteriaQuery<E> all = cq.select(rootEntry);
		final TypedQuery<E> allQuery = em.createQuery(all);
		return allQuery.getResultList();
	}

	@Override
	public Page<E> findPaginated(int page, int size) {
		return findAll(PageRequest.of(page, size));
	}

	@Override
	public E createEntity(final E entity) {
		em.persist(entity);
		em.flush();
		return entity;
	}

	@Override
	public E updateEntity(E entity) {
		em.merge(entity);
		em.flush();
		return entity;
	}

	@Override
	public void deleteEntity(E entity) {
		em.remove(entity);
	}

	@Override
	public void deleteEntityByKey(K key) {
		delete(findOne(key));
	}
}
*/