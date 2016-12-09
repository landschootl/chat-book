package gui.discussions;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import domain.Discussion;
import domain.enums.ERole;
import service.DiscussionService;
import service.UserService;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;
import java.sql.SQLException;

public class DiscussionsPanel extends JPanel {

    private UserService userService;
    private DiscussionService DiscussionService;

    private JPanel discussionsPanelLeft;
    private JPanel discussionsPanelRight;
    private JPanel discussionPanel;
    private JLabel nameDiscussion;

    private JList discussionsList;
    private DefaultListModel<Discussion> discussionsListModel;
    private JTextField newMessageTextField;
    private JButton sendButton;

    public DiscussionsPanel() {
        this.userService = UserService.getInstance();
        this.DiscussionService = DiscussionService.getInstance();
        initComponents();
    }

    private void initComponents() {
        this.setLayout(new BorderLayout(0, 0));

        initDiscussionPanelLeft();


        discussionsPanelLeft.add(discussionsList, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        discussionsPanelRight = new JPanel();
        discussionsPanelRight.setLayout(new BorderLayout(0, 0));
        discussionsPanelRight.setOpaque(true);
        discussionsPanelRight.setPreferredSize(new Dimension(400, 50));
        this.add(discussionsPanelRight, BorderLayout.EAST);
        nameDiscussion = new JLabel();
        nameDiscussion.setFont(new Font(nameDiscussion.getFont().getName(), nameDiscussion.getFont().getStyle(), 18));
        nameDiscussion.setHorizontalAlignment(0);
        nameDiscussion.setHorizontalTextPosition(0);
        nameDiscussion.setText("");
        discussionsPanelRight.add(nameDiscussion, BorderLayout.NORTH);
        discussionPanel  = new JPanel();
        discussionPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        discussionsPanelRight.add(discussionPanel, BorderLayout.SOUTH);

        newMessageTextField = new JTextField();
        newMessageTextField.setText("Taper votre message...");
        discussionPanel.add(newMessageTextField);
        sendButton = new JButton();
        sendButton.setText("Envoyer");
        discussionPanel.add(sendButton);
    }

    private void initDiscussionPanelLeft() {
        discussionsPanelLeft = new JPanel();
        discussionsPanelLeft.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        this.add(discussionsPanelLeft, BorderLayout.WEST);
        initDiscussionsList();
    }

    public void initDiscussionsList() {
        try {
            discussionsList = new JList();
            discussionsListModel = new DefaultListModel<>();
            discussionsList = new JList(discussionsListModel);
            java.util.List<Discussion> listDiscussion;
            if (ERole.USER_ADMIN.equals(userService.getConnectedUser().getRole())) {
                listDiscussion = DiscussionService.findAll();
            } else {
                listDiscussion = DiscussionService.findByUser(userService.getConnectedUser());
            }
            for (Discussion Discussion : listDiscussion) {
                discussionsListModel.addElement(Discussion);
            }
            discussionsList.setVisibleRowCount(10);
            discussionsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            this.add(new JScrollPane(discussionsList));
            discussionsList.addListSelectionListener((ListSelectionEvent e) -> {
                if (!e.getValueIsAdjusting()) {
                    Discussion Discussionselected = (Discussion) discussionsList.getSelectedValue();
                    nameDiscussion.setText(Discussionselected.getName());
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
