import javafx.embed.swing.JFXPanel;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.Tooltip;
import javax.swing.*;
import java.awt.*;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

@SuppressWarnings("serial")
public class Chart extends JFXPanel {

	private int xDim = 820;
	private int yDim = 370;

	private Scene scene;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	double initXLowerBound = 0, initXUpperBound = 0, initYLowerBound = 0, initYUpperBound = 0;

	public Chart() {

		super();
		this.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.gray));

	}

	public void setSDFFormat(String s) {

		try {
			sdf = new SimpleDateFormat(s);
		} catch (NullPointerException e) {
			System.err.println("SDF argument cannot be null");
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			System.err.println("SDF argument is not of the valid format");
			e.printStackTrace();
		}

	}

	public void initFX() {

		CategoryAxis xAxis = new CategoryAxis();
		NumberAxis yAxis = new NumberAxis();
		xAxis.setLabel("Day");

		final LineChart<String, Number> lineChart =
				new LineChart<String, Number>(xAxis, yAxis);

		lineChart.setLegendVisible(false);

		scene = new Scene(lineChart, xDim, yDim);
		scene.getStylesheets().add("chartstyle.css");
		this.setScene(scene);

		lineChart.setCursor(Cursor.CROSSHAIR);
		displayOnHover(lineChart);

	}

	public void showImpressionsChart(ArrayList<ImpressionLog> impressionList) {

		CategoryAxis xAxis = new CategoryAxis();
		NumberAxis yAxis = new NumberAxis();
		xAxis.setLabel("Date");
		yAxis.setLabel("Number of Impressions");

		LinkedHashMap<String, Integer> impressionPairs = new LinkedHashMap<String, Integer>();
		String date;

		for (ImpressionLog impression : impressionList) {
			date = sdf.format(impression.getDate());
			if (!impressionPairs.containsKey(date)) {
				impressionPairs.put(date, 1);
			} else {
				impressionPairs.put(date, impressionPairs.get(date) + 1);
			}
		}

		LineChart<String, Number> lineChart =
				new LineChart<String, Number>(xAxis, yAxis);
		lineChart.setAnimated(true);
		XYChart.Series series = new XYChart.Series();
		series.setName("Impressions Over Time");

		for (Entry<String, Integer> entry : impressionPairs.entrySet()) {
			series.getData().add(new XYChart.Data(entry.getKey(), entry.getValue()));
		}
		lineChart.getData().add(series);


		lineChart.setCursor(Cursor.CROSSHAIR);
		scene = new Scene(lineChart, xDim, yDim);
		scene.getStylesheets().add("chartstyle.css");
		this.setScene(scene);
		displayOnHover(lineChart);

	}

	public void showUniqueChart(HashSet<ClickLog> hashSet) {
		CategoryAxis xAxis = new CategoryAxis();
		NumberAxis yAxis = new NumberAxis();
		xAxis.setLabel("Date");
		yAxis.setLabel("Number of Unique Clicks");


		LinkedHashMap<String, Integer> uniquePairs = new LinkedHashMap<String, Integer>();
		String date;

		for (ClickLog click : hashSet) {
			date = sdf.format(click.getDate());
			if (!uniquePairs.containsKey(date)) {
				uniquePairs.put(date, 1);
			} else {
				uniquePairs.put(date, uniquePairs.get(date) + 1);
			}
		}

		LineChart<String, Number> lineChart =
				new LineChart<String, Number>(xAxis, yAxis);

		XYChart.Series series = new XYChart.Series();
		series.setName("Unique Clicks Over Time");

		for (Entry<String, Integer> entry : uniquePairs.entrySet()) {
			series.getData().add(new XYChart.Data(entry.getKey(), entry.getValue()));
		}
		lineChart.getData().add(series);

		scene = new Scene(lineChart, xDim, yDim);
		scene.getStylesheets().add("chartstyle.css");
		this.setScene(scene);

		lineChart.setCursor(Cursor.CROSSHAIR);
		displayOnHover(lineChart);
	}

	public void showClicksChart(ArrayList<ClickLog> clickList) {
		
		CategoryAxis xAxis = new CategoryAxis();
		NumberAxis yAxis = new NumberAxis();
		xAxis.setLabel("Date");
		yAxis.setLabel("Number of Clicks");


		LinkedHashMap<String, Integer> clickPairs = new LinkedHashMap<String, Integer>();
		String date;


		/* 'Counts' number of clicks per day */
		/*TODO Implement granularity */
		for (ClickLog click : clickList) {
			date = sdf.format(click.getDate());
			if (!clickPairs.containsKey(date)) {
				clickPairs.put(date, 1);
			} else {
				clickPairs.put(date, clickPairs.get(date) + 1);
			}
		}


		LineChart<String, Number> lineChart =
				new LineChart<String, Number>(xAxis, yAxis);

		XYChart.Series series = new XYChart.Series();
		series.setName("Clicks Over Time");

		for (Entry<String, Integer> entry : clickPairs.entrySet()) {
			series.getData().add(new XYChart.Data(entry.getKey(), entry.getValue()));
		}
		lineChart.getData().add(series);

		scene = new Scene(lineChart, xDim, yDim);
		scene.getStylesheets().add("chartstyle.css");
		this.setScene(scene);

		lineChart.setCursor(Cursor.CROSSHAIR);
		displayOnHover(lineChart);
		
	}

	public void showBounceChart(ArrayList<ServerLog> serverList, int bounce) {
		
		CategoryAxis xAxis = new CategoryAxis();
		NumberAxis yAxis = new NumberAxis();
		xAxis.setLabel("Date");
		yAxis.setLabel("Number of Bounces");

		LinkedHashMap<String, Integer> bouncePairs = new LinkedHashMap<String, Integer>();
		String date;

		for (ServerLog server : serverList) {
			date = sdf.format(server.getStartDate());
			if (server.getPagesViewed() < bounce) {
				if (!bouncePairs.containsKey(date)) {
					bouncePairs.put(date, 1);
				} else {
					bouncePairs.put(date, bouncePairs.get(date) + 1);
				}
			}
		}

		LineChart<String, Number> lineChart =
				new LineChart<String, Number>(xAxis, yAxis);

		XYChart.Series series = new XYChart.Series();
		series.setName("Bounces Over Time");

		for (Entry<String, Integer> entry : bouncePairs.entrySet()) {
			series.getData().add(new XYChart.Data(entry.getKey(), entry.getValue()));
		}
		lineChart.getData().add(series);

		scene = new Scene(lineChart, xDim, yDim);
		scene.getStylesheets().add("chartstyle.css");
		this.setScene(scene);

		lineChart.setCursor(Cursor.CROSSHAIR);
		displayOnHover(lineChart);
		
	}

	public void showConversionChart(ArrayList<ServerLog> serverList) {
		
		CategoryAxis xAxis = new CategoryAxis();
		NumberAxis yAxis = new NumberAxis();
		xAxis.setLabel("Date");
		yAxis.setLabel("Number of Conversions");

		LinkedHashMap<String, Integer> convertPairs = new LinkedHashMap<String, Integer>();
		String date;

		for (ServerLog server : serverList) {
			date = sdf.format(server.getStartDate());
			if (server.isConverted()) {
				if (!convertPairs.containsKey(date)) {
					convertPairs.put(date, 1);
				} else {
					convertPairs.put(date, convertPairs.get(date) + 1);
				}
			}
		}

		LineChart<String, Number> lineChart =
				new LineChart<String, Number>(xAxis, yAxis);

		XYChart.Series series = new XYChart.Series();
		series.setName("Conversions Over Time");

		for (Entry<String, Integer> entry : convertPairs.entrySet()) {
			series.getData().add(new XYChart.Data(entry.getKey(), entry.getValue()));
		}
		lineChart.getData().add(series);

		scene = new Scene(lineChart, xDim, yDim);
		scene.getStylesheets().add("chartstyle.css");
		this.setScene(scene);

		lineChart.setCursor(Cursor.CROSSHAIR);
		displayOnHover(lineChart);
	}

	public void showCumulativeCost(ArrayList<ClickLog> clickList) {
		
		CategoryAxis xAxis = new CategoryAxis();
		NumberAxis yAxis = new NumberAxis();
		xAxis.setLabel("Date");
		yAxis.setLabel("Cumulative Cost (pence)");

		LinkedHashMap<String, Double> costPairs = new LinkedHashMap<String, Double>();
		String date, previous = null;

		for (ClickLog click : clickList) {
			date = sdf.format(click.getDate());
			if (!costPairs.containsKey(date)) {
				if (previous != null) {
					costPairs.put(date, click.getClickCost() + costPairs.get(previous));
				} else {
					costPairs.put(date, click.getClickCost());
				}
			} else {
				costPairs.put(date, costPairs.get(date) + click.getClickCost());
			}
			previous = date;
		}

		LineChart<String, Number> lineChart =
				new LineChart<String, Number>(xAxis, yAxis);

		XYChart.Series series = new XYChart.Series();
		series.setName("Cumulative Cost Over Time");

		for (Entry<String, Double> entry : costPairs.entrySet()) {
			series.getData().add(new XYChart.Data(entry.getKey(), entry.getValue()));
		}
		lineChart.getData().add(series);

		scene = new Scene(lineChart, xDim, yDim);
		scene.getStylesheets().add("chartstyle.css");
		this.setScene(scene);

		lineChart.setCursor(Cursor.CROSSHAIR);
		displayOnHover(lineChart);
		
	}

	public void showClickCosts(ArrayList<ClickLog> clickList) {
		
		CategoryAxis xAxis = new CategoryAxis();
		NumberAxis yAxis = new NumberAxis();
		xAxis.setLabel("Date");
		yAxis.setLabel("Cost (pence)");

		LinkedHashMap<String, Double> costPairs = new LinkedHashMap<String, Double>();
		String date;

		for (ClickLog click : clickList) {
			date = sdf.format(click.getDate());
			if (!costPairs.containsKey(date)) {
				costPairs.put(date, click.getClickCost());
			} else {
				costPairs.put(date, costPairs.get(date) + click.getClickCost());
			}
		}

		BarChart<String, Number> barChart =
				new BarChart<String, Number>(xAxis, yAxis);

		XYChart.Series series = new XYChart.Series();
		series.setName("Click Costs Over Time");

		barChart.setBarGap(0);
		barChart.setCategoryGap(0);

		for (Entry<String, Double> entry : costPairs.entrySet()) {
			series.getData().add(new XYChart.Data(entry.getKey(), entry.getValue()));
		}
		barChart.getData().add(series);

		scene = new Scene(barChart, xDim, yDim);
		scene.getStylesheets().add("chartstyle.css");
		this.setScene(scene);
		
	}
	
	private void displayOnHover(LineChart<String, Number> lineChart) {

		for (XYChart.Series<String, Number> s : lineChart.getData()) {
			for (final XYChart.Data<String, Number> d : s.getData()) {
				Tooltip.install(d.getNode(), new Tooltip(
						d.getXValue().toString() + "\n" +
								"Number Of Events : " + d.getYValue()));

				//Adding class on hover
				d.getNode().setOnMouseEntered(new EventHandler<javafx.event.Event>() {

					public void handle(javafx.event.Event event) {
						d.getNode().getStyleClass().add("onHover");
					}

				});

				//Removing class on exit
				d.getNode().setOnMouseExited(new EventHandler<javafx.event.Event>() {

					public void handle(javafx.event.Event event) {
						d.getNode().getStyleClass().remove("onHover");
					}

				});
			}
		}

	}

}
