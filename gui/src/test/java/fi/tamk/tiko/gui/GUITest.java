package fi.tamk.tiko.gui;

import org.junit.Test;
import static org.junit.Assert.*;

import fi.tamk.tiko.gui.GUI;
import fi.tamk.tiko.parser.JSONObject;

public class GUITest {

    @Test
    public void testIsPlusInteger() {
        GUI testGUI = new GUI();

        assertEquals(true, testGUI.isPlusInteger("3"));
    }
}