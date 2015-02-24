import java.awt.*;

import javafx.application.Platform;
import javafx.embed.swing.*;
import javafx.scene.*;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;


public class Content extends JPanel{
	
	SimpleTableModel tableModel;

	JPanel graphPanel, metricsPanel;

	public Content() {

		init();

	}

	public void init(){

		this.setLayout(new BorderLayout());

		graphPanel = new JPanel();
		graphPanel.setLayout(new GridBagLayout());

		metricsPanel = new JPanel();
		metricsPanel.setLayout(new GridBagLayout());

		final JFXPanel fxPanel = new JFXPanel();

		Platform.runLater(new Runnable() {
			public void run() {
				initFX(fxPanel);
			}
		});

		graphPanel.add(fxPanel);

		// Create a new table instance
		
		tableModel = new SimpleTableModel();
		
		JTable table = new JTable( tableModel );
		
		TableCellRenderer rendererFromHeader = table.getTableHeader().getDefaultRenderer();
		JLabel headerLabel = (JLabel) rendererFromHeader;
		headerLabel.setHorizontalAlignment(JLabel.CENTER);

		table.setRowHeight(40);
		table.getColumnModel().getColumn(0).setPreferredWidth(60);
		table.getColumnModel().getColumn(1).setPreferredWidth(85);
		table.getColumnModel().getColumn(2).setPreferredWidth(70);
		table.getColumnModel().getColumn(3).setPreferredWidth(70);
		table.getColumnModel().getColumn(4).setPreferredWidth(85);
		table.getColumnModel().getColumn(5).setPreferredWidth(80);
		table.getColumnModel().getColumn(6).setPreferredWidth(60);
		table.getColumnModel().getColumn(7).setPreferredWidth(60);
		table.getColumnModel().getColumn(8).setPreferredWidth(60);
		table.getColumnModel().getColumn(9).setPreferredWidth(60);
		table.getColumnModel().getColumn(10).setPreferredWidth(80);

		table.setDefaultRenderer(Object.class, new TableCellRenderer(){

			private DefaultTableCellRenderer DEFAULT_RENDERER = new DefaultTableCellRenderer();

			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

				Component c = DEFAULT_RENDERER.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

				if (c instanceof JLabel) {
					((JLabel) c).setHorizontalAlignment(JLabel.CENTER);
				}

				if (column%2 == 0){
					c.setBackground(Color.WHITE);
				}

				if(column%2 != 0){
					c.setBackground(Color.decode("#f5f5f5"));
				}

				return c;

			}

		});

		table.setEnabled(false);		
		table.setFillsViewportHeight(true);
		table.setPreferredScrollableViewportSize(table.getPreferredSize());

		JScrollPane scrollPane = new JScrollPane( table );

		table.getTableHeader().setPreferredSize(new Dimension(scrollPane.getWidth(), 30));

		JPanel tablePanel = new JPanel();
		tablePanel.add(scrollPane);

		metricsPanel.add(tablePanel);
		metricsPanel.setPreferredSize(new Dimension(900, 160));
		metricsPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.gray));

		this.add(graphPanel, BorderLayout.PAGE_START);
		this.add(metricsPanel, BorderLayout.PAGE_END);

	}
	
	public void setMetrics(int rowIndex, String[] rowValues){
		
		tableModel.updateRow(0, rowValues);
		
	}

	private static void initFX(JFXPanel fxPanel) {
		// This method is invoked on the JavaFX thread
		Scene scene = createScene();
		fxPanel.setScene(scene);
	}

	public static Scene createScene() {

		final CategoryAxis xAxis = new CategoryAxis();
		final NumberAxis yAxis = new NumberAxis();
		xAxis.setLabel("Month");       

		final LineChart<String,Number> lineChart = 
				new LineChart<String,Number>(xAxis,yAxis);

		lineChart.setTitle("Stock Monitoring, 2010");

		XYChart.Series series = new XYChart.Series();
		series.setName("My portfolio");

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

		Scene scene  = new Scene(lineChart,600,300);

		return scene;
	}

}

class SimpleTableModel extends AbstractTableModel {
	
	private String[][] rowData = {
			{ "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0" },
	};
	
	private String[] columnNames = { "Clicks", "Impressions", "Uniques", "Bounces", "Conversions", 
			"Total Cost", "CTR", "CPA", "CPC", "CPM", "Bounce Rate" };

	public String getColumnName(int col) {
		
		return columnNames[col].toString();
		
	}
	public int getRowCount() {
		
		return rowData.length;
		
	}
	
	public int getColumnCount() {
		
		return columnNames.length;
		
	}
	
	public Object getValueAt(int row, int col) {
		
		return rowData[row][col];
		
	}
	
	public boolean isCellEditable(int row, int col){
		
		return true;
		
	}
	
	public void setValueAt(String value, int row, int col) {
		
		rowData[row][col] = value;
		fireTableCellUpdated(row, col);
		
	}
	
	public void updateRow(int index, String[] values){
		
        for (int i = 0 ; i < values.length ; i++)
            setValueAt(values[i],index,i);

    }
	
}
