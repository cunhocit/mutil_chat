package org.example.dao;

import org.example.jdbc.JDBCUtil;
import org.example.model.Message;
import org.example.model.RoomChat;
import org.example.model.SendMessage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Random;

public class RoomChatDAO {

    public static ArrayList<RoomChat> getRoomList() {
        ArrayList<RoomChat> arrayList = new ArrayList<>();
        try {
            Connection connection = JDBCUtil.getConnection();
            String sql = "select * from roomlist";
            PreparedStatement pst = connection.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                arrayList.add(new RoomChat(
                        rs.getString("nameRoom"),
                        rs.getString("creater")
                ));
            }

            JDBCUtil.closeConnection(connection);
        }catch (Exception e){
            e.printStackTrace();
        }
        return arrayList;
    }

    public static void newRoomChat(String nameRoom) {
        String sql = "create table " + nameRoom + " (" +
                "id int primary key auto_increment, " +
                "sender varchar(255)," +
                "message varchar(255)," +
                "created_at varchar(255)" +
                ")";
        String sql2 = "create table " + nameRoom + "members (" +
                "id int primary key auto_increment, " +
                "member varchar(255)" +
                ");";
        try {
            Connection connection = JDBCUtil.getConnection();
            PreparedStatement pst = connection.prepareStatement(sql);
            PreparedStatement pst2 = connection.prepareStatement(sql2);
            pst.execute();
            pst2.execute();

            JDBCUtil.closeConnection(connection);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addNewAccountToRoomMembers(String roomName, String senderAccount) {
        String sql = "insert into " + roomName + "members(member) values(?)";
        try {
            Connection connection = JDBCUtil.getConnection();
            PreparedStatement pst = connection.prepareStatement(sql);
            pst.setString(1, senderAccount);
            pst.execute();
            JDBCUtil.closeConnection(connection);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addNewRoomToRoomList(String creater ,String nameRoom) {
        String sql = "insert into roomlist(creater, nameRoom) values(?, ?)";
        try {
            Connection connection = JDBCUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, creater);
            preparedStatement.setString(2, nameRoom);
            preparedStatement.execute();

            JDBCUtil.closeConnection(connection);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isNameRoomExist(String nameRoom) {
        String sql = "select * from roomlist where nameRoom = ?";
        try {
            Connection connection = JDBCUtil.getConnection();
            PreparedStatement pst = connection.prepareStatement(sql);
            pst.setString(1, nameRoom);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) return true;

            JDBCUtil.closeConnection(connection);
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public static ArrayList<String> getListRoomMembers(String nameRoom) {
        String sql = "select * from " + nameRoom + "members";
        ArrayList<String> members = new ArrayList<>();
        try {
            Connection connection = JDBCUtil.getConnection();
            PreparedStatement pst = connection.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                members.add(rs.getString("member"));
            }

            JDBCUtil.closeConnection(connection);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return members;
    }

    public static RoomChat getChatRoomByNameRoom(String nameRoom) {
        RoomChat roomChat = null;
        String sql = "select * from roomlist where nameRoom = ?";
        try {
            Connection connection = JDBCUtil.getConnection();
            PreparedStatement pst = connection.prepareStatement(sql);
            pst.setString(1, nameRoom);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) roomChat = new RoomChat(
                    rs.getString("nameRoom"),
                    rs.getString("creater")
            );
            JDBCUtil.closeConnection(connection);
        }catch (Exception e){
            e.printStackTrace();
        }
        return roomChat;
    }

    public static ArrayList<Message> getMessagesByRoom(RoomChat roomChat) {
        ArrayList<Message> messages = new ArrayList<>();
        String sql = "select * from " + roomChat.getNameRoom();
        try {
            Connection connection = JDBCUtil.getConnection();
            PreparedStatement pst = connection.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                messages.add(new Message(
                        rs.getString("sender"),
                        rs.getString("message"),
                        rs.getString("created_at")
                ));
            }

            JDBCUtil.closeConnection(connection);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return messages;
    }

    public static void saveMessage(SendMessage sendMessage) {
        String sender = UserDAO.getUserByAccount(sendMessage.getSender()).getAccount();
        String sql = "insert into " + sendMessage.getToRoom() + "(sender, message, created_at) values(?,?,?)";
        Connection connection = JDBCUtil.getConnection();
        try {
            PreparedStatement pst = connection.prepareStatement(sql);
            pst.setString(1, sender);
            pst.setString(2, sendMessage.getMessage());
            pst.setString(3, LocalDateTime.now().toString());
            pst.execute();
            JDBCUtil.closeConnection(connection);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
