import javafx.application.Platform;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;

public class Content extends JPanel{
	
	SimpleTableModel tableModel;

	JPanel graphPanel, metricsPanel, headerPanel;
	
	JLabel clicksValueLabel, impressionsValueLabel, totalCostValueLabel;
	
	Dashboard dashboard;
	
	Chart chart;
	JComboBox<String> graphChoiceBox;

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
		
		graphChoiceBox = new JComboBox(graphChoices);
		graphChoiceBox.setPrototypeDisplayValue("XXXXXXXXXXXX");
		graphChoiceBox.setEnabled(false);		
		graphChoiceBox.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				JComboBox<String> cb = (JComboBox) e.getSource();
                final int item = cb.getSelectedIndex();
                Platform.runLater(new Runnable() {
                    public void run() {
                        switch (item) {
                            case 1:
                                chart.showImpressionsChart(dashboard.getImpressionLogs());
                                break;
                            case 2:
                            	chart.showUniqueChart(dashboard.dataAnalytics.uniqueClickSet(dashboard.getClickLogs()));
                            	break;
                            case 3:
                            	//TODO Get chosen bounce threshold from filters
                            	chart.showBounceChart(dashboard.getServerLogs(), 5); // 5 = dummy value
                            	break;
                            case 4:
                            	chart.showConversionChart(dashboard.getServerLogs());
                            	break;
                            case 5:
                            	chart.showCumulativeCost(dashboard.getClickLogs());
                            	break;
                            default:
                                chart.showClicksChart(dashboard.getClickLogs());
                                break;
                        }
                    }
                });

			}
		});	
		
		JLabel graphChoiceLabel = new JLabel("Display graph");
		graphChoiceLabel.setFont(labelFont);
		
		headerPanel.add(graphChoiceBox, graphChoiceBoxC);
		headerPanel.add(graphChoiceLabel, graphChoiceLabelC);
		
		// ######### Graph Panel #########

		chart = new Chart();

		Platform.setImplicitExit(false);
		Platform.runLater(new Runnable() {
			public void run() {
				chart.initFX();
			}
		});
		
		graphPanel.add(chart);

		// ######### Header Panel #########
		
		GridBagConstraints c1 = new GridBagConstraints();
		GridBagConstraints c2 = new GridBagConstraints();
		GridBagConstraints c3 = new GridBagConstraints();
		
		c1.gridx = 0;
		c1.gridy = 0;
		c1.anchor = GridBagConstraints.LINE_START;
		c1.insets = new Insets(10, 8, 10, 0);
		
		c2.gridx = 0;
		c2.gridy = 1;
		c2.insets = new Insets (0, 0, 40, 0);
		
		c3.gridx = 0;
		c3.gridy = 2;
		
		Font metricsLabelFont = new Font("", Font.BOLD, 14);
		
		JLabel metricsLabel = new JLabel("Detailed Metrics");
		metricsLabel.setFont(metricsLabelFont);

		tableModel = new SimpleTableModel();
		
		JTable table = new JTable( tableModel );
		TableCellRenderer rendererFromHeader = table.getTableHeader().getDefaultRenderer();
		JLabel headerLabel = (JLabel) rendererFromHeader;
		headerLabel.setHorizontalAlignment(JLabel.CENTER);

		table.setRowHeight(40);
        table.getColumnModel().getColumn(0).setMinWidth(65);
        table.getColumnModel().getColumn(1).setMinWidth(85);
        table.getColumnModel().getColumn(2).setMinWidth(70);
        table.getColumnModel().getColumn(3).setMinWidth(70);
        table.getColumnModel().getColumn(4).setMinWidth(85);
        table.getColumnModel().getColumn(5).setMinWidth(70);
        table.getColumnModel().getColumn(6).setMinWidth(60);
        table.getColumnModel().getColumn(7).setMinWidth(60);
        table.getColumnModel().getColumn(8).setMinWidth(60);
        table.getColumnModel().getColumn(9).setMinWidth(60);
        table.getColumnModel().getColumn(10).setMinWidth(85);

		table.setDefaultRenderer(Object.class, new TableCellRenderer(){

			private DefaultTableCellRenderer DEFAULT_RENDERER = new DefaultTableCellRenderer();

			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

				Component c = DEFAULT_RENDERER.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

				if (c instanceof JLabel) {
					((JLabel) c).setHorizontalAlignment(JLabel.CENTER);
				}

				if (column%2 == 0){
					c.setBackground(Color.WHITE);
                } else {
                    c.setBackground(Color.decode("#e6e6e6"));
                }

                if (!hasFocus) {
                    c.setForeground(Color.BLACK);
                } else {
                    c.setForeground(Color.black);
                    c.setBackground(Color.decode("#82e9ff"));
                }

                /*
                For Future :---->

                 //  Color row based on a cell value

                 So Green color if that value is good , Red if its bad .

                if (!isRowSelected(row))
                {
                    c.setBackground(getBackground());
                    int modelRow = convertRowIndexToModel(row);
                    String type = (String)getModel().getValueAt(modelRow, 0);
                    if ("Buy".equals(type)) c.setBackground(Color.GREEN);
                    if ("Sell".equals(type)) c.setBackground(Color.YELLOW);
                }


                 */


				return c;

			}

		});

        table.setEnabled(true);
        table.setFillsViewportHeight(true);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setToolTipText(" Table showing the Key Metrics. ");
        table.setPreferredScrollableViewportSize(table.getPreferredSize());
        table.setCellSelectionEnabled(true);

		JScrollPane scrollPane = new JScrollPane( table );
		scrollPane.setPreferredSize(new Dimension(
				826, 
				(int)table.getPreferredSize().getHeight() + 30
				));

        //table.getTableHeader().setMinimumSize(new Dimension(scrollPane.getWidth(), 30));

		JPanel tablePanel = new JPanel();
		tablePanel.add(scrollPane);
		
		// ######### Panels #########

		headerPanel.setPreferredSize(new Dimension(800, 100));
//		headerPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.gray));
		
		metricsPanel.add(metricsLabel, c1);
		metricsPanel.add(tablePanel, c2);
		//metricsPanel.setPreferredSize(new Dimension(800, 200));
//		metricsPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.gray));
		
//		headerPanel.setBackground(Color.decode("#ececec"));
//		graphPanel.setBackground(Color.decode("#ececec"));
//		metricsPanel.setBackground(Color.decode("#ececec"));

		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		this.add(headerPanel);
		this.add(graphPanel);
		this.add(metricsPanel);

	}
	
	public void setMetrics(int rowIndex, String[] rowValues){
		
		tableModel.updateRow(0, rowValues);
		
	}
	
	public void setHeaderMetrics(String clicks, String impressions, String cost) {
		this.clicksValueLabel.setText(clicks);
		this.impressionsValueLabel.setText(impressions);
		this.totalCostValueLabel.setText("£" + cost);
	}
	
	public void defaultChart() {
		chart.showClicksChart(dashboard.getClickLogs());
		class Worker extends SwingWorker<Void, Void> {
		    protected Void doInBackground() throws Exception {
				graphChoiceBox.setEnabled(true);
				return null;
		    }
		}
        new Worker().execute();
	}
}

@SuppressWarnings("serial")
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


    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
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
