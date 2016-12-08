package gui.groups;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import domain.Group;
import domain.enums.Role;
import service.GroupService;
import service.UserService;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;
import java.sql.SQLException;

public class GroupsPanel extends JPanel {

    private UserService userService;
    private GroupService groupService;

    private JList groupsList;
    private DefaultListModel<Group> groupsListModel;
    private JPanel groupsPanelLeft;
    private JPanel groupsPanelRight;
    private JLabel nameGroup;

    private JTextField newMessageTextField;
    private JButton sendButton;

    public GroupsPanel() {
        this.userService = UserService.getInstance();
        this.groupService = GroupService.getInstance();

        initComponents();
        initGroupsList();
    }

    private void initComponents() {
        this.setLayout(new BorderLayout(0, 0));
        groupsPanelLeft = new JPanel();
        groupsPanelLeft.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        this.add(groupsPanelLeft, BorderLayout.WEST);
        groupsList = new JList();
        groupsPanelLeft.add(groupsList, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        groupsPanelRight = new JPanel();
        groupsPanelRight.setLayout(new BorderLayout(0, 0));
        groupsPanelRight.setOpaque(true);
        groupsPanelRight.setPreferredSize(new Dimension(400, 50));
        this.add(groupsPanelRight, BorderLayout.EAST);
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

    public void initGroupsList() {
        try {
            groupsListModel = new DefaultListModel<>();
            groupsList = new JList(groupsListModel);
            java.util.List<Group> listGroup;
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
            this.add(new JScrollPane(groupsList));
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
}
