var getPersonalHomePageUrl = "../../Pedia/back/getPersonalHomePage";

var seeEntryUrl = "../../Pedia/back/seeEntry";
//这个url要特别注意一下，最后加斜线
var deleteEntryUrl= "../../Pedia/back/deleteEntry/";
var logoutUrl = "../../Pedia/back/logout";

//测试通过
function PopupActionListener(){
    //弹出框按钮
    $(".mytable a").click(function(){
        var entryId = $(this).attr("myAttr");
        seeEntry(entryId);
        document.getElementById('light').style.display='block';
        document.getElementById('fade').style.display='block';
    })
}

function seeEntry(eidNum){
    $.ajax({                                                 //使用post方法向服务器传送json字符串
            type:"GET",
            url:seeEntryUrl,
            contentType:"application/json;charset=utf-8",
            data:"eid="+eidNum,
            dataType:"json",
            cache:false,
            success:function(data){              //请求成功后的返回函数
                var jsonData = data;
                var dataCode = jsonData.code;
                var dataKey = jsonData.data;
                if (dataCode==200) {
                    console.log(dataKey);
                    $("#lemma").text(dataKey.entryName);
                    $("tags").html("<p>"+dataKey.label1+"</p>");
                    if(dataKey.pictureAddr!=null){
                        $("#starboard img").attr("src","../../static/images/"+dataKey.pictureAddr);
                    }
                    else{
                        $("#starboard img").attr("src","../images/manager/exam.png");
                    }
                    alert(dataKey.detail);
                    $("#textArea").text(dataKey.detail);
                }
            },
            error:function(data){                          //请求失败时调用此函数
                alert("error:" + data);
            }
        });
}

function deleteActionListener(){

    //删除按钮
    $("#table1 div").click(function(){
        var entryId = $(this).attr("myAttr");
        //alert(entryId);
        var RowNum = getRow(this);
        //delRow(RowNum);
        $.ajax({
            // JSON.stringify(sendData)                                                 //使用post方法向服务器传送json字符串
            type:"GET",
            url:deleteEntryUrl+entryId,
            contentType:"application/json;charset=utf-8",
            dataType:"json",
            cache:false,
            success:function(data){              //请求成功后的返回函数
                var jsonData = data;
                var dataCode = jsonData.code;
                //var oneMessage = new Array();
                if (dataCode==200) {
                    delRow(RowNum);
                }
            },
            error:function(data){                          //请求失败时调用此函数
                alert("error:" + data);
            }
        });
    })
    $("#table2 div").click(function(){
        // var RowNum = getRow(this);
        // delRowTable2(RowNum);
        var entryId = $(this).attr("myAttr");
        //alert(entryId);
        var RowNum = getRow(this);
        $.ajax({
            // JSON.stringify(sendData)                                                 //使用post方法向服务器传送json字符串
            type:"GET",
            url:deleteEntryUrl+entryId,
            contentType:"application/json;charset=utf-8",
            dataType:"json",
            cache:false,
            success:function(data){              //请求成功后的返回函数
                var jsonData = data;
                var dataCode = jsonData.code;
                //var oneMessage = new Array();
                if (dataCode==200) {
                    delRow(RowNum);
                }
            },
            error:function(data){                          //请求失败时调用此函数
                alert("error:" + data);
            }
        });
    })
    $("#table3 div").click(function(){
        // var RowNum = getRow(this);
        // delRowTable3(RowNum);
        var entryId = $(this).attr("myAttr");
        //alert(entryId);
        var RowNum = getRow(this);
        $.ajax({
            // JSON.stringify(sendData)                                                 //使用post方法向服务器传送json字符串
            type:"GET",
            url:deleteEntryUrl+entryId,
            contentType:"application/json;charset=utf-8",
            dataType:"json",
            cache:false,
            success:function(data){              //请求成功后的返回函数
                var jsonData = data;
                var dataCode = jsonData.code;
                //var oneMessage = new Array();
                if (dataCode==200) {
                    delRow(RowNum);
                }
            },
            error:function(data){                          //请求失败时调用此函数
                alert("error:" + data);
            }
        });
    })
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
    $("#changePW").click(function(){

    });
    $("#Picture").click(function(){

    });
    $("#changeMsg").click(function(){

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




//----获取行号-----
function getRow(r){
    var i=r.parentNode.parentNode.rowIndex;
    return i ;
}
//初始化ajax
$(function(){
    // $("#username").text("root");
    // $("#Degree").text("10");
    // $("#expNum").text("1700");
    // $("#hasPassNum").text("3");
    // $("#toPassNum").text("4");
    // $("#hasNotPassNum").text("5");
    // $("#reportNum").text("10");
    // $("#passRateNum").text("88%");
    // var oneMessage1 = new Array();
    // oneMessage1[0]="<a href='#' myAttr='"+"1"+"'>"+"java"+"</a>";
    // oneMessage1[1]="2017-22-07 02:22:08";
    // oneMessage1[2]="2017-22-07 02:22:08";
    // oneMessage1[3]="0";
    // oneMessage1[4]="<div myAttr='"+1+"'></div>";
    // //dataKey.hasPassList[i].entryId;
    // insertInfo(oneMessage1);

    // var oneMessage2 = new Array();
    // oneMessage2[0]="<a href='#' myAttr='"+"14"+"'>"+"用户222"+"</a>";
    // oneMessage2[1]="2017-09-05 17:09:50";
    // oneMessage2[2]="<div myAttr='"+14+"'></div>";
    // insertInfoTable2(oneMessage2);

    // var oneMessage3 = new Array();
    // oneMessage3[0]="<a href='#' myAttr='"+"1"+"'>"+"java"+"</a>";
    // oneMessage3[1]="2017-22-07 02:22:08";
    // oneMessage3[2]="不符合";
    // oneMessage3[3]="<div myAttr='"+1+"'></div>";
    // insertInfoTable3(oneMessage3);
    // deleteActionListener();
    // PopupActionListener();
    $.ajax({                                                 //使用post方法向服务器传送json字符串
        type:"GET",
        url:getPersonalHomePageUrl,
        contentType:"application/json;charset=utf-8",
        dataType:"json",
        cache:false,
        success:function(data){              //请求成功后的返回函数
            console.log(data);
            var jsonData = data;
            var dataCode = jsonData.code;
            var dataKey = jsonData.data;
            var oneMessage = new Array();
            if (dataCode==200) {
                //hasNotPassNum  level  hasPassNum  entriesNum  passRate
                //exp  toPassNum  hasPassList toPassList  hasNotPassList
                    //$("#username").text("root");
                    $("#username").text(dataKey.username);
                    $("#Degree").text(dataKey.level);
                    $("#expNum").text(dataKey.exp);
                    $("#hasPassNum").text(dataKey.hasPassNum);
                    $("#toPassNum").text(dataKey.toPassNum);
                    $("#hasNotPassNum").text(dataKey.hasNotPassNum);
                    $("#reportNum").text(dataKey.entriesNum);
                    $("#passRateNum").text(dataKey.passRate);
                    var oneMessage1 = new Array();
                    // oneMessage1[0]="<a href='#' myAttr='"+"1"+"'>"+"java"+"</a>";
                    // oneMessage1[1]="2017-22-07 02:22:08";
                    // oneMessage1[2]="2017-22-07 02:22:08";
                    // oneMessage1[3]="0";
                    // oneMessage1[4]="<div myAttr='"+1+"'></div>";
                    //dataKey.hasPassList[i].entryId;
                    //insertInfo(oneMessage1);
                    for (var i = 0; i < dataKey.hasPassList.length; i++) {
                        //alert("ads");
                        oneMessage1[0]="<a href='#' myAttr='"+dataKey.hasPassList[i].eid+"'>"+dataKey.hasPassList[i].entryName+"</a>";
                        oneMessage1[1]=dataKey.hasPassList[i].createDate;
                        oneMessage1[2]=dataKey.hasPassList[i].passDate;
                        oneMessage1[3]=dataKey.hasPassList[i].modifiTimes;
                        oneMessage1[4]="<div myAttr='"+dataKey.hasPassList[i].eid+"'></div>";
                        //dataKey.hasPassList[i].entryId;
                        insertInfo(oneMessage1);
                    }

                    var oneMessage2 = new Array();
                    // oneMessage2[0]="<a href='#' myAttr='"+"14"+"'>"+"用户222"+"</a>";
                    // oneMessage2[1]="2017-09-05 17:09:50";
                    // oneMessage2[2]="<div myAttr='"+14+"'></div>";

                    // insertInfoTable2(oneMessage2);
                    for (var i = 0; i < dataKey.toPassList.length; i++) {
                        //alert("ads");
                        oneMessage2[0]="<a href='#' myAttr='"+dataKey.toPassList[i].entryId+"'>"+dataKey.toPassList[i].entryName+"</a>";
                        oneMessage2[1]=dataKey.toPassList[i].createDate;
                        oneMessage2[2]="<div myAttr='"+dataKey.toPassList[i].entryId+"'></div>";
                        //dataKey.toPassList[i].entryId;
                        insertInfoTable2(oneMessage2);
                    }


                    var oneMessage3 = new Array();


                    for (var i = 0; i < dataKey.hasNotPassList.length; i++) {
                        //alert("ads");
                        oneMessage3[0]="<a href='#' myAttr='"+dataKey.hasNotPassList[i].eid+"'>"+dataKey.hasNotPassList[i].entryName+"</a>";
                        oneMessage3[1]="2017-22-07 02:22:08";
                        oneMessage3[2]=dataKey.hasNotPassList[i].refuseReason;
                        oneMessage3[3]="<div myAttr='"+dataKey.hasNotPassList[i].eid+"'></div>";
                        //dataKey.hasNotPassList[i].entryId;
                        insertInfoTable3(oneMessage3);
                    }
                    deleteActionListener();
                    PopupActionListener();
            }
        },
        error:function(data){                          //请求失败时调用此函数
            alert("error:" + data);
        }
    });

});







//----删除某一行-----
function delRow(r){
    document.getElementById('table1').deleteRow(r);
}
function delRowTable2(r){
    document.getElementById('table2').deleteRow(r);
}
function delRowTable3(r){
    document.getElementById('table3').deleteRow(r);
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
function insertInfoTable2(arr){
    //根据id获取表单信息
    var x = document.getElementById('table2').insertRow(1); //获取第一行对象
    var count = 0;
    for(var i in arr){
        x.insertCell(count).innerHTML = arr[i];
        count++;
    }
}
function insertInfoTable3(arr){
    //根据id获取表单信息
    var x = document.getElementById('table3').insertRow(1); //获取第一行对象
    var count = 0;
    for(var i in arr){
        x.insertCell(count).innerHTML = arr[i];
        count++;
    }
}



$(function(){
    $("#goback").click(function(){
        document.getElementById('light').style.display='none';
        document.getElementById('fade').style.display='none';
    });
})


//返回首页的按钮
$(function(){
    $("#BackHome").click(function(){
        //
        //alert($("#username").text());
        window.location.href=encodeURI("../index.html"+"?"+"userName="+$("#username").text());;
    })
})


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
        button1.style.backgroundImage = "url(../images/self/hasPass_selected.png)";
        button2.style.backgroundImage = "url(../images/self/toPass.png)";
        button3.style.backgroundImage = "url(../images/self/notPass.png)";
        table1.style.display = "";
        table2.style.display = "none";
        table3.style.display = "none";
        break;
      case 2:
        button1.style.backgroundImage = "url(../images/self/hasPass.png)";
        button2.style.backgroundImage = "url(../images/self/toPass_selected.png)";
        button3.style.backgroundImage = "url(../images/self/notPass.png)";
        table1.style.display = "none";
        table2.style.display = "";
        table3.style.display = "none";
        break;
      case 3:
        button1.style.backgroundImage = "url(../images/self/hasPass.png)";
        button2.style.backgroundImage = "url(../images/self/toPass.png)";
        button3.style.backgroundImage = "url(../images/self/notPass_selected.png)";
        table1.style.display = "none";
        table2.style.display = "none";
        table3.style.display = "";
        break;
    }
}