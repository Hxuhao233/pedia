package com.pedia.tool;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.pedia.model.Action;
import com.pedia.model.Entry;
import com.pedia.model.User;

public class DetailedUserData { //个人主页信息
	private User user;
	
	List<Map<String,Object>> passEntryList;
	List<Map<String,Object>> uncheckedEntryList;
	List<Map<String,Object>> unpassEntryList;
	
	public DetailedUserData(){
		passEntryList = new ArrayList<Map<String,Object>>();
		uncheckedEntryList = new ArrayList<Map<String,Object>>();
		unpassEntryList = new ArrayList<Map<String,Object>>();
	}
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public List<Map<String, Object>> getPassEntryList() {
		return passEntryList;
	}
	public void setPassEntryList(List<Map<String, Object>> passEntryList) {
		this.passEntryList = passEntryList;
	}
	public List<Map<String, Object>> getUncheckedEntryList() {
		return uncheckedEntryList;
	}
	public void setUncheckedEntryList(List<Map<String, Object>> uncheckedEntryList) {
		this.uncheckedEntryList = uncheckedEntryList;
	}
	public List<Map<String, Object>> getUnpassEntryList() {
		return unpassEntryList;
	}
	public void setUnpassEntryList(List<Map<String, Object>> unpassEntryList) {
		this.unpassEntryList = unpassEntryList;
	}
	
	public void addUncheckedEntry(Map<String,Object> entry){
		uncheckedEntryList.add(entry);
	}
	public void addPassEntry(Map<String,Object> entry){
		passEntryList.add(entry);
	}
	public void addModifiedEntry(Map<String,Object> entry){
		unpassEntryList.add(entry);
	}
	
	
}
