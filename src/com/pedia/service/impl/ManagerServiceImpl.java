package com.pedia.service.impl;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pedia.dao.ActionMapper;
import com.pedia.dao.CommentMapper;
import com.pedia.dao.EntryMapper;
import com.pedia.dao.ReportMapper;
import com.pedia.dao.UserMapper;
import com.pedia.model.Action;
import com.pedia.model.Entry;
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
	private ReportMapper reportDao;
	
	@Autowired
	private ActionMapper actionDao;
	
	

	@Override
	public BaseEntryDataList getUnCheckedEntry() {
		// TODO Auto-generated method stub
		BaseEntryDataList entryDataList = new BaseEntryDataList();
		List<Entry> result  = entryDao.selectByStatus(1);

		for(Entry item : result){
			//List<Label> labels = labelDao.selectByEid(item.getEid());
			entryDataList.addUncheckedEntry(item);
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
		List<Action> actions = actionDao.selectByStatusAndType(1,2);
		for (Action action : actions){
			User modifier = userDao.selectByPrimaryKey(action.getUid());
			String modifierName = modifier.getUsername()!=null? modifier.getUsername() : modifier.getAccount();
			Entry e = entryDao.selectByPrimaryKey(action.getEid());
			entryDataList.addModifiedEntry(e,modifierName,action.getAid());
		}
		return entryDataList;

	}
	
	

	
	
	@Override
	public int checkEntry(int aid, Boolean allow,String reason) {
		// TODO Auto-generated method stub
		Action a = actionDao.selectByPrimaryKey(aid);
		//allow false 不通过 true 通过
		if(allow == true){
			
			a.setStatus(2);
			User u = userDao.selectByPrimaryKey(a.getUid());
			u.AddExp(5);
			userDao.updateByPrimaryKeySelective(u);
			Entry e = entryDao.selectByPrimaryKey(a.getEid());
			e.setStatus(2);
			entryDao.updateByPrimaryKeySelective(e);
		}else{
			
			a.setStatus(3);
			a.setRefusereason(reason);
		}
		
		int ret = actionDao.updateByPrimaryKeySelective(a);
		return ret;
	}

	@Override
	public int checkModifiedEntry(int aid, Boolean allow, String reason) {
		// TODO Auto-generated method stub
		Action action = actionDao.selectByPrimaryKey(aid);
		int ret = 0;
		
		if(allow == true){
			
			int eid = action.getEid();
			List<Action> oldContent = actionDao.selectByEidAndStatus(eid,2);
			oldContent.get(0).setStatus(4);
			actionDao.updateByPrimaryKeySelective(oldContent.get(0));
			
			action.setStatus(2);
			ret = actionDao.updateByPrimaryKeySelective(action);
			
			User modifier = userDao.selectByPrimaryKey(action.getUid());
			modifier.AddExp(5);
			userDao.updateByPrimaryKeySelective(modifier);
			
		}else{
			
			action.setRefusereason(reason);
			action.setStatus(3);
			ret = actionDao.updateByPrimaryKeySelective(action);
		

			
		}
		
		return ret;
	}


}
