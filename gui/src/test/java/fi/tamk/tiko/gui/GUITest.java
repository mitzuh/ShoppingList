package fi.tamk.tiko.gui;

import org.junit.Test;
import static org.junit.Assert.*;

import fi.tamk.tiko.gui.GUI;
import fi.tamk.tiko.parser.JSONObject;

public class GUITest {
    GUI testGUI = new GUI();

    @Test
    public void testIsPlusInteger() {
        assertEquals(true, testGUI.isPlusInteger("3"));
    }

    @Test
    public void testSaveButtonClicked() {
        assertEquals(false, testGUI.saveButtonClicked());
    }
}