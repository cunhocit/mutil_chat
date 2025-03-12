package org.example.handler;

import org.example.dao.RoomChatDAO;
import org.example.model.JsonResponse;
import org.example.model.SendMessage;
import org.example.model.User;
import org.example.security.Encrypt;
import org.example.server.ChatServer;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable{
    private Socket socket;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private String clientID;
    private String account;

    public ClientHandler(Socket socket, String clientID, ObjectInputStream ois, ObjectOutputStream oos) {
        this.socket = socket;
        this.clientID = clientID;
        this.ois = ois;
        this.oos = oos;
    }

    @Override
    public void run() {
        try {
            while (true) {
                JsonResponse jsonResponse = (JsonResponse) ois.readObject();
                handlerClientRequest(jsonResponse);
            }
        }catch(Exception e) {
            System.out.println("...Một máy khách vừa ngắt kết nối...");
        }
    }

    private void handlerClientRequest(JsonResponse jsonResponse) {
        String command = Encrypt.AESDecrypt(jsonResponse.getCommand());
        switch (command) {
            case "register": {
                User user = (User) jsonResponse.getResult();
                String clientID = Encrypt.AESDecrypt(jsonResponse.getClientID());
                UserHandler.Register(user, clientID);
                break;
            }
            case "login": {
                User user = (User) jsonResponse.getResult();
                String clientID = Encrypt.AESDecrypt(jsonResponse.getClientID());
                UserHandler.Login(user, clientID);
                break;
            }
            case "get_room_list": {
                try {
                    oos.writeObject(new JsonResponse(
                            Encrypt.AESEncrypt("return_room_list"),
                            Encrypt.AESEncryptRoomChatArrayList(RoomChatDAO.getRoomList())
                    ));
                    oos.flush();
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
            }
            case "new_chat_room": {
                ArrayList<String> data = Encrypt.AESDecryptArrayList((ArrayList<String>) jsonResponse.getResult());
                String roomName = data.get(0);
                String createrName = data.get(1);
                String clientID = Encrypt.AESDecrypt(jsonResponse.getClientID());
                RoomChatHandler.newRoomChat(roomName, createrName, clientID);
                break;
            }
            case "get_info_chat_room": {
                String roomName = Encrypt.AESDecrypt((String) jsonResponse.getResult());
                String clientID = Encrypt.AESDecrypt(jsonResponse.getClientID());
                RoomChatHandler.returnChatHistory(clientID, roomName);
                break;
            }
            case "send_message": {
                SendMessage sendMessage = Encrypt.AESDecryptSendMessage(
                        (SendMessage) jsonResponse.getResult()
                );
                RoomChatHandler.handlerResponseMessage(sendMessage);
                break;
            }
        }
    }

    public ObjectInputStream getOis() {
        return ois;
    }

    public ObjectOutputStream getOos() {
        return oos;
    }

    public void setOos(ObjectOutputStream oos) {
        this.oos = oos;
    }

    public void setOis(ObjectInputStream ois) {
        this.ois = ois;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public Socket getSocket() {
        return socket;
    }

    public String getClientID() {
        return clientID;
    }

    public String getAccount() {
        return account;
    }
}
