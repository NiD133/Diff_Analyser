package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link Entities#unescape(String)} method.
 */
public class EntitiesUnescapeTest {

    /**
     * Verifies that the unescape method correctly handles an empty string input
     * by returning an empty string.
     */
    @Test
    public void unescape_withEmptyString_returnsEmptyString() {
        // Arrange: Define the input and the expected outcome.
        String input = "";
        String expected = "";

        // Act: Execute the method under test.
        String result = Entities.unescape(input);

        // Assert: Verify that the actual result matches the expected outcome.
        assertEquals("Unescaping an empty string should yield an empty string.", expected, result);
    }
}