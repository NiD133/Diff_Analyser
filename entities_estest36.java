package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test suite for the {@link Entities} class, focusing on its string escaping functionality.
 */
public class EntitiesTest {

    /**
     * Verifies that the {@link Entities#escape(String)} method is null-safe and returns
     * an empty string when the input is null.
     */
    @Test
    public void escapeWithNullInputShouldReturnEmptyString() {
        // The escape method is expected to handle null input gracefully without throwing an exception.
        String escapedString = Entities.escape(null);

        // It should return an empty string as a safe default.
        assertEquals("Escaping a null string should result in an empty string.", "", escapedString);
    }
}