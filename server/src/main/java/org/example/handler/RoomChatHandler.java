package org.example.handler;

import org.example.dao.RoomChatDAO;
import org.example.model.JsonResponse;
import org.example.model.Message;
import org.example.model.RoomChat;
import org.example.model.SendMessage;
import org.example.security.Encrypt;
import org.example.server.ChatServer;

import java.util.ArrayList;

public class RoomChatHandler {
    public synchronized static void newRoomChat(String nameRoom, String createrName, String clientID) {
        try {
            ClientHandler clientHandler = ChatServer.getClientHandler(clientID);
            if (RoomChatDAO.isNameRoomExist(nameRoom)) {
                JsonResponse jsonResponse = new JsonResponse(
                        Encrypt.AESEncrypt("new_room_chat_failed"),
                        Encrypt.AESEncrypt("Tên phòng đã tồn tại!"),
                        Encrypt.AESEncrypt(clientHandler.getClientID())
                );
                clientHandler.getOos().writeObject(jsonResponse);
                clientHandler.getOos().flush();
                return;
            }

            RoomChatDAO.newRoomChat(nameRoom);
            RoomChatDAO.addNewRoomToRoomList(createrName, nameRoom);
            RoomChatDAO.addNewAccountToRoomMembers(nameRoom, clientHandler.getAccount());
//            JsonResponse jsonResponse = new JsonResponse(
//                    Encrypt.AESEncrypt("new_room_chat_successful"),
//                    Encrypt.AESEncryptRoomChatArrayList(RoomChatDAO.getRoomList()),
//                    Encrypt.AESEncrypt(clientHandler.getClientID())
//            );
//            clientHandler.getOos().writeObject(jsonResponse);
//            clientHandler.getOos().flush();

            for (ClientHandler c : ChatServer.getClientHandlers().values()) {
                JsonResponse jsonResponse = new JsonResponse(
                        Encrypt.AESEncrypt("new_room_chat_successful"),
                        Encrypt.AESEncryptRoomChatArrayList(RoomChatDAO.getRoomList()),
                        Encrypt.AESEncrypt(clientHandler.getClientID())
                );
                c.getOos().writeObject(jsonResponse);
                c.getOos().flush();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized static void returnChatHistory (String clientID, String roomName) {
        try {
            ClientHandler clientHandler = ChatServer.getClientHandler(clientID);
            RoomChat roomChat = RoomChatDAO.getChatRoomByNameRoom(roomName);
            if (roomChat != null) {
                ArrayList<Message> messages = RoomChatDAO.getMessagesByRoom(roomChat);
                JsonResponse jsonResponse = new JsonResponse(
                        Encrypt.AESEncrypt("return_chat_history"),
                        new RoomChat(
                                Encrypt.AESEncrypt(roomChat.getNameRoom()),
                                Encrypt.AESEncrypt(roomChat.getCreater()),
                                Encrypt.AESEncryptMessageArrayList(messages)
                        ),
                        Encrypt.AESEncrypt(clientHandler.getClientID())
                );
                clientHandler.getOos().writeObject(jsonResponse);
                clientHandler.getOos().flush();
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public synchronized static void handlerResponseMessage(SendMessage sendMessage) {
        try {
            RoomChatDAO.saveMessage(sendMessage);
            ArrayList<String> memberAccounts = RoomChatDAO.getListRoomMembers(sendMessage.getToRoom());

            for (ClientHandler c : ChatServer.getClientHandlers().values()) {
                c.getOos().writeObject(new JsonResponse(
                        Encrypt.AESEncrypt("update_message"),
                        Encrypt.AESEncrypt(sendMessage.getToRoom())
                ));
                c.getOos().flush();
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
