package com.babycare.service;

import java.io.Serializable;
import java.util.List;

import com.babycare.dao.IOperations;


public abstract class AbstractService<T extends Serializable> implements IOperations<T> {

    @Override
    public T findOne(final Object key) {
        return getDao().findOne(key);
    }

    @Override
    public List<T> findAll() {
        return getDao().findAll();
    }

    @Override
    public void create(final T entity) {
        getDao().create(entity);
    }

    @Override
    public T update(final T entity) {
        return getDao().update(entity);
    }

    @Override
    public void delete(final T entity) {
        getDao().delete(entity);
    }

    @Override
    public void deleteById(final Object id) {
        getDao().deleteById(id);
    }

    @Override
    public T updateEntity(final T entity) {
       return getDao().updateEntity(entity);
    }
    
    protected abstract IOperations<T> getDao();

}
