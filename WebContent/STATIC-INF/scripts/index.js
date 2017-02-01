var $sun = $("#sun");
var $cloud = $("#cloud");
function happyCat() {
  $(".eyelid").css("bottom");
  $(".eyelid").css("bottom", "120%");
  $(".mouth").css("border-radius");
  $(".mouth").css("border-radius", "0 0 90% 90%");
  return true;
}
function sadCat() {
  $(".eyelid").css("bottom");
  $(".eyelid").css("bottom", "60%");
  $(".mouth").css("border-radius");
  $(".mouth").css("border-radius", "90% 90% 0 0");
  return true;
}
function addSun() {
  $sun.addClass("sun");
  $sun.css({
    "left": "30%",
    "top": "100%"
  });
  $sun.css("left");
  $sun.css("top");
  $sun.css({
    "left": "57%",
    "top": "13%"
  });
  return true
};
function addCloud() {
  $cloud.addClass("cloud");
  $cloud.css("left", "-35%");
  $cloud.css("left");
  $cloud.css("left", "34%");
  $("body").css("background-color", "#2c3e50");
  $(".cat, .eyelid").css({
    "background-color": " #D06C1F",
    "border-bottom-color": "#D06C1F"
  });
  sadCat();
  return true;
}
function removeCloud() {
  $cloud.css("left");
  $cloud.css("left", "-35%");
  $("body").css("background-color", "#469CCE");
  $(".cat, .eyelid").css({
    "background-color": "#E78336",
    "border-bottom-color": "#E78336"
  });
  happyCat();
  return true;
}
$(document).ready(function() {
  addSun();
  $(".cat").mouseenter(function() {
    sadCat();
  });
  $(".cat").mouseleave(function() {
    happyCat();
  });
  $('input[type="checkbox"]').click(function() {
    if ($(this).is(":checked")) {
      addCloud();
    } else if ($(this).is(":not(:checked)")) {
      removeCloud();
    }
  });
});
$(function(){              
   var userName="null";
   var tag="?";
   var url=decodeURI(location.href);
   if(url.indexOf(tag)!=-1){
      var tmp=url.split("?")[1];
      if(tmp!=""){
      userName=tmp.split("=")[1];
      if(userName!="null"){
         $("#about").empty().append('<p class="user">'+userName+'</p>');  //如果用户是已登录状态，右上角显示用户名
       }
      }
   }
   if(userName!="null"){
      $("#about").delegate(".user","click",function(){      //使用 delegate() 方法向尚未创建的元素添加事件处理程序
        window.location.href="about/self.html?username="+encodeURI(userName);
      });
   }
   $("#entry").click(function(){
    if ($("#keyword").val()!="") {
    searchParam = $("#keyword").val().replace(/\+/g,'%2B');
    //alert(encodeURIComponent($("#keyword").val()));
	$.ajax({                                                 
        type:"GET",
        url:"../../Pedia/entry/enterEntryDirectly", // 此处加入url地址
        contentType:"application/json;charset=utf-8",
        data:"entryName=" + searchParam,
        dataType:"json",
        cache:false,
        success:function(data){              
        var jsonData = data;

        // 一级json
        var code = jsonData.code; // 返回码
        var listData = jsonData.data; // 数据

        if (code == "200") {
        	console.log("检索成功!");
        	window.location.href="about/readDetail.html?user="+encodeURI(userName)+"&search="+ searchParam;
        } else if (code == "404"){
        	console.log("没有此数据!");
        	window.location.href="about/readList.html?user="+encodeURI(userName)+"&search="+ searchParam;
        	return;
        } else {
        	console.log("未知错误!");
        	return;
        }
    },
    error:function(data){                          //请求失败时调用此函数
        console.log("Error");
    }
    });
      //window.location.href="about/readDetail.html"+"?"+"user="+ encodeURI(userName) + "&search=" + encodeURI($("#keyword").val());
    }
  });
  $("#search").click(function(){
    if ($("#keyword").val()!="") {
   
    	window.location.href="about/readList.html"+"?"+"user="+encodeURI(userName)+"&search="+ encodeURI($("#keyword").val());
    }
  });
  $("#create").click(function(){
        window.location.href="about/createLemma.html"+"?"+"username="+encodeURI(userName);
  });
});