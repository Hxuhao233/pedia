package com.pedia.controller;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.pedia.model.Entry;
import com.pedia.model.Label;
import com.pedia.model.User;
import com.pedia.service.IEntryService;
import com.pedia.tool.BaseEntryDataList;
import com.pedia.tool.DetailedEntryData;
import com.pedia.tool.NewEntryInfo;
import com.pedia.tool.RequestData;
import com.pedia.tool.ResponseData;

@Controller
@RequestMapping(value="/entry")
public class EntryController {
	
	@Autowired
	private IEntryService entryService;
	
	// 查询词条
	@RequestMapping(value = "/queryEntry",method = RequestMethod.GET)
	public @ResponseBody ResponseData queryEntry(@RequestParam(value = "search" , required=false)String info){
		
		ResponseData response = new ResponseData();
		System.out.println("info : " + info);
		if(info == null)
			info=".*";
		
		BaseEntryDataList entryDataList = entryService.queryEntry(info);
		if(entryDataList.getData().size()>0){
			
			response.setCode(200);
			Map<String,Object> data = new HashMap<String,Object>();
			data.put("listNum", entryDataList.getData().size());
			data.put("list",entryDataList.getData());
			response.setData(data);
			
		}else{
			
			response.setCode(500);
			//response.setData(new HashMap<String,Object>());
			
		}

		return response;
	}
	
	// 进入词条
	@ResponseBody
	@RequestMapping(value="/enterEntry",method=RequestMethod.GET)
	public ResponseData entryEntry(@RequestParam(value = "eid") int eid){
		ResponseData response = new ResponseData();
		DetailedEntryData entryData = entryService.enterEntry(eid);
		if(entryData != null){
			response.setCode(200);
			Map<String,Object> data = new HashMap<String,Object>();
			//Map<String,String> entryinfo = new Has
			data.put("entry", entryData.getEntry());
			data.put("labels", entryData.getLabels());
			data.put("comments", entryData.getComments());
			response.setData(data);
		}else{
			response.setCode(404);
		}
		
		return response;
		
	}
	
	// 创建词条
	@ResponseBody
	@RequestMapping(value="createEntry",method=RequestMethod.POST)
	public  ResponseData createEntry(NewEntryInfo entryInfo, @RequestParam(value = "file", required = false) MultipartFile file, HttpServletRequest request,HttpSession session){
		ResponseData response = new ResponseData();
		User u = (User) session.getAttribute("user");
		
		Integer uid ; 
		if(u==null)
			uid = new Integer(2);
		else
			uid = u.getUid();
		
		Entry entry = new Entry();
		entry.setEntryname(entryInfo.getEntryName());
		entry.setEntrycontent(entryInfo.getEntryContent());
		entry.setUid(uid);
		String filePath = "";
		
		if(file!=null){
			String pathRoot = request.getSession().getServletContext().getRealPath("/") + "../static";
			filePath = entryService.uploadImage(pathRoot, file);
			entry.setPictureaddr(filePath);
			//System.out.println(filePath);
		}
		
		
		List<Label> labels = new ArrayList<Label>();
		String content = entryInfo.getLabel1();
		if(content!=null){
			Label l = new Label();
			l.setLabelcontent(content);
			labels.add(l);
		}
		content = entryInfo.getLabel2(); 	
		if(content!=null){
			Label l = new Label();
			l.setLabelcontent(content);
			labels.add(l);
		}
		content = entryInfo.getLabel3(); 	
		if(content!=null){
			Label l = new Label();
			l.setLabelcontent(content);
			labels.add(l);
		}
		content = entryInfo.getLabel4(); 	
		if(content!=null){
			Label l = new Label();
			l.setLabelcontent(content);
			labels.add(l);
		}
		
		
		int ret=0;
		if(entry.getEid()==null){
			ret = entryService.createEntry(entry, labels);
		}else{
			int oldEntryid = entry.getEid();
			entry.setEid(null);
			ret = entryService.modifyEntry(oldEntryid, entry, labels);
		}
		Map<String,Object> data = new HashMap<String,Object>();
		if(ret > 0){
			response.setCode(200);
			data.put("info", "succeed");
			response.setData(data);
		}else{
			response.setCode(500);
			data.put("info", "failed");
			response.setData(data);
		}
		return response;
	}
	

	//检测词条是否可以被创建
	@ResponseBody
	@RequestMapping(value="/checkEntryCreatable/{lemmaName}",method = RequestMethod.POST)
	public ResponseData checkEntryCreatable(@PathVariable("lemmaName")String entryName){
			ResponseData ret=new ResponseData();
			if (entryService.checkEntryCreatable(entryName)){
				ret.setCode(200);
				Map<String, Object> data=new HashMap<>();
				data.put("info", "success");
				ret.setData(data);
			}
			else {
				ret.setCode(203);
				Map<String, Object> data=new HashMap<>();
				data.put("info", "此词条名已存在");
				ret.setData(data);
			}
			return ret;
		}
	
}
