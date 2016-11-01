package com.zqb.serverlet;

import com.zqb.javaBean.User;
import com.zqb.util.DBHelper;
import net.sf.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zqb on 2016/10/26.
 */
@WebServlet(name = "UpdateRecord",urlPatterns = "/UpdateRecord")
public class UpdateRecord extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        int id=Integer.valueOf(request.getParameter("id"));
        String name=request.getParameter("name");
        String phone_number=request.getParameter("phone_number");
        User user=new User();
        user.setPhone_number(phone_number);
        user.setId(id);//此处id为号码id
        user.setName(name);
        response.setCharacterEncoding("UTF-8");
        response.setHeader("content-type","text/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        Map<String,String> map=new HashMap<>();
        if(user.validate())
        {
            if(DBHelper.update(user))
            {
                map.put("code","success");
                map.put("msg","更新记录成功");
            }
            else
            {

                map.put("code","error");
                map.put("update_record_result","更新记录失败");
            }
        }
        else
        {
            map=user.getErrors();
        }
        JSONObject json = JSONObject.fromObject(map);
        out.print(json);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
