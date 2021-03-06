package gui.discussions;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import domain.*;
import domain.enums.ECrud;
import gui.components.PlaceholderTextField;
import persistence.uow.Observer;
import service.DiscussionService;
import service.MessageService;
import service.SecurityService;
import service.UserService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Enumeration;

/**
 * Classe représentant le panneau des discussions de l'utilisateur connecté.
 *
 * @author Laurent THIEBAULT & Ludovic LANDSCHOOT
 */
public class DiscussionsPanel extends JPanel implements Observer {
    private JPanel discussionsPanelLeft;
    private JPanel discussionsPanelRight;
    private JPanel wrapSendDiscussionPanel;
    private JPanel sendDiscussionPanel;
    private JPanel checkboxPanel;
    private JPanel expirationPanel;
    private JCheckBox accusedCheckbox;
    private JCheckBox priorityCheckbox;
    private JCheckBox codeCheckbox;
    private PlaceholderTextField expirationDateTextField;
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

    private boolean accused;
    private boolean priority;
    private boolean code;

    public DiscussionsPanel() {
        this.userService = UserService.getInstance();
        this.connectedUser = userService.getConnectedUser();
        this.discussionService = discussionService.getInstance();
        this.messageService = MessageService.getInstance();
        initComponents();
    }

    /**
     * Intialise les composants du panneau.
     */
    private void initComponents() {
        this.setLayout(new BorderLayout(0, 0));
        initAddDiscussionButton();
        initDiscussionsPanelLeft();
        initDiscussionsPanelRight();
    }

    /**
     * Initialise le bouton d'ajout d'une discussion.
     */
    private void initAddDiscussionButton() {
        addDiscussionButton = new JButton();
        addDiscussionButton.setText("Créer une nouvelle discussion");
        addDiscussionButton.addActionListener((ActionEvent e) -> {
            java.util.List<IUser> users = new ArrayList<>();
            users.add(connectedUser);
            UpdateDiscussionFrame updateDiscussionFrame = new UpdateDiscussionFrame(Discussion.builder()
                    .mod(connectedUser)
                    .users(users)
                    .messages(new ArrayList<>())
                    .build());
            updateDiscussionFrame.addObserver(this);
        });
        this.add(addDiscussionButton, BorderLayout.NORTH);
    }

    /**
     * Initialise le panneau de droite représentant le détail de la discussion sélectionnée.
     */
    private void initDiscussionsPanelRight() {
        discussionsPanelRight = new JPanel();
        discussionsPanelRight.setLayout(new BorderLayout(0, 0));
        discussionsPanelRight.setOpaque(true);
        discussionsPanelRight.setPreferredSize(new Dimension(400, 50));
        this.add(discussionsPanelRight, BorderLayout.EAST);

        initHeaderDiscussion();
        initMessagesPanel();
        initFooterDiscussion();
        clearDiscussionPanel();
    }

    /**
     * Initialise le haut du panneau de droite.
     */
    private void initHeaderDiscussion() {
        headerDiscussionPanel = new JPanel();
        headerDiscussionPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        discussionsPanelRight.add(headerDiscussionPanel, BorderLayout.NORTH);

        nameDiscussion = new JLabel();
        nameDiscussion.setFont(new Font(nameDiscussion.getFont().getName(), nameDiscussion.getFont().getStyle(), 18));
        nameDiscussion.setHorizontalAlignment(0);
        nameDiscussion.setHorizontalTextPosition(0);
        nameDiscussion.setText("");
        headerDiscussionPanel.add(nameDiscussion);

        updateDiscussionButton = new JButton("Gérer");
        updateDiscussionButton.addActionListener((ActionEvent e) -> {
            UpdateDiscussionFrame updateDiscussionFrame = new UpdateDiscussionFrame(discussionSelected);
            updateDiscussionFrame.addObserver(this);
        });
        headerDiscussionPanel.add(updateDiscussionButton);

        deleteDiscussionButton = new JButton("Supprimer");
        deleteDiscussionButton.addActionListener((ActionEvent e) -> {
            int dialogResult = JOptionPane.showConfirmDialog(null, "Etes-vous sûr de vouloir supprimer la discussion ?", "Attention", 0);
            if (dialogResult == JOptionPane.YES_OPTION) {
                discussionService.delete(discussionSelected);
                discussionsListModel.removeElement(discussionSelected);
                clearDiscussionPanel();
            }
        });
        headerDiscussionPanel.add(deleteDiscussionButton);
    }

    /**
     * Initialise le corps du panneau de droite.
     */
    private void initMessagesPanel() {
        messagesPanel = new JPanel();
        messagesPanel.setLayout(new BoxLayout(messagesPanel, BoxLayout.Y_AXIS));
        messagesScrollPane = new JScrollPane(messagesPanel);
        discussionsPanelRight.add(messagesScrollPane, BorderLayout.CENTER);
    }

    /**
     * Initialise le bas de page du panneau de droite.
     */
    private void initFooterDiscussion() {
        wrapSendDiscussionPanel = new JPanel();
        wrapSendDiscussionPanel.setLayout(new BoxLayout(wrapSendDiscussionPanel, BoxLayout.Y_AXIS));

        sendDiscussionPanel = new JPanel();
        sendDiscussionPanel.setLayout(new BoxLayout(sendDiscussionPanel, BoxLayout.X_AXIS));

        newMessageTextField = new PlaceholderTextField(42);
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

        wrapSendDiscussionPanel.add(sendDiscussionPanel);

        checkboxPanel = new JPanel();
        checkboxPanel.setLayout(new BoxLayout(checkboxPanel, BoxLayout.X_AXIS));

        codeCheckbox = new JCheckBox("Chiffré");
        priorityCheckbox = new JCheckBox("Prioritaire");
        accusedCheckbox = new JCheckBox("Accusé de réception");

        checkboxPanel.add(codeCheckbox);
        checkboxPanel.add(priorityCheckbox);
        checkboxPanel.add(accusedCheckbox);

        ActionListener actionListener = new ActionHandler();
        codeCheckbox.addActionListener(actionListener);
        accusedCheckbox.addActionListener(actionListener);
        priorityCheckbox.addActionListener(actionListener);

        wrapSendDiscussionPanel.add(checkboxPanel);

        expirationPanel = new JPanel();
        expirationPanel.setLayout(new BoxLayout(expirationPanel, BoxLayout.X_AXIS));
        expirationPanel.setBorder(new EmptyBorder(0,0,0,95));

        JLabel lblExpirationDate = new JLabel("Date d'expiration :");
        lblExpirationDate.setBorder(new EmptyBorder(0,95,0,0));
        expirationPanel.add(lblExpirationDate);
        expirationDateTextField = new PlaceholderTextField(10);
        expirationDateTextField.setPlaceholder("jj/mm/aaaa");
        expirationDateTextField.addKeyListener(keyListener);
        expirationPanel.add(expirationDateTextField);

        wrapSendDiscussionPanel.add(expirationPanel);

        discussionsPanelRight.add(wrapSendDiscussionPanel, BorderLayout.SOUTH);
    }

    /**
     * Envoie un message dans la discussion sélectionnée.
     */
    private void sendMessage() {
        LocalDate expirationDate = null;

        boolean expirationIsValid = true;
        if (expirationDateTextField.getText().length() != 0) {
            if(!expirationDateTextField.getText().matches("\\d{2}/\\d{2}/\\d{4}")){
                expirationIsValid = false;
                expirationDateTextField.setBorder(BorderFactory.createLineBorder(Color.red));
            } else {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                expirationDate = LocalDate.parse(expirationDateTextField.getText(), formatter);
            }
        }

        if (newMessageTextField.getText().length() == 0) {
            JOptionPane.showMessageDialog(new JFrame(), "Votre message est vide.");
        } else if(expirationDate != null && expirationDate.isBefore(LocalDate.now())) {
            JOptionPane.showMessageDialog(new JFrame(), "Veuillez mettre une date d'expiration supérieur à la date du jour.");
            expirationDateTextField.setBorder(BorderFactory.createLineBorder(Color.red));
        } else {
            expirationDateTextField.setBorder(null);
            if(expirationIsValid) {
                Message message = messageService.create(Message.builder()
                        .idConnection(discussionSelected.getId())
                        .user(connectedUser)
                        .message(newMessageTextField.getText())
                        .accused(accused)
                        .priority(priority)
                        .expiration(expirationDate)
                        .code(code)
                        .build());
                if (message.isCode()) {
                    try {
                        message.setMessage(SecurityService.getInstance().decrypt(message.getMessage()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                addMessagePanel(message);
                discussionSelected.addMessage(message);
                newMessageTextField.setText("");
                expirationDateTextField.setText("");
            } else {
                JOptionPane.showMessageDialog(new JFrame(), "Votre date d'expiration n'est pas correct.");
            }
        }
    }

    /**
     * Dessine le panneau de droite.
     */
    private void drawDiscussionsPanelRight(){
        nameDiscussion.setVisible(true);
        newMessageTextField.setVisible(true);
        sendButton.setVisible(true);
        wrapSendDiscussionPanel.setVisible(true);
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

    /**
     * Ajout un panneau de message.
     * @param message
     */
    private void addMessagePanel(Message message) {
        JPanel wrapMessagePanel = new JPanel();
        wrapMessagePanel.setLayout(new BorderLayout());
        wrapMessagePanel.setBorder(BorderFactory.createRaisedSoftBevelBorder());

        JPanel messagePanel = new JPanel();
        messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS));

        JLabel userLabel = new JLabel();
        userLabel.setFont(new Font("Lucida Grande", Font.BOLD, 14));
        userLabel.setBorder(new EmptyBorder(5,5,2,5));
        userLabel.setText(message.getUser().toString());
        messagePanel.add(userLabel);

        JLabel dateLabel = new JLabel();
        dateLabel.setFont(new Font("Lucida Grande", Font.ITALIC, 9));
        dateLabel.setBorder(new EmptyBorder(0,5,6,5));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy à HH:mm");
        String formattedDateTime = "Le " + message.getDateExpedition().format(formatter);
        dateLabel.setText(formattedDateTime);
        messagePanel.add(dateLabel);

        JLabel expirationLabel = new JLabel();
        if (message.getExpiration() != null) {
            expirationLabel.setFont(new Font("Lucida Grande", Font.ITALIC, 9));
            dateLabel.setBorder(new EmptyBorder(0,5,0,5));
            expirationLabel.setBorder(new EmptyBorder(0,5,6,5));
            formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            formattedDateTime = "(Expire le " + message.getExpiration().format(formatter) + ")";
            expirationLabel.setText(formattedDateTime);
            messagePanel.add(expirationLabel);
        }

        JLabel messageLabel = new JLabel();
        messageLabel.setText(message.getMessage());
        messageLabel.setBorder(new EmptyBorder(5,5,10,5));
        messageLabel.setFont(new Font("Lucida Grande", Font.BOLD, 12));
        messagePanel.add(messageLabel);

        if (message.isAccused() &&
                connectedUser.getId() != message.getUser().getId() &&
                message.getDateAccused() == null) {
            message.setDateAccused(LocalDateTime.now());
            messageService.updateDateAccused(message);
        }

        JLabel accusedLabel = new JLabel();
        if (message.isAccused() && message.getDateAccused() != null) {
            accusedLabel.setFont(new Font("Lucida Grande", Font.ITALIC, 9));
            messageLabel.setBorder(new EmptyBorder(5,5,2,5));
            accusedLabel.setBorder(new EmptyBorder(0,5,10,5));
            formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy à HH:mm");
            formattedDateTime = "(Lu le " + message.getDateAccused().format(formatter) + ")";
            accusedLabel.setText(formattedDateTime);
            messagePanel.add(accusedLabel);
        }

        if (connectedUser.getId() == message.getUser().getId()) {
            userLabel.setAlignmentX(JLabel.RIGHT_ALIGNMENT);
            dateLabel.setAlignmentX(JLabel.RIGHT_ALIGNMENT);
            expirationLabel.setAlignmentX(JLabel.RIGHT_ALIGNMENT);
            messageLabel.setAlignmentX(JLabel.RIGHT_ALIGNMENT);
            accusedLabel.setAlignmentX(JLabel.RIGHT_ALIGNMENT);
            wrapMessagePanel.add(messagePanel, BorderLayout.EAST);
        } else {
            userLabel.setAlignmentX(JLabel.LEFT_ALIGNMENT);
            dateLabel.setAlignmentX(JLabel.LEFT_ALIGNMENT);
            expirationLabel.setAlignmentX(JLabel.LEFT_ALIGNMENT);
            messageLabel.setAlignmentX(JLabel.LEFT_ALIGNMENT);
            accusedLabel.setAlignmentX(JLabel.LEFT_ALIGNMENT);
            wrapMessagePanel.add(messagePanel, BorderLayout.WEST);
        }

        if (message.isPriority()) {
            Color red = new Color(214, 0, 0);
            userLabel.setForeground(red);
            messageLabel.setForeground(red);
            accusedLabel.setForeground(red);
            dateLabel.setForeground(red);
            expirationLabel.setForeground(red);
        }

        messagesPanel.add(wrapMessagePanel);
        messagesPanel.revalidate();
        messagesPanel.repaint();

        messagesPanel.scrollRectToVisible(
                new Rectangle(0, messagesPanel.getHeight()+200, 0, 0));
    }

    /**
     * Initialise le panneau possédant la liste des discussions.
     */
    private void initDiscussionsPanelLeft() {
        discussionsPanelLeft = new JPanel();
        discussionsPanelLeft.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        this.add(discussionsPanelLeft, BorderLayout.WEST);
        initDiscussionsList();
    }

    /**
     * Initialise la liste des discussions.
     */
    public void initDiscussionsList() {
        try {
            discussionsListModel = new DefaultListModel<>();
            discussionsList = new JList(discussionsListModel);
            discussionsList.setFixedCellHeight(30);
            discussionsList.setSelectionBackground(new Color(59, 89, 152));
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
                    } else {
                        clearDiscussionPanel();
                    }
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Vider la panneau de discussion.
     */
    private void clearDiscussionPanel(){
        updateDiscussionButton.setVisible(false);
        deleteDiscussionButton.setVisible(false);
        nameDiscussion.setVisible(false);
        newMessageTextField.setVisible(false);
        sendButton.setVisible(false);
        wrapSendDiscussionPanel.setVisible(false);
        expirationDateTextField.setBorder(null);
        messagesPanel.removeAll();
        messagesPanel.repaint();
    }

    @Override
    public void action(IDomainObject o) {}

    @Override
    public void action(Object o) {}

    @Override
    public void action(Object crud, Object element) {
        switch ((ECrud) crud) {
            case UPDATE:
                Enumeration<Discussion> discussionEnumeration = discussionsListModel.elements();
                int i=0;
                while(discussionEnumeration.hasMoreElements()){
                    Discussion discussion = discussionEnumeration.nextElement();
                    if(discussion.getId() == ((Discussion) element).getId()){
                        discussionsListModel.setElementAt((Discussion) element, i);
                    }
                    i++;
                }
                discussionsList.clearSelection();
                break;
            case CREATE:
                discussionsListModel.addElement((Discussion) element);
                break;
        }
    }

    /**
     * Gestion des checkbox pour la sélection du type de message à envoyer.
     */
    class ActionHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            JCheckBox checkbox = (JCheckBox) event.getSource();
            if (checkbox.isSelected()) {
                if (checkbox == codeCheckbox) {
                    code = true;
                } else if (checkbox == priorityCheckbox) {
                    priority = true;
                } else if (checkbox == accusedCheckbox) {
                    accused = true;
                }
            } else {
                if (checkbox == codeCheckbox) {
                    code = false;
                } else if (checkbox == priorityCheckbox) {
                    priority = false;
                } else if (checkbox == accusedCheckbox) {
                    accused = false;
                }
            }
        }
    }
}
