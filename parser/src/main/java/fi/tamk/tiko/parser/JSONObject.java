package fi.tamk.tiko.parser;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * This is the class for JSONObjects.
 * 
 * @author      Jimi Savola
 * @version     1.8, 2018.1117
 * @since       2018.1107
 */
public class JSONObject extends LinkedHashMap implements Map {

    /**
     * Constructs JSONObject.
     */
    public JSONObject() {

    }

    /**
     * Returns the value from key "Product".
     * @return value of the JSONObjects key, "Product".
     */
    public String getProduct() {
        return (String) this.get("Product");
    }

    /**
     * Returns the value from key "Quantity".
     * @return value of the JSONObjects key, "Quantity".
     */
    public String getQuantity() {
        return (String) this.get("Quantity");
    }
    
}