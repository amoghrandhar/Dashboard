/**
 * Created by Amogh on 20-02-2015.
 */
public class WrongFileException extends Exception {

    String fileName;

    WrongFileException(String name) {
        super();
        fileName = name;
    }

}
