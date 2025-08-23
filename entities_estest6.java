package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link Entities} class.
 */
public class EntitiesTest {

    /**
     * Verifies that calling Entities.escape() with a null input string
     * returns an empty string, preventing NullPointerExceptions.
     */
    @Test
    public void escapeWithNullInputShouldReturnEmptyString() {
        // Arrange: Create the necessary configuration for the method under test.
        Document.OutputSettings outputSettings = new Document.OutputSettings();

        // Act: Call the escape method with a null input.
        String result = Entities.escape(null, outputSettings);

        // Assert: Verify that the result is an empty string as expected.
        assertEquals("", result);
    }
}