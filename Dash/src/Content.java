import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javafx.application.Platform;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

public class Content extends JPanel{
	
	SimpleTableModel tableModel;

	JPanel graphPanel, metricsPanel;
	
	Dashboard dashboard;
	
	Chart chart;
	JComboBox<String> chartCombo;

	public Content(Dashboard d) {

		dashboard = d;
		init();

	}

	public void init(){

		this.setLayout(new BorderLayout());

		graphPanel = new JPanel();
		graphPanel.setLayout(new GridBagLayout());

		metricsPanel = new JPanel();
		metricsPanel.setLayout(new GridBagLayout());

		chart = new Chart();

		Platform.runLater(new Runnable() {
			public void run() {
				chart.initFX();
			}
		});
		
		String[] chartList = {"Clicks", "Impressions", "Uniques", "Bounces", "Conversions"};
		
		chartCombo = new JComboBox<String>(chartList);
		chartCombo.setSelectedIndex(0);
		chartCombo.setEnabled(false);
		chartCombo.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				JComboBox<String> cb = (JComboBox<String>) e.getSource();
				int item = cb.getSelectedIndex();
				switch (item) {
					case 1 : chart.showImpressionsChart(dashboard.getImpressionLogs());
					break;
					default : chart.showClicksChart(dashboard.getClickLogs());
					break;
				}
			}
		});	
		
		graphPanel.add(chart);
		graphPanel.add(chartCombo);

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
	
	public void defaultChart() {
		chartCombo.setEnabled(true);
		chart.showClicksChart(dashboard.getClickLogs());
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
