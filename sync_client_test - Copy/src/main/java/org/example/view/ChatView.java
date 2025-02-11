package org.example.view;

import org.example.controller.ChatViewController;

import javax.swing.*;
import java.awt.*;

public class ChatView extends JFrame {
    private JFrame frame;
    private JTextField idJoinRoom;
    public static JButton btnNewRoom;
    public static JPanel panel_2;
    public static JPanel panel_1;
    public static JTextField messageInput;
    public static JTextArea textArea;
    public static JLabel lblRoomName;
    public static JPanel panel_3;
    public static JButton btnSendMessage;

    public ChatView(String name) {
        initialize(name);
    }

    private void initialize(String name) {
        frame = new JFrame();
        frame.setTitle(name);
        frame.setBounds(100, 100, 811, 510);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        panel_1 = new JPanel();
        panel_1.setBounds(260, 10, 527, 458);
        panel_1.setBorder(BorderFactory.createTitledBorder("Chat"));
        panel_1.setLayout(new BorderLayout());
        frame.getContentPane().add(panel_1);

        panel_3 = new JPanel();
        panel_1.add(panel_3, BorderLayout.CENTER);
        panel_3.setLayout(new BorderLayout(0, 0));

        JPanel panel_4 = new JPanel();
        panel_3.add(panel_4, BorderLayout.SOUTH);
        panel_4.setLayout(new BorderLayout(0, 0));

        messageInput = new JTextField();
        panel_4.add(messageInput);
        messageInput.setColumns(10);

        btnSendMessage = new JButton("Gửi");
        panel_4.add(btnSendMessage, BorderLayout.EAST);

        JPanel panel_5 = new JPanel();
        panel_3.add(panel_5, BorderLayout.NORTH);
        panel_5.setLayout(new BorderLayout(0, 0));

        lblRoomName = new JLabel("Phòng chat: <tên phòng> (chủ phòng)");
        panel_5.add(lblRoomName);

        JPanel panel_6 = new JPanel();
        panel_3.add(panel_6, BorderLayout.CENTER);
        panel_6.setLayout(new BorderLayout(0, 0));

        textArea = new JTextArea();
        textArea.setEditable(false);
        panel_6.add(textArea, BorderLayout.CENTER);

        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder("Chức năng"));
        panel.setBounds(10, 260, 240, 210);
        frame.getContentPane().add(panel);
        panel.setLayout(null);

        idJoinRoom = new JTextField();
        idJoinRoom.setBorder(BorderFactory.createTitledBorder("Nhập mã phòng"));
        idJoinRoom.setBounds(10, 20, 220, 36);
        panel.add(idJoinRoom);
        idJoinRoom.setColumns(10);

        JButton btnJoinRoom = new JButton("Vào phòng");
        btnJoinRoom.setBounds(10, 64, 220, 36);
        panel.add(btnJoinRoom);

        btnNewRoom = new JButton("Tạo phòng chat");
        btnNewRoom.setBounds(10, 110, 220, 36);
        btnNewRoom.setActionCommand("new chat room");
        panel.add(btnNewRoom);

        JButton btnNewLogout = new JButton("Đăng xuất");
        btnNewLogout.setBounds(10, 156, 220, 36);
        panel.add(btnNewLogout);

        panel_2 = new JPanel();
        panel_2.setBorder(BorderFactory.createTitledBorder("Phòng chat"));
        panel_2.setBounds(10, 10, 240, 252);
        panel_2.setLayout(new BorderLayout());
        frame.getContentPane().add(panel_2);

        frame.setVisible(true);

        ChatViewController.repaintNullChatView();
        ChatViewController.sendMessage();
    }
}
