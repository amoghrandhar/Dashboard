import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Created by Amogh on 22-02-2015.
 */
public class DataAnalytics {

    public  static  long noOfImpression(ArrayList<ImpressionLog> impressionArrayList) {
        //This returns the total no. of impressions
        return impressionArrayList.size();
    }

    public  static  long totalClicks(ArrayList<ClickLog> clickLogArrayList) {
        return clickLogArrayList.size();
    }

    public  static HashSet<ClickLog> uniqueClickSet(ArrayList<ClickLog> clickLogArrayList) {
        //This will return a Hash Set of ClickLogs based on unique ID of the user
        //So it removes the repetition
        return new HashSet(clickLogArrayList);
    }

    public static  long noOfUniques(ArrayList<ClickLog> clickLogArrayList) {
        //It tells the no. of unique users who clicked
        return (uniqueClickSet(clickLogArrayList)).size();
    }

    public static  long noOfBounces(ArrayList<ServerLog> slog) {
        //It returns the total no bounces happened, compared and based on the bounce property
        
        return slog.size();
        
    }

    public static  long noOfConversions(ArrayList<ServerLog> slog) {
        //It tells the total no. of conversions which happened
        long total = 0;
        for (ServerLog aSlog : slog) {
            if (aSlog.isConverted()) {
                total++;
            }
        }
        return total;
    }


    public static  Double totalImpressionCost(ArrayList<ImpressionLog> impressionArrayList) {
        //Returns total money spent on impressions in Pounds
        double total = 0;
        for (ImpressionLog anImpressionArrayList : impressionArrayList) {
            total = total + anImpressionArrayList.getImpression();
        }
        return total / 100;
    }


    public  static Double totalClickCost(ArrayList<ClickLog> clickLogArrayList) {
        //Returns total money spent on Clicks in Pounds
        double total = 0;
        for (ClickLog aClickLogArrayList : clickLogArrayList) {
            total = total + aClickLogArrayList.getClickCost();
        }
        return total / 100;
    }

    public static  Double totalCost(ArrayList<ImpressionLog> impressionArrayList, ArrayList<ClickLog> clickLogArrayList) {
        // It returns the sum of the total cost  spent on impressions + clicks
        return (totalImpressionCost(impressionArrayList) + totalClickCost(clickLogArrayList));
    }


    public static  Double getCTR(ArrayList<ClickLog> clickLogArrayList, ArrayList<ImpressionLog> impressionArrayList) {
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

    public static  Double getCPA(ArrayList<ImpressionLog> impressionArrayList, ArrayList<ClickLog> clickLogArrayList, ArrayList<ServerLog> slog) {
        // This returns the CPA
        return totalCost(impressionArrayList, clickLogArrayList) / noOfConversions(slog);
    }


    public static  Double getCPC(ArrayList<ImpressionLog> impressionArrayList, ArrayList<ClickLog> clickLogArrayList) {
        // This returns the CPC
        return totalCost(impressionArrayList, clickLogArrayList) / totalClicks(clickLogArrayList);
    }

    public static  Double getCPM(ArrayList<ImpressionLog> impressionArrayList, ArrayList<ClickLog> clickLogArrayList) {
        // This returns the CPM
        return (totalCost(impressionArrayList, clickLogArrayList) / noOfImpression(impressionArrayList)) * 1000;
    }

    public static Double bounceRate( ArrayList<ClickLog> clickLogArrayList, ArrayList<ServerLog> slog) {
        // This returns the average bounceRate
        return 1 - ((double) noOfBounces(slog)) / totalClicks(clickLogArrayList);
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
    public static  HashMap<Boolean, Long> sexRatioDivision(ArrayList<ImpressionLog> impressionLogs) {
        HashMap<Boolean, Long> counted = impressionLogs.parallelStream()
                .collect(Collectors.groupingBy(imp -> imp.getGender(), HashMap::new, Collectors.counting()));
        return counted;
    }


    public static  HashMap<Integer, Long> ageGroupDivision(ArrayList<ImpressionLog> impressionLogs) {
        HashMap<Integer, Long> counted = impressionLogs.parallelStream()
                .collect(Collectors.groupingBy(imp -> imp.getAgeGroup(), HashMap::new, Collectors.counting()));
        return counted;
    }

    public static  HashMap<Integer, Long> incomeGroupDivision(ArrayList<ImpressionLog> impressionLogs) {
        HashMap<Integer, Long> counted = impressionLogs.parallelStream()
                .collect(Collectors.groupingBy(imp -> imp.getIncomeGroup(), HashMap::new, Collectors.counting()));
        return counted;
    }


    public static  HashMap<String, Long> contextGroupDivision(ArrayList<ImpressionLog> impressionLogs) {
        HashMap<String, Long> counted = impressionLogs.parallelStream()
                .collect(Collectors.groupingBy(imp -> imp.getContext(), HashMap::new, Collectors.counting()));
        return counted;
    }





    /*

    ---------> Filtering Data

     */


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
    public  static List<ImpressionLog> filterImpressionLogs(Predicate<ImpressionLog> datePredicate, Predicate<ImpressionLog> genderPredicate
            , Predicate<ImpressionLog> agePredicate, Predicate<ImpressionLog> incomePredicate
            , Predicate<ImpressionLog> contextPredicate, ArrayList<ImpressionLog> impressionLogs) {


        return impressionLogs.parallelStream()
        		.filter(datePredicate)
        		.filter(genderPredicate)
        		.filter(agePredicate)
        		.filter(incomePredicate)
        		.filter(contextPredicate)
        		.collect(Collectors.<ImpressionLog>toList());
    }



    /**
     * This Will filter the Clicklogs on the basis of DatePredicate
     *@param idCheck
     * @param datePred
     * @param clickLogs
     * @return filtered Clicklogs
     */
    public static  List<ClickLog> filterClickLogs(Predicate<ClickLog> datePred, ArrayList<ClickLog> clickLogs , Set<Double> idCheck) {

        Predicate<ClickLog> checkClicks = clp -> idCheck.contains(clp.getID());
        return clickLogs.parallelStream().filter(checkClicks).filter(datePred).collect(Collectors.<ClickLog>toList());
    }


    /**
     * This will filter Impression Logs on the basis of following Predicate
     *
     * @param beforeDate
     * @param afterDate
     * @param pagesV
     * @param serverLogs
     * @return
     */
    public static  List<ServerLog> filterServerLogs(Predicate<ServerLog> beforeDate, Predicate<ServerLog> afterDate
            , Predicate<ServerLog> pagesV, Predicate<ServerLog> timeSpent
            , ArrayList<ServerLog> serverLogs , Set<Double> idCheck) {

        Predicate<ServerLog> checkServers = sp -> idCheck.contains(sp.getID());

        return serverLogs.parallelStream()
                .filter(checkServers)
        		.filter(beforeDate)
        		.filter(afterDate)
        		.filter(pagesV)
        		.filter(timeSpent)
        		.collect(Collectors.<ServerLog>toList());
        
    }


}
