package com.pedia.service.impl;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationConfigurationException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.pedia.dao.ActionMapper;
import com.pedia.dao.CommentMapper;
import com.pedia.dao.EntryMapper;
import com.pedia.dao.ReportMapper;
import com.pedia.dao.UserMapper;
import com.pedia.model.Action;
import com.pedia.model.Comment;
import com.pedia.model.Entry;
import com.pedia.model.Report;
import com.pedia.model.User;
import com.pedia.service.IEntryService;
import com.pedia.tool.BaseEntryDataList;
import com.pedia.tool.CommentData;
import com.pedia.tool.DetailedEntryData;

@Service("entryService")
public class EntryServiceImpl implements IEntryService{
	
	
	@Autowired
	private UserMapper userDao;
	
	@Autowired
	private EntryMapper entryDao;

	
	@Autowired
	private CommentMapper commentDao;
	
	@Autowired
	private ReportMapper reportDao;
	
	@Autowired
	private ActionMapper actionDao;
	
	@Override
	public int createEntry(Entry newEntry,Action create) {
		// TODO Auto-generated method stub
		int ret = entryDao.insertSelective(newEntry);
		int newEntryId = newEntry.getEid();
		if(ret > 0){
			System.out.println("create Entry " + newEntryId);
			create.setEid(newEntryId);
			ret = actionDao.insertSelective(create);
		}
		return ret;
	}

	@Override
	public int modifyEntry(Action modify) {
		// TODO Auto-generated method stub
		int ret = actionDao.insertSelective(modify);
		return ret;
		
	}

	@Override
	public int deleteEntry(int eid) { //删除词条 输入eid 输出删除结果

		List<Action> nowContent = actionDao.selectByEidAndStatus(eid,2);
		int ret = 0;
		
		nowContent.get(0).setStatus(5);
		ret = actionDao.updateByPrimaryKey(nowContent.get(0));
		if(ret > 0){
			List<Action> previousContentList = actionDao.selectByEidAndStatus(eid,5);
			Action previousContent = previousContentList.get(previousContentList.size()-1);
			previousContent.setStatus(2);
			actionDao.updateByPrimaryKey(previousContent);
		}
		
		
		return ret;
	}
	
	//处理举报
	@Override
	public int handleReport(int rid,int eid,int status){
		int ret = 0;
		if(status == 2 ){
			ret= reportDao.updateByEid(eid,status);
		}else{
			Report r = new Report();
			r.setRid(rid);
			r.setStatus(status);
			ret = reportDao.updateByPrimaryKeySelective(r);
		}
 		return ret;
	}

	
	
	
	@Override
	public DetailedEntryData enterEntry(int eid) {
		// TODO Auto-generated method stub
		DetailedEntryData detailedEntryData = new DetailedEntryData();
		Entry result  = entryDao.selectByPrimaryKey(eid);
		if(result !=null){
			List<Action> nowContent = actionDao.selectByEidAndStatus(eid, 2);
			List<Comment> comments = commentDao.selectByEid(result.getEid());
			List<CommentData> commentData = new ArrayList<CommentData>();
			
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd");
			
			for(Comment item : comments){
				CommentData data = new CommentData();
				User commenter = userDao.selectByPrimaryKey(item.getUid());
				data.setCommenterName(commenter.getUsername());
				data.setCommenterPic(commenter.getIconaddr());
				data.setCommentDate(simpleDateFormat.format(item.getCommenttime()));
				data.setCommentDetail(item.getCommentcontent());
				commentData.add(data);
			}
			
			detailedEntryData.setEntry(result);
			detailedEntryData.setComments(commentData);
			detailedEntryData.setNowContent(nowContent.get(0));
			return detailedEntryData;
		}
		return null;
	}

	@Override
	public BaseEntryDataList queryEntry(String info) {
		// TODO Auto-generated method stub
		BaseEntryDataList entryDataList = new BaseEntryDataList();
		List<Entry> result  = entryDao.selectByInfo(info);
		for(Entry item : result){
			List<Action> nowContent = actionDao.selectByEidAndStatus(item.getEid(),2);
			//System.out.println("233");
			entryDataList.addNormalEntry(item,nowContent.get(0));
		}
		return entryDataList;
	}
	
	
	@Override
	public BaseEntryDataList seeEntry(int aid) {
		// TODO Auto-generated method stub
		BaseEntryDataList entryDataList = new BaseEntryDataList();

		Action Content = actionDao.selectByPrimaryKey(aid);
		Entry item  = entryDao.selectByPrimaryKey(Content.getEid());
		entryDataList.addNormalEntry(item,Content);
	
		return entryDataList;
	}
	
	@Override
	public int submitComment(Comment comment) {
		// TODO Auto-generated method stub
		return commentDao.insertSelective(comment);
	}

	@Override
	public int submitReport(Report report) {
		// TODO Auto-generated method stub
	
		entryDao.addOneByPrimaryKey(report.getEid(), "reportTimes");
		return reportDao.insertSelective(report);
	}

	@Override
	public int priase(int entryId) {//点赞 输入词条id 输出是否点赞成功
		System.out.println("为id为"+entryId+"的词条点赞");
		return entryDao.addOneByPrimaryKey(entryId, "praiseTimes");
	}

	@Override
	public int badReview(int entryId) {//差评 输入词条id 输出是否差评成功
		System.out.println("为id为"+entryId+"的词条差评");
		return entryDao.addOneByPrimaryKey(entryId, "badReviewTimes");
	}
	
	@Override
	public String uploadImage(String pathRoot, MultipartFile file) {
		// TODO Auto-generated method stub
		//String filePath = new String();

		String path = "";
		if (!file.isEmpty()) {
			// 生成uuid作为文件名称
			String uuid = UUID.randomUUID().toString().replaceAll("-", "");
			// 获得文件类型（可以判断如果不是图片，禁止上传）
			String contentType = file.getContentType();
			// 获得文件后缀名称
			String imageName = contentType.substring(contentType.indexOf("/") + 1);
			pathRoot += "/images/";
			path = uuid + "." + imageName.trim();

			try {
				file.transferTo(new File(pathRoot + path));

				System.out.println(pathRoot + path);

				// request.setAttribute("imagesPath", "../../static" + path);
				//filePath = "../../static/image/" + path;

				
				return path;
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		}
		
		return null;
	}
	

	@Override
	public boolean checkEntryCreatable(String entryName) { //检测词条是否可以被创建
		Entry entry=entryDao.selectByAllEntryName(entryName);
		
		if (entry!=null &&entry.getStatus()==2) 
			return false; //只要有已发布版本就不能被创建

		return true;
	}

	@Override
	public CommentData getComment(Comment item){
		item = commentDao.selectByPrimaryKey(item.getCid());
		CommentData data = new CommentData();
		User commenter = userDao.selectByPrimaryKey(item.getUid());
		data.setCommenterName(commenter.getUsername());
		data.setCommenterPic(commenter.getIconaddr());
		data.setCommentDate(new SimpleDateFormat("yyyy-MM-dd").format(item.getCommenttime()));
		data.setCommentDetail(item.getCommentcontent());
		return data;
	}
	
	@Override
	public DetailedEntryData enterEntry(String info) {
		// TODO Auto-generated method stub
		DetailedEntryData detailedEntryData = new DetailedEntryData();
		Entry result  = entryDao.selectByAllEntryName(info);
		if(result !=null){
			
			List<Action> nowContent = actionDao.selectByEidAndStatus(result.getEid(), 2);
			List<Comment> comments = commentDao.selectByEid(result.getEid());
			List<CommentData> commentData = new ArrayList<CommentData>();
			
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd");
			
			for(Comment item : comments){
				CommentData data = new CommentData();
				User commenter = userDao.selectByPrimaryKey(item.getUid());
				data.setCommenterName(commenter.getUsername());
				data.setCommenterPic(commenter.getIconaddr());
				data.setCommentDate(simpleDateFormat.format(item.getCommenttime()));
				data.setCommentDetail(item.getCommentcontent());
				commentData.add(data);
			}
			
			detailedEntryData.setEntry(result);
			detailedEntryData.setComments(commentData);
			detailedEntryData.setNowContent(nowContent.get(0));
			return detailedEntryData;
		}
		return null;
	}
}
