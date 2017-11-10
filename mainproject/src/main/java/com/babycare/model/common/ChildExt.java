package com.babycare.model.common;

import com.babycare.model.entity.ChildEntity;
import com.babycare.model.payload.Child;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@JsonIgnoreProperties({"requestBySessionId"})
public class ChildExt extends Child {
	public ChildExt toChild(ChildEntity entity) {
		return (ChildExt) new ChildExt().
				setBabyType(entity.getBabyType()).
				setBlood(entity.getBlood()).
				setChildId(entity.getChildId()).
				setGender(entity.getGender()).
				setDob(entity.getDob()).
				setName(entity.getName()).
				setRegion(entity.getGender()).
				setUserId(entity.getUserId());
	}
}
