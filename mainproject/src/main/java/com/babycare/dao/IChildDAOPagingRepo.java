package com.babycare.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.babycare.model.entity.ChildEntity;

public interface IChildDAOPagingRepo extends JpaRepository<ChildEntity, Long> {

}
