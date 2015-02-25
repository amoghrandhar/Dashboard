import java.util.*;

/**
 * Created by Amogh on 22-02-2015.
 */
public class DataAnalytics {

    public long noOfImpression(ArrayList<ImpressionLog> impressionArrayList) {
        //This returns the total no. of impressions
        return impressionArrayList.size();
    }

    public long totalClicks(ArrayList<ClickLog> clickLogArrayList) {
        //This will tell you total no. of click records
        //Its based on if the Click cost is greater than zero.
//        int total = 0;
//        for (int i = 0; i < clickLogArrayList.size(); i++) {
//            if (clickLogArrayList.get(i).getClickCost() > 0) {
//                total++;
//            }
//        }
//
//        return total;

        return clickLogArrayList.size();

    }

    public LinkedHashSet<ClickLog> uniqueClickSet(ArrayList<ClickLog> clickLogArrayList) {
        //This will return a Hash Set of ClickLogs based on unique ID of the user
        //So it removes the repetition
        return new LinkedHashSet<ClickLog>(clickLogArrayList);

    }

    public long noOfUniques(ArrayList<ClickLog> clickLogArrayList) {
        //It tells the no. of unique users who clicked
        return (uniqueClickSet(clickLogArrayList)).size();
    }

    public long noOfBounces(ArrayList<ServerLog> slog, int bounceProperty) {
        //It returns the total no bounces happened, compared and based on the bounce property
        long total = 0;
        for (int i = 0; i < slog.size(); i++) {
            if (slog.get(i).getPagesViewed() < bounceProperty) {
                total++;
            }
        }
        return total;
    }

    public long noOfConversions(ArrayList<ServerLog> slog) {
        //It tells the total no. of conversions which happened
        long total = 0;
        for (int i = 0; i < slog.size(); i++) {
            if (slog.get(i).isConverted()) {
                total++;
            }
        }
        return total;
    }


    public Double totalImpressionCost(ArrayList<ImpressionLog> impressionArrayList) {
        //Returns total money spent on impressions in Pounds
        double total = 0;
        for (int i = 0; i < impressionArrayList.size(); i++) {
            total = total + impressionArrayList.get(i).getImpression();
        }
        return total / 100;
    }


    public Double totalClickCost(ArrayList<ClickLog> clickLogArrayList) {
        //Returns total money spent on Clicks in Pounds
        double total = 0;
        for (int i = 0; i < clickLogArrayList.size(); i++) {
            total = total + clickLogArrayList.get(i).getClickCost();
        }
        return total / 100;
    }

    public Double totalCost(ArrayList<ImpressionLog> impressionArrayList, ArrayList<ClickLog> clickLogArrayList) {
        // It returns the sum of the total cost  spent on impressions + clicks
        double total = totalImpressionCost(impressionArrayList) + totalClickCost(clickLogArrayList);
        return total;
    }


    public Double getCTR(ArrayList<ClickLog> clickLogArrayList, ArrayList<ImpressionLog> impressionArrayList) {
        //Returns the CTR
        double ctr = ((double) totalClicks(clickLogArrayList)) / impressionArrayList.size();
        return ctr;
    }


    /*
    public double getCPA(ArrayList<ImpressionLog> impressionArrayList, ArrayList<ClickLog> clickLogArrayList, ArrayList<ServerLog> slog) {
    //This is the wrong CPA calculation -- // Old Code
        double total = 0;
        int im = 0;     //The current location on ImpressionLog list
        int cl = 0;     //The current location on click list
        for (int i = 0; i < slog.size(); i++) {

            if (slog.get(i).isConversation())
                for (int j = im; j < impressionArrayList.size(); j++) {
                    if (impressionArrayList.get(j).getDate().after(slog.get(i).getStartDate()) && impressionArrayList.get(j).getID() == slog.get(i).getID()) {
                        total = impressionArrayList.get(j).getImpression() + total;
                        im = j;
                        break;
                    }
                }
            for (int k = cl; k < clickLogArrayList.size(); k++) {
                if (clickLogArrayList.get(k).getDate().after(slog.get(i).getStartDate()) && clickLogArrayList.get(k).getID() == slog.get(i).getID()) {
                    total = clickLogArrayList.get(k).getClickCost() + total;
                    cl = k;
                    break;
                }
            }

        }
        return total;
    }
    */

    public Double getCPA(ArrayList<ImpressionLog> impressionArrayList, ArrayList<ClickLog> clickLogArrayList, ArrayList<ServerLog> slog) {
        // This returns the CPA
        double cpa = totalCost(impressionArrayList, clickLogArrayList) / noOfConversions(slog);
        return cpa;
    }


    public Double getCPC(ArrayList<ImpressionLog> impressionArrayList, ArrayList<ClickLog> clickLogArrayList) {
        // This returns the CPC
        double cpc = totalCost(impressionArrayList, clickLogArrayList) / totalClicks(clickLogArrayList);
        return cpc;
    }

    public Double getCPM(ArrayList<ImpressionLog> impressionArrayList, ArrayList<ClickLog> clickLogArrayList) {
        // This returns the CPM
        double cpm = (totalCost(impressionArrayList, clickLogArrayList) / noOfImpression(impressionArrayList)) * 1000;
        return cpm;
    }

    public Double bounceRate(int bounceProperty, ArrayList<ClickLog> clickLogArrayList, ArrayList<ServerLog> slog) {
        // This returns the average bounceRate
        double bRate = ((double) noOfBounces(slog, bounceProperty)) / totalClicks(clickLogArrayList);
        return bRate;
    }


    /*

    ---------> The Graph Methods Starts Here.

     */

    public ArrayList<Map<Date, Long>> getDateVsClick(ArrayList<ClickLog> clickLogs) {

        //AbstractMap.SimpleEntry dateLongSimpleEntry = new AbstractMap.SimpleEntry<Date, Long>(new Date(), new Long(45));

        Calendar cal = Calendar.getInstance();
        cal.setTime(clickLogs.get(0).getDate());

        int hr = cal.get(Calendar.DAY_OF_MONTH);

//        for (int i = 0 ; i < clickLogs.size() ; i++){
//            if(clickLogs.get(i).getDate())
//        }


        return null;
    }

}
