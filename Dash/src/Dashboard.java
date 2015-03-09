import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.function.Predicate;
import java.util.stream.Collectors;


public class Dashboard extends JFrame {

    public SideBar sidebar;
    public Content content;
    public DataAnalytics dataAnalytics;

    private ArrayList<ClickLog> clickLogs;
    private ArrayList<ImpressionLog> impressionLogs;
    private ArrayList<ServerLog> serverLogs;

    private ArrayList<ClickLog> originalClickLogs;
    private ArrayList<ImpressionLog> originalImpressionLogs;
    private ArrayList<ServerLog> originalServerLogs;


    public Dashboard(String title) {
        super(title);
        dataAnalytics = new DataAnalytics();
    }

    // Display login panel
    public void init() {

        sidebar = new SideBar(this, dataAnalytics);
        sidebar.setPreferredSize(new Dimension(200, 700));

        content = new Content(this);
        content.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, Color.gray));

        Container container = this.getContentPane();

        container.add(sidebar, BorderLayout.LINE_START);
        container.add(content);

        this.pack();
        this.setMinimumSize(new Dimension(1080, 740));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);

    }

    /**
     * This is used to set the original log files.
     *
     * @param clickLogs
     * @param impressionLogs
     * @param serverLogs
     */
    public void setOriginalLogs(ArrayList<ClickLog> clickLogs, ArrayList<ImpressionLog> impressionLogs, ArrayList<ServerLog> serverLogs) {
//        originalClickLogs = clickLogs;
        originalImpressionLogs = impressionLogs;
//        originalServerLogs = serverLogs;


        HashSet<Double> idSet = new HashSet<Double>();
        for (ImpressionLog impressionLog : impressionLogs) {
            idSet.add(impressionLog.getID());
        }

        Predicate<ClickLog> checkClicks = clp -> idSet.contains(clp.getID());
        Predicate<ServerLog> checkServers = sp -> idSet.contains(sp.getID());

        originalClickLogs = (ArrayList <ClickLog>) clickLogs.parallelStream().filter(checkClicks).collect(Collectors.toList());
        originalServerLogs = (ArrayList<ServerLog>) serverLogs.parallelStream().filter(checkServers).collect(Collectors.toList());

        updateLogs(originalClickLogs , originalImpressionLogs, originalServerLogs);
    }

    /**
     * This will reset the log files.
     */
    public void resetLogs() {
        updateLogs(originalClickLogs, originalImpressionLogs, originalServerLogs);
    }


    public void updateLogs(ArrayList<ClickLog> clickLogArrayList,ArrayList<ImpressionLog> impressionArrayList
            , ArrayList<ServerLog> serverLogArrayList ){
        impressionLogs = impressionArrayList;
        clickLogs = clickLogArrayList;
        serverLogs = serverLogArrayList;

    }


    public ArrayList<ClickLog> getClickLogs() {
        return clickLogs;
    }

    public ArrayList<ImpressionLog> getImpressionLogs() {
        return impressionLogs;
    }

    public ArrayList<ServerLog> getServerLogs() {
        return serverLogs;
    }

    public void defaultChart() {
        content.defaultChart();
    }

    public void updateMetrics() {

        long clicks = DataAnalytics.totalClicks(clickLogs);
        long impressions = DataAnalytics.noOfImpression(impressionLogs);
        long uniques = DataAnalytics.noOfUniques(clickLogs);
        long bounces = originalServerLogs.size() - DataAnalytics.noOfBounces(serverLogs);
        long conversions = DataAnalytics.noOfConversions(serverLogs);
        double totalCost = DataAnalytics.totalCost(impressionLogs, clickLogs);
        double CTR = DataAnalytics.getCTR(clickLogs, impressionLogs);
        double CPA = DataAnalytics.getCPA(impressionLogs, clickLogs, serverLogs);
        double CPC = DataAnalytics.getCPC(impressionLogs, clickLogs);
        double CPM = DataAnalytics.getCPM(impressionLogs, clickLogs);
        double bounceRate = DataAnalytics.bounceRate(clickLogs, serverLogs);

        // Update metrics table
        String[] rowData = {
                String.valueOf(clicks),
                String.valueOf(impressions),
                String.valueOf(uniques),
                String.valueOf(bounces),
                String.valueOf(conversions),
                round(totalCost, 2),
                round(CTR, 4),
                round(CPA, 4),
                round(CPC, 4),
                round(CPM, 4),
                round(bounceRate, 4)
        };
        content.setMetrics(0, rowData);
        content.setHeaderMetrics(
                String.valueOf(clicks),
                String.valueOf(impressions),
                round(totalCost, 2));

    }

    public String round(double value, int scale) {

        if (Double.isFinite(value)) {
            return new BigDecimal(String.valueOf(value)).setScale(scale, BigDecimal.ROUND_HALF_UP).toString();
        } else {
            return "0";
        }
    }

}
