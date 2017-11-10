package com.babycare.model.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonIgnore;

import com.babycare.model.payload.Child;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name = "children", catalog = "babycare")
@JsonIdentityInfo(
generator = ObjectIdGenerators.PropertyGenerator.class, 
property = "childId")
@JsonIgnoreProperties({"userId","requestBySessionId"})
public class ChildEntity extends Child implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private UserEntity user;

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
	@Column(name="userid", insertable = false, updatable = false)
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

	@ManyToOne(optional = false)
	@JoinColumn(name = "userid", referencedColumnName = "userid", 
		foreignKey = @ForeignKey(name = "FK_children_user"))
	@JsonIgnoreProperties({"children"})
	public UserEntity getUser() {
		return user;
	}


	public void setUser(UserEntity user) {
		this.user = user;
	}
}
