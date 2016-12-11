package gui.discussions;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import domain.*;
import domain.enums.ECrud;
import persistence.uow.Observer;
import service.DiscussionService;
import service.MessageService;
import service.UserService;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.util.ArrayList;

public class DiscussionsPanel extends JPanel implements Observer {
    private JPanel discussionsPanelLeft;
    private JPanel discussionsPanelRight;
    private JPanel sendDiscussionPanel;
    private JPanel headerDiscussionPanel;
    private JLabel nameDiscussion;

    private JPanel messagesPanel;
    private JList discussionsList;

    private JTextField newMessageTextField;
    private JButton sendButton;
    private JButton addDiscussionButton;
    private JButton updateDiscussionButton;

    private UserService userService;
    private User connectedUser;
    private DiscussionService discussionService;
    private DefaultListModel<Discussion> discussionsListModel;
    private MessageService messageService;

    private Discussion discussionSelected;

    public DiscussionsPanel() {
        this.userService = UserService.getInstance();
        this.connectedUser = userService.getConnectedUser();
        this.discussionService = discussionService.getInstance();
        this.messageService = MessageService.getInstance();
        initComponents();
    }

    private void initComponents() {
        this.setLayout(new BorderLayout(0, 0));
        initDiscussionsPanelLeft();
        initDiscussionsPanelRight();

        addDiscussionButton = new JButton();
        addDiscussionButton.setText("Créer une nouvelle discussion");
        addDiscussionButton.addActionListener((ActionEvent e) -> {
            UpdateDiscussionFrame updateDiscussionFrame = new UpdateDiscussionFrame(Discussion.builder().mod(connectedUser).users(new ArrayList<IUser>()).build());
            updateDiscussionFrame.addObserver(this);
        });
        this.add(addDiscussionButton, BorderLayout.NORTH);
    }

    private void initDiscussionsPanelRight() {
        discussionsPanelRight = new JPanel();
        discussionsPanelRight.setLayout(new BorderLayout(0, 0));
        discussionsPanelRight.setOpaque(true);
        discussionsPanelRight.setPreferredSize(new Dimension(400, 50));
        this.add(discussionsPanelRight, BorderLayout.EAST);

        headerDiscussionPanel = new JPanel();
        headerDiscussionPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        discussionsPanelRight.add(headerDiscussionPanel, BorderLayout.NORTH);

        nameDiscussion = new JLabel();
        nameDiscussion.setFont(new Font(nameDiscussion.getFont().getName(), nameDiscussion.getFont().getStyle(), 18));
        nameDiscussion.setHorizontalAlignment(0);
        nameDiscussion.setHorizontalTextPosition(0);
        nameDiscussion.setText("");
        headerDiscussionPanel.add(nameDiscussion);


        messagesPanel = new JPanel();
        discussionsPanelRight.add(messagesPanel, BorderLayout.CENTER);

        updateDiscussionButton = new JButton("gérer");
        updateDiscussionButton.addActionListener((ActionEvent e) -> {
            UpdateDiscussionFrame updateDiscussionFrame = new UpdateDiscussionFrame(discussionSelected);
            updateDiscussionFrame.addObserver(this);
        });
        headerDiscussionPanel.add(updateDiscussionButton);

        sendDiscussionPanel = new JPanel();
        sendDiscussionPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        discussionsPanelRight.add(sendDiscussionPanel, BorderLayout.SOUTH);

        newMessageTextField = new JTextField();
        newMessageTextField.setText("Taper votre message...");
        sendDiscussionPanel.add(newMessageTextField);

        sendButton = new JButton();
        sendButton.setText("Envoyer");
        sendButton.addActionListener((ActionEvent e) -> {
            Message message = messageService.create(Message.builder()
                    .id_connection(discussionSelected.getId())
                    .id_user(connectedUser.getId())
                    .message(newMessageTextField.getText())
                    .accused(false)
                    .priority(false)
                    .expiration(null)
                    .code(false)
                    .build());
            addMessagePanel(message);
            discussionSelected.addMessages(message);

        });
        sendDiscussionPanel.add(sendButton);
    }

    private void refreshDiscussionsPanelRight(){
        nameDiscussion.setText(discussionSelected.getName());
        if(discussionSelected.getMod().getId() == connectedUser.getId() || connectedUser.isAdmin()){
            updateDiscussionButton.setVisible(true);
        } else {
            updateDiscussionButton.setVisible(false);
        }

        messagesPanel.removeAll();
        messagesPanel.repaint();
        for (Message message: discussionSelected.getMessages()) {
            addMessagePanel(message);
        }

    }

    private void addMessagePanel(Message message) {
        messagesPanel.add(new JLabel(message.getMessage()));
        messagesPanel.revalidate();
        messagesPanel.repaint();
    }

    private void initDiscussionsPanelLeft() {
        discussionsPanelLeft = new JPanel();
        discussionsPanelLeft.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        this.add(discussionsPanelLeft, BorderLayout.WEST);
        initDiscussionsList();
    }

    public void initDiscussionsList() {
        try {
            discussionsListModel = new DefaultListModel<>();
            discussionsList = new JList(discussionsListModel);
            discussionsPanelLeft.add(discussionsList, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));

            java.util.List<Discussion> listDiscussion;
            if (connectedUser.isAdmin()) {
                listDiscussion = discussionService.findAll();
            } else {
                listDiscussion = discussionService.findByUser(connectedUser);
            }
            for (Discussion discussion : listDiscussion) {
                discussionsListModel.addElement(discussion);
            }
            discussionsList.setVisibleRowCount(10);
            discussionsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            this.add(new JScrollPane(discussionsList));
            discussionsList.addListSelectionListener((ListSelectionEvent e) -> {
                if (!e.getValueIsAdjusting()) {
                    discussionSelected = (Discussion) discussionsList.getSelectedValue();
                    refreshDiscussionsPanelRight();
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void action(IDomainObject o) {}

    @Override
    public void action(Object o) {}

    @Override
    public void action(Object crud, Object element) {
        switch ((ECrud) crud) {
            case UPDATE:

                break;
            case CREATE:
                discussionsListModel.addElement((Discussion) element);
                break;
            case DELETE:

                break;
        }
    }

}
