import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import javax.swing.*;
import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DateFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class SideBar extends JPanel {

    Dashboard dashboard;

    JButton importButton, exportButton;
    JPanel filePanel, menuPanel;

    UtilDateModel dateModel, dateModel2;
    SpinnerDateModel timeModel, timeModel2;
    JToggleButton male, female;
    JSlider ageSlider, incomeSlider;

    ModifiedButtonGroup sexGroup, contextGroup;

    Color SECONDARY = Color.decode("#fafafa");

    public SideBar(Dashboard dashboard) {

        this.dashboard = dashboard;
        init();

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
        filePanel.add(exportButton, exportC);

        // Menu Panel (Bottom)

        JButton updateButton = new JButton("Update");
        JButton resetButton = new JButton("Reset");

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

        for (AbstractExpansionPanel p : titledPanes) {
            p.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.gray));
            p.label.addMouseListener(new TitledPaneAdapter(p, titledPanes));
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

                        dateModel = new UtilDateModel();
                        dateModel2 = new UtilDateModel();
                        JDatePanelImpl datePanel = new JDatePanelImpl(dateModel, p);
                        JDatePanelImpl datePanel2 = new JDatePanelImpl(dateModel2, p);
                        JDatePickerImpl startPicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
                        JDatePickerImpl endPicker = new JDatePickerImpl(datePanel2, new DateLabelFormatter());

                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.HOUR_OF_DAY, 24); // 24 == 12 PM == 00:00:00
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
                        formatter.setAllowsInvalid(false); // this makes what you want
                        formatter.setOverwriteMode(true);

                        DateFormatter formatter2 = (DateFormatter) editor2.getTextField().getFormatter();
                        formatter2.setAllowsInvalid(false); // this makes what you want
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

                        // ######### Male or Female buttons #########

                        male = new JToggleButton("Male");
                        female = new JToggleButton("Female");

                        ButtonGroup sexGroup = new ModifiedButtonGroup();
                        sexGroup.add(male);
                        sexGroup.add(female);

                        JPanel buttonPanel = new JPanel(new FlowLayout());
                        buttonPanel.setBackground(SECONDARY);
                        buttonPanel.add(male);
                        buttonPanel.add(female);

                        // ######### Age Group SLider #########

                        JLabel ageLabel = new JLabel("Age");

                        Hashtable<Integer, JLabel> labels = new Hashtable<Integer, JLabel>();
                        labels.put(0, new JLabel("<25"));
                        labels.put(1, new JLabel("25-34"));
                        labels.put(2, new JLabel("35-44"));
                        labels.put(3, new JLabel("45-54"));
                        labels.put(4, new JLabel(">55"));

                        ageSlider = new JSlider(0, 4);
                        ageSlider.setMinorTickSpacing(1);
                        ageSlider.setPaintTicks(true);
                        ageSlider.setPaintLabels(true);
                        ageSlider.setSnapToTicks(true);
//						ageSlider.setLowerValue(0);
//						ageSlider.setUpperValue(5);
                        ageSlider.setLabelTable(labels);

                        // ######### Income SLider #########

                        JLabel incomeLabel = new JLabel("Income");

                        Hashtable<Integer, JLabel> labels2 = new Hashtable<Integer, JLabel>();
                        labels2.put(0, new JLabel("Low"));
                        labels2.put(1, new JLabel("Mid"));
                        labels2.put(2, new JLabel("High"));

                        incomeSlider = new JSlider(0, 2);
                        incomeSlider.setMinorTickSpacing(1);
                        incomeSlider.setPaintTicks(true);
                        incomeSlider.setPaintLabels(true);
                        incomeSlider.setSnapToTicks(true);
//						incomeSlider.setLowerValue(0);
//						incomeSlider.setUpperValue(3);
                        incomeSlider.setLabelTable(labels2);

                        // Add listener to update display.
//				        ageSlider.addChangeListener(new ChangeListener() {
//				            public void stateChanged(ChangeEvent e) {
//				                RangeageSlider ageSlider = (RangeageSlider) e.getSource();
//				                rangeageSliderValue1.setText(String.valueOf(ageSlider.getValue()));
//				                rangeageSliderValue2.setText(String.valueOf(ageSlider.getUpperValue()));
//				            }
//				        });

                        // ######### Panel #########

                        JPanel pnl = new JPanel(new GridLayout(0, 1));
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

                        contextGroup = new ModifiedButtonGroup();
                        JRadioButton b1 = new JRadioButton("News");
                        JRadioButton b2 = new JRadioButton("Shopping");
                        JRadioButton b3 = new JRadioButton("Social");
                        JRadioButton b4 = new JRadioButton("Media");
                        JRadioButton b5 = new JRadioButton("Blog");
                        JRadioButton b6 = new JRadioButton("Hobbies");
                        JRadioButton b7 = new JRadioButton("Travel");

                        JPanel pnl = new JPanel(new GridLayout(0, 1));

                        for (JRadioButton b : Arrays.asList(b1, b2, b3, b4, b5, b6, b7)) {
                            b.setOpaque(false);
                            contextGroup.add(b);
                            pnl.add(b);
                        }

                        return pnl;

                    }

                },
                new AbstractExpansionPanel(" Define Bounce") {

                    public JPanel makePanel() {

                        String[] bounceOptions = {"Time spent on website", "Number of pages visited"};
                        JComboBox bounceBox = new JComboBox(bounceOptions);

                        JPanel pnl = new JPanel(new GridLayout(0, 1));
                        pnl.add(bounceBox);

                        return pnl;

                    }

                }

        );

    }

    public Date getChosenStartDate() {

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

    public Date getChosenEndDate() {

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

    public int getChosenAge() {

        return ageSlider.getValue();

    }

    public int getChosenIncome() {

        return incomeSlider.getValue();

    }

    public Boolean getChosenSex() {

        if (male.isSelected())
            return true;
        if (female.isSelected())
            return false;
        else return null;

    }

    public String getChosenContext() {

        return ((JLabel) contextGroup.getSelection()).getName();

    }

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
//		label.addMouseListener(new MouseAdapter() {
//			@Override public void mousePressed(MouseEvent e) {
//				initPanel();
//			}
//		});
        label.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        this.add(label, BorderLayout.NORTH);

        panel = makePanel();
        panel.setVisible(false);
        panel.setOpaque(true);
        panel.setBackground(Color.decode("#fafafa"));
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
        //fireExpansionEvent();

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

        Import importFrame = new Import("Import Files", dashboard);
        importFrame.init();

    }

}

// Collects all filter options and updates the graphs and metrics
class UpdateListener implements ActionListener {

    Dashboard dashboard;

    public UpdateListener(Dashboard dashboard) {

        this.dashboard = dashboard;

    }

    public void actionPerformed(ActionEvent e) {


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
