package org.jsoup.nodes;

import org.jsoup.nodes.Document.OutputSettings;
import org.junit.Test;

/**
 * Test suite for the {@link Entities} class, focusing on exception handling.
 */
public class EntitiesTest {

    /**
     * Verifies that the {@link Entities#escape(String, Document.OutputSettings)} method
     * throws a NullPointerException when the provided OutputSettings are null.
     * This is expected behavior, as the settings are required to determine the
     * character set and escape mode.
     */
    @Test(expected = NullPointerException.class)
    public void escapeWithNullOutputSettingsThrowsNullPointerException() {
        String input = "An example string with < & > characters.";
        OutputSettings nullSettings = null;

        Entities.escape(input, nullSettings);
    }
}