<%@ page import="java.sql.SQLException" %>
<%@ page import="java.sql.Connection" %>
<%@ page import="java.sql.Statement" %>
<%@ page import="com.zqb.util.DBConnectHelper" %>
<%@ page import="java.sql.ResultSet" %><%--
  Created by IntelliJ IDEA.
  User: zqb
  Date: 2016/10/24
  Time: 22:32
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>查看联系人</title>
    <style type="text/css">
        .body_content
        {
            width: 1280px;
            height: 100%;
            margin: 0 auto;
            margin-top: 50px;
        }
        .main_content
        {
            width: 1000px;
            margin: 0 auto;
            height: 100%;
        }
        #alternatecolor
        {
            margin: 0 auto;
            font-size: 14px;
        }
        .altrowstable {
            font-family: verdana,arial,sans-serif;
            color:#333333;
            border-width: 1px;
            border-color: #a9c6c9;
            border-collapse: collapse;
        }
        .altrowstable th {
            border-width: 1px;
            padding: 8px;
            border-style: solid;
            border-color: #a9c6c9;
            width: 150px;
            height: 45px;
        }
        .altrowstable td {
            border-width: 1px;
            padding: 8px;
            border-style: solid;
            border-color: #a9c6c9;
            width: 150px;
            height: 45px;
            text-align: center;
        }
        .oddrowcolor{
            background-color:#d4e3e5;
        }
        .evenrowcolor{
            background-color:#c3dde0;
        }
        #mask {
            background: #575757;
            opacity: 0.5;
            position: fixed;
            z-index: 1;
            top: 0;
            left: 0;
        }
        .input_txt
        {
            width: 250px;
            height:40px;
            font-size: 22px;
        }
        .export
        {
            width: 800px;
            height: 50px;
            text-align: center;
            margin: 0 auto;
        }
    </style>
    <script type="text/javascript" src="lib/jquery-1.10.2.js"></script>
    <script type="text/javascript" src="dist/dialog-min.js"></script>
    <link rel="stylesheet" href="css/ui-dialog.css"/>
</head>
<body>
<div style="width: 100%;height: 100px;background-color: rgba(194, 255, 244, 0.45);text-align: center;padding-top: 1px;padding-bottom: 3px;">
    <h1 style="font-size: 50px;">联系人列表</h1>
</div>
<div class="body_content">
    <div class="export">
        <button onclick="export_to_excel()" style="margin-left: 400px;">导出excel</button>
    </div>
    <div class="main_content">
        <%
            Connection con=null;
            Statement sql=null;
            ResultSet rs;
            con= DBConnectHelper.getConnection("contects");
            out.print("<table class=\"altrowstable\" id=\"alternatecolor\">");
            out.print("<tr><th>序号</th><th>姓名</th><th>号码</th><th>操作</th></tr>");
            sql=con.createStatement();
            if(con!=null)
            {
                try {
                    String condition="SELECT contectors.ID AS User_ID," +
                            "contectors.name AS name,contects.phone_number as phone_number,contects.ID AS phone_ID " +
                            "FROM contectors,contects WHERE contectors.ID=contects.contector_id " +
                            "ORDER BY contects.contector_id";
                    rs=sql.executeQuery(condition);
                    int counter=1;
                    int cur_id=0;
                    int cnt=1;
                    while(rs.next())
                    {
                        int id=rs.getInt("User_ID");
                        byte[] name_bytes=rs.getBytes("name");
                        String name=new String(name_bytes,"UTF-8");
                        byte[] phone_number_byte=rs.getBytes("phone_number");
                        String phone_number=new String(phone_number_byte,"UTF-8");
                        if(id!=cur_id)
                        {
                            cur_id=id;
                            out.print("<tr>");
                            out.print("<td>"+(counter++)+"</td>");
                            out.print("<td>"+name+"</td>");
                            out.print("<td>"+phone_number+"</td>");
                            out.print("<td><button id='"+rs.getInt("phone_ID")+"' onclick=delete_record(this)>删除</button>&nbsp;&nbsp;<button id='"+id+"' onclick='edit_record(this)'>编辑</button>&nbsp;&nbsp;<button id='"+id+"' onclick='add_to_exist(this)'>添加</button></td>");
                            out.print("</tr>");
                        }
                        else
                        {
                            out.print("<tr>");
                            out.print("<td></td>");
                            out.print("<td>"+name+"</td>");
                            out.print("<td>"+phone_number+"</td>");
                            out.print("<td><button id='"+rs.getInt("phone_ID")+"' onclick='delete_record(this)'>删除</button>&nbsp;&nbsp;<button id='"+id+"' onclick='edit_record(this)'>编辑</button>&nbsp;&nbsp;<button id='"+id+"' onclick='add_to_exist(this)'>添加</button></td>");
                            out.print("</tr>");
                        }
                    }
                }
                catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            out.print("</table>");
        %>
    </div>
</div>
<script>
    var mask = function () {
        $('<div id="mask" style="width: ' + $(window).width() + 'px; '
                + 'height: ' + $(window).height() + 'px;"></div>').appendTo("body");
    };

    // 窗口大小变化，调整 mask 大小
    $(window).on("resize", function () {
        if ($("#mask").length) {
            $("#mask").css({
                width: $(window).width(),
                height: $(window).height(),
                opacity: 0
            });
        }
    });
    function edit_record(obj) {
        var par=obj.parentNode.parentNode;
        var name=par.childNodes.item(1).innerHTML;
        var number=par.childNodes.item(2).innerHTML;
        var d = dialog({
            title: '编辑联系人',
            width: 400,
            zIndex:9999,
            content: '<p><span style="font-size: 20px;">姓名：</span><input id="name" type="text" value="'+name+'" class="input_txt"/></p>'+'<p><span style="font-size: 20px";>号码：</span><input id="phone_number" type="text" value="'+number+'" class="input_txt"/></p>',
            okValue: '确定',
            ok: function () {
                var txt_name=$("#name").val();
                var phone_number=$("#phone_number").val();
                //alert("谢谢");
                $.ajax({
                    type: "POST",
                    url: "UpdateRecord",
                    data:{"id":obj.id,"name":txt_name,"phone_number":phone_number},
                    success: function(data)
                    {
                        var res=eval(data);
                        alert(res["msg"]);
                        $("#mask").remove();
                        window.location.reload();
                    },
                    error: function(data)
                    {
                        $("#mask").remove();
                        window.location.reload();
                    }
                });
            },
            cancelValue: '取消',
            cancel: function () {
                $("#mask").remove();
            }
        });
        d.show();
        mask();
    }


    function add_to_exist(obj) {
        //alert(obj.id);
        var par=obj.parentNode.parentNode;
        var name=par.childNodes.item(1).innerHTML;
        var d = dialog({
            title: '添加到联系人',
            width: 400,
            zIndex:9999,
            content: '<p><span style="font-size: 20px;">姓名：</span><label  style="font-size: 20px">'+name+'</label></p>'+'<p><span style="font-size: 20px">号码：</span><input id="phone_number" type="text" class="input_txt"/></p>',
            okValue: '确定添加',
            ok: function () {
                var phone_number=$("#phone_number").val();
                $.ajax({
                    type: "POST",
                    url: "AddToExist",
                    data:{"id":obj.id,"phone_number":phone_number},
                    success: function(data)
                    {
                        var res=eval(data);
                        alert(res["msg"]);
                        $("#mask").remove();
                        window.location.reload();
                    },
                    error: function(data)
                    {
                        $("#mask").remove();
                        window.location.reload();
                    }
                });
            },
            cancelValue: '取消',
            cancel: function () {
                $("#mask").remove();
            }
        });
        d.show();
        mask();

    }
    function altRows(id){
        if(document.getElementsByTagName){
            var table = document.getElementById(id);
            var rows = table.getElementsByTagName("tr");

            for(i = 0; i < rows.length; i++){
                if(i % 2 == 0){
                    rows[i].className = "evenrowcolor";
                }else{
                    rows[i].className = "oddrowcolor";
                }
            }
        }
    };
    function delete_record(obj)
    {
        $.ajax({
            type:"post",
            url:"LookUp",
            data:{"id":obj.id},
            success:function (data) {
                var res=eval(data);
                alert(res["delete_record_result"]);
                window.location.reload();
            },
            error:function (data) {
                alert("删除记录出错");
            }
        })
    };
    window.onload=function(){
        altRows('alternatecolor');
    };
    function export_to_excel()
    {
        var d = dialog({
            width:400,
            title:"保存路径文件夹",
            content:'<p><input type="text" class="input_txt" id="save_path"/></p><p>如:F:\\BaiduYunDownload\\quanquan</p>',
            okValue:"确定导出",
            ok:function () {
                var path=$("#save_path").val();
                //alert(path);
                $.ajax({
                    type:"POST",
                    url:"ExportExcel",
                    data:{"save_path":path},
                    success: function (data) {
                        var res=eval(data);
                        alert(res["msg"]);
                        $("#mask").remove();
                        window.location.reload();
                    },
                    error:function (data) {
                        alert("请求出错");
                        $("#mask").remove();
                        window.location.reload();
                    }
                });
            },
            cancelValue:"取消",
            cancel: function () {
                $("#mask").remove();
            }
        });
        d.show();
        mask();
    }
</script>
</body>
</html>
