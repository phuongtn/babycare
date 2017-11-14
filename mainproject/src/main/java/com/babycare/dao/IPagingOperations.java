package com.babycare.dao;

import java.io.Serializable;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IPagingOperations<T extends Serializable> extends IOperations<T> {
	Page<T> findPaginated(Pageable pageable);
	Page<T> findPaginated(Integer page, Integer size);
	Page<T> findExamplePaginated(Example<T> example, Pageable pageable);
	Page<T> findExamplePaginated(Example<T> example, Integer page, Integer size);
}
