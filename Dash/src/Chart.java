import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map.Entry;


@SuppressWarnings("serial")
public class Chart extends JFXPanel {

    private Scene scene;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public Chart() {
        super();
    }

    public void initFX() {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Month");

        final LineChart<String, Number> lineChart =
                new LineChart<String, Number>(xAxis, yAxis);

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

    public void showImpressionsChart(ArrayList<ImpressionLog> impressionList) {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Date");

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

        XYChart.Series series = new XYChart.Series();
        series.setName("Impressions Over Time");

        for (Entry<String, Integer> entry : impressionPairs.entrySet()) {
            series.getData().add(new XYChart.Data(entry.getKey(), entry.getValue()));
        }
        lineChart.getData().add(series);

        scene = new Scene(lineChart, 600, 300);
        this.setScene(scene);

    }

    public void showUniqueChart(HashSet<ClickLog> hashSet) {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Date");


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

        scene = new Scene(lineChart, 600, 300);
        this.setScene(scene);
    }

    public void showClicksChart(ArrayList<ClickLog> clickList) {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Date");


        LinkedHashMap<String, Integer> clickPairs = new LinkedHashMap<String, Integer>();
        String date;

		
		/* 'Counts' number of clicks per day */
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

        scene = new Scene(lineChart, 600, 300);
        this.setScene(scene);
    }

    public void showBounceChart(ArrayList<ServerLog> serverList, int bounce) {
    	CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Date");
        
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

        scene = new Scene(lineChart, 600, 300);
        this.setScene(scene);
    }
    
    public void showConversionChart(ArrayList<ServerLog> serverList) {
    	CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Date");
        
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

        scene = new Scene(lineChart, 600, 300);
        this.setScene(scene);
    }
    
    public void showCumulativeCost(ArrayList<ClickLog> clickList) {
    	CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Date");
        
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
        series.setName("Cost Over Time");

        for (Entry<String, Double> entry : costPairs.entrySet()) {
            series.getData().add(new XYChart.Data(entry.getKey(), entry.getValue()));
        }
        lineChart.getData().add(series);

        scene = new Scene(lineChart, 600, 300);
        this.setScene(scene);
    }
}
