package com.zqb.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by zqb on 2016/10/16.
 */
public class DBConnectHelper {

    public static Connection getConnection(String dbName)
    {
        String dbUrl="jdbc:mysql://localhost:3306/"+dbName;
        String username="root";
        String password="2011zqb,.";
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con= DriverManager.getConnection(dbUrl,username,password);
            if(!con.isClosed())
            {
                return con;
            }
            else
            {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
