package gui.accounts;

import domain.IUser;
import domain.enums.ERole;
import gui.components.FormUtility;
import persistence.uow.UnitOfWork;
import service.UserService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;
import java.awt.event.ActionEvent;

public class AccountsPanel extends JPanel {

    private UserService userService;
    private IUser userSelected;
    private UnitOfWork unitOfWork;

    private JPanel accountsPanelRight;

    private DefaultListModel<IUser> accountsListModel;
    private JList accountsJList;
    private JTextField loginAccountField;
    private JPanel infosAccountPanel;
    private JTextField nameAccountField;
    private JTextField firstnameAccountField;
    private JRadioButton adminButton;
    private JRadioButton userButton;
    private JButton createAccountButton;
    private JButton updateAccountButton;
    private JButton deleteAccountButton;
    private JButton passwordAccountButton;
    private ButtonGroup rolesGroup;

    public AccountsPanel() {
        this.userService = UserService.getInstance();
        this.unitOfWork = UnitOfWork.getInstance();
        this.setLayout(new BorderLayout(0, 0));

        initCreateAccountButton();
        initAccountsList();
        initAccountsPanelRight();
    }

    private void initCreateAccountButton() {
        createAccountButton = new JButton("Créer un nouvel utilisateur");
        createAccountButton.addActionListener((ActionEvent e) -> new CreateAccountFrame(accountsListModel));
        this.add(createAccountButton, BorderLayout.NORTH);
    }

    private void initAccountsList() {
        accountsListModel = new DefaultListModel<>();
        accountsJList = new JList(accountsListModel);
        accountsJList.setFixedCellHeight(30);
        accountsJList.setSelectionBackground(new Color(59, 89, 152));
        java.util.List<IUser> accountsList = userService.findAll();
        for (IUser user : accountsList) {
            if (user.getId() != userService.getConnectedUser().getId()) {
                accountsListModel.addElement(user);
            }
        }
        accountsJList.setVisibleRowCount(10);
        accountsJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.add(new JScrollPane(accountsJList));
        accountsJList.addListSelectionListener((ListSelectionEvent e) -> {
            if (!e.getValueIsAdjusting()) {
                userSelected = (IUser) accountsJList.getSelectedValue();
                if (userSelected != null) {
                    setVisibleAccountInfos(true);
                    loginAccountField.setText(userSelected.getLogin());
                    nameAccountField.setText(userSelected.getLastname());
                    firstnameAccountField.setText(userSelected.getFirstname());
                    if (userSelected.getRole().equals(ERole.USER_ADMIN)) {
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
    }

    private void initAccountsPanelRight() {
        accountsPanelRight = new JPanel();
        accountsPanelRight.setLayout(new BorderLayout());
        accountsPanelRight.setOpaque(true);
        accountsPanelRight.setPreferredSize(new Dimension(400, 100));
        accountsPanelRight.setBorder(new EmptyBorder(0, 10, 10, 10));

        loginAccountField = new JTextField();
        loginAccountField.setFont(new Font(loginAccountField.getFont().getName(), loginAccountField.getFont().getStyle(), 24));
        loginAccountField.setHorizontalAlignment(0);
        loginAccountField.setBorder(new EmptyBorder(0, 0, 10, 0));
        accountsPanelRight.add(loginAccountField, BorderLayout.NORTH);

        infosAccountPanel = new JPanel();
        infosAccountPanel.setLayout(new GridBagLayout());
        firstnameAccountField = new JTextField();
        nameAccountField = new JTextField();

        FormUtility formUtility = new FormUtility();

        formUtility.addLabel("Prénom :", infosAccountPanel);
        formUtility.addLastField(firstnameAccountField, infosAccountPanel);
        formUtility.addLabel("Nom :", infosAccountPanel);
        formUtility.addLastField(nameAccountField, infosAccountPanel);

        infosAccountPanel.add(new JLabel("Role :", SwingConstants.RIGHT));
        adminButton = new JRadioButton("Administrateur");
        adminButton.setActionCommand(ERole.USER_ADMIN.toString());
        userButton = new JRadioButton("Utilisateur");
        userButton.setActionCommand(ERole.USER_DEFAULT.toString());
        rolesGroup = new ButtonGroup();
        rolesGroup.add(adminButton);
        rolesGroup.add(userButton);
        infosAccountPanel.add(adminButton);
        infosAccountPanel.add(userButton);

        infosAccountPanel.setAlignmentY(JPanel.TOP_ALIGNMENT);

        accountsPanelRight.add(infosAccountPanel, BorderLayout.CENTER);

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));

        updateAccountButton = new JButton("Enregistrer");
        updateAccountButton.addActionListener((ActionEvent e) -> {
            if (this.userSelected != null) {
                updateUserInfos();
            }
            this.unitOfWork.commit();
            JOptionPane.showMessageDialog(new JFrame(), "Mise à jour effectuée.");
        });
        updateAccountButton.setAlignmentX(JButton.CENTER_ALIGNMENT);
        buttonsPanel.add(updateAccountButton);

        deleteAccountButton = new JButton("Supprimer");
        deleteAccountButton.addActionListener((ActionEvent e) -> {
            this.userService.delete(userSelected);
            this.accountsListModel.remove(accountsJList.getSelectedIndex());
            JOptionPane.showMessageDialog(new JFrame(), "Suppression effectuée.");
        });
        deleteAccountButton.setAlignmentX(JButton.CENTER_ALIGNMENT);
        buttonsPanel.add(deleteAccountButton);

        passwordAccountButton = new JButton("Modifier le mot de passe");
        passwordAccountButton.addActionListener((ActionEvent e) -> new UpdatePasswordAccountFrame(userSelected));
        passwordAccountButton.setAlignmentX(JButton.CENTER_ALIGNMENT);
        buttonsPanel.add(passwordAccountButton);

        accountsPanelRight.add(buttonsPanel, BorderLayout.SOUTH);

        this.add(accountsPanelRight, BorderLayout.EAST);

        setVisibleAccountInfos(false);
    }

    private void updateUserInfos() {
        this.userSelected.setLogin(this.loginAccountField.getText());
        this.userSelected.setFirstname(this.firstnameAccountField.getText());
        this.userSelected.setLastname(this.nameAccountField.getText());
        if (this.adminButton.isSelected()) {
            this.userSelected.setRole(ERole.USER_ADMIN);
        } else {
            this.userSelected.setRole(ERole.USER_DEFAULT);
        }
    }

    private void setVisibleAccountInfos(boolean visible) {
        loginAccountField.setVisible(visible);
        infosAccountPanel.setVisible(visible);
        userButton.setVisible(visible);
        adminButton.setVisible(visible);
        updateAccountButton.setVisible(visible);
        deleteAccountButton.setVisible(visible);
        passwordAccountButton.setVisible(visible);
    }
}
