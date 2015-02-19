import javax.swing.*;
import java.awt.*;


public class Import extends JFrame {

    JButton openButton, cancelButton;

    public Import(String title) {

        super(title);

    }

    public void init() {

        this.setLayout(new GridBagLayout());

        GridBagConstraints formPanelC = new GridBagConstraints();
        formPanelC.gridx = 0;
        formPanelC.gridy = 0;

        GridBagConstraints buttonPanelC = new GridBagConstraints();
        buttonPanelC.gridx = 0;
        buttonPanelC.gridy = 1;
        buttonPanelC.anchor = GridBagConstraints.PAGE_END;

        GridBagConstraints label1 = new GridBagConstraints();
        formPanelC.gridx = 0;
        formPanelC.gridy = 0;

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        this.add(formPanel, formPanelC);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridBagLayout());
        this.add(buttonPanel, buttonPanelC);

        JLabel impression = new JLabel("Impression");
        JTextField impressionField = new JTextField(16);
        JLabel click = new JLabel("Click");
        JTextField clickField = new JTextField(16);
        JLabel server = new JLabel("Server");
        JTextField serverField = new JTextField(16);

        cancelButton = new JButton("Cancel");
        buttonPanel.add(cancelButton);

        openButton = new JButton("Open");
        buttonPanel.add(openButton);

        this.pack();
        this.setMinimumSize(new Dimension(300, 360));
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setVisible(true);

    }

}
