package com.zqb.serverlet;

import com.zqb.util.ExcelHelper;
import net.sf.json.JSONObject;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zqb on 2016/10/31.
 */
@WebServlet(name = "FileUpload",urlPatterns = "/FileUpload")
public class FileUpload extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path="";
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("content-type","text/json;charset=UTF-8");
        PrintWriter outPrinter = response.getWriter();
        Map<String,String> map=new HashMap<>();

        // 得到上传文件的保存目录，将上传文件存放在WEB-INF目录下，不允许外界直接访问，保证上传文件的安全
        String save_path=this.getServletContext().getRealPath("/WEB-INF/upload");
        File file=new File(save_path);
        if(!file.exists()&&!file.isDirectory())
        {
            System.out.println(save_path+"目录不存在，需要创建");
            file.mkdirs();
        }
        //System.out.print("hhhh");
        //使用Apache文件上传组件处理文件上传步骤：
        //1、创建一个DiskFileItemFactory工厂
        DiskFileItemFactory factory=new DiskFileItemFactory();
        //2、创建一个文件上传解析器
        ServletFileUpload upload=new ServletFileUpload(factory);
        //解决上传文件中文乱码
        upload.setHeaderEncoding("UTF-8");
        // 设置上传单个文件的最大值
        upload.setFileSizeMax(1024 * 1024 );// 1M
        // 设置上传文件总量的最大值，就是本次上传的所有文件的总和的最大值
        upload.setSizeMax(1024 * 1024 * 10);// 10M
        //3、判断提交上来的数据是否是上传表单的数据
        if(!ServletFileUpload.isMultipartContent(request))
        {
            return;
        }
        try {

            List<FileItem> list = upload.parseRequest(request);
            if(list==null){
                //System.out.print("list为空");
                map.put("code","error");
                map.put("msg","上传文件列表为空");
                JSONObject json = JSONObject.fromObject(map);
                outPrinter.print(json);
                return;
            }
            for(FileItem item:list)
            {
                //如果fileitem中封装的是普通输入项的数据
                if(item.isFormField())
                {
                    String name=item.getName();
                    String value=item.getString("UTF-8");
                    System.out.println(name+"="+value);

                }
                else//如果fileitem中封装的是上传文件
                {
                    String filename = item.getName();
                    System.out.println(filename);
                    if(filename==null || filename.trim().equals(""))
                    {
                        continue;
                    }
                    //注意：不同的浏览器提交的文件名是不一样的，有些浏览器提交上来的文件名
                    // 是带有路径的，如：  c:\a\b\1.txt，而有些只是单纯的文件名，如：1.txt
                    //处理获取到的上传文件的文件名的路径部分，只保留文件名部分
                    filename = filename.substring(filename.lastIndexOf("\\")+1);
                    //获取item中的上传文件的输入流
                    InputStream in = item.getInputStream();
                    //创建一个文件输出流
                    FileOutputStream out=new FileOutputStream(save_path + "\\" + filename);
                    path=save_path + "\\" + filename;//保存文件全路径，用于后面读取文件导入数据库
                    //创建一个缓冲区
                    byte buffer[] = new byte[1024];
                    //判断输入流中的数据是否已经读完的标识
                    int len = 0;
                    //循环将输入流读入到缓冲区当中，(len=in.read(buffer))>0就表示in里面还有数据
                    while((len=in.read(buffer))>0)
                    {
                        out.write(buffer,0,len);
                    }
                    //关闭输入流
                    in.close();
                    //关闭输出流
                    out.close();
                    //删除处理文件上传时生成的临时文件
                    item.delete();

                }
            }
        } catch (FileUploadException e) {
            e.printStackTrace();
            return;
        }

        //读取刚刚上传的文件
        File read_file=new File(path);
        if(!read_file.exists())
        {
            map.put("code","error");
            map.put("msg","读取上传文件出错");
            JSONObject json = JSONObject.fromObject(map);
            outPrinter.print(json);
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
        outPrinter.print(json);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
