package gui.search;

import domain.Friendship;
import domain.IUser;
import domain.User;
import domain.enums.ERole;
import gui.accounts.ShowFriendsFrame;
import gui.components.PlaceholderTextField;
import persistence.uow.UnitOfWork;
import service.FriendshipService;
import service.SearchService;
import service.UserService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.SQLException;

public class SearchPanel extends JPanel {
    private SearchService searchService;
    private UserService userService;
    private FriendshipService friendshipService;
    private IUser userSelected;

    private PlaceholderTextField firstnameSearch;
    private PlaceholderTextField lastnameSearch;
    private JButton searchButton;
    private JButton showFriendsButton;

    private JPanel contentPanel;

    private JScrollPane accountsScrollPane;
    private DefaultListModel<IUser> accountsListModel;
    private JList accountsJList;

    private JPanel panelRight;
    private JLabel loginAccount;
    private JLabel lastnameAccount;
    private JLabel firstnameAccount;
    private JLabel waitingFriendshipLabel;
    private JButton addFriendButton;
    private JButton deleteFriendButton;
    private JPanel infosAccountPanel;

    public SearchPanel() {
        this.searchService = SearchService.getInstance();
        this.friendshipService = FriendshipService.getInstance();
        this.userService = UserService.getInstance();
        this.setLayout(new BorderLayout(0, 0));

        initSearch();
        initAccountsList();
        initAccountsPanelRight();
    }

    private void initAccountsList() {
        try {
            accountsListModel = new DefaultListModel<>();
            accountsJList = new JList(accountsListModel);
            accountsJList.setVisibleRowCount(10);
            accountsJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            accountsScrollPane = new JScrollPane(accountsJList);
            this.add(accountsScrollPane);
            accountsJList.addListSelectionListener((ListSelectionEvent e) -> {
                if (!e.getValueIsAdjusting()) {
                    userSelected = (IUser) accountsJList.getSelectedValue();

                    if (userSelected != null) {
                        setVisibleAccountInfos(true);
                        loginAccount.setText(userSelected.getLogin());
                        lastnameAccount.setText(userSelected.getLastname());
                        firstnameAccount.setText(userSelected.getFirstname());
                        checkVisibilityFriendButtons();
                    } else {
                        setVisibleAccountInfos(false);
                    }
                }
            });
            accountsScrollPane.setVisible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateAccountsList() {
        this.accountsListModel.removeAllElements();
        java.util.List<IUser> accountsList = this.searchService.searchUsers(lastnameSearch.getText(), firstnameSearch.getText());

        for (IUser user : accountsList) {
            if (user.getId() != userService.getConnectedUser().getId()) {
                accountsListModel.addElement(user);
            }
        }
        accountsScrollPane.setVisible(true);
        this.validate();
        this.repaint();
    }

    private void initSearch() {
        JPanel panelSearch = new JPanel();
        this.add(panelSearch, BorderLayout.NORTH);

        firstnameSearch = new PlaceholderTextField();
        firstnameSearch.setPlaceholder("Prénom");
        firstnameSearch.setPreferredSize(new Dimension(200, 24));
        panelSearch.add(firstnameSearch);
        lastnameSearch = new PlaceholderTextField();
        lastnameSearch.setPlaceholder("Nom");
        lastnameSearch.setPreferredSize(new Dimension(200, 24));
        panelSearch.add(lastnameSearch);

        KeyListener keyListener = new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    updateAccountsList();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        };
        lastnameSearch.addKeyListener(keyListener);
        firstnameSearch.addKeyListener(keyListener);

        searchButton = new JButton("Rechercher");
        searchButton.addActionListener((ActionEvent e) -> updateAccountsList());
        panelSearch.add(searchButton);

        showFriendsButton = new JButton("Voir vos amis");
        showFriendsButton.addActionListener((ActionEvent e) -> new ShowFriendsFrame());
        panelSearch.add(showFriendsButton);
    }

    private void initAccountsPanelRight() {
        panelRight = new JPanel();
        panelRight.setLayout(new BorderLayout(0, 0));
        panelRight.setOpaque(true);
        panelRight.setPreferredSize(new Dimension(400, 50));
        panelRight.setLayout(new BoxLayout(panelRight, BoxLayout.Y_AXIS));
        panelRight.setBorder(new EmptyBorder(0, 10, 10, 10));

        loginAccount = new JLabel();
        loginAccount.setFont(new Font(loginAccount.getFont().getName(), loginAccount.getFont().getStyle(), 18));
        loginAccount.setHorizontalTextPosition(11);
        loginAccount.setText("");
        loginAccount.setVerticalAlignment(1);
        loginAccount.setVerticalTextPosition(1);
        loginAccount.setBorder(new EmptyBorder(0, 0, 10, 0));
        this.panelRight.add(loginAccount, BorderLayout.WEST);

        infosAccountPanel = new JPanel();
        lastnameAccount = new JLabel();
        firstnameAccount = new JLabel();
        waitingFriendshipLabel = new JLabel("Demande d'amitié en attente de confirmation");
        waitingFriendshipLabel.setVisible(false);

        infosAccountPanel.setLayout(new GridLayout(3, 3));
        infosAccountPanel.add(firstnameAccount);
        infosAccountPanel.add(lastnameAccount);
        infosAccountPanel.add(waitingFriendshipLabel);

        addFriendButton = new JButton("Ajouter en ami");
        addFriendButton.addActionListener((ActionEvent e) -> {
            Friendship friendship = Friendship.builder()
                                        .user1(userService.getConnectedUser())
                                        .user2(userSelected)
                                        .build();

            friendshipService.create(friendship);
            addFriendButton.setVisible(false);
            waitingFriendshipLabel.setVisible(true);
            JOptionPane.showMessageDialog(new JFrame(), "Demande d'amitié envoyée avec succès.");
        });
        addFriendButton.setVisible(false);
        infosAccountPanel.add(addFriendButton);

        deleteFriendButton = new JButton("Supprimer de vos amis");
        deleteFriendButton.addActionListener((ActionEvent e) -> {
            Friendship friendship = Friendship.builder()
                    .user1(userService.getConnectedUser())
                    .user2(userSelected)
                    .build();

            friendshipService.delete(friendship);
            deleteFriendButton.setVisible(false);
            addFriendButton.setVisible(true);
            JOptionPane.showMessageDialog(new JFrame(), "Suppression avec succès.");
        });

        deleteFriendButton.setVisible(false);
        infosAccountPanel.add(deleteFriendButton);

        panelRight.add(infosAccountPanel);

        this.add(panelRight, BorderLayout.EAST);

        setVisibleAccountInfos(false);
    }

    private void setVisibleAccountInfos(boolean visible) {
        loginAccount.setVisible(visible);
        infosAccountPanel.setVisible(visible);
        firstnameAccount.setVisible(visible);
        lastnameAccount.setVisible(visible);
    }

    private void checkVisibilityFriendButtons() {
        IUser connectedUser = userService.getConnectedUser();

        Friendship friendship = this.friendshipService.find(connectedUser, userSelected);

        if (friendship != null) {
            if (friendship.isConfirmed()) {
                this.addFriendButton.setVisible(false);
                this.deleteFriendButton.setVisible(true);
                this.waitingFriendshipLabel.setVisible(false);
            } else {
                this.addFriendButton.setVisible(false);
                this.deleteFriendButton.setVisible(false);
                this.waitingFriendshipLabel.setVisible(true);
            }
        } else {
            this.addFriendButton.setVisible(true);
            this.deleteFriendButton.setVisible(false);
            this.waitingFriendshipLabel.setVisible(false);
        }
    }
}
