package gui;

import javax.swing.*;

public abstract class AppFrame extends JFrame {
    public void initFrame() {
        this.setTitle("Chatbook");
        this.setSize(800, 600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
    }
}
