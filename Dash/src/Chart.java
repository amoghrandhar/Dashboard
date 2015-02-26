import javafx.embed.swing.JFXPanel;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Tooltip;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map.Entry;


@SuppressWarnings("serial")
public class Chart extends JFXPanel{
	
	private int xDim = 820;
	private int yDim = 370;
	private Scene scene;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	public Chart() {
		super();
		this.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.gray));
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
		
		LinkedHashMap<String,Integer> impressionPairs = new LinkedHashMap<String,Integer>();
		String date;
		
		for (ImpressionLog impression : impressionList) {
			date = sdf.format(impression.getDate());
			if (!impressionPairs.containsKey(date)) {
				impressionPairs.put(date, 1);
			} else {
				impressionPairs.put(date, impressionPairs.get(date) + 1);
			}
		}
		
		LineChart<String,Number> lineChart =
				new LineChart<String,Number>(xAxis,yAxis);

		XYChart.Series series = new XYChart.Series();
		series.setName("Impressions Over Time");

		for (Entry<String,Integer> entry : impressionPairs.entrySet()) {
			series.getData().add(new XYChart.Data(entry.getKey(), entry.getValue()));
		}
		lineChart.getData().add(series);



        lineChart.setCursor(Cursor.CROSSHAIR);
		scene = new Scene(lineChart, xDim, yDim);
		scene.getStylesheets().add("chartstyle.css");
		this.setScene(scene);
        displayOnHover(lineChart);


    }

    private void displayOnHover(LineChart<String, Number> lineChart) {
        /**
         * Browsing through the Data and applying ToolTip
         * as well as the class on hover
         */
        for (XYChart.Series<String, Number> s : lineChart.getData()) {
            for (final XYChart.Data<String, Number> d : s.getData()) {
                Tooltip.install(d.getNode(), new Tooltip(
                        d.getXValue().toString() + "\n" +
                                "Number Of Events : " + d.getYValue()));

                //Adding class on hover
                d.getNode().setOnMouseEntered(new EventHandler<javafx.event.Event>() {

                    @Override
                    public void handle(javafx.event.Event event) {
                        d.getNode().getStyleClass().add("onHover");
                    }

                });

                //Removing class on exit
                d.getNode().setOnMouseExited(new EventHandler<javafx.event.Event>() {

                    @Override
                    public void handle(javafx.event.Event event) {
                        d.getNode().getStyleClass().remove("onHover");
                    }

                });
            }
        }
    }

    public void showUniqueChart(HashSet<ClickLog> hashSet) {
		CategoryAxis xAxis = new CategoryAxis();
		NumberAxis yAxis = new NumberAxis();
		xAxis.setLabel("Date");
		yAxis.setLabel("Number of Unique Clicks");


		LinkedHashMap<String,Integer> uniquePairs = new LinkedHashMap<String,Integer>();
		String date;

		for (ClickLog click : hashSet) {
			date = sdf.format(click.getDate());
			if (!uniquePairs.containsKey(date)) {
				uniquePairs.put(date, 1);
			} else {
				uniquePairs.put(date, uniquePairs.get(date) + 1);
			}
		}

		LineChart<String,Number> lineChart =
				new LineChart<String,Number>(xAxis,yAxis);

		XYChart.Series series = new XYChart.Series();
		series.setName("Unique Clicks Over Time");

		for (Entry<String,Integer> entry : uniquePairs.entrySet()) {
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
		
		
		LinkedHashMap<String,Integer> clickPairs = new LinkedHashMap<String,Integer>();
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
		
		
		LineChart<String,Number> lineChart = 
				new LineChart<String,Number>(xAxis,yAxis);

		XYChart.Series series = new XYChart.Series();
		series.setName("Clicks Over Time");

		for (Entry<String,Integer> entry : clickPairs.entrySet()) {
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
    
    public void showCost(ArrayList<ClickLog> clickList) {
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
        
        LineChart<String, Number> lineChart =
                new LineChart<String, Number>(xAxis, yAxis);

        XYChart.Series series = new XYChart.Series();
        series.setName("Cost Over Time");

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
}
