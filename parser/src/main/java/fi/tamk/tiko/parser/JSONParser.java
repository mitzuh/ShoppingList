package fi.tamk.tiko.parser;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.LinkedList;

/**
 * This is the parser class, which is used to write .json -files.
 * 
 * @author      Jimi Savola
 * @version     1.8, 2018.1119
 * @since       2018.1107
 */
public class JSONParser {
    private int tabNumber = 1;
    private char lastChar = ' ';
    private boolean insideArray = false;

    /**
     * Constructs the JSONParser.
     */
    public JSONParser() {
        
    }

    /**
     * Writes JSONObject values to JSON file.
     * @param obj JSONObject to print on the file.
     * @param out BufferedWriter, which writes the values to the JSON file.
     */
    public void writeJSONString(JSONObject obj, BufferedWriter out) {
        try {
            /*if (!insideArray) {
                out.write("{");
                out.newLine();
                out.write(tabs(tabNumber));
            }
            else {
                out.newLine();
                out.write(tabs(tabNumber));
                out.write("{");
                out.newLine();
                out.write(tabs(tabNumber));
            }*/
            out.write("{");
            out.newLine();
            out.write(tabs(tabNumber));

            boolean newLine = true;

            Iterator it = obj.entrySet().iterator();

            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();

                if (lastChar == '}') {
                    out.write(",");
                    out.newLine();
                    out.write(tabs(tabNumber));
                    lastChar = ' ';
                }

                // If the current value on the map is JSONObject, a new inner JSON data is made to the current key
                Object o = pair.getValue();
                if(o instanceof Map) {
                    tabNumber++;

                    out.write("\"" + pair.getKey() + "\"" +  ": ");
                    writeJSONString((JSONObject)pair.getValue(), out);
                    tabNumber--;
                }

                // If the current value of JSONObject is an array.
                else if(o instanceof LinkedList || o instanceof ArrayList) {
                    insideArray = true;
                    LinkedList<JSONObject> arrayObj = (LinkedList) pair.getValue();
                    tabNumber++;
                    out.write("\"" + pair.getKey() + "\"" +  ": ");
                    out.write("[");
                    out.newLine();
                    out.write(tabs(tabNumber));

                    for(int i=0; i<arrayObj.size(); i++) {
                        writeJSONString(arrayObj.get(i), out);
                        if(i < arrayObj.size()-1) {
                            out.write(",");
                        }
                        out.newLine();
                        if(i == arrayObj.size()-1) {
                            out.write(tabs(tabNumber-1));
                        }
                        else {
                            out.write(tabs(tabNumber));
                        }
                    }
                    tabNumber--;
                    insideArray = false;
                    out.write("]");
                    if (it.hasNext()) {
                        out.write(",");
                        out.newLine();
                        out.write(tabs(tabNumber));
                    }
                }

                else if(it.hasNext()) {
                    out.write("\"" + pair.getKey() + "\"" +  ": " + "\"" + pair.getValue() + "\",");
                    out.newLine();
                    out.write(tabs(tabNumber));
                }
                else {
                    out.write("\"" + pair.getKey() + "\"" +  ": " + "\"" + pair.getValue() + "\"");
                    out.newLine();
                    newLine = false;
                }
                it.remove();
                out.flush();
            }

            if(insideArray) {
                out.write(tabs(tabNumber) + "}");
                lastChar = '}';
            }
            else if (newLine) {
                out.newLine();
                out.write(tabs(tabNumber-1) + "}");
                lastChar = '}';
            }
            else {
                out.write(tabs(tabNumber-1) + "}");
                lastChar = '}';
            }

            if(!insideArray) {
                out.newLine();
                out.flush();
                lastChar = ' ';
            }
            else {
                out.flush();
                lastChar = ' ';
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Makes 4 space tabs.
     * @param amount Number of wanted tabs.
     * @return String, which contains the amount of tabs wanted.
     */
    public String tabs(int amount) {
        String tabs = "";
        for(int i=0; i<amount; i++) {
            tabs = tabs + "    ";
        }
        return tabs;
    }
}