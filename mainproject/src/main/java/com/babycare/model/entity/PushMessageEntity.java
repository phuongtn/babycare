package com.babycare.model.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import com.babycare.model.payload.PushMessage;

@Entity
@Table(name = "pushmessage", catalog = "babycare")
public class PushMessageEntity extends PushMessage implements Serializable {

	private static final long serialVersionUID = 1L;
	private String payLoad;
	private String action;
	private DateTime timeStamp;
	private String pushId;

	@Override
	public PushMessageEntity setMessageId(String messageId) {
		this.messageId = messageId;
		return this;
	}

	@Column(name = "payload")
	public String getPayLoad() {
		return payLoad;
	}

	@Id
	@Column(name = "messageid")
	public String getMessageId() {
		return messageId;
	}


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

	
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	@Column(name = "timestamp", updatable = false, insertable = false)
	public DateTime getTimeStamp() {
		return timeStamp;
	}

	public PushMessageEntity setTimeStamp(DateTime timeStamp) {
		this.timeStamp = timeStamp;
		return this;
	}

	@Column(name = "pushid")
	public String getPushId() {
		return pushId;
	}

	public PushMessageEntity setPushId(String pushId) {
		this.pushId = pushId;
		return this;
	}

}
