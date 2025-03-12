package org.example.server;

import com.google.protobuf.Message;
import org.example.dao.RoomChatDAO;
import org.example.handler.ClientHandler;
import org.example.model.JsonResponse;
import org.example.security.Encrypt;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class ChatServer {
    private static final int PORT = 12345;
    private static Map<String, ClientHandler> clientHandlers = new HashMap<>();
    private static String clientID;
    private static ObjectOutputStream oos;
    private static ObjectInputStream ois;

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Server đang chạy trên cổng " + PORT);

            while (true) {
                Socket socket = serverSocket.accept();

                oos = new ObjectOutputStream(socket.getOutputStream());
                ois = new ObjectInputStream(socket.getInputStream());

                clientID = UUID.randomUUID().toString();

                System.out.println("...Máy khách " + clientID + " vừa kết nối đến server...");

                JsonResponse jsonResponse = new JsonResponse(
                        Encrypt.AESEncrypt("server_connect"),
                        Encrypt.AESEncrypt("Kết nối thành công đến chat server...."),
                        Encrypt.AESEncrypt(clientID)
                );
                oos.writeObject(jsonResponse);
                oos.flush();

                ClientHandler clientHandler = new ClientHandler(socket, clientID, ois, oos);
                clientHandlers.put(clientID, clientHandler);
                new Thread(clientHandler).start();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public synchronized static Map<String, ClientHandler> getClientHandlers() {
        return clientHandlers;
    }

    public synchronized static ClientHandler getClientHandler(String clientID) {
        return clientHandlers.get(clientID);
    }

    public synchronized static ObjectOutputStream getOos() {
        return oos;
    }

    public synchronized static ClientHandler getClientHandlerByAccount(String account) {
        for (ClientHandler clientHandler : clientHandlers.values()) {
            if (clientHandler.getAccount().equals(account)) {
                return clientHandler;
            }
        }
        return null;
    }

}