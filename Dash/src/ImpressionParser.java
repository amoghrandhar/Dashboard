import com.univocity.parsers.common.ParsingContext;
import com.univocity.parsers.common.processor.ObjectRowProcessor;
import com.univocity.parsers.conversions.Conversions;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

/**
 * Created by Amogh on 19-02-2015.
 */
public class ImpressionParser implements Runnable {
    // creates a CSV parser

    ArrayList<Impression> impressions;


    ImpressionParser() {
        impressions = new ArrayList<Impression>();
    }

    public static void main(String[] args) throws WrongFileException {

        System.out.println(new File(".").getAbsolutePath());

        ImpressionParser ip = new ImpressionParser();
        ip.generateImpressionsMethod1();
//        ip.generateImpressionsMethod2();

        HashSet<Impression> him = new HashSet<Impression>();
        him.addAll(ip.getImpressions());
        System.out.println(him.size());


    }

    public void generateImpressionsMethod1() throws WrongFileException {

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            System.out.println("UNI Velocity --- One By One");
            long startTime = System.currentTimeMillis();

            CsvParserSettings settings = new CsvParserSettings();
            settings.setHeaderExtractionEnabled(true);      // This will remove the header data from csv
            CsvParser parser = new CsvParser(settings);
            // call beginParsing to read records one by one, iterator-style.
            parser.beginParsing(new FileReader("impression_log.csv"));


            String[] row;
            while ((row = parser.parseNext()) != null) {
                if (row[3].equals("<25")) {
                    row[3] = "0";
                } else if (row[3].equals("25-34")) {
                    row[3] = "1";
                } else if (row[3].equals("35-44")) {
                    row[3] = "2";
                } else if (row[3].equals("45-54")) {
                    row[3] = "3";
                } else {
                    row[3] = "4";
                }

                if (row[4].equals("Low")) {
                    row[4] = "0";
                } else if (row[3].equals("Medium")) {
                    row[4] = "1";
                } else {
                    row[4] = "2";
                }

                impressions.add(new Impression(sdf.parse(row[0]), Double.parseDouble(row[1]), "Male".equals(row[2]), Integer.valueOf(row[3]), Integer.valueOf(row[4]), row[5], Double.parseDouble(row[6])));
            }

            long endTime = System.currentTimeMillis();
            System.out.println(impressions.size());
            System.out.println(impressions.get(0).getGender());

            long totalTime = endTime - startTime;
            System.out.println(totalTime);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
            throw new WrongFileException("Impression File : Wrong Date Format");
        }

    }

    public void generateImpressionsMethod2() {
        try {
            System.out.println("UNI Velocity --- Object processor");
            long startTime = System.currentTimeMillis();


            // ObjectRowProcessor converts the parsed values and gives you the resulting row.
            ObjectRowProcessor rowProcessor = new ObjectRowProcessor() {
                @Override
                public void rowProcessed(Object[] row, ParsingContext context) {
                    //here is the row. Let's just print it.
                    impressions.add(new Impression((Date) row[0], (Double) row[1], (Boolean) row[2], (Integer) row[3], (Integer) row[4], (String) row[5], (Double) row[6]));
                }
            };

            // converts values in the "Price" column (index 4) to BigDecimal
            rowProcessor.convertIndexes(Conversions.toDate("yyyy-MM-dd HH:mm:ss")).set(0);
            rowProcessor.convertIndexes(Conversions.toDouble()).set(1);
            rowProcessor.convertIndexes(Conversions.toBoolean("Male", "Female")).set(2);
            rowProcessor.convertIndexes(Conversions.replace("<25", "0"),
                    Conversions.replace("25-34", "1"),
                    Conversions.replace("35-44", "2"),
                    Conversions.replace("45-54", "3"),
                    Conversions.replace(">54", "4"), Conversions.toInteger()).set(3);
            rowProcessor.convertIndexes(Conversions.replace("Low", "0"),
                    Conversions.replace("Medium", "1"),
                    Conversions.replace("High", "2"), Conversions.toInteger()).set(4);
            rowProcessor.convertIndexes(Conversions.toDouble()).set(6);

            CsvParserSettings parserSettings = new CsvParserSettings();
            parserSettings.setRowProcessor(rowProcessor);
            parserSettings.setHeaderExtractionEnabled(true);

            CsvParser parser = new CsvParser(parserSettings);

            //the rowProcessor will be executed here.
            parser.parse(new FileReader("impression_log.csv"));

            long endTime = System.currentTimeMillis();
            // System.out.println(rows.size());

            System.out.println(impressions.size());
            System.out.println(impressions.get(0).getAgeGroup());

            long totalTime = endTime - startTime;
            System.out.println(totalTime);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public ArrayList<Impression> getImpressions() {
        return impressions;
    }

    @Override
    public void run() {
        try {
            this.generateImpressionsMethod1();
        } catch (WrongFileException e) {
            e.printStackTrace();
        }
    }
}
