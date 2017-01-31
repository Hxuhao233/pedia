var searchParam = ""; // 用于保存搜索的参数，用于back
var user = ""; // 保存用户信息，是否有用户存在
var eID; // 用于储存解析来的eid
var praiseTimes; // 用于保存点赞数
var badTimes; // 用于保存差评数
var reportOrNot = false; // 用于保存是否已经举报过
var loginOrNot = false; // 记录是否有登陆

// 扩展一个获取url参数的方法
(function ($) {
  $.getUrlParam = function (name) {
   var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
   var r = window.location.search.substr(1).match(reg);
   if (r != null) {
   		return (r[2]);		//去掉unescape
   }
   return null;
  };
 })(jQuery);

function backToLast() {
	window.location.href = "readList.html?search=" + searchParam + "&user=" + user;
}

// 点赞操作
function priase() {
	$.ajax({
        type:"GET",
        url:"../../Pedia/entry/priase/" + eID, // 此处加入url地址
        contentType:"application/json;charset=utf-8",
        dataType:"json",
        cache:false,
        success:function(data){
            var jsonData = data;

            // 一级json
            var code = jsonData.code; // 返回码

            if (code == "200") {
            	console.log("点赞成功!");
				$("#great").find("img").attr("src", "../images/readDetail/up_Sel.png");
            	$("#greatNum").html(Number(praiseTimes) + 1);
            } else if (code == "500"){
            	alert(jsonData.data.info);
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

// 差评操作
function badReview() {
	$.ajax({
        type:"GET",
        url:"../../Pedia/entry/badReview/" + eID, // 此处加入url地址
        contentType:"application/json;charset=utf-8",
        dataType:"json",
        cache:false,
        success:function(data){
            var jsonData = data;

            var code = jsonData.code; // 返回码

            if (code == "200") {
            	console.log("差评成功!");
				$("#bad").find("img").attr("src", "../images/readDetail/down_Sel.png");
            	$("#badNum").html(Number(badTimes) + 1);
            } else if (code == "500"){
            	alert(jsonData.data.info);
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

// 举报相关
function getReport() {
	var json = {
		data: {
			report: $("input[name='reportReason']").val(),
			eid: eID
		}
	};

	return json;
}

function report() {
	$.ajax({
        type:"POST",
        url:"../../Pedia/entry/report", // 此处加入url地址
        contentType:"application/json;charset=utf-8",
        dataType:"json",
        data: JSON.stringify(getReport()),
        cache:false,
        success:function(data){
            var jsonData = data;

            var code = jsonData.code; // 返回码

            if (code == "200") {
            	console.log("举报成功!");
				$("#report").find("img").attr("src", "../images/readDetail/report_Sel.png");
				alert("感谢您的举报，我们将认真核实！");
            } else if (code == "500"){
            	alert("举报失败: " + jsonData.data.info);
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

// 评论操作相关函数
var addOrNot = false;
function addComments() {
	if (!addOrNot) {
		addOrNot = true;
		$("#addTables").css("display", "block");
		console.log("add Comments!");
	}
}

function commentUpdate() {
	// comment相关恢复原装
	$("#twoButton").css("display", "none");
	$("#addTableBotton").find("a").css("display", "block");
}

function commentCancel() {
	addOrNot = false;
	console.log("cancel Comments!");
	commentUpdate();
	$("#addTables").css("display", "none");
}

// 增加评论相关
function getComment() {
	var json = {
		data: {
			eid: eID,
			comment: $("input[name='commentAdd']").val(),
		}
	};

	return json;
}

function commentAddOne(commentData) {
	// 动态增加一条评论
	var commentHtml = "";
	commentHtml +=
	"<div class=\"content\">" +
		"<img src=\"" + "../../static/images/" + commentData.commenterPic + "\" alt=\"头像\" class=\"headPic\" />" +
		"<p class=\"nickName\">" + commentData.commenterName + "&nbsp&nbsp&nbsp<span class=\"date\">" + commentData.commentDate + "</span></p>" +
		"<div class=\"commentDiv\">" +
			"<p>" + commentData.commentDetail + "</p>" +
		"</div>" +
	"</div>";
}

function commentAdd() {
	if ($("input[name='commentAdd']").val() != "") {
		$.ajax({
            type:"POST",
            url:"../../Pedia/entry/comment", // 此处加入url地址
            contentType:"application/json;charset=utf-8",
            dataType:"json",
            data: JSON.stringify(getComment()),
            cache:false,
            success:function(data){
	            var jsonData = data;

	            var code = jsonData.code; // 返回码

	            if (code == "200") {
	            	console.log("评论成功!");
					addOrNot = false;
					commentUpdate();
					$("#addTables").css("display", "none");
	            	$("#commentTable").append(commentAddOne(jsonData.data.comment));
	            } else if (code == "500"){
	            	alert("评论失败");
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
	} else {
		alert("请不要空评论！");
	}

}

function loadComments(num, commentList) {
	var lists = commentList; // 保存comment列表数组
	var commentHtml = "";
	for (var i=0; i<num; i++) {
		commentHtml +=
			"<div class=\"content\">" +
				"<img src=\"" + "../../static/images/" + commentList[i].commenterPic + "\" alt=\"头像\" class=\"headPic\" />" +
				"<p class=\"nickName\">" + commentList[i].commenterName + "&nbsp&nbsp&nbsp<span class=\"date\">" + commentList[i].commentDate + "</span></p>" +
				"<div class=\"commentDiv\">" +
					"<p>" + commentList[i].commentDetail + "</p>" +
				"</div>" +
			"</div>";
	}
	return commentHtml; // 返回生成的Html字符串
}

function createHtml(data) {
	var loadData = data; // 保存jsonData
	praiseTimes = loadData.praiseTime; // 储存点赞数
	badTimes = loadData.badReviewTimes; // 保存差评数
	var Html = "";
	Html +=
			"<div id=\"describe\">" +
				"<a href=\"#\" onClick=\"backToLast()\" id=\"back\"></a>" +
				"<div id=\"tableTitle\">" +
					"<p id=\"title\">" + loadData.entryName + "</p>" +
					"<p id=\"label0\">标签：</p>" +
					"<p id=\"label1\">" + loadData.label1 + "</p>" +
					"<p id=\"label2\">" + loadData.label2 + "</p>" +
					"<p id=\"label3\">" + loadData.label3 + "</p>" +
					"<p id=\"label4\">" + loadData.label4 + "</p>" +
				"</div>" +
				"<div id=\"tableLike\">" +
					"<div id=\"great\">" +
						"<img src=\"../images/readDetail/up.png\" alt=\"点击\" />" +
						"<p>点赞&nbsp&nbsp<span id=\"greatNum\">" + loadData.praiseTime + "</span></p>" +
					"</div>" +
					"<div id=\"bad\">" +
						"<img src=\"../images/readDetail/down.png\" alt=\"点击\" />" +
						"<p>差评&nbsp&nbsp<span id=\"badNum\">" + loadData.badReviewTimes + "</span></p>" +
					"</div>" +
					"<div id=\"report\">" +
						"<img src=\"../images/readDetail/report.png\" alt=\"点击\" />" +
						"<p>举报</p>" +
					"</div>" +
				"</div>" +
				"<div id=\"tablePic\">" +
					"<img src=\"" + "../../static/images/"+ loadData.pic +"\" alt=\"预览图\" />" +
				"</div>" +
				"<div id=\"tableReport\">" +
					"<img src=\"../images/readDetail/end.png\" alt=\"关闭\" id=\"endPic\" />" +
					"<p>请填写举报原因：</p>" +
					"<input type=\"text\" id=\"reportReason\" name=\"reportReason\">" +
					"<img src=\"../images/readDetail/reportEnd.png\" alt=\"完成\" id=\"endReport\" />" +
				"</div>" +
			"</div>" +
			"<div id=\"details\">" +
				"<p id=\"detailsTitle\">正文</p>" +
				"<div id=\"mainDetail\">" +
					"<p id=\"titleDetail\">" + loadData.entryName + "</p>" +
					"<p id=\"textDetail\">" + loadData.detail + "</p>" +
				"</div>" +
			"</div>" +
			"<div id=\"comments\">" +
				"<a href=\"#\" id=\"chervon\"></a>" +
				"<p id=\"commentTitle\">评论区<span id=\"commentNumTitle\">（共<span id=\"commentNum\">" + loadData.commentsNum + "</span>条）</span></p>" +
				"<div id=\"addComments\">" +
					"<a href=\"##\" onclick=\"addComments()\" id=\"addTitle\">增加评论</a>" +
					"<img src=\"../images/readDetail/write.png\" alt=\"新建\" id=\"write\" />" +
				"</div>" +
				"<div id=\"addTables\">" +
					"<div id=\"addTable\">" +
						"<input type=\"text\" name=\"commentAdd\" />" +
					"</div>" +
					"<div id=\"twoButton\">" +
						"<a href=\"##\" onclick=\"commentAdd()\" id=\"Ok\">评论</a>" +
						"<a href=\"##\" onclick=\"commentCancel()\" id=\"cancel\">取消</a>" +
					"</div>" +
					"<div id=\"addTableBotton\">" +
						"<img src=\"../images/readDetail/flower.png\" alt=\"logo\" />" +
						"<a href=\"##\" onclick=\"commentCancel()\">取消</a>" +
					"</div>" +
				"</div>" +
				"<div id=\"commentTable\">" +
					loadComments(loadData.commentsNum, loadData.comments) +
				"</div>" +
			"</div>";
	return Html; // 返回生成的Html字符串
}

function clear() {
	// 清除上次加载
	$("#load").empty();
}

// 直接进入词条
function tapGoTo() {
	console.log("直接进入词条Ajax");
    searchParam = $("input[name='search']").val();
	$.ajax({
        type:"GET",
        url:"../../Pedia/entry/enterEntryDirectly", // 此处加入url地址
        contentType:"application/json;charset=utf-8",
        data:"entryName=" + searchParam,
        dataType:"json",
        cache:false,
        async:false,
        success:function(data){
            var jsonData = data;

            // 一级json
            var code = jsonData.code; // 返回码
            var loadData = jsonData.data; // 数据
            eID = loadData.eid; // 更新entryID

            if (code == "200") {
            	console.log("进入词条成功!");


            	// 创建Html
            	var HtmlStr = createHtml(loadData);
            	clear();
            	$("#load").append(HtmlStr);

            	if (loadData.label4 == "") {
            		$("#label4").addClass("null");
            	} else {
            		$("#label4").removeClass("null");
            	}
            	if (loadData.label3 == "") {
            		$("#label3").addClass("null");
            	} else {
            		$("#label3").removeClass("null");
            	}
            	if (loadData.label2 == "") {
            		$("#label2").addClass("null");
            	} else {
            		$("#label2").removeClass("null");
            	}
            	if (loadData.label1 == "") {
            		$("#label1").addClass("null");
            	} else {
            		$("#label1").removeClass("null");
            	}

            } else if (code == "404"){
            	alert("没有此词条！");
				window.location.href = "readList.html?search=" + searchParam + "&user=" + user;
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

// 跳到readList界面
function tapSearch() {
	var search = $("input[name='search']").val();
	window.location.href = "readList.html?search=" + search + "&user=" + user;
}

function loadHtml(entryID) {
	clear(); // 重置
	console.log("Ajax更新" + entryID);
	eID = entryID;
	$.ajax({
        type:"GET",
        url:"../../Pedia/entry/enterEntry", // 此处加入url地址
        contentType:"application/json;charset=utf-8",
        data:"eid=" + eID,
        dataType:"json",
        cache:false,
        async:false,
        success:function(data){
            var jsonData = data;

            // 一级json
            var code = jsonData.code; // 返回码
            var loadData = jsonData.data; // 数据

            if (code == "200") {
            	console.log("加载成功!");

            	// 创建Html
            	var HtmlStr = createHtml(loadData);
            	$("#load").append(HtmlStr);

            	if (loadData.label4 == "") {
            		$("#label4").addClass("null");
            	} else {
            		$("#label4").removeClass("null");
            	}
            	if (loadData.label3 == "") {
            		$("#label3").addClass("null");
            	} else {
            		$("#label3").removeClass("null");
            	}
            	if (loadData.label2 == "") {
            		$("#label2").addClass("null");
            	} else {
            		$("#label2").removeClass("null");
            	}
            	if (loadData.label1 == "") {
            		$("#label1").addClass("null");
            	} else {
            		$("#label1").removeClass("null");
            	}

            } else if (code == "500"){
            	console.log("没有此数据!");
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

	// 预加载
	eID = $.getUrlParam('eid'); // 获取entryID参数
	console.log("OnLoad: " + eID);
	if(eID == null){
		searchParam = $.getUrlParam('search'); // 获取search参数
		console.log("search: " + searchParam);
		var eidsss = getEid(searchParam);
		console.log("eid: " + eidsss);
		loadHtml(eidsss); //Ａjax加载
	}else{
		loadHtml(eID);
	}

	var H = $(document).height();
	console.log(H);
	$("body").height(H);

	// 以下为评论区动态
	$(".commentDiv").mouseover(function() {
		$(this).animate({
			width: "425px"
		}, 'fast');
	}).mouseout(function() {
		$(this).animate({
			width: "375px"
		}, 'fast');
	});

	$("#header img").mouseover(function() {
		$(this).rotate({
			animateTo: 90
		});
	}).mouseout(function() {
		$(this).rotate({
			animateTo: 0
		});
	});

	// 以下为点击编辑动态
	$("#edit").click(function() {
		console.log("click");
		$("#change").css("display", "block");
	});

	// 点赞
	$("#great").click(function() {
		console.log("Priase");
		priase(); // 点赞Ajax
	});

	// 差评
	$("#bad").click(function() {
		console.log("BadReview");
		badReview(); // 差评Ajax
	});

	// 举报
	$("#report").click(function() {
		if (!reportOrNot) {
			$("#tableReport").css("display", "block");
			$("#tablePic").css("display", "none");
		}
	});

	$("#endReport").click(function() {
		// 上传举报内容函数
		report(); // 举报Ajax
		$("#tableReport").css("display", "none");
		$("#tablePic").css("display", "block");
		reportOrNot = true;
	});

	$("#endPic").click(function() {
		$("#tableReport").css("display", "none");
		$("#tablePic").css("display", "block");
	});

	$("#addTable").find("input").click(function() {
		$("#twoButton").css("display", "block");
		$("#addTableBotton").find("a").css("display", "none");
	});

	$("#edit").find("img").click(function() {
		window.location.href = "createLemma.html?user=" + user + "&eid=" + eID;
	});
});

function getEid(search){
    var eidsss=null;
    $.ajax({
        type:"GET",
        url:"../../Pedia/entry/enterEntryDirectly", // 此处加入url地址
        contentType:"application/json;charset=utf-8",
        data:"entryName=" + decodeURI(search),
        dataType:"json",
        cache:false,
        async:false,
        success:function(data){
          console.log(data);
            var jsonData = data;
            // 一级json
            var code = jsonData.code; // 返回码
            var loadData = jsonData.data; // 数据
            //alert(code);
            if (code == "200") {
              //alert(loadData.eid);
              eidsss=loadData.eid;
            } else if (code == "404"){
              console.log("没有此数据!");
              return;
            } else {
              console.log("未知错误!");
              return;
            }
        },
        error:function(data){                          //请求失败时调用此函数
            console.log("getEidError");
            alert("333");
            return;
        }
    });
    return eidsss;
}