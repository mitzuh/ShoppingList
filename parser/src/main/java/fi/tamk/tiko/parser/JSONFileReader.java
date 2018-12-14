package fi.tamk.tiko.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.LinkedList;

/**
 * This is the class for JSONFile Reader.
 * 
 * @author      Jimi Savola
 * @version     1.8, 2018.1117
 * @since       2018.1212
 */
public class JSONFileReader {
    /**
     * Constructs JSONFileReader.
     */
    public JSONFileReader() {
    }

    /**
     * Reads the .json shopping list file and returns JSONObjects as a list.
     * @param file User chosen file to read.
     * @return JSONObjects from the .json file.
     */
    public LinkedList<JSONObject> readFile(File file) {
        LinkedList<JSONObject> list = new LinkedList<>();
        BufferedReader reader;

        try {
            reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();
            String product = "";
            String quantity = "";
            JSONObject newObject = new JSONObject();

            while (line != null) {
                if(line.contains("\"Product\"")) {
                    newObject = new JSONObject();
                    product = checkLine(line);
                    line = reader.readLine();
                }
                if(line.contains("\"Quantity\"")) {
                    quantity = checkLine(line);
                    newObject.put("Product",product);
                    newObject.put("Quantity",quantity);
                    list.add(newObject);
                }
                line = reader.readLine();
            }
            reader.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Checks the current line from the file, and returns the value.
     * @param line Current line of the file.
     * @return Value of the JSONObjects Key
     */
    public String checkLine(String line) {
        String value = "";

        for (int i=0; i<line.length(); i++) {
            if(line.charAt(i) == ':') {
                int j = i;
                while (line.charAt(j) != '"' && j<line.length()-1 && i<line.length()) {
                    j++;
                    if(line.charAt(j) == '"') {
                        j++;
                        while (line.charAt(j) != '"') {
                            value = value + line.charAt(j);
                            j++;
                        }
                        i = line.length();
                    }
                }
            }
        }
        return value;
    }
}