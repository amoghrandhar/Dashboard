import javax.swing.*;
import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.text.DateFormatter;
import org.jdatepicker.impl.*;
import java.awt.*;
import java.awt.event.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;

public class SideBar extends JPanel {

	Dashboard dashboard;

	JPanel filePanel, menuPanel;

	JButton importButton, exportButton, updateButton, resetButton;
	Calendar calendar;
	UtilDateModel dateModel, dateModel2;
	SpinnerDateModel timeModel, timeModel2;
	JToggleButton male, female;
	JRadioButton ageLabel, incomeLabel;
	JSlider ageSlider, incomeSlider;
	ModifiedButtonGroup sexGroup, contextGroup;
	JComboBox bounceBox;
	JLabel bounceLabel;
	JSpinner bounceSpinner;

	Color SECONDARY = Color.decode("#fafafa");

	public SideBar(Dashboard dashboard) {

		this.dashboard = dashboard;
		init();

	}

	public void init() {

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

		ImageIcon importIcon = new ImageIcon(getClass().getResource("plus.png"));
		Image img = importIcon.getImage();
		Image newimg = img.getScaledInstance(16, 16, java.awt.Image.SCALE_SMOOTH);
		importIcon = new ImageIcon(newimg);

		importButton = new JButton(" Import ", importIcon);
		importButton.setFont(new Font("", Font.PLAIN, 12));
		importButton.setPreferredSize(new Dimension(120, 46));
		importButton.setFocusable(false);
		importButton.addActionListener(new ImportListener(dashboard));
		filePanel.add(importButton, importC);

		ImageIcon exportIcon = new ImageIcon(getClass().getResource("download.png"));
		Image img2 = exportIcon.getImage();
		Image newimg2 = img2.getScaledInstance(16, 16, java.awt.Image.SCALE_SMOOTH);
		exportIcon = new ImageIcon(newimg2);

		exportButton = new JButton(" Export ", exportIcon);
		exportButton.setFont(new Font("", Font.PLAIN, 12));
		exportButton.setPreferredSize(new Dimension(120, 46));
		exportButton.setFocusable(false);

		// Menu Panel (Bottom)

		resetButton = new JButton ("Reset");		
		resetButton.setFocusable(false);
		resetButton.addActionListener(new ResetListener(this));
		
		updateButton = new JButton("Update");
		updateButton.setFocusable(false);
		updateButton.addActionListener(new UpdateListener(this.dashboard));
		

		JPanel updatePanel = new JPanel();
		updatePanel.setLayout(new FlowLayout());
		updatePanel.add(resetButton);
		updatePanel.add(updateButton);
		Border b1 = BorderFactory.createEmptyBorder(6, 0, 6, 0);
		Border b2 = (BorderFactory.createMatteBorder(0, 0, 1, 0, Color.gray));
		updatePanel.setBorder(new CompoundBorder(b2, b1));

		Box accordion = Box.createVerticalBox();
		accordion.setOpaque(true);

		List<AbstractExpansionPanel> titledPanes = makeList();

		for (AbstractExpansionPanel p: titledPanes) {
			p.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.gray));
			p.label.addMouseListener(new TitledPaneAdapter(p, titledPanes));
			accordion.add(p);
		}

		accordion.add(Box.createVerticalGlue());
		accordion.setPreferredSize(new Dimension(200, 499));
		
		updatePanel.setBackground(Color.decode("#c4c7cc"));
		accordion.setBackground(Color.decode("#c4c7cc"));
		filePanel.setBackground(Color.decode("#c4c7cc"));
		menuPanel.setBackground(Color.decode("#c4c7cc"));
		this.setBackground(Color.decode("#c4c7cc"));		

		// Frame

		filePanel.add(importButton, importC);
		// TODO
		//filePanel.add(exportButton, exportC);

		menuPanel.add(updatePanel);
		menuPanel.add(accordion);

		this.add(filePanel, BorderLayout.PAGE_START);
		// TODO
		//this.add(menuPanel, BorderLayout.PAGE_END);

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
						JDatePickerImpl startPicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
						JDatePickerImpl endPicker = new JDatePickerImpl(datePanel2, new DateLabelFormatter());

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

						DateFormatter formatter = (DateFormatter)editor.getTextField().getFormatter();
						formatter.setAllowsInvalid(false);
						formatter.setOverwriteMode(true);

						DateFormatter formatter2 = (DateFormatter)editor2.getTextField().getFormatter();
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
						ageLabel = new JRadioButton("Age");

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
						incomeLabel = new JRadioButton("Income");

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
						JRadioButton b3 = new JRadioButton("Social");
						JRadioButton b4 = new JRadioButton("Media");
						JRadioButton b5 = new JRadioButton("Blog");
						JRadioButton b6 = new JRadioButton("Hobbies");
						JRadioButton b7 = new JRadioButton("Travel");

						for (JRadioButton b: Arrays.asList(b1, b2, b3, b4, b5, b6, b7)) {
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

						GridBagConstraints bounceBoxC = new GridBagConstraints();
						GridBagConstraints bounceLabelC = new GridBagConstraints();
						GridBagConstraints bounceSpinnerC = new GridBagConstraints();

						bounceBoxC.gridx = 0;
						bounceBoxC.gridy = 0;
						bounceBoxC.gridwidth = 2;
						bounceBoxC.insets = new Insets(0, 0, 12, 0);

						bounceLabelC.gridx = 0;
						bounceLabelC.gridy = 1;
						bounceLabelC.insets = new Insets(0, 6, 0, 0);

						bounceSpinnerC.gridx = 1;
						bounceSpinnerC.gridy = 1;
						bounceSpinnerC.anchor = GridBagConstraints.LINE_END;

						String[] bounceOptions = { "Number of pages visited", "Time spent on website" };
						bounceBox = new JComboBox(bounceOptions);

						bounceLabel = new JLabel("Pages:");						

						SpinnerModel spinnerModel = new SpinnerNumberModel(0, 0, 60, 1);
						bounceSpinner = new JSpinner(spinnerModel);

						JComponent editor = bounceSpinner.getEditor();
						JFormattedTextField tf = ((JSpinner.DefaultEditor) editor).getTextField();
						tf.setColumns(7);					

						bounceBox.addActionListener(new ActionListener(){

							public void actionPerformed(ActionEvent e) {

								JComboBox<String> cb = (JComboBox) e.getSource();
								final int item = cb.getSelectedIndex();

								if (item == 0){
									bounceLabel.setText("Pages:");
									bounceSpinner.setValue(0);
								}
								if (item == 1){
									bounceLabel.setText("Minutes:");
									bounceSpinner.setValue(0);
								}

							}

						});

						pnl.add(bounceBox, bounceBoxC);
						pnl.add(bounceLabel, bounceLabelC);
						pnl.add(bounceSpinner, bounceSpinnerC);

						return pnl;

					}

				}

				);

	}

	public Date getChosenStartDate(){

		Calendar temp = Calendar.getInstance();
		//Date dA = new SimpleDateFormat("dd:hh:mm").parse(source); 
		temp.setTime(timeModel.getDate());

		Calendar date = Calendar.getInstance();
		date.set(Calendar.YEAR, dateModel.getDay());
		date.set(Calendar.MONTH, dateModel.getMonth());
		date.set(Calendar.DAY_OF_MONTH, dateModel.getYear());
		date.set(Calendar.HOUR_OF_DAY, temp.get(Calendar.HOUR_OF_DAY));
		date.set(Calendar.MINUTE, temp.get(Calendar.MINUTE));
		date.set(Calendar.SECOND, temp.get(Calendar.SECOND));

		return date.getTime();

	}

	public Date getChosenEndDate(){

		Calendar temp = Calendar.getInstance();
		temp.setTime(timeModel2.getDate());

		Calendar date = Calendar.getInstance();
		date.set(Calendar.YEAR, dateModel2.getDay());
		date.set(Calendar.MONTH, dateModel2.getMonth());
		date.set(Calendar.DAY_OF_MONTH, dateModel2.getYear());
		date.set(Calendar.HOUR_OF_DAY, temp.get(Calendar.HOUR_OF_DAY));
		date.set(Calendar.MINUTE, temp.get(Calendar.MINUTE));
		date.set(Calendar.SECOND, temp.get(Calendar.SECOND));

		return date.getTime();

	}

	public int getChosenAge(){

		return ageSlider.getValue();

	}

	public int getChosenIncome(){

		return incomeSlider.getValue();

	}

	public Boolean getChosenSex(){

		if(male.isSelected())
			return true;
		if(female.isSelected())
			return false;
		else return null;

	}

	public String getChosenContext(){

		return ((JLabel) contextGroup.getSelection()).getName();

	}

}

abstract class AbstractExpansionPanel extends JPanel {

	private final String title;
	public final JLabel label;
	public final JPanel panel;

	public abstract JPanel makePanel();

	public AbstractExpansionPanel(String title) {

		super(new BorderLayout());
		this.title = title;

		label = new JLabel("\u25BA " + title) {
			protected void paintComponent(Graphics g) {
				Graphics2D g2 = (Graphics2D) g.create();
				g2.setPaint(new GradientPaint(new Point(0, 0), Color.decode("#f5f5f5"),
						new Point(0, getHeight()), Color.decode("#d5d5d5")));
				g2.fillRect(0, 0, getWidth(), getHeight());
				g2.setColor(Color.GRAY);
				g2.setStroke(new BasicStroke(2));
				g2.drawLine(0, getHeight(), getWidth(), getHeight());
				g2.dispose();
				super.paintComponent(g);
			}
		};
		label.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		/*
		label.addMouseListener(new MouseAdapter() {
			@Override public void mousePressed(MouseEvent e) {
				initPanel();
			}
		});
		 */

		this.add(label, BorderLayout.NORTH);

		panel = makePanel();
		panel.setVisible(false);
		panel.setOpaque(true);
		panel.setBackground(Color.decode("#fafafa"));
		panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		this.add(panel);

	}

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
	AbstractExpansionPanel clickedPane ;

	public TitledPaneAdapter(AbstractExpansionPanel clickedPane, List<AbstractExpansionPanel> titledPanes){

		this.clickedPane = clickedPane;
		this.titledPanes = titledPanes;

	}

	public void mousePressed(MouseEvent e) {

		if(clickedPane.panel.isVisible()){
			clickedPane.initPanel();
		}
		else{

			for(AbstractExpansionPanel titledPane: titledPanes){
				if(titledPane.panel.isVisible())
					titledPane.initPanel();
			}

			clickedPane.initPanel();

		}

	}

}

// Opens a new frame to import files
class ImportListener implements ActionListener {

	Dashboard dashboard;

	public ImportListener (Dashboard dashboard){

		this.dashboard = dashboard;

	}

	public void actionPerformed(ActionEvent e) {

		Import importFrame = new Import("Import Files", dashboard);
		importFrame.init();

	}

}

// Collects all filter options and updates the graphs and metrics
class UpdateListener implements ActionListener {

	Dashboard dashboard;

	public UpdateListener(Dashboard dashboard){

		this.dashboard = dashboard;

	}

	public void actionPerformed(ActionEvent e) {

		

	}

}

class ResetListener implements ActionListener {


	SideBar sidebar;

	public ResetListener(SideBar sidebar){


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
		sidebar.bounceBox.setSelectedIndex(0);
		sidebar.bounceLabel.setText("Pages:");
		sidebar.bounceSpinner.setValue(0);

	}

}

// ButtonGroup that allows deselection
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
