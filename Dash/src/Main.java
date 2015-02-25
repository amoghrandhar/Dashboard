import javax.swing.*;
import javax.swing.UIManager.LookAndFeelInfo;


public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(
                new Runnable() {
                    public void run() {                	
                        try {
                        	
                            /*LookAndFeelInfo[] la = UIManager.getInstalledLookAndFeels();
                            for (int i = 0; i < la.length; i++) {
                                LookAndFeelInfo l = la[i];
                                System.out.println(l.getName());
                            }*/

                            for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                                if ("Nimbus".equals(info.getName())) {
                                    UIManager.setLookAndFeel(info.getClassName());
                                    break;
                                }
                                UIManager.setLookAndFeel(
                                        UIManager.getCrossPlatformLookAndFeelClassName());
                            }

                            Dashboard frame = new Dashboard("Ad Dashboard");
                            frame.init();
                            
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InstantiationException e) {
                            e.printStackTrace();
                        } catch (UnsupportedLookAndFeelException e) {
                            e.printStackTrace();
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }                 
                    }
                }
        );
    }
    
}
