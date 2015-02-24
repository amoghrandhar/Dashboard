import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;

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

	JPanel graphPanel, metricsPanel, headerPanel;
	
	JLabel clicksValueLabel, impressionsValueLabel, totalCostValueLabel;
	
	Dashboard dashboard;
	
	Chart chart;
	JComboBox<String> chartCombo;

	public Content(Dashboard d) {

		dashboard = d;
		init();

	}

	public void init(){

		this.setLayout(new BorderLayout());

		headerPanel = new JPanel();
		headerPanel.setLayout(new GridBagLayout());
		
		graphPanel = new JPanel();
		graphPanel.setLayout(new GridBagLayout());

		metricsPanel = new JPanel();
		metricsPanel.setLayout(new GridBagLayout());
		
		// ######### GridBagLayout Constraints #########
		
		GridBagConstraints l1 = new GridBagConstraints();
		GridBagConstraints l2 = new GridBagConstraints();
		GridBagConstraints l3 = new GridBagConstraints();
		GridBagConstraints v1 = new GridBagConstraints();
		GridBagConstraints v2 = new GridBagConstraints();
		GridBagConstraints v3 = new GridBagConstraints();
		GridBagConstraints graphChoiceBoxC = new GridBagConstraints();
		GridBagConstraints graphChoiceLabelC = new GridBagConstraints();
		
		v1.gridx = 0;
		v1.gridy = 0;
		v1.anchor = GridBagConstraints.LINE_START;
		v1.insets = new Insets(0, 20, 0, 20);
		
		v2.gridx = 1;
		v2.gridy = 0;
		v2.anchor = GridBagConstraints.LINE_START;
		v2.insets = new Insets(0, 20, 0, 20);
		
		v3.gridx = 2;
		v3.gridy = 0;
		v3.anchor = GridBagConstraints.LINE_START;
		v3.insets = new Insets(0, 20, 0, 20);
		
		l1.gridx = 0;
		l1.gridy = 1;
		l1.anchor = GridBagConstraints.LINE_START;
		l1.insets = new Insets(0, 20, 0, 20);
		
		l2.gridx = 1;
		l2.gridy = 1;
		l2.anchor = GridBagConstraints.LINE_START;
		l2.insets = new Insets(0, 20, 0, 20);
		
		l3.gridx = 2;
		l3.gridy = 1;
		l3.anchor = GridBagConstraints.LINE_START;
		l3.insets = new Insets(0, 20, 0, 20);
		
		graphChoiceBoxC.gridx = 3;
		graphChoiceBoxC.gridy = 1;
		graphChoiceBoxC.anchor = GridBagConstraints.LINE_START;
		graphChoiceBoxC.insets = new Insets(-4, 300, 0, 20);
		 
		graphChoiceLabelC.gridx = 3;
		graphChoiceLabelC.gridy = 0;
		graphChoiceLabelC.anchor = GridBagConstraints.LINE_START;
		graphChoiceLabelC.insets = new Insets(0, 304, 0, 20);
		
		// ######### Panels #########
		
		Font valueFont = new Font("", Font.BOLD, 20);
		Font labelFont = new Font("", Font.PLAIN, 12);
		
		clicksValueLabel = new JLabel("0");
		impressionsValueLabel = new JLabel("0");
		totalCostValueLabel = new JLabel("£ 0");
		
		clicksValueLabel.setFont(valueFont);
		impressionsValueLabel.setFont(valueFont);
		totalCostValueLabel.setFont(valueFont);
		
		JLabel clicksLabel = new JLabel("Clicks");
		JLabel impressionsLabel = new JLabel("Impressions");
		JLabel totalCostLabel = new JLabel("Total Cost");
		
		clicksLabel.setFont(labelFont);
		impressionsLabel.setFont(labelFont);
		totalCostLabel.setFont(labelFont);
		
		headerPanel.add(clicksValueLabel, v1);
		headerPanel.add(impressionsValueLabel, v2);
		headerPanel.add(totalCostValueLabel, v3);
		
		headerPanel.add(clicksLabel, l1);
		headerPanel.add(impressionsLabel, l2);
		headerPanel.add(totalCostLabel, l3);
		
		String[] graphChoices = { "Clicks", "Impressions", "Uniques", "Bounces", "Conversions", 
				"Total Cost", "CTR", "CPA", "CPC", "CPM", "Bounce Rate" };
		
		JComboBox graphChoiceBox = new JComboBox(graphChoices);
		graphChoiceBox.setPrototypeDisplayValue("XXXXXXXXXX");
		
		JLabel graphChoiceLabel = new JLabel("Display graph");
		graphChoiceLabel.setFont(labelFont);
		
		headerPanel.add(graphChoiceBox, graphChoiceBoxC);
		headerPanel.add(graphChoiceLabel, graphChoiceLabelC);
		
		// ######### Graph Panel #########

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

		// ######### Header Panel #########
		
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
		
		// ######### Panels #########

		headerPanel.setPreferredSize(new Dimension(900, 100));
		headerPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.gray));
		
		metricsPanel.add(tablePanel);
		metricsPanel.setPreferredSize(new Dimension(900, 160));
		metricsPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.gray));

		this.add(headerPanel, BorderLayout.PAGE_START);
		this.add(graphPanel, BorderLayout.CENTER);
		this.add(metricsPanel, BorderLayout.PAGE_END);

	}
	
	public void setMetrics(int rowIndex, String[] rowValues){
		
		tableModel.updateRow(0, rowValues);
		
	}
	
	public void setHeaderMetrics(String clicks, String impressions, String cost){
		
		this.clicksValueLabel.setText(clicks);
		this.impressionsValueLabel.setText(impressions);
		this.totalCostValueLabel.setText("£" + cost);
		
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
	
	public String round(double value, int scale){
		
		return new BigDecimal(String.valueOf(value)).setScale(scale, BigDecimal.ROUND_HALF_UP).toString();
		
	}
	
}
