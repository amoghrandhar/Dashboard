import javafx.application.Platform;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

@SuppressWarnings("serial")
public class Import extends JFrame {
    final JFileChooser fc;
    private File clickLog, impressionLog, serverLog;
    private Container container;
    private GridBagConstraints c;
    private JButton browseButton1, browseButton2, browseButton3;
    private JButton cancelButton, openButton;
    private DragAndDropPanel panel1, panel2, panel3;
    private Dashboard dashboard;

    public Import(String title, Dashboard dashboard) {
        super(title);
        this.dashboard = dashboard;
        fc = new JFileChooser();
    }

    public void init() {
        this.dashboard.sidebar.importButton.setEnabled(false);
        container = this.getContentPane();
        container.setLayout(new GridBagLayout());
        c = new GridBagConstraints();
        ButtonListener buttonListener = new ButtonListener(this.dashboard);
        c.fill = GridBagConstraints.HORIZONTAL;

        this.browseButton1 = new JButton("Browse for the file");
        this.browseButton1.addActionListener(buttonListener);
        c.gridx = 0;
        c.gridy = 0;
        c.gridheight = 1;
        c.insets = new Insets(-6, 7, 2, 7);
        container.add(browseButton1, c);

        this.configurePanel1("dropClick.png");

        this.browseButton2 = new JButton("Browse for the file");
        this.browseButton2.addActionListener(buttonListener);
        c.gridx = 1;
        c.gridy = 0;
        c.gridheight = 1;
        c.insets = new Insets(-6, 7, 2, 7);
        container.add(browseButton2, c);

        this.configurePanel2("dropImpression.png");

        this.browseButton3 = new JButton("Browse for the file");
        this.browseButton3.addActionListener(buttonListener);
        c.gridx = 2;
        c.gridy = 0;
        c.gridheight = 1;
        c.insets = new Insets(-6, 7, 2, 7);
        container.add(browseButton3, c);

        this.configurePanel3("dropServer.png");

        this.cancelButton = new JButton("Cancel");
        this.cancelButton.addActionListener(buttonListener);
        c.gridx = 2;
        c.gridy = 5;
        c.gridheight = 1;
        c.insets = new Insets(-3, 7, -2, 95);
        container.add(cancelButton, c);

        this.openButton = new JButton("Open");
        this.openButton.addActionListener(buttonListener);
        c.gridx = 2;
        c.gridy = 5;
        c.gridheight = 1;
        c.insets = new Insets(-3, 95, -2, 7);
        container.add(openButton, c);

        container.setBackground(new Color(0xf5f5f5));
        this.pack();
        this.setSize(580, 200);
        this.setMinimumSize(getSize());
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setVisible(true);
    }

    public void configurePanel1(String type) {
        panel1 = new DragAndDropPanel(type);
        c.gridx = 0;
        c.gridy = 1;
        c.gridheight = 4;
        c.insets = new Insets(-4, 0, 0, 0);
        container.add(panel1, c);
    }

    public void configurePanel2(String type) {
        panel2 = new DragAndDropPanel(type);
        c.gridx = 1;
        c.gridy = 1;
        c.gridheight = 4;
        c.insets = new Insets(-4, 0, 0, 0);
        container.add(panel2, c);
    }

    public void configurePanel3(String type) {
        panel3 = new DragAndDropPanel(type);
        c.gridx = 2;
        c.gridy = 1;
        c.gridheight = 4;
        c.insets = new Insets(-4, 0, 2, 0);
        container.add(panel3, c);
    }

    class DragAndDropPanel extends JPanel {

        public DragAndDropPanel(String type) {
            ImageIcon icon = new ImageIcon(getClass().getResource(type));
            Image img = icon.getImage();
            icon = new ImageIcon(img);
            add(new JLabel(icon));
            setBackground(new Color(0xf5f5f5));
            new DropTarget(this, new DragDropListener());
        }
    }

    class DragDropListener implements DropTargetListener {

        public void drop(DropTargetDropEvent event) {
            event.acceptDrop(DnDConstants.ACTION_COPY);
            try {
                @SuppressWarnings("unchecked")
                List<File> files = ((List<File>) event.getTransferable().getTransferData(DataFlavor.javaFileListFlavor));
                File file = files.get(0);
                if (files.size() > 3) {
                    JOptionPane.showMessageDialog(Import.this, "<html>Too many<br> files selected.</html>", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
                else if (files.size() == 3) {
                	if ((files.get(0).getName().equals("click_log.csv")) && (files.get(1).getName().equals("impression_log.csv")) && (files.get(2).getName().equals("server_log.csv"))) {
                        clickLog = files.get(0);
                        impressionLog = files.get(1);
                        serverLog = files.get(2);
                        configurePanel1("clickLogImported.png");
                        configurePanel2("impressionLogImported.png");
                        configurePanel3("serverLogImported.png");
                        revalidate();
                	} else {
                        JOptionPane.showMessageDialog(Import.this, "<html>One or more incorrect<br> files selected.</html>", "Error",
                                JOptionPane.ERROR_MESSAGE);
                	}
                }
                else if (files.size() == 2) {
            		if ((files.get(0).getName().equals("click_log.csv")) && (files.get(1).getName().equals("impression_log.csv")))
            		{
            		    clickLog = files.get(0);
                        impressionLog = files.get(1);
                        configurePanel1("clickLogImported.png");
                        configurePanel2("impressionLogImported.png");
                        revalidate();
            		}
            		else if ((files.get(0).getName().equals("click_log.csv")) && (files.get(1).getName().equals("server_log.csv")))
            		{
            		    clickLog = files.get(0);
                        serverLog = files.get(1);
                        configurePanel1("clickLogImported.png");
                        configurePanel3("serverLogImported.png");
                        revalidate();
            		}
            		else if ((files.get(0).getName().equals("impression_log.csv")) && (files.get(1).getName().equals("server_log.csv")))
            		{
            		    impressionLog = files.get(0);
                        serverLog = files.get(1);
                        configurePanel2("impressionLogImported.png");
                        configurePanel3("serverLogImported.png");
                        revalidate();
            		} else {
                        JOptionPane.showMessageDialog(Import.this, "<html>One or more incorrect<br> files selected.</html>", "Error",
                                JOptionPane.ERROR_MESSAGE);
            		}
                } else {              
	                if (file.getName().equals("click_log.csv")) {
	                    clickLog = file;
	                    System.out.println("Opening: " + clickLog.getName());
	                    configurePanel1("clickLogImported.png");
	                    revalidate();
	                } else if (file.getName().equals("impression_log.csv")) {
	                    impressionLog = file;
	                    System.out.println("Opening: " + impressionLog.getName());
	                    configurePanel2("impressionLogImported.png");
	                    revalidate();
	                } else if (file.getName().equals("server_log.csv")) {
	                    serverLog = file;
	                    System.out.println("Opening: " + serverLog.getName());
	                    configurePanel3("serverLogImported.png");
	                    revalidate();
	                } else {
	                    // throw exception?
	                    JOptionPane.showMessageDialog(Import.this, "<html>Please drop either click_log.csv, <br> impression_log.csv or server_log.csv.</html>", "Error",
	                            JOptionPane.ERROR_MESSAGE);
	                }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            event.dropComplete(true);
        }

        public void dragEnter(DropTargetDragEvent dtde) {
        }

        public void dragOver(DropTargetDragEvent dtde) {
        }

        public void dropActionChanged(DropTargetDragEvent dtde) {
        }

        public void dragExit(DropTargetEvent dte) {
        }
    }

    class ButtonListener implements ActionListener {

        Dashboard dashboard;

        public ButtonListener(Dashboard dashboard) {
            this.dashboard = dashboard;
        }

        public void showProcessingAnimation() {

            JPanel panel = new JPanel();
            panel.setLayout(new BorderLayout());
            ImageIcon icon = new ImageIcon(getClass().getResource("loading.gif"));
            Image img = icon.getImage();
            icon = new ImageIcon(img);
            panel.add(new JLabel(icon), BorderLayout.CENTER);
            panel.setOpaque(false);
            setGlassPane(panel);
            panel.setVisible(true);

        }

        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == openButton) {
                if (clickLog != null && impressionLog != null && serverLog != null) {

                    showProcessingAnimation();

//                                // running this in a thread to stop GUI hanging
                    new Thread(() -> {
	                    ClicklogParser clicklogParser = new ClicklogParser(clickLog.getAbsolutePath());
	                    ImpressionParser impressionParser = new ImpressionParser(impressionLog.getAbsolutePath());
	                    ServerlogParser serverlogParser = new ServerlogParser(serverLog.getAbsolutePath());
	
	                    try {
	                        //This will start the parsing of the csv log files and generate Arraylist of Data
	                        clicklogParser.generateClickLogs();
	                        impressionParser.generateImpressionsMethod1();
	                        serverlogParser.generateServerLogs();
	                    } catch (WrongFileException e1) {
	                        JOptionPane.showMessageDialog(Import.this, "Wrong File Passed for Processing \n" + e1.fileName, "Error",
	                                JOptionPane.ERROR_MESSAGE);
	                        e1.printStackTrace();
	                    }
	
	
	                    // This will update the ArrayList of data logs with new data
	                    dashboard.setOriginalLogs(clicklogParser.getClickLogs(), impressionParser.getImpressions(), serverlogParser.getServerLogs());
	                    dashboard.updateMetrics(dashboard.DEFAULT_BOUNCE_PAGES_PROP,dashboard.DEFAULT_BOUNCE_TIME_PROP);
	                    dashboard.updateHeader();
	
	                    Platform.runLater(dashboard::defaultChart);
	
	                    setVisible(false);
	                    dispose();
	                    dashboard.sidebar.importButton.setEnabled(true);
	                    dashboard.sidebar.exportButton.setEnabled(true);
	                    dashboard.sidebar.compareButton.setEnabled(true);
                    }).start();

                } else {
                    JOptionPane.showMessageDialog(Import.this, "Please import all three\nfiles before continuing.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            } else if (e.getSource() == cancelButton) {
                this.dashboard.sidebar.importButton.setEnabled(true);
                clickLog = null;
                impressionLog = null;
                serverLog = null;
                setVisible(false);
                dispose();
            } else {
                int returnVal = fc.showOpenDialog(Import.this);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    if (e.getSource() == browseButton1) {
                        if (fc.getSelectedFile().getName().equals("click_log.csv")) {
                            clickLog = fc.getSelectedFile();
                            System.out.println("Opening: " + clickLog.getName());
                            configurePanel1("clickLogImported.png");
                            revalidate();
                        } else {
                            // throw exception?
                            JOptionPane.showMessageDialog(Import.this, "Please select click_log.csv.", "Error",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    } else if (e.getSource() == browseButton2) {
                        if (fc.getSelectedFile().getName().equals("impression_log.csv")) {
                            impressionLog = fc.getSelectedFile();
                            System.out.println("Opening: " + impressionLog.getName());
                            configurePanel2("impressionLogImported.png");
                            revalidate();
                        } else {
                            // throw exception?
                            JOptionPane.showMessageDialog(Import.this, "Please select impresion_log.csv.", "Error",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    } else if (e.getSource() == browseButton3) {
                        if (fc.getSelectedFile().getName().equals("server_log.csv")) {
                            serverLog = fc.getSelectedFile();
                            System.out.println("Opening: " + serverLog.getName());
                            configurePanel3("serverLogImported.png");
                            revalidate();
                        } else {
                            // throw exception?
                            JOptionPane.showMessageDialog(Import.this, "Please select server_log.csv.", "Error",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }
        }
    }
}
