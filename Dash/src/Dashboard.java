import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;


public class Dashboard extends JFrame {

    public JPanel menu;
    public JPanel content;


    private ArrayList<ClickLog> clickLogs;
    private ArrayList<Impression> impressions;
    private ArrayList<ServerLog> serverLogs;

    public Dashboard(String title) {
        super(title);
    }

    // Display login panel
    public void init() {

        menu = new SideBar();
        menu.setPreferredSize(new Dimension(200, 700));
        
        content = new Content();
        content.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, Color.gray));

        Container container = this.getContentPane();

        container.add(menu, BorderLayout.LINE_START);
        container.add(content);

        this.pack();
        this.setMinimumSize(new Dimension(1000, 700));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);

    }

    public void updateClickLogs(ArrayList<ClickLog> clickLogArrayList) {
        clickLogs = clickLogArrayList;
    }

    public void updateImpresssionLogs(ArrayList<Impression> impressionArrayList) {
        impressions = impressionArrayList;
    }

    public void updateServerLogs(ArrayList<ServerLog> serverLogArrayList) {
        serverLogs = serverLogArrayList;
    }

}
