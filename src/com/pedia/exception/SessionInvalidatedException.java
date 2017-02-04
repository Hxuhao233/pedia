package com.pedia.exception;


public class SessionInvalidatedException extends RuntimeException{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String errMsg;
	public SessionInvalidatedException(){
		super();
	}
	public SessionInvalidatedException(String err){
		super(err);
		errMsg = err;
	}
	public String getErrMsg() {
		return errMsg;
	}
	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}
	
}
