package gui;

import service.UserService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class AdminFrame extends AppFrame {
    private JPanel navPanel;
    private JLabel titleLabel;
    private JPanel mainPanel;
    private JPanel bodyPanel;
    private JLabel userLabel;

    private UserService userService;

    public AdminFrame() {
        this.userService = UserService.getInstance();
        initFrame();
        initLabels();
        this.setTitle("Chatbook - Admin");
        this.setContentPane(this.mainPanel);
        this.setVisible(true);
    }

    public void initLabels() {
        this.titleLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
        this.userLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
        this.userLabel.setText(userService.getConnectedUser().getFirstname() + " " + userService.getConnectedUser().getLastname());
    }
}
