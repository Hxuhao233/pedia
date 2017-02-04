package com.pedia.exception.handler;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.NestedServletException;

import com.pedia.exception.SessionInvalidatedException;
import com.pedia.tool.ResponseData;

@ControllerAdvice
public class SessionFailedExceptionHandler {
	
	// 异常处理 可以处理整个控制器抛出的IllegalStateExceptions
	@ResponseBody
	@ExceptionHandler(SessionInvalidatedException.class)
	public ResponseData handleSessionInvalidatedException(SessionInvalidatedException e){
		ResponseData ret = new ResponseData();
		ret.setCode(500);
		
		System.out.println(e.getMessage());
		return ret;
	}
	
	/*
	// 异常处理 可以处理整个控制器抛出的ResourceNotFoundException
		@ResponseBody
		@ExceptionHandler(ResourceNotFoundException.class)
		public ResponseEntity<ResponeData> handleResourceNotFound(ResourceNotFoundException e){
			ResponeData ret = new ResponeData();
			ret.setCode(404);
			ret.setInfo(e.getErrMsg());
			return new ResponseEntity<ResponeData>(ret,HttpStatus.NOT_FOUND);
		}
		*/
/*
	@Override
	public ModelAndView resolveException(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2,
			Exception arg3) {
		// TODO Auto-generated method stub
		System.out.println("get Exception!");
		if(arg3 instanceof SessionInvalidatedException){
			System.out.println(arg3.getMessage());
			return new ModelAndView("about/login.html");
		}
		return null;
	}
	*/
	@ResponseBody
	@ExceptionHandler(NestedServletException.class)
	public ResponseData NestedServletException(NestedServletException e){
		ResponseData ret = new ResponseData();
		ret.setCode(500);
		System.out.println("500 c p");
		System.out.println(e.getMessage());
		return ret;
	}
}
