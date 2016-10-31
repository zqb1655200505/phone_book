package com.zqb.serverlet;

import com.zqb.javaBean.User;
import com.zqb.util.DBConnectHelper;
import net.sf.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zqb on 2016/10/24.
 */
@WebServlet(name = "AddToExist",urlPatterns ="/AddToExist")
public class AddToExist extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String phone_number=request.getParameter("phone_number");
        int contector_id=Integer.valueOf(request.getParameter("id"));
        Connection con= DBConnectHelper.getConnection("contects");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("content-type","text/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        Map<String,String> map=new HashMap<>();
        try {
            Statement statement=con.createStatement();
            if(isNumeric(phone_number))
            {
                String sql="INSERT INTO contects(contector_id,phone_number) VALUES("+contector_id+",'"+phone_number+"')";
                statement.execute(sql);
            }
            else
            {
                map.put("code","error");
                map.put("msg","号码不符");
            }
        } catch (SQLException e) {
            map.put("code","error");
            map.put("msg","添加出错");
            e.printStackTrace();
        }
        if(map.isEmpty())
        {
            map.put("code","seccess");
            map.put("msg","添加成功");
        }
        JSONObject json = JSONObject.fromObject(map);
        out.print(json);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    protected boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }
}
