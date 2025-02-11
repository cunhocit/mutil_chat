package org.example.controller;

import org.example.model.JsonResponse;
import org.example.model.Message;
import org.example.model.RoomChat;
import org.example.model.SendMessage;
import org.example.security.Encrypt;
import org.example.view.ChatView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ChatViewController implements ActionListener {
    private static ChatView chatView;
    private static TCPSocketConnection TCPSocket;
    public static ArrayList<RoomChat> roomList;
    public static RoomChat currentChatRoom;

    public ChatViewController (ChatView chatView, TCPSocketConnection TCPSocket) {
        this.chatView = chatView;
        this.TCPSocket = TCPSocket;
        roomList = new ArrayList<>();
        addActionListener();
    }

    public void addActionListener() {
        chatView.btnNewRoom.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (command.equals("new chat room")) newChatRoom();
    }

    private static void newChatRoom() {
        String roomName = JOptionPane.showInputDialog(null, "Nhập tên phòng: ");

        if (roomName.matches(".*[áàảạãâấầẩẫăắằẳẵ].*") || roomName.contains(" ")) {
            JOptionPane.showMessageDialog(null, "Tên phòng không được chứa khoảng trắng hoặc ký tự đặc biệt...");
            return;
        }

        ArrayList<String> data = new ArrayList<>();
        data.add(roomName);
        data.add(TCPSocket.getName());

        JsonResponse jsonResponse = new JsonResponse(
                Encrypt.AESEncrypt("new_chat_room"),
                Encrypt.AESEncryptArrayList(data),
                TCPSocket.getClientIDEncrypt()
        );
        try {
            TCPSocket.getOos().writeObject(jsonResponse);
            TCPSocket.getOos().flush();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void repaintRoomList() {
        String[] columnNames = {"STT", "Tên phòng"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

        for (int i=0; i<roomList.size(); i++) {
            tableModel.addRow(new Object[]{i, roomList.get(i).getNameRoom()});
        }

        JTable table = new JTable(tableModel);
        table.setDefaultEditor(Object.class, null);
        table.getColumnModel().getColumn(0).setPreferredWidth(50);
        table.getColumnModel().getColumn(1).setPreferredWidth(200);

        JScrollPane scrollPane = new JScrollPane(table);

        ChatView.panel_2.removeAll();
        ChatView.panel_2.add(scrollPane);
        getInfoChatRoom(table);
        ChatView.panel_2.revalidate();
        ChatView.panel_2.repaint();
    }

    public static void setRoomList(ArrayList<RoomChat> roomList) {
        ChatViewController.roomList = roomList;
    }

    public static ArrayList<RoomChat> getRoomList() {
        return roomList;
    }

    public static void getInfoChatRoom(JTable table) {
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    String creater = table.getValueAt(selectedRow, 0).toString();
                    String roomName = table.getValueAt(selectedRow, 1).toString();
                    currentChatRoom = new RoomChat(roomName, creater);

                    JsonResponse jsonResponse = new JsonResponse(
                            Encrypt.AESEncrypt("get_info_chat_room"),
                            Encrypt.AESEncrypt(roomName),
                            TCPSocket.getClientIDEncrypt()
                    );
                    try {
                        TCPSocket.getOos().writeObject(jsonResponse);
                        TCPSocket.getOos().flush();
                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
                }else repaintNullChatView();
            }
        });
    }

    public static void showInfoChatRoom(RoomChat roomChat) {
        ChatView.panel_1.removeAll();
        String lblRoomNameStr = "Phòng chat: " + roomChat.getNameRoom() + " ("+ roomChat.getCreater() +")";
        ChatView.lblRoomName.setText(lblRoomNameStr);

        if (!roomChat.getMessages().isEmpty()) {
            ChatView.textArea.setText("");
            for (Message message : roomChat.getMessages()) {
                ChatView.textArea.append(
                        "(" + convertToHourMinute(message.getTimestamp()) + ")" +
                        message.getSender() + ": " +
                        message.getMessage() + "\n"
                );
            }
        }else {
            ChatView.textArea.setText("");
        }

        ChatView.panel_1.add(ChatView.panel_3);
        ChatView.panel_1.revalidate();
        ChatView.panel_1.repaint();
    }

    public static void repaintNullChatView() {
        ChatView.panel_1.removeAll();
        ChatView.panel_1.setLayout(new BorderLayout());
        JLabel label = new JLabel("Hãy chọn 1 phòng chat để tham gia cuộc trò chuyện!");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        ChatView.panel_1.add(label);
        ChatView.panel_1.revalidate();
        ChatView.panel_1.repaint();
    }

    public static void sendMessage() {
        ChatView.btnSendMessage.addActionListener(e -> {
            if (!ChatView.messageInput.getText().isEmpty()) {
                JsonResponse jsonResponse = new JsonResponse(
                        Encrypt.AESEncrypt("send_message"),
                        Encrypt.AESEncryptSendMessage(
                                new SendMessage(
                                        TCPSocket.getAccount(),
                                        currentChatRoom.getNameRoom(),
                                        ChatView.messageInput.getText()
                                )
                        ),
                        TCPSocket.getClientIDEncrypt()
                );
                ChatView.messageInput.setText("");
                try {
                    TCPSocket.getOos().writeObject(jsonResponse);
                    TCPSocket.getOos().flush();
                }catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    public static String convertToHourMinute(String inputTime) {
        // Tạo một formatter để chỉ lấy giờ:phút
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("HH:mm");

        // Chuyển đổi chuỗi đầu vào thành LocalDateTime, nhưng chỉ giữ phần giờ và phút
        LocalDateTime dateTime = LocalDateTime.parse(inputTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        // Trả về chuỗi theo định dạng HH:mm (bỏ giây và micro giây)
        return dateTime.format(outputFormatter);
    }


}
