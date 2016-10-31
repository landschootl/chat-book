package gui;

import service.UserService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class UserFrame extends AppFrame {
    private JPanel mainPanel;
    private JPanel navPanel;
    private JLabel titleLabel;
    private JLabel userLabel;
    private JPanel bodyPanel;

    private UserService userService;

    public UserFrame() {
        this.userService = UserService.getInstance();
        initFrame();
        initLabels();
        this.setTitle("Chatbook - " + this.userService.getConnectedUser().getFirstname() + " " + this.userService.getConnectedUser().getLastname());
        this.setContentPane(this.mainPanel);
        this.setVisible(true);
    }

    public void initLabels() {
        this.titleLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
        this.userLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
        this.userLabel.setText(userService.getConnectedUser().getFirstname() + " " + userService.getConnectedUser().getLastname());
    }
}
