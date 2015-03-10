import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.JFXPanel;
import javafx.event.EventHandler;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.Glow;
import javafx.scene.input.MouseEvent;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;


@SuppressWarnings("serial")
public class ChartPie extends JFXPanel {

	private int xDim = 390;
	private int yDim = 250;

	private Scene scene;
	private PieChart chart;
	private PieChart.Data selectedData;
	private Tooltip tooltip;
	private Dashboard dashboard;

	public ChartPie(Dashboard dashboard) {

		super();
		this.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.gray));
		this.dashboard = dashboard;
		this.chart = new PieChart();

	}

	public void initFX(String title){

		chart.setLegendSide(Side.LEFT);
		chart.setLabelsVisible(false);
		chart.setTitle(title);

		tooltip = new Tooltip("");


		for (final PieChart.Data data : chart.getData()) {
			Tooltip.install(data.getNode(), tooltip);
			applyMouseEvents(data);
		}

		scene = new Scene(chart, xDim, yDim);
//		scene.getStylesheets().add("chartstyle2.css");
		this.setScene(scene);

	}

	public void showGenderPie() {

		HashMap<Boolean,Long> map = dashboard.dataAnalytics.sexRatioDivision(dashboard.getImpressionLogs());

		ObservableList<PieChart.Data> pieChartData =
				FXCollections.observableArrayList();

        if(map.containsKey(true)){
            PieChart.Data d = new PieChart.Data("Male", map.get(true));
//            d.getNode().setStyle(
//                    "-fx-pie-color: #00dcff ;"
//            );
            pieChartData.add(d);
        }
        if(map.containsKey(false)){
            PieChart.Data d = new PieChart.Data("Female", map.get(false));
//            d.getNode().setStyle(
//                    "-fx-pie-color: #ff00b5 ;"
//            );
            pieChartData.add(d);
        }

		chart = new PieChart(pieChartData);

		initFX("Gender Distribution");

	}

	public void showAgeGroupPie() {

		HashMap<Integer, Long> map = dashboard.dataAnalytics.ageGroupDivision(dashboard.getImpressionLogs());

		ObservableList<PieChart.Data> pieChartData =
				FXCollections.observableArrayList();

        if(map.containsKey(0)){
            PieChart.Data d = new PieChart.Data("< 25", map.get(0));
//            d.getNode().setStyle(
//                    "-fx-pie-color: #00ccff ;"
//            );
            pieChartData.add(d);
        }
        if(map.containsKey(1)){
            PieChart.Data d = new PieChart.Data("25-34", map.get(1));
//            d.getNode().setStyle(
//                    "-fx-pie-color: #00ccff ;"
//            );
            pieChartData.add(d);
        }
        if(map.containsKey(2)){
            PieChart.Data d = new PieChart.Data("35-44", map.get(2));
            pieChartData.add(d);
        }
        if(map.containsKey(3)){
            PieChart.Data d = new PieChart.Data("45-54", map.get(3));
            pieChartData.add(d);
        }
        if(map.containsKey(4)){
            PieChart.Data d = new PieChart.Data("> 55", map.get(4));
            pieChartData.add(d);
        }
		chart = new PieChart(pieChartData);

		initFX("Age Distribution");

	}

    private void applyCustomColorSequence(
            ObservableList<PieChart.Data> pieChartData,
            String... pieColors) {
        int i = 0;
        for (PieChart.Data data : pieChartData) {
            data.getNode().setStyle(
                    "-fx-pie-color: " + pieColors[i % pieColors.length] + ";"
            );
            i++;
        }
    }

	public void showIncomePie() {

		HashMap<Integer, Long> map = dashboard.dataAnalytics.incomeGroupDivision(dashboard.getImpressionLogs());

		ObservableList<PieChart.Data> pieChartData =
				FXCollections.observableArrayList();

        if(map.containsKey(0)){
            PieChart.Data d = new PieChart.Data("Low", map.get(0));
            pieChartData.add(d);
        }
        if(map.containsKey(1)){
            PieChart.Data d = new PieChart.Data("Medium", map.get(1));
            pieChartData.add(d);
        }
        if(map.containsKey(2)) {
            PieChart.Data d = new PieChart.Data("High", map.get(2));
            pieChartData.add(d);
        }
		chart = new PieChart(pieChartData);

		initFX("Income Distribution");

	}

	public void showContextPie() {

		HashMap<String, Long> map = dashboard.dataAnalytics.contextGroupDivision(dashboard.getImpressionLogs());

		ObservableList<PieChart.Data> pieChartData =
				FXCollections.observableArrayList();

        if(map.containsKey("News")){
            PieChart.Data d = new PieChart.Data("News", map.get("News"));
            pieChartData.add(d);
        }
        if(map.containsKey("Shopping")){
            PieChart.Data d = new PieChart.Data("Shopping", map.get("Shopping"));
            pieChartData.add(d);
        }
        if(map.containsKey("Social Media")){
            PieChart.Data d = new PieChart.Data("Social Media", map.get("Social Media"));
            pieChartData.add(d);
        }
        if(map.containsKey("Blog")){
            PieChart.Data d = new PieChart.Data("Blog", map.get("Blog"));
            pieChartData.add(d);
        }
        if(map.containsKey("Hobbies")){
            PieChart.Data d = new PieChart.Data("Hobbies", map.get("Hobbies"));
            pieChartData.add(d);
        }
        if(map.containsKey("Travel")){
            PieChart.Data d = new PieChart.Data("Travel", map.get("Travel"));
            pieChartData.add(d);
        }

		chart = new PieChart(pieChartData);

		initFX("Context Distribution");

	}

	private void applyMouseEvents(final PieChart.Data data) {

		final Node node = data.getNode();

		node.setOnMouseEntered(new EventHandler<MouseEvent>() {

			public void handle(MouseEvent arg0) {
				node.setEffect(new Glow());
				String styleString = "-fx-border-color: white; -fx-border-width: 3; -fx-border-style: dashed;";
				node.setStyle(styleString);
				tooltip.setText(String.valueOf(data.getName() + "\n" + (int) data.getPieValue()));
			}
		});

		node.setOnMouseExited(new EventHandler<MouseEvent>() {

			public void handle(MouseEvent arg0) {
				node.setEffect(null);
				node.setStyle("");
			}
		});

		node.setOnMouseReleased(new EventHandler<MouseEvent>() {

			public void handle(MouseEvent mouseEvent) {
				selectedData = data;
				System.out.println("Selected data " + selectedData.toString());
			}
		});

	}

}