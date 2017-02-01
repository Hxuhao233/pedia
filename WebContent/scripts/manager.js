var InitUrl = "../../Pedia/manager/init";
var seeEntryUrl = "../../Pedia/entry/seeEntry";
var checkEntryUrl = "../../Pedia/manager/checkEntry";
var checkReportedEntryUrl = "../../Pedia/manager/checkReportedEntry";
var checkModifiedEntryUrl = "../../Pedia/manager/checkModifiedEntry";
var logoutUrl = "../../Pedia/user/logout";


//初始化函数
$(function(){
    $("#goback").click(function(){
        document.getElementById('light').style.display='none';
        document.getElementById('fade').style.display='none';
    });
    $("#usernameController").hover(function(){
        $("#drop").attr("src","../images/manager/u896_hover.png")
    })
});
function seeEntry(eidNum,aidNum){
    $.ajax({
            // JSON.stringify(sendData)                                                 //使用post方法向服务器传送json字符串
            type:"GET",
            url:seeEntryUrl,
            contentType:"application/json;charset=utf-8",
            data:"eid="+eidNum+"&aid="+aidNum,
            dataType:"json",
            cache:false,
            success:function(data){              //请求成功后的返回函数
                var jsonData = data;
                var dataCode = jsonData.code;
                var dataKey = jsonData.data;
                if (dataCode==200) {
                    console.log(dataKey);
                    $("#lemma").text(dataKey.entryName);
                    $("#labels1").text(dataKey.label1);
                    $("#labels2").text(dataKey.label2);
                    $("#labels3").text(dataKey.label3);
                    $("#labels4").text(dataKey.label4);
                    if(dataKey.pictureAddr!=null){
                        $("#starboard img").attr("src","../../static/images/"+dataKey.pictureAddr);
                    }
                    else{
                        $("#starboard img").attr("src","../images/manager/exam.png");
                    }

                    $("#textAreas").text(dataKey.detail);
                }
            },
            error:function(data){                          //请求失败时调用此函数
                alert("error:" + data);
            }
        });
}
/************************table1**************/
function btn1ActionListener(){
    $("#btn1").unbind("click");
    $("#btn1").click(function(){
        var sendData = {
            "data":
                {
                    "eid":$(this).attr("eid"),
                    "aid":$(this).attr("aid"),
                    "allow":"1",
                    "reason":""
                }
        }
        var RowNum = $(this).attr("myAttrs");
        //alert(RowNum);
        //delRow(RowNum);
        //$("#goback").click();F
        $.ajax({                                                 //使用post方法向服务器传送json字符串
            type:"POST",
            url:checkEntryUrl,
            contentType:"application/json;charset=utf-8",
            data:JSON.stringify(sendData),
            dataType:"json",
            cache:false,
            success:function(data){              //请求成功后的返回函数
                var jsonData = data;
                var dataCode = jsonData.code;
                var oneMessage = new Array();
                if (dataCode==200) {
                    delRow(RowNum);
                    $("#goback").click();
                }
            },
            error:function(data){                          //请求失败时调用此函数
                alert("error:" + data);
            }
        });
    })
}

//未加驳回理由
function btn2ActionListener(){
    $("#btn2").unbind("click");
    $("#btn2").click(function(){

        var sendData = {
            "data":
                {
                    "eid":$(this).attr("eid"),
                    "aid":$(this).attr("aid"),
                    "allow":"0",
                    "reason":""
                }
        }
        var RowNum = $(this).attr("myAttrs");
        $.ajax({                                                 //使用post方法向服务器传送json字符串
        type:"POST",
        url:checkEntryUrl,
        contentType:"application/json;charset=utf-8",
        data:JSON.stringify(sendData),
        dataType:"json",
        cache:false,
        success:function(data){              //请求成功后的返回函数
            var jsonData = data;
            var dataCode = jsonData.code;
            var oneMessage = new Array();
            if (dataCode==200) {

                delRow(RowNum);
                $("#goback").click();
            }
        },
        error:function(data){                          //请求失败时调用此函数
            alert("error:" + data);
        }
    });
    })
}
//----删除某一行-----
function delRow(r){
    document.getElementById('table1').deleteRow(r);
}
//----删除某一行-----
//----新增信息的插入方法-----
function insertInfo(arr){
    //根据id获取表单信息
    var x = document.getElementById('table1').insertRow(1); //获取第一行对象
    var count = 0;
    for(var i in arr){
        x.insertCell(count).innerHTML = arr[i];
        count++;
    }
}
/*****************table2****************************/
//删除按钮
function btn1ActionListenerTable2(){
    $("#btn1").unbind("click");
    $("#btn1").click(function(){
        // alert($(this).attr("myAttrs"));
        // alert("hhhh");
        // delRowTable2($(this).attr("myAttrs"));
        var RowNum = $(this).attr("myAttrs");
        var eid = $(this).attr("eid");
        var rid = $(this).attr("rid");

        //alert(eid);
        $.ajax({                                                 //使用post方法向服务器传送json字符串
        type:"GET",
        url:checkReportedEntryUrl,
        contentType:"application/json;charset=utf-8",
        data:"eid="+eid+"&rid="+rid+"&allow=1",
        dataType:"json",
        cache:false,
        success:function(data){              //请求成功后的返回函数

            var jsonData = data;
            var dataCode = jsonData.code;
            var oneMessage = new Array();
            if (dataCode==200) {
                //alert("success");
                delRowTable2(RowNum);
                $("#goback").click();
            }
        },
        error:function(data){                          //请求失败时调用此函数
            alert("error:" + data);
        }
    });
    })
}
//驳回
function btn2ActionListenerTable2(){
    $("#btn2").unbind("click");
    $("#btn2").click(function(){
        var RowNum = $(this).attr("myAttrs");
        var eid = $(this).attr("eid").toString();
        var rid = $(this).attr("rid").toString();
        $.ajax({                                                 //使用post方法向服务器传送json字符串
        type:"GET",
        url:checkReportedEntryUrl,
        contentType:"application/json;charset=utf-8",
        data:"eid="+eid+"&rid="+rid+"&allow=0",
        dataType:"json",
        cache:false,
        success:function(data){              //请求成功后的返回函数
            var jsonData = data;
            var dataCode = jsonData.code;
            var oneMessage = new Array();
            if (dataCode==200) {

                delRowTable2(RowNum);
                $("#goback").click();
            }
        },
        error:function(data){                          //请求失败时调用此函数
            alert("error:" + data);
        }
    });
    })
}
//----删除某一行-----
function delRowTable2(r){
    document.getElementById('table2').deleteRow(r);
}
//----删除某一行-----
//----新增信息的插入方法-----
function insertInfoTable2(arr){
    //根据id获取表单信息
    var x = document.getElementById('table2').insertRow(1); //获取第一行对象
    var count = 0;
    for(var i in arr){
        x.insertCell(count).innerHTML = arr[i];
        count++;
    }
}
/*****************table3****************************/
//通过
function btn1ActionListenerTable3(){
        $("#btn1").unbind("click");
     $("#btn1").click(function(){
        var sendData = {
            "data":
                {
            		"eid":$(this).attr("eid").toString(),
                    "aid":$(this).attr("aid").toString(),
                    "allow":"1",
                    "reason":""
                }
        }
        console.log($(this).attr("eid"));
        var RowNum = $(this).attr("myAttrs");
        $.ajax({                                                 //使用post方法向服务器传送json字符串
            type:"POST",
            url:checkModifiedEntryUrl,
            contentType:"application/json;charset=utf-8",
            data:JSON.stringify(sendData),
            dataType:"json",
            cache:false,
            success:function(data){              //请求成功后的返回函数
                var jsonData = data;
                var dataCode = jsonData.code;
                var oneMessage = new Array();
                if (dataCode==200) {
                    delRowTable3(RowNum);
                    $("#goback").click();
                }
            },
            error:function(data){                          //请求失败时调用此函数
                alert("error:" + data);
            }
        });
    })
}
//驳回
function btn2ActionListenerTable3(){
    $("#btn2").unbind("click");
    $("#btn2").click(function(){
        var sendData = {
            "data":
                {
                    "eid":$(this).attr("eid").toString(),
                    "aid":$(this).attr("aid").toString(),
                    "allow":"0",
                    "reason":""
                }
        }
        var RowNum = $(this).attr("myAttrs");
        $.ajax({                                                 //使用post方法向服务器传送json字符串
        type:"POST",
        url:checkModifiedEntryUrl,
        contentType:"application/json;charset=utf-8",
        data:JSON.stringify(sendData),
        dataType:"json",
        cache:false,
        success:function(data){              //请求成功后的返回函数
            var jsonData = data;
            var dataCode = jsonData.code;
            var oneMessage = new Array();
            if (dataCode==200) {

                delRowTable3(RowNum);
                $("#goback").click();
            }
        },
        error:function(data){                          //请求失败时调用此函数
            alert("error:" + data);
        }
    });
    })
}
//----删除某一行-----
function delRowTable3(r){
    document.getElementById('table3').deleteRow(r);
}
//----删除某一行-----
//----新增信息的插入方法-----
function insertInfoTable3(arr){
    //根据id获取表单信息
    var x = document.getElementById('table3').insertRow(1); //获取第一行对象
    var count = 0;
    for(var i in arr){
        x.insertCell(count).innerHTML = arr[i];
        count++;
    }
}
/******************************************************/

//----获取行号-----
function getRow(r){
    var i=r.parentNode.parentNode.rowIndex;
    return i ;
}
$(function(){
    var usernameController = $("#usernameController");
    var dropFrame = $("#dropFrame");
    usernameController.mouseenter(function(){
        // t_delay= setTimeout(function(){
        //     dropFrame.fadeIn("slow");
        // },200);
        dropFrame[0].style.display="block";
    });
    // usernameController.mouseleave(function(){
    //     // clearTimeout(t_delay);
    //     // dropFrame.fadeOut("slow");
    //     dropFrame[0].style.display="none";
    // });
    dropFrame.mouseleave(function(){
        dropFrame[0].style.display="none";
    });
    $("#Picture").click(function(){

    });
    $("#revoke").click(function(){
        $.ajax({                                                 //使用post方法向服务器传送json字符串
        type:"GET",
        url:logoutUrl,
        contentType:"application/json;charset=utf-8",
        dataType:"json",
        cache:false,
        success:function(data){              //请求成功后的返回函数
            var jsonData = data;
            var dataCode = jsonData.code;
            console.log(jsonData);
            if (dataCode==200) {
                window.location.href=encodeURI("login.html");
            }
        },
        error:function(data){                          //请求失败时调用此函数
            alert("error:" + data);
        }
        })
    })
});
//初始化ajax
$(function(){
    // JSON.stringify(getData())
    $.ajax({                                                 //使用post方法向服务器传送json字符串
        type:"GET",
        url:InitUrl,
        contentType:"application/json;charset=utf-8",
        dataType:"json",
        cache:false,
        success:function(data){              //请求成功后的返回函数
            //console.log(data);
            var jsonData = data;
            var dataCode = jsonData.code;
            var dataKey = jsonData.data;
            var oneMessage = new Array();
            if (dataCode==200) {
                //console.log(dataKey);
                // dataKey.uncheckedEntryList.length
                $("#username").text(dataKey.userName);
                for (var i = 0; i < dataKey.uncheckedEntryList.length; i++) {
                    //alert("ads");
                    oneMessage[0]=dataKey.uncheckedEntryList[i].entryName;
                    oneMessage[1]=dataKey.uncheckedEntryList[i].createTime;
                    oneMessage[2]=dataKey.uncheckedEntryList[i].publisher;
                    oneMessage[3]="<div eid="+dataKey.uncheckedEntryList[i].eid+ " aid=" + dataKey.uncheckedEntryList[i].aid + "></div>";
                    //dataKey.uncheckedEntryList[i].entryId;
                    insertInfo(oneMessage);
                }
                for (var i = 0; i < dataKey.reportedEntryList.length; i++) {
                    //alert("ads");
                    oneMessage[0]=dataKey.reportedEntryList[i].entryName;
                    oneMessage[1]=dataKey.reportedEntryList[i].reported;
                    oneMessage[2]=dataKey.reportedEntryList[i].reason;
                    oneMessage[3]="<div eid='"+dataKey.reportedEntryList[i].eid+"' "+"rid='"+dataKey.reportedEntryList[i].rid+"'></div>";
                    //alert(oneMessage[3]);
                    //dataKey.reportedEntryList[i].entryId;
                    insertInfoTable2(oneMessage);
                }
                for (var i = 0; i < dataKey.modifiedEntryList.length; i++) {
                    //alert("ads");
                    oneMessage[0]=dataKey.modifiedEntryList[i].entryName;
                    oneMessage[1]=dataKey.modifiedEntryList[i].modifyTime;
                    oneMessage[2]=dataKey.modifiedEntryList[i].modifier;
                    oneMessage[3]="<div eid='"+dataKey.modifiedEntryList[i].eid+"' " +"aid='" +dataKey.modifiedEntryList[i].aid+"'></div>";
                    //dataKey.modifiedEntryList[i].entryId;
                    insertInfoTable3(oneMessage);
                }
                $("#table1 div").click(function(){
                    var eid = $(this).attr("eid");
                    var aid = $(this).attr("aid");
                    $("#btn1").attr("eid",eid);
                    $("#btn1").attr("aid",aid);
                    $("#btn1").attr("myAttrs",getRow(this));
                    $("#btn2").attr("eid",eid);
                    $("#btn2").attr("aid",aid);
                    $("#btn2").attr("myAttrs",getRow(this));
                    seeEntry(eid,aid);
                    document.getElementById('light').style.display='block';
                    document.getElementById('fade').style.display='block';
                    btn1ActionListener();
                    btn2ActionListener();
                });
                $("#table2 div").click(function(){
                    var eid = $(this).attr("eid");
                    var rid = $(this).attr("rid");
                    $("#btn1").attr("eid",eid);
                    $("#btn1").attr("myAttrs",getRow(this));
                    $("#btn1").attr("rid",rid);
                    $("#btn2").attr("eid",eid);
                    $("#btn2").attr("myAttrs",getRow(this));
                    $("#btn2").attr("rid",rid);
                    seeEntry(eid,aid);
                    document.getElementById('light').style.display='block';
                    document.getElementById('fade').style.display='block';
                    //alert("h");
                    btn1ActionListenerTable2();
                    btn2ActionListenerTable2();
                });
                $("#table3 div").click(function(){
                    var eid = $(this).attr("eid");
                    var aid = $(this).attr("aid");
                    $("#btn1").attr("eid",eid);
                    $("#btn1").attr("myAttrs",getRow(this));
                    $("#btn1").attr("aid",aid);
                    $("#btn2").attr("eid",eid);
                    $("#btn2").attr("myAttrs",getRow(this));
                    $("#btn2").attr("aid",aid);
                    seeEntry(eid,aid);
                    document.getElementById('light').style.display='block';
                    document.getElementById('fade').style.display='block';
                    btn1ActionListenerTable3();
                    btn2ActionListenerTable3();
                });
            }
        },
        error:function(data){                          //请求失败时调用此函数
            alert("error:" + data);
        }
    });
});


















$(function(){
    $("#BackHome").click(function(){
        window.location.href="../index.html";
    })
})
//弹出框的关闭按钮
function Close(){
    document.getElementById('light').style.display='none';
    document.getElementById('fade').style.display='none';
}

window.onload=function(){
    Cmd(1);
}
function Cmd(v){
    var button1 = document.getElementById("button1");
    var button2 = document.getElementById("button2");
    var button3 = document.getElementById("button3");
    var table1 = document.getElementById("table1");
    var table2 = document.getElementById("table2");
    var table3 = document.getElementById("table3");
    switch(v){
      case 1:
        button1.style.backgroundImage = "url(../images/manager/navibar.png)";
        button2.style.backgroundImage = "";
        button3.style.backgroundImage = "";
        table1.style.display = "";
        table2.style.display = "none";
        table3.style.display = "none";
        break;
      case 2:
        button1.style.backgroundImage = "";
        button2.style.backgroundImage = "url(../images/manager/navibar.png)";
        button3.style.backgroundImage = "";
        table1.style.display = "none";
        table2.style.display = "";
        table3.style.display = "none";
        break;
      case 3:
        button1.style.backgroundImage = "";
        button2.style.backgroundImage = "";
        button3.style.backgroundImage = "url(../images/manager/navibar.png)";
        table1.style.display = "none";
        table2.style.display = "none";
        table3.style.display = "";
        break;
    }
}