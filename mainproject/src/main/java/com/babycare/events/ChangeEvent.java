package com.babycare.events;
import com.babycare.model.BaseModel;

public class ChangeEvent extends BaseApplicationEvent<BaseModel> {

	private static final long serialVersionUID = 1L;

	public ChangeEvent(BaseModel source) {
		super(source);
	}

}
