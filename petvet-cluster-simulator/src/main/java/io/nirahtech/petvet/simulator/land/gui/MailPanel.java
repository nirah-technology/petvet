package io.nirahtech.petvet.simulator.land.gui;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class MailPanel extends JPanel {
    // département
    // code postal

    private final JLabel departmentTitle = new JLabel("<html><h3>Department</h3></html>");
    private final JTextField departmentTextField;
    private final JLabel zipCodeTitle = new JLabel("<html><h3>Zip Code</h3></html>");
    private final JTextField zipCodeTextField;

    public MailPanel() {
        this.departmentTextField = new JTextField();
        this.zipCodeTextField = new JTextField();
    }

}
