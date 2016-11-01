<%@ page import="java.util.Map" %><%--
  Created by IntelliJ IDEA.
  User: zqb
  Date: 2016/10/24
  Time: 22:06
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>添加联系人</title>
    <meta charset="utf-8">
    <style type="text/css">
        .body_content
        {
            width: 1280px;
            height: 800px;
            margin: 0 auto;
            font-size: 20px;
        }
        .main_content
        {
            width: 500px;
            height: 350px;
            margin: 0 auto;
        }
        .form_div
        {
            width: 350px;
            height: 250px;
            margin: 0 auto;
        }
        .for_button
        {
            width: 100%;
            height: 50px;
            margin-left: 48%;
            margin-top: 35px;
        }

        .input_txt
        {
            width: 80%;
            height: 40px;
            font-size: 20px;
        }
        .button
        {
            width: 60px;
            height: 35px;
            font-size: 18px;
        }
        .button:hover
        {
            cursor: hand;
        }
    </style>


</head>
<body>
<div class="body_content">
    <div style="width: 100%;height: 250px;"></div>
    <div class="main_content">
        <form action="AddRecord" method="post" id="AddRecord">
            <div class="form_div">
                <p>姓名：<input type="text" name="name" class="input_txt"/></p>
                <p>号码：<input type="text" name="phone_number" class="input_txt"/></p>
                <div class="for_button"><input type="submit" name="submit" value="添加" class="button"/></div>
            </div>
        </form>
        <form id="upload">
            <div style="width: 500px;height: 80px;margin: 0 auto;text-align: center;font-size: 20px;">
                <p>从excel导入：
                    <%--name属性必须要有，这是form表单提交成功的关键--%>
                    <input value="从excel导入" type="file" name="upload_file" id="import_from_excel">
                </p>
                <%--<p><input type="submit" value="确认导入"></p>--%>

            </div>
        </form>
        <p><button onclick="import_from_excel()">确认导入</button></p>
        <%
            Map<String,String> result=(Map<String,String>)request.getAttribute("errmsg");
            if(result!=null&&!result.isEmpty())
            {
                //System.out.println(result);
                for (String key: result.keySet())
                {
                    String value=result.get(key);
                    StringBuilder sb=new StringBuilder();
                    sb.append("<script>alert('");
                    sb.append(value);
                    sb.append("');</script>");
                    out.print(sb.toString());
                }
            }
        %>
    </div>
</div>
    <script type="text/javascript" src="js/jquery-2.0.0.min.js"></script>
    <script type="text/javascript">
        function import_from_excel() {
            var formDate = new FormData($("#upload")[0]);
            $.ajax({
                type:"post",
                url:"FileUpload",
                data:formDate,
                contentType:false,//避免提交时被jQuery修改
                processData:false,
                success:function (data) {
                    var res=eval(data);
                    alert(res["msg"]);
                },
                error:function () {
                    alert("请求出错");
                }
            });
        }
    </script>
</body>
</html>
