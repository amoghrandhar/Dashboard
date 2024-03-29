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
    
    private JCheckBox checkBox;
    
    private String[] colours;

    public Import(String title, Dashboard dashboard, String[] colours) {
        super(title);
        this.colours = colours;
        this.dashboard = dashboard;
        fc = new JFileChooser();
        this.setAlwaysOnTop(true);
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

        this.checkBox = new JCheckBox("Compare this campaign with a previously imported one.");
        this.checkBox.setSelected(false);
	    c.gridx = 0;
	    c.gridy = 5;
	    c.gridheight = 1;
	    c.gridwidth = 2;
	    c.insets = new Insets(0, 7, 2, 7);
	    container.add(checkBox, c);
	    c.gridwidth = 1;
	    this.checkBox.setEnabled(dashboard.isFirstCampaign());;
	    

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

        container.setBackground(Color.decode(colours[2]));
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
            setBackground(Color.decode(colours[2]));
            new DropTarget(this, new DragDropListener());
        }
    }

    class DragDropListener implements DropTargetListener {

        public void drop(DropTargetDropEvent event) {
            event.acceptDrop(DnDConstants.ACTION_COPY);
            try {
                @SuppressWarnings("unchecked")
                List<File> files = ((List<File>) event.getTransferable().getTransferData(DataFlavor.javaFileListFlavor));
                boolean wrongFile = false;
                if (files.size() <= 3) { 

                	for (File f: files) {
                		switch (f.getName()) {
                			case "click_log.csv":
                				clickLog = f;
                                configurePanel1("clickLogImported.png");
                				break;
                			case "impression_log.csv":
                				impressionLog = f;
                                configurePanel2("impressionLogImported.png");
                				break;
                			case "server_log.csv":
                				serverLog = f;
                                configurePanel3("serverLogImported.png");
                				break;  
                			default:
                				wrongFile = true;
                				break;
                		}
                	}
                    revalidate();
                	if (wrongFile) {
                		   JOptionPane.showMessageDialog(Import.this, "<html>One or more incorrect<br> files selected.</html>", "Error",
                                   JOptionPane.ERROR_MESSAGE);
                	}
                }
                else {
                    JOptionPane.showMessageDialog(Import.this, "<html>Too many<br> files selected.</html>", "Error",
                            JOptionPane.ERROR_MESSAGE);
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
        	int replaceCampaign = 0;
            if (e.getSource() == openButton) {
                if (clickLog != null && impressionLog != null && serverLog != null) {
                	checkBox.setEnabled(false);
                	cancelButton.setEnabled(false);
                	openButton.setEnabled(false);
                    showProcessingAnimation();
                    
                    if (dashboard.isFirstCampaign()) {
                    	if (dashboard.isSecondCampaign()) {                          
                    		Object[] options = {"Campaign 1", "Campaign 2", "Cancel"};
                            int userResponse = JOptionPane.showOptionDialog(dashboard,
                                    "Which campaign do you wish to replace?", "Replace Campaign",
                                    JOptionPane.YES_NO_CANCEL_OPTION,
                                    JOptionPane.QUESTION_MESSAGE,
                                    null,
                                    options,
                                    options[0]);
                            if (userResponse == JOptionPane.YES_OPTION) {
                            	replaceCampaign = 1;
                            }                  
                            else if (userResponse == JOptionPane.NO_OPTION) {
                            	replaceCampaign = 2;
                            }
                            else {
                                this.dashboard.sidebar.importButton.setEnabled(true);
                                setVisible(false);
                                dispose();
                            }
                    	} else {
	                    	if (checkBox.isSelected()) {
	                    		replaceCampaign = 2;
	                    	} else {
	                    		replaceCampaign = 1;
	                    	} 
                    	}
                    } else {
                		replaceCampaign = 1;
                    }                         
                    
                    // final variable to be used inside thread
                    final int _replaceCampaign = replaceCampaign;
                    // running this in a thread to stop GUI hanging
                    new Thread(() -> {
                        final ClicklogParser clicklogParser = new ClicklogParser(clickLog.getAbsolutePath());
                        final ImpressionParser impressionParser = new ImpressionParser(impressionLog.getAbsolutePath());
                        final ServerlogParser serverlogParser = new ServerlogParser(serverLog.getAbsolutePath());
                        //This will start the parsing of the csv log files and generate Arraylist of Data

	                    /* MULTITHREADED */

                        Thread thread1 = new Thread() {
                            public void run() {
                                try {
                                    clicklogParser.generateClickLogs();
                                } catch (WrongFileException e1) {
                                    JOptionPane.showMessageDialog(Import.this, "Wrong File Passed for Processing \n" + e1.fileName, "Error",
                                            JOptionPane.ERROR_MESSAGE);
                                    e1.printStackTrace();
                                }
                            }
                        };
                        Thread thread2 = new Thread() {
                            public void run() {
                                try {
                                    impressionParser.generateImpressionsMethod1();
                                }
                                catch (WrongFileException e1) {
                                    JOptionPane.showMessageDialog(Import.this, "Wrong File Passed for Processing \n" + e1.fileName, "Error",
                                            JOptionPane.ERROR_MESSAGE);
                                    e1.printStackTrace();
                                }
                            }
                        };
                        Thread thread3 = new Thread() {
                            public void run() {
                                try {
                                    serverlogParser.generateServerLogs();
                                } catch (WrongFileException e1) {
                                    JOptionPane.showMessageDialog(Import.this, "Wrong File Passed for Processing \n" + e1.fileName, "Error",
                                            JOptionPane.ERROR_MESSAGE);
                                    e1.printStackTrace();
                                }
                            }
                        };

                        thread1.start();
                        thread2.start();
                        thread3.start();
                        try {
                            thread1.join();
                            thread2.join();
                            thread3.join();
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }

	                    /* SINGLETHREADED
	                     *
                   		try {
                			clicklogParser.generateClickLogs();
                            impressionParser.generateImpressionsMethod1();
                            serverlogParser.generateServerLogs();
                		} catch (WrongFileException e1) {
                			JOptionPane.showMessageDialog(Import.this, "Wrong File Passed for Processing \n" + e1.fileName, "Error",
                                JOptionPane.ERROR_MESSAGE);
                			e1.printStackTrace();
                		}
                       	*/

                        // This will update the ArrayList of data logs with new data
                    
                        if (_replaceCampaign == 1) {
                            dashboard.setOriginalLogsC1(clicklogParser.getClickLogs(), impressionParser.getImpressions(), serverlogParser.getServerLogs());
                        }
                        else if (_replaceCampaign == 2) {
                        	dashboard.setOriginalLogsC2(clicklogParser.getClickLogs(), impressionParser.getImpressions(), serverlogParser.getServerLogs());
                        	if(dashboard.isComparing())
                        		dashboard.content.tabbedPane.setTitleAt(2, "Demographics of Campaign 2");
                        }
                        
                        dashboard.updateMetrics(dashboard.DEFAULT_BOUNCE_PAGES_PROP,dashboard.DEFAULT_BOUNCE_TIME_PROP);
                        dashboard.updateHeader();
                        dashboard.sidebar.resetAllSeries();
                        Platform.runLater(dashboard::defaultChart);
                        
                        dashboard.sidebar.importButton.setEnabled(true);
                        dashboard.sidebar.exportButton.setEnabled(true);
                        dashboard.sidebar.compareButton.setEnabled(true);

                        setVisible(false);
                        dispose();

                        
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
