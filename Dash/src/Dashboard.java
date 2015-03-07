import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.util.ArrayList;


public class Dashboard extends JFrame {

    public SideBar sidebar;
    public Content content;
    public DataAnalytics dataAnalytics;

    
    private ArrayList<ClickLog> clickLogs;
    private ArrayList<ImpressionLog> impressionLogs;
    private ArrayList<ServerLog> serverLogs;

    //Default Bounce Rate is 5

    public Dashboard(String title) {
        super(title);
        dataAnalytics = new DataAnalytics();
    }

    // Display login panel
    public void init() {

        sidebar = new SideBar(this , dataAnalytics);
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

    public void updateClickLogs(ArrayList<ClickLog> clickLogArrayList) {
        System.out.println("Dashboard.updateClickLogs :  Called  " + clickLogArrayList.size());
        clickLogs = clickLogArrayList;
    }

    public void updateImpresssionLogs(ArrayList<ImpressionLog> impressionArrayList) {
        impressionLogs = impressionArrayList;
    }

    public void updateServerLogs(ArrayList<ServerLog> serverLogArrayList) {
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
    	
    	long clicks = dataAnalytics.totalClicks(clickLogs);
    	long impressions = dataAnalytics.noOfImpression(impressionLogs);
    	long uniques = dataAnalytics.noOfUniques(clickLogs);
    	long bounces = dataAnalytics.noOfBounces(serverLogs, 5);
    	long conversions = dataAnalytics.noOfConversions(serverLogs);
    	double totalCost = dataAnalytics.totalCost(impressionLogs, clickLogs);
    	double CTR = dataAnalytics.getCTR(clickLogs, impressionLogs);
    	double CPA = dataAnalytics.getCPA(impressionLogs, clickLogs, serverLogs);
    	double CPC = dataAnalytics.getCPC(impressionLogs, clickLogs);
    	double CPM = dataAnalytics.getCPM(impressionLogs, clickLogs);
    	double bounceRate = dataAnalytics.bounceRate(5, clickLogs, serverLogs);
    	
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
    
    public String round(double value, int scale){
		
		return new BigDecimal(String.valueOf(value)).setScale(scale, BigDecimal.ROUND_HALF_UP).toString();
		
	}
    
}
