package gui.search;

import domain.Friendship;
import domain.IUser;
import domain.enums.ERole;
import gui.accounts.ShowFriendsFrame;
import gui.components.PlaceholderTextField;
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

/**
 * Classe représentant le panneau de recherche d'utilisateurs.
 *
 * @author Laurent THIEBAULT & Ludovic LANDSCHOOT
 */
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
    private JLabel roleAccount;
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

    /**
     * Initialise la liste des utilisateurs de l'application.
     */
    private void initAccountsList() {
        try {
            accountsListModel = new DefaultListModel<>();
            accountsJList = new JList(accountsListModel);
            accountsJList.setVisibleRowCount(10);
            accountsJList.setFixedCellHeight(30);
            accountsJList.setSelectionBackground(new Color(59, 89, 152));
            accountsJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            accountsScrollPane = new JScrollPane(accountsJList);
            this.add(accountsScrollPane);
            accountsJList.addListSelectionListener((ListSelectionEvent e) -> {
                if (!e.getValueIsAdjusting()) {
                    userSelected = (IUser) accountsJList.getSelectedValue();

                    if (userSelected != null) {
                        setVisibleAccountInfos(true);
                        loginAccount.setText(userSelected.getLogin());
                        firstnameAccount.setText("Prénom : " + userSelected.getFirstname());
                        lastnameAccount.setText("Nom : " + userSelected.getLastname());
                        String role;
                        if (userSelected.getRole().equals(ERole.USER_ADMIN)) {
                            role = "Administrateur";
                        } else {
                            role = "Utilisateur";
                        }
                        roleAccount.setText("Role : " + role);
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

    /**
     * Met à jour la liste des utilisateurs.
     */
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

    /**
     * Initialise la partie recherche.
     */
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

    /**
     * Initialise le panneau de droite représentant le détail de l'utilisateur sélectionné.
     */
    private void initAccountsPanelRight() {
        panelRight = new JPanel();
        panelRight.setOpaque(true);
        panelRight.setPreferredSize(new Dimension(400, 50));
        panelRight.setLayout(new BorderLayout());
        panelRight.setBorder(new EmptyBorder(0, 10, 10, 10));

        loginAccount = new JLabel();
        loginAccount.setFont(new Font(loginAccount.getFont().getName(), loginAccount.getFont().getStyle(), 24));
        loginAccount.setHorizontalAlignment(JLabel.CENTER);
        loginAccount.setBorder(new EmptyBorder(0, 0, 10, 0));
        panelRight.add(loginAccount, BorderLayout.NORTH);

        infosAccountPanel = new JPanel();
        infosAccountPanel.setLayout(new BoxLayout(infosAccountPanel, BoxLayout.Y_AXIS));
        firstnameAccount = new JLabel();
        firstnameAccount.setBorder(new EmptyBorder(8, 0, 8, 0));
        lastnameAccount = new JLabel();
        lastnameAccount.setBorder(new EmptyBorder(8, 0, 8, 0));
        roleAccount = new JLabel();
        roleAccount.setBorder(new EmptyBorder(8, 0, 8, 0));

        infosAccountPanel.add(firstnameAccount);
        infosAccountPanel.add(lastnameAccount);
        infosAccountPanel.add(roleAccount);

        panelRight.add(infosAccountPanel, BorderLayout.CENTER);

        JPanel buttonsPanel = new JPanel();

        waitingFriendshipLabel = new JLabel("Demande d'amitié en attente de confirmation");
        waitingFriendshipLabel.setVisible(false);

        buttonsPanel.add(waitingFriendshipLabel);

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
        buttonsPanel.add(addFriendButton);

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
        buttonsPanel.add(deleteFriendButton);

        panelRight.add(buttonsPanel, BorderLayout.SOUTH);

        this.add(panelRight, BorderLayout.EAST);

        setVisibleAccountInfos(false);
    }

    /**
     * Rend visible ou non les informations de l'utilisateur dans le panneau de droite.
     * @param visible
     */
    private void setVisibleAccountInfos(boolean visible) {
        loginAccount.setVisible(visible);
        infosAccountPanel.setVisible(visible);
        firstnameAccount.setVisible(visible);
        lastnameAccount.setVisible(visible);
    }

    /**
     * Dynamise l'affichage des composants "Ajout en ami", "En attente de confirmation" et "Supprimer de vos amis".
     */
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
