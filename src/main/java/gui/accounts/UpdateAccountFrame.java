package gui.accounts;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import domain.User;
import domain.enums.ECrud;
import gui.AppFrame;
import persistence.uow.Observable;
import persistence.uow.Observer;
import service.UserService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class UpdateAccountFrame extends AppFrame implements Observable {
    private List<Observer> obs;

    private UserService userService;

    private JPanel mainPanel;
    private JTextField loginTextField;
    private JTextField lastnameTextField;
    private JTextField firstNameTextField;
    private JButton cancelButton;
    private JLabel errorLabel;
    private JButton saveButton;
    private JButton deleteButton;

    public UpdateAccountFrame() {
        this.obs = new ArrayList<>();
        this.userService = UserService.getInstance();
        initComponents();
        initButtons();
        this.setVisible(true);
    }

    public void initButtons() {
        saveButton.addActionListener((ActionEvent e) -> {
            if (!fieldsEmpty()) {
                User user = User.builder()
                        .id(userService.getConnectedUser().getId())
                        .login(loginTextField.getText())
                        .lastname(lastnameTextField.getText())
                        .firstname(firstNameTextField.getText())
                        .role(userService.getConnectedUser().getRole())
                        .build();
                try {
                    userService.updateAccount(user);
                    userService.setConnectedUser(user);
                    notif(ECrud.UPDATE);
                    this.dispose();
                    JOptionPane.showMessageDialog(new JFrame(), "Compte mise à jour avec succès.");
                } catch (Exception e1) {
                    JOptionPane.showMessageDialog(new JFrame(), "Erreur lors de la mise à jour de l'utilisateur.");
                    e1.printStackTrace();
                }
            } else {
                JOptionPane.showMessageDialog(new JFrame(), "Veuillez remplir l'ensemble des champs.");
            }
        });

        deleteButton.addActionListener((ActionEvent e) -> {
            int dialogResult = JOptionPane.showConfirmDialog(null, "Etes-vous sûr de vouloir supprimer votre compte ?", "Attention", 0);
            if (dialogResult == JOptionPane.YES_OPTION) {
                userService.delete(userService.getConnectedUser());
                JOptionPane.showMessageDialog(new JFrame(), "Utilisateur supprimé avec succès.");
                notif(ECrud.DELETE);
                userService.setConnectedUser(null);
                this.dispose();
            }
        });

        cancelButton.addActionListener((ActionEvent e) -> this.dispose());
    }

    public boolean fieldsEmpty() {
        return this.loginTextField.getText().isEmpty() ||
                this.lastnameTextField.getText().isEmpty() ||
                this.firstNameTextField.getText().isEmpty();
    }

    @Override
    public void initComponents() {
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        this.setContentPane(mainPanel);

        loginTextField.setText(userService.getConnectedUser().getLogin());
        lastnameTextField.setText(userService.getConnectedUser().getLastname());
        firstNameTextField.setText(userService.getConnectedUser().getFirstname());

        this.setTitle("Mon compte");
        this.setSize(400, 230);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
    }

    @Override
    public void addObserver(Observer o) {
        this.obs.add(o);
    }

    @Override
    public void notif(Object o) {
        for (Observer ob : obs)
            ob.action(o);
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayoutManager(9, 3, new Insets(0, 0, 0, 0), -1, -1));
        final Spacer spacer1 = new Spacer();
        mainPanel.add(spacer1, new GridConstraints(0, 2, 3, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Nom");
        mainPanel.add(label1, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lastnameTextField = new JTextField();
        mainPanel.add(lastnameTextField, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Prénom");
        mainPanel.add(label2, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        firstNameTextField = new JTextField();
        mainPanel.add(firstNameTextField, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        errorLabel = new JLabel();
        errorLabel.setForeground(new Color(-3932139));
        errorLabel.setText("");
        mainPanel.add(errorLabel, new GridConstraints(8, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Identifiant");
        mainPanel.add(label3, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        loginTextField = new JTextField();
        mainPanel.add(loginTextField, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        saveButton = new JButton();
        saveButton.setText("Enregistrer");
        mainPanel.add(saveButton, new GridConstraints(5, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        deleteButton = new JButton();
        deleteButton.setText("Supprimer le compte");
        mainPanel.add(deleteButton, new GridConstraints(6, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cancelButton = new JButton();
        cancelButton.setText("Annuler les modifications");
        mainPanel.add(cancelButton, new GridConstraints(7, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }
}
