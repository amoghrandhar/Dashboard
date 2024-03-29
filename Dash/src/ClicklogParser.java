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

    private ArrayList<ClickLog> clickLogs;
    private String fileLocation;

    ClicklogParser(String fileLocation) {
        clickLogs = new ArrayList<ClickLog>();
        this.fileLocation = fileLocation;
    }

    public void generateClickLogs() throws WrongFileException {

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            System.out.println("UNI Velocity --- One By One");
//            long startTime = System.currentTimeMillis();
            CsvParserSettings settings = new CsvParserSettings();
            settings.setHeaderExtractionEnabled(true);      // This will remove the header data from csv
            CsvParser parser = new CsvParser(settings);
            // call beginParsing to read records one by one, iterator-style.
            parser.beginParsing(new FileReader(fileLocation));
            String[] row;
            while ((row = parser.parseNext()) != null) {
                if (row.length == 3) {
                    clickLogs.add(new ClickLog(sdf.parse(row[0]), Double.parseDouble(row[1]), Double.parseDouble(row[2])));
                }
            }

//            long endTime = System.currentTimeMillis();
//            System.out.println(clickLogs.size());
//            long totalTime = endTime - startTime;
//            System.out.println(totalTime);


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
            throw new WrongFileException("ClickLog File");
        }
    }

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
