package com.pedia.tool;

import java.util.List;

import com.pedia.model.Entry;
import com.pedia.model.User;

public class DetailedUserData { //个人主页信息
	private User user;
	private List<Entry> entries;
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public List<Entry> getEntries() {
		return entries;
	}
	public void setEntries(List<Entry> entries) {
		this.entries = entries;
	}
	
	
}
