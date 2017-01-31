package com.pedia.tool;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pedia.model.Action;
import com.pedia.model.Entry;
import com.pedia.model.Report;

public class BaseEntryDataList {
	
	//private int listNum;
	protected int code;

	
	protected List<Map<String,Object>> data;

	public BaseEntryDataList(){
		//listNum=0;
		data = new ArrayList<Map<String,Object>>();
	}
	public int addNormalEntry(Entry item,Action nowContent){
		
		//EntryData newEntry = new EntryData(item,publisher,labels);
		Map<String,Object>entryInfo;
		entryInfo = new HashMap<String,Object>();
		entryInfo.put("eid", item.getEid().toString());
		entryInfo.put("entryName",item.getEntryname());
		entryInfo.put("createName",item.getPublisher());
		entryInfo.put("createDate",new SimpleDateFormat("yyyy-MM-dd").format(item.getPublishtime()));
		entryInfo.put("pictureAddr",nowContent.getPictureaddr());
		entryInfo.put("detail",nowContent.getEntrycontent());
		entryInfo.put("label1", nowContent.getLabel1());
		entryInfo.put("label2", nowContent.getLabel2());
		entryInfo.put("label3", nowContent.getLabel3());
		entryInfo.put("label4", nowContent.getLabel4());
		data.add(entryInfo);
		//listNum++;
		return 1;
		
	}
	
	public int addReportedEntry(Entry entry,String reporter,Report report){
		Map<String,Object>item;
		item = new HashMap<String,Object>();
		item.put("eid", entry.getEid().toString());
		item.put("rid",report.getRid());
		item.put("entryName",entry.getEntryname());
		item.put("reported", reporter);
		item.put("reason",report.getReason());
		data.add(item);
		//listNum++;
		return 1;
	}
	
	public int addUncheckedEntry(Entry entry,Action action,String publisher){
		Map<String,Object>item;
		item = new HashMap<String,Object>();
		item.put("eid", entry.getEid().toString());
		item.put("aid", action.getAid());
		item.put("entryName",entry.getEntryname());
		item.put("publisher",publisher);
		item.put("createTime",new SimpleDateFormat("yyyy-MM-dd").format(entry.getPublishtime()));
		data.add(item);
		//listNum++;
		return 1;
	}
	public int addModifiedEntry(Entry entry,String modifier,int aid){
		Map<String,Object>item;
		item = new HashMap<String,Object>();
		item.put("eid", entry.getEid().toString());
		item.put("entryName",entry.getEntryname());
		item.put("modifier",modifier);
		item.put("modifyTime",new SimpleDateFormat("yyyy-MM-dd").format(entry.getPublishtime()));
		item.put("aid", aid);
		data.add(item);
		//listNum++;
		return 1;
	}
	public List<Map<String,Object>> getData() {
		return data;
	}
	public void setData(List<Map<String,Object>> data) {
		this.data = data;
	}
	/*
	public int getListNum() {
		return listNum;
	}
	public void setListNum(int listNum) {
		this.listNum = listNum;
	}
	*/
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
}
