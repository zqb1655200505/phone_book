package com.zqb.serverlet;

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

/**
 * Created by zqb on 2016/10/24.
 */
@WebServlet(name = "LookUp",urlPatterns ="/LookUp" )
public class LookUp extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        int id=Integer.valueOf(request.getParameter("id"));
        Connection con= DBConnectHelper.getConnection("contects");
        try {
            Statement statement=con.createStatement();
            String sql="DELETE FROM contects WHERE ID="+id;
            response.setContentType("text/json;charset=UTF-8;pageEncoding=UTF-8");
                /*目的是用于response.getWriter()输出的字符流的乱码问题，
                 如果是response.getOutputStream()是不需要此种解决方案的；
                 因为这句话的意思是为了将response对象中的数据以UTF-8解码后发向浏览器；
                 */
            response.setCharacterEncoding("UTF-8");
            response.setHeader("content-type","text/json;charset=UTF-8");
            PrintWriter out = response.getWriter();
            Map<String,String>map=new HashMap<>();
            if(!statement.execute(sql))
            {
                ////控制浏览器的编码行为
                map.put("delete_record_result","删除记录成功");
                JSONObject json = JSONObject.fromObject(map);
                out.print(json);
                return;
            }
            else
            {
                map.put("delete_record_result","删除记录失败");
                JSONObject json = JSONObject.fromObject(map);
                out.print(json);
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
