<%--
  Created by IntelliJ IDEA.
  User: zqb
  Date: 2016/10/24
  Time: 22:01
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
  String path = request.getContextPath();
  String basePath = request.getScheme() + "://"
          + request.getServerName() + ":" + request.getServerPort()
          + path +"/";

%>
<html>
<head>
  <base href="<%=basePath%>">
  <title>个人电话簿</title>
  <meta charset="utf-8"/>

  <style type="text/css">
    body{
      width: 100%;
      margin: 0 auto;
      background-image: url("res/background.jpg");
      background-repeat: no-repeat;
    }
    .main_content
    {
      width:400px;
      height: 500px;
      margin: 0 auto;
      font-size: 25px;
      margin-top:8%;
      text-decoration: none;
    }
    .top_nav
    {
      width: 500px;
      height: 70px;
      background-color: rgba(121, 161, 154, 0.86);
      margin: 0 auto;
      margin-top: 150px;
    }
    .round_back
    {
      width: 150px;
      height: 150px;
      border-radius: 80px;
      color: white;
      font-size: 20px;
      margin: 0 auto;
      opacity: 0.85;
    }
    .round_back:hover
    {
      opacity: 0.65;
    }
    .new_one
    {
      float: left;
      background: green;
    }
    .look_up
    {
      float: right;
      background: cadetblue;
    }

    .for_txt
    {
      width: 100px;
      height: 40px;
      margin-top: 60px;
      margin-left: 30px;
    }

  </style>
</head>
<body>
<div class="top_nav">
  <div style="width:100%;height:100%;margin: 0 auto;font-size: 26px;">
    <h1>My telephone book</h1>
  </div>
</div>
<div class="main_content">
  <a href="add.jsp">
    <div class="round_back new_one">
      <div class="for_txt">
        <span class="txt">新建联系人</span>
      </div>
    </div>
  </a>
  <a href="look_up.jsp">
    <div class="round_back look_up">
      <div class="for_txt">
        <span class="txt">查看联系人</span>
      </div>
    </div>
  </a>

</div>
<div>

</div>
</body>
</html>
