package org.example.controller;

import org.example.model.JsonResponse;
import org.example.model.User;
import org.example.security.Encrypt;
import org.example.valid.AuthValid;
import org.example.view.LoginView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Socket;

public class AuthController implements ActionListener {
    private LoginView loginView;
    private static TCPSocketConnection TCPSocket;

    public AuthController(LoginView loginView, TCPSocketConnection TCPSocket) {
        this.loginView = loginView;
        this.TCPSocket = TCPSocket;
        addActionListener();
    }

    private void addActionListener() {
        loginView.btnLogin.addActionListener(this);
        loginView.btnRegister.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (command.equals("login")) Login();
        if (command.equals("register")) Register();
    }

    private static void Login() {
        try {
            String account = LoginView.accountTextField.getText();
            String password = LoginView.passwordTextField.getText();
            if (validInfo(account, password)) {
                User user = new User(
                        Encrypt.AESEncrypt(account),
                        Encrypt.hashPassword(password)
                );

                JsonResponse jsonResponse = new JsonResponse(
                        Encrypt.AESEncrypt("login"),
                        user,
                        TCPSocket.getClientIDEncrypt()
                );

                TCPSocket.getOos().writeObject(jsonResponse);
                TCPSocket.getOos().flush();
                clearLoginForm();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void Register() {
        try {
            String account = LoginView.accountTextField.getText();
            String password = LoginView.passwordTextField.getText();
            if (validInfo(account, password)) {
                User user = new User(
                        Encrypt.AESEncrypt(account),
                        Encrypt.hashPassword(password)
                );

                JsonResponse jsonResponse = new JsonResponse(
                        Encrypt.AESEncrypt("register"),
                        user,
                        TCPSocket.getClientIDEncrypt()
                );
                TCPSocket.getOos().writeObject(jsonResponse);
                TCPSocket.getOos().flush();
                clearLoginForm();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void clearLoginForm() {
        LoginView.accountTextField.setText("");
        LoginView.passwordTextField.setText("");
    }

    private static boolean validInfo(String account, String password) {
        if (!AuthValid.checkNullTextField(account)) {
            JOptionPane.showMessageDialog(null, "Vui lòng không bỏ trống thông tin!");
            return false;
        }
        if (!AuthValid.checkNullTextField(password)) {
            JOptionPane.showMessageDialog(null, "Vui lòng không bỏ trống thông tin!");
            return false;
        }
        if (!AuthValid.checkLengthTextField(account)) {
            JOptionPane.showMessageDialog(null, "Tài khoản phải dài hơn 6 ký tự!");
            return false;
        }
        if (!AuthValid.checkLengthTextField(password)) {
            JOptionPane.showMessageDialog(null, "Mật khẩu phải dài hơn 6 ký tự!");
            return false;
        }
        if (!AuthValid.isPasswordDifferentFromAccount(account, password)) {
            JOptionPane.showMessageDialog(null, "Mât khẩu và tài khoản không được giống nhau!");
            return false;
        }
        return true;
    }
}
