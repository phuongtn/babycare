/*package com.babycare.paging;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.domain.Page;

public interface IPagingOperations<E extends Serializable, K extends Object> {

	E findOne(final K key);

	List<E> listAll();

	Page<E> findPaginated(int page, int size);

	//E create(final E entity);

	//E createEntity(final E entity);

	//E update(final E entity);

	E createEntity(final E entity);

	E updateEntity(final E entity);
	
	void deleteEntity(final E entity);

	void deleteEntityByKey(final K key);

}
*/