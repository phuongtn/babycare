package com.babycare.model.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.babycare.model.payload.Child;

@Entity
@Table(name = "children", catalog = "babycare")
public class ChildEntity extends Child implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1416878046123153824L;

	public ChildEntity() {}
	
	public ChildEntity(Child child) {
		this.childId = child.getChildId();
		this.name = child.getName();
		this.userId = child.getUserId();
		this.babyType = child.getBabyType();
		this.blood = child.getBlood();
		this.dob = child.getDob();
		this.gender = child.getGender();
		this.region = child.getRegion();
	}

	@Override
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="childid")
	public Long getChildId() {
		return childId;
	}

	@Override
	@Column(name="userid")
	public Long getUserId() {
		return userId;
	}

	@Override
	@Column(name="name")
	public String getName() {
		return name;
	}

	@Override
	@Column(name="dob")
	public Long getDob() {
		return dob;
	}

	@Override
	@Column(name="babytype")
	public Integer getBabyType() {
		return babyType;
	}

	@Override
	@Column(name="blood")
	public Integer getBlood() {
		return blood;
	}

	@Override
	@Column(name="gender")
	public Integer getGender() {
		return gender;
	}

	@Override
	@Column(name="region")
	public Integer getRegion() {
		return region;
	}


	@Override
	public boolean equals(Object rhs) {
		return EqualsBuilder.reflectionEquals(this, rhs, false);
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this, false);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
