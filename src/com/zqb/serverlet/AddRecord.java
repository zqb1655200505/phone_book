package com.zqb.serverlet;

import com.zqb.javaBean.User;
import com.zqb.util.DBHelper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zqb on 2016/10/24.
 */
@WebServlet(name = "AddRecord" ,urlPatterns ="/AddRecord")
public class AddRecord extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user=new User();
        request.setCharacterEncoding("UTF-8");
        user.setName(request.getParameter("name"));
        user.setPhone_number(request.getParameter("phone_number"));
        if(!user.validate())
        {
            Map<String,String>errmsg=user.getErrors();
            request.setAttribute("errmsg",errmsg);
            request.getRequestDispatcher("/add.jsp").forward(request,response);
            return;
        }
        else
        {
            if(DBHelper.add(user))
            {
//                response.setCharacterEncoding("UTF-8");
//                PrintWriter writer=response.getWriter();
//                writer.println("添加成功");
//                response.setHeader("refresh","2;url='/add.jsp'");
                Map<String,String>map=new HashMap<>();
                map.put("add_success","添加成功");
                request.setAttribute("errmsg",map);
                request.getRequestDispatcher("/add.jsp").forward(request,response);
                return;
            }
        }
    }
}
