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
import java.util.Map.Entry;

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

	public void showImpressionsChart(ArrayList<ImpressionLog> impressionList) {

		xAxis.setLabel("Date");
		yAxis.setLabel("Number of Impressions");

		LinkedHashMap<String, Integer> impressionPairs = new LinkedHashMap<String, Integer>();
		String date;

		for (ImpressionLog impression : impressionList) {
			
			date = sdf.format(impression.getDate());
			
			if (!impressionPairs.containsKey(date))
				impressionPairs.put(date, 1);
			else
				impressionPairs.put(date, impressionPairs.get(date) + 1);
			
		}

		XYChart.Series series = new XYChart.Series();

		for (Entry<String, Integer> entry : impressionPairs.entrySet())
			series.getData().add(new XYChart.Data(entry.getKey(), entry.getValue()));

		initFX(series, "Impressions over Time");

	}
	
	public void showClicksChart(ArrayList<ClickLog> clickList) {

		xAxis.setLabel("Date");
		yAxis.setLabel("Number of Clicks");

		LinkedHashMap<String, Integer> clickPairs = new LinkedHashMap<String, Integer>();
		String date;

		/* 'Counts' number of clicks per day */
		for (ClickLog click : clickList) {
			
			date = sdf.format(click.getDate());
			
			if (!clickPairs.containsKey(date))
				clickPairs.put(date, 1);
			else
				clickPairs.put(date, clickPairs.get(date) + 1);

		}

		XYChart.Series series = new XYChart.Series();

		for (Entry<String, Integer> entry : clickPairs.entrySet())
			series.getData().add(new XYChart.Data(entry.getKey(), entry.getValue()));

		initFX(series, "Clicks over Time");

	}

	public void showUniqueChart(HashSet<ClickLog> hashSet) {

		xAxis.setLabel("Date");
		yAxis.setLabel("Number of Unique Clicks");

		LinkedHashMap<String, Integer> uniquePairs = new LinkedHashMap<String, Integer>();
		String date;

		for (ClickLog click : hashSet) {
			
			date = sdf.format(click.getDate());
			
			if (!uniquePairs.containsKey(date))
				uniquePairs.put(date, 1);
			else 
				uniquePairs.put(date, uniquePairs.get(date) + 1);
			
		}

		XYChart.Series series = new XYChart.Series();

		for (Entry<String, Integer> entry : uniquePairs.entrySet())
			series.getData().add(new XYChart.Data(entry.getKey(), entry.getValue()));

		initFX(series, "Unique Clicks over Time");

	}

	public void showBounceChart(ArrayList<ServerLog> serverList) {

		xAxis.setLabel("Date");
		yAxis.setLabel("Number of Bounces");

		LinkedHashMap<String, Integer> bouncePairs = new LinkedHashMap<String, Integer>();
		String date;

		for (ServerLog server : serverList) {

			date = sdf.format(server.getStartDate());

			if (!bouncePairs.containsKey(date))
				bouncePairs.put(date, 1);
			else
				bouncePairs.put(date, bouncePairs.get(date) + 1);

		}

		XYChart.Series series = new XYChart.Series();
		series.setName("Bounces Over Time");

		for (Entry<String, Integer> entry : bouncePairs.entrySet())
			series.getData().add(new XYChart.Data(entry.getKey(), entry.getValue()));

		initFX(series, "Bounces over Time");

	}

	public void showConversionChart(ArrayList<ServerLog> serverList) {

		xAxis.setLabel("Date");
		yAxis.setLabel("Number of Conversions");

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

		XYChart.Series series = new XYChart.Series();

		for (Entry<String, Integer> entry : convertPairs.entrySet())
			series.getData().add(new XYChart.Data(entry.getKey(), entry.getValue()));

		initFX(series, "Conversions over Time");

	}

	public void showCumulativeCostChart(ArrayList<ClickLog> clickList , ArrayList<ImpressionLog> impressionLogs) {

		xAxis.setLabel("Date");
		yAxis.setLabel("Cumulative Cost (pence)");

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

		XYChart.Series series = new XYChart.Series();

		for (Entry<String, Double> entry : costPairs.entrySet())
			series.getData().add(new XYChart.Data(entry.getKey(), entry.getValue()));

		initFX(series, "Cumulative Cost over Time");

	}

	public void showClickCostsHistogram(ArrayList<ClickLog> clickList) {

		xAxis.setLabel("Cost Division");
       // xAxis.setLabel("Date");
		yAxis.setLabel("Cost (pence)");

        int modVal = 100 ;
        int temp ;

		LinkedHashMap<String, Double> costPairs = new LinkedHashMap<String, Double>();
		String date;

//		for (ClickLog click : clickList) {
//			date = sdf.format(click.getDate());
//			if (!costPairs.containsKey(date))
//				costPairs.put(date, click.getClickCost());
//			else
//				costPairs.put(date, costPairs.get(date) + click.getClickCost());
//
//		}

        for (ClickLog click : clickList) {
            temp = click.getClickCost().intValue() % 100;
            date = String.valueOf(temp);

            if (!costPairs.containsKey(date)) {
                System.out.println(date);
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


		BarChart<String, Number> barChart = new BarChart<String, Number>(xAxis, yAxis);

		XYChart.Series series = new XYChart.Series();

		for (Entry<String, Double> entry : costDivi.entrySet())
			series.getData().add(new XYChart.Data(entry.getKey().concat(" - ").concat(String.valueOf(Integer.parseInt(entry.getKey()) + 1)), entry.getValue()));

//		series.setName("Cost Over Time");
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
