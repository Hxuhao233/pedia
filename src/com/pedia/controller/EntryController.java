package com.pedia.controller;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.pedia.model.Action;
import com.pedia.model.Comment;
import com.pedia.model.Entry;
import com.pedia.model.Report;
import com.pedia.model.User;
import com.pedia.service.IEntryService;
import com.pedia.service.ISearchService;
import com.pedia.tool.BaseEntryDataList;
import com.pedia.tool.CommentData;
import com.pedia.tool.DetailedEntryData;
import com.pedia.tool.EntryInfo;
import com.pedia.tool.RequestData;
import com.pedia.tool.ResponseData;

@Controller
@RequestMapping(value="/entry")
public class EntryController {
	
	@Autowired
	private IEntryService entryService;
	/*
	@Autowired
	private ISearchService searchService;
	*/
	// 查询词条
	@RequestMapping(value = "/queryEntry",method = RequestMethod.GET)
	public @ResponseBody ResponseData queryEntry(@RequestParam(value = "search" , required=false)String info){
		
		ResponseData response = new ResponseData();
		System.out.println("queryEntry : " + info.trim());
		if(info.equals(""))
			info=".*";
		List<EntryInfo> entryDataList = entryService.queryEntry(info.trim());
		//entryDataList.addAll(searchService.search(info));
		if(entryDataList.size()>0){
			
			response.setCode(200);
			Map<String,Object> data = new HashMap<String,Object>();
			data.put("listNum", entryDataList.size());
			data.put("list",entryDataList);
			response.setData(data);
			
		}else{
			
			response.setCode(404);
			//response.setData(new HashMap<String,Object>());
			
		}

		return response;
	}
	
	// 直接进入词条
		@ResponseBody
		@RequestMapping(value="/enterEntryDirectly",method=RequestMethod.GET)
		public ResponseData entryEntryDirectly(@RequestParam(value = "entryName") String entryName){
			ResponseData response = new ResponseData();
			EntryInfo entryData = entryService.enterEntry(entryName);
			System.out.println("enterEntryDirectly :" + entryName);
			if(entryData!=null){
				
				Map<String,Object> data = new HashMap<String,Object>();
				String eid = entryData.getEid();
				List<CommentData> commentList = new ArrayList<>();
				if(eid!=null){
					commentList = entryService.queryComment(Integer.valueOf(eid));
				}
				data.put("eid", entryData.getEid());
 				data.put("entryName", entryData.getEntryName());
				data.put("pic",entryData.getPictureAddr());
				data.put("detail", entryData.getEntryContent());
				data.put("label1", entryData.getLabel1());
				data.put("label2", entryData.getLabel2());
				data.put("label3", entryData.getLabel3());
				data.put("label4", entryData.getLabel4());
				data.put("praiseTime",entryData.getPraiseTimes());
				data.put("badReviewTimes", entryData.getBadReviewTimes());
				data.put("commentsNum", commentList.size());
				data.put("comments", commentList);
	
				response.setCode(200);		
				response.setData(data);
			
			}else{
				response.setCode(404);
				Map<String,Object> data = new HashMap<String,Object>();
				data.put("info", "未找到该词条");
				response.setData(data);
			}
			
			return response;
			
		}
		
	
	
	
	// 进入词条
	@ResponseBody
	@RequestMapping(value="/enterEntry",method=RequestMethod.GET)
	public ResponseData entryEntry(@RequestParam(value = "eid") int eid){
		ResponseData response = new ResponseData();
		EntryInfo e = entryService.enterEntry(eid);
		if(e != null){

			Map<String,Object> data = new HashMap<String,Object>();

			List<CommentData> commentList = entryService.queryComment(Integer.valueOf(e.getEid()));
			
			data.put("eid", e.getEid());
			data.put("entryName", e.getEntryName());
			data.put("pic",e.getPictureAddr());
			data.put("detail", e.getEntryContent());
			data.put("label1", e.getLabel1());
			data.put("label2", e.getLabel2());
			data.put("label3", e.getLabel3());
			data.put("label4", e.getLabel4());
			data.put("praiseTime",e.getPraiseTimes());
			data.put("badReviewTimes", e.getBadReviewTimes());
			data.put("commentsNum",commentList.size());
			data.put("comments",commentList);
					
			response.setCode(200);		
			response.setData(data);
			
		}else{
			response.setCode(404);
		}
		
		return response;
		
	}
	
	// 创建词条
	@ResponseBody
	@RequestMapping(value="createEntry",method=RequestMethod.POST)
	public  ResponseData createEntry(EntryInfo entryInfo, @RequestParam(value = "file", required = false) MultipartFile file, HttpServletRequest request,HttpSession session){
		ResponseData response = new ResponseData();
		User u = (User) session.getAttribute("user");
		
		Integer uid ; 
		if(u==null)
			uid = new Integer(2);
		else
			uid = u.getUid();
		
		Entry entry = new Entry();
		Action action = new Action();
		entry.setEntryname(entryInfo.getEntryName());
		entry.setPublisher(u.getUsername());
		action.setEntrycontent(entryInfo.getEntryContent());
		action.setUid(uid);
		action.setLabel1(entryInfo.getLabel1());
		action.setLabel2(entryInfo.getLabel2());
		action.setLabel3(entryInfo.getLabel3());
		action.setLabel4(entryInfo.getLabel4());
		String filePath = "";
		
		if(file!=null){
			String pathRoot = request.getSession().getServletContext().getRealPath("/") + "../static";
			filePath = entryService.uploadImage(pathRoot, file);
			action.setPictureaddr(filePath);
			//System.out.println(filePath);
		}

		
		int ret=0;
		if(entryInfo.getEid()==null||"".equals(entryInfo.getEid())){
			action.setType(1);
			ret = entryService.createEntry(entry, action);
		}else{
			action.setEid(Integer.valueOf(entryInfo.getEid()));
			action.setType(2);
			ret = entryService.modifyEntry(action);
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
	@RequestMapping(value="/checkEntryCreatable",method = RequestMethod.GET)
	public ResponseData checkEntryCreatable(@PathParam("entryName")String entryName){
			System.out.println("check " + entryName);
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
	
	
	//点赞
		@ResponseBody
		@RequestMapping(value="/priase/{eid}",method = RequestMethod.GET)
		public ResponseData praise(@PathVariable("eid")Integer entryID,HttpSession session){
			ResponseData ret=new ResponseData();
			Map<String, Object> data=new HashMap<>();
			List<Integer> priaseList=(List<Integer>)session.getAttribute("priaseList");
			if(priaseList==null){
				priaseList = new ArrayList<Integer>();
				entryService.priase(entryID);
				priaseList.add(entryID);
				session.setAttribute("priaseList", priaseList);
				ret.setCode(200);
				data.put("info", "点赞成功");
				ret.setData(data);
			}else{
				for (int i=0;i<priaseList.size();i++)
				{
					if (priaseList.get(i)==entryID){
						ret.setCode(500);
						data.put("info", "点赞失败，该用户短期内对该词条点过赞");
						ret.setData(data);
						return ret;
					}
				}
				entryService.priase(entryID);
				priaseList.add(entryID);
				session.setAttribute("priaseList", priaseList);
				ret.setCode(200);
				data.put("info", "点赞成功");
				ret.setData(data);
			}

			return ret;
		}
		
		//差评
		@ResponseBody
		@RequestMapping(value="/badReview/{eid}",method = RequestMethod.GET)
		public ResponseData badReview(@PathVariable("eid")Integer entryID,HttpSession session){
			ResponseData ret=new ResponseData();
			Map<String, Object> data=new HashMap<>();
			List<Integer> badReviewList=(List<Integer>)session.getAttribute("badReviewList");
			if(badReviewList==null){
				badReviewList = new ArrayList<Integer>();
				entryService.badReview(entryID);
				badReviewList.add(entryID);
				session.setAttribute("badReviewList", badReviewList);
				ret.setCode(200);
				data.put("info", "差评成功");
				ret.setData(data);
			}else{
				for (int i=0;i<badReviewList.size();i++)
				{
					if (badReviewList.get(i)==entryID){
						ret.setCode(500);
						data.put("info", "差评失败，该用户短期内对该词条点过差评");
						ret.setData(data);
						return ret;
					}
				}
				entryService.badReview(entryID);
				badReviewList.add(entryID);
				session.setAttribute("badReviewList", badReviewList);
				ret.setCode(200);
				data.put("info", "差评成功");
				ret.setData(data);
			}
			return ret;
			
		}
		
		
		//提交举报
		@ResponseBody
		@RequestMapping(value="/report",method = RequestMethod.POST)
		public ResponseData report(@RequestBody RequestData requestData,HttpSession session){
			ResponseData ret=new ResponseData();
			Map<String, Object> data=new HashMap<>();
			int eid=Integer.parseInt(requestData.getData().get("eid").trim());
			String report=requestData.getData().get("report");
			User u = (User) session.getAttribute("user");
			if (u!=null){
				Report submitReport=new Report();	
				submitReport.setEid(eid);
				submitReport.setReason(report);
				submitReport.setUid(u.getUid());
				entryService.submitReport(submitReport);
				ret.setCode(200);
				data.put("info", "提交举报成功");
			}
			else {
				data.put("info", "用户未登录");
				ret.setCode(500);
			}
			ret.setData(data);
			return ret;
		}
		
		//提交评论
		@ResponseBody
		@RequestMapping(value="/comment",method = RequestMethod.POST)
		public ResponseData comment(@RequestBody RequestData requestData,HttpSession session){
			ResponseData ret=new ResponseData();
			Map<String, Object> data=new HashMap<>();
			int eid=Integer.parseInt(requestData.getData().get("eid"));
			String comment=requestData.getData().get("comment");
			User u = (User) session.getAttribute("user");
			
			if (u!=null){
				Comment submitComment=new Comment();
				submitComment.setCommentcontent(comment);
				submitComment.setEid(eid);
				submitComment.setUid(u.getUid());
				entryService.submitComment(submitComment);
				ret.setCode(200);
				data.put("info", "提交评论成功");
				data.put("comment",entryService.getComment(submitComment));
			}
			else {
				data.put("info", "用户未登录");
				ret.setCode(500);
			}
			ret.setData(data);
			return ret;
		}
		
		// 查看词条
		@ResponseBody
		@RequestMapping(value="/seeEntry",method=RequestMethod.GET)
		public ResponseData seeEntry(@RequestParam(value = "eid") int eid,@RequestParam(value = "aid") int aid){
			ResponseData response = new ResponseData();
			BaseEntryDataList entryData = entryService.seeEntry(eid,aid);
			if(entryData.getData().size()>0){
				response.setCode(200);
				//Map<String,String> entryinfo = new Has
				response.setData(entryData.getData().get(0));
			}else{
				response.setCode(404);
			}
			
			return response;
			
		}
}
