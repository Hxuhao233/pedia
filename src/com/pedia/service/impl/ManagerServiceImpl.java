package com.pedia.service.impl;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pedia.dao.ActionMapper;
import com.pedia.dao.CommentMapper;
import com.pedia.dao.EntryMapper;
import com.pedia.dao.LabelMapper;
import com.pedia.dao.ReportMapper;
import com.pedia.dao.UserMapper;
import com.pedia.model.Action;
import com.pedia.model.Entry;
import com.pedia.model.Label;
import com.pedia.model.Report;
import com.pedia.model.User;
import com.pedia.service.IManagerService;
import com.pedia.tool.BaseEntryDataList;

@Service("mangerService")
public class ManagerServiceImpl implements IManagerService{

	
	@Autowired
	private UserMapper userDao;
	
	@Autowired
	private EntryMapper entryDao;
	
	@Autowired
	private LabelMapper labelDao;
	

	
	@Autowired
	private ReportMapper reportDao;
	
	@Autowired
	private ActionMapper actionDao;
	
	

	@Override
	public BaseEntryDataList getUnCheckedEntry() {
		// TODO Auto-generated method stub
		BaseEntryDataList entryDataList = new BaseEntryDataList();
		List<Entry> result  = entryDao.selectByInfo(".*",1);

		for(Entry item : result){
			User publisher = userDao.selectByPrimaryKey(item.getUid());
			String publisherName = publisher.getUsername() != null ?publisher.getUsername() : "未知"; 
			//List<Label> labels = labelDao.selectByEid(item.getEid());
			entryDataList.addUncheckedEntry(item, publisherName);
		}
		System.out.println(result.size());
		System.out.println(entryDataList.getData().size());
		return entryDataList;

	}

	@Override
	public BaseEntryDataList getReportedEntry() {
		// TODO Auto-generated method stub
		
		BaseEntryDataList entryDataList = new BaseEntryDataList();
		List<Report> reports = reportDao.selectByStatus(1);
		
		for (Report report : reports){
			User reporter = userDao.selectByPrimaryKey(report.getUid());
			String reporterName = reporter.getUsername()!=null? reporter.getUsername() : reporter.getAccount();
			Entry e = entryDao.selectByPrimaryKey(report.getEid());
			entryDataList.addReportedEntry(e,reporterName, report);
		}


		return entryDataList;
	}

	@Override
	public BaseEntryDataList getModifiedEntry() {
		// TODO Auto-generated method stub

		BaseEntryDataList entryDataList = new BaseEntryDataList();
		List<Action> actions = actionDao.selectByStatus(1);
		for (Action action : actions){
			User modifier = userDao.selectByPrimaryKey(action.getUid());
			String modifierName = modifier.getUsername()!=null? modifier.getUsername() : modifier.getAccount();
			Entry e = entryDao.selectByPrimaryKey(action.getNeweid());
			entryDataList.addModifiedEntry(e,modifierName);
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
	public int checkEntry(int entryId, Boolean allow,String reason) {
		// TODO Auto-generated method stub
		Entry e = entryDao.selectByPrimaryKey(entryId);
		//allow false 不通过 true 通过
		if(allow == true){
			
			e.setStatus(2);
			User u = userDao.selectByPrimaryKey(e.getUid());
			u.AddExp(5);
			userDao.updateByPrimaryKeySelective(u);
			
		}else{
			
			e.setStatus(3);
			e.setRefusereason(reason);
		}
		
		int ret = entryDao.updateByPrimaryKeySelective(e);
		return ret;
	}

	@Override
	public int checkModifiedEntry(int entryId, Boolean allow, String reson) {
		// TODO Auto-generated method stub
		Action action = actionDao.selectByKey(entryId);
		int ret = 0;
		
		if(allow == true){
			
			int oldEntryId = action.getEid();
			Entry oldEntry = entryDao.selectByPrimaryKey(oldEntryId);
			oldEntry.setStatus(4);
			entryDao.updateByPrimaryKeySelective(oldEntry);
			
			Entry newEntry = entryDao.selectByPrimaryKey(entryId);
			newEntry.setStatus(2);
			newEntry.setPraisetimes(oldEntry.getPraisetimes());
			newEntry.setBadreviewtimes(oldEntry.getBadreviewtimes());
			newEntry.setUid(oldEntry.getUid());
			ret = entryDao.updateByPrimaryKeySelective(newEntry);
			
			
			User modifier = userDao.selectByPrimaryKey(action.getUid());
			modifier.AddExp(5);
			userDao.updateByPrimaryKeySelective(modifier);
			
			
			
			List<Action> otherActions = actionDao.selectByEid(oldEntryId);
			for(Action item : otherActions){
				
				if(item.getNeweid() == entryId){
					item.setStatus(2);	
				}	
				else{			
					item.setEid(entryId);
				}
				actionDao.updateByPrimaryKeySelective(item);
			}

		}else{
			
			Entry newEntry = entryDao.selectByPrimaryKey(entryId);
			newEntry.setStatus(5);
			newEntry.setRefusereason(reson);
			ret = entryDao.updateByPrimaryKeySelective(newEntry);
		
			action.setStatus(3);
			actionDao.updateByPrimaryKey(action);
			
		}
		
		return ret;
	}



	@Override
	public int deleteEntry(int entryId) {
		Entry entry=entryDao.selectByPrimaryKey(entryId);
		if (entry!=null){
			entry.setStatus(5);
			entryDao.updateByPrimaryKeySelective(entry);
			System.out.println("管理员删除词条成功！"+entry.getEntryname()+"词条的状态值更改为5");
			return 1;
		}
		else {
			System.out.println("管理员删除词条失败！没有该词条");
			return 0;
		}	
	}

}
