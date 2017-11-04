package com.babycare.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.babycare.model.entity.PushMessageEntity;

public interface IPushMessagePagingRepo extends JpaRepository<PushMessageEntity, String> {

}
