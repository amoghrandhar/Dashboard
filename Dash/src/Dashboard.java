import javax.swing.*;

import java.awt.*;
import java.math.BigDecimal;
import java.util.ArrayList;


public class Dashboard extends JFrame {

    public final int DEFAULT_BOUNCE_PAGES_PROP = -1;
    public final int DEFAULT_BOUNCE_TIME_PROP = -1;
    public SideBar sidebar;
    public Content content;
    public DataAnalytics dataAnalytics;
    private ArrayList<ClickLog> clickLogsC1;
    private ArrayList<ImpressionLog> impressionLogsC1;
    private ArrayList<ServerLog> serverLogsC1;

    private ArrayList<ClickLog> clickLogs2;
    private ArrayList<ImpressionLog> impressionLogs2;
    private ArrayList<ServerLog> serverLogs2;

    private boolean firstCampaign;
    private ArrayList<ClickLog> originalClickLogsC1;
    private ArrayList<ImpressionLog> originalImpressionLogsC1;
    private ArrayList<ServerLog> originalServerLogsC1;

    private boolean secondCampaign;
    private ArrayList<ClickLog> originalClickLogsC2;
    private ArrayList<ImpressionLog> originalImpressionLogsC2;
    private ArrayList<ServerLog> originalServerLogsC2;


    public Dashboard(String title) {
        super(title);
        dataAnalytics = new DataAnalytics();
        secondCampaign = false;
        firstCampaign = false;
    }

    // Display login panel
    public void init(String[] colours) {

        sidebar = new SideBar(this, dataAnalytics, colours);
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
    public void setOriginalLogsC1(ArrayList<ClickLog> clickLogs, ArrayList<ImpressionLog> impressionLogs, ArrayList<ServerLog> serverLogs) {
        originalClickLogsC1 = clickLogs;
        originalImpressionLogsC1 = impressionLogs;
        originalServerLogsC1 = serverLogs;
        firstCampaign = true;
        updateLogs(originalClickLogsC1, originalImpressionLogsC1, originalServerLogsC1);
        if(secondCampaign == false){
            updateLogs2(originalClickLogsC1, originalImpressionLogsC1, originalServerLogsC1);
        }
        else{
            updateLogs2(originalClickLogsC2, originalImpressionLogsC2, originalServerLogsC2);
        }

    }

    /**
     * This is used to set the original log files.
     *
     * @param clickLogs
     * @param impressionLogs
     * @param serverLogs
     */
    public void setOriginalLogsC2(ArrayList<ClickLog> clickLogs, ArrayList<ImpressionLog> impressionLogs, ArrayList<ServerLog> serverLogs) {
        originalClickLogsC2 = clickLogs;
        originalImpressionLogsC2 = impressionLogs;
        originalServerLogsC2 = serverLogs;
        secondCampaign = true;
        updateLogs(originalClickLogsC1, originalImpressionLogsC1, originalServerLogsC1);
        updateLogs2(originalClickLogsC2, originalImpressionLogsC2, originalServerLogsC2);
        
        String[] graphChoices = {"Campaign1 1", "Campaign 2"};
        
        sidebar.compareBox.removeItemAt(1);
        sidebar.compareBox.removeItemAt(0);
        
        sidebar.compareBox.addItem(new String("Campaign 1"));
        sidebar.compareBox.addItem(new String("Campaign 2"));
        
    }

    // Reset log files to default (of selected series)
    public void resetLogs() {

        if (sidebar.selectedSeries == 1)
            updateLogs(originalClickLogsC1, originalImpressionLogsC1, originalServerLogsC1);
        if (sidebar.selectedSeries == 2 && secondCampaign )
            updateLogs2(originalClickLogsC2, originalImpressionLogsC2, originalServerLogsC2);
        else updateLogs2(originalClickLogsC1, originalImpressionLogsC1, originalServerLogsC1);

    }

    // Update first set of log files
    public void updateLogs(ArrayList<ClickLog> clickLogArrayList, ArrayList<ImpressionLog> impressionArrayList
            , ArrayList<ServerLog> serverLogArrayList) {
        impressionLogsC1 = impressionArrayList;
        clickLogsC1 = clickLogArrayList;
        serverLogsC1 = serverLogArrayList;
    }

    // Update second set of log files
    public void updateLogs2(ArrayList<ClickLog> clickLogArrayList, ArrayList<ImpressionLog> impressionArrayList
            , ArrayList<ServerLog> serverLogArrayList) {
        impressionLogs2 = impressionArrayList;
        clickLogs2 = clickLogArrayList;
        serverLogs2 = serverLogArrayList;
    }

    public ArrayList<ClickLog> getClickLogsC1() {
        return clickLogsC1;
    }

    public ArrayList<ImpressionLog> getImpressionLogsC1() {
        return impressionLogsC1;
    }

    public ArrayList<ServerLog> getServerLogsC1() {
        return serverLogsC1;
    }

    public ArrayList<ClickLog> getClickLogs2() {
        return clickLogs2;
    }

    public ArrayList<ImpressionLog> getImpressionLogs2() {
        return impressionLogs2;
    }

    public ArrayList<ServerLog> getServerLogs2() {
        return serverLogs2;
    }

    public void defaultChart() {
        content.defaultChart();
    }

    public void updateMetrics(int pagesView, int timeSpent) {

        ArrayList<ClickLog> clickLogs = null;
        ArrayList<ImpressionLog> impressionLogs = null;
        ArrayList<ServerLog> serverLogs = null;

        if (sidebar.selectedSeries == 1) {
            clickLogs = this.clickLogsC1;
            impressionLogs = this.impressionLogsC1;
            serverLogs = this.serverLogsC1;
        }
        if (sidebar.selectedSeries == 2) {
            clickLogs = this.clickLogs2;
            impressionLogs = this.impressionLogs2;
            serverLogs = this.serverLogs2;
        }

        long clicks = DataAnalytics.totalClicks(clickLogs);
        long impressions = DataAnalytics.noOfImpression(impressionLogs);
        long uniques = DataAnalytics.noOfUniques(clickLogs);
        long bounces = DataAnalytics.noOfBounces(serverLogs, pagesView, timeSpent);
        long conversions = DataAnalytics.noOfConversions(serverLogs);
        double totalCost = DataAnalytics.totalCost(impressionLogs, clickLogs);
        double CTR = DataAnalytics.getCTR(clickLogs, impressionLogs);
        double CPA = DataAnalytics.getCPA(serverLogs, totalCost);
        double CPC = DataAnalytics.getCPC(clickLogs, totalCost);
        double CPM = DataAnalytics.getCPM(impressionLogs, totalCost);
        double bounceRate = DataAnalytics.bounceRate(clickLogs, serverLogs, pagesView, timeSpent);

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

        if (sidebar.selectedSeries == 1)
            content.setMetrics(0, rowData);
        if (sidebar.selectedSeries == 2)
            content.setMetrics(1, rowData);

        // TODO Header with two series does not make sense

        //        content.setHeaderMetrics(
        //                String.valueOf(clicks),
        //                String.valueOf(impressions),
        //                round(totalCost, 2));

    }

    public void updateHeader() {

        long clicks = DataAnalytics.totalClicks(clickLogsC1);
        long impressions = DataAnalytics.noOfImpression(impressionLogsC1);
        double totalCost = DataAnalytics.totalCost(impressionLogsC1, clickLogsC1);

        content.setHeaderMetrics(
                String.valueOf(clicks),
                String.valueOf(impressions),
                round(totalCost, 2));

    }

    // Return true if comparison mode is enabled
    public Boolean isComparing() {

        if (sidebar.compareButton.isSelected())
            return true;

        else return false;

    }

    // Modify GUI when comparison mode is toggled
    public void updateComparing(Boolean comparing) {

        if (comparing) {

            content.comparing = true;

            content.tablePanel.remove(content.scrollPane);

            content.table = content.createTable2(content.rowData2);
            content.setMetrics(0, content.row1);
            content.setMetrics(1, content.row1);
            content.scrollPane = new JScrollPane(content.table);
            content.scrollPane.setPreferredSize(new Dimension(
                    826,
                    (int) content.table.getPreferredSize().getHeight() + 28
            ));

            content.tablePanel.add(content.scrollPane);

            repaint();
        } else {

            content.comparing = false;

            content.tablePanel.remove(content.scrollPane);

            content.table = content.createTable(content.rowData);
            content.setMetrics(0, content.row1);
            content.scrollPane = new JScrollPane(content.table);
            content.scrollPane.setPreferredSize(new Dimension(
                    826,
                    (int) content.table.getPreferredSize().getHeight() + 28
            ));

            content.tablePanel.add(content.scrollPane);

            repaint();
        }

    }

    public void updateGraph(int item) {

        if (!isComparing()) {

            switch (item) {
                case 1:
                    content.chart.showImpressionsChart(getImpressionLogsC1());
                    break;
                case 2:
                    content.chart.showUniqueChart(dataAnalytics.uniqueClickSet(getClickLogsC1()));
                    break;
                case 3:
                    content.chart.showBounceChart(dataAnalytics.getFilteredServerLogOnBounce(getServerLogsC1(),
                            sidebar.getChosenPages(), sidebar.getChosenTime()));
                    break;
                case 4:
                    content.chart.showConversionChart(getServerLogsC1());
                    break;
                case 5:
                    content.chart.showCumulativeCostChart(getClickLogsC1(), getImpressionLogsC1());
                    break;
                case 6:
                    content.chart.showClickCostsHistogram(getClickLogsC1());
                    break;
                default:
                    content.chart.showClicksChart(getClickLogsC1());
                    break;
            }

        }

        if (isComparing()) {

            switch (item) {
                case 1:
                    content.chart.showImpressionsChartMarcos(getImpressionLogsC1(), getImpressionLogs2());
                    break;
                case 2:
                    content.chart.showUniqueChart2(dataAnalytics.uniqueClickSet(getClickLogsC1()),
                            dataAnalytics.uniqueClickSet(getClickLogs2()));
                    break;
                case 3:
                    content.chart.showBounceChart2(
                            dataAnalytics.getFilteredServerLogOnBounce(
                                    getServerLogsC1(),
                                    sidebar.getSeriesBouncePages(sidebar.series1),
                                    sidebar.getSeriesBounceTime(sidebar.series1)),
                            dataAnalytics.getFilteredServerLogOnBounce(
                                    getServerLogs2(),
                                    sidebar.getSeriesBouncePages(sidebar.series2),
                                    sidebar.getSeriesBounceTime(sidebar.series2)));
                    break;
                case 4:
                    content.chart.showConversionChart2(getServerLogsC1(), getServerLogs2());
                    break;
                case 5:
                    content.chart.showCumulativeCostChart2(
                            getClickLogsC1(), getImpressionLogsC1(),
                            getClickLogs2(), getImpressionLogs2());
                    break;
                case 6:
                    content.chart.showClickCostsHistogram2(getClickLogsC1(), getClickLogs2());
                    break;
                default:
                    content.chart.showClicksChart2(getClickLogsC1(), getClickLogs2());
                    break;
            }

        }

    }

    public void updatePieCharts() {

        content.pieChart1.showGenderPie();
        content.pieChart2.showAgeGroupPie();
        content.pieChart3.showIncomePie();
        content.pieChart4.showContextPie();

    }

    public String round(double value, int scale) {

        if (Double.isFinite(value)) {
            return new BigDecimal(String.valueOf(value)).setScale(scale, BigDecimal.ROUND_HALF_UP).toString();
        } else {
            return "0";
        }

    }

    public boolean isFirstCampaign() {
        return firstCampaign;
    }

    public boolean isSecondCampaign() {
        return secondCampaign;
    }

}
