import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;


@SuppressWarnings("serial")
public class Chart extends JFXPanel{
	
	private Scene scene;
	
	public Chart() {
		super();
	}
	
	public void initFX() {
		CategoryAxis xAxis = new CategoryAxis();
		NumberAxis yAxis = new NumberAxis();
		xAxis.setLabel("Month");       

		final LineChart<String,Number> lineChart = 
				new LineChart<String,Number>(xAxis,yAxis);

		//lineChart.setTitle("Stock Monitoring, 2010");

		XYChart.Series series = new XYChart.Series();
		//series.setName("My portfolio");

		series.getData().add(new XYChart.Data<String, Integer>("Jan", 23));
		series.getData().add(new XYChart.Data<String, Integer>("Feb", 14));
		series.getData().add(new XYChart.Data<String, Integer>("Mar", 15));
		series.getData().add(new XYChart.Data<String, Integer>("Apr", 24));
		series.getData().add(new XYChart.Data<String, Integer>("May", 34));
		series.getData().add(new XYChart.Data<String, Integer>("Jun", 36));
		series.getData().add(new XYChart.Data<String, Integer>("Jul", 22));
		series.getData().add(new XYChart.Data<String, Integer>("Aug", 45));
		series.getData().add(new XYChart.Data<String, Integer>("Sep", 43));
		series.getData().add(new XYChart.Data<String, Integer>("Oct", 17));
		series.getData().add(new XYChart.Data<String, Integer>("Nov", 29));
		series.getData().add(new XYChart.Data<String, Integer>("Dec", 25));
		lineChart.getData().add(series);
		
		scene = new Scene(lineChart, 600, 300);
		this.setScene(scene);
	}

	public void showClicksChart(ArrayList<ClickLog> clickList) {
		CategoryAxis xAxis = new CategoryAxis();
		NumberAxis yAxis = new NumberAxis();
		xAxis.setLabel("Date");
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		LinkedHashMap<String,Integer> clickPairs = new LinkedHashMap<String, Integer>();
		String s;
		
		
		/* 'Counts' number of clicks per day */
		/*TODO Implement granularity */
		for (ClickLog click : clickList) {
			s = sdf.format(click.getDate());
			if (!clickPairs.containsKey(s)) {
				clickPairs.put(s, 1);
			} else {
				clickPairs.put(s, clickPairs.get(s) + 1);
			}
		}
		
		
		final LineChart<String,Number> lineChart = 
				new LineChart<String,Number>(xAxis,yAxis);

		XYChart.Series series = new XYChart.Series();
		series.setName("Clicks Over Time");

		for (Entry<String,Integer> entry : clickPairs.entrySet()) {
			series.getData().add(new XYChart.Data(entry.getKey(), entry.getValue()));
		}
		lineChart.getData().add(series);
		
		scene = new Scene(lineChart, 600, 300);
		this.setScene(scene);
	}
}
