import javax.swing.*;
import javax.swing.UIManager.LookAndFeelInfo;


public class Main {

    public static void main(String[] args) {

    	
        SwingUtilities.invokeLater(
                () -> {
                    try {

//                        LookAndFeelInfo[] la = UIManager.getInstalledLookAndFeels();
//                        for (LookAndFeelInfo l : la) {
//                            System.out.println(l.getName());
//                        }

                        for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                            if ("Nimbus".equals(info.getName())) {
                                UIManager.setLookAndFeel(info.getClassName());
                                break;
                            }
                            UIManager.setLookAndFeel(
                                    UIManager.getCrossPlatformLookAndFeelClassName());
                        }

                        Dashboard frame = new Dashboard("Ad Dashboard");
                        frame.init(new String[] {"#c4c7cc", "#f5f5f5", "#d5d5d5", "#fafafa", "#fafafa"});     
                    } catch (IllegalAccessException | InstantiationException | ClassNotFoundException | UnsupportedLookAndFeelException e) {
                        e.printStackTrace();
                    }
                }
        );
    }

}
