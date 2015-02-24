import java.awt.*;
import javafx.application.Platform;
import javafx.embed.swing.*;
import javafx.scene.*;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javax.swing.BorderFactory;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;


public class Content extends JPanel{

	JPanel graphPanel, metricsPanel;

	private String metrics[][] = {
			{ "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0" },
	};

	public Content() {

		init();

	}

	public void init(){

		this.setLayout(new BorderLayout());

		graphPanel = new JPanel();
		graphPanel.setLayout(new GridBagLayout());

		metricsPanel = new JPanel();
		metricsPanel.setLayout(new GridBagLayout());

		final Chart chart = new Chart();

		Platform.runLater(new Runnable() {
			public void run() {
				chart.initFX();
			}
		});

		graphPanel.add(chart);

		String columnNames[] = { "Clicks", "Impressions", "Uniques", "Bounces", "Conversions", 
				"Total Cost", "CTR", "CPA", "CPC", "CPM", "Bounce Rate" };

		// Create a new table instance
		JTable table = new JTable( metrics, columnNames );
		
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
	
	public void setMetrics(String dataValues[][]){
		
		metrics = dataValues;
		
	}
}
