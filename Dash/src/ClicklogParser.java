import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by Amogh on 22-02-2015.
 */
public class ClicklogParser implements Runnable {

    ArrayList<ClickLog> clickLogs;

    ClicklogParser() {
        clickLogs = new ArrayList<ClickLog>();
    }

    public static void main(String[] args) {
        ClicklogParser clp = new ClicklogParser();
        clp.run();
    }

    public void generateClickLogs() throws WrongFileException {

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            System.out.println("UNI Velocity --- One By One");
            long startTime = System.currentTimeMillis();
            CsvParserSettings settings = new CsvParserSettings();
            settings.setHeaderExtractionEnabled(true);      // This will remove the header data from csv
            CsvParser parser = new CsvParser(settings);
            // call beginParsing to read records one by one, iterator-style.
            parser.beginParsing(new FileReader("click_log.csv"));
            String[] row;
            while ((row = parser.parseNext()) != null) {
                clickLogs.add(new ClickLog(sdf.parse(row[0]), Double.parseDouble(row[1]), Double.parseDouble(row[2])));
            }

            long endTime = System.currentTimeMillis();
            System.out.println(clickLogs.size());

            long totalTime = endTime - startTime;
            System.out.println(totalTime);


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
            throw new WrongFileException("Impression File : Wrong Date Format");
        }
    }

    @Override
    public void run() {
        try {
            generateClickLogs();
        } catch (WrongFileException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<ClickLog> getClickLogs() {
        return clickLogs;
    }
}
