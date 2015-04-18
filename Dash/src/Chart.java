import javafx.embed.swing.JFXPanel;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.Tooltip;

import javax.swing.*;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.*;

@SuppressWarnings("serial")
public class Chart extends JFXPanel {

	private int xDim = 820;
	private int yDim = 370;

	private Scene scene;
	private LineChart lineChart;
	private CategoryAxis xAxis;
	private NumberAxis yAxis;
	private javafx.scene.control.ScrollPane scrollPane;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	ExecutorService executor =  Executors.newFixedThreadPool(10);

	public Chart() {

		super();
		this.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.gray));
		xAxis = new CategoryAxis();
		yAxis = new NumberAxis();
		this.lineChart = new LineChart(xAxis, yAxis);

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

	public void initFX(XYChart.Series series, String legend) {

		lineChart = new LineChart<String, Number>(xAxis, yAxis);
		series.setName(legend);
		lineChart.getData().add(series);
		lineChart.setAnimated(true);
		lineChart.setCursor(Cursor.CROSSHAIR);
		displayOnHover(lineChart);

		scene = new Scene(lineChart, xDim, yDim);
		scene.getStylesheets().add("chartstyle.css");
		this.setScene(scene);

	}

	public void initFX(XYChart.Series series1, XYChart.Series series2) {

		lineChart = new LineChart<String, Number>(xAxis, yAxis);
		series1.setName("Series 1");
		series2.setName("Series 2");
		lineChart.getData().addAll(series1,series2);
		lineChart.setAnimated(true);
		lineChart.setCursor(Cursor.CROSSHAIR);
		displayOnHover(lineChart);

		scene = new Scene(lineChart, xDim, yDim);
		//scene.getStylesheets().add("chartstyle.css");
		this.setScene(scene);

	}

	public void showImpressionsChart(ArrayList<ImpressionLog> impressionList) {

		xAxis.setLabel("Date");
		yAxis.setLabel("Number of Impressions");

		XYChart.Series series = new XYChart.Series();

		int i = 0;
		for (Entry<String, Integer> entry : getImpressionPairs(impressionList).entrySet()) {
			i++;
			series.getData().add(new XYChart.Data(String.valueOf(i), entry.getValue()));
		}

		initFX(series, "Impressions over Time");

	}

	public void showImpressionsChart(ArrayList<ImpressionLog> impressionList1, ArrayList<ImpressionLog> impressionList2) {
		System.out.println("Chart.showImpressionsChart");
		xAxis.setLabel("Date");
		yAxis.setLabel("Number of Impressions");


		MyImpressionWorker w1 = new MyImpressionWorker(impressionList1);
		executor.submit(w1);
		MyImpressionWorker w2 = new MyImpressionWorker(impressionList2);
		executor.submit(w2);

//		executor.shutdown(); /// To Check this Linee of Code

//		List<Future<Object>> futureList = executor.invokeAll();

		XYChart.Series series1 = new XYChart.Series();
		XYChart.Series series2 = new XYChart.Series();

		int i = 0;
		for (Entry<String, Integer> entry : w1.impressionPairs.entrySet()) {
			i++;
			series1.getData().add(new XYChart.Data(String.valueOf(i), entry.getValue()));
		}

		i = 0;
		for (Entry<String, Integer> entry : w2.impressionPairs.entrySet()) {
			i++;
			series2.getData().add(new XYChart.Data(String.valueOf(i), entry.getValue()));
		}

		initFX(series1 , series2);

	}

	public void showImpressionsChartMarcos(ArrayList<ImpressionLog> impressionList1, ArrayList<ImpressionLog> impressionList2) {

		xAxis.setLabel("Date");
		yAxis.setLabel("Number of Impressions");

		XYChart.Series series1 = new XYChart.Series();
		XYChart.Series series2 = new XYChart.Series();

		int i = 0;
		for (Entry<String, Integer> entry : getImpressionPairs(impressionList1).entrySet()) {
			i++;
			series1.getData().add(new XYChart.Data(String.valueOf(i), entry.getValue()));
		}

		i = 0;
		for (Entry<String, Integer> entry : getImpressionPairs(impressionList2).entrySet()) {
			i++;
			series2.getData().add(new XYChart.Data(String.valueOf(i), entry.getValue()));
		}

		initFX(series1 , series2);

	}

	public LinkedHashMap<String, Integer> getImpressionPairs(ArrayList<ImpressionLog> impressionList1){

		LinkedHashMap<String, Integer> impressionPairs = new LinkedHashMap<String, Integer>();
		String date;

		for (ImpressionLog impression : impressionList1) {

			date = sdf.format(impression.getDate());

			if (!impressionPairs.containsKey(date))
				impressionPairs.put(date, 1);
			else
				impressionPairs.put(date, impressionPairs.get(date) + 1);

		}

		return impressionPairs;

	}

	public void showClicksChart(ArrayList<ClickLog> clickList) {

		xAxis.setLabel("Date");
		yAxis.setLabel("Number of Clicks");

		XYChart.Series series = new XYChart.Series();

		for (Entry<String, Integer> entry : getClickPairs(clickList).entrySet())
			series.getData().add(new XYChart.Data(entry.getKey(), entry.getValue()));

		initFX(series, "Clicks over Time");

	}

	public void showClicksChart2(ArrayList<ClickLog> clickList1, ArrayList<ClickLog> clickList2) {

		xAxis.setLabel("Date");
		yAxis.setLabel("Number of Clicks");

		XYChart.Series series1 = new XYChart.Series();
		XYChart.Series series2 = new XYChart.Series();

		for (Entry<String, Integer> entry : getClickPairs(clickList1).entrySet())
			series1.getData().add(new XYChart.Data(entry.getKey(), entry.getValue()));

		for (Entry<String, Integer> entry : getClickPairs(clickList2).entrySet())
			series2.getData().add(new XYChart.Data(entry.getKey(), entry.getValue()));

		initFX(series1, series2);

	}

	public LinkedHashMap<String, Integer> getClickPairs(ArrayList<ClickLog> clickList){

		LinkedHashMap<String, Integer> clickPairs = new LinkedHashMap<String, Integer>();
		String date;

		for (ClickLog click : clickList) {

			date = sdf.format(click.getDate());

			if (!clickPairs.containsKey(date))
				clickPairs.put(date, 1);
			else
				clickPairs.put(date, clickPairs.get(date) + 1);

		}

		return clickPairs;

	}

	public void showUniqueChart(HashSet<ClickLog> hashSet) {

		xAxis.setLabel("Date");
		yAxis.setLabel("Number of Unique Clicks");

		XYChart.Series series = new XYChart.Series();

		for (Entry<String, Integer> entry : getUniquePairs(hashSet).entrySet())
			series.getData().add(new XYChart.Data(entry.getKey(), entry.getValue()));

		initFX(series, "Unique Clicks over Time");

	}

	public void showUniqueChart2(HashSet<ClickLog> hashSet1, HashSet<ClickLog> hashSet2) {

		xAxis.setLabel("Date");
		yAxis.setLabel("Number of Unique Clicks");

		XYChart.Series series1 = new XYChart.Series();
		XYChart.Series series2 = new XYChart.Series();

		for (Entry<String, Integer> entry : getUniquePairs(hashSet1).entrySet())
			series1.getData().add(new XYChart.Data(entry.getKey(), entry.getValue()));

		for (Entry<String, Integer> entry : getUniquePairs(hashSet2).entrySet())
			series2.getData().add(new XYChart.Data(entry.getKey(), entry.getValue()));

		initFX(series1, series2);

	}

	public LinkedHashMap<String, Integer> getUniquePairs(HashSet<ClickLog> hashSet){

		LinkedHashMap<String, Integer> uniquePairs = new LinkedHashMap<String, Integer>();
		String date;

		for (ClickLog click : hashSet) {

			date = sdf.format(click.getDate());

			if (!uniquePairs.containsKey(date))
				uniquePairs.put(date, 1);
			else 
				uniquePairs.put(date, uniquePairs.get(date) + 1);

		}

		return uniquePairs;

	}

	public void showBounceChart(ArrayList<ServerLog> serverList) {

		xAxis.setLabel("Date");
		yAxis.setLabel("Number of Bounces");

		XYChart.Series series = new XYChart.Series();

		for (Entry<String, Integer> entry : getBouncePairs(serverList).entrySet())
			series.getData().add(new XYChart.Data(entry.getKey(), entry.getValue()));

		initFX(series, "Bounces over Time");

	}

	public void showBounceChart2(ArrayList<ServerLog> serverList1, ArrayList<ServerLog> serverList2) {

		xAxis.setLabel("Date");
		yAxis.setLabel("Number of Bounces");

		XYChart.Series series1 = new XYChart.Series();
		XYChart.Series series2 = new XYChart.Series();

		for (Entry<String, Integer> entry : getBouncePairs(serverList1).entrySet())
			series1.getData().add(new XYChart.Data(entry.getKey(), entry.getValue()));

		for (Entry<String, Integer> entry : getBouncePairs(serverList2).entrySet())
			series2.getData().add(new XYChart.Data(entry.getKey(), entry.getValue()));

		initFX(series1, series2);

	}

	public LinkedHashMap<String, Integer> getBouncePairs(ArrayList<ServerLog> serverList){

		LinkedHashMap<String, Integer> bouncePairs = new LinkedHashMap<String, Integer>();
		String date;

		for (ServerLog server : serverList) {

			date = sdf.format(server.getStartDate());

			if (!bouncePairs.containsKey(date))
				bouncePairs.put(date, 1);
			else
				bouncePairs.put(date, bouncePairs.get(date) + 1);

		}

		return bouncePairs;

	}

	public void showConversionChart(ArrayList<ServerLog> serverList) {

		xAxis.setLabel("Date");
		yAxis.setLabel("Number of Conversions");

		XYChart.Series series = new XYChart.Series();

		for (Entry<String, Integer> entry : getConversionPairs(serverList).entrySet())
			series.getData().add(new XYChart.Data(entry.getKey(), entry.getValue()));

		initFX(series, "Conversions over Time");

	}

	public void showConversionChart2(ArrayList<ServerLog> serverList1, ArrayList<ServerLog> serverList2) {

		xAxis.setLabel("Date");
		yAxis.setLabel("Number of Conversions");

		XYChart.Series series1 = new XYChart.Series();
		XYChart.Series series2 = new XYChart.Series();

		for (Entry<String, Integer> entry : getConversionPairs(serverList1).entrySet())
			series1.getData().add(new XYChart.Data(entry.getKey(), entry.getValue()));

		for (Entry<String, Integer> entry : getConversionPairs(serverList2).entrySet())
			series2.getData().add(new XYChart.Data(entry.getKey(), entry.getValue()));

		initFX(series1, series2);

	}

	public LinkedHashMap<String, Integer> getConversionPairs(ArrayList<ServerLog> serverList){

		LinkedHashMap<String, Integer> convertPairs = new LinkedHashMap<String, Integer>();
		String date;

		for (ServerLog server : serverList) {

			date = sdf.format(server.getStartDate());

			if (server.isConverted()) {
				if (!convertPairs.containsKey(date))
					convertPairs.put(date, 1);
				else
					convertPairs.put(date, convertPairs.get(date) + 1);			
			}

		}

		return convertPairs;

	}

	public void showCumulativeCostChart(ArrayList<ClickLog> clickList , ArrayList<ImpressionLog> impressionLogs) {

		xAxis.setLabel("Date");
		yAxis.setLabel("Cumulative Cost (pence)");

		XYChart.Series series = new XYChart.Series();

		for (Entry<String, Double> entry : getCostPairs(clickList, impressionLogs).entrySet())
			series.getData().add(new XYChart.Data(entry.getKey(), entry.getValue()));

		initFX(series, "Cumulative Cost over Time");

	}

	public void showCumulativeCostChart2(ArrayList<ClickLog> clickList1 , ArrayList<ImpressionLog> impressionLogs1,
			ArrayList<ClickLog> clickList2 , ArrayList<ImpressionLog> impressionLogs2) {

		xAxis.setLabel("Date");
		yAxis.setLabel("Cumulative Cost (pence)");

		XYChart.Series series1 = new XYChart.Series();
		XYChart.Series series2 = new XYChart.Series();

		for (Entry<String, Double> entry : getCostPairs(clickList1, impressionLogs1).entrySet())
			series1.getData().add(new XYChart.Data(entry.getKey(), entry.getValue()));

		for (Entry<String, Double> entry : getCostPairs(clickList2, impressionLogs2).entrySet())
			series2.getData().add(new XYChart.Data(entry.getKey(), entry.getValue()));

		initFX(series1, series2);

	}

	public LinkedHashMap<String, Double> getCostPairs(ArrayList<ClickLog> clickList , ArrayList<ImpressionLog> impressionLogs){

		LinkedHashMap<String, Double> costPairs = new LinkedHashMap<String, Double>();
		String date, previous = null;

		for (ClickLog click : clickList) {
			date = sdf.format(click.getDate());
			if (!costPairs.containsKey(date)) {
				if (previous != null)
					costPairs.put(date, click.getClickCost() + costPairs.get(previous));
				else 
					costPairs.put(date, click.getClickCost());
			}
			else costPairs.put(date, costPairs.get(date) + click.getClickCost());
			previous = date;
		}

		for (ImpressionLog impression : impressionLogs) {
			date = sdf.format(impression.getDate());
			if (!costPairs.containsKey(date)) {
				if (previous != null)
					costPairs.put(date, impression.getImpression() + costPairs.get(previous));
				else
					costPairs.put(date, impression.getImpression());
			}
			else costPairs.put(date, costPairs.get(date) + impression.getImpression());
			previous = date;
		}

		return costPairs;

	}

	public void showClickCostsHistogram(ArrayList<ClickLog> clickList) {

		xAxis.setLabel("Cost Division");
		yAxis.setLabel("Frequency");

		BarChart<String, Number> barChart = new BarChart<String, Number>(xAxis, yAxis);

		XYChart.Series series = new XYChart.Series();

		for (Entry<String, Double> entry : getHistogramPairs(clickList).entrySet())
			series.getData().add(new XYChart.Data(entry.getKey().concat(" - ").concat(String.valueOf(Integer.parseInt(entry.getKey()) + 1)), entry.getValue()));

		series.setName("Cost Over Cost Division");
		barChart.getData().add(series);
		barChart.setAnimated(true);
		barChart.setCursor(Cursor.CROSSHAIR);		
		barChart.setBarGap(1);
		barChart.setCategoryGap(0);

		scene = new Scene(barChart, xDim, yDim);
		scene.getStylesheets().add("chartstyle2.css");
		this.setScene(scene);

	}


	public void showClickCostsHistogram2(ArrayList<ClickLog> clickList1, ArrayList<ClickLog> clickList2) {

		xAxis.setLabel("Cost Division");
		yAxis.setLabel("Frequency");

		BarChart<String, Number> barChart = new BarChart<String, Number>(xAxis, yAxis);

		XYChart.Series series = new XYChart.Series();
		XYChart.Series series2 = new XYChart.Series();

		for (Entry<String, Double> entry : getHistogramPairs(clickList1).entrySet())
			series.getData().add(new XYChart.Data(entry.getKey().concat(" - ").concat(String.valueOf(Integer.parseInt(entry.getKey()) + 1)), entry.getValue()));

		for (Entry<String, Double> entry : getHistogramPairs(clickList2).entrySet())
			series2.getData().add(new XYChart.Data(entry.getKey().concat(" - ").concat(String.valueOf(Integer.parseInt(entry.getKey()) + 1)), entry.getValue()));

		series.setName("Series1");
		series2.setName("Series2");
		barChart.getData().addAll(series, series2);
		barChart.setAnimated(true);
		barChart.setCursor(Cursor.CROSSHAIR);		
		barChart.setBarGap(1);
		barChart.setCategoryGap(0);

		scene = new Scene(barChart, xDim, yDim);
		scene.getStylesheets().add("chartstyle2.css");
		this.setScene(scene);

	}

	public TreeMap<String, Double> getHistogramPairs(ArrayList<ClickLog> clickList){

		int modVal = 100 ;
		int temp ;

		LinkedHashMap<String, Double> costPairs = new LinkedHashMap<String, Double>();
		String date;

		for (ClickLog click : clickList) {
			temp = click.getClickCost().intValue() % 100;
			date = String.valueOf(temp);

			if (!costPairs.containsKey(date)) {
				costPairs.put(date, 1.0);
			}
			else
				costPairs.put(date, costPairs.get(date) + 1.0);

		}

		TreeMap<String,Double> costDivi = new TreeMap<String,Double>(new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				Integer a = Integer.parseInt(o1);
				Integer b = Integer.parseInt(o2);
				return a.compareTo(b);
			}
		});

		costDivi.putAll(costPairs);

		return costDivi;

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

	private class MyImpressionWorker implements Callable {

		private ArrayList<ImpressionLog> impressionList1;
		private LinkedHashMap<String, Integer> impressionPairs;

		public MyImpressionWorker(ArrayList<ImpressionLog> impressionList1) {
			this.impressionList1 = impressionList1;
			this.impressionPairs = new LinkedHashMap<String, Integer>();
		}

		public void run() {
			String date;

			for (ImpressionLog impression : impressionList1) {
				date = sdf.format(impression.getDate());
				if (!impressionPairs.containsKey(date))
					impressionPairs.put(date, 1);
				else
					impressionPairs.put(date, impressionPairs.get(date) + 1);

			}

		}

		@Override
		public Object call() throws Exception {
			String date;

			for (ImpressionLog impression : impressionList1) {
				date = sdf.format(impression.getDate());
				if (!impressionPairs.containsKey(date))
					impressionPairs.put(date, 1);
				else
					impressionPairs.put(date, impressionPairs.get(date) + 1);

			}

			return impressionPairs;
		}
	}

}
