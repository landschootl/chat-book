package gui;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import domain.IDomainObject;
import domain.enums.ECrud;
import domain.enums.ERole;
import gui.accounts.AccountsPanel;
import gui.accounts.UpdateAccountFrame;
import gui.discussions.DiscussionsPanel;
import gui.login.LoginFrame;
import gui.search.SearchPanel;
import gui.waitingFriendships.WaitingFriendshipsFrame;
import persistence.db.SingletonDB;
import persistence.uow.Observer;
import persistence.uow.UnitOfWork;
import service.FriendshipService;
import service.UserService;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

public class MainFrame extends AppFrame implements Observer {
    private JPanel navPanel;
    private JLabel titleLabel;
    private JButton deconnectButton;
    private JPanel mainPanel;
    private JLabel userLabel;
    private JPanel bodyPanel;

    private JPanel dashboardPanel;
    private JButton accountsButton;
    private JButton discussionsButton;

    private JPanel userPanel;
    private JPanel contentPanel;
    private JButton updateAccountButton;
    private JButton searchButton;
    private JButton waitingFriendshipsButton;

    private AccountsPanel accountsPanel;
    private DiscussionsPanel discussionsPanel;
    private SearchPanel searchPanel;

    private UserService userService;
    private FriendshipService friendshipService;

    private UnitOfWork unitOfWork;

    public MainFrame() {
        this.unitOfWork = UnitOfWork.getInstance();
        this.userService = UserService.getInstance();
        this.friendshipService = FriendshipService.getInstance();
        this.setContentPane(this.mainPanel);
        initPanels();
        cleanPanels();
        initFrame();
        initComponents();
        configUpdateAccountButton();
        configWaitingFriendshipButton();
        configDeconnectButton();
        configDashboard();
        cleanButtons();
        checkRole();
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                SingletonDB.getInstance().close();
            }
        });
        this.setVisible(true);
    }

    private void checkRole() {
        if (userService.getConnectedUser().getRole().equals(ERole.USER_ADMIN)) {
            this.setTitle("Chatbook - Admin");
            this.titleLabel.setText("chatbook - admin");
            cleanPanels();
            cleanButtons();
            accountsButton.setFont(new Font("Lucida Grande", Font.BOLD, 16));
            accountsButton.setForeground(new Color(59, 89, 152));
            this.accountsPanel.setVisible(true);
        } else {
            this.setTitle("Chatbook - " + userService.getConnectedUser().getFirstname() + " " + userService.getConnectedUser().getLastname());
            cleanPanels();
            cleanButtons();
            discussionsButton.setFont(new Font("Lucida Grande", Font.BOLD, 16));
            discussionsButton.setForeground(new Color(59, 89, 152));
            this.discussionsPanel.setVisible(true);
        }
    }

    public void initPanels() {
        this.accountsPanel = new AccountsPanel();
        contentPanel.add(accountsPanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        this.discussionsPanel = new DiscussionsPanel();
        contentPanel.add(discussionsPanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        this.searchPanel = new SearchPanel();
        contentPanel.add(searchPanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
    }

    public void cleanPanels() {
        this.accountsPanel.setVisible(false);
        this.discussionsPanel.setVisible(false);
        this.searchPanel.setVisible(false);
    }

    public void initComponents() {
        titleLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
        userLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
        userLabel.setText(userService.getConnectedUser().getFirstname() + " " + userService.getConnectedUser().getLastname());
        dashboardPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        userPanel.setBorder(new EmptyBorder(0, 0, 0, 14));
        checkComponentRoles();
        initImages();
        configWaitingFriendshipImage();
    }

    private void configWaitingFriendshipImage() {
        Image waitingFriendshipImg = null;
        try {
            if (friendshipService.findWaitingFriendships(UserService.getInstance().getConnectedUser()).size() != 0) {
                waitingFriendshipImg = ImageIO.read(MainFrame.class.getResource("../img/demandes_amis_new.png"));
            } else {
                waitingFriendshipImg = ImageIO.read(MainFrame.class.getResource("../img/demandes_amis.png"));
            }
            waitingFriendshipImg = waitingFriendshipImg.getScaledInstance(16, 16, Image.SCALE_DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        waitingFriendshipsButton.setIcon(new ImageIcon(waitingFriendshipImg));
    }


    private void initImages() {
        Image updateAccountImg = null;
        Image logoutImg = null;
        try {
            updateAccountImg = ImageIO.read(MainFrame.class.getResource("../img/mon_compte.png"));
            updateAccountImg = updateAccountImg.getScaledInstance(16, 16, Image.SCALE_DEFAULT);
            logoutImg = ImageIO.read(MainFrame.class.getResource("../img/deconnexion.png"));
            logoutImg = logoutImg.getScaledInstance(16, 16, Image.SCALE_DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        updateAccountButton.setIcon(new ImageIcon(updateAccountImg));
        deconnectButton.setIcon(new ImageIcon(logoutImg));
    }

    public void checkComponentRoles() {
        if (!ERole.USER_ADMIN.equals(userService.getConnectedUser().getRole())) {
            accountsButton.setVisible(false);
        }
    }

    public void configDashboard() {
        accountsButton.addActionListener((ActionEvent e) -> {
            cleanPanels();
            cleanButtons();
            accountsButton.setFont(new Font("Lucida Grande", Font.BOLD, 16));
            accountsButton.setForeground(new Color(59, 89, 152));
            this.accountsPanel.setVisible(true);
        });
        discussionsButton.addActionListener((ActionEvent e) -> {
            cleanPanels();
            cleanButtons();
            discussionsButton.setFont(new Font("Lucida Grande", Font.BOLD, 16));
            discussionsButton.setForeground(new Color(59, 89, 152));
            this.discussionsPanel.setVisible(true);
        });
        searchButton.addActionListener((ActionEvent e) -> {
            cleanPanels();
            cleanButtons();
            searchButton.setFont(new Font("Lucida Grande", Font.BOLD, 16));
            searchButton.setForeground(new Color(59, 89, 152));
            this.searchPanel.updateAccountsList();
            this.searchPanel.setVisible(true);
        });
    }

    public void configDeconnectButton() {
        deconnectButton.addActionListener(e -> {
            this.userService.setConnectedUser(null);
            this.setVisible(false);
            this.unitOfWork.rollback();
            new LoginFrame();
        });
    }

    public void configUpdateAccountButton() {
        updateAccountButton.addActionListener(e -> {
            UpdateAccountFrame updateAccountFrame = new UpdateAccountFrame();
            updateAccountFrame.addObserver(this);
        });
    }

    public void configWaitingFriendshipButton() {
        waitingFriendshipsButton.addActionListener(e -> {
            WaitingFriendshipsFrame waitingFriendshipsFrame = new WaitingFriendshipsFrame();
            waitingFriendshipsFrame.addObserver(this);
        });
    }

    public void cleanButtons() {
        accountsButton.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
        accountsButton.setForeground(Color.BLACK);
        discussionsButton.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
        discussionsButton.setForeground(Color.BLACK);
        searchButton.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
        searchButton.setForeground(Color.BLACK);
    }

    @Override
    public void action(IDomainObject o) {
    }

    @Override
    public void action(Object o) {
        ECrud crud = (ECrud) o;

        switch (crud) {
            case UPDATE:
                userLabel.setText(userService.getConnectedUser().getFirstname() + " " + userService.getConnectedUser().getLastname());
                configWaitingFriendshipImage();
                break;
            case DELETE:
                userService.setConnectedUser(null);
                this.setVisible(false);
                this.unitOfWork.rollback();
                new LoginFrame();
                break;
        }
    }

    @Override
    public void action(Object crud, Object element) {

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
        navPanel = new JPanel();
        navPanel.setLayout(new BorderLayout(0, 0));
        navPanel.setBackground(new Color(-12887656));
        navPanel.setForeground(new Color(-1771521));
        mainPanel.add(navPanel, BorderLayout.NORTH);
        titleLabel = new JLabel();
        titleLabel.setEnabled(true);
        titleLabel.setFont(new Font("Lucida Grande", Font.BOLD, 24));
        titleLabel.setForeground(new Color(-1705985));
        titleLabel.setHorizontalAlignment(2);
        titleLabel.setText("chatbook");
        navPanel.add(titleLabel, BorderLayout.WEST);
        userPanel = new JPanel();
        userPanel.setLayout(new GridLayoutManager(1, 4, new Insets(0, 0, 0, 0), -1, -1));
        userPanel.setBackground(new Color(-12887656));
        navPanel.add(userPanel, BorderLayout.EAST);
        userLabel = new JLabel();
        userLabel.setForeground(new Color(-2561551));
        userLabel.setText("Label");
        userPanel.add(userLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        deconnectButton = new JButton();
        deconnectButton.setText("");
        deconnectButton.setToolTipText("Se déconnecter");
        userPanel.add(deconnectButton, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(10, 10), null, 0, false));
        updateAccountButton = new JButton();
        updateAccountButton.setText("");
        updateAccountButton.setToolTipText("Modifier mon compte");
        userPanel.add(updateAccountButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        waitingFriendshipsButton = new JButton();
        waitingFriendshipsButton.setFont(new Font(waitingFriendshipsButton.getFont().getName(), Font.BOLD, waitingFriendshipsButton.getFont().getSize()));
        waitingFriendshipsButton.setText("");
        waitingFriendshipsButton.setToolTipText("Demandes d'amitié en attente");
        userPanel.add(waitingFriendshipsButton, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        bodyPanel = new JPanel();
        bodyPanel.setLayout(new BorderLayout(0, 0));
        mainPanel.add(bodyPanel, BorderLayout.CENTER);
        dashboardPanel = new JPanel();
        dashboardPanel.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        bodyPanel.add(dashboardPanel, BorderLayout.NORTH);
        accountsButton = new JButton();
        accountsButton.setText("Utilisateurs");
        dashboardPanel.add(accountsButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(204, 32), null, 0, false));
        discussionsButton = new JButton();
        discussionsButton.setText("Discussions");
        dashboardPanel.add(discussionsButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        searchButton = new JButton();
        searchButton.setText("Recherche d'amis");
        dashboardPanel.add(searchButton, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        contentPanel = new JPanel();
        contentPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        bodyPanel.add(contentPanel, BorderLayout.CENTER);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }
}
