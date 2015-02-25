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

        sidebar = new SideBar(this);
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

    	// Update metrics table
    	String[] rowData = {
    			String.valueOf(dataAnalytics.totalClicks(clickLogs)),
    			String.valueOf(dataAnalytics.noOfImpression(impressionLogs)),
    			String.valueOf(dataAnalytics.noOfUniques(clickLogs)),
    			String.valueOf(dataAnalytics.noOfBounces(serverLogs, 5)),
    			String.valueOf(dataAnalytics.noOfConversions(serverLogs)),
    			round(dataAnalytics.totalCost(impressionLogs, clickLogs).floatValue(), 2),
    			round(dataAnalytics.getCTR(clickLogs, impressionLogs).floatValue(), 4),
    			round(dataAnalytics.getCPA(impressionLogs, clickLogs, serverLogs).floatValue(), 4),
    			round(dataAnalytics.getCPC(impressionLogs, clickLogs).floatValue(), 4),
    			round(dataAnalytics.getCPM(impressionLogs, clickLogs).floatValue(), 4),
    			round(dataAnalytics.bounceRate(5, clickLogs, serverLogs).floatValue(), 4)
    	};
    	content.setMetrics(0, rowData);
    	content.setHeaderMetrics(
    			String.valueOf(dataAnalytics.totalClicks(clickLogs)), 
    			String.valueOf(dataAnalytics.noOfImpression(impressionLogs)),
    			round(dataAnalytics.totalCost(impressionLogs, clickLogs).floatValue(), 2)
    			);
        
    }
    
    public String round(double value, int scale){
		
		return new BigDecimal(String.valueOf(value)).setScale(scale, BigDecimal.ROUND_HALF_UP).toString();
		
	}
    
}
