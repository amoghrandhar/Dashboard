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
    File clickLog, impressionLog, serverLog;
    Container container;
    GridBagConstraints c;
    JButton browseButton1, browseButton2, browseButton3;
    JButton cancelButton, openButton;
    DragAndDropPanel panel1, panel2, panel3;

    public Import(String title) {
        super(title);
    }

    public static void main(String[] args) {
        new Import("Import");
    }

    public void init() {
        container = this.getContentPane();
        container.setLayout(new GridBagLayout());
        c = new GridBagConstraints();
        ButtonListener buttonListener = new ButtonListener();
        c.fill = GridBagConstraints.HORIZONTAL;

        this.browseButton1 = new JButton("Browse for the file");
        this.browseButton1.addActionListener(buttonListener);
        c.gridx = 0;
        c.gridy = 0;
        c.gridheight = 1;
        c.insets = new Insets(1, 7, -2, 7);
        container.add(browseButton1, c);

        this.configurePanel1("dropClick.png");

        this.browseButton2 = new JButton("Browse for the file");
        this.browseButton2.addActionListener(buttonListener);
        c.gridx = 1;
        c.gridy = 0;
        c.gridheight = 1;
        c.insets = new Insets(1, 7, -2, 7);
        container.add(browseButton2, c);

        this.configurePanel2("dropImpression.png");

        this.browseButton3 = new JButton("Browse for the file");
        this.browseButton3.addActionListener(buttonListener);
        c.gridx = 2;
        c.gridy = 0;
        c.gridheight = 1;
        c.insets = new Insets(1, 7, -2, 7);
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
        this.setSize(580, 190);
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
        c.insets = new Insets(-4, 0, 0, 0);
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
                File file = ((List<File>) event.getTransferable().getTransferData(DataFlavor.javaFileListFlavor)).get(0);
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
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == openButton) {
                if (clickLog != null && impressionLog != null && serverLog != null) {
                    setVisible(false);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(Import.this, "Please import all three\nfiles before continuing.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            } else if (e.getSource() == cancelButton) {
                clickLog = null;
                impressionLog = null;
                serverLog = null;
                setVisible(false);
                dispose();
            } else {
                final JFileChooser fc = new JFileChooser();
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
