import javax.swing.*;
import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.border.*;
import javax.swing.event.*;
import org.jdatepicker.impl.*;

import java.awt.*;
import java.awt.event.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;

public class SideBar extends JPanel {

	JButton importButton, exportButton;
	JPanel filePanel, menuPanel;
	
	Color FOREGROUND = Color.decode("#fafafa");

	public SideBar() {

		init();

	}

	public static JPanel getDummyPanel(String name) {

		JPanel panel = new JPanel(new BorderLayout());
		panel.add(new JLabel(name, JLabel.CENTER));
		return panel;

	}

	public void init() {

		//this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

		GridBagConstraints importC = new GridBagConstraints();
		GridBagConstraints exportC = new GridBagConstraints();

		importC.gridx = 0;
		importC.gridy = 0;
		importC.insets = new Insets(4, 0, 4, 0);

		exportC.gridx = 0;
		exportC.gridy = 1;
		exportC.insets = new Insets(4, 0, 4, 0);

		// Panels - Top import/export panel and bottom collapsible menu for filters, bounce definition, etc.

		filePanel = new JPanel();
		filePanel.setLayout(new GridBagLayout());

		menuPanel = new JPanel();
		menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.PAGE_AXIS));
		menuPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.gray));
		
		filePanel.setBorder(new EmptyBorder(16, 0, 16, 0));

		menuPanel.setPreferredSize(new Dimension(200, 500));

		// File Panel (Top)

		ImageIcon importIcon = new ImageIcon(getClass().getResource("plus.png"));
		Image img = importIcon.getImage();
		Image newimg = img.getScaledInstance(16, 16, java.awt.Image.SCALE_SMOOTH);
		importIcon = new ImageIcon(newimg);

		importButton = new JButton(" Import ", importIcon);
		importButton.setFont(new Font("", Font.PLAIN, 12));
		importButton.setPreferredSize(new Dimension(120, 46));
		importButton.setFocusable(false);
		importButton.addActionListener(new ImportListener());
		filePanel.add(importButton, importC);

		ImageIcon exportIcon = new ImageIcon(getClass().getResource("download.png"));
		Image img2 = exportIcon.getImage();
		Image newimg2 = img2.getScaledInstance(16, 16, java.awt.Image.SCALE_SMOOTH);
		exportIcon = new ImageIcon(newimg2);

		exportButton = new JButton(" Export ", exportIcon);
		exportButton.setFont(new Font("", Font.PLAIN, 12));
		exportButton.setPreferredSize(new Dimension(120, 46));
		exportButton.setFocusable(false);
		filePanel.add(exportButton, exportC);

		// Menu Panel (Bottom)
		
		JButton updateButton = new JButton("Update");
		JButton resetButton = new JButton ("Reset");
		
		JPanel updatePanel = new JPanel();
		updatePanel.setLayout(new FlowLayout());
		updatePanel.add(resetButton);
		updatePanel.add(updateButton);
		Border b1 = BorderFactory.createEmptyBorder(6, 0, 6, 0);
		Border b2 = (BorderFactory.createMatteBorder(0, 0, 1, 0, Color.gray));
		updatePanel.setBorder(new CompoundBorder(b2, b1));

		Box accordion = Box.createVerticalBox();
		accordion.setOpaque(true);
		for (AbstractExpansionPanel p: makeList()) {
			p.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.gray));
			accordion.add(p);
		}
		accordion.add(Box.createVerticalGlue());
		accordion.setPreferredSize(new Dimension(200, 499));

		menuPanel.add(updatePanel);
		menuPanel.add(accordion);

		// Frame

		this.add(filePanel, BorderLayout.PAGE_START);
		this.add(menuPanel, BorderLayout.PAGE_END);

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
						
						UtilDateModel model = new UtilDateModel();
						UtilDateModel model2 = new UtilDateModel();
						JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
						JDatePanelImpl datePanel2 = new JDatePanelImpl(model2, p);
						JDatePickerImpl startPicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
						JDatePickerImpl endPicker = new JDatePickerImpl(datePanel2, new DateLabelFormatter());
//						UtilDateModel model = new UtilDateModel();
//						JDatePanelImpl datePanel = new JDatePanelImpl(model);
//						JDatePickerImpl datePicker = new JDatePickerImpl(datePanel);
//						pnl.add(datePicker);
						
						pnl.add(startLabel);
						pnl.add(startPicker);
						pnl.add(endLabel);
						pnl.add(endPicker);
						
						return pnl;
					}
				},
				
				new AbstractExpansionPanel(" Audience Segments") {
					
					public JPanel makePanel() {
						
						JPanel pnl = new JPanel(new GridLayout(0, 1));
						
						JToggleButton male = new JToggleButton("Male");
						JToggleButton female = new JToggleButton("Female");
						
						ButtonGroup bg = new ModifiedButtoGroup();
						bg.add(male);
						bg.add(female);
						
						JPanel buttonPanel = new JPanel(new FlowLayout());
						buttonPanel.setBackground(FOREGROUND);
						buttonPanel.add(male);
						buttonPanel.add(female);
						
						JLabel ageLabel = new JLabel("Age");
						
						Hashtable<Integer, JLabel> labels = new Hashtable<Integer, JLabel>();
				        labels.put(0, new JLabel("0"));
				        labels.put(1, new JLabel("25"));
				        labels.put(2, new JLabel("35"));
				        labels.put(3, new JLabel("45"));
				        labels.put(4, new JLabel("55"));
				        labels.put(5, new JLabel("100"));
						
						RangeSlider ageSlider = new RangeSlider(0, 5);
						ageSlider.setMinorTickSpacing(1);
						ageSlider.setPaintTicks(true);
					    ageSlider.setPaintLabels(true);
					    ageSlider.setLowerValue(0);
					    ageSlider.setUpperValue(5);
						ageSlider.setLabelTable(labels);
						
						JLabel incomeLabel = new JLabel("Income");
						
						Hashtable<Integer, JLabel> labels2 = new Hashtable<Integer, JLabel>();
				        labels2.put(1, new JLabel("Low"));
				        labels2.put(3, new JLabel("Mid"));
				        labels2.put(5, new JLabel("High"));
						
						RangeSlider incomeSlider = new RangeSlider(0, 6);
						incomeSlider.setMinorTickSpacing(2);
						incomeSlider.setPaintTicks(true);
					    incomeSlider.setPaintLabels(true);
					    incomeSlider.setLowerValue(0);
					    incomeSlider.setUpperValue(4);
						incomeSlider.setLabelTable(labels2);
						
//						// Add listener to update display.
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
						JRadioButton b1 = new JRadioButton("News");
						JRadioButton b2 = new JRadioButton("Shopping");
						JRadioButton b3 = new JRadioButton("Social");
						JRadioButton b4 = new JRadioButton("Media");
						JRadioButton b5 = new JRadioButton("Blog");
						JRadioButton b6 = new JRadioButton("Hobbies");
						JRadioButton b7 = new JRadioButton("Travel");
						for (JRadioButton b: Arrays.asList(b1, b2, b3, b4, b5, b6, b7)) {
							b.setOpaque(false); pnl.add(b);
						}
						return pnl;
					}
				},
				new AbstractExpansionPanel(" Define Bounce") {
					public JPanel makePanel() {
						JPanel pnl = new JPanel(new GridLayout(0, 1));
						ButtonGroup bg = new ButtonGroup();
						JRadioButton b1 = new JRadioButton("aaa");
						JRadioButton b2 = new JRadioButton("bbb");
						JRadioButton b3 = new JRadioButton("ccc");
						JRadioButton b4 = new JRadioButton("ddd");
						JRadioButton b5 = new JRadioButton("aaa");
						JRadioButton b6 = new JRadioButton("bbb");
						JRadioButton b7 = new JRadioButton("ccc");
						JRadioButton b8 = new JRadioButton("ddd");
						JRadioButton b9 = new JRadioButton("aaa");
						JRadioButton b10 = new JRadioButton("bbb");
						JRadioButton b11 = new JRadioButton("ccc");
						JRadioButton b12 = new JRadioButton("ddd");
						for (JRadioButton b: Arrays.asList(b1, b2, b3, b4, b5, b6, b7, b8,
								b9, b10, b11, b12)) {
							b.setOpaque(false); pnl.add(b); bg.add(b);
						}
						b1.setSelected(true);
						return pnl;
					}
				}
				);
	}

}

abstract class AbstractExpansionPanel extends JPanel {

	private final String title;
	private final JLabel label;
	private final JPanel panel;

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
		label.addMouseListener(new MouseAdapter() {
			@Override public void mousePressed(MouseEvent e) {
				initPanel();
			}
		});
		label.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
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
		//fireExpansionEvent();
		EventQueue.invokeLater(new Runnable() {
			@Override public void run() {
				panel.scrollRectToVisible(panel.getBounds());
			}
		});

	}

}

class ImportListener implements ActionListener {

	public void actionPerformed(ActionEvent e) {


		Import importFrame = new Import("Import Files");
		importFrame.init();

	}

}

class ModifiedButtoGroup extends ButtonGroup {

	  @Override
	  public void setSelected(ButtonModel model, boolean selected) {

	    if (selected) {

	      super.setSelected(model, selected);

	    } else {

	      clearSelection();
	    }
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