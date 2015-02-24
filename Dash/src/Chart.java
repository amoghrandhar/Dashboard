import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;


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

		series.getData().add(new XYChart.Data("Jan", 23));
		series.getData().add(new XYChart.Data("Feb", 14));
		series.getData().add(new XYChart.Data("Mar", 15));
		series.getData().add(new XYChart.Data("Apr", 24));
		series.getData().add(new XYChart.Data("May", 34));
		series.getData().add(new XYChart.Data("Jun", 36));
		series.getData().add(new XYChart.Data("Jul", 22));
		series.getData().add(new XYChart.Data("Aug", 45));
		series.getData().add(new XYChart.Data("Sep", 43));
		series.getData().add(new XYChart.Data("Oct", 17));
		series.getData().add(new XYChart.Data("Nov", 29));
		series.getData().add(new XYChart.Data("Dec", 25));
		lineChart.getData().add(series);
		
		scene = new Scene(lineChart, 600, 300);
		this.setScene(scene);
	}

	public void showClicksChart() {
		
	}
}
