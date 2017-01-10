package com.pedia.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pedia.service.IEntryService;
import com.pedia.service.IManagerService;
import com.pedia.service.IUserService;
import com.pedia.tool.BaseEntryDataList;
import com.pedia.tool.DetailedEntryData;
import com.pedia.tool.RequestData;
import com.pedia.tool.ResponseData;


@Controller
@RequestMapping(value="/manager")
public class ManagerController {
	
	@Autowired
	private IManagerService managerService;
	

	
	@Autowired
	private IEntryService entryService;
	
	
	// 管理员初始页面
	@ResponseBody
	@RequestMapping(value="/init",method=RequestMethod.GET)
	public ResponseData getAllData(){
		ResponseData response = new ResponseData();
		
		BaseEntryDataList uncheckedEntryList =  managerService.getUnCheckedEntry();
		BaseEntryDataList reportedEntryList =  managerService.getReportedEntry();
		BaseEntryDataList modifiedEntryList =  managerService.getModifiedEntry();
		System.out.println(uncheckedEntryList.getData().size());
		
		Map<String,Object> data = new HashMap<String,Object>();
		data.put("userName", "root");
		data.put("uncheckedEntryList", uncheckedEntryList.getData());
		data.put("reportedEntryList", reportedEntryList.getData());
		data.put("modifiedEntryList", modifiedEntryList.getData());
		response.setData(data);
		response.setCode(200);
		
		return response;
	}
	
	
	// 查看词条
		@ResponseBody
		@RequestMapping(value="/seeEntry",method=RequestMethod.GET)
		public ResponseData seeEntry(@RequestParam(value = "eid") int eid){
			ResponseData response = new ResponseData();
			BaseEntryDataList entryData = managerService.seeEntry(eid);
			if(entryData.getData().size()>0){
				response.setCode(200);
				//Map<String,String> entryinfo = new Has
				response.setData(entryData.getData().get(0));
			}else{
				response.setCode(404);
			}
			
			return response;
			
		}
	
	// 审核待发布词条
	@ResponseBody
	@RequestMapping(value="/checkEntry",method = RequestMethod.POST)
	public ResponseData checkEntry(@RequestBody RequestData request){
		ResponseData response = new ResponseData();
		Map<String,String> requestData = request.getData();
		for (String in : requestData.keySet()) {
			//map.keySet()返回的是所有key的值            
			String str = requestData.get(in);//得到每个key多对用value的值
			System.out.println(in + "     " + str);
		}
		Integer eid = new Integer(requestData.get("eid").trim());
		Boolean allow = requestData.get("allow").equals("1") ? true : false;
		String reason = requestData.get("reason");
		int ret = managerService.checkEntry(eid, allow, reason);
		if(ret > 0){
			response.setCode(200);
		}else{
			response.setCode(500);
		}
		return response;
	}
	
	
	
	// 审核已修改词条
	@ResponseBody
	@RequestMapping(value="/checkModifiedEntry",method = RequestMethod.POST)
	public ResponseData checkModifiedEntry(@RequestBody RequestData request){
		ResponseData response = new ResponseData();
		Map<String,String> requestData = request.getData();
		Integer eid = Integer.valueOf(requestData.get("eid").trim());
		Boolean allow = requestData.get("allow").equals("1") ? true : false;
		String reason = requestData.get("reason");
		int ret = managerService.checkModifiedEntry(eid, allow, reason);
		if(ret > 0){
			response.setCode(200);
		}else{
			response.setCode(500);
		}
		return response;
	}
	
	
	// 删除被举报词条
	@ResponseBody
	@RequestMapping(value="/checkReportedEntry",method = RequestMethod.GET)
	public ResponseData deleteEntry(@RequestParam("rid")int rid,@RequestParam("eid") int eid,@RequestParam("allow") int allow){
		ResponseData response = new ResponseData();

		int ret =0;
		if(allow==1){
			ret = entryService.deleteEntry(eid);
			entryService.handleReport(rid,eid, 2);
		}else{
			ret = entryService.handleReport(rid,eid, 3);
			
		}
		if(ret > 0){
			response.setCode(200);
		}else{
			response.setCode(500);
		}
		return response;
	}
}
