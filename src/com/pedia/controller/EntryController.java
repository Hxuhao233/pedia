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

import com.pedia.model.Comment;
import com.pedia.model.Entry;
import com.pedia.model.Label;
import com.pedia.model.Report;
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
	
	// 直接进入词条
		@ResponseBody
		@RequestMapping(value="/enterEntryDirectly",method=RequestMethod.GET)
		public ResponseData entryEntryDirectly(@RequestParam(value = "entryName") String entryName){
			ResponseData response = new ResponseData();
			DetailedEntryData entryData = entryService.enterEntry(entryName);
			if(entryData != null){

				Map<String,Object> data = new HashMap<String,Object>();

				Entry e = entryData.getEntry();
				List<Label> labels = entryData.getLabels();
				data.put("eid", e.getEid());
				data.put("entryName", e.getEntryname());
				data.put("pic",e.getPictureaddr());
				data.put("detail", e.getEntrycontent());
				for(int i=0;i<labels.size();i++){
					data.put("label" +(i+1), labels.get(i).getLabelcontent());
				}
				for(int i=labels.size();i<4;i++){
					data.put("label" +(i+1), "");
				}		
				data.put("praiseTime",e.getPraisetimes());
				data.put("badReviewTimes", e.getBadreviewtimes());
				data.put("commentsNum",entryData.getComments().size());
				data.put("comments", entryData.getComments());
						
						
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
		DetailedEntryData entryData = entryService.enterEntry(eid);
		if(entryData != null){

			Map<String,Object> data = new HashMap<String,Object>();

			Entry e = entryData.getEntry();
			List<Label> labels = entryData.getLabels();
			data.put("eid", e.getEid());
			data.put("entryName", e.getEntryname());
			data.put("pic",e.getPictureaddr());
			data.put("detail", e.getEntrycontent());
			for(int i=0;i<labels.size();i++){
				data.put("label" +(i+1), labels.get(i).getLabelcontent());
			}
			for(int i=labels.size();i<4;i++){
				data.put("label" +(i+1), "");
			}		
			data.put("praiseTime",e.getPraisetimes());
			data.put("badReviewTimes", e.getBadreviewtimes());
			data.put("commentsNum",entryData.getComments().size());
			data.put("comments", entryData.getComments());
					
					
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
		if(entryInfo.getEid()==null||"".equals(entryInfo.getEid())){
			ret = entryService.createEntry(entry, labels);
		}else{
			int oldEntryid = Integer.valueOf(entryInfo.getEid());
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
	@RequestMapping(value="/checkEntryCreatable",method = RequestMethod.GET)
	public ResponseData checkEntryCreatable(@PathParam("entryName")String entryName){
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
					Integer uid = u!=null? u.getUid() : 2;
					if (uid!=null){
						Comment submitComment=new Comment();
						submitComment.setCommentcontent(comment);
						submitComment.setEid(eid);
						submitComment.setUid(uid);
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
					public ResponseData seeEntry(@RequestParam(value = "eid") int eid){
						ResponseData response = new ResponseData();
						BaseEntryDataList entryData = entryService.seeEntry(eid);
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
