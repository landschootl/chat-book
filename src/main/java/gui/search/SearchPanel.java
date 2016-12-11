package gui.search;

import domain.User;
import gui.components.PlaceholderTextField;
import persistence.uow.UnitOfWork;
import service.UserService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class SearchPanel extends JPanel {
    private UserService userService;
    private User userSelected;
    private UnitOfWork unitOfWork;

    private PlaceholderTextField firstnameSearch;
    private PlaceholderTextField lastnameSearch;
    private JButton searchButton;

    private DefaultListModel<User> accountsListModel;
    private JList accountsJList;

    private JPanel panelRight;
    private JLabel loginAccount;
    private JLabel lastnameAccount;
    private JLabel firstnameAccount;
    private JPanel infosAccountPanel;

    public SearchPanel() {
        this.userService = UserService.getInstance();
        this.unitOfWork = UnitOfWork.getInstance();
        this.setLayout(new BorderLayout(0, 0));

        initSearch();
        initAccountsPanelRight();
    }

    private void initSearch() {
        JPanel panelSearch = new JPanel();
        this.add(panelSearch, BorderLayout.NORTH);

        lastnameSearch = new PlaceholderTextField();
        lastnameSearch.setPlaceholder("Nom");
        lastnameSearch.setPreferredSize(new Dimension(200, 24));
        panelSearch.add(lastnameSearch);
        firstnameSearch = new PlaceholderTextField();
        firstnameSearch.setPlaceholder("Prénom");
        firstnameSearch.setPreferredSize(new Dimension(200, 24));
        panelSearch.add(firstnameSearch);
        searchButton = new JButton("Rechercher");
        panelSearch.add(searchButton);
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

        infosAccountPanel.setLayout(new GridLayout(3, 3));
        infosAccountPanel.add(firstnameAccount);
        infosAccountPanel.add(lastnameAccount);
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
}
