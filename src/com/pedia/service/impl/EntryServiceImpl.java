package com.pedia.service.impl;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.pedia.dao.ActionMapper;
import com.pedia.dao.CommentMapper;
import com.pedia.dao.EntryMapper;
import com.pedia.dao.LabelMapper;
import com.pedia.dao.ReportMapper;
import com.pedia.dao.UserMapper;
import com.pedia.model.Action;
import com.pedia.model.Comment;
import com.pedia.model.Entry;
import com.pedia.model.Label;
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
	private LabelMapper labelDao;
	
	@Autowired
	private CommentMapper commentDao;
	
	@Autowired
	private ReportMapper reportDao;
	
	@Autowired
	private ActionMapper actionDao;
	
	@Override
	public int createEntry(Entry newEntry,List<Label> labels) {
		// TODO Auto-generated method stub
		int ret = entryDao.insertSelective(newEntry);
		int newEntryId = newEntry.getEid();
		System.out.println("create Entry " + newEntryId);
		if(newEntryId > 0){
			for(Label item : labels){
				item.setEid(newEntryId);
				labelDao.insertSelective(item);
			}
			return 1;
		}
		return 0;
	}

	@Override
	public int modifyEntry(int oldEntryId, Entry newEntry, List<Label> labels) {
		// TODO Auto-generated method stub
		Entry oldEntry = entryDao.selectByPrimaryKey(oldEntryId);
		Integer newUId = newEntry.getUid();
		newEntry.setUid(oldEntry.getUid());
		newEntry.setStatus(6);
		int newEntryId = entryDao.insertSelective(newEntry);
		if(newEntryId > 0){
			
			for(Label item : labels){
				item.setEid(newEntryId);
				labelDao.insertSelective(item);
			}
			Action action = new Action();
			action.setEid(oldEntryId);
			action.setUid(newUId);
			action.setNeweid(newEntryId);
			return 1;
		}
		return 0;
	}

	@Override
	public int deleteEntry(int eid) { //删除词条 输入eid 输出删除结果
		Entry entry=entryDao.selectByPrimaryKey(eid);
		if (entry!=null){
			entry.setStatus(5);
			entryDao.updateByPrimaryKeySelective(entry);
			System.out.println("删除词条成功！"+entry.getEntryname()+"词条的状态值更改为5");
			Action a = actionDao.selectByKey(eid);
			if(a!=null){
				Entry oldEntry = entryDao.selectByPrimaryKey(a.getEid());
				oldEntry.setStatus(2);
				entryDao.updateByPrimaryKeySelective(oldEntry);
			}
			return 1;
		}
		else {
			System.out.println("删除词条失败！没有该词条");
			return 0;
		}	
		
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
			User u = userDao.selectByPrimaryKey(result.getUid());
			String publisher = u.getUsername() != null ? u.getUsername() : u.getAccount(); 
			List<Label> labels = labelDao.selectByEid(result.getEid());
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
			detailedEntryData.setLabels(labels);
			detailedEntryData.setPublisher(publisher);
			return detailedEntryData;
		}
		return null;
	}

	@Override
	public BaseEntryDataList queryEntry(String info) {
		// TODO Auto-generated method stub
		BaseEntryDataList entryDataList = new BaseEntryDataList();
		List<Entry> result  = entryDao.selectByInfo(info,2);
		for(Entry item : result){
			User u = userDao.selectByPrimaryKey(item.getUid());
			String publisher = u.getUsername() != null ? u.getUsername() : u.getAccount(); 
			List<Label> labels = labelDao.selectByEid(item.getEid());
			//System.out.println("233");
			entryDataList.addNormalEntry(item, publisher, labels);
		}
		return entryDataList;
	}
	
	
	@Override
	public BaseEntryDataList seeEntry(int eid) {
		// TODO Auto-generated method stub
		BaseEntryDataList entryDataList = new BaseEntryDataList();
		Entry item  = entryDao.selectByPrimaryKey(eid);

		User u = userDao.selectByPrimaryKey(item.getUid());
		String publisher = u.getUsername() != null ? u.getUsername() : u.getAccount(); 
		List<Label> labels = labelDao.selectByEid(item.getEid());
		//System.out.println("233");
		entryDataList.addNormalEntry(item, publisher, labels);
	
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
	
		entryDao.addOne(report.getEid(), "reportTimes");
		return reportDao.insertSelective(report);
	}

	@Override
	public int priase(int entryId) {//点赞 输入词条id 输出是否点赞成功
		System.out.println("为id为"+entryId+"的词条点赞");
		return entryDao.addOne(entryId, "praiseTimes");
	}

	@Override
	public int badReview(int entryId) {//差评 输入词条id 输出是否差评成功
		System.out.println("为id为"+entryId+"的词条差评");
		return entryDao.addOne(entryId, "badReviewTimes");
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
		List<Entry> entries=entryDao.selectByEntryName(entryName);
		for (int i=0;i<entries.size();i++)
		{
			if (entries.get(i).getStatus()==2) return false; //只要有已发布版本就不能被创建
		}
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
		Entry result  = entryDao.selectByAllEntryName(info, 2);
		if(result !=null){
			User u = userDao.selectByPrimaryKey(result.getUid());
			String publisher = u.getUsername() != null ? u.getUsername() : u.getAccount(); 
			List<Label> labels = labelDao.selectByEid(result.getEid());
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
			detailedEntryData.setLabels(labels);
			detailedEntryData.setPublisher(publisher);
			return detailedEntryData;
		}
		return null;
	}
}
