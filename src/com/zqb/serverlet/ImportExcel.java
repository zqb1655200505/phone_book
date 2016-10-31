package com.zqb.serverlet;

import com.zqb.util.ExcelHelper;
import net.sf.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zqb on 2016/10/27.
 */
@WebServlet(name = "ImportExcel",urlPatterns = "/ImportExcel")
public class ImportExcel extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String path=request.getParameter("path");
        System.out.println(path);
        response.setCharacterEncoding("UTF-8");
        response.setHeader("content-type","text/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        Map<String,String> map=new HashMap<>();
        if(!new File(path).exists())//文件不存在
        {
            map.put("code","error");
            map.put("msg","文件不存在");
            return;
        }
        else
        {
            if(!path.endsWith(".xls"))
            {
                map.put("code","error");
                map.put("msg","文件类型不符");
            }
            else
            {
                try
                {
                    ExcelHelper.addRecordFromExcel(path);
                }
                catch (Exception e)
                {
                    map.put("code","error");
                    map.put("msg","导入出错");
                    return;
                }
            }
        }
        if(map.isEmpty())
        {
            map.put("code","success");
            map.put("msg","导入数据成功");
        }
        JSONObject json = JSONObject.fromObject(map);
        out.print(json);
    }

    /**
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
