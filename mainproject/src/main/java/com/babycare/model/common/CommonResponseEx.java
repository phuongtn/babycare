package com.babycare.model.common;

import com.babycare.model.response.CommonResponse;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@JsonIgnoreProperties({"requestBySessionId"})
public class CommonResponseEx extends CommonResponse {
	public CommonResponseEx() {
	}

	public CommonResponseEx(String message, Boolean isSuccess) {
		super(message, isSuccess);
	}
}
