package com.babycare.model.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.babycare.model.BaseModel;

@Entity
@Table(name = "pushmessage", catalog = "babycare")
public class PushMessageEntity extends BaseModel implements Serializable {

	private static final long serialVersionUID = 1L;
	private String messageId;
	private String payLoad;
	private String action;

	@Id
	@Column(name = "messageid")
	public String getMessageId() {
		return messageId;
	}

	public PushMessageEntity setMessageId(String messageId) {
		this.messageId = messageId;
		return this;
	}

	public String getPayLoad() {
		return payLoad;
	}

	@Column(name = "payload")
	public PushMessageEntity setPayLoad(String payLoad) {
		this.payLoad = payLoad;
		return this;
	}

	public PushMessageEntity() {

	}

	@Column(name = "action")
	public String getAction() {
		return action;
	}

	public PushMessageEntity setAction(String action) {
		this.action = action;
		return this;
	}
}
