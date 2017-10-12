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
import com.babycare.model.payload.User;

@Entity
@Table(name = "user", catalog = "babycare")
public class UserEntity extends User implements Serializable {

	private static final long serialVersionUID = -3922441868508208542L;
	@Override
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "userid")
	public Long getUserId() {
		return userId;
	}

	@Override
	@Column(name = "name")
	public String getName() {
		return name;
	}

	@Override
	@Column(name = "dob")
	public Long getDob() {
		return dob;
	}

	@Override
	public String getProvider() {
		return provider;
	}

	@Override
	@Column(name = "email")
	public String getEmail() {
		return email;
	}

	@Override
	@Column(name = "password")
	public String getPassword() {
		return password;
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

	public UserEntity() {
		super();
	}

	public UserEntity(User user) {
		this.email = user.getEmail();
		this.name = user.getName();
		this.provider = user.getProvider();
		this.dob = user.getDob();
		this.userId = user.getUserId();
		this.password = user.getPassword();
	}
}
