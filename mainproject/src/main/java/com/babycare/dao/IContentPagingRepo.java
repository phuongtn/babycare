package com.babycare.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.babycare.model.entity.ContentEntity;

public interface IContentPagingRepo extends JpaRepository<ContentEntity, String> {

}
