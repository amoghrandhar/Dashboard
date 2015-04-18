import javax.swing.*;

import java.awt.*;
import java.math.BigDecimal;
import java.util.ArrayList;


public class Dashboard extends JFrame {

	public SideBar sidebar;
	public Content content;
	public DataAnalytics dataAnalytics;

	public final int DEFAULT_BOUNCE_PAGES_PROP = -1;
	public final int DEFAULT_BOUNCE_TIME_PROP = -1;

	private ArrayList<ClickLog> clickLogs;
	private ArrayList<ImpressionLog> impressionLogs;
	private ArrayList<ServerLog> serverLogs;

	private ArrayList<ClickLog> clickLogs2;
	private ArrayList<ImpressionLog> impressionLogs2;
	private ArrayList<ServerLog> serverLogs2;

	private ArrayList<ClickLog> originalClickLogs;
	private ArrayList<ImpressionLog> originalImpressionLogs;
	private ArrayList<ServerLog> originalServerLogs;


	public Dashboard(String title) {
		super(title);
		dataAnalytics = new DataAnalytics();
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
	public void setOriginalLogs(ArrayList<ClickLog> clickLogs, ArrayList<ImpressionLog> impressionLogs, ArrayList<ServerLog> serverLogs) {
		originalClickLogs = clickLogs;
		originalImpressionLogs = impressionLogs;
		originalServerLogs = serverLogs;
		updateLogs(originalClickLogs , originalImpressionLogs, originalServerLogs);
		updateLogs2(originalClickLogs , originalImpressionLogs, originalServerLogs);
	}

	// Reset log files to default (of selected series)
	public void resetLogs() {
		
		if(sidebar.selectedSeries == 1)
			updateLogs(originalClickLogs, originalImpressionLogs, originalServerLogs);
		if(sidebar.selectedSeries == 2)
			updateLogs2(originalClickLogs, originalImpressionLogs, originalServerLogs);
		
	}

	// Update first set of log files
	public void updateLogs(ArrayList<ClickLog> clickLogArrayList,ArrayList<ImpressionLog> impressionArrayList
			, ArrayList<ServerLog> serverLogArrayList ){
		impressionLogs = impressionArrayList;
		clickLogs = clickLogArrayList;
		serverLogs = serverLogArrayList;
	}

	// Update second set of log files
	public void updateLogs2(ArrayList<ClickLog> clickLogArrayList,ArrayList<ImpressionLog> impressionArrayList
			, ArrayList<ServerLog> serverLogArrayList ){
		impressionLogs2 = impressionArrayList;
		clickLogs2 = clickLogArrayList;
		serverLogs2 = serverLogArrayList;
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

	public void updateMetrics(int pagesView , int timeSpent) {
		
		ArrayList<ClickLog> clickLogs = null;
		ArrayList<ImpressionLog> impressionLogs = null;
		ArrayList<ServerLog> serverLogs = null;

		if(sidebar.selectedSeries == 1){
			clickLogs = this.clickLogs;
			impressionLogs = this.impressionLogs;
			serverLogs = this.serverLogs;
		}
		if(sidebar.selectedSeries == 2){
			clickLogs = this.clickLogs2;
			impressionLogs = this.impressionLogs2;
			serverLogs = this.serverLogs2;
		}

		long clicks = DataAnalytics.totalClicks(clickLogs);
		long impressions = DataAnalytics.noOfImpression(impressionLogs);
		long uniques = DataAnalytics.noOfUniques(clickLogs);
		long bounces = DataAnalytics.noOfBounces(serverLogs,pagesView, timeSpent);
		long conversions = DataAnalytics.noOfConversions(serverLogs);
		double totalCost = DataAnalytics.totalCost(impressionLogs, clickLogs);
		double CTR = DataAnalytics.getCTR(clickLogs, impressionLogs);
		double CPA = DataAnalytics.getCPA(serverLogs , totalCost);
		double CPC = DataAnalytics.getCPC(clickLogs,totalCost);
		double CPM = DataAnalytics.getCPM(impressionLogs, totalCost);
		double bounceRate = DataAnalytics.bounceRate(clickLogs, serverLogs , pagesView, timeSpent);

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

		if(sidebar.selectedSeries == 1)
			content.setMetrics(0, rowData);
		if(sidebar.selectedSeries == 2)
			content.setMetrics(1, rowData);

		// TODO Header with two series does not make sense

		//        content.setHeaderMetrics(
		//                String.valueOf(clicks),
		//                String.valueOf(impressions),
		//                round(totalCost, 2));

	}

	public void updateHeader(){

		long clicks = DataAnalytics.totalClicks(clickLogs);
		long impressions = DataAnalytics.noOfImpression(impressionLogs);
		double totalCost = DataAnalytics.totalCost(impressionLogs, clickLogs);

		content.setHeaderMetrics(
				String.valueOf(clicks),
				String.valueOf(impressions),
				round(totalCost, 2));

	}
	
	// Return true if comparison mode is enabled
	public Boolean isComparing(){
		
		if(sidebar.compareButton.isSelected())
			return true;
		
		else return false;
		
	}

	// Modify GUI when comparison mode is toggled
	public void updateComparing(Boolean comparing){

		if(comparing){

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
		}

		else{

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
	
	public void updateGraph(int item){
		
		if(!isComparing()){

			switch (item) {
			case 1:
				content.chart.showImpressionsChart(getImpressionLogs());
				break;
			case 2:
				content.chart.showUniqueChart(dataAnalytics.uniqueClickSet(getClickLogs()));
				break;
			case 3:
				content.chart.showBounceChart(dataAnalytics.getFilteredServerLogOnBounce(getServerLogs(), 
						sidebar.getChosenPages(), sidebar.getChosenTime()));
				break;
			case 4:
				content.chart.showConversionChart(getServerLogs());
				break;
			case 5:
				content.chart.showCumulativeCostChart(getClickLogs(), getImpressionLogs());
				break;
			case 6:
				content.chart.showClickCostsHistogram(getClickLogs());
				break;
			default:
				content.chart.showClicksChart(getClickLogs());
				break;
			}

		}

		if(isComparing()){

			switch (item) {
			case 1:
				content.chart.showImpressionsChartMarcos(getImpressionLogs(), getImpressionLogs2());
				break;
			case 2:
				content.chart.showUniqueChart2(dataAnalytics.uniqueClickSet(getClickLogs()),
						dataAnalytics.uniqueClickSet(getClickLogs2()));
				break;
			case 3:
				content.chart.showBounceChart2(
						dataAnalytics.getFilteredServerLogOnBounce(
								getServerLogs(), 
								sidebar.getSeriesBouncePages(sidebar.series1), 
								sidebar.getSeriesBounceTime(sidebar.series1)),
						dataAnalytics.getFilteredServerLogOnBounce(
								getServerLogs2(),
								sidebar.getSeriesBouncePages(sidebar.series2), 
								sidebar.getSeriesBounceTime(sidebar.series2)));
				break;
			case 4:
				content.chart.showConversionChart2(getServerLogs(), getServerLogs2());
				break;
			case 5:
				content.chart.showCumulativeCostChart2(
						getClickLogs(),getImpressionLogs(),
						getClickLogs2(),getImpressionLogs2());
				break;
			case 6:
				content.chart.showClickCostsHistogram2(getClickLogs(), getClickLogs2());
				break;
			default:
				content.chart.showClicksChart2(getClickLogs(), getClickLogs2());
				break;
			}

		}
		
	}
	
	public void updatePieCharts(){
		
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

}
