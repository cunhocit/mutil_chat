package org.example;

import org.example.controller.AuthController;
import org.example.controller.TCPSocketConnection;
import org.example.view.LoginView;

public class Main {
    public static void main(String[] args) {
        TCPSocketConnection socket = new TCPSocketConnection();
        new AuthController(new LoginView(), socket);
    }
}