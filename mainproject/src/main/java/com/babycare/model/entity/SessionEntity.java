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
import javax.persistence.Transient;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.babycare.model.payload.Session;

@Entity
@Table(name = "session", catalog = "babycare")
public class SessionEntity extends Session implements Serializable {

	private static final long serialVersionUID = 856603274488076082L;

	private UserEntity user;

	@Override
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="sessionid")
	public Long getSessionId() {
		return sessionId;
	}

	@Override
	@Column(name="hardwareid")
	public String getHardwareId() {
		return hardwareId;
	}

	@Override
	@Column(name="pushid")
	public String getPushId() {
		return pushId;
	}

	@Override
	@Column(name="platform")
	public String getPlatform() {
		return platform;
	}

	@Override
	@Column(name="status")
	public Integer getStatus() {
		return status;
	}

	@Override
	@Transient
	public Long getUserId() {
		return userId;
	}

	
	@ManyToOne(optional = false)
	@JoinColumn(name = "userid", referencedColumnName = "userid", 
		foreignKey = @ForeignKey(name = "FK_session_user"))
	public UserEntity getUser() {
		return user;
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


	public void setUser(UserEntity user) {
		this.user = user;
	}
	

	public SessionEntity(Session session) {
		this.sessionId = session.getSessionId();
		this.hardwareId = session.getHardwareId();
		this.platform = session.getPlatform();
		this.pushId = session.getPushId();
		this.status = session.getStatus();
		this.user = new UserEntity(session.getUser());
	}
	
	public SessionEntity() {
	}

}
