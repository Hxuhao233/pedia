var lemmaName;
var tags=new Array();
var detail;
var username="null";
var photoURL;
$(function(){              //如果用户是已登录状态，记录用户名
   var tag="&";
   var url=decodeURI(location.href);
   if(url.indexOf(tag)==-1){
      var tmp=url.split("?")[1];
      if(tmp!=""){
      username=tmp.split("=")[1];
      }
      $("#stage0").click(function(){
        window.location.href=encodeURI("../index.html"+"?"+"username="+username);
      });
   }else{
    $("#stage0").click(function(){
       window.location.href=encodeURI("readDetail.html"+"?"+"username="+username);
      });
      var tmp1=url.split("?")[1];
      var tmp2=tmp1.split("&")[0];
      var tmp3=tmp1.split("&")[1];
      username=tmp2.split("=")[1];
      var lemmaId=tmp3.split("=")[1];
       $("#lemmaId").val(lemmaId);          //设置step3的lemmId的值，标识这是修改词条而不是创建
      $("#step1").css("visibility","hidden");
      $("#step2").css("visibility","visible");
      $("#stage2").find("a").removeClass("list_default").addClass("list_focus")
                  .parent().siblings().find("a").removeClass("list_focus").addClass("list_default");
      $("#pre").css("display","none");
      $("#next").css({"left":"-270px","top":"190px"});
      retriveMsg(lemmaId);
   }
});

function retriveMsg(lemId){                       //修改词条时显示给用户的信息
  var ID=lemId;
  $.ajax({
      type:"GET",
      url:"../../Pedia/entry/enterEntry", // 此处加入url地址
      contentType:"application/json;charset=utf-8",
      data:"eid=" + ID,
      dataType:"json",
      cache:false,
      async:false,
      success:function(result){
         lemmaName=result.data.entryName;
         $("#lemmaName>input").val(result.data.entryName);
         var temp=$(".tag");
         $(temp[0]).val(result.data.label1);
         $(temp[1]).val(result.data.label2);
         $(temp[2]).val(result.data.label3);
         $(temp[3]).val(result.data.label4);
         var i=0;
         temp.each(function(index,value){
              if($(temp[index]).val()!=""){
                tags[i]=temp[index];
                i++;
              }
          });
         $("#text").val(result.data.detail);
         var url="../../static/images/"+result.data.pic;
         $("#addPhoto").find("img").attr("src",url);   //将图片路径存入src中，显示出图片
          photoURL=url;
      },
      error:function(jqXHR,textStatus,errorThrown){                          //请求失败时调用此函数
          console.log("发生错误：" + jqXHR.status);  
          console.log(jqXHR.readyState);
          console.log(textStatus);
      }
  });
}

$(function(){
	$("#stage1").find("a").removeClass("list_default").addClass("list_focus");
    $("#stage0").hover(function(){                //为返回按钮添加鼠标悬浮效果
      $(this).find("a").removeClass("goback_default").addClass("goback_hover");
    },function(){
      $(this).find("a").removeClass("goback_hover").addClass("goback_default");
    });
});

$(function(){
    $("#scr1").click(function(){
       if(validateLemma()){                   //调用validateLemma()函数进行词条名验证
       	  var temp=$(".tag");
       	  var i=0;
       	  tags=[];                           //清空数组
       	  temp.each(function(index,value){
              if($(temp[index]).val()!=""){
              	tags[i]=temp[index];
                //alert($(tags[i]).val());
              	i++;
              }
       	  });
          $("#step1").css("visibility","hidden");
          $("#step2").css("visibility","visible");
          $("#stage2").find("a").removeClass("list_default").addClass("list_focus")
          .parent().siblings().find("a").removeClass("list_focus").addClass("list_default");
       }
    });
});
function validateLemma(){                                    //对词条名进行验证
	lemmaName=$("#lemmaName>input").val();
    $("#step1").find(".errorTips").remove();
    $("#step1").find(".hyperLink").remove();
    if (lemmaName=="") {
    	var errorMsg="请输入词条名";
        $("#step1").append('<span class="errorTips tip1">'+errorMsg+'</span>');
        return false;
    }else{
    	var dec=0;
        $.ajax({                                                 //使用post方法向服务器传送json字符串
            //type:"GET",
            async:false,                                       //取消异步操作
            url:"../../Pedia/entry/checkEntryCreatable",
         //   contentType:"application/json;charset=utf-8",
            data:{entryName:lemmaName},     //向服务器传送词条名进行验证
             dataType: "json",
            success:function(text,textStatus){              //请求成功后的返回函数
             if(text.code=="200"){
                dec=1;
             }else if(text.code=="203"){
                $("#step1").append('<span class="errorTips tip2">'+text.data.info+'</span><a class="hyperLink" href="javascript:void(0)">去看看</a>');     //词条名已存在时向用户提供错误信息
             }
            },
            error:function(jqXHR,textStatus,errorThrown){                          //请求失败时调用此函数
                alert("发生错误：" + jqXHR.status);  
                alert(jqXHR.readyState);
                alert(textStatus);
            }
        });
        if(dec==1){
        	return true;
        }
        return false;
    }
}
$(function(){
  $("#step1").delegate(".hyperLink","click",function(){      //使用 delegate() 方法向尚未创建的元素添加事件处理程序
  	 lemmaName=$("#lemmaName>input").val();
  	 if (lemmaName!="") {
      window.location.href=encodeURI("readDetail.html"+"?"+"user="+username+"&search="+lemmaName);
    }
  });
});

$(function(){
    $("#pre").click(function(){          //设置创建词条和填写正文两个图标之间的转换
       $("#step2").css("visibility","hidden");
       $("#step1").css("visibility","visible");
       $("#stage1").find("a").removeClass("list_default").addClass("list_focus")
       .parent().siblings().find("a").removeClass("list_focus").addClass("list_default");
    });
});
$(function(){
    $("#addPhoto").click(function(){             //点击上传词条图片
       $("#upload").click();                     //隐藏了input:file的样式后点击addphoto按钮也可上传图片
       $("#upload").on("change",function(){
       	var objUrl=getObjectURL(this.files[0]);//获取图片的路径，该路径不是图片在本地的路径
       	if(objUrl){
       		$("#addPhoto").find("img").attr("src",objUrl);   //将图片路径存入src中，显示出图片
       		photoURL=objUrl;
       	}
       });
    });
});
function getObjectURL(file){  //建立一个可存取到该file的url
	var url=null;
	if(window.createObjectURL!=undefined){
		url=createObjectURL(file);
	}else if(window.URL!=undefined){   //mozilla(firefox)
		url=window.URL.createObjectURL(file);
	}else if(window.webkitURL!=undefined){   //webkit or chrome
		url=window.webkitURL.createObjectURL(file);
	}
	return url;
}

$(function(){
    $("#next").click(function(){
       if(validateText()){
       	  $("#lemma")[0].value=lemmaName;
          setTags();
          $(".textArea").text("\v\v"+detail);
          $("#realText")[0].value=detail;
          $("#step2").css("visibility","hidden");
          $("#step3").css("visibility","visible");
          $("#stage3").find("a").removeClass("list_default").addClass("list_focus")
          .parent().siblings().find("a").removeClass("list_focus").addClass("list_default");
       }
    });
});

function validateText(){
	detail=$("#text").val();
	$("#step2").find(".errorTips").remove();          //将以前的提醒元素清除
	if(detail!=""){
		return true;
	}else{
		var errorMsg="正文不能为空";
		$("#step2").append('<span class="errorTips tip3">'+errorMsg+'</span>');
		return false;
	}
}

function setTags(){
	$("#tags").find("p").remove();
	for(var j=0;j<$(".tally").length;j++){          //将更改前的标签清除
		var tag=$(".tally").eq(j);
		tag[0].value="";
		tag.css("display","none");
	}
	var num=tags.length;
	//alert(num);
	if (num==0) {
		$("#tags").append('<p class="blank">'+"暂无标签"+'</p>');
	}else{
		for(var i=0;i<num;i++){
			var target=$(".tally").eq(i);
			target[0].value=tags[i].value;
			target.css("display","inline-block");
		}
	}
}

$(function(){     //一旦填写正文区域获得焦点便将提醒元素清除
	$("#text").focus(function(){
		$("#step2").find(".errorTips").remove();
	})
})
$(function(){
    $("#prev").click(function(){
    	//添加有关词条以及其他信息的参数传递事件
       $("#step3").css("visibility","hidden");
       $("#step2").css("visibility","visible");
       $("#stage2").find("a").removeClass("list_default").addClass("list_focus")
       .parent().siblings().find("a").removeClass("list_focus").addClass("list_default");
    });
});
$(function(){
	$("#submit").click(function(){
		upload();
	})
})
function upload(){  
    var form = new FormData($("#upload_form")[0]);  
    $.ajax({  
      url:'../../Pedia/entry/createEntry',  
      type:'POST', 
      dataType:'json',
      processData: false,
      contentType: false,
      data:form,  
      success:function (result){  
        if(result.code=="200"){
            window.location.href=encodeURI("createDone.html"+"?"+"username="+username);
        }else if(result.code=="203"){
        	   window.location.href=encodeURI("createFail.html"+"?"+"msg="+result.data.info+"&username="+username);
        }  
      },  
      error:function (result){  
        console.log(result);  
      }  
   });  
}