/*package com.babycare.paging;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public abstract class AbstractPagingService<E extends Serializable, K extends Object>
		implements IPagingOperations<E, K> {
	
	protected abstract IPagingOperations<E, K> getDao();

	@Override
	public E findOne(K key) {
		return getDao().findOne(key);
	}

	@Override
	public List<E> listAll() {
		return getDao().listAll();
	}

	@Override
	public Page<E> findPaginated(int page, int size) {
		return getDao().findPaginated(page, size);
	}

	@Override
	public E createEntity(E entity) {
		return getDao().createEntity(entity);
	}

	@Override
	public E updateEntity(E entity) {
		return getDao().updateEntity(entity);
	}

	@Override
	public void deleteEntity(E entity) {
		getDao().deleteEntity(entity);
	}

	@Override
	public void deleteEntityByKey(K key) {
		getDao().deleteEntityByKey(key);
	}

}
*/