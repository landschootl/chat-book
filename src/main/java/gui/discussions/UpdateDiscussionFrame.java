package gui.discussions;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import domain.Discussion;
import domain.User;
import gui.AppFrame;

import javax.swing.*;
import java.awt.*;

/**
 * Created by landschoot on 09/12/16.
 */
public class UpdateDiscussionFrame extends AppFrame {

    private JPanel mainPanel;
    private JTextField titreTextField;
    private JButton saveButton;
    private JList friendsList;
    private JList usersList;
    private JLabel titleLabel;
    private JLabel managerLabel;

    private Discussion discussion;
    private DefaultListModel<User> friends;
    private DefaultListModel<User> users;

    public UpdateDiscussionFrame(Discussion discussion) {
        super();
        this.discussion = discussion;
        initComponents();
        initInformationsDiscution();
    }

    @Override
    public void initComponents() {
        this.setTitle("Gestion d'une discussion");
        this.setSize(400, 400);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setVisible(true);
        this.setContentPane(mainPanel);
    }

    public void initInformationsDiscution() {
        titreTextField.setText(discussion.getName());
        managerLabel.setText(managerLabel.getText() + discussion.getMod().getLogin());
    }

    public void initListUsers() {

    }

    public void initListFriends() {

    }

    public void initButton() {

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
        mainPanel.setLayout(new GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.setEnabled(true);
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new FormLayout("fill:d:noGrow,left:4dlu:noGrow,fill:d:grow,left:4dlu:noGrow,fill:d:grow,left:4dlu:noGrow,fill:d:noGrow", "center:d:noGrow"));
        mainPanel.add(panel1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        titleLabel = new JLabel();
        titleLabel.setText("Titre");
        CellConstraints cc = new CellConstraints();
        panel1.add(titleLabel, cc.xy(1, 1));
        titreTextField = new JTextField();
        panel1.add(titreTextField, cc.xy(3, 1, CellConstraints.FILL, CellConstraints.DEFAULT));
        managerLabel = new JLabel();
        managerLabel.setText("Gérant : ");
        panel1.add(managerLabel, cc.xy(7, 1));
        final Spacer spacer1 = new Spacer();
        panel1.add(spacer1, cc.xy(5, 1, CellConstraints.FILL, CellConstraints.DEFAULT));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.add(panel2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        panel2.add(scrollPane1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        friendsList = new JList();
        scrollPane1.setViewportView(friendsList);
        final JScrollPane scrollPane2 = new JScrollPane();
        panel2.add(scrollPane2, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        usersList = new JList();
        scrollPane2.setViewportView(usersList);
        final JSeparator separator1 = new JSeparator();
        panel2.add(separator1, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        saveButton = new JButton();
        saveButton.setText("Sauvegarder");
        mainPanel.add(saveButton, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }
}
