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
		scene.getStylesheets().add("chartstyle2.css");
		this.setScene(scene);

	}

	public void showGenderPie() {

		HashMap<Boolean,Long> map = dashboard.dataAnalytics.sexRatioDivision(dashboard.getImpressionLogs());

		ObservableList<PieChart.Data> pieChartData =
				FXCollections.observableArrayList(
						new PieChart.Data("Male", map.get(true)),
						new PieChart.Data("Female", map.get(false)));
		chart = new PieChart(pieChartData);

		initFX("Gender Distribution");

	}

	public void showAgeGroupPie() {

		HashMap<Integer, Long> map = dashboard.dataAnalytics.ageGroupDivision(dashboard.getImpressionLogs());

		ObservableList<PieChart.Data> pieChartData =
				FXCollections.observableArrayList(
						new PieChart.Data("<25", map.get(0)),
						new PieChart.Data("25-34", map.get(1)),
						new PieChart.Data("35-44", map.get(2)),
						new PieChart.Data("45-54", map.get(3)),
						new PieChart.Data(">55", map.get(4)));
		chart = new PieChart(pieChartData);

		initFX("Age Distribution");

	}

	public void showIncomePie() {

		HashMap<Integer, Long> map = dashboard.dataAnalytics.incomeGroupDivision(dashboard.getImpressionLogs());

		ObservableList<PieChart.Data> pieChartData =
				FXCollections.observableArrayList(
						new PieChart.Data("Low", map.get(0)),
						new PieChart.Data("Medium", map.get(1)),
						new PieChart.Data("High", map.get(2)));
		chart = new PieChart(pieChartData);

		initFX("Income Distribution");

	}

	public void showContextPie() {

		HashMap<String, Long> map = dashboard.dataAnalytics.contextGroupDivision(dashboard.getImpressionLogs());

		ObservableList<PieChart.Data> pieChartData =
				FXCollections.observableArrayList(
						new PieChart.Data("News", map.get("News")),
						new PieChart.Data("Shopping", map.get("Shopping")),
						new PieChart.Data("Social Media", map.get("Social Media")),
						new PieChart.Data("Blog", map.get("Blog")));
		chart = new PieChart(pieChartData);

		initFX("Gender Distribution");

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