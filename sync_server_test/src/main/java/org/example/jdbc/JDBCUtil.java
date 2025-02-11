package org.example.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;

public class JDBCUtil {
    public static Connection getConnection() {
        Connection c = null;
        try {
            DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());

            String url= "jdbc:mySQL://127.0.0.1:3306/chat_app_tcp_test";
            String usreName="root";
            String passWord="@Santo.22";

            c = DriverManager.getConnection(url, usreName, passWord);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return c;
    }

    public static void closeConnection(Connection c) {
        try {
            if(c!=null) {
                c.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
