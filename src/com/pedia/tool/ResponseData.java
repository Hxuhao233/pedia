package com.pedia.tool;

import java.util.Map;

public class ResponseData {
	private int code;
	private Map<String,Object>data;
	
	
	public ResponseData() {
		super();
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public Map<String, Object> getData() {
		return data;
	}
	public void setData(Map<String, Object> data) {
		this.data = data;
	}
	
}
