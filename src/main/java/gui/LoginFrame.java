package gui;

import domain.User;
import service.UserService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class LoginFrame extends JFrame {
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

    private UserService userService;

    public LoginFrame() {
        this.userService = UserService.getInstance();
        initFrame();
        initPanels();
        initLabels();
        configConnectButton();
        this.setVisible(true);
    }

    public void initFrame() {
        this.setTitle("Chatbook");
        this.setSize(800, 600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setContentPane(this.mainPanel);
        this.setResizable(false);
    }

    public void initPanels() {
        this.bodyPanel.setBorder(new EmptyBorder(30, 30, 30, 30));
    }

    public void initLabels() {
        this.titleLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
        ImageIcon icon = new ImageIcon(getClass().getResource("/img/logo.png"));
        this.logoLabel.setIcon(icon);
    }

    public void configConnectButton() {
        connectButton.addActionListener(event -> {
            if (this.loginField.getText() != "" && this.passwordField.getPassword().length != 0) {
                try {
                    User user = this.userService.findByCredentials(this.loginField.getText(), new String(this.passwordField.getPassword()));

                    if (user != null) {
                        this.errorLabel.setText("Bienvenue" + user);
                    } else {
                        this.errorLabel.setText("Identifiant / Mot de passe incorrects");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                this.errorLabel.setText("Veuillez remplir l'ensemble des champs");
            }
        });
    }
}
