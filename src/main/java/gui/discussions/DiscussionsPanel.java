package gui.discussions;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import domain.*;
import domain.enums.ECrud;
import gui.components.PlaceholderTextField;
import persistence.uow.Observer;
import service.DiscussionService;
import service.MessageService;
import service.UserService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.ArrayList;

public class DiscussionsPanel extends JPanel implements Observer {
    private JPanel discussionsPanelLeft;
    private JPanel discussionsPanelRight;
    private JPanel sendDiscussionPanel;
    private JPanel headerDiscussionPanel;
    private JLabel nameDiscussion;

    private JPanel messagesPanel;
    private JScrollPane messagesScrollPane;
    private JList discussionsList;

    private PlaceholderTextField newMessageTextField;
    private JButton sendButton;
    private JButton addDiscussionButton;
    private JButton updateDiscussionButton;
    private JButton deleteDiscussionButton;

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
            UpdateDiscussionFrame updateDiscussionFrame = new UpdateDiscussionFrame(Discussion.builder()
                    .mod(connectedUser)
                    .users(new ArrayList<IUser>())
                    .messages(new ArrayList<Message>())
                    .build());
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
        messagesPanel.setLayout(new BoxLayout(messagesPanel, BoxLayout.Y_AXIS));
        messagesScrollPane = new JScrollPane(messagesPanel);
        discussionsPanelRight.add(messagesScrollPane, BorderLayout.CENTER);

        updateDiscussionButton = new JButton("Gérer");
        updateDiscussionButton.addActionListener((ActionEvent e) -> {
            UpdateDiscussionFrame updateDiscussionFrame = new UpdateDiscussionFrame(discussionSelected);
            updateDiscussionFrame.addObserver(this);
        });
        headerDiscussionPanel.add(updateDiscussionButton);

        deleteDiscussionButton = new JButton("Supprimer");
        deleteDiscussionButton.setVisible(false);
        deleteDiscussionButton.addActionListener((ActionEvent e) -> {
            discussionService.delete(discussionSelected);
            discussionsListModel.removeElement(discussionSelected);
        });
        headerDiscussionPanel.add(deleteDiscussionButton);

        sendDiscussionPanel = new JPanel();
        sendDiscussionPanel.setLayout(new BoxLayout(sendDiscussionPanel, BoxLayout.X_AXIS));
        discussionsPanelRight.add(sendDiscussionPanel, BorderLayout.SOUTH);

        newMessageTextField = new PlaceholderTextField();
        newMessageTextField.setPlaceholder("Tapez votre message...");
        KeyListener keyListener = new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    sendMessage();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        };
        newMessageTextField.addKeyListener(keyListener);
        sendDiscussionPanel.add(newMessageTextField);

        sendButton = new JButton();
        sendButton.setText("Envoyer");
        sendButton.addActionListener((ActionEvent e) -> sendMessage());

        sendDiscussionPanel.add(sendButton);

        clearDiscussionPanel();
    }

    private void sendMessage() {
        Message message = messageService.create(Message.builder()
                .idConnection(discussionSelected.getId())
                .user(connectedUser)
                .message(newMessageTextField.getText())
                .accused(false)
                .priority(false)
                .expiration(null)
                .code(false)
                .build());
        addMessagePanel(message);
        discussionSelected.addMessage(message);
        newMessageTextField.setText("");
    }

    private void drawDiscussionsPanelRight(){
        nameDiscussion.setVisible(true);
        newMessageTextField.setVisible(true);
        sendButton.setVisible(true);
        nameDiscussion.setText(discussionSelected.getName());
        if (discussionSelected.getMod().getId() == connectedUser.getId() || connectedUser.isAdmin()) {
            updateDiscussionButton.setVisible(true);
            deleteDiscussionButton.setVisible(true);
        } else {
            updateDiscussionButton.setVisible(false);
            deleteDiscussionButton.setVisible(false);
        }
        messagesPanel.removeAll();
        messagesPanel.repaint();
        for (Message message : discussionSelected.getMessages()) {
            addMessagePanel(message);
        }
    }

    private void addMessagePanel(Message message) {
        JPanel messagePanel = new JPanel();
        messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS));
        JLabel infosLabel = new JLabel();
        infosLabel.setFont(new Font("Lucida Grande", Font.BOLD, 14));
        infosLabel.setBorder(new EmptyBorder(5,0,5,0));
        infosLabel.setText(message.getUser().toString());
        messagePanel.add(infosLabel);
        JLabel messageLabel = new JLabel();
        messageLabel.setText(message.getMessage());
        messageLabel.setBorder(new EmptyBorder(0,0,5,0));
        messagePanel.add(messageLabel);

        messagesPanel.add(messagePanel);
        messagesPanel.revalidate();
        messagesPanel.repaint();

        messagesPanel.scrollRectToVisible(
                new Rectangle(0, messagesPanel.getHeight()+200, 0, 0));
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
                    if(discussionSelected != null){
                        drawDiscussionsPanelRight();
                        discussionsPanelRight.setVisible(true);
                    } else {
                        clearDiscussionPanel();
                    }
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void clearDiscussionPanel(){
        updateDiscussionButton.setVisible(false);
        deleteDiscussionButton.setVisible(false);
        nameDiscussion.setVisible(false);
        newMessageTextField.setVisible(false);
        sendButton.setVisible(false);
    }

    @Override
    public void action(IDomainObject o) {}

    @Override
    public void action(Object o) {}

    @Override
    public void action(Object crud, Object element) {
        switch ((ECrud) crud) {
            case UPDATE:
//                discussionsListModel.setElementAt(element, i);
                break;
            case CREATE:
                discussionsListModel.addElement((Discussion) element);
                break;
        }
    }

}
