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

import com.babycare.model.payload.ContentType;

@Entity
@Table(name = "contenttype", catalog = "babycare")
public class ContentTypeEntity extends ContentType implements Serializable {

	private static final long serialVersionUID = 1L;


	public ContentTypeEntity(ContentType contentType) {
		setContentTypeId(contentType.getContentTypeId())
		.setType(contentType.getType());
	}

	@Override
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="contenttypeid")
	public Integer getContentTypeId() {
		return contentTypeId;
	}
	
	@Override
	@Column(name="type")
	public String getType() {
		return type;
	}
	
	public ContentTypeEntity() {
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
