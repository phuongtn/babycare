package com.babycare.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.babycare.model.entity.UserEntity;

public interface IUserDAOPagingRepo extends JpaRepository<UserEntity, Long>{

}
