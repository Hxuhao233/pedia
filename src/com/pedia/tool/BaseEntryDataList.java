package com.pedia.tool;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pedia.model.Entry;
import com.pedia.model.Label;
import com.pedia.model.Report;

public class BaseEntryDataList {
	
	//private int listNum;
	protected int code;

	
	protected List<Map<String,Object>> data;

	public BaseEntryDataList(){
		//listNum=0;
		data = new ArrayList<Map<String,Object>>();
	}
	public int addNormalEntry(Entry item,String publisher,List<Label> labels){
		
		//EntryData newEntry = new EntryData(item,publisher,labels);
		Map<String,Object>entryInfo;
		entryInfo = new HashMap<String,Object>();
		entryInfo.put("eid", item.getEid().toString());
		entryInfo.put("entryName",item.getEntryname());
		entryInfo.put("createName",publisher);
		entryInfo.put("createDate",new SimpleDateFormat("yyyy-MM-dd").format(item.getPublishtime()));
		entryInfo.put("pic",item.getPictureaddr());
		entryInfo.put("detail",item.getEntrycontent());
		for(int i=0;i<labels.size();i++){
			entryInfo.put("label" + (i+1),labels.get(i).getLabelcontent());
		}
		for(int i=labels.size();i<4;i++){
			entryInfo.put("label" + (i+1),"");
		}
		data.add(entryInfo);
		//listNum++;
		return 1;
		
	}
	
	public int addReportedEntry(Entry entry,String reporter,Report report){
		Map<String,Object>item;
		item = new HashMap<String,Object>();
		item.put("entryId", entry.getEid().toString());
		item.put("rid",report.getRid());
		item.put("entryName",entry.getEntryname());
		item.put("reported", reporter);
		item.put("reason",report.getReason());
		data.add(item);
		//listNum++;
		return 1;
	}
	
	public int addUncheckedEntry(Entry entry,String publisher){
		Map<String,Object>item;
		item = new HashMap<String,Object>();
		item.put("entryId", entry.getEid().toString());
		item.put("entryName",entry.getEntryname());
		item.put("publisher", publisher);
		item.put("createTime",new SimpleDateFormat("yyyy-MM-dd").format(entry.getPublishtime()));
		data.add(item);
		//listNum++;
		return 1;
	}
	public int addModifiedEntry(Entry entry,String publisher){
		Map<String,Object>item;
		item = new HashMap<String,Object>();
		item.put("entryId", entry.getEid().toString());
		item.put("entryName",entry.getEntryname());
		item.put("publisher", publisher);
		item.put("modifyTime",new SimpleDateFormat("yyyy-MM-dd").format(entry.getPublishtime()));
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
