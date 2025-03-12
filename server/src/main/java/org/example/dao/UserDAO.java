package org.example.dao;

import org.example.jdbc.JDBCUtil;
import org.example.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class UserDAO {
    public static ArrayList<User> getUsers() {
        ArrayList<User> users = new ArrayList<>();
        String sql = "SELECT * FROM user_account";
        try {
            Connection connection = JDBCUtil.getConnection();
            PreparedStatement pst = connection.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String password = rs.getString("password");
                String account = rs.getString("account");
                users.add(new User(id, name, account, password));
            }
            JDBCUtil.closeConnection(connection);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }

    public static void addUser(User user) {
        try {
            Connection connection = JDBCUtil.getConnection();
            String sql = "INSERT INTO user_account (account, password, name) VALUES (?, ?, ?)";
            PreparedStatement pst = connection.prepareStatement(sql);
            pst.setString(1, user.getAccount());
            pst.setString(2, user.getPassword());
            pst.setString(3, user.getName());
            pst.execute();
            JDBCUtil.closeConnection(connection);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static boolean isUserExist(String account) {
        String sql = "SELECT * FROM user_account WHERE account = ?";
        try {
            Connection connection = JDBCUtil.getConnection();
            PreparedStatement pst = connection.prepareStatement(sql);
            pst.setString(1, account);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                JDBCUtil.closeConnection(connection);
                return true;
            };
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public static User getUserByAccount(String account) {
        User user = null;
        String sql = "SELECT * FROM user_account WHERE account = ?";
        try {
            Connection connection = JDBCUtil.getConnection();
            PreparedStatement pst = connection.prepareStatement(sql);
            pst.setString(1, account);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String account_ = rs.getString("account");
                String password = rs.getString("password");
                user = new User(id, name, account_, password);
                JDBCUtil.closeConnection(connection);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return user;
    }
}
