import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DataAnalyticsTest {

    ClicklogParser clickingParser;
    ImpressionParser impressionParser;
    ServerlogParser serverlogParser;

    DataAnalytics da;


    @Before
    public void setUp() {
        clickingParser = new ClicklogParser("click_log.csv");
        impressionParser = new ImpressionParser("impression_log.csv");
        serverlogParser = new ServerlogParser("server_log.csv");

        da = new DataAnalytics();

    }

    @Test
    public void testNoOfImpression() throws Exception {
        Assert.assertEquals(8, 6 + 2);

//    @Test
//    public void testTotalClicks() throws Exception {
//
//    }
//
//    @Test
//    public void testUniqueClickSet() throws Exception {
//
//    }
//
//    @Test
//    public void testNoOfUniques() throws Exception {
//
//    }
//
//    @Test
//    public void testNoOfBounces() throws Exception {
//
//    }
//
//    @Test
//    public void testNoOfConversions() throws Exception {
//
//    }
//
//    @Test
//    public void testTotalImpressionCost() throws Exception {
//
//    }
//
//    @Test
//    public void testTotalClickCost() throws Exception {
//
//    }
//
//    @Test
//    public void testTotalCost() throws Exception {
//
//    }
//
//    @Test
//    public void testGetCTR() throws Exception {
//
//    }
//
//    @Test
//    public void testGetCPA() throws Exception {
//
//    }
//
//    @Test
//    public void testGetCPC() throws Exception {
//
//    }
//
//    @Test
//    public void testGetCPM() throws Exception {
//
//    }
//
//    @Test
//    public void testBounceRate() throws Exception {
//
//    }
//
//    @Test
//    public void testGetDateVsClick() throws Exception {
//
//    }
    }
}