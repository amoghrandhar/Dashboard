import java.io.File;

/**
 * Created by Amogh on 22-02-2015.
 */
public class TestingParser {

    public static void main(String[] args) throws WrongFileException {

        System.out.println("Running The Data Tester : ->");

        System.out.println("Test Files Location\t" + new File(".").getAbsolutePath());

        System.out.println("\nImpression Log Parser");
        ImpressionParser ip = new ImpressionParser("impression_log.csv");
        ip.generateImpressionsMethod1();
        System.out.println("Impression Log Parsed!");
        if (ip.getImpressions().size() == 39) {
            System.out.println("ImP Pass");
        } else {
            System.out.println("ImP fail");
        }


        System.out.println("\nClick Log Parser");
        ClicklogParser clp = new ClicklogParser("click_log.csv");
        clp.generateClickLogs();
        System.out.println("Click Log Parsed!");
        if (clp.getClickLogs().size() == 13) {
            System.out.println("ClP Pass");
        } else {
            System.out.println("ClP fail");
        }

        System.out.println("\nServer Log Parser");
        ServerlogParser slp = new ServerlogParser("server_log.csv");
        slp.generateServerLogs();
        System.out.println("Server Log Parsed!");
        if (clp.getClickLogs().size() == 13) {
            System.out.println("SlP Pass");
        } else {
            System.out.println("SlP fail");
        }


        System.out.println("\n\n");
        System.out.println("Data Analytics Test");
        DataAnalytics dataAnalytics = new DataAnalytics();

        System.out.println("Total Clicks : " + dataAnalytics.totalClicks(clp.getClickLogs()));
        System.out.println("No. of Impressions : " + dataAnalytics.noOfImpression(ip.getImpressions()));
        System.out.println("No. of Uniques : " + dataAnalytics.noOfUniques(clp.getClickLogs()));
        System.out.println("No. of Bounces for property 5 : " + dataAnalytics.noOfBounces(slp.getServerLogs()));
        System.out.println("No. of Conversions : " + dataAnalytics.noOfConversions(slp.getServerLogs()));
        System.out.println("Total Imprression Cost : " + dataAnalytics.totalImpressionCost(ip.getImpressions()));
        System.out.println("Total Click  Cost : " + dataAnalytics.totalClickCost(clp.getClickLogs()));
        System.out.println("Total Cost : " + dataAnalytics.totalCost(ip.getImpressions(), clp.getClickLogs()).floatValue());
        System.out.println("CPA : " + dataAnalytics.getCPA(ip.getImpressions(), clp.getClickLogs(), slp.getServerLogs()).floatValue());
        System.out.println("CTR : " + dataAnalytics.getCTR(clp.getClickLogs(), ip.getImpressions()).floatValue());
        System.out.println("CPC : " + dataAnalytics.getCPC(ip.getImpressions(), clp.getClickLogs()).floatValue());
        System.out.println("CPM : " + dataAnalytics.getCPM(ip.getImpressions(), clp.getClickLogs()).floatValue());
        System.out.println("Bounce Rate : " + dataAnalytics.bounceRate(clp.getClickLogs(), slp.getServerLogs()).floatValue());


        System.out.println("\n\n\n");
        System.out.println("Pie Charts");
        System.out.println("Gender Group : " + dataAnalytics.sexRatioDivision(ip.getImpressions()));
        System.out.println("Age Group : " + dataAnalytics.ageGroupDivision(ip.getImpressions()));
        System.out.println("Income Group : " + dataAnalytics.incomeGroupDivision(ip.getImpressions()));
        System.out.println("Context Group : " + dataAnalytics.contextGroupDivision(ip.getImpressions()));


//        System.out.println("\n\n Testing the filtering ::: - >");
//        Predicate<ClickLog> clickLogPredicate = clickLog -> clickLog.getDate().after(new Date());
//        System.out.println(dataAnalytics.filterClickLogs(clickLogPredicate, clp.getClickLogs()).size());
//

    }
}
