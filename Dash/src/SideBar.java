import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.List;


public class SideBar extends JPanel {

	JButton importButton, exportButton;
	JPanel filePanel, menuPanel;

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
		menuPanel.setLayout(new GridBagLayout());
		menuPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.gray));

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

		Box accordion = Box.createVerticalBox();
		accordion.setOpaque(true);
		for (AbstractExpansionPanel p: makeList()) {
			p.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.gray));
			accordion.add(p);
		}
		accordion.add(Box.createVerticalGlue());

		accordion.setPreferredSize(new Dimension(200, 499));
		menuPanel.add(accordion);

		// Frame

		filePanel.setBorder(new EmptyBorder(16, 0, 16, 0));

		menuPanel.setPreferredSize(new Dimension(200, 500));

		this.add(filePanel, BorderLayout.PAGE_START);
		this.add(menuPanel, BorderLayout.PAGE_END);

	}

	private List<AbstractExpansionPanel> makeList() {
		return Arrays.asList(
				new AbstractExpansionPanel(" Choose Filter") {
					public JPanel makePanel() {
						JPanel pnl = new JPanel(new GridLayout(0, 1));
						JCheckBox c1 = new JCheckBox("aaaa");
						JCheckBox c2 = new JCheckBox("aaaaaaa");
						c1.setOpaque(false);
						c2.setOpaque(false);
						pnl.add(c1);
						pnl.add(c2);
						return pnl;
					}
				},
				new AbstractExpansionPanel(" Define Bounce") {
					public JPanel makePanel() {
						JPanel pnl = new JPanel(new GridLayout(0, 1));
						pnl.add(new JLabel("Desktop"));
						pnl.add(new JLabel("My Network Places"));
						pnl.add(new JLabel("My Documents"));
						pnl.add(new JLabel("Shared Documents"));
						return pnl;
					}
				},
				new AbstractExpansionPanel(" Time Granularity") {
					public JPanel makePanel() {
						JPanel pnl = new JPanel(new GridLayout(0, 1));
						pnl.add(new JLabel("Desktop"));
						pnl.add(new JLabel("My Network Places"));
						pnl.add(new JLabel("My Documents"));
						pnl.add(new JLabel("Shared Documents"));
						return pnl;
					}
				},
				new AbstractExpansionPanel(" Export?") {
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
