import javafx.application.Application;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.Date;

public class DateLineChartTest extends Application {
    Path path;//Add path for freehand
    Rectangle rect;
    SimpleDoubleProperty rectinitX = new SimpleDoubleProperty();
    SimpleDoubleProperty rectinitY = new SimpleDoubleProperty();
    SimpleDoubleProperty rectX = new SimpleDoubleProperty();
    SimpleDoubleProperty rectY = new SimpleDoubleProperty();
    LineChart lineChart2 =null;
    Date initXLowerBound =new Date(2013-1900,4,1);
    Date  initXUpperBound =new Date(2013-1900,4,30);
    double initYLowerBound , initYUpperBound;
    DateAxis xAxis;
    NumberAxis yAxis;

    @Override
    public void start(Stage stage) {

        stage.setTitle("Lines plot");
        StackPane Mainpane = new StackPane();
        Mainpane.getChildren().add(addMainpane());
        Scene scene = new Scene(Mainpane, 800, 600);
        stage.setScene(scene);
        path = new Path();
        path.setStrokeWidth(1);
        path.setStroke(Color.BLACK);
        stage.show();
    }
    private StackPane addMainpane(){

        StackPane Mainpane = new StackPane();

        final DateAxis xAxis = new DateAxis();
        final NumberAxis yAxis = new NumberAxis(0, 13, 1);


        final LineChart lineChart = new LineChart(xAxis, yAxis);

        XYChart.Series series1 = new XYChart.Series();
        series1.getData().add(new XYChart.Data(new Date(2013-1900,4,1), 1));
        series1.getData().add(new XYChart.Data(new Date(2013-1900,4,2), 2));
        series1.getData().add(new XYChart.Data(new Date(2013-1900,4,3), 3));
        series1.getData().add(new XYChart.Data(new Date(2013-1900,4,4), 4));
        series1.getData().add(new XYChart.Data(new Date(2013-1900,4,5), 5));
        series1.getData().add(new XYChart.Data(new Date(2013-1900,4,6), 6));
        series1.getData().add(new XYChart.Data(new Date(2013-1900,4,7), 7));
        series1.getData().add(new XYChart.Data(new Date(2013-1900,4,8), 8));
        series1.getData().add(new XYChart.Data(new Date(2013-1900,4,9), 9));
        series1.getData().add(new XYChart.Data(new Date(2013-1900,4,10), 10));
        series1.getData().add(new XYChart.Data(new Date(2013-1900,4,11), 11));
        series1.getData().add(new XYChart.Data(new Date(2013-1900,4,12), 12));
        series1.getData().add(new XYChart.Data(new Date(2013-1900,4,13), 1));
        series1.getData().add(new XYChart.Data(new Date(2013-1900,4,14), 2));
        series1.getData().add(new XYChart.Data(new Date(2013-1900,4,15), 3));
        series1.getData().add(new XYChart.Data(new Date(2013-1900,4,16), 4));
        series1.getData().add(new XYChart.Data(new Date(2013-1900,4,17), 5));
        series1.getData().add(new XYChart.Data(new Date(2013-1900,4,18), 6));
        series1.getData().add(new XYChart.Data(new Date(2013-1900,4,19), 7));
        series1.getData().add(new XYChart.Data(new Date(2013-1900,4,20), 8));
        series1.getData().add(new XYChart.Data(new Date(2013-1900,4,21), 9));
        series1.getData().add(new XYChart.Data(new Date(2013-1900,4,22), 10));
        series1.getData().add(new XYChart.Data(new Date(2013-1900,4,23), 11));
        series1.getData().add(new XYChart.Data(new Date(2013-1900,4,24), 12));
        series1.getData().add(new XYChart.Data(new Date(2013-1900,4,25), 1));
        series1.getData().add(new XYChart.Data(new Date(2013-1900,4,26), 2));
        series1.getData().add(new XYChart.Data(new Date(2013-1900,4,27), 3));
        series1.getData().add(new XYChart.Data(new Date(2013-1900,4,28), 4));
        series1.getData().add(new XYChart.Data(new Date(2013-1900,4,29), 5));
        series1.getData().add(new XYChart.Data(new Date(2013-1900,4,30), 6));


        initXLowerBound = xAxis.getLowerBound();
        initXUpperBound = xAxis.getUpperBound();
        initYLowerBound = ((NumberAxis) lineChart.getYAxis()).getLowerBound();
        initYUpperBound = ((NumberAxis) lineChart.getYAxis()).getUpperBound();
        System.out.println("initXLowerBound:"+initXLowerBound);

        BorderPane pane;
        pane = new BorderPane();
        pane.setCenter(addpane(lineChart));
        Mainpane.getChildren().add(pane);
        lineChart.getData().addAll(series1);
        lineChart2 = lineChart;

        pane.setOnMouseClicked(mouseHandler);
        pane.setOnMouseDragged(mouseHandler);
        pane.setOnMouseEntered(mouseHandler);
        pane.setOnMouseExited(mouseHandler);
        pane.setOnMouseMoved(mouseHandler);
        pane.setOnMousePressed(mouseHandler);
        pane.setOnMouseReleased(mouseHandler);

        rect = new Rectangle();
        rect.setFill(Color.web("blue", 0.1));
        rect.setStroke(Color.BLUE);
        rect.setStrokeDashOffset(50);

        rect.widthProperty().bind(rectX.subtract(rectinitX));
        rect.heightProperty().bind(rectY.subtract(rectinitY));
        pane.getChildren().add(rect);

        return Mainpane;
    }
    private StackPane addpane(LineChart lineChart){

        StackPane Subpane = new StackPane();
        Subpane.getChildren().add(lineChart);
        return Subpane;
    }

    EventHandler<MouseEvent> mouseHandler = new EventHandler<MouseEvent>() {

        @Override
        public void handle(MouseEvent mouseEvent) {
            if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                if (mouseEvent.getEventType() == MouseEvent.MOUSE_PRESSED) {
                    rect.setX(mouseEvent.getX());
                    rect.setY(mouseEvent.getY());
                    rectinitX.set(mouseEvent.getX());
                    rectinitY.set(mouseEvent.getY());
                    System.out.println("rect:"+rect);
                } else if (mouseEvent.getEventType() == MouseEvent.MOUSE_DRAGGED) {
                    rectX.set(mouseEvent.getX());
                    rectY.set(mouseEvent.getY());
                } else if (mouseEvent.getEventType() == MouseEvent.MOUSE_RELEASED) {

                    if ((rectinitX.get() >= rectX.get()) && (rectinitY.get() >= rectY.get())) {
                        //Condizioni Iniziali
                        LineChart lineChart = lineChart2;

                        xAxis.setLowerBound(initXLowerBound);
                        xAxis.setUpperBound(initXUpperBound);

                        ((NumberAxis) lineChart.getYAxis()).setLowerBound(initYLowerBound);
                        ((NumberAxis) lineChart.getYAxis()).setUpperBound(initYUpperBound);



                    } else {
                        //Zoom In

                        double Tgap = 0;
                        double newLowerBound, newUpperBound, axisShift;
                        double xScaleFactor, yScaleFactor;
                        double xaxisShift, yaxisShift;
                        LineChart  lineChart = lineChart2;

                        // Zoom in Y-axis by changing bound range.
                        NumberAxis yAxis = (NumberAxis) lineChart.getYAxis();
                        Tgap = yAxis.getHeight()/(yAxis.getUpperBound() - yAxis.getLowerBound());
                        axisShift = getSceneShiftY(yAxis);
                        yaxisShift = axisShift;
                        System.out.println("yAxisUPPER:"+yAxis.getUpperBound()+"yAxisLOWER:"+ yAxis.getLowerBound());
                        newUpperBound = yAxis.getUpperBound() - ((rectinitY.get() - axisShift) / Tgap);
                        newLowerBound = yAxis.getUpperBound() - (( rectY.get() - axisShift) / Tgap);

                        if (newUpperBound > yAxis.getUpperBound())
                            newUpperBound = yAxis.getUpperBound();

                        yScaleFactor = (yAxis.getUpperBound() - yAxis.getLowerBound())/(newUpperBound - newLowerBound);
                        yAxis.setLowerBound(newLowerBound);
                        yAxis.setUpperBound(newUpperBound);

                        XYChart.Series series1 = (Series) lineChart.getData().get(0);
                        if (!series1.getData().isEmpty()) {
                            series1.getData().remove(0);
                            series1.getData().remove(series1.getData().size() - 2);
                        }

                        DateAxis xAxis = (DateAxis) lineChart.getXAxis();

                    }
                    // Hide the rectangle
                    rectX.set(0);
                    rectY.set(0);
                }
            }// end if (mouseEvent.getButton() == MouseButton.PRIMARY)

        }
    };

    private static double getSceneShiftX(Node node) {
        double shift = 0;
        do {
            shift += node.getLayoutX();
            node = node.getParent();
        } while (node != null);
        return shift;
    }

    private static double getSceneShiftY(Node node) {
        double shift = 0;
        do {
            shift += node.getLayoutY();
            node = node.getParent();
        } while (node != null);
        return shift;
    }
    public static void main(String[] args) {
        launch(args);
    }

}