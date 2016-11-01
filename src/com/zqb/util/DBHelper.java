package com.zqb.util;

import com.zqb.javaBean.User;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by zqb on 2016/10/25.
 */
public class DBHelper {
    static Connection con=DBConnectHelper.getConnection("contects");
    static Statement sql= null;
    public static boolean add(User user)
    {
        try {
            sql = con.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        String condition="INSERT INTO contectors(name) VALUES('"+user.getName()+"')";
        String condition1="INSERT INTO contects(contector_id,phone_number) " +
                "VALUES(LAST_INSERT_ID(),'"+user.getPhone_number()+"')";
        try {
            sql.execute(condition);
            sql.execute(condition1);
            sql.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean update(User user)
    {
        try {
            sql = con.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        String name=user.getPhone_number();
        String phone_number=user.getPhone_number();
        int id=user.getId();
        if(phone_number!=null&&phone_number.length()>0)
        {
            String condition="UPDATE contects SET contects.phone_number='"+user.getPhone_number()
                    +"' WHERE ID="+id;
            try {
                sql.execute(condition);
                //sql.close();
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }
        else
        {
            return false;
        }
        if(name!=null&&name.length()>0)
        {
            String condition2="SELECT * FROM contects WHERE ID="+id;
            try {
                ResultSet res=sql.executeQuery(condition2);
                int user_id=0;
                while (res.next())
                {
                    user_id=res.getInt("contector_id");
                    break;
                }
                if(user_id!=0)
                {
                    String condition1="UPDATE contectors SET name='"+user.getName()+"' WHERE ID="+user_id;
                    sql.execute(condition1);
                }
                sql.close();
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }
        else {
            return false;
        }
        return true;
    }
}
