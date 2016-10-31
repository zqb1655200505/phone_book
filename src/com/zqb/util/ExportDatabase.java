package com.zqb.util;

import java.io.File;
import java.io.IOException;

/**
 * Created by zqb on 2016/10/19.
 */
public class ExportDatabase {
    /**
     * Java代码实现MySQL数据库导出
     *
     * @author GaoHuanjie
     * @param hostIP MySQL数据库所在服务器地址IP
     * @param userName 进入数据库所需要的用户名
     * @param password 进入数据库所需要的密码
     * @param savePath 数据库导出文件保存路径
     * @param fileName 数据库导出文件文件名(一般为数据库名+日期)
     * @param databaseName 要导出的数据库名
     * @return 返回true表示导出成功，否则返回false。
     */
    public static boolean exportDatabaseTool(String hostIP,String userName,String password,String savePath, String fileName, String databaseName)
    {
        //数据库备份
//       SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
//       java.util.Date date=new Date();
//       ExportDatabase.exportDatabaseTool("localhost","root","2011zqb,.","H:/database_back_up/",format.format(date)+"contects.sql","contects");
        File saveFile=new File(savePath);
        if(!saveFile.exists())//文件夹不存在，则创建文件夹
        {
            saveFile.mkdirs();
        }
        if (!savePath.endsWith(File.separator)) {
            savePath = savePath + File.separator;
        }
        StringBuilder sb=new StringBuilder();
        sb.append("mysqldump").append(" --opt").append(" -h").append(hostIP);
        sb.append(" --user=").append(userName) .append(" --password=").append(password).append(" --lock-all-tables=true");
        sb.append(" --result-file=").append(savePath + fileName).append(" --default-character-set=utf8 ").append(databaseName);
        try {
            Process process=Runtime.getRuntime().exec(sb.toString());
            if (process.waitFor() == 0) {// 0 表示线程正常终止。
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }
}
