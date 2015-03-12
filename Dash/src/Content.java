import javafx.application.Platform;
import javafx.scene.chart.XYChart;

import javax.imageio.ImageIO;
import javax.swing.*;
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

public class Content extends JPanel {

	Dashboard dashboard;
	Chart chart;
	ChartPie pieChart1, pieChart2, pieChart3, pieChart4;

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

	public Content(Dashboard d) {

		this.dashboard = d;
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
				switch (item) {
				case 1:
					chart.showImpressionsChart(dashboard.getImpressionLogs());
					break;
				case 2:
					chart.showUniqueChart(dashboard.dataAnalytics.uniqueClickSet(dashboard.getClickLogs()));
					break;
				case 3:
					chart.showBounceChart(dashboard.dataAnalytics.getFilteredServerLogOnBounce(dashboard.getServerLogs(), 
							dashboard.sidebar.getChosenPages(), dashboard.sidebar.getChosenTime()));
					break;
				case 4:
					chart.showConversionChart(dashboard.getServerLogs());
					break;
				case 5:
					chart.showCumulativeCostChart(dashboard.getClickLogs(),dashboard.getImpressionLogs());
					break;
				case 6:
					chart.showClickCostsHistogram(dashboard.getClickLogs());
					break;
				default:
					chart.showClicksChart(dashboard.getClickLogs());
					break;
				}

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

			// TODO For OLIVER to implement time granularity changes
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

					switch (graphChoiceBox.getSelectedIndex()) {
					case 1:
						chart.showImpressionsChart(dashboard.getImpressionLogs());
						break;
					case 2:
						chart.showUniqueChart(dashboard.dataAnalytics.uniqueClickSet(dashboard.getClickLogs()));
						break;
					case 3:
						chart.showBounceChart(dashboard.dataAnalytics.getFilteredServerLogOnBounce(dashboard.getServerLogs(), 
								dashboard.sidebar.getChosenPages(), dashboard.sidebar.getChosenTime()));
						break;
					case 4:
						chart.showConversionChart(dashboard.getServerLogs());
						break;
					case 5:
						chart.showCumulativeCostChart(dashboard.getClickLogs(),dashboard.getImpressionLogs());
						break;
					case 6:
						chart.showClickCostsHistogram(dashboard.getClickLogs());
						break;
					default:
						chart.showClicksChart(dashboard.getClickLogs());
						break;
					}

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

        chart = new Chart();
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
		c1.insets = new Insets(10, 8, 10, 0);

		c2.gridx = 0;
		c2.gridy = 1;
		c2.insets = new Insets(0, 0, 40, 0);

		c3.gridx = 0;
		c3.gridy = 2;

		Font metricsLabelFont = new Font("", Font.BOLD, 14);

		JLabel metricsLabel = new JLabel("Detailed Metrics");
		metricsLabel.setFont(metricsLabelFont);

		tableModel = new SimpleTableModel();

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

		scrollPane = new JScrollPane(table);
		scrollPane.setPreferredSize(new Dimension(
				826,
				(int) table.getPreferredSize().getHeight() + 30
				));

		//table.getTableHeader().setMinimumSize(new Dimension(scrollPane.getWidth(), 30));

		JPanel tablePanel = new JPanel();
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

		pieChart1 = new ChartPie(dashboard);
		pieChart2 = new ChartPie(dashboard);
		pieChart3 = new ChartPie(dashboard);
		pieChart4 = new ChartPie(dashboard);

		pieChart1.addMouseListener(new PrintScreenListener());
		pieChart2.addMouseListener(new PrintScreenListener());
		pieChart3.addMouseListener(new PrintScreenListener());
		pieChart4.addMouseListener(new PrintScreenListener());

		Platform.setImplicitExit(false);
		Platform.runLater(() -> {
			pieChart1.initFX("Gender Distribution");
			pieChart2.initFX("Age Distribution");
			pieChart3.initFX("Income Distribution");
			pieChart4.initFX("Context Distribution");
		});

		piePanel.add(pieChart1, pc1);
		piePanel.add(pieChart2, pc2);
		piePanel.add(pieChart3, pc3);
		piePanel.add(pieChart4, pc4);

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
		tab3 = new JPanel();

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
					
						Point rootPaneOrigin = pieChart1.getRootPane().getContentPane().getLocationOnScreen();
						
						Point pPie1 = pieChart1.getLocationOnScreen();
						Point pPie2 = pieChart2.getLocationOnScreen();
						Point pPie3 = pieChart3.getLocationOnScreen();
						Point pPie4 = pieChart4.getLocationOnScreen();
						
						Graphics2D g2d = (Graphics2D) g;
						g2d.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
						g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
						
						
						Rectangle pie1Rect = new Rectangle(pPie1.x - rootPaneOrigin.x + (pieChart1.getWidth() / 2) - 85, 
								pPie1.y - rootPaneOrigin.y + (pieChart1.getHeight() / 2) - 40, 170, 80);
						Rectangle pie2Rect = new Rectangle(pPie2.x - rootPaneOrigin.x + (pieChart2.getWidth() / 2) - 85, 
								pPie2.y - rootPaneOrigin.y + (pieChart2.getHeight() / 2) - 40, 170, 80);
						Rectangle pie3Rect = new Rectangle(pPie3.x - rootPaneOrigin.x + (pieChart3.getWidth() / 2) - 85, 
								pPie3.y - rootPaneOrigin.y + (pieChart3.getHeight() / 2) - 40, 170, 80);
						Rectangle pie4Rect = new Rectangle(pPie4.x - rootPaneOrigin.x + (pieChart4.getWidth() / 2) - 85, 
								pPie4.y - rootPaneOrigin.y + (pieChart4.getHeight() / 2) - 40, 170, 80);
			
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

	public void setMetrics(int rowIndex, String[] rowValues) {

		tableModel.updateRow(rowIndex, rowValues);

	}

	public void setHeaderMetrics(String clicks, String impressions, String cost) {

		this.clicksValueLabel.setText(clicks);
		this.impressionsValueLabel.setText(impressions);
		this.totalCostValueLabel.setText("\u00A3" + cost);

	}

	public void defaultChart() {

		chart.showClicksChart(dashboard.getClickLogs());
		pieChart1.showGenderPie();
		pieChart2.showAgeGroupPie();
		pieChart3.showIncomePie();
		pieChart4.showContextPie();

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
							pieChart1.getRootPane().getContentPane().getLocationOnScreen();

					Point pPie1 = pieChart1.getLocationOnScreen();
					Point pPie2 = pieChart2.getLocationOnScreen();
					Point pPie3 = pieChart3.getLocationOnScreen();
					Point pPie4 = pieChart4.getLocationOnScreen();

					if (e.getSource() == pieChart1)
						drawSquare( pPie1.x - rootPaneOrigin.x, pPie1.y - rootPaneOrigin.y ,pieChart1.getWidth(),pieChart1.getHeight());

					if (e.getSource() == pieChart2)
						drawSquare( pPie2.x - rootPaneOrigin.x, pPie2.y - rootPaneOrigin.y ,pieChart2.getWidth(),pieChart2.getHeight());

					if (e.getSource() == pieChart3)
						drawSquare( pPie3.x - rootPaneOrigin.x, pPie3.y - rootPaneOrigin.y ,pieChart3.getWidth(),pieChart3.getHeight());

					if (e.getSource() == pieChart4)
						drawSquare( pPie4.x - rootPaneOrigin.x, pPie4.y - rootPaneOrigin.y ,pieChart4.getWidth(),pieChart4.getHeight());
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
			
			//screenShotMode = false;  
			dashboard.sidebar.exportButton.setText(" Export ");
			dashboard.sidebar.exportButton.setIcon(dashboard.sidebar.exportIcon);
			dashboard.sidebar.exportButton.setBackground(UIManager.getColor("Button.background")); 
         }

		@Override
		public void mouseReleased(MouseEvent e) {

			if (screenShotMode) {
				if (e.getSource() == chart)
				{
					drawSquare(chart.getWidth(), chart.getHeight(), chart.getWidth(), chart.getHeight());
					JFileChooser fc = new JFileChooser();
					int retrival = fc.showSaveDialog(null);
					if (retrival == JFileChooser.APPROVE_OPTION) {
						File file = fc.getSelectedFile();
						BufferedImage bufImage = new BufferedImage(graphPanel.getSize().width, graphPanel.getSize().height,BufferedImage.TYPE_INT_RGB);
						graphPanel.paint(bufImage.createGraphics());
						File imageFile = new File((file.getAbsolutePath() + ".png"));
						try {
							imageFile.createNewFile();
							ImageIO.write(bufImage, "png", imageFile);
						} catch(Exception ex) {
							ex.printStackTrace();
						}
					}
				}
				else if (e.getSource() == table)
				{
					drawSquare(table.getWidth(), table.getHeight(), table.getWidth(), table.getHeight());
					JFileChooser fc = new JFileChooser();
					int retrival = fc.showSaveDialog(null);
					if (retrival == JFileChooser.APPROVE_OPTION) {
						File file = fc.getSelectedFile();
						BufferedImage bufImage = new BufferedImage(scrollPane.getSize().width, scrollPane.getSize().height,BufferedImage.TYPE_INT_RGB);
						scrollPane.paint(bufImage.createGraphics());
						File imageFile = new File((file.getAbsolutePath() + ".png"));
						try {
							imageFile.createNewFile();
							ImageIO.write(bufImage, "png", imageFile);
						} catch(Exception ex) {
							ex.printStackTrace();
						}
					}				
				}
				else if (e.getSource() == pieChart1)
				{
					drawSquare(pieChart1.getWidth(), pieChart1.getHeight(), pieChart1.getWidth(), pieChart1.getHeight());
					JFileChooser fc = new JFileChooser();
					int retrival = fc.showSaveDialog(null);
					if (retrival == JFileChooser.APPROVE_OPTION) {
						File file = fc.getSelectedFile();
						BufferedImage bufImage = new BufferedImage(pieChart1.getSize().width, pieChart1.getSize().height,BufferedImage.TYPE_INT_RGB);
						pieChart1.paint(bufImage.createGraphics());
						File imageFile = new File((file.getAbsolutePath() + ".png"));
						try {
							imageFile.createNewFile();
							ImageIO.write(bufImage, "png", imageFile);
						} catch(Exception ex) {
							ex.printStackTrace();
						}
					}				
				}
				else if (e.getSource() == pieChart2)
				{
					drawSquare(pieChart2.getWidth(), pieChart2.getHeight(), pieChart2.getWidth(), pieChart2.getHeight());
					JFileChooser fc = new JFileChooser();
					int retrival = fc.showSaveDialog(null);
					if (retrival == JFileChooser.APPROVE_OPTION) {
						File file = fc.getSelectedFile();
						BufferedImage bufImage = new BufferedImage(pieChart2.getSize().width, pieChart2.getSize().height,BufferedImage.TYPE_INT_RGB);
						pieChart2.paint(bufImage.createGraphics());
						File imageFile = new File((file.getAbsolutePath() + ".png"));
						try {
							imageFile.createNewFile();
							ImageIO.write(bufImage, "png", imageFile);
						} catch(Exception ex) {
							ex.printStackTrace();
						}
					}				
				}
				else if (e.getSource() == pieChart3)
				{
					drawSquare(pieChart3.getWidth(), pieChart3.getHeight(), pieChart3.getWidth(), pieChart3.getHeight());
					JFileChooser fc = new JFileChooser();
					int retrival = fc.showSaveDialog(null);
					if (retrival == JFileChooser.APPROVE_OPTION) {
						File file = fc.getSelectedFile();
						BufferedImage bufImage = new BufferedImage(pieChart3.getSize().width, pieChart3.getSize().height,BufferedImage.TYPE_INT_RGB);
						pieChart3.paint(bufImage.createGraphics());
						File imageFile = new File((file.getAbsolutePath() + ".png"));
						try {
							imageFile.createNewFile();
							ImageIO.write(bufImage, "png", imageFile);
						} catch(Exception ex) {
							ex.printStackTrace();
						}
					}				
				}
				else if (e.getSource() == pieChart4)
				{
					drawSquare(pieChart4.getWidth(), pieChart4.getHeight(), pieChart4.getWidth(), pieChart4.getHeight());
					JFileChooser fc = new JFileChooser();
					int retrival = fc.showSaveDialog(null);
					if (retrival == JFileChooser.APPROVE_OPTION) {
						File file = fc.getSelectedFile();
						BufferedImage bufImage = new BufferedImage(pieChart4.getSize().width, pieChart4.getSize().height,BufferedImage.TYPE_INT_RGB);
						pieChart4.paint(bufImage.createGraphics());
						File imageFile = new File((file.getAbsolutePath() + ".png"));
						try {
							imageFile.createNewFile();
							ImageIO.write(bufImage, "png", imageFile);
						} catch(Exception ex) {
							ex.printStackTrace();
						}
					}				
				}
				screenShotMode = false;
			}
		}

		@Override
		public void mouseClicked(MouseEvent e) {}

		@Override
		public void mouseEntered(MouseEvent e) {}

		@Override
		public void mouseExited(MouseEvent e) {}   	
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

	}

	private class SimpleTableModel extends AbstractTableModel {

		private String[][] rowData = {
				{"0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0"},
		};

		private String[] columnNames = {"Clicks", "Impressions", "Uniques", "Bounces",
				"Conversions", "Total Cost", "CTR", "CPA", "CPC", "CPM", "Bounce Rate"};

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
