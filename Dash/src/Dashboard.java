import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;


public class Dashboard extends JFrame {

    public SideBar sidebar;
    public Content content;
    public DataAnalytics dataAnalytics;

    
    ArrayList<ClickLog> clickLogs;
    ArrayList<Impression> impressions;
    ArrayList<ServerLog> serverLogs;

    //Default Bounce Rate is 5

    public Dashboard(String title) {
        super(title);
        dataAnalytics = new DataAnalytics();
    }

    // Display login panel
    public void init() {

        sidebar = new SideBar(this);
        sidebar.setPreferredSize(new Dimension(200, 700));
        
        content = new Content();
        content.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, Color.gray));

        Container container = this.getContentPane();

        container.add(sidebar, BorderLayout.LINE_START);
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

    public void updateMetrics() {

        // Update metrics table
        String[] rowData = {
                String.valueOf(dataAnalytics.totalClicks(clickLogs)),
                String.valueOf(dataAnalytics.noOfImpression(impressions)),
                String.valueOf(dataAnalytics.noOfUniques(clickLogs)),
                String.valueOf(dataAnalytics.noOfBounces(serverLogs, 5)),
                String.valueOf(dataAnalytics.noOfConversions(serverLogs)),
                String.valueOf(dataAnalytics.totalCost(impressions, clickLogs).floatValue()),
                String.valueOf(dataAnalytics.getCTR(clickLogs, impressions).floatValue()),
                String.valueOf(dataAnalytics.getCPA(impressions, clickLogs, serverLogs).floatValue()),
                String.valueOf(dataAnalytics.getCPC(impressions, clickLogs).floatValue()),
                String.valueOf(dataAnalytics.getCPM(impressions, clickLogs).floatValue()),
                String.valueOf(dataAnalytics.bounceRate(5, clickLogs, serverLogs).floatValue())
        };
        content.setMetrics(0, rowData);
    }
    
}
