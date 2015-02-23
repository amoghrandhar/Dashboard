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
public class ServerlogParser implements Runnable {

    private ArrayList<ServerLog> serverLogs;

    ServerlogParser() {
        serverLogs = new ArrayList<ServerLog>();
    }


    public static void main(String[] args) {
        ServerlogParser slp = new ServerlogParser();
        slp.run();
    }

    @Override
    public void run() {
        try {
            generateServerLogs();
        } catch (WrongFileException e) {
            e.printStackTrace();
        }
    }

    public void generateServerLogs() throws WrongFileException {

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            System.out.println("UNI Velocity --- One By One");
            long startTime = System.currentTimeMillis();
            CsvParserSettings settings = new CsvParserSettings();
            settings.setHeaderExtractionEnabled(true);      // This will remove the header data from csv
            CsvParser parser = new CsvParser(settings);
            // call beginParsing to read records one by one, iterator-style.
            parser.beginParsing(new FileReader("server_log.csv"));
            String[] row;
            while ((row = parser.parseNext()) != null) {
                if (row[2].equals("n/a")) {
                    serverLogs.add(new ServerLog(sdf.parse(row[0]), Double.parseDouble(row[1]), null, Integer.valueOf(row[3]), "Yes".equals(row[4])));
                } else {
                    serverLogs.add(new ServerLog(sdf.parse(row[0]), Double.parseDouble(row[1]), sdf.parse(row[2]), Integer.valueOf(row[3]), "Yes".equals(row[4])));
                }
            }

            long endTime = System.currentTimeMillis();
            System.out.println(serverLogs.size());

            long totalTime = endTime - startTime;
            System.out.println(totalTime);


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
            throw new WrongFileException("Impression File : Wrong Date Format");
        }
    }

    public ArrayList<ServerLog> getServerLogs() {
        return serverLogs;
    }
}

