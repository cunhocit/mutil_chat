package org.example.controller;

import org.example.model.JsonResponse;
import org.example.model.Message;
import org.example.model.RoomChat;
import org.example.model.User;
import org.example.security.Encrypt;
import org.example.view.ChatView;
import org.example.view.LoginView;

import javax.swing.*;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class TCPSocketConnection implements Runnable{
    private String hostname = "localhost";
    private int PORT = 12345;
    private static Socket socket;
    private ObjectOutputStream oos; // gửi
    private ObjectInputStream ois; // nhận
    private static String clientID;
    private static String account;
    private static String name;

    public TCPSocketConnection() {
        try {
            socket = new Socket(hostname, PORT);
            oos = new ObjectOutputStream(socket.getOutputStream());
            oos.flush();
            ois = new ObjectInputStream(socket.getInputStream());

            new Thread(this).start();
        }catch (Exception e){
            System.out.println("Kết nối thất bại, có lỗi xảy ra...");
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                JsonResponse response = (JsonResponse) ois.readObject();
                responseHandler(response);
            }
        }catch (Exception e){
            System.out.println("Có lỗi xảy ra...");
            e.printStackTrace();
        }
    }

    private void responseHandler(JsonResponse response) {
        String command = Encrypt.AESDecrypt(response.getCommand());
        System.out.println("COMMAND: " + command);
        switch (command) {
            case "server_connect": {
                String id = Encrypt.AESDecrypt(response.getClientID());
                TCPSocketConnection.setClientID(id);
                System.out.println("Client ID: " + TCPSocketConnection.clientID);
                break;
            }
            case "new_room_chat_failed":
            case "login_failed":
            case "register_failed":
            case "register_success":{
                String message = Encrypt.AESDecrypt((String) response.getResult());
                JOptionPane.showMessageDialog(null, message);
                break;
            }
            case "login_successful": {
                User user = (User) response.getResult();
                setAccount(Encrypt.AESDecrypt(user.getAccount()));
                setName(Encrypt.AESDecrypt(user.getName()));
                LoginView.frame.dispose();
                new ChatViewController(new ChatView(getName()), this);

                try {
                    getOos().writeObject(new JsonResponse(
                            Encrypt.AESEncrypt("get_room_list"),
                            Encrypt.AESEncrypt(""),
                            Encrypt.AESEncrypt(clientID)
                    ));
                    getOos().flush();
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
            }
            case "new_room_chat_successful": {
                ArrayList<RoomChat> roomList = Encrypt.AESDecryptRoomChatArrayList(
                        (ArrayList<RoomChat>) response.getResult()
                );
                ChatViewController.setRoomList(roomList);
                ChatViewController.repaintRoomList();
                System.out.println(roomList);
                break;
            }
            case "return_room_list": {
                ArrayList<RoomChat> roomList = Encrypt.AESDecryptRoomChatArrayList(
                        (ArrayList<RoomChat>) response.getResult()
                );
                ChatViewController.setRoomList(roomList);
                ChatViewController.repaintRoomList();
                break;
            }
            case "return_chat_history": {
                RoomChat roomChat = (RoomChat) response.getResult();
                RoomChat newRoomChat = new RoomChat(
                        Encrypt.AESDecrypt(roomChat.getNameRoom()),
                        Encrypt.AESDecrypt(roomChat.getCreater()),
                        Encrypt.AESDecryptMessageArrayList(roomChat.getMessages())
                );
                ChatViewController.showInfoChatRoom(newRoomChat);
                break;
            }
            case "update_message": {
                try {
                    oos.writeObject(new JsonResponse(
                            Encrypt.AESEncrypt("get_info_chat_room"),
                            response.getResult(),
                            getClientIDEncrypt()
                    ));
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
            }
            default:
                System.out.println("Có lỗi xảy ra...!");
        }
    }

    public static String getName() {
        return name;
    }

    public static String getAccount() {
        return account;
    }

    public String getClientID() {
        return clientID;
    }

    public String getClientIDEncrypt() {
        return Encrypt.AESEncrypt(clientID);
    }

    public String getHostname() {
        return hostname;
    }

    public int getPORT() {
        return PORT;
    }

    public static Socket getSocket() {
        return socket;
    }

    public ObjectOutputStream getOos() {
        return oos;
    }

    public ObjectInputStream getOis() {
        return ois;
    }

    public static void setName(String name) {
        TCPSocketConnection.name = name;
    }

    public static void setAccount(String account) {
        TCPSocketConnection.account = account;
    }

    public static void setClientID(String clientID) {
        TCPSocketConnection.clientID = clientID;
    }
}
