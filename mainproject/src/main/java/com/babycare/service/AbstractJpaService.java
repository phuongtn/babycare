package com.babycare.service;

import java.io.Serializable;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;


@Transactional
public abstract class AbstractJpaService<T extends Serializable> extends AbstractService<T> {

    @Override
    public T findOne(final Object id) {
        return super.findOne(id);
    }

    @Override
    public List<T> findAll() {
        return super.findAll();
    }

    @Override
    public void create(final T entity) {
        super.create(entity);
    }

    @Override
    public T update(final T entity) {
        return super.update(entity);
    }

    @Override
    public void delete(final T entity) {
        super.delete(entity);
    }

    @Override
    public void deleteById(final Object entityId) {
        super.deleteById(entityId);
    }

}
