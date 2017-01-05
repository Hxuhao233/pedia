package com.pedia.service;

import java.util.List;

import com.pedia.tool.BaseEntryDataList;

public interface IManagerService {
	BaseEntryDataList getUnCheckedEntry();						// 查询待发布词条
	BaseEntryDataList getReportedEntry();							// 查询被举报词条
	BaseEntryDataList getModifiedEntry();							// 查询已修改词条
	
	int checkEntry(int entryId,Boolean allow,String reson);					// 审核待发布词条
	int checkModifiedEntry(int entryId,Boolean allow,String reson);	// 审核已修改词条
	int deleteEntry(int entryId);												// 删除词条
	BaseEntryDataList seeEntry(int eid);									// 查看词条
}
