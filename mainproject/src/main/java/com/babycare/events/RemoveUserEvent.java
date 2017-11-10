package com.babycare.events;

import java.util.Set;

import com.babycare.model.BaseModel;
import com.babycare.model.entity.SessionEntity;

public class RemoveUserEvent extends BaseModel {
	private Set<SessionEntity> sessions;

	public RemoveUserEvent() {
	}

	public RemoveUserEvent(Set<SessionEntity> sessions) {
		this.sessions = sessions;
	}

	public Set<SessionEntity> getSessions() {
		return sessions;
	}

	public void setSessions(Set<SessionEntity> sessions) {
		this.sessions = sessions;
	}
}
