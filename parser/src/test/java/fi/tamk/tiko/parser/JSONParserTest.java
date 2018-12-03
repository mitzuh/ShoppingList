package fi.tamk.tiko.parser;

import org.junit.Test;
import static org.junit.Assert.*;

import fi.tamk.tiko.parser.JSONParser;

public class JSONParserTest {

    @Test
    public void testTabs() {
        JSONParser testParser = new JSONParser();

        assertEquals("    ", testParser.tabs(1));
        assertEquals("        ", testParser.tabs(2));
    }
}