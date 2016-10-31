package gui;

import domain.User;
import service.UserService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.SQLException;

public class LoginFrame extends AppFrame {
    private JPanel mainPanel;
    private JLabel titleLabel;
    private JPanel navPanel;
    private JPanel bodyPanel;
    private JLabel logoLabel;
    private JPanel formPanel;
    private JTextField loginField;
    private JLabel loginLabel;
    private JLabel passwordLabel;
    private JPasswordField passwordField;
    private JButton connectButton;
    private JLabel errorLabel;
    private JLabel userLabel;

    private UserService userService;

    public LoginFrame() {
        this.userService = UserService.getInstance();
        initFrame();
        initPanels();
        initLabels();
        configConnectButton();
        this.setContentPane(this.mainPanel);
        this.setVisible(true);
    }

    public void initPanels() {
        this.bodyPanel.setBorder(new EmptyBorder(30, 30, 30, 30));
    }

    public void initLabels() {
        this.titleLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
        this.userLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
        ImageIcon icon = new ImageIcon(getClass().getResource("/img/logo.png"));
        this.logoLabel.setIcon(icon);
    }

    public void configConnectButton() {
        connectButton.addActionListener(event -> {
            if (!fieldEmpty()) {
                try {
                    checkUser(this.userService.findByCredentials(this.loginField.getText(), new String(this.passwordField.getPassword())));
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                this.errorLabel.setText("Veuillez remplir l'ensemble des champs");
            }
        });
    }

    public boolean fieldEmpty() {
        return this.loginField.getText() == "" || this.passwordField.getPassword().length == 0;
    }

    public void checkUser(User user) {
        if (user != null) {
            this.setVisible(false);
            if (user.getRole().equals("USER_ADMIN")) {
                new AdminFrame();
            } else {
                new UserFrame();
            }
        } else {
            this.errorLabel.setText("Identifiant / Mot de passe incorrects");
        }
    }
}
