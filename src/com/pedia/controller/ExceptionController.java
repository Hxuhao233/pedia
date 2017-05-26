package com.pedia.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pedia.tool.ResponseData;

@ControllerAdvice
public class ExceptionController {
    //空指针异常
    @ExceptionHandler(NullPointerException.class)  
    @ResponseBody  
    public ResponseData nullPointerExceptionHandler(NullPointerException ex) {  
        ResponseData response = new ResponseData();
        response.setCode(500);
        Map<String,Object> data = new HashMap<>();
        data.put("info", "服务器炸了,等等在过来看看吧>_ （空指针异常)");
        data.put("reason",ex.getMessage());
        response.setData(data);
        return response;
    }   
}
