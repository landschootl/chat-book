package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * Created by lauthieb on 31/10/2016.
 */
public class MainFrame extends JFrame {
    private JPanel mainPanel;
    private JLabel titleLabel;

    public MainFrame() {
        initGui();
    }

    public void initGui() {
        this.setTitle("Chat-Book");
        this.setSize(600, 600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setContentPane(this.mainPanel);
        this.setVisible(true);
        this.titleLabel.setBorder(new EmptyBorder(20, 20, 20, 20));
    }
}
