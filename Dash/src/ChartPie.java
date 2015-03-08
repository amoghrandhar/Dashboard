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

    private PieChart.Data selectedData;
    private Tooltip tooltip;
    
    private Dashboard dashboard;

    public ChartPie(Dashboard dashboard) {
        super();
        this.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.gray));
        this.dashboard = dashboard;
    }

    public void initFX() {
    	
    	HashMap<Boolean,Long> sexMap = dashboard.dataAnalytics.sexRatioDivision(dashboard.getImpressionLogs());
    	
        ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList(
                        new PieChart.Data("Male", sexMap.get(true)),
                        new PieChart.Data("Female", sexMap.get(false)));
        final PieChart chart = new PieChart(pieChartData);

        chart.setLegendSide(Side.LEFT);
        chart.setLabelsVisible(false);

        tooltip = new Tooltip("");


        for (final PieChart.Data data : chart.getData()) {
            Tooltip.install(data.getNode(), tooltip);
            applyMouseEvents(data);
        }

        scene = new Scene(chart, xDim, yDim);
        scene.getStylesheets().add("chartstyle2.css");
        this.setScene(scene);

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