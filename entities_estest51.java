package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test suite for the {@link Entities} class, focusing on the unescape functionality.
 */
public class EntitiesTest {

    /**
     * Verifies that the unescape() method correctly decodes the named character reference for a double quote (&quot;).
     * This is a common and important entity to handle correctly.
     */
    @Test
    public void unescapeShouldConvertQuotEntityToDoubleQuote() {
        // Arrange: Define the input string with an HTML entity and the expected output.
        String textWithHtmlEntity = "uml&quot;";
        String expectedDecodedText = "uml\"";

        // Act: Call the method under test.
        String actualDecodedText = Entities.unescape(textWithHtmlEntity);

        // Assert: Check if the actual output matches the expected output.
        assertEquals(expectedDecodedText, actualDecodedText);
    }
}