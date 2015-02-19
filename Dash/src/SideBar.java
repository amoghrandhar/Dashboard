import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class SideBar extends JPanel {

    JButton importButton, exportButton;
    JPanel filePanel, menuPanel;

    public SideBar() {

        init();

    }

    public static JPanel getDummyPanel(String name) {

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel(name, JLabel.CENTER));
        return panel;

    }

    public void init() {

        //this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        GridBagConstraints importC = new GridBagConstraints();
        GridBagConstraints exportC = new GridBagConstraints();

        importC.gridx = 0;
        importC.gridy = 0;
        importC.insets = new Insets(4, 0, 4, 0);

        exportC.gridx = 0;
        exportC.gridy = 1;
        exportC.insets = new Insets(4, 0, 4, 0);

        // Panels - Top import/export panel and bottom collapsible menu for filters, bounce definition, etc.

        filePanel = new JPanel();
        filePanel.setLayout(new GridBagLayout());

        menuPanel = new JPanel();
        menuPanel.setLayout(new GridBagLayout());
        menuPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.gray));

        // File Panel (Top)

        ImageIcon importIcon = new ImageIcon(getClass().getResource("plus.png"));
        Image img = importIcon.getImage();
        Image newimg = img.getScaledInstance(16, 16, java.awt.Image.SCALE_SMOOTH);
        importIcon = new ImageIcon(newimg);

        importButton = new JButton(" Import ", importIcon);
        importButton.setFont(new Font("", Font.PLAIN, 12));
        importButton.setPreferredSize(new Dimension(120, 46));
        importButton.setFocusable(false);
        importButton.addActionListener(new ImportListener());
        filePanel.add(importButton, importC);

        ImageIcon exportIcon = new ImageIcon(getClass().getResource("download.png"));
        Image img2 = exportIcon.getImage();
        Image newimg2 = img2.getScaledInstance(16, 16, java.awt.Image.SCALE_SMOOTH);
        exportIcon = new ImageIcon(newimg2);

        exportButton = new JButton(" Export ", exportIcon);
        exportButton.setFont(new Font("", Font.PLAIN, 12));
        exportButton.setPreferredSize(new Dimension(120, 46));
        exportButton.setFocusable(false);
        filePanel.add(exportButton, exportC);

        // Menu Panel (Bottom)


        // Frame

        filePanel.setBorder(new EmptyBorder(16, 0, 16, 0));

        menuPanel.setPreferredSize(new Dimension(200, 500));

        this.add(filePanel, BorderLayout.PAGE_START);
        this.add(menuPanel, BorderLayout.PAGE_END);

    }

}

class ImportListener implements ActionListener {

    public void actionPerformed(ActionEvent e) {


        Import importFrame = new Import("Import Files");
        importFrame.init();

    }

}
