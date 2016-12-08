package gui.accounts;

import domain.User;
import domain.enums.Role;
import persistence.uow.UnitOfWork;
import service.UserService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.SQLException;

public class AccountsPanel extends JPanel {

    private UserService userService;
    private User userSelected;
    private UnitOfWork unitOfWork;

    private JPanel accountsPanelRight;

    private DefaultListModel<User> accountsListModel;
    private JList accountsJList;
    private JLabel loginAccount;
    private JPanel infosAccountPanel;
    private JTextField nameAccountField;
    private JTextField firstnameAccountField;
    private JRadioButton adminButton;
    private JRadioButton userButton;
    private JButton updateAccountButton;
    private JButton deleteAccountButton;
    private ButtonGroup rolesGroup;

    public AccountsPanel() {
        this.userService = UserService.getInstance();
        this.unitOfWork = UnitOfWork.getInstance();
        this.setLayout(new BorderLayout(0, 0));

        initAccountsList();
        initAccountsPanelRight();
    }

    public void initAccountsList() {
        try {
            accountsListModel = new DefaultListModel<>();
            accountsJList = new JList(accountsListModel);
            java.util.List<User> accountsList = userService.findAll();

            for (User user : accountsList) {
                accountsListModel.addElement(user);
            }
            accountsJList.setVisibleRowCount(10);
            accountsJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            this.add(new JScrollPane(accountsJList));
            accountsJList.addListSelectionListener((ListSelectionEvent e) -> {
                if (!e.getValueIsAdjusting()) {
                    if (this.userSelected != null) {
                        updateUserInfos();
                    }
                    userSelected = (User) accountsJList.getSelectedValue();

                    if (userSelected != null) {
                        setVisibleAccountInfos(true);
                        loginAccount.setText(userSelected.getLogin());
                        nameAccountField.setText(userSelected.getLastname());
                        firstnameAccountField.setText(userSelected.getFirstname());
                        if (userSelected.getRole().equals(Role.USER_ADMIN)) {
                            adminButton.setSelected(true);
                            userButton.setSelected(false);
                        } else {
                            userButton.setSelected(true);
                            adminButton.setSelected(false);
                        }
                    } else {
                        setVisibleAccountInfos(false);
                    }
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void initAccountsPanelRight() {
        accountsPanelRight = new JPanel();
        accountsPanelRight.setLayout(new BorderLayout(0, 0));
        accountsPanelRight.setOpaque(true);
        accountsPanelRight.setPreferredSize(new Dimension(400, 50));
        accountsPanelRight.setLayout(new BoxLayout(accountsPanelRight, BoxLayout.Y_AXIS));
        accountsPanelRight.setBorder(new EmptyBorder(0, 10, 10, 10));

        loginAccount = new JLabel();
        loginAccount.setFont(new Font(loginAccount.getFont().getName(), loginAccount.getFont().getStyle(), 18));
        loginAccount.setHorizontalTextPosition(11);
        loginAccount.setText("");
        loginAccount.setVerticalAlignment(1);
        loginAccount.setVerticalTextPosition(1);
        loginAccount.setBorder(new EmptyBorder(0, 0, 10, 0));
        this.accountsPanelRight.add(loginAccount, BorderLayout.WEST);

        infosAccountPanel = new JPanel();
        nameAccountField = new JTextField();
        firstnameAccountField = new JTextField();

        infosAccountPanel.setLayout(new GridLayout(3, 3));
        infosAccountPanel.add(new JLabel("Nom"));
        infosAccountPanel.add(nameAccountField);
        infosAccountPanel.add(new JLabel("Prénom"));
        infosAccountPanel.add(firstnameAccountField);
        accountsPanelRight.add(infosAccountPanel);

        adminButton = new JRadioButton("Administrateur");
        adminButton.setActionCommand(Role.USER_ADMIN.toString());
        userButton = new JRadioButton("Utilisateur");
        userButton.setActionCommand(Role.USER_DEFAULT.toString());
        rolesGroup = new ButtonGroup();
        rolesGroup.add(adminButton);
        rolesGroup.add(userButton);
        accountsPanelRight.add(adminButton);
        accountsPanelRight.add(userButton);

        updateAccountButton = new JButton("Enregistrer");
        updateAccountButton.addActionListener((ActionEvent e) -> {
            if (this.userSelected != null) {
                updateUserInfos();
            }
            this.unitOfWork.commit();
            JOptionPane.showMessageDialog(new JFrame(), "Mise à jour effectuée.");
        });
        accountsPanelRight.add(updateAccountButton);

        deleteAccountButton = new JButton("Supprimer");
        deleteAccountButton.addActionListener((ActionEvent e) -> {
            this.userService.delete(userSelected);
            this.accountsListModel.remove(accountsJList.getSelectedIndex());
            JOptionPane.showMessageDialog(new JFrame(), "Suppression effectuée.");
        });
        accountsPanelRight.add(deleteAccountButton);

        this.add(accountsPanelRight, BorderLayout.EAST);

        setVisibleAccountInfos(false);
    }

    private void updateUserInfos() {
        this.userSelected.setFirstname(this.firstnameAccountField.getText());
        this.userSelected.setLastname(this.nameAccountField.getText());
        if (this.adminButton.isSelected()) {
            this.userSelected.setRole(Role.USER_ADMIN);
        } else {
            this.userSelected.setRole(Role.USER_DEFAULT);
        }
    }

    private void setVisibleAccountInfos(boolean visible) {
        loginAccount.setVisible(visible);
        infosAccountPanel.setVisible(visible);
        userButton.setVisible(visible);
        adminButton.setVisible(visible);
        updateAccountButton.setVisible(visible);
        deleteAccountButton.setVisible(visible);
    }
}
