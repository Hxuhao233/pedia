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
        window.location.href=encodeURI("about/self.html"+"?"+"username="+userName);
      });
   }
   $("#entry").click(function(){
    if ($("#keyword").val()!="") {
     	alert(encodeURI($("#keyword").val()));
      window.location.href="about/readDetail.html"+"?"+"user="+userName+"&search="+ encodeURI(encodeURI($("#keyword").val()));
    }
  });
  $("#search").click(function(){
    if ($("#keyword").val()!="") {
   
    	window.location.href="about/readList.html"+"?"+"user="+userName+"&search="+ encodeURI(encodeURI($("#keyword").val()));
    }
  });
  $("#create").click(function(){
        window.location.href=encodeURI("about/createLemma.html"+"?"+"username="+userName);
  });
});