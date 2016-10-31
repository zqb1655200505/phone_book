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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zqb on 2016/10/26.
 */
@WebServlet(name = "ExportExcel",urlPatterns = "/ExportExcel")
public class ExportExcel extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String save_path=request.getParameter("save_path");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("content-type","text/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        Map<String,String> map=new HashMap<>();
        if(!save_path.endsWith("\\"))
        {
            save_path+="\\";
        }
        try {
            if (!(new File(save_path).isDirectory())) {
                new File(save_path).mkdir();
            }
        } catch (SecurityException e) {
            e.printStackTrace();
            map.put("code","error");
            map.put("msg","文件夹创建失败");
            return;
        }
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd-hh-mm");
        Date date=new Date();
        String file_name=format.format(date)+"-contectors.xls";
        try {
            ExcelHelper.excel_export(save_path+file_name);
            map.put("code","success");
            map.put("msg","导出成功");
        }
        catch (Exception e)
        {
            map.put("code","error");
            map.put("msg","导出失败");
        }
        JSONObject json = JSONObject.fromObject(map);
        out.print(json);
        return;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
