package com.pedia.tool;

import java.util.HashMap;
import java.util.Map;

public class RequestData {
	private Map<String,String> data;
	
	public RequestData(){
		data = new HashMap<String,String>();
	}

	public Map<String, String> getData() {
		return data;
	}

	public void setData(Map<String, String> data) {
		this.data = data;
	}
	
}
