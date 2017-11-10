package com.babycare.model.common;

import java.util.List;

import com.babycare.model.BaseModel;
import com.babycare.model.ResultList;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@JsonIgnoreProperties({"requestBySessionId"})
public class ResultListEx<T extends BaseModel> extends ResultList<T> {
	public ResultListEx(List<T> resultList) {
		super(resultList);
	}
}
