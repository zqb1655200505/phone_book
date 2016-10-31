package com.zqb.util;

import com.zqb.javaBean.User;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import org.apache.poi.hssf.usermodel.*;

import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zqb on 2016/10/17.
 */
public class ExcelHelper {
    public static void addRecordFromExcel(String file_path)
    {
        List<User>list=getAllByExcel(file_path);
        if(list!=null)
        {
            Connection con= DBConnectHelper.getConnection("contects");
            for(int i=0;i<list.size();i++)
            {
                try {
                    Statement statement=con.createStatement();
                    String sql="INSERT INTO contectors(name) VALUES('"+list.get(i).getName()+"')";
                    String sql2="INSERT INTO contects(contector_id,phone_number) VALUES(LAST_INSERT_ID(),'"+list.get(i).getPhone_number()+"')";
                    statement.execute(sql);
                    statement.execute(sql2);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    /**
     * 查询指定目录中电子表格中所有的数据
     * @param file_path 文件完整路径
     * @return
     */
    private static List<User> getAllByExcel(String file_path)
    {
        File file=new File(file_path);
        if(!file.exists())
        {
            System.out.println("文件不存在");
            return null;
        }
        List<User> list=new ArrayList<>();
        try {
            Workbook workbook=Workbook.getWorkbook(file);
            Sheet sheet=workbook.getSheet(0);
            //获取sheet中的行和列数
            int cols=sheet.getColumns();
            int rows=sheet.getRows();
            for(int i=1;i<rows;i++)
            {
                for(int j=1;j<cols;j++)
                {
                    String name=sheet.getCell(j++,i).getContents();
                    String phone_number=sheet.getCell(j++,i).getContents();
                    User user=new User();
                    user.setId(i);
                    user.setName(name);
                    user.setPhone_number(phone_number);
                    list.add(user);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static void excel_export(String savePath)
    {
        // 第一步，创建一个webbook，对应一个Excel文件
        HSSFWorkbook wb = new HSSFWorkbook();
        //第二步，在workbook中添加一个sheet，对应excel文件中的sheet
        HSSFSheet sheet=wb.createSheet("电话簿-");
        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
        HSSFRow row = sheet.createRow((int)0);
        // 第四步，创建单元格，并设置值表头 设置表头居中
        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
        HSSFCell cell = row.createCell((short) 0);
        cell.setCellValue("序号");
        cell.setCellStyle(style);
        cell = row.createCell((short) 1);
        cell.setCellValue("姓名");
        cell.setCellStyle(style);
        cell = row.createCell((short) 2);
        cell.setCellValue("号码");
        cell.setCellStyle(style);

        // 第五步，写入实体数据 实际应用中这些数据从数据库得到，
        List<User> list=getRecordFromDb();
        for(int i=0;i<list.size();i++)
        {
            row=sheet.createRow(i+1);
            User rec=list.get(i);
            row.createCell(0).setCellValue(rec.getId());
            row.createCell(1).setCellValue(rec.getName());
            row.createCell(2).setCellValue(rec.getPhone_number());
        }

        // 第六步，将文件存到指定位置
        try {
            FileOutputStream fout=new FileOutputStream(savePath);
            wb.write(fout);
            fout.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<User> getRecordFromDb()
    {
        List<User> list=new ArrayList<>();
        Connection con= DBConnectHelper.getConnection("contects");
        try {
            if(con!=null&&!con.isClosed())
            {
                Statement statement=con.createStatement();

                String condition1="SELECT contectors.ID AS User_ID," +
                        "contectors.name AS name,contects.phone_number as phone_number,contects.ID AS phone_ID " +
                        "FROM contectors,contects WHERE contectors.ID=contects.contector_id " +
                        "ORDER BY contects.contector_id";

                //String condition="select * from contectors";
                ResultSet rs=statement.executeQuery(condition1);
                while (rs.next())
                {
                    int id=rs.getInt("User_ID");
                    byte[] name_bytes=rs.getBytes("name");
                    byte[] phone_bytes=rs.getBytes("phone_number");
                    try {
                        String name=new String(name_bytes,"UTF-8");
                        String phone_number=new String(phone_bytes,"UTF-8");
                        User user=new User();
                        user.setId(id);
                        user.setName(name);
                        user.setPhone_number(phone_number);
                        list.add(user);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
//                String sql="";
//                ResultSet rs2=null;
//                while (rs1.next())
//                {
//                    int id=rs1.getInt("ID");
//                    String name=rs1.getString("name");
//                    sql="SELECT * FROM contects WHERE contector_id="+id;
//                    Statement statement1=con.createStatement();
//                    rs2=statement1.executeQuery(sql);
//                    while (rs2.next())
//                    {
//                        User user=new User();
//                        user.setId(id);
//                        user.setName(name);
//                        user.setPhone_number(rs2.getString("phone_number"));
//                        list.add(user);
//                    }
//                }

                con.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
