package gui.accounts;

import domain.Friendship;
import domain.IUser;
import gui.AppFrame;
import service.FriendshipService;
import service.UserService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.stream.Collectors;

public class ShowFriendsFrame extends AppFrame {

    private java.util.List<IUser> friends;
    private JLabel noFriendLabel;

    private UserService userService;
    private FriendshipService friendshipService;

    private JPanel contentPanel;

    public ShowFriendsFrame() {
        this.userService = UserService.getInstance();
        this.friendshipService = FriendshipService.getInstance();

        initComponents();
        initFriendshipList();
        this.setVisible(true);
    }

    @Override
    public void initComponents() {
        this.setTitle("Vos amis");
        this.setSize(400, 230);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setLayout(new BorderLayout());

        this.contentPanel = new JPanel();
        this.contentPanel.setLayout(new BoxLayout(this.contentPanel, BoxLayout.Y_AXIS));
        this.add(new JScrollPane(contentPanel), BorderLayout.CENTER);

        this.noFriendLabel = new JLabel("Vous n'avez pas d'amis. Voulez-vous un curly ?");
        this.noFriendLabel.setHorizontalAlignment(0);
        this.noFriendLabel.setBorder(new EmptyBorder(20, 20, 20, 20));
        this.noFriendLabel.setVisible(false);
        this.add(noFriendLabel, BorderLayout.NORTH);
    }

    public void initFriendshipList() {
        friends = userService.findConnectedUserFriends();

        checkVisibilityNoFriendLabel();

        for (IUser friend: friends) {
            JPanel p = new JPanel();
            p.setLayout(new BorderLayout());

            JPanel userPanel = new JPanel();

            JLabel label = new JLabel(friend.toString());
            userPanel.add(label);
            p.add(userPanel, BorderLayout.CENTER);

            JPanel buttonsPanel = new JPanel();

            JButton deleteButton = new JButton("Supprimer");
            deleteButton.addActionListener((ActionEvent e) -> {
                Friendship friendship = Friendship.builder()
                        .user1(userService.getConnectedUser())
                        .user2(friend)
                        .build();

                friendshipService.delete(friendship);

                friends = friends.stream()
                        .filter((IUser u) -> u.getId() != friend.getId())
                        .collect(Collectors.toList());

                p.setVisible(false);
                checkVisibilityNoFriendLabel();
            });
            buttonsPanel.add(deleteButton);
            p.add(buttonsPanel, BorderLayout.EAST);
            contentPanel.add(p);
        }
    }

    private void checkVisibilityNoFriendLabel() {
        if (friends.isEmpty()) {
            this.noFriendLabel.setVisible(true);
        }
    }
}
