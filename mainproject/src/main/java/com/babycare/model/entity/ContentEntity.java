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

import com.babycare.model.payload.Content;

@Entity
@Table(name = "content", catalog = "babycare")
public class ContentEntity extends Content implements Serializable {

	private static final long serialVersionUID = 1L;
	private ContentTypeEntity contentType;

	@Override
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="contentid")
	public Long getContentId() {
		return contentId;
	}

	@Override
	@ManyToOne(optional = false)
	@JoinColumn(name = "contenttypeid", referencedColumnName = "contenttypeid", 
		foreignKey = @ForeignKey(name = "FK_content_contenttype"))
	public ContentTypeEntity getContentType() {
		return contentType;
	}

	@Override
	@Column(name="header")
	public String getHeader() {
		return header;
	}

	@Override
	@Column(name="content")
	public String getContent() {
		return content;
	}

	@Override
	@Column(name="mediatype")
	public Integer getMediaType() {
		return mediaType;
	}

	@Override
	@Column(name="end")
	public Integer getEnd() {
		return end;
	}

	@Override
	@Column(name="start")
	public Integer getStart() {
		return start;
	}

	@Override
	@Column(name="timeunit")
	public Integer getTimeUnit() {
		return timeUnit;
	}

	@Override
	@Column(name="babytype")
	public Integer getBabyType() {
		return babyType;
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
	public Integer getBloodType() {
		return bloodType;
	}


	public ContentEntity() {

	}
	
	public ContentEntity(Content content) {
		 setBabyType(content.getBabyType())
		.setBloodType(content.getBloodType())
		.setContent(content.getContent())
		.setContentId(content.getContentId())
		.setContentTypeId(content.getContentTypeId())
		.setEnd(content.getEnd())
		.setGender(content.getGender())
		.setHeader(content.getHeader())
		.setMediaType(content.getMediaType())
		.setRegion(content.getRegion())
		.setStart(content.getStart())
		.setTimeUnit(content.getTimeUnit());
		 this.contentType = new ContentTypeEntity(content.getContentType());
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
