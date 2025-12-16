package view;

import manager.LoginManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LoginForm extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JLabel lblStatus;

    LoginManager loginManager = new LoginManager();

    public LoginForm() {
        setTitle("Restaurant Management - Login");
        setSize(420, 320);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setResizable(false);

        // ===== HEADER =====
        JPanel header = new JPanel();
        header.setBackground(new Color(52, 152, 219));
        header.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));

        JLabel lblTitle = new JLabel("ĐĂNG NHẬP HỆ THỐNG", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitle.setForeground(Color.WHITE);

        header.add(lblTitle);
        add(header, BorderLayout.NORTH);

        // ===== FORM =====
        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 15));
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 20, 40));
        formPanel.setBackground(Color.WHITE);

        JLabel lblUser = new JLabel("Username:");
        JLabel lblPass = new JLabel("Password:");
        lblUser.setFont(new Font("Arial", Font.PLAIN, 14));
        lblPass.setFont(new Font("Arial", Font.PLAIN, 14));

        txtUsername = new JTextField();
        txtPassword = new JPasswordField();

        // Thêm padding mềm mại
        txtUsername.setBorder(BorderFactory.createCompoundBorder(
                txtUsername.getBorder(),
                new EmptyBorder(5, 8, 5, 8)
        ));
        txtPassword.setBorder(BorderFactory.createCompoundBorder(
                txtPassword.getBorder(),
                new EmptyBorder(5, 8, 5, 8)
        ));

        formPanel.add(lblUser);
        formPanel.add(txtUsername);
        formPanel.add(lblPass);
        formPanel.add(txtPassword);

        add(formPanel, BorderLayout.CENTER);

        // ===== STATUS =====
        lblStatus = new JLabel("", SwingConstants.CENTER);
        lblStatus.setForeground(Color.RED);
        lblStatus.setFont(new Font("Arial", Font.ITALIC, 13));
        add(lblStatus, BorderLayout.SOUTH);

        // ===== LOGIN BUTTON =====
        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(Color.WHITE);

        JButton btnLogin = new JButton("Đăng nhập");
        btnLogin.setFont(new Font("Arial", Font.BOLD, 14));
        btnLogin.setFocusPainted(false);
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setBackground(new Color(46, 204, 113));
        btnLogin.setPreferredSize(new Dimension(130, 35));
        btnLogin.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        btnLogin.addActionListener(e -> login());

        btnPanel.add(btnLogin);
        add(btnPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void login() {
        String user = txtUsername.getText().trim();
        String pass = new String(txtPassword.getPassword());

        if (loginManager.checkLogin(user, pass)) {
            lblStatus.setForeground(new Color(39, 174, 96));
            lblStatus.setText("Đăng nhập thành công!");

            JOptionPane.showMessageDialog(this,
                    "Xin chào " + user + "!",
                    "Thành công",
                    JOptionPane.INFORMATION_MESSAGE
            );

            new DashboardForm();
            this.dispose();
        } else {
            lblStatus.setForeground(Color.RED);
            lblStatus.setText("Sai username hoặc password!");
        }
    }

    public static void main(String[] args) {
        new LoginForm();
    }
}
