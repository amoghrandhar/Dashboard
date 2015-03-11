import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.function.Predicate;

import static org.junit.Assert.assertEquals;

public class DashboardTest {

    private static final double DELTA = 1e-15;

    static ArrayList<ClickLog> OriginalClickLogs;
    static ArrayList<ImpressionLog> OriginalImpressionLogs;
    static ArrayList<ServerLog> OriginalServerLogs;

    ArrayList<ClickLog> clickLogs;
    ArrayList<ImpressionLog> impressionLogs;
    ArrayList<ServerLog> serverLogs;

    @BeforeClass
    public static void initialSetup() throws Exception {
        ClicklogParser cp = new ClicklogParser("C:\\Users\\Amogh\\Dashboard\\Test/click_log.csv");
        ImpressionParser ip = new ImpressionParser("C:\\Users\\Amogh\\Dashboard\\Test/impression_log.csv");
        ServerlogParser sp = new ServerlogParser("C:\\Users\\Amogh\\Dashboard\\Test/server_log.csv");

        cp.generateClickLogs();
        ip.generateImpressionsMethod1();
        sp.generateServerLogs();

        OriginalClickLogs = cp.getClickLogs();
        OriginalImpressionLogs = ip.getImpressions();
        OriginalServerLogs = sp.getServerLogs();
    }


    @Before
    public void setUp() throws Exception {

       clickLogs = OriginalClickLogs;
        impressionLogs =OriginalImpressionLogs;
        serverLogs = OriginalServerLogs;

    }

    @Test
    public void testParser() throws Exception {

        assertEquals("Click Log Parser", 13, clickLogs.size());
        assertEquals("Click Log Parser 1st Entry ID", 1, clickLogs.get(0).getID() , DELTA);

        assertEquals("Impression Log Parser", 39, impressionLogs.size());
        assertEquals("Impression Log Parser 1st Entry ID", 1, impressionLogs.get(0).getID() , DELTA);

        assertEquals("Server Log Parser", 13, serverLogs.size());
        assertEquals("ServerS Log Parser 1st Entry ID", 1, serverLogs.get(0).getID() , DELTA);


    }



    public void testFilterMetrics(Boolean gender, Integer ageGroup,
                                  Integer income, String context,
                                  Integer pages, Integer time) throws Exception {


        //Start Date Predicates
        Predicate<ClickLog> clickLogStartDatePredicate = click -> true;
        Predicate<ImpressionLog> impressionLogStartDatePredicate = imp -> true;
        Predicate<ServerLog> serverLogStartDatePredicate = ser -> true;

//        if (startDate != null) {
//            clickLogStartDatePredicate = click -> click.getDate().after(startDate);
//            impressionLogStartDatePredicate = imp -> imp.getDate().after(startDate);
//            serverLogStartDatePredicate = ser -> ser.getStartDate().after(startDate);
//        }
//
        //End Date Predicates
        Predicate<ServerLog> serverLogEndDatePredicate = ser -> true;
//        if (endDate != null) {
//            serverLogStartDatePredicate = ser -> ser.getEndDate() != null && ser.getEndDate().before(endDate);
//        }

        //Gender Predicate
        Predicate<ImpressionLog> impressionLogGenderPredicate = imp -> true;
        if (gender != null) {
            impressionLogGenderPredicate = imp -> imp.getGender() == gender;
        }

        //Age Group Predicate
        Predicate<ImpressionLog> impressionAgePredicate = imp -> true;
        if (ageGroup != -1) {
            impressionAgePredicate = imp -> imp.getAgeGroup() == ageGroup;
        }

        //Income Predicate
        Predicate<ImpressionLog> impressionIncomePredicate = imp -> true;
        ;
        if (income != -1) {
            impressionIncomePredicate = imp -> imp.getIncomeGroup() == income;
        }

        //Context Predicate
        Predicate<ImpressionLog> impressionContextPredicate = imp -> true;
        if (context != null) {
            impressionContextPredicate = imp -> imp.getContext().equals(context);
        }

        Predicate<Integer> integerPredicate = kel -> kel.intValue() == 4;

        //Bounce Predicate
        //No of pages viewed
        Predicate<ServerLog> serverLogNoPredicate = ser -> true;
        if (pages != -1) {
            serverLogNoPredicate = ser -> ser.getPagesViewed() >= pages;
        }


        //Time spent on website
        Predicate<ServerLog> serverTimeSpentPredicate = ser -> true;
        if (time != -1) {
            serverTimeSpentPredicate = ser -> (ser.getEndDate() != null ?
                    (ser.getEndDate().getTime() - ser.getStartDate().getTime()) >= ( time * 1000) : true );
        }


        impressionLogs = (ArrayList <ImpressionLog>) DataAnalytics.filterImpressionLogs(
                impressionLogStartDatePredicate,
                impressionLogGenderPredicate,
                impressionAgePredicate,
                impressionIncomePredicate,
                impressionContextPredicate,
                impressionLogs);

        HashSet<Double> idSet = new HashSet<Double>();
        for (ImpressionLog impressionLog : impressionLogs) {
            idSet.add(impressionLog.getID());
        }

        clickLogs = (ArrayList <ClickLog>) DataAnalytics
                .filterClickLogs(clickLogStartDatePredicate, clickLogs, idSet);

        serverLogs = (ArrayList<ServerLog>) DataAnalytics
                .filterServerLogs(serverLogStartDatePredicate,serverLogEndDatePredicate,
                        serverLogNoPredicate,serverTimeSpentPredicate,serverLogs,idSet);

    }

    @Test
    public void testFiltersJustMale() throws Exception {

        //Just Gender = male
        testFilterMetrics(true,-1,-1,null,-1,-1);

        assertEquals("Testing Filter ClickLogs" , 2 , clickLogs.size());
        assertEquals("Testing Filter ImpressionLogs", 11, impressionLogs.size());
        assertEquals("Testing Filter ServerLogs" , 2 , serverLogs.size());
    }
    
    
    @Test
        public  void testFiltersJustFemale() throws  Exception {
        //Just Gender = Female
        testFilterMetrics(false, -1, -1, null, -1, -1);

        assertEquals("Testing Filter ClickLogs" , 11 , clickLogs.size());
        assertEquals("Testing Filter ImpressionLogs" , 28 , impressionLogs.size());
        assertEquals("Testing Filter ServerLogs" , 11 , serverLogs.size());
    }

    @Test
    public  void testFiltersMaleAndAge() throws  Exception {
        //Just Gender = Male and Age = 35-44
        testFilterMetrics(true, 2, -1, null, -1, -1);

        assertEquals("Testing Filter ClickLogs" , 0 , clickLogs.size());
        assertEquals("Testing Filter ImpressionLogs" , 4 , impressionLogs.size());
        assertEquals("Testing Filter ServerLogs" , 0 , serverLogs.size());
    }

    @Test
    public  void testFiltersIncomeAndContext() throws  Exception {
        //Just Gender = Female
        testFilterMetrics(null, -1, 1, "Shopping", -1, -1);

        assertEquals("Testing Filter ClickLogs" , 3 , clickLogs.size());
        assertEquals("Testing Filter ImpressionLogs" , 5 , impressionLogs.size());
        assertEquals("Testing Filter ServerLogs" , 3 , serverLogs.size());
    }

    @Test
    public  void testFiltersGenderAndIncomeAndContext() throws  Exception {
        //Just Gender = Female
        testFilterMetrics(false, -1, 1, "Shopping", -1, -1);

        assertEquals("Testing Filter ClickLogs" , 3 , clickLogs.size());
        assertEquals("Testing Filter ImpressionLogs" , 5 , impressionLogs.size());
        assertEquals("Testing Filter ServerLogs" , 3 , serverLogs.size());
    }
    
}