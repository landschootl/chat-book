package gui;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import domain.Group;
import domain.User;
import domain.enums.Role;
import persistence.uow.UnitOfWork;
import service.GroupService;
import service.UserService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.util.List;

public class MainFrame extends AppFrame {
    //header
    private JPanel navPanel;
    private JLabel titleLabel;
    private JButton deconnectButton;
    private JPanel mainPanel;
    private JLabel userLabel;
    private JPanel bodyPanel;
    //navbar
    private JPanel dashboardPanel;
    private JButton accountsButton;
    private JButton groupsButton;
    //user panel
    private JPanel userPanel;
    private JPanel contentPanel;
    private JPanel accountsPanel;
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

    //groupe panel
    private JPanel groupsPanel;
    private JPanel accountsPanelRight;
    private JPanel accountsPanelLeft;
    private JList groupsList;
    private DefaultListModel<Group> groupsListModel;
    private JPanel groupsPanelLeft;
    private JPanel groupsPanelRight;
    private JLabel nameGroup;

    private JTextField newMessageTextField;
    private JButton sendButton;


    private UserService userService;
    private GroupService groupService;

    private UnitOfWork unitOfWork;

    private User userSelected;

    public MainFrame() {
        this.unitOfWork = UnitOfWork.getInstance();
        this.userService = UserService.getInstance();
        this.groupService = GroupService.getInstance();
        this.setContentPane(this.mainPanel);
        cleanPanels();
        initFrame();
        initComponents();
        configDeconnectButton();
        configDashboard();
        if (userService.getConnectedUser().getRole().equals(Role.USER_ADMIN)) {
            this.setTitle("Chatbook - Admin");
            this.titleLabel.setText("chatbook - admin");
        } else {
            this.setTitle("Chatbook - " + userService.getConnectedUser().getFirstname() + " " + userService.getConnectedUser().getLastname());
        }
        this.setVisible(true);
    }

    public void cleanPanels() {
        this.accountsPanel.setVisible(false);
        this.groupsPanel.setVisible(false);
    }

    public void initComponents() {
        titleLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
        userLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
        userLabel.setText(userService.getConnectedUser().getFirstname() + " " + userService.getConnectedUser().getLastname());
        dashboardPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        checkComponentRoles();
        initAccountsList();
        initAccountsPanelRight();
        initGroupsList();
    }

    public void initAccountsList() {
        try {
            accountsListModel = new DefaultListModel<>();
            accountsJList = new JList(accountsListModel);
            List<User> accountsList = userService.findAll();

            for (User user : accountsList) {
                accountsListModel.addElement(user);
            }
            accountsJList.setVisibleRowCount(10);
            accountsJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            accountsPanel.add(new JScrollPane(accountsJList));
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

    private void updateUserInfos() {
        this.userSelected.setFirstname(this.firstnameAccountField.getText());
        this.userSelected.setLastname(this.nameAccountField.getText());
        if (this.adminButton.isSelected()) {
            this.userSelected.setRole(Role.USER_ADMIN);
        } else {
            this.userSelected.setRole(Role.USER_DEFAULT);
        }
    }

    public void initAccountsPanelRight() {
        accountsPanelRight.setLayout(new BoxLayout(accountsPanelRight, BoxLayout.Y_AXIS));
        accountsPanelRight.setBorder(new EmptyBorder(0, 10, 10, 10));
        loginAccount.setBorder(new EmptyBorder(0, 0, 10, 0));

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

        setVisibleAccountInfos(false);
    }

    private void setVisibleAccountInfos(boolean visible) {
        loginAccount.setVisible(visible);
        infosAccountPanel.setVisible(visible);
        userButton.setVisible(visible);
        adminButton.setVisible(visible);
        updateAccountButton.setVisible(visible);
        deleteAccountButton.setVisible(visible);
    }

    public void initGroupsList() {
        try {
            groupsListModel = new DefaultListModel<>();
            groupsList = new JList(groupsListModel);
            List<Group> listGroup;
            if (Role.USER_ADMIN.equals(userService.getConnectedUser().getRole())) {
                listGroup = groupService.findAll();
            } else {
                listGroup = groupService.findByUser(userService.getConnectedUser());
            }
            for (Group group : listGroup) {
                groupsListModel.addElement(group);
            }
            groupsList.setVisibleRowCount(10);
            groupsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            groupsPanel.add(new JScrollPane(groupsList));
            groupsList.addListSelectionListener((ListSelectionEvent e) -> {
                if (!e.getValueIsAdjusting()) {
                    Group groupSelected = (Group) groupsList.getSelectedValue();
                    nameGroup.setText(groupSelected.getName());
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void checkComponentRoles() {
        if (!Role.USER_ADMIN.equals(userService.getConnectedUser().getRole())) {
            accountsButton.setVisible(false);
        }
    }

    public void configDashboard() {
        accountsButton.addActionListener((ActionEvent e) -> {
            cleanPanels();
            this.accountsPanel.setVisible(true);
        });
        groupsButton.addActionListener((ActionEvent e) -> {
            cleanPanels();
            this.groupsPanel.setVisible(true);
        });
    }

    public void configDeconnectButton() {
        deconnectButton.addActionListener(e -> {
            this.userService.setConnectedUser(null);
            this.setVisible(false);
            this.unitOfWork.rollback();
            new LoginFrame();
        });
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
        mainPanel.setLayout(new BorderLayout(0, 0));
        navPanel = new JPanel();
        navPanel.setLayout(new BorderLayout(0, 0));
        navPanel.setBackground(new Color(-12887656));
        navPanel.setForeground(new Color(-1771521));
        mainPanel.add(navPanel, BorderLayout.NORTH);
        titleLabel = new JLabel();
        titleLabel.setEnabled(true);
        titleLabel.setFont(new Font("Lucida Grande", Font.BOLD, 24));
        titleLabel.setForeground(new Color(-1705985));
        titleLabel.setHorizontalAlignment(2);
        titleLabel.setText("chatbook");
        navPanel.add(titleLabel, BorderLayout.WEST);
        userPanel = new JPanel();
        userPanel.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        userPanel.setBackground(new Color(-12887656));
        navPanel.add(userPanel, BorderLayout.EAST);
        userLabel = new JLabel();
        userLabel.setForeground(new Color(-2561551));
        userLabel.setText("Label");
        userPanel.add(userLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        deconnectButton = new JButton();
        deconnectButton.setText("✖");
        deconnectButton.setToolTipText("Se déconnecter");
        userPanel.add(deconnectButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(10, 10), null, 0, false));
        bodyPanel = new JPanel();
        bodyPanel.setLayout(new BorderLayout(0, 0));
        mainPanel.add(bodyPanel, BorderLayout.CENTER);
        dashboardPanel = new JPanel();
        dashboardPanel.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        bodyPanel.add(dashboardPanel, BorderLayout.NORTH);
        accountsButton = new JButton();
        accountsButton.setText("Gestion des comptes");
        dashboardPanel.add(accountsButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        groupsButton = new JButton();
        groupsButton.setText("Gestion des groupes");
        dashboardPanel.add(groupsButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        contentPanel = new JPanel();
        contentPanel.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        bodyPanel.add(contentPanel, BorderLayout.CENTER);
        accountsPanel = new JPanel();
        accountsPanel.setLayout(new BorderLayout(0, 0));
        contentPanel.add(accountsPanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        accountsPanelRight = new JPanel();
        accountsPanelRight.setLayout(new BorderLayout(0, 0));
        accountsPanelRight.setOpaque(true);
        accountsPanelRight.setPreferredSize(new Dimension(400, 50));
        accountsPanel.add(accountsPanelRight, BorderLayout.EAST);
        loginAccount = new JLabel();
        loginAccount.setFont(new Font(loginAccount.getFont().getName(), loginAccount.getFont().getStyle(), 18));
        loginAccount.setHorizontalTextPosition(11);
        loginAccount.setText("");
        loginAccount.setVerticalAlignment(1);
        loginAccount.setVerticalTextPosition(1);
        accountsPanelRight.add(loginAccount, BorderLayout.WEST);
        accountsPanelLeft = new JPanel();
        accountsPanelLeft.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        accountsPanel.add(accountsPanelLeft, BorderLayout.WEST);
        accountsJList = new JList();
        accountsPanelLeft.add(accountsJList, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        groupsPanel = new JPanel();
        groupsPanel.setLayout(new BorderLayout(0, 0));
        contentPanel.add(groupsPanel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        groupsPanelLeft = new JPanel();
        groupsPanelLeft.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        groupsPanel.add(groupsPanelLeft, BorderLayout.WEST);
        groupsList = new JList();
        groupsPanelLeft.add(groupsList, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        groupsPanelRight = new JPanel();
        groupsPanelRight.setLayout(new BorderLayout(0, 0));
        groupsPanelRight.setOpaque(true);
        groupsPanelRight.setPreferredSize(new Dimension(400, 50));
        groupsPanel.add(groupsPanelRight, BorderLayout.EAST);
        nameGroup = new JLabel();
        nameGroup.setFont(new Font(nameGroup.getFont().getName(), nameGroup.getFont().getStyle(), 18));
        nameGroup.setHorizontalAlignment(0);
        nameGroup.setHorizontalTextPosition(0);
        nameGroup.setText("");
        groupsPanelRight.add(nameGroup, BorderLayout.NORTH);
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        groupsPanelRight.add(panel1, BorderLayout.SOUTH);
        newMessageTextField = new JTextField();
        newMessageTextField.setText("Taper votre message...");
        panel1.add(newMessageTextField);
        sendButton = new JButton();
        sendButton.setText("Envoyer");
        panel1.add(sendButton);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }
}
