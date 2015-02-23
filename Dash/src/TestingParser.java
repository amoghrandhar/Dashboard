import java.io.File;

/**
 * Created by Amogh on 22-02-2015.
 */
public class TestingParser {

    public static void main(String[] args) throws WrongFileException {
        System.out.println(new File(".").getAbsolutePath());

        ImpressionParser ip = new ImpressionParser();
        ip.generateImpressionsMethod1();

        ClicklogParser clp = new ClicklogParser();
        //clp.generateClickLogs();

        ServerlogParser slp = new ServerlogParser();
        slp.generateServerLogs();


        System.out.println("\n\n\n\n");
        System.out.println("Data Analytics Test");

        DataAnalytics dataAnalytics = new DataAnalytics();
        Double cpa = dataAnalytics.getCPA(ip.getImpressions(), clp.getClickLogs(), slp.getServerLogs());
        System.out.println(cpa);
    }
}
