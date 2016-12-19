package gui.waitingFriendships;

import domain.Friendship;
import domain.enums.ECrud;
import gui.AppFrame;
import persistence.uow.Observable;
import persistence.uow.Observer;
import service.FriendshipService;
import service.UserService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

/**
 * Classe représentant la fenêtre des demandes d'amitié en attente de confirmation.
 *
 * @author Laurent THIEBAULT & Ludovic LANDSCHOOT
 */
public class WaitingFriendshipsFrame extends AppFrame implements Observable {

    private java.util.List<Observer> obs;

    private java.util.List<Friendship> waitingFrienships;
    private FriendshipService friendshipService;

    private JLabel noWaitingFriendships;

    private JPanel contentPanel;

    public WaitingFriendshipsFrame() {
        this.obs = new ArrayList<>();
        this.waitingFrienships = new ArrayList<>();
        this.friendshipService = FriendshipService.getInstance();
        initComponents();
        initFriendshipList();
        this.setVisible(true);
    }

    @Override
    public void initComponents() {
        this.setTitle("Demandes d'amitié en attente");
        this.setSize(400, 230);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setLayout(new BorderLayout());

        this.contentPanel = new JPanel();
        this.contentPanel.setLayout(new BoxLayout(this.contentPanel, BoxLayout.Y_AXIS));
        this.add(new JScrollPane(contentPanel), BorderLayout.CENTER);

        this.noWaitingFriendships = new JLabel("Vous n'avez pas de demandes d'amitié.");
        this.noWaitingFriendships.setHorizontalAlignment(0);
        this.noWaitingFriendships.setBorder(new EmptyBorder(20, 20, 20, 20));
        this.noWaitingFriendships.setVisible(false);
        this.add(noWaitingFriendships, BorderLayout.NORTH);
    }

    /**
     * Initialise la liste des demandes d'amitié en attente de confirmation.
     */
    public void initFriendshipList() {
        waitingFrienships = friendshipService.findWaitingFriendships(UserService.getInstance().getConnectedUser());

        checkVisibilityNoWaitingFriendships();

        for (Friendship f: waitingFrienships) {
            JPanel p = new JPanel();
            p.setLayout(new BorderLayout());


            JPanel userPanel = new JPanel();

            JLabel label = new JLabel(f.getUser1().toString());
            userPanel.add(label);
            p.add(userPanel, BorderLayout.CENTER);

            JPanel buttonsPanel = new JPanel();

            JButton acceptButton = new JButton("Accepter");
            acceptButton.addActionListener((ActionEvent e) -> {
                friendshipService.acceptFriendship(f);
                waitingFrienships.remove(f);
                p.setVisible(false);
                checkVisibilityNoWaitingFriendships();
                notif(ECrud.UPDATE);
            });
            buttonsPanel.add(acceptButton);

            JButton refuseButton = new JButton("Refuser");
            refuseButton.addActionListener((ActionEvent e) -> {
                friendshipService.refuseFriendship(f);
                waitingFrienships.remove(f);
                p.setVisible(false);
                checkVisibilityNoWaitingFriendships();
                notif(ECrud.UPDATE);
            });
            buttonsPanel.add(refuseButton);
            p.add(buttonsPanel, BorderLayout.EAST);
            contentPanel.add(p);
        }
    }

    /**
     * Affiche ou non "Vous n'avez pas d'amis.".
     */
    private void checkVisibilityNoWaitingFriendships() {
        if (waitingFrienships.isEmpty()) {
            this.noWaitingFriendships.setVisible(true);
        }
    }

    @Override
    public void addObserver(Observer o) {
        this.obs.add(o);
    }

    @Override
    public void notif(Object o) {
        for (Observer ob : obs)
            ob.action(o);
    }
}
