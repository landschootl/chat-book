package gui.discussions;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import domain.Discussion;
import domain.IUser;
import domain.User;
import domain.enums.ECrud;
import gui.AppFrame;
import persistence.uow.Observable;
import persistence.uow.Observer;
import service.DiscussionService;
import service.UserService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * Created by landschoot on 11/12/16.
 */
public class UpdateDiscussionFrame extends AppFrame implements Observable {
    private JPanel mainPanel;
    private JTextField titleField;
    private JButton addUserButton;
    private JButton removeUserButton;
    private JList friendsList;
    private JList usersList;
    private JLabel titleLabel;
    private JLabel managerLabel;
    private JButton saveButton;

    private java.util.List<Observer> obs;

    private Discussion discussion;
    private DefaultListModel<IUser> users;
    private DefaultListModel<IUser> friends;
    private DiscussionService discussionService;
    private UserService userService;
    private User userConnected;

    private final String INIT_MANAGER_LABEL = "Gérant : ";

    public UpdateDiscussionFrame(Discussion discussion) {
        super();
        this.discussionService = DiscussionService.getInstance();
        this.userService = UserService.getInstance();
        this.userConnected = userService.getConnectedUser();
        this.obs = new ArrayList<>();
        this.discussion = discussion;
        initComponents();
        initInformationsDiscution();
        initListUsers();
        initListFriends();
        initButton();
    }

    @Override
    public void initComponents() {
        this.setTitle("Gestion d'une discussion");
        this.setSize(400, 400);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setVisible(true);
        this.setContentPane(mainPanel);
    }

    public void initInformationsDiscution() {
        titleField.setText(discussion.getName());
        managerLabel.setText(INIT_MANAGER_LABEL + discussion.getMod().getLogin());
    }

    public void initListUsers() {
        users = new DefaultListModel<>();
        for (IUser user : discussion.getUsers()) {
            if (user.getId() != discussion.getMod().getId()) {
                users.addElement(user);
            }
        }
        usersList.setModel(users);
        usersList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    IUser userSelected = (IUser) usersList.getSelectedValue();
                    int dialogResult = JOptionPane.showConfirmDialog(null, "Mettre  " + userSelected.getLogin() + " moderateur du groupe ?", "Attention", 0);
                    if (dialogResult == JOptionPane.YES_OPTION) {
                        users.addElement(discussion.getMod());
                        discussion.setMod(userSelected);
                        managerLabel.setText(INIT_MANAGER_LABEL + userSelected.getLogin());
                        for (int i = 0; i < users.getSize(); i++) {
                            IUser user = users.get(i);
                            if (user.getId() == userSelected.getId()) {
                                users.remove(i);
                            }
                        }
                    }
                }
            }
        });

    }

    public void initListFriends() {
        friendsList.setPreferredSize(new Dimension(150, 150));
        friends = new DefaultListModel<>();
        java.util.List<IUser> list;
        if (userConnected.isAdmin()) {
            list = userService.findAll();
        } else {
            list = userService.findConnectedUserFriends();
        }
        for (IUser friend : list) {
            if (!friendInTheUsers(friend) && (discussion.getMod().getId() != friend.getId())) {
                friends.addElement(friend);
            }
        }
        friendsList.setModel(friends);
    }

    public boolean friendInTheUsers(IUser friend) {
        for (IUser user : discussion.getUsers()) {
            if (friend.getId() == user.getId()) {
                return true;
            }
        }
        return false;
    }

    public void initButton() {
        addUserButton.addActionListener((ActionEvent e) -> {
            IUser friendSelected = (IUser) friendsList.getSelectedValue();
            if (friendSelected != null) {
                users.addElement(friendSelected);
                discussion.addUser(friendSelected);
                for (int i = 0; i < friends.size(); i++) {
                    IUser friend = friends.get(i);
                    if (friend.getId() == friendSelected.getId()) {
                        friends.remove(i);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(new JFrame(), "Veuillez sélectionner votre ami à ajouter");
            }
        });
        removeUserButton.addActionListener((ActionEvent e) -> {
            IUser userSelected = (IUser) usersList.getSelectedValue();
            if (userSelected == null) {
                JOptionPane.showMessageDialog(new JFrame(), "Veuillez sélectionner l'utilisateur à retirer");
            } else if (userSelected.getId() == userConnected.getId() && !userConnected.isAdmin()) {
                JOptionPane.showMessageDialog(new JFrame(), "En tant que administrateur, vous ne pouvez pas quitter la discussion");
            } else {
                friends.addElement(userSelected);
                for (int i = 0; i < users.size(); i++) {
                    IUser user = users.get(i);
                    if (user.getId() == userSelected.getId()) {
                        users.remove(i);
                        discussion.removeUser(user.getId());
                    }
                }
            }
        });
        saveButton.addActionListener((ActionEvent e) -> {
            if (!"".equals(titleField.getText())) {
                discussion.setName(titleField.getText());
                if (discussion.getId() == 0) {
                    discussion = discussionService.create(discussion);
                    notif(ECrud.CREATE);
                } else {
                    discussion = discussionService.update(discussion);
                    notif(ECrud.UPDATE);
                }
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(new JFrame(), "N'oubliez pas de donner un nom à la conversation");
            }
        });
    }

    @Override
    public void addObserver(Observer o) {
        this.obs.add(o);
    }

    @Override
    public void notif(Object o) {
        for (Observer ob : obs)
            ob.action(o, discussion);
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
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 4, new Insets(10, 10, 10, 10), -1, -1));
        mainPanel.add(panel1, BorderLayout.NORTH);
        titleLabel = new JLabel();
        titleLabel.setText("Title");
        panel1.add(titleLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        titleField = new JTextField();
        titleField.setText("");
        panel1.add(titleField, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel1.add(spacer1, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        managerLabel = new JLabel();
        managerLabel.setText("Gérant : ");
        panel1.add(managerLabel, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 3, new Insets(10, 10, 10, 10), -1, -1));
        mainPanel.add(panel2, BorderLayout.CENTER);
        final JScrollPane scrollPane1 = new JScrollPane();
        scrollPane1.setHorizontalScrollBarPolicy(31);
        scrollPane1.setVerticalScrollBarPolicy(20);
        panel2.add(scrollPane1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        friendsList = new JList();
        scrollPane1.setViewportView(friendsList);
        final JScrollPane scrollPane2 = new JScrollPane();
        panel2.add(scrollPane2, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        usersList = new JList();
        scrollPane2.setViewportView(usersList);
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel2.add(panel3, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        addUserButton = new JButton();
        addUserButton.setBackground(new Color(-12390858));
        addUserButton.setEnabled(true);
        addUserButton.setFont(new Font(addUserButton.getFont().getName(), Font.BOLD, 12));
        addUserButton.setForeground(new Color(-16777216));
        addUserButton.setText("->");
        panel3.add(addUserButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_SOUTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        removeUserButton = new JButton();
        removeUserButton.setBackground(new Color(-2212278));
        removeUserButton.setEnabled(true);
        removeUserButton.setFont(new Font(removeUserButton.getFont().getName(), Font.BOLD, 12));
        removeUserButton.setForeground(new Color(-16777216));
        removeUserButton.setHideActionText(false);
        removeUserButton.setText("<-");
        panel3.add(removeUserButton, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        saveButton = new JButton();
        saveButton.setText("Sauvegarder");
        mainPanel.add(saveButton, BorderLayout.SOUTH);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }
}
