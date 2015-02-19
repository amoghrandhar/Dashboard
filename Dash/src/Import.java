import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;


@SuppressWarnings("serial")
public class Import extends JFrame 
{
    JButton openButton, cancelButton;

    public Import(String title) 
    {    
        super(title);
    }
    

    public void init() 
    {
    	Container container = this.getContentPane();
    	container.setLayout(new GridBagLayout());
    	GridBagConstraints c = new GridBagConstraints();
    	c.fill = GridBagConstraints.HORIZONTAL;
    	
    	JPanel glassPanel = new JPanel()  
    	{  
    	   public void paintComponent(Graphics g)  
    	   {  
    		  Graphics2D g2 = (Graphics2D) g;
    		  g2.setColor(Color.black);  
    		  g2.setStroke(new BasicStroke(2));
    		  g2.drawRect(7, 7, 335, 115);
    		  g2.drawRect(7, 130, 335, 115);
    		  g2.drawRect(7, 255, 335, 115);


    	   }  
    	};     	
    	glassPanel.setOpaque(false);  
    	this.setGlassPane(glassPanel);
    	glassPanel.setVisible(true);

    	//////////////////////////////////////////////////////
    	
    	DragAndDropPanel panel = new DragAndDropPanel();
    	c.weighty = 0.5;
    	c.weightx = 0;
    	c.gridx = 0;
    	c.gridy = 0;
    	c.gridheight = 4;
        c.insets = new Insets(0, -10, -10, 10);
        container.add(panel, c);

    	JLabel impressionLabel = new JLabel("Impression Log");
    	impressionLabel.setFont(new Font("", Font.PLAIN, 16));
    	c.gridheight = 1;
    	c.gridx = 1;
    	c.gridy = 0;
        c.insets = new Insets(5, 18, 0, 0);
        container.add(impressionLabel, c);
    	

    	JLabel label1 = new JLabel("Alternatively you can");
    	c.gridx = 1;
    	c.gridy = 1;
        c.insets = new Insets(0, 18, -5, 0);
    	container.add(label1, c);
    	
    
    	JLabel label2 = new JLabel("browse for the file");
    	c.gridx = 1;
    	c.gridy = 2;
        c.insets = new Insets(-5, 18, 0, 0);
    	container.add(label2, c);
    	

    	JButton browseButton = new JButton("Browse");
    	c.gridx = 1;
    	c.gridy = 3;
    	c.anchor = GridBagConstraints.LAST_LINE_END; //bottom of space
        c.insets = new Insets(0, 70, 0, 0);
    	container.add(browseButton, c);
    	    	
    	//////////////////////////////////////////////////////

    	DragAndDropPanel panel2 = new DragAndDropPanel();
    	c.weighty = 0.5;
    	c.weightx = 0;
    	c.gridx = 0;
    	c.gridy = 4;
    	c.gridheight = 4;
        c.insets = new Insets(0, -10, 0, 10);
    	container.add(panel2, c);

    	JLabel clickLabel = new JLabel("Click Log");
    	clickLabel.setFont(new Font("", Font.PLAIN, 16));
    	c.gridheight = 1;
    	c.gridx = 1;
    	c.gridy = 4;
        c.insets = new Insets(8, 18, 0, 0);
    	container.add(clickLabel, c);
    	
    	JLabel label3 = new JLabel("Alternatively you can");
    	c.gridx = 1;
    	c.gridy = 5;
        c.insets = new Insets(0, 18, -5, 0);
    	container.add(label3, c);
    	
    	JLabel label4 = new JLabel("browse for the file");
    	c.gridx = 1;
    	c.gridy = 6;
        c.insets = new Insets(-5, 18, 0, 0);
    	container.add(label4, c);
    	
    	JButton browseButton2 = new JButton("Browse");
    	c.gridx = 1;
    	c.gridy = 7;
        c.insets = new Insets(0, 70, 0, 0);
    	container.add(browseButton2, c);
    	
    	//////////////////////////////////////////////////////

    	DragAndDropPanel panel3 = new DragAndDropPanel();
    	c.weighty = 0.5;
    	c.weightx = 0;
    	c.gridx = 0;
    	c.gridy = 8;
    	c.gridheight = 4;
        c.insets = new Insets(0, -10, 10, 10);
    	container.add(panel3, c);

    	JLabel serverLabel = new JLabel("Server Log");
    	serverLabel.setFont(new Font("", Font.PLAIN, 16));
    	c.gridheight = 1;
    	c.gridx = 1;
    	c.gridy = 8;
        c.insets = new Insets(8, 18, 0, 0);
    	container.add(serverLabel, c);
    	
    	JLabel label5 = new JLabel("Alternatively you can");
    	c.gridx = 1;
    	c.gridy = 9;
        c.insets = new Insets(0, 18, -5, 0);
    	container.add(label5, c);
    	
    	JLabel label6 = new JLabel("browse for the file");
    	c.gridx = 1;
    	c.gridy = 10;
        c.insets = new Insets(-5, 18, 0, 0);
    	container.add(label6, c);
    	
    	JButton browseButton3 = new JButton("Browse");
    	c.gridx = 1;
    	c.gridy = 11;
        c.insets = new Insets(0, 70, 10, 0);
    	container.add(browseButton3, c);
    	
    	//////////////////////////////////////////////////////
    	container.setBackground(Color.WHITE);
        this.pack();
        this.setMinimumSize(new Dimension(350, 398));
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setVisible(true);
    }
    
    class DragAndDropPanel extends JPanel
    {
    	public DragAndDropPanel()
    	{
    		ImageIcon icon = new ImageIcon(getClass().getResource("dropImpression.png"));
    		Image img = icon.getImage();
    		icon = new ImageIcon(img);
    		add(new JLabel(icon));
    		setBackground(Color.WHITE);
    	}
    }
}
