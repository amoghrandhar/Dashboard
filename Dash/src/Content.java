import javafx.application.Platform;
import javafx.scene.chart.XYChart;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.LinkedList;
import javafx.embed.swing.JFXPanel;

public class Content extends JPanel {

	Dashboard dashboard;
	Chart chart;
	ChartPie pieChart1C1, pieChart2C1, pieChart3C1, pieChart4C1;
	ChartPie pieChart1C2, pieChart2C2, pieChart3C2, pieChart4C2;

	JPanel graphPanel, metricsPanel, headerPanel, piePanel;
	JLabel clicksValueLabel, impressionsValueLabel, totalCostValueLabel;
	JComboBox<String> graphChoiceBox, timeBox;
	SimpleTableModel tableModel;

	JTabbedPane tabbedPane;
	JPanel tab1, tab2, tab3;

	JPanel tablePanel;
	JTable table;
	JScrollPane scrollPane;

	JPanel glassPanel;
	boolean clicked;
	boolean screenShotMode;
	int x, y, width, height;

	String[] row1, row2 , rowFirst;
	Boolean comparing;

	String[][] rowData = {
			{"0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0"},
	};

	String[][] rowData2 = {
			{"0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0"},
			{"0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0"},
	};

	public Content(Dashboard d) {

		this.dashboard = d;
		comparing = false;
		init();

	}

	public void init() {

		this.setLayout(new BorderLayout());

		headerPanel = new JPanel();
		headerPanel.setLayout(new GridBagLayout());

		graphPanel = new JPanel();
		graphPanel.setLayout(new GridBagLayout());

		metricsPanel = new JPanel();
		metricsPanel.setLayout(new GridBagLayout());

		piePanel = new JPanel();
		piePanel.setLayout(new GridBagLayout());

		// ######### GridBagLayout Constraints #########

		GridBagConstraints l1 = new GridBagConstraints();
		GridBagConstraints l2 = new GridBagConstraints();
		GridBagConstraints l3 = new GridBagConstraints();
		GridBagConstraints v1 = new GridBagConstraints();
		GridBagConstraints v2 = new GridBagConstraints();
		GridBagConstraints v3 = new GridBagConstraints();
		GridBagConstraints graphChoiceBoxC = new GridBagConstraints();
		GridBagConstraints graphChoiceLabelC = new GridBagConstraints();
		GridBagConstraints timeBoxC = new GridBagConstraints();
		GridBagConstraints timeLabelC = new GridBagConstraints();

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
		graphChoiceBoxC.insets = new Insets(-4, 150, 0, 20);

		graphChoiceLabelC.gridx = 3;
		graphChoiceLabelC.gridy = 0;
		graphChoiceLabelC.anchor = GridBagConstraints.LINE_START;
		graphChoiceLabelC.insets = new Insets(0, 154, 0, 20);

		timeBoxC.gridx = 4;
		timeBoxC.gridy = 1;
		timeBoxC.anchor = GridBagConstraints.LINE_START;
		timeBoxC.insets = new Insets(-4, 10, 0, 20);

		timeLabelC.gridx = 4;
		timeLabelC.gridy = 0;
		timeLabelC.anchor = GridBagConstraints.LINE_START;
		timeLabelC.insets = new Insets(0, 14, 0, 20);

		// ######### Panels #########

		Font valueFont = new Font("", Font.BOLD, 20);
		Font labelFont = new Font("", Font.PLAIN, 12);

		clicksValueLabel = new JLabel("0");
		impressionsValueLabel = new JLabel("0");
		totalCostValueLabel = new JLabel("\u00A3 0");

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

		String[] graphChoices = {"Number of Clicks", "Impressions", "Uniques", "Bounces", "Conversions",
				"Cumulative Cost", "Click Cost Distribution"};

		graphChoiceBox = new JComboBox(graphChoices);
		graphChoiceBox.setPrototypeDisplayValue("XXXXXXXXXXXXXX");
		graphChoiceBox.setEnabled(false);
		graphChoiceBox.addActionListener(e -> {
			JComboBox<String> cb = (JComboBox) e.getSource();
			final int item = cb.getSelectedIndex();

			Platform.runLater(() -> {

				dashboard.updateGraph(item);

			});

		});

		JLabel graphChoiceLabel = new JLabel("Display graph");
		graphChoiceLabel.setFont(labelFont);

		String[] timeGranularities = {"Hour", "Day", "Week"};

		timeBox = new JComboBox<String>(timeGranularities);
		timeBox.setPrototypeDisplayValue("XXXXXXXXXX");
		timeBox.setEnabled(false);
		timeBox.setSelectedIndex(1);
		timeBox.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				JComboBox<String> cb = (JComboBox) e.getSource();
				final int item = cb.getSelectedIndex();

				Platform.runLater(() -> {

					switch (item) {
					case 1:
						chart.setSDFFormat("yyyy-MM-dd");
						break;
					case 2:
						chart.setSDFFormat("yyyy-ww");
						break;
					default:
						chart.setSDFFormat("yyyy-MM-dd : HH");
						break;
					}

					dashboard.updateGraph(item);

				});

			}

		});

		JLabel timeLabel = new JLabel("Time Granularity");
		graphChoiceLabel.setFont(labelFont);

		headerPanel.add(graphChoiceBox, graphChoiceBoxC);
		headerPanel.add(graphChoiceLabel, graphChoiceLabelC);

		headerPanel.add(timeBox, timeBoxC);
		headerPanel.add(timeLabel, timeLabelC);

		// ######### Tab 1 #########

		chart = new Chart(dashboard);
		chart.addMouseListener(new PrintScreenListener());

		Platform.setImplicitExit(false);
		Platform.runLater(() -> chart.initFX(new XYChart.Series(), "No data"));

		graphPanel.add(chart);

		GridBagConstraints c1 = new GridBagConstraints();
		GridBagConstraints c2 = new GridBagConstraints();
		GridBagConstraints c3 = new GridBagConstraints();

		c1.gridx = 0;
		c1.gridy = 0;
		c1.anchor = GridBagConstraints.LINE_START;
		c1.insets = new Insets(0, 8, 10, 0);

		c2.gridx = 0;
		c2.gridy = 1;
		c2.insets = new Insets(0, 0, 20, 0);

		c3.gridx = 0;
		c3.gridy = 2;

		Font metricsLabelFont = new Font("", Font.BOLD, 14);

		JLabel metricsLabel = new JLabel("Detailed Metrics");
		metricsLabel.setFont(metricsLabelFont);

		table = createTable(rowData);

		scrollPane = new JScrollPane(table);
		scrollPane.setPreferredSize(new Dimension(
				826,
				(int) table.getPreferredSize().getHeight() + 28
				));

		tablePanel = new JPanel();
		tablePanel.add(scrollPane);

		// ######### Tab 2 #########

		GridBagConstraints pc1 = new GridBagConstraints();
		GridBagConstraints pc2 = new GridBagConstraints();
		GridBagConstraints pc3 = new GridBagConstraints();
		GridBagConstraints pc4 = new GridBagConstraints();

		pc1.gridx = 0;
		pc1.gridy = 0;
		pc1.insets = new Insets(16, 16, 16, 16);

		pc2.gridx = 1;
		pc2.gridy = 0;
		pc2.insets = new Insets(16, 16, 16, 16);

		pc3.gridx = 0;
		pc3.gridy = 1;
		pc3.insets = new Insets(16, 16, 16, 16);

		pc4.gridx = 1;
		pc4.gridy = 1;
		pc4.insets = new Insets(16, 16, 16, 16);

		pieChart1C1 = new ChartPie(dashboard);
		pieChart2C1 = new ChartPie(dashboard);
		pieChart3C1 = new ChartPie(dashboard);
		pieChart4C1 = new ChartPie(dashboard);

		pieChart1C1.addMouseListener(new PrintScreenListener());
		pieChart2C1.addMouseListener(new PrintScreenListener());
		pieChart3C1.addMouseListener(new PrintScreenListener());
		pieChart4C1.addMouseListener(new PrintScreenListener());

		Platform.setImplicitExit(false);
		Platform.runLater(() -> {
			pieChart1C1.initFX("Gender Distribution");
			pieChart2C1.initFX("Age Distribution");
			pieChart3C1.initFX("Income Distribution");
			pieChart4C1.initFX("Context Distribution");
		});

		piePanel.add(pieChart1C1, pc1);
		piePanel.add(pieChart2C1, pc2);
		piePanel.add(pieChart3C1, pc3);
		piePanel.add(pieChart4C1, pc4);

		// ######### Panels #########

		headerPanel.setPreferredSize(new Dimension(800, 102));
		headerPanel.setMaximumSize(new Dimension(800, 400));
		//		headerPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.gray));

		metricsPanel.add(metricsLabel, c1);
		metricsPanel.add(tablePanel, c2);
		//		metricsPanel.setPreferredSize(new Dimension(800, 200));
		//		metricsPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.gray));

		tab1 = new JPanel();
		tab2 = new JPanel();

		tab1.setLayout(new BorderLayout());
		tab1.add(graphPanel, BorderLayout.CENTER);
		tab1.add(metricsPanel, BorderLayout.PAGE_END);

		tab2.setLayout(new BorderLayout());
		tab2.add(piePanel, BorderLayout.CENTER);

		tabbedPane = new JTabbedPane();
		tabbedPane.addTab("Metrics Display", tab1);
		tabbedPane.addTab("Impressions Demographics", tab2);
		tabbedPane.setFocusable(false);
		tabbedPane.setSelectedComponent(tab2);

		JPanel bodyPanel = new JPanel();
		bodyPanel.setLayout(new BorderLayout());
		bodyPanel.add(tabbedPane);

		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		this.add(headerPanel);
		this.add(bodyPanel);
		//		this.add(graphPanel);
		//		this.add(metricsPanel);

		glassPanel = new JPanel() {  
			public void paintComponent(Graphics g)  
			{ 			
				if (clicked) {
					Graphics2D g2d = (Graphics2D) g;
					g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
					g2d.fillRect(x, y, width, height);
				}
				else if (screenShotMode)
				{
					if(tabbedPane.getSelectedIndex() == 0) {

						Point rootPaneOrigin = chart.getRootPane().getContentPane().getLocationOnScreen();
						Point pGraph = chart.getLocationOnScreen();
						Point pTable = table.getLocationOnScreen();

						Graphics2D g2d = (Graphics2D) g;
						g2d.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
						g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));

						Rectangle chartRect = new Rectangle(pGraph.x - rootPaneOrigin.x + (chart.getWidth() / 2) - 85, 
								pGraph.y - rootPaneOrigin.y + (chart.getHeight() / 2) - 40, 170, 80);
						Rectangle tableRect = new Rectangle(pTable.x - rootPaneOrigin.x + (table.getWidth() / 2) - 140, 
								pTable.y - rootPaneOrigin.y + 5, 280, 30);

						g2d.fillRoundRect(chartRect.x, chartRect.y, chartRect.width, chartRect.height, 30, 30);
						g2d.fillRoundRect(tableRect.x, tableRect.y, tableRect.width, tableRect.height, 30, 30);

						g2d.setFont(new Font("Arial", Font.PLAIN, 20));
						g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
						g2d.setColor(Color.WHITE);
						g2d.drawString("Click on the", chartRect.x + 33, chartRect.y + 35);
						g2d.drawString("graph to export", chartRect.x + 20, chartRect.y + 55);	
						g2d.drawString("Click on the table to export", tableRect.x + 20, tableRect.y + 20);
					}

					if (tabbedPane.getSelectedIndex() == 1) {

					Point rootPaneOrigin = pieChart1C1.getRootPane().getContentPane().getLocationOnScreen();

					Point pPie1 = pieChart1C1.getLocationOnScreen();
					Point pPie2 = pieChart2C1.getLocationOnScreen();
					Point pPie3 = pieChart3C1.getLocationOnScreen();
					Point pPie4 = pieChart4C1.getLocationOnScreen();

					Graphics2D g2d = (Graphics2D) g;
					g2d.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
					g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));


					Rectangle pie1Rect = new Rectangle(pPie1.x - rootPaneOrigin.x + (pieChart1C1.getWidth() / 2) - 85,
							pPie1.y - rootPaneOrigin.y + (pieChart1C1.getHeight() / 2) - 40, 170, 80);
					Rectangle pie2Rect = new Rectangle(pPie2.x - rootPaneOrigin.x + (pieChart2C1.getWidth() / 2) - 85,
							pPie2.y - rootPaneOrigin.y + (pieChart2C1.getHeight() / 2) - 40, 170, 80);
					Rectangle pie3Rect = new Rectangle(pPie3.x - rootPaneOrigin.x + (pieChart3C1.getWidth() / 2) - 85,
							pPie3.y - rootPaneOrigin.y + (pieChart3C1.getHeight() / 2) - 40, 170, 80);
					Rectangle pie4Rect = new Rectangle(pPie4.x - rootPaneOrigin.x + (pieChart4C1.getWidth() / 2) - 85,
							pPie4.y - rootPaneOrigin.y + (pieChart4C1.getHeight() / 2) - 40, 170, 80);

					g2d.fillRoundRect(pie1Rect.x, pie1Rect.y, pie1Rect.width, pie1Rect.height, 30, 30);
					g2d.fillRoundRect(pie2Rect.x, pie2Rect.y, pie2Rect.width, pie2Rect.height, 30, 30);
					g2d.fillRoundRect(pie3Rect.x, pie3Rect.y, pie3Rect.width, pie3Rect.height, 30, 30);
					g2d.fillRoundRect(pie4Rect.x, pie4Rect.y, pie4Rect.width, pie4Rect.height, 30, 30);


					g2d.setFont(new Font("Arial", Font.PLAIN, 20));
					g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
					g2d.setColor(Color.WHITE);
					g2d.drawString("Click on the", pie1Rect.x + 33, pie1Rect.y + 35);
					g2d.drawString("chart to export", pie1Rect.x + 20, pie1Rect.y + 55);
					g2d.drawString("Click on the", pie2Rect.x + 33, pie2Rect.y + 35);
					g2d.drawString("chart to export", pie2Rect.x + 20, pie2Rect.y + 55);
					g2d.drawString("Click on the", pie3Rect.x + 33, pie3Rect.y + 35);
					g2d.drawString("chart to export", pie3Rect.x + 20, pie3Rect.y + 55);
					g2d.drawString("Click on the", pie4Rect.x + 33, pie4Rect.y + 35);
					g2d.drawString("chart to export", pie4Rect.x + 20, pie4Rect.y + 55);
				}
					if (tabbedPane.getSelectedIndex() == 2) {

						Point rootPaneOrigin = pieChart1C1.getRootPane().getContentPane().getLocationOnScreen();

						Point pPie1 = pieChart1C2.getLocationOnScreen();
						Point pPie2 = pieChart2C2.getLocationOnScreen();
						Point pPie3 = pieChart3C2.getLocationOnScreen();
						Point pPie4 = pieChart4C2.getLocationOnScreen();

						Graphics2D g2d = (Graphics2D) g;
						g2d.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
						g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));


						Rectangle pie1Rect = new Rectangle(pPie1.x - rootPaneOrigin.x + (pieChart1C2.getWidth() / 2) - 85,
								pPie1.y - rootPaneOrigin.y + (pieChart1C2.getHeight() / 2) - 40, 170, 80);
						Rectangle pie2Rect = new Rectangle(pPie2.x - rootPaneOrigin.x + (pieChart2C2.getWidth() / 2) - 85,
								pPie2.y - rootPaneOrigin.y + (pieChart2C2.getHeight() / 2) - 40, 170, 80);
						Rectangle pie3Rect = new Rectangle(pPie3.x - rootPaneOrigin.x + (pieChart3C2.getWidth() / 2) - 85,
								pPie3.y - rootPaneOrigin.y + (pieChart3C2.getHeight() / 2) - 40, 170, 80);
						Rectangle pie4Rect = new Rectangle(pPie4.x - rootPaneOrigin.x + (pieChart4C2.getWidth() / 2) - 85,
								pPie4.y - rootPaneOrigin.y + (pieChart4C2.getHeight() / 2) - 40, 170, 80);

						g2d.fillRoundRect(pie1Rect.x, pie1Rect.y, pie1Rect.width, pie1Rect.height, 30, 30);
						g2d.fillRoundRect(pie2Rect.x, pie2Rect.y, pie2Rect.width, pie2Rect.height, 30, 30);
						g2d.fillRoundRect(pie3Rect.x, pie3Rect.y, pie3Rect.width, pie3Rect.height, 30, 30);
						g2d.fillRoundRect(pie4Rect.x, pie4Rect.y, pie4Rect.width, pie4Rect.height, 30, 30);


						g2d.setFont(new Font("Arial", Font.PLAIN, 20));
						g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
						g2d.setColor(Color.WHITE);
						g2d.drawString("Click on the", pie1Rect.x + 33, pie1Rect.y + 35);
						g2d.drawString("chart to export", pie1Rect.x + 20, pie1Rect.y + 55);
						g2d.drawString("Click on the", pie2Rect.x + 33, pie2Rect.y + 35);
						g2d.drawString("chart to export", pie2Rect.x + 20, pie2Rect.y + 55);
						g2d.drawString("Click on the", pie3Rect.x + 33, pie3Rect.y + 35);
						g2d.drawString("chart to export", pie3Rect.x + 20, pie3Rect.y + 55);
						g2d.drawString("Click on the", pie4Rect.x + 33, pie4Rect.y + 35);
						g2d.drawString("chart to export", pie4Rect.x + 20, pie4Rect.y + 55);
					}
				}

			}  
		};

	}

	public JTable createTable(String[][] data){

		String[] columnNames = {"Clicks", "Impressions", "Uniques", "Bounces",
				"Conversions", "Total Cost", "CTR", "CPA", "CPC", "CPM", "Bounce Rate"};

		tableModel = new SimpleTableModel(data, columnNames);

		table = new JTable(tableModel);
		table.addMouseListener(new PrintScreenListener());
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

		table.setDefaultRenderer(Object.class, new MetricsTableRenderer());
		table.setEnabled(true);
		table.setFillsViewportHeight(true);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		table.setToolTipText(" Table showing the Key Metrics. ");
		table.setPreferredScrollableViewportSize(table.getPreferredSize());
		table.setCellSelectionEnabled(true);

		return table;

	}

	public JTable createTable2(String[][] data){

		String[] columnNames = {"#", "Clicks", "Impressions", "Uniques", "Bounces",
				"Conversions", "Total Cost", "CTR", "CPA", "CPC", "CPM", "Bounce Rate"};

		tableModel = new SimpleTableModel(data, columnNames);

		table = new JTable(tableModel);
		table.addMouseListener(new PrintScreenListener());
		TableCellRenderer rendererFromHeader = table.getTableHeader().getDefaultRenderer();
		JLabel headerLabel = (JLabel) rendererFromHeader;
		headerLabel.setHorizontalAlignment(JLabel.CENTER);

		table.setRowHeight(40);
		table.getColumnModel().getColumn(0).setMinWidth(15);
		table.getColumnModel().getColumn(1).setMinWidth(60);
		table.getColumnModel().getColumn(2).setMinWidth(85);
		table.getColumnModel().getColumn(3).setMinWidth(65);
		table.getColumnModel().getColumn(4).setMinWidth(65);
		table.getColumnModel().getColumn(5).setMinWidth(85);
		table.getColumnModel().getColumn(6).setMinWidth(70);
		table.getColumnModel().getColumn(7).setMinWidth(60);
		table.getColumnModel().getColumn(8).setMinWidth(60);
		table.getColumnModel().getColumn(9).setMinWidth(60);
		table.getColumnModel().getColumn(10).setMinWidth(60);
		table.getColumnModel().getColumn(11).setMinWidth(85);

		table.setDefaultRenderer(Object.class, new MetricsTableRenderer());
		table.setEnabled(true);
		table.setFillsViewportHeight(true);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		table.setToolTipText(" Table showing the Key Metrics. ");
		table.setPreferredScrollableViewportSize(table.getPreferredSize());
		table.setCellSelectionEnabled(true);

		return table;

	}

	public void setMetrics(int rowIndex, String[] rowValues) {

		if(dashboard.sidebar.selectedSeries == 1)
			row1 = rowValues;
		if(dashboard.sidebar.selectedSeries == 2)
			row2 = rowValues;
		if(row2 == null)
			row2 = rowValues;

		row1 = rowValues;

		if(rowIndex == 0){
			rowFirst = rowValues;
			System.out.println("Row 1 updated .. to row f");
		}

		if(comparing){

			String[] myArray = rowValues;
			LinkedList<String> list = new LinkedList<String>(Arrays.asList(myArray));
			list.add(0, Integer.toString(rowIndex+1));
			myArray = list.toArray(new String[rowValues.length+1]);

			tableModel.updateRow(rowIndex, myArray);

			row1 = rowFirst;

		}

		else {
			row1 = rowFirst;
			tableModel.updateRow(rowIndex, rowFirst);
		}


	}

	public void setHeaderMetrics(String clicks, String impressions, String cost) {

		this.clicksValueLabel.setText(clicks);
		this.impressionsValueLabel.setText(impressions);
		this.totalCostValueLabel.setText("\u00A3" + cost);

	}

	public void defaultChart() {

		if(!dashboard.isComparing()) {
			chart.showClicksChart(dashboard.getClickLogsC1());
			pieChart1C1.showGenderPie(1);
			pieChart2C1.showAgeGroupPie(1);
			pieChart3C1.showIncomePie(1);
			pieChart4C1.showContextPie(1);
		}
		if(dashboard.isComparing()){
			chart.showClicksChart2(dashboard.getClickLogsC1(), dashboard.getClickLogs2());
			pieChart1C1.showGenderPie(1);
			pieChart2C1.showAgeGroupPie(1);
			pieChart3C1.showIncomePie(1);
			pieChart4C1.showContextPie(1);
			pieChart1C2.showGenderPie(2);
			pieChart2C2.showAgeGroupPie(2);
			pieChart3C2.showIncomePie(2);
			pieChart4C2.showContextPie(2);
		}

		class Worker extends SwingWorker<Void, Void> {

			protected Void doInBackground() throws Exception {
				graphChoiceBox.setEnabled(true);
				timeBox.setEnabled(true);
				dashboard.sidebar.updateButton.setEnabled(true);
				dashboard.sidebar.resetButton.setEnabled(true);
				return null;
			}

		}

		new Worker().execute();

	}
	
	public void addExtraTab(String title){
		
		tab3 = new JPanel();	
		tab3.setLayout(new BorderLayout());
		
		JPanel piePanel2 = new JPanel();
		piePanel2.setLayout(new GridBagLayout());
		
		GridBagConstraints pc1 = new GridBagConstraints();
		GridBagConstraints pc2 = new GridBagConstraints();
		GridBagConstraints pc3 = new GridBagConstraints();
		GridBagConstraints pc4 = new GridBagConstraints();

		pc1.gridx = 0;
		pc1.gridy = 0;
		pc1.insets = new Insets(16, 16, 16, 16);

		pc2.gridx = 1;
		pc2.gridy = 0;
		pc2.insets = new Insets(16, 16, 16, 16);

		pc3.gridx = 0;
		pc3.gridy = 1;
		pc3.insets = new Insets(16, 16, 16, 16);

		pc4.gridx = 1;
		pc4.gridy = 1;
		pc4.insets = new Insets(16, 16, 16, 16);
		
		pieChart1C2= new ChartPie(dashboard);
		pieChart2C2 = new ChartPie(dashboard);
		pieChart3C2 = new ChartPie(dashboard);
		pieChart4C2 = new ChartPie(dashboard);

		pieChart1C2.addMouseListener(new PrintScreenListener());
		pieChart2C2.addMouseListener(new PrintScreenListener());
		pieChart3C2.addMouseListener(new PrintScreenListener());
		pieChart4C2.addMouseListener(new PrintScreenListener());

		Platform.setImplicitExit(false);
		Platform.runLater(() -> {
			pieChart1C2.initFX("Gender Distribution");
			pieChart2C2.initFX("Age Distribution");
			pieChart3C2.initFX("Income Distribution");
			pieChart4C2.initFX("Context Distribution");
		});
		
		piePanel2.add(pieChart1C2, pc1);
		piePanel2.add(pieChart2C2, pc2);
		piePanel2.add(pieChart3C2, pc3);
		piePanel2.add(pieChart4C2, pc4);

		this.pieChart1C2.showGenderPie(2);
		this.pieChart2C2.showAgeGroupPie(2);
		this.pieChart3C2.showIncomePie(2);
		this.pieChart4C2.showContextPie(2);

		tab3.add(piePanel2, BorderLayout.CENTER);
		
		tabbedPane.addTab("Demographics of " + title, tab3);
		
	}
	
	public void removeExtraTab(){
		
		tabbedPane.remove(tab3);
		
	}

	class PrintScreenListener implements MouseListener{

		@Override
		public void mousePressed(MouseEvent e) {

			if (screenShotMode) {

				if(tabbedPane.getSelectedIndex() == 0) {

					//Get "absolute" coordinates of root pane
					Point rootPaneOrigin =
							chart.getRootPane().getContentPane().getLocationOnScreen();

					//Get "absolute" coordinates of chart
					Point pGraph = chart.getLocationOnScreen();

					//Get "absolute" coordinates of Table
					Point pTable = table.getLocationOnScreen();

					if (e.getSource() == chart)
						drawSquare( pGraph.x - rootPaneOrigin.x, pGraph.y - rootPaneOrigin.y, chart.getWidth(), chart.getHeight());

					if (e.getSource() == table)
						drawSquare(pTable.x - rootPaneOrigin.x, pTable.y - rootPaneOrigin.y - 22, table.getWidth(), table.getHeight() + 22);

				}

				if(tabbedPane.getSelectedIndex() == 1) {

					//Get "absolute" coordinates of root pane
					Point rootPaneOrigin =
							pieChart1C1.getRootPane().getContentPane().getLocationOnScreen();

					Point pPie1 = pieChart1C1.getLocationOnScreen();
					Point pPie2 = pieChart2C1.getLocationOnScreen();
					Point pPie3 = pieChart3C1.getLocationOnScreen();
					Point pPie4 = pieChart4C1.getLocationOnScreen();

					if (e.getSource() == pieChart1C1)
						drawSquare(pPie1.x - rootPaneOrigin.x, pPie1.y - rootPaneOrigin.y, pieChart1C1.getWidth(), pieChart1C1.getHeight());

					if (e.getSource() == pieChart2C1)
						drawSquare(pPie2.x - rootPaneOrigin.x, pPie2.y - rootPaneOrigin.y, pieChart2C1.getWidth(), pieChart2C1.getHeight());

					if (e.getSource() == pieChart3C1)
						drawSquare( pPie3.x - rootPaneOrigin.x, pPie3.y - rootPaneOrigin.y , pieChart3C1.getWidth(), pieChart3C1.getHeight());

					if (e.getSource() == pieChart4C1)
						drawSquare( pPie4.x - rootPaneOrigin.x, pPie4.y - rootPaneOrigin.y , pieChart4C1.getWidth(), pieChart4C1.getHeight());
				}
				if(tabbedPane.getSelectedIndex() == 2) {

					//Get "absolute" coordinates of root pane
					Point rootPaneOrigin =
							pieChart1C1.getRootPane().getContentPane().getLocationOnScreen();

					Point pPie1 = pieChart1C2.getLocationOnScreen();
					Point pPie2 = pieChart2C2.getLocationOnScreen();
					Point pPie3 = pieChart3C2.getLocationOnScreen();
					Point pPie4 = pieChart4C2.getLocationOnScreen();

					if (e.getSource() == pieChart1C2)
						drawSquare(pPie1.x - rootPaneOrigin.x, pPie1.y - rootPaneOrigin.y, pieChart1C2.getWidth(), pieChart1C2.getHeight());

					if (e.getSource() == pieChart2C2)
						drawSquare(pPie2.x - rootPaneOrigin.x, pPie2.y - rootPaneOrigin.y, pieChart2C2.getWidth(), pieChart2C2.getHeight());

					if (e.getSource() == pieChart3C2)
						drawSquare( pPie3.x - rootPaneOrigin.x, pPie3.y - rootPaneOrigin.y , pieChart3C2.getWidth(), pieChart3C2.getHeight());

					if (e.getSource() == pieChart4C2)
						drawSquare( pPie4.x - rootPaneOrigin.x, pPie4.y - rootPaneOrigin.y , pieChart4C2.getWidth(), pieChart4C2.getHeight());
				}
			}
		}

		private void drawSquare(int _x, int _y, int _width, int _height) {
			x = _x;
			y = _y;
			width = _width;
			height = _height;
			clicked = !clicked;	
			glassPanel.setOpaque(false);  
			dashboard.setGlassPane(glassPanel);  
			glassPanel.setVisible(clicked);
			glassPanel.repaint();

			dashboard.sidebar.exportButton.setText(" Export ");
			dashboard.sidebar.exportButton.setIcon(dashboard.sidebar.exportIcon);
			dashboard.sidebar.exportButton.setBackground(UIManager.getColor("Button.background")); 
		}

		@Override
		public void mouseReleased(MouseEvent e) {

			if (screenShotMode) {
				
				if (e.getSource() == chart)
					exportJFXChart(chart);
				
				else if (e.getSource() == table){
					drawSquare(table.getWidth(), table.getHeight(), table.getWidth(), table.getHeight());
					JFileChooser fc = new JFileChooser();
					FileFilter f1 = new ImageFilter("PNG", new String[]{"PNG"});
					FileFilter f2 = new ImageFilter("JPG", new String[]{"JPG"});
					fc.setFileFilter(f1);
					fc.addChoosableFileFilter(f1);
					fc.addChoosableFileFilter(f2);
					fc.setAcceptAllFileFilterUsed(false);
					int retrival = fc.showSaveDialog(null);
					if (retrival == fc.APPROVE_OPTION) {
						String ext = "";
						String extension = fc.getFileFilter().getDescription();
						if (extension.equals("JPG")) {
							ext = ".jpg";
						}
						if (extension.equals("PNG")) {
							ext = ".png";
						}
						File file = fc.getSelectedFile();
						BufferedImage bufImage = new BufferedImage(scrollPane.getSize().width, scrollPane.getSize().height,BufferedImage.TYPE_INT_RGB);
						scrollPane.paint(bufImage.createGraphics());
						File imageFile = new File((file.getAbsolutePath() + ext));
						try {
							imageFile.createNewFile();
							ImageIO.write(bufImage, extension, imageFile);
						} catch(Exception ex) {
							ex.printStackTrace();
						}
					}				
				}
				
				else if (e.getSource() == pieChart1C1)
					exportJFXChart(pieChart1C1);

				else if (e.getSource() == pieChart2C1)
					exportJFXChart(pieChart2C1);
				
				else if (e.getSource() == pieChart3C1)
					exportJFXChart(pieChart3C1);
				
				else if (e.getSource() == pieChart4C1)
					exportJFXChart(pieChart4C1);
				
				screenShotMode = false;
				
			}
		}

		@Override
		public void mouseClicked(MouseEvent e) {}

		@Override
		public void mouseEntered(MouseEvent e) {}

		@Override
		public void mouseExited(MouseEvent e) {}   	
		
		public void exportJFXChart(JFXPanel jfxPanel){
			
			drawSquare(jfxPanel.getWidth(), jfxPanel.getHeight(), jfxPanel.getWidth(), jfxPanel.getHeight());
			JFileChooser fc = new JFileChooser();
			FileFilter f1 = new ImageFilter("PNG", new String[]{"PNG"});
			FileFilter f2 = new ImageFilter("JPG", new String[]{"JPG"});
			fc.setFileFilter(f1);
			fc.addChoosableFileFilter(f1);
			fc.addChoosableFileFilter(f2);
			fc.setAcceptAllFileFilterUsed(false);
			int retrival = fc.showSaveDialog(null);
			if (retrival == fc.APPROVE_OPTION) {
				String ext = "";
				String extension = fc.getFileFilter().getDescription();
				if (extension.equals("JPG")) {
					ext = ".jpg";
				}
				if (extension.equals("PNG")) {
					ext = ".png";
				}
				File file = fc.getSelectedFile();
				BufferedImage bufImage = new BufferedImage(jfxPanel.getSize().width, jfxPanel.getSize().height,BufferedImage.TYPE_INT_RGB);
				jfxPanel.paint(bufImage.createGraphics());
				File imageFile = new File((file.getAbsolutePath() + ext));
				try {
					imageFile.createNewFile();
					ImageIO.write(bufImage, extension, imageFile);
				} catch(Exception ex) {
					ex.printStackTrace();
				}
			}	
		}
		
	}

	private class MetricsTableRenderer implements TableCellRenderer {

		private DefaultTableCellRenderer DEFAULT_RENDERER = new DefaultTableCellRenderer();

		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

			Component c = DEFAULT_RENDERER.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

			if (c instanceof JLabel) {
				((JLabel) c).setHorizontalAlignment(JLabel.CENTER);
			}

			if (column % 2 == 0) {
				c.setBackground(Color.WHITE);
			} else {
				c.setBackground(Color.decode("#e6e6e6"));
			}

			if (!isSelected) {
				c.setForeground(Color.BLACK);
			} else {
				c.setForeground(Color.black);
				c.setBackground(Color.decode("#82e9ff"));
			}

			if(!hasFocus){
				c.setForeground(Color.BLACK);
			} else {
				c.setForeground(Color.BLACK);
				c.setBackground(Color.decode("#74efec"));
			}

			return c;

		}

	}

	private class SimpleTableModel extends AbstractTableModel {

		private String[] columnNames;
		private String[][] rowData;

		public SimpleTableModel(String[][] rowData, String[] columnNames){

			this.columnNames = columnNames;
			this.rowData = rowData;

		}

		public String getColumnName(int col) {
			return columnNames[col];
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

		public void updateRow(int index, String[] values) {

			for (int i = 0; i < values.length; i++)
				setValueAt(values[i], index, i);

		}

		public String round(double value, int scale) {

			return new BigDecimal(String.valueOf(value)).setScale(scale, BigDecimal.ROUND_HALF_UP).toString();

		}

	}

}
