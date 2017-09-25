package com.babycare.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface IOperations<T extends Serializable> {

	T findOne(final Long id);

    List<T> findAll();

    void create(final T entity);

    T createEntity(final T entity);
    
    T update(final T entity);

    T updateEntity(final T entity);
    
    void delete(T entity);

    void deleteById(final long entityId);

}
