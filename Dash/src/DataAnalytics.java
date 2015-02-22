import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by Amogh on 22-02-2015.
 */
public class DataAnalytics {

    public long noOfImpression(ArrayList<Impression> impressionArrayList) {
        //This returns the total no. of impressions
        return impressionArrayList.size();
    }

    public long totalClicks(ArrayList<ClickLog> clickLogArrayList) {
        //This will tell you total no. of click records
        return clickLogArrayList.size();
    }

    public HashSet<ClickLog> uniqueClickSet(ArrayList<ClickLog> clickLogArrayList) {
        //This will return a Hash Set of ClickLogs based on unique ID of the user
        //So it removes the repetition
        return new HashSet<ClickLog>(clickLogArrayList);
    }

    public long noOfUniques(ArrayList<ClickLog> clickLogArrayList) {
        //It tells the no. of unique users who clicked
        return (uniqueClickSet(clickLogArrayList)).size();
    }

    public long noOfBounces(ArrayList<ServerLog> slog, int bounceProperty) {
        //It returns the total no bounces happened, compared and based on the bounce property
        long total = 0;
        for (int i = 0; i < slog.size(); i++) {
            if (slog.get(i).getPagesViewed() >= bounceProperty) {
                total++;
            }
        }
        return total;
    }

    public long noOfConversions(ArrayList<ServerLog> slog) {
        //It tells the total no. of conversions which happened
        long total = 0;
        for (int i = 0; i < slog.size(); i++) {
            if (slog.get(i).isConversation()) {
                total++;
            }
        }
        return total;
    }


    public double totalImpressionCost(ArrayList<Impression> impressionArrayList) {
        //Returns total money spent on impressions in Penny
        double total = 0;
        for (int i = 0; i < impressionArrayList.size(); i++) {
            total = total + impressionArrayList.get(i).getImpression();
        }
        return total;
    }


    public double totalClickCost(ArrayList<ClickLog> clickLogArrayList) {
        //Returns total money spent on Clicks in Penny
        double total = 0;
        for (int i = 0; i < clickLogArrayList.size(); i++) {
            total = total + clickLogArrayList.get(i).getImpression();
        }
        return total;
    }

    public Double totalCost(ArrayList<Impression> impressionArrayList, ArrayList<ClickLog> clickLogArrayList) {
        // It returns the sum of the total cost  spent on impressions + clicks
        double total = totalImpressionCost(impressionArrayList) + totalClickCost(clickLogArrayList);
        return total;
    }
}
