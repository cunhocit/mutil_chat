package org.example.view;

import javax.swing.*;
import java.awt.*;

public class LoginView extends JFrame {
    public static JFrame frame;
    public static JTextField accountTextField;
    public static JTextField passwordTextField;
    public static JButton btnLogin;
    public static JButton btnRegister;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    LoginView window = new LoginView();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the application.
     */
    public LoginView() {
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 284, 379);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        accountTextField = new JTextField();
        accountTextField.setBorder(BorderFactory.createTitledBorder("Tài khoản"));
        accountTextField.setBounds(32, 80, 207, 37);
        frame.getContentPane().add(accountTextField);
        accountTextField.setColumns(10);

        passwordTextField = new JTextField();
        passwordTextField.setColumns(10);
        passwordTextField.setBorder(BorderFactory.createTitledBorder("Mật khẩu"));
        passwordTextField.setBounds(32, 135, 207, 37);
        frame.getContentPane().add(passwordTextField);

        btnLogin = new JButton("Đăng nhập");
        btnLogin.setBounds(32, 214, 207, 37);
        btnLogin.setActionCommand("login");
        frame.getContentPane().add(btnLogin);

        btnRegister = new JButton("Đăng ký");
        btnRegister.setBounds(32, 261, 207, 37);
        btnRegister.setActionCommand("register");
        frame.getContentPane().add(btnRegister);

        JLabel lblNewLabel = new JLabel("Đăng nhập");
        lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 17));
        lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewLabel.setBounds(32, 22, 207, 48);
        frame.getContentPane().add(lblNewLabel);

        frame.setVisible(true);
    }
}
