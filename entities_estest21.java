package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link Entities} class, focusing on character escaping.
 */
public class EntitiesTest {

    /**
     * Verifies that the escape() method correctly converts a double quote character (")
     * into its corresponding HTML entity (&quot;), while leaving other characters unchanged.
     */
    @Test
    public void escapeShouldEncodeDoubleQuoteToQuotEntity() {
        // Arrange: Define an input string containing a double quote.
        String originalString = "This is a string with a \"double quote\".";
        String expectedEscapedString = "This is a string with a &quot;double quote&quot;.";

        // Act: Call the escape method on the input string.
        String actualEscapedString = Entities.escape(originalString);

        // Assert: Verify that the double quote was correctly escaped.
        assertEquals(expectedEscapedString, actualEscapedString);
    }
}