package org.example.handler;

import org.example.dao.RoomChatDAO;
import org.example.dao.UserDAO;
import org.example.model.JsonResponse;
import org.example.model.RoomChat;
import org.example.model.User;
import org.example.security.Encrypt;
import org.example.server.ChatServer;

import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

public class UserHandler {
    public synchronized static void Login(User user, String clienID) {
        try {
            String account = Encrypt.AESDecrypt(user.getAccount());
            String password = user.getPassword();
            ClientHandler clientHandler = ChatServer.getClientHandler(clienID);

            if (!UserDAO.isUserExist(account)) {
                JsonResponse jsonResponse = new JsonResponse(
                        Encrypt.AESEncrypt("login_failed"),
                        Encrypt.AESEncrypt("Tài khoản chưa được đăng ký!"),
                        Encrypt.AESEncrypt(clientHandler.getClientID())
                );
                clientHandler.getOos().writeObject(jsonResponse);
                clientHandler.getOos().flush();
                return;
            }

            User user_ = UserDAO.getUserByAccount(account);
            if (user_.getAccount().equals(account) && user_.getPassword().equals(password)) {
                JsonResponse jsonResponse = new JsonResponse(
                        Encrypt.AESEncrypt("login_successful"),
                        new User(
                                Encrypt.AESEncrypt(user_.getAccount()),
                                "",
                                Encrypt.AESEncrypt(user_.getName())
                        ),
                        Encrypt.AESEncrypt(clientHandler.getClientID())
                );
                clientHandler.getOos().writeObject(jsonResponse);
                clientHandler.getOos().flush();
                clientHandler.setAccount(user_.getAccount());
            }else  {
                JsonResponse jsonResponse = new JsonResponse(
                        Encrypt.AESEncrypt("login_failed"),
                        Encrypt.AESEncrypt("Tài khoản hoặc mật khẩu không chính xác!"),
                        Encrypt.AESEncrypt(clientHandler.getClientID())
                );
                clientHandler.getOos().writeObject(jsonResponse);
                clientHandler.getOos().flush();
            }

        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized static void Register(User user, String clientID) {
        try {
            String account = Encrypt.AESDecrypt(user.getAccount());
            String password = user.getPassword();
            ClientHandler clientHandler = ChatServer.getClientHandler(clientID);

            if (UserDAO.isUserExist(account)) {
                JsonResponse jsonResponse = new JsonResponse(
                        Encrypt.AESEncrypt("register_failed"),
                        Encrypt.AESEncrypt("Tài khoản đã tồn tại!"),
                        Encrypt.AESEncrypt(clientHandler.getClientID())
                );
                clientHandler.getOos().writeObject(jsonResponse);
                clientHandler.getOos().flush();
                return;
            }

            UserDAO.addUser(new User(account, password, account + generateKey()));
            JsonResponse jsonResponse = new JsonResponse(
                    Encrypt.AESEncrypt("register_success"),
                    Encrypt.AESEncrypt("Đăng ký thành công!"),
                    Encrypt.AESEncrypt(clientHandler.getClientID())
            );
            clientHandler.getOos().writeObject(jsonResponse);
            clientHandler.getOos().flush();

            for (RoomChat roomChat : RoomChatDAO.getRoomList()) {
                RoomChatDAO.addNewAccountToRoomMembers(roomChat.getNameRoom(), account);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int generateKey() {
        return 1000 + (new Random().nextInt(9000));
    }
}
