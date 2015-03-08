import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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
        return new LinkedHashSet(clickLogArrayList);

    }

    public long noOfUniques(ArrayList<ClickLog> clickLogArrayList) {
        //It tells the no. of unique users who clicked
        return (uniqueClickSet(clickLogArrayList)).size();
    }

    public long noOfBounces(ArrayList<ServerLog> slog, int bounceProperty) {
        //It returns the total no bounces happened, compared and based on the bounce property
        long total = 0;
        for (ServerLog aSlog : slog) {
            if (aSlog.getPagesViewed() < bounceProperty) {
                total++;
            }
        }
        return total;
    }

    public long noOfConversions(ArrayList<ServerLog> slog) {
        //It tells the total no. of conversions which happened
        long total = 0;
        for (ServerLog aSlog : slog) {
            if (aSlog.isConverted()) {
                total++;
            }
        }
        return total;
    }


    public Double totalImpressionCost(ArrayList<ImpressionLog> impressionArrayList) {
        //Returns total money spent on impressions in Pounds
        double total = 0;
        for (ImpressionLog anImpressionArrayList : impressionArrayList) {
            total = total + anImpressionArrayList.getImpression();
        }
        return total / 100;
    }


    public Double totalClickCost(ArrayList<ClickLog> clickLogArrayList) {
        //Returns total money spent on Clicks in Pounds
        double total = 0;
        for (ClickLog aClickLogArrayList : clickLogArrayList) {
            total = total + aClickLogArrayList.getClickCost();
        }
        return total / 100;
    }

    public Double totalCost(ArrayList<ImpressionLog> impressionArrayList, ArrayList<ClickLog> clickLogArrayList) {
        // It returns the sum of the total cost  spent on impressions + clicks
        return (totalImpressionCost(impressionArrayList) + totalClickCost(clickLogArrayList));
    }


    public Double getCTR(ArrayList<ClickLog> clickLogArrayList, ArrayList<ImpressionLog> impressionArrayList) {
        //Returns the CTR
        return ((double) totalClicks(clickLogArrayList)) / impressionArrayList.size();
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
        return totalCost(impressionArrayList, clickLogArrayList) / noOfConversions(slog);
    }


    public Double getCPC(ArrayList<ImpressionLog> impressionArrayList, ArrayList<ClickLog> clickLogArrayList) {
        // This returns the CPC
        return totalCost(impressionArrayList, clickLogArrayList) / totalClicks(clickLogArrayList);
    }

    public Double getCPM(ArrayList<ImpressionLog> impressionArrayList, ArrayList<ClickLog> clickLogArrayList) {
        // This returns the CPM
        return (totalCost(impressionArrayList, clickLogArrayList) / noOfImpression(impressionArrayList)) * 1000;
    }

    public Double bounceRate(int bounceProperty, ArrayList<ClickLog> clickLogArrayList, ArrayList<ServerLog> slog) {
        // This returns the average bounceRate
        return ((double) noOfBounces(slog, bounceProperty)) / totalClicks(clickLogArrayList);
    }




    /*

    ---------> Pie Chart Data

     */




    /**
     * This will tell the no of males and no of females present.
     *
     * @param impressionLogs
     * @return Map
     */
    public HashMap<Boolean, Long> sexRatioDivision(ArrayList<ImpressionLog> impressionLogs) {
        HashMap<Boolean, Long> counted = impressionLogs.parallelStream()
                .collect(Collectors.groupingBy(imp -> imp.getGender(), HashMap::new, Collectors.counting()));
        return counted;
    }


    public HashMap<Integer, Long> ageGroupDivision(ArrayList<ImpressionLog> impressionLogs) {
        HashMap<Integer, Long> counted = impressionLogs.parallelStream()
                .collect(Collectors.groupingBy(imp -> imp.getAgeGroup(), HashMap::new, Collectors.counting()));
        return counted;
    }

    public HashMap<Integer, Long> incomeGroupDivision(ArrayList<ImpressionLog> impressionLogs) {
        HashMap<Integer, Long> counted = impressionLogs.parallelStream()
                .collect(Collectors.groupingBy(imp -> imp.getIncomeGroup(), HashMap::new, Collectors.counting()));
        return counted;
    }


    public HashMap<String, Long> contextGroupDivision(ArrayList<ImpressionLog> impressionLogs) {
        HashMap<String, Long> counted = impressionLogs.parallelStream()
                .collect(Collectors.groupingBy(imp -> imp.getContext(), HashMap::new, Collectors.counting()));
        return counted;
    }





    /*

    ---------> Filtering Data

     */


    /**
     * This Will filter the Clicklogs on the basis of DatePredicate
     *
     * @param datePred
     * @param clickLogs
     * @return filtered Clicklogs
     */
    public List<ClickLog> filterClickLogs(Predicate<ClickLog> datePred, ArrayList<ClickLog> clickLogs) {
        return clickLogs.stream().filter(datePred).collect(Collectors.<ClickLog>toList());
    }

    /**
     * This will Filter ImpressionLogs on the basis of following predicates
     *
     * @param datePredicate
     * @param genderPredicate
     * @param agePredicate
     * @param incomePredicate
     * @param contextPredicate
     * @param impressionLogs
     * @return filtered list
     */
    public List<ImpressionLog> filterImpressionLogs(Predicate<ImpressionLog> datePredicate, Predicate<ImpressionLog> genderPredicate
            , Predicate<ImpressionLog> agePredicate, Predicate<ImpressionLog> incomePredicate
            , Predicate<ImpressionLog> contextPredicate, ArrayList<ImpressionLog> impressionLogs) {


        return impressionLogs.parallelStream().filter(datePredicate).filter(genderPredicate).filter(agePredicate).filter(incomePredicate).filter(contextPredicate).collect(Collectors.<ImpressionLog>toList());
    }

    /**
     * This will filter Impression Logs on the basis of following Predicate
     *
     * @param beforeDate
     * @param afterDate
     * @param pagesV
     * @param conV
     * @param serverLogs
     * @return
     */
    public List<ServerLog> filterServerLogs(Predicate<ServerLog> beforeDate, Predicate<ServerLog> afterDate
            , Predicate<ServerLog> pagesV, Predicate<ServerLog> conV
            , ArrayList<ServerLog> serverLogs) {

        return serverLogs.stream().filter(beforeDate).filter(afterDate).filter(pagesV).filter(conV).collect(Collectors.<ServerLog>toList());
    }


}
