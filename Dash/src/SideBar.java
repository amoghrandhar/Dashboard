import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import javafx.application.Platform;

import javax.swing.*;
import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DateFormatter;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.function.Predicate;

public class SideBar extends JPanel {

	Dashboard dashboard;
	DataAnalytics dataAnalytics;

	JPanel filePanel, menuPanel, comparePanel;

	JButton importButton, exportButton, updateButton, resetButton, settingsButton;
	JPopupMenu popUpMenu, settingsPopUp;
	JMenuItem printItem, pngItem, jpegItem, piesItem, multiItem;
	JMenuItem lightTheme, darkTheme;
	Calendar calendar;
	JDatePickerImpl startPicker, endPicker;
	UtilDateModel dateModel, dateModel2;
	SpinnerDateModel timeModel, timeModel2;
	JToggleButton male, female;
	JCheckBox ageLabel, incomeLabel;
	JSlider ageSlider, incomeSlider;
	ModifiedButtonGroup sexGroup, contextGroup;
	JLabel pagesLabel, timeLabel;
	JCheckBox pagesCheckBox, timeCheckBox;
	JSpinner pagesSpinner, timeSpinner;

	ImageIcon importIcon, exportIcon, settingsIcon;
	Color SECONDARY;

	JLabel compareLabel, selectedLabel;
	JToggleButton compareButton;
	JComboBox compareBox;
	Integer selectedSeries;
	Series series1, series2;

	Box accordion;
	JPanel updatePanel;

	String[] colours;

	public SideBar(Dashboard dashboard, DataAnalytics dataAnalytics, String[] colours) {

		this.dashboard = dashboard;
		this.dataAnalytics = dataAnalytics;
		this.selectedSeries = 1;
		this.series1 = new Series();
		this.series2 = new Series();
		init(colours);

	}

	public void init(String[] colours) {

		this.colours = colours;
		SECONDARY = Color.decode(colours[3]);
		//this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

		filePanel = new JPanel();
		filePanel.setLayout(new GridBagLayout());
		filePanel.setBorder(new EmptyBorder(16, 0, 16, 0));

		menuPanel = new JPanel();
		menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.PAGE_AXIS));
		menuPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.gray));
		menuPanel.setPreferredSize(new Dimension(200, 500));

		// GridBagLayout Constraints

		GridBagConstraints importC = new GridBagConstraints();
		GridBagConstraints exportC = new GridBagConstraints();

		importC.gridx = 0;
		importC.gridy = 0;
		importC.insets = new Insets(4, 0, 4, 0);

		exportC.gridx = 0;
		exportC.gridy = 1;
		exportC.insets = new Insets(4, 0, 4, 0);

		// File Panel (Top)

		importIcon = new ImageIcon(getClass().getResource("plus.png"));
		Image img = importIcon.getImage();
		Image newimg = img.getScaledInstance(16, 16, java.awt.Image.SCALE_SMOOTH);
		importIcon = new ImageIcon(newimg);

		importButton = new JButton(" Import ", importIcon);
		importButton.setFont(new Font("", Font.PLAIN, 12));
		importButton.setPreferredSize(new Dimension(120, 46));
		importButton.setFocusable(false);
		importButton.setToolTipText("Import your campaign CSV files");
		importButton.addActionListener(new ImportListener(dashboard));
		filePanel.add(importButton, importC);

		exportIcon = new ImageIcon(getClass().getResource("download.png"));
		Image img2 = exportIcon.getImage();
		Image newimg2 = img2.getScaledInstance(16, 16, java.awt.Image.SCALE_SMOOTH);
		exportIcon = new ImageIcon(newimg2);

		exportButton = new JButton(" Export ", exportIcon);
		exportButton.setFont(new Font("", Font.PLAIN, 12));
		exportButton.setPreferredSize(new Dimension(120, 46));
		exportButton.setFocusable(false);
		exportButton.setEnabled(false);

		pngItem = new JMenuItem("Export as Image");
		pngItem.addActionListener(new PopupListener(dashboard));
		printItem = new JMenuItem("Print main graph");
		printItem.addActionListener(new PopupListener(dashboard));
		multiItem = new JMenuItem("Print graph and table");
		multiItem.addActionListener(new PopupListener(dashboard));
		piesItem = new JMenuItem("Print pie charts");
		piesItem.addActionListener(new PopupListener(dashboard));

		popUpMenu = new JPopupMenu("Menu");
		popUpMenu.add(pngItem);
		//popUpMenu.add(jpegItem);
		popUpMenu.addSeparator();
		popUpMenu.add(printItem);
		popUpMenu.add(multiItem);
		popUpMenu.add(piesItem);

		exportButton.addActionListener(new ExportListener(dashboard));

		// Menu Panel (Bottom)

		GridBagConstraints gc1 = new GridBagConstraints();
		GridBagConstraints gc2 = new GridBagConstraints();
		GridBagConstraints gc3 = new GridBagConstraints();
		GridBagConstraints gc4 = new GridBagConstraints();

		gc1.gridy = 0;
		gc1.anchor = GridBagConstraints.LINE_START;
		gc1.insets = new Insets(4, 0, 4, 0);

		gc2.gridy = 0;
		gc2.anchor = GridBagConstraints.LINE_END;
		gc2.insets = new Insets(4, 0, 4, 0);

		gc3.gridy = 1;
		gc3.anchor = GridBagConstraints.LINE_START;
		gc3.insets = new Insets(4, 0, 4, 0);

		gc4.gridy = 1;
		gc4.anchor = GridBagConstraints.LINE_END;
		gc4.insets = new Insets(4, 0, 4, 0);

		compareLabel = new JLabel("Comparison:   ");

		compareButton = new JToggleButton("OFF");
		compareButton.setPreferredSize(new Dimension(50, compareButton.getPreferredSize().height));
		compareButton.setFocusable(false);
		compareButton.setEnabled(false);
		compareButton.addActionListener(new ComparingListener(dashboard));

		String[] graphChoices = {"Series 1", "Series 2"};

		selectedLabel = new JLabel("Selected:   ");

		compareBox = new JComboBox(graphChoices);
		compareBox.setPrototypeDisplayValue("XXXXXXX");
		compareBox.setEnabled(true);
		compareBox.addActionListener(new ComparingBoxListener());

		comparePanel = new JPanel();
		comparePanel.setLayout(new GridBagLayout());
		Border bb1 = BorderFactory.createEmptyBorder(6, 0, 6, 0);
		Border bb2 = (BorderFactory.createMatteBorder(0, 0, 1, 0, Color.gray));
		comparePanel.setBorder(new CompoundBorder(bb2, bb1));
		comparePanel.setMaximumSize(new Dimension(200, 140));
		comparePanel.add(compareLabel, gc1);
		comparePanel.add(compareButton, gc2);
		comparePanel.add(selectedLabel, gc3);
		comparePanel.add(compareBox, gc4);

		selectedLabel.setVisible(false);
		compareBox.setVisible(false);

		resetButton = new JButton("Reset");
		resetButton.setFocusable(false);
		resetButton.setEnabled(false);
		resetButton.addActionListener(new ResetListener(this));

		updateButton = new JButton("Update");
		updateButton.setFocusable(false);
		updateButton.setEnabled(false);
		updateButton.addActionListener(new UpdateListener(this));

		updatePanel = new JPanel();
		updatePanel.setLayout(new FlowLayout());
		updatePanel.add(resetButton);
		updatePanel.add(updateButton);
		Border b1 = BorderFactory.createEmptyBorder(6, 0, 6, 0);
		Border b2 = (BorderFactory.createMatteBorder(0, 0, 1, 0, Color.gray));
		updatePanel.setBorder(new CompoundBorder(b2, b1));
		updatePanel.setMaximumSize(new Dimension(200, 100));

		accordion = Box.createVerticalBox();
		accordion.setOpaque(true);

		List<AbstractExpansionPanel> titledPanes = makeList();

		for (AbstractExpansionPanel p : titledPanes) {
			p.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.gray));
			p.label.addMouseListener(new TitledPaneAdapter(p, titledPanes));
			accordion.add(p);
		}

		accordion.add(Box.createVerticalGlue());
		accordion.setPreferredSize(new Dimension(200, 499));

		comparePanel.setBackground(Color.decode(colours[0]));
		updatePanel.setBackground(Color.decode(colours[0]));
		accordion.setBackground(Color.decode(colours[0]));
		filePanel.setBackground(Color.decode(colours[0]));
		menuPanel.setBackground(Color.decode(colours[0]));
		this.setBackground(Color.decode(colours[0]));

		// Frame

		filePanel.add(importButton, importC);
		filePanel.add(exportButton, exportC);
		filePanel.setMaximumSize(new Dimension(200, 200));

		menuPanel.add(comparePanel);
		menuPanel.add(updatePanel);
		menuPanel.add(accordion);

		this.setLayout(new BorderLayout());
		this.add(filePanel, BorderLayout.PAGE_START);
		this.add(menuPanel, BorderLayout.LINE_END);
		JPanel buttonPanel = new JPanel();

		importIcon = new ImageIcon(getClass().getResource("cog.png"));
		Image img3 = importIcon.getImage();
		Image newimg3 = img3.getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH);
		importIcon = new ImageIcon(newimg3);

		buttonPanel.setBackground(UIManager.getColor("this.background"));
		buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		settingsButton = new JButton(importIcon);
		settingsButton.setBorder(null);
		settingsButton.setContentAreaFilled(false);
		settingsButton.setFocusable(false);
		settingsButton.addActionListener(new SettingsListener(dashboard));
		buttonPanel.add(settingsButton);
		this.add(buttonPanel, BorderLayout.SOUTH);

		lightTheme = new JMenuItem("Light Theme");
		lightTheme.addActionListener(new SettingsPopupListener(dashboard));
		darkTheme = new JMenuItem("Dark Theme");
		darkTheme.addActionListener(new SettingsPopupListener(dashboard));
		settingsPopUp = new JPopupMenu("Menu");
		settingsPopUp.add(lightTheme);
		settingsPopUp.addSeparator();
		settingsPopUp.add(darkTheme);  
	}

	private List<AbstractExpansionPanel> makeList() {

		return Arrays.asList(

				new AbstractExpansionPanel(" Date Range") {

					public JPanel makePanel() {

						JPanel pnl = new JPanel(new GridLayout(0, 1));

						JLabel startLabel = new JLabel("Start");
						JLabel endLabel = new JLabel("End");

						Properties p = new Properties();
						p.put("text.today", "Today");
						p.put("text.month", "Month");
						p.put("text.year", "Year");

						dateModel = new UtilDateModel();
						dateModel2 = new UtilDateModel();
						JDatePanelImpl datePanel = new JDatePanelImpl(dateModel, p);
						JDatePanelImpl datePanel2 = new JDatePanelImpl(dateModel2, p);
						startPicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
						endPicker = new JDatePickerImpl(datePanel2, new DateLabelFormatter());

						calendar = Calendar.getInstance();
						calendar.set(Calendar.HOUR_OF_DAY, 24);
						calendar.set(Calendar.MINUTE, 0);
						calendar.set(Calendar.SECOND, 0);

						timeModel = new SpinnerDateModel();
						timeModel.setValue(calendar.getTime());

						timeModel2 = new SpinnerDateModel();
						timeModel2.setValue(calendar.getTime());

						JSpinner spinner = new JSpinner(timeModel);
						JSpinner spinner2 = new JSpinner(timeModel2);

						JSpinner.DateEditor editor = new JSpinner.DateEditor(spinner, "HH:mm:ss");
						JSpinner.DateEditor editor2 = new JSpinner.DateEditor(spinner2, "HH:mm:ss");

						DateFormatter formatter = (DateFormatter) editor.getTextField().getFormatter();
						formatter.setAllowsInvalid(false);
						formatter.setOverwriteMode(true);

						DateFormatter formatter2 = (DateFormatter) editor2.getTextField().getFormatter();
						formatter2.setAllowsInvalid(false);
						formatter2.setOverwriteMode(true);

						spinner.setEditor(editor);
						spinner2.setEditor(editor2);

						startPicker.setBackground(SECONDARY);
						endPicker.setBackground(SECONDARY);

						pnl.add(startLabel);
						pnl.add(startPicker);
						pnl.add(spinner);
						pnl.add(endLabel);
						pnl.add(endPicker);
						pnl.add(spinner2);

						return pnl;

					}

				},
				new AbstractExpansionPanel(" Audience Segments") {

					public JPanel makePanel() {

						JPanel pnl = new JPanel(new GridLayout(0, 1));

						// ######### Male or Female buttons #########

						male = new JToggleButton("Male");
						female = new JToggleButton("Female");

						sexGroup = new ModifiedButtonGroup();
						sexGroup.add(male);
						sexGroup.add(female);

						JPanel buttonPanel = new JPanel(new FlowLayout());
						buttonPanel.setBackground(SECONDARY);
						buttonPanel.add(male);
						buttonPanel.add(female);

						// ######### Age Group SLider #########

						//JLabel ageLabel = new JLabel("Age");
						ageLabel = new JCheckBox("Age");

						JLabel age0 = new JLabel("<25");
						JLabel age1 = new JLabel("25-34");
						JLabel age2 = new JLabel("35-44");
						JLabel age3 = new JLabel("45-54");
						JLabel age4 = new JLabel(">55");

						Font labelFont = new Font("", Font.PLAIN, 10);

						age0.setFont(labelFont);
						age1.setFont(labelFont);
						age2.setFont(labelFont);
						age3.setFont(labelFont);
						age4.setFont(labelFont);

						Hashtable<Integer, JLabel> labels = new Hashtable<Integer, JLabel>();
						labels.put(0, age0);
						labels.put(1, age1);
						labels.put(2, age2);
						labels.put(3, age3);
						labels.put(4, age4);

						ageSlider = new JSlider(0, 4);
						ageSlider.setMinorTickSpacing(1);
						ageSlider.setPaintTicks(true);
						ageSlider.setPaintLabels(true);
						ageSlider.setSnapToTicks(true);
						ageSlider.setLabelTable(labels);


						// ######### Income SLider #########

						//JLabel incomeLabel = new JLabel("Income");
						incomeLabel = new JCheckBox("Income");

						Hashtable<Integer, JLabel> labels2 = new Hashtable<Integer, JLabel>();
						labels2.put(0, new JLabel("Low"));
						labels2.put(1, new JLabel("Medium"));
						labels2.put(2, new JLabel("High"));

						incomeSlider = new JSlider(0, 2);
						incomeSlider.setMinorTickSpacing(1);
						incomeSlider.setPaintTicks(true);
						incomeSlider.setPaintLabels(true);
						incomeSlider.setSnapToTicks(true);
						incomeSlider.setLabelTable(labels2);

						// Add listener to update display.
						//				        ageSlider.addChangeListener(new ChangeListener() {
						//				            public void stateChanged(ChangeEvent e) {
						//				                RangeageSlider ageSlider = (RangeageSlider) e.getSource();
						//				                rangeageSliderValue1.setText(String.valueOf(ageSlider.getValue()));
						//				                rangeageSliderValue2.setText(String.valueOf(ageSlider.getUpperValue()));
						//				            }
						//				        });

						pnl.add(buttonPanel);
						pnl.add(ageLabel);
						pnl.add(ageSlider);
						pnl.add(incomeLabel);
						pnl.add(incomeSlider);

						return pnl;

					}

				},
				new AbstractExpansionPanel(" Context") {

					public JPanel makePanel() {

						JPanel pnl = new JPanel(new GridLayout(0, 1));

						contextGroup = new ModifiedButtonGroup();
						JRadioButton b1 = new JRadioButton("News");
						JRadioButton b2 = new JRadioButton("Shopping");
						JRadioButton b3 = new JRadioButton("Social Media");
						JRadioButton b5 = new JRadioButton("Blog");
						JRadioButton b6 = new JRadioButton("Hobbies");
						JRadioButton b7 = new JRadioButton("Travel");

						for (JRadioButton b : Arrays.asList(b1, b2, b3, b5, b6, b7)) {
							b.setBorder(BorderFactory.createEmptyBorder(4, 0, 4, 0));
							b.setOpaque(false);
							contextGroup.add(b);
							pnl.add(b);
						}

						return pnl;

					}

				},
				new AbstractExpansionPanel(" Define Bounce") {

					public JPanel makePanel() {

						JPanel pnl = new JPanel(new GridBagLayout());

						GridBagConstraints pagesCheckBoxC = new GridBagConstraints();
						GridBagConstraints pagesSpinnerC = new GridBagConstraints();
						GridBagConstraints timeCheckBoxC = new GridBagConstraints();
						GridBagConstraints timeSpinnerC = new GridBagConstraints();

						pagesCheckBoxC.gridx = 0;
						pagesCheckBoxC.gridy = 0;
						pagesCheckBoxC.anchor = GridBagConstraints.LINE_START;
						pagesCheckBoxC.insets = new Insets(0, 0, 0, 24);

						pagesSpinnerC.gridx = 1;
						pagesSpinnerC.gridy = 0;

						timeCheckBoxC.gridx = 0;
						timeCheckBoxC.gridy = 1;
						timeCheckBoxC.anchor = GridBagConstraints.WEST;
						pagesCheckBoxC.insets = new Insets(0, 0, 0, 24);

						timeSpinnerC.gridx = 1;
						timeSpinnerC.gridy = 1;

						SpinnerModel spinnerModel = new SpinnerNumberModel(0, 0, 60, 1);

						pagesCheckBox = new JCheckBox("Pages visited");
						pagesSpinner = new JSpinner(spinnerModel);

						JComponent editor = pagesSpinner.getEditor();
						JFormattedTextField tf = ((JSpinner.DefaultEditor) editor).getTextField();
						tf.setColumns(2);

						SpinnerModel spinnerModel2 = new SpinnerNumberModel(0, 0, 1000, 1);

						timeCheckBox = new JCheckBox("Time spent (s)");
						timeSpinner = new JSpinner(spinnerModel2);

						JComponent editor2 = timeSpinner.getEditor();
						JFormattedTextField tf2 = ((JSpinner.DefaultEditor) editor2).getTextField();
						tf2.setColumns(2);

						pnl.add(pagesCheckBox, pagesCheckBoxC);
						pnl.add(pagesSpinner, pagesSpinnerC);
						pnl.add(timeCheckBox, timeCheckBoxC);
						pnl.add(timeSpinner, timeSpinnerC);

						return pnl;

					}

				}

				);

	}

	public Date getChosenStartDate() {

		if (dateModel.getValue() != null) {

			Calendar temp = Calendar.getInstance();
			//Date dA = new SimpleDateFormat("dd:hh:mm").parse(source);
			temp.setTime(timeModel.getDate());

			Calendar date = Calendar.getInstance();
			date.set(Calendar.YEAR, dateModel.getYear());
			date.set(Calendar.MONTH, dateModel.getMonth());
			date.set(Calendar.DAY_OF_MONTH, dateModel.getDay());
			date.set(Calendar.HOUR_OF_DAY, temp.get(Calendar.HOUR_OF_DAY));
			date.set(Calendar.MINUTE, temp.get(Calendar.MINUTE));
			date.set(Calendar.SECOND, temp.get(Calendar.SECOND));

			return date.getTime();

		} else return null;

	}

	public Date getChosenEndDate() {

		if (dateModel2.getValue() != null) {

			Calendar temp = Calendar.getInstance();
			temp.setTime(timeModel2.getDate());

			Calendar date = Calendar.getInstance();
			date.set(Calendar.YEAR, dateModel2.getYear());
			date.set(Calendar.MONTH, dateModel2.getMonth());
			date.set(Calendar.DAY_OF_MONTH, dateModel2.getDay());
			date.set(Calendar.HOUR_OF_DAY, temp.get(Calendar.HOUR_OF_DAY));
			date.set(Calendar.MINUTE, temp.get(Calendar.MINUTE));
			date.set(Calendar.SECOND, temp.get(Calendar.SECOND));

			return date.getTime();

		} else return null;

	}

	public int getChosenAge() {

		if (ageLabel.isSelected())
			return ageSlider.getValue();

		else return -1;

	}

	public int getChosenIncome() {

		if (incomeLabel.isSelected())
			return incomeSlider.getValue();

		else return -1;

	}

	public Boolean getChosenSex() {

		if (male.isSelected())
			return true;

		if (female.isSelected())
			return false;

		else return null;

	}

	public String getChosenContext() {

		Enumeration<AbstractButton> e = contextGroup.getElements();

		while (e.hasMoreElements()) {
			AbstractButton b = e.nextElement();
			if (b.isSelected()) return b.getText();
		}

		return null;

	}

	public int getChosenPages(){

		if(pagesCheckBox.isSelected())
			return (int) pagesSpinner.getValue();

		else return dashboard.DEFAULT_BOUNCE_PAGES_PROP;

	}

	public int getChosenTime(){

		if(timeCheckBox.isSelected())
			return (int) timeSpinner.getValue();

		else return dashboard.DEFAULT_BOUNCE_TIME_PROP;

	}

	public int getSeriesBouncePages(Series series){

		return series.getBouncePages();

	}

	public int getSeriesBounceTime(Series series){

		return series.getBounceTime();

	}

	public void saveFilters(Series series){

		series.setStartDate(getChosenStartDate());
		series.setEndDate(getChosenEndDate());
		series.setGender(getChosenSex());
		series.setAgeGroup(getChosenAge());
		series.setIncome(getChosenIncome());
		series.setContext(getChosenContext());
		series.setBouncePages(getChosenPages());
		series.setBounceTime(getChosenTime());

	}

	abstract class AbstractExpansionPanel extends JPanel {

		public final JLabel label;
		public final JPanel panel;
		private final String title;

		public AbstractExpansionPanel(String title) {

			super(new BorderLayout());
			this.title = title;

			label = new JLabel("\u25BA " + title) {
				protected void paintComponent(Graphics g) {
					Graphics2D g2 = (Graphics2D) g.create();
					g2.setPaint(new GradientPaint(new Point(0, 0), Color.decode(colours[1]),
							new Point(0, getHeight()), Color.decode(colours[2])));
					g2.fillRect(0, 0, getWidth(), getHeight());
					g2.setColor(Color.GRAY);
					g2.setStroke(new BasicStroke(2));
					g2.drawLine(0, getHeight(), getWidth(), getHeight());
					g2.dispose();
					super.paintComponent(g);
				}
			};
			label.setBorder(BorderFactory.createEmptyBorder(12, 10, 12, 10));

			this.add(label, BorderLayout.NORTH);

			panel = makePanel();
			panel.setVisible(false);
			panel.setOpaque(true);
			panel.setBackground(Color.decode(colours[3]));
			panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

			this.add(panel);

		}

		public abstract JPanel makePanel();

		public Dimension getPreferredSize() {

			Dimension d = label.getPreferredSize();
			if (panel.isVisible()) {
				d.height += panel.getPreferredSize().height;
			}
			return d;

		}

		public Dimension getMaximumSize() {

			Dimension d = getPreferredSize();
			d.width = Short.MAX_VALUE;
			return d;

		}

		protected void initPanel() {

			panel.setVisible(!panel.isVisible());
			label.setText(String.format("%s %s", panel.isVisible() ? "\u25BD" : "\u25BA", title));
			revalidate();

			EventQueue.invokeLater(new Runnable() {
				public void run() {
					panel.scrollRectToVisible(panel.getBounds());
				}
			});

		}

	}

	// Controls expansion of titled panes in the SideBar
	class TitledPaneAdapter extends MouseAdapter {

		List<AbstractExpansionPanel> titledPanes;
		AbstractExpansionPanel clickedPane;

		public TitledPaneAdapter(AbstractExpansionPanel clickedPane, List<AbstractExpansionPanel> titledPanes) {

			this.clickedPane = clickedPane;
			this.titledPanes = titledPanes;

		}

		public void mousePressed(MouseEvent e) {

			if (clickedPane.panel.isVisible()) {
				clickedPane.initPanel();
			} else {

				for (AbstractExpansionPanel titledPane : titledPanes) {
					if (titledPane.panel.isVisible())
						titledPane.initPanel();
				}

				clickedPane.initPanel();

			}

		}

	}

	// Opens a new frame to import files
	class ImportListener implements ActionListener {

		Dashboard dashboard;

		public ImportListener(Dashboard dashboard) {

			this.dashboard = dashboard;

		}

		public void actionPerformed(ActionEvent e) {

			Import importFrame = new Import("Import Files", dashboard, colours);
			importFrame.init();
			importFrame.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					dashboard.sidebar.importButton.setEnabled(true);
				}
			});

		}

	}

	//Opens a new frame to import files
	class ExportListener implements ActionListener {

		Dashboard dashboard;

		public ExportListener(Dashboard dashboard) {

			this.dashboard = dashboard;

		}

		public void actionPerformed(ActionEvent e) {

			if (exportButton.getText() == " Export ") {
				dashboard.sidebar.popUpMenu.show(dashboard.sidebar.exportButton,
						dashboard.sidebar.exportButton.getWidth(), 2);

			} else {          	            		
				this.dashboard.content.screenShotMode = false;  
				this.dashboard.content.glassPanel.setVisible(false);
				this.dashboard.content.glassPanel.repaint();
				exportButton.setText(" Export ");
				exportButton.setIcon(exportIcon);
				exportButton.setBackground(UIManager.getColor("Button.background"));
			}
		}

	}

	class PopupListener implements ActionListener {

		Dashboard dashboard;

		public PopupListener(Dashboard dashboard) {

			this.dashboard = dashboard;

		}

		public void actionPerformed(ActionEvent event) {

			if (event.getSource() == dashboard.sidebar.pngItem) {
				this.dashboard.content.screenShotMode = true;  
				this.dashboard.content.table.setCellSelectionEnabled(false);
				this.dashboard.content.glassPanel.setOpaque(false);  
				this.dashboard.content.dashboard.setGlassPane(this.dashboard.content.glassPanel);  
				this.dashboard.content.glassPanel.setVisible(true);
				this.dashboard.content.glassPanel.repaint();
				exportButton.setText(" Cancel ");
				exportButton.setIcon(null);
				exportButton.setBackground(Color.decode("#c54343"));
			}
			
			if (event.getSource() == dashboard.sidebar.printItem) 
				PrintSupport.printComponent(dashboard.content.chart);

			if (event.getSource() == dashboard.sidebar.multiItem) 
				PrintSupport.printComponent(dashboard.content.tab1);

			if (event.getSource() == dashboard.sidebar.piesItem)
				PrintSupport.printComponent(dashboard.content.tab2);

		}

	}

	class SettingsListener implements ActionListener {

		Dashboard dashboard;

		public SettingsListener(Dashboard dashboard) {

			this.dashboard = dashboard;

		}

		public void actionPerformed(ActionEvent e) {

			dashboard.sidebar.settingsPopUp.show(dashboard.sidebar, dashboard.sidebar.getX() + 30, dashboard.sidebar.getHeight() - 60);
		}
	}

	class SettingsPopupListener implements ActionListener {

		Dashboard dashboard;

		public SettingsPopupListener(Dashboard dashboard) {

			this.dashboard = dashboard;
		}

		public void actionPerformed(ActionEvent event) {

			Object[] options = {"Yes", "No"};
			int userResponsoe = JOptionPane.showOptionDialog(dashboard,
					"Changing the theme will restart the application.\nDo you want to continue?", "Warning",
					JOptionPane.YES_NO_OPTION,
					JOptionPane.WARNING_MESSAGE,
					null,  
					options, 
					options[0]);
			
	        if (userResponsoe == JOptionPane.YES_OPTION) {
	        	
	    		if (event.getSource() == dashboard.sidebar.lightTheme) {
					SwingUtilities.invokeLater(
							() -> {
								try {
									for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
										if ("Nimbus".equals(info.getName())) {
											UIManager.setLookAndFeel(info.getClassName());
											break;
										}
										UIManager.setLookAndFeel(
												UIManager.getCrossPlatformLookAndFeelClassName());
									}

									Dashboard frame = new Dashboard("Ad Dashboard");
									frame.init(new String[] {"#c4c7cc", "#f5f5f5", "#d5d5d5", "#fafafa"});

								} catch (IllegalAccessException | InstantiationException | ClassNotFoundException | UnsupportedLookAndFeelException e) {
									e.printStackTrace();
								}
							}
							);
					dashboard.dispose();
				}

				if (event.getSource() == dashboard.sidebar.darkTheme) {
					//                try {
					//                    UIManager.setLookAndFeel("com.jtattoo.plaf.hifi.HiFiLookAndFeel");
					//
					//                } catch (ClassNotFoundException | InstantiationException
					//                        | IllegalAccessException | UnsupportedLookAndFeelException e) {
					//                    e.printStackTrace();
					//                }
					//                SwingUtilities.updateComponentTreeUI(dashboard);


					SwingUtilities.invokeLater(
							() -> {
								try {
									UIManager.setLookAndFeel("com.jtattoo.plaf.hifi.HiFiLookAndFeel");
									Dashboard frame = new Dashboard("Ad Dashboard");
									frame.init(new String[] {"#0d0d0c", "#1a1818", "#524e4e", "#242424"});

								} catch (IllegalAccessException | InstantiationException | ClassNotFoundException | UnsupportedLookAndFeelException e) {
									e.printStackTrace();
								}
							}
							);
					dashboard.dispose();
				}
	        	
	        } else {
	        	
	        }
		}
	}

	class ComparingListener implements ActionListener{

		Dashboard dashboard;

		public ComparingListener(Dashboard dashboard){

			this.dashboard = dashboard;

		}

		@Override
		public void actionPerformed(ActionEvent e) {

			if(compareButton.isSelected()){
				compareButton.setText("ON");
				selectedLabel.setVisible(true);
				compareBox.setVisible(true);
				dashboard.updateComparing(true);
			}

			else{
				selectedSeries = 1;
				compareButton.setText("OFF");
				selectedLabel.setVisible(false);
				compareBox.setVisible(false);
				dashboard.updateComparing(false);
			}

			Platform.runLater(() -> {
				dashboard.updateGraph(dashboard.content.graphChoiceBox.getSelectedIndex());
			});

		}

	}

	class ComparingBoxListener implements ActionListener{

		public void actionPerformed(ActionEvent e) {	

			Series series = new Series();

			selectedSeries = compareBox.getSelectedIndex() + 1;

			if(selectedSeries == 1)
				series = series1;
			if(selectedSeries == 2)
				series = series2;

			// Start date
			if(series.getStartDate() != null){

				dateModel.setValue(series.getStartDate());

				Calendar temp = Calendar.getInstance();
				temp.setTime(series.getStartDate());
				timeModel.setValue(temp.getTime());

			}
			else{ 
				dateModel.setValue(null);
				timeModel.setValue(calendar.getTime());
			}

			// End date
			if(series.getEndDate() != null){

				dateModel2.setValue(series.getEndDate());

				Calendar temp = Calendar.getInstance();
				temp.setTime(series.getEndDate());
				timeModel2.setValue(temp.getTime());

			}
			else{
				dateModel2.setValue(null);
				timeModel2.setValue(calendar.getTime());
			}

			// Gender
			if(series.getGender() != null){
				if(series.getGender() == true)
					male.setSelected(true);
				if(series.getGender() == false)
					female.setSelected(true);
			}
			else{
				male.setSelected(false);
				female.setSelected(false);
			}

			// Age Group
			if(series.getAgeGroup() != -1){
				ageLabel.setSelected(true);
				ageSlider.setValue(series.getAgeGroup());
			}
			else ageLabel.setSelected(false);

			// Income
			if(series.getIncome() != -1){
				incomeLabel.setSelected(true);
				incomeSlider.setValue(series.getIncome());
			}
			else incomeLabel.setSelected(false);

			// Context
			if(series.getContext() != null){
				for (Enumeration eRadio=contextGroup.getElements(); eRadio.hasMoreElements(); ) {
					JRadioButton radioButton = (JRadioButton)eRadio.nextElement();
					if (radioButton.getText() == series.getContext()) {
						contextGroup.setSelected(radioButton.getModel(), true);
					}
				}
			}
			else contextGroup.clearSelection();

			// Bounce Pages
			if(series.getBouncePages() != -1){
				pagesCheckBox.setSelected(true);
				pagesSpinner.setValue(series.getBouncePages());
			}
			else pagesCheckBox.setSelected(false);

			// Bounce Time
			if(series.getBounceTime() != -1){
				timeCheckBox.setSelected(true);
				timeSpinner.setValue(series.getBounceTime());
			}
			else timeCheckBox.setSelected(false);

		}

	}

	// Collects all filter options and updates the graphs and metrics
	class UpdateListener implements ActionListener {

		SideBar sidebar;

		public UpdateListener(SideBar sidebar) {

			this.sidebar = sidebar;

		}

		public void actionPerformed(ActionEvent e) {

			Date startDate = sidebar.getChosenStartDate();
			Date endDate = sidebar.getChosenEndDate();
			Boolean gender = sidebar.getChosenSex();
			int ageGroup = sidebar.getChosenAge();
			int income = sidebar.getChosenIncome();
			String context = sidebar.getChosenContext();
			int pages = sidebar.getChosenPages();
			int time = sidebar.getChosenTime();

			//Start Date Predicates
			Predicate<ClickLog> clickLogStartDatePredicate = click -> true;
			Predicate<ImpressionLog> impressionLogStartDatePredicate = imp -> true;
			Predicate<ServerLog> serverLogStartDatePredicate = ser -> true;
			
			if (startDate != null) {
				clickLogStartDatePredicate = click -> click.getDate().after(startDate);
				impressionLogStartDatePredicate = imp -> imp.getDate().after(startDate);
				serverLogStartDatePredicate = ser -> ser.getStartDate().after(startDate);
			}

			//End Date Predicates
			Predicate<ClickLog> clickLogEndDatePredicate = ser -> true;
			Predicate<ImpressionLog> impressionLogEndDatePredicate = ser -> true;
			Predicate<ServerLog> serverLogEndDatePredicate = ser -> true;
			
			if (endDate != null) {
				
				Calendar c = Calendar.getInstance();
				c.setTime(endDate);
				c.add(Calendar.DATE, 1);
				Date newDate = c.getTime();
				
				clickLogEndDatePredicate = click -> click.getDate().before(newDate);
				impressionLogEndDatePredicate = imp -> imp.getDate().before(newDate);
				serverLogEndDatePredicate = ser -> ser.getStartDate().before(newDate);
				
			}

			//Gender Predicate
			Predicate<ImpressionLog> impressionLogGenderPredicate = imp -> true;
			if (gender != null) {
				impressionLogGenderPredicate = imp -> imp.getGender() == gender;
			}

			//Age Group Predicate
			Predicate<ImpressionLog> impressionAgePredicate = imp -> true;
			if (ageGroup != -1) {
				impressionAgePredicate = imp -> imp.getAgeGroup() == ageGroup;
			}

			//Income Predicate
			Predicate<ImpressionLog> impressionIncomePredicate = imp -> true;
			;
			if (income != -1) {
				impressionIncomePredicate = imp -> imp.getIncomeGroup() == income;
			}

			//Context Predicate
			Predicate<ImpressionLog> impressionContextPredicate = imp -> true;
			if (context != null) {
				impressionContextPredicate = imp -> imp.getContext().equals(context);
			}

			dashboard.resetLogs();
			ArrayList<ImpressionLog> impressionLogs = null;
			ArrayList<ClickLog> clickLogArrayList = null;
			ArrayList<ServerLog> serverLogArrayList = null;

			// Actually unnecessary because logs are reset and both have identical original logs

			if(selectedSeries == 1){
				impressionLogs = dashboard.getImpressionLogs();
				clickLogArrayList = dashboard.getClickLogs();
				serverLogArrayList = dashboard.getServerLogs();  
			}
			if(selectedSeries == 2){ 	
				impressionLogs = dashboard.getImpressionLogs2();
				clickLogArrayList = dashboard.getClickLogs2();
				serverLogArrayList = dashboard.getServerLogs2();
			}

			impressionLogs = (ArrayList <ImpressionLog>) DataAnalytics.filterImpressionLogs(
					impressionLogStartDatePredicate,
					impressionLogEndDatePredicate,
					impressionLogGenderPredicate,
					impressionAgePredicate,
					impressionIncomePredicate,
					impressionContextPredicate,
					impressionLogs);

			HashSet<Double> idSet = new HashSet<Double>();
			for (ImpressionLog impressionLog : impressionLogs) {
				idSet.add(impressionLog.getID());
			}

			clickLogArrayList = (ArrayList <ClickLog>) DataAnalytics
					.filterClickLogs(
							clickLogStartDatePredicate, 
							clickLogEndDatePredicate, 
							clickLogArrayList, 
							idSet);

			serverLogArrayList = (ArrayList<ServerLog>) DataAnalytics
					.filterServerLogs(serverLogStartDatePredicate,serverLogEndDatePredicate,
							serverLogArrayList,idSet);

			// For comparison requirement

			if(selectedSeries == 1){
				saveFilters(series1);
				dashboard.updateLogs(clickLogArrayList,impressionLogs,serverLogArrayList);
			}
			if(selectedSeries == 2){
				dashboard.updateLogs2(clickLogArrayList,impressionLogs,serverLogArrayList);
				saveFilters(series2);
			}
			
			dashboard.updateMetrics(pages , time);

			Platform.runLater(() -> {
				dashboard.updateGraph(dashboard.content.graphChoiceBox.getSelectedIndex());
			});

			Platform.runLater(() -> {
				dashboard.updatePieCharts();
			});

		}

	}


	class ResetListener implements ActionListener {

		SideBar sidebar;

		public ResetListener(SideBar sidebar) {

			this.sidebar = sidebar;

		}

		public void actionPerformed(ActionEvent e) {

			sidebar.dateModel.setValue(null);
			sidebar.timeModel.setValue(sidebar.calendar.getTime());
			sidebar.dateModel2.setValue(null);
			sidebar.timeModel2.setValue(sidebar.calendar.getTime());
			sidebar.sexGroup.clearSelection();
			sidebar.ageLabel.setSelected(false);
			sidebar.incomeLabel.setSelected(false);
			sidebar.contextGroup.clearSelection();
			sidebar.pagesCheckBox.setSelected(false);
			sidebar.pagesSpinner.setValue(0);
			sidebar.timeCheckBox.setSelected(false);
			sidebar.timeSpinner.setValue(0);

			if(selectedSeries == 1)
				saveFilters(series1);
			if(selectedSeries == 2)
				saveFilters(series2);

			dashboard.resetLogs();
			dashboard.updateMetrics(dashboard.DEFAULT_BOUNCE_PAGES_PROP,dashboard.DEFAULT_BOUNCE_TIME_PROP);

			Platform.runLater(() -> {
				dashboard.updateGraph(dashboard.content.graphChoiceBox.getSelectedIndex());
			});

			Platform.runLater(() -> {
				dashboard.updatePieCharts();
			});

		}

	}

	// ButtonGroup that allows de-selection
	class ModifiedButtonGroup extends ButtonGroup {

		@Override
		public void setSelected(ButtonModel model, boolean selected) {

			if (selected)
				super.setSelected(model, selected);

			else clearSelection();
		}

	}

	class DateLabelFormatter extends AbstractFormatter {

		private String datePattern = "yyyy-MM-dd";
		private SimpleDateFormat dateFormatter = new SimpleDateFormat(datePattern);

		@Override
		public Object stringToValue(String text) throws ParseException {
			return dateFormatter.parseObject(text);
		}

		@Override
		public String valueToString(Object value) throws ParseException {
			if (value != null) {
				Calendar cal = (Calendar) value;
				return dateFormatter.format(cal.getTime());
			}

			return "";
		}

	}
	
}
