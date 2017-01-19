package com.pedia.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.pedia.model.Action;
import com.pedia.model.Comment;
import com.pedia.model.Entry;
import com.pedia.model.Report;
import com.pedia.tool.BaseEntryDataList;
import com.pedia.tool.CommentData;
import com.pedia.tool.DetailedEntryData;
import com.pedia.tool.EntryInfo;

public interface IEntryService {
	int createEntry(Entry newEntry,Action create);							// 创建词条
	int modifyEntry(Action modify);														// 修改词条
	int deleteEntry(int eid);    																	// 删除词条
	
	DetailedEntryData enterEntry(int eid);											// 进入词条（返回类型待定）
	List<EntryInfo> queryEntry(String info);								// 搜索词条
	
	int submitComment(Comment comment);						// 提交评论
	int submitReport(Report report);											// 提交举报
	int priase(int entryId);																// 点赞
	int badReview(int entryId);														// 差评
	
	String uploadImage(String pathRoot, MultipartFile file);		// 上传词条图片
	boolean checkEntryCreatable(String entryName);

	BaseEntryDataList seeEntry(int eid);
	int handleReport(int rid, int eid, int status);
	CommentData getComment(Comment item);
	EntryInfo enterEntry(String info);
	List<CommentData> queryComment(Integer eid);
	

}
