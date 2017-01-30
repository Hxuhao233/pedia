var user; // 保存用户信息，是否有用户存在
var loginOrNot = false; // 记录是否有登陆
var searchData = ""; // 用于保存search参数

// 扩展一个获取url参数的方法
(function ($) {
  $.getUrlParam = function (name) {
   var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
   var r = window.location.search.substr(1).match(reg);
   if (r != null) {
   		return r[2];		// 去掉unescape
   }
   return null;
  };
 })(jQuery);

function toDetail(entryID) {
	var ID = entryID;
	window.location.href = "readDetail.html?search=" + searchData + "&eid=" + ID + "&user=" + user; // 跳转
}

function validNotEmpty() {
	if ($("input[name='search']").val() == "") {	
		$("input[name='search']").attr("placeholder", "请不要空搜索！");
		console.log("Empty Search!");
		return false;
	}
	return true;
}

function clear() {
	// 清空上一次搜索结果
	$("#tableList").empty();
}

function createListHtml(listData) {
	var data = listData; // 储存传入的数据
	$("#tableList").append(
		"<div class='list'>" + 					
			"<div class='listHeader'>" +
				"<a href='##' onclick=\"toDetail("+ data.eid +")\" class='listTitle'>" + data.entryName + "</a>" +
				"<p class='listCreate'>" +
					"<span class='createName'>" + data.createName + "&nbsp&nbsp</span>" +
					"<span class='createDate'>" + data.createDate + "</span>" +
				"</p>" +
			"</div>" +
			"<img src='" + "../../static/images/" + data.pictureAddr + "' alt='词条图片' class='listPic' />" +
			"<p class='listDetail'>" + data.entryContent + "</p>" +
			"<p class='listLabels'>" +
				"<span class='listLabel0'>标签：&nbsp</span>" +
				"&nbsp&nbsp<span class='listLabel1'>" + data.label1 + "</span>" +
				"&nbsp&nbsp<span class='listLabel2'>" + data.label2 + "</span>" +
				"&nbsp&nbsp<span class='listLabel3'>" + data.label3 + "</span>" +
				"&nbsp&nbsp<span class='listLabel4'>" + data.label4 + "</span>" +
			"</p>" +
		"</div>"
	);
}

function createHeaderHtml(num) {
	console.log("CreateHeader");
	var search = "";
	if (searchData == "") {
		search = $("input[name='search']").val();
	} else {
		search = searchData;
	}
	$("#tableHeader").html(
		"<p>搜索<span id='listWord'>&nbsp" +
		decodeURI(search) +
		"&nbsp</span>共搜索到<span id='listNum'>&nbsp" +
		num +
		"&nbsp</span>条结果</p>"
	);
}

function search(searchData) {
	clear();
	$.ajax({                                                 
        type:"GET",
        url:"../../Pedia/back/queryEntry", // 此处加入url地址
        contentType:"application/json;charset=utf-8",
        data:"search=" + decodeURI(searchData),
        dataType:"json",
        cache:false,
        success:function(data){              
            var jsonData = data;

            // 一级json
            var code = jsonData.code; // 返回码
            var listData = jsonData.data; // 数据

            if (code == "200") {
            	console.log("检索成功!");

            	// 二级json
	            var listNum = listData.listNum; // lists条目数
	            var listsArray = listData.list; // lists数组

            	createHeaderHtml(listNum); // 创建Header

	            // 循环创建list列表
	            for (var i=0; i<listNum; i++) {
	            	createListHtml(listsArray[i]);
	            }

            } else if (code == "500"){
            	console.log("没有此数据!");
            	
            	createHeaderHtml(0); // 创建Header
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
}

$("Document").ready(function() {
	// 检测是否有登陆
	user = $.getUrlParam("user");
	if (user == "null") {
		console.log("未登录");
		loginOrNot = false;
		$("#headerRight").css("display", "block");
		$("#userCenter").css("display", "none");
	} else {
		console.log("登录:" + user);
		loginOrNot = true;
		$("#headerRight").css("display", "none");
		$("#userCenter").css("display", "block");
	}
	searchData = $.getUrlParam("search");
	search(searchData);
	
	$("#tapSearch").click(function() {
		if (validNotEmpty()) {
			// 如果不为空
			searchData = $("input[name='search']").val();
			search(searchData); // 进行Ajax
		}
	});
});