import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.PrintQuality;
import javax.print.attribute.standard.PrinterResolution;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

/**
 * // \\anywhere
 * @author User
 */
public class PrintSupport implements Printable {
 
    private Component print_component;
 
    public static void printComponent(Component c) {
        new PrintSupport(c).doPrint();
    }
 
    public PrintSupport(Component comp) {
        this.print_component = comp;
    }
 
    public void doPrint() {
        PrinterJob printJob = PrinterJob.getPrinterJob();
        PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
        PrinterResolution pr = new PrinterResolution(300, 300, PrinterResolution.DPI);
        aset.add(pr);
        aset.add(PrintQuality.HIGH);
        PageFormat pf = printJob.defaultPage();
        pf.setOrientation(PageFormat.LANDSCAPE); 
        printJob.setPrintable(this, pf);
        if (printJob.printDialog()) {
            try {
                printJob.print();
            } catch (PrinterException pe) {
                System.out.println("Error printing: " + pe);
            }
        }
    }
 
    @Override
    public int print(Graphics g, PageFormat pf, int pageNumber) {

        if (pageNumber > 0) {
            return Printable.NO_SUCH_PAGE;
        }

        // Get the preferred size ofthe component...
        Dimension compSize = print_component.getPreferredSize();
        // Make sure we size to the preferred size
        print_component.setSize(compSize);
        // Get the the print size
        Dimension printSize = new Dimension();
        printSize.setSize(pf.getImageableWidth()-50, pf.getImageableHeight()-50);

        // Calculate the scale factor
        double scaleFactor = getScaleFactorToFit(compSize, printSize);
        // Don't want to scale up, only want to scale down
        if (scaleFactor > 1d) {
            scaleFactor = 1d;
        }

        // Calculate the scaled size...
        double scaleWidth = compSize.width * scaleFactor;
        double scaleHeight = compSize.height * scaleFactor;

        // Create a clone of the graphics context.  This allows us to manipulate
        // the graphics context without begin worried about what effects
        // it might have once we're finished
        Graphics2D g2 = (Graphics2D) g.create();

        RenderingHints rh = new RenderingHints(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        g2.setRenderingHints(rh);

        // Calculate the x/y position of the component, this will center
        // the result on the page if it can
        double x = ((pf.getImageableWidth() - scaleWidth) / 2d) + pf.getImageableX();
        double y = ((pf.getImageableHeight() - scaleHeight) / 2d) + pf.getImageableY();
        // Create a new AffineTransformation
        AffineTransform at = new AffineTransform();
        // Translate the offset to out "center" of page
        at.translate(x, y);
        // Set the scaling
        at.scale(scaleFactor, scaleFactor);
        // Apply the transformation
        g2.transform(at);
        // Print the component
        print_component.printAll(g2);
        // Dispose of the graphics context, freeing up memory and discarding
        // our changes
        g2.dispose();
        print_component.revalidate();
        return Printable.PAGE_EXISTS;
        
    }
 
    public static void disableDoubleBuffering(Component c) {
        RepaintManager currentManager = RepaintManager.currentManager(c);
        currentManager.setDoubleBufferingEnabled(false);
    }
 
    public static void enableDoubleBuffering(Component c) {
        RepaintManager currentManager = RepaintManager.currentManager(c);
        currentManager.setDoubleBufferingEnabled(true);
    }
    
    public static double getScaleFactorToFit(Dimension original, Dimension toFit) {

        double dScale = 1d;

        if (original != null && toFit != null) {

            double dScaleWidth = getScaleFactor(original.width, toFit.width);
            double dScaleHeight = getScaleFactor(original.height, toFit.height);

            dScale = Math.min(dScaleHeight, dScaleWidth);

        }

        return dScale;

    }

    public static double getScaleFactor(int iMasterSize, int iTargetSize) {

        double dScale;
        if (iMasterSize > iTargetSize) {

            dScale = (double) iTargetSize / (double) iMasterSize;

        } else {

            dScale = (double) iTargetSize / (double) iMasterSize;

        }

        return dScale;

    }
}