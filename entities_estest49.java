package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test suite for the {@link Entities} class.
 */
public class EntitiesTest {

    /**
     * Verifies that the escape method correctly converts the less-than character ('<')
     * into its corresponding HTML entity ("&lt;").
     */
    @Test
    public void escape_shouldConvertLessThanCharacter_toEntity() {
        // Arrange: Define an input string containing a special character and the expected result after escaping.
        String input = "/*<![CDATA[*/\n";
        String expectedOutput = "/*&lt;![CDATA[*/\n";

        // Act: Call the method under test.
        String actualOutput = Entities.escape(input);

        // Assert: Check if the output matches the expected escaped string.
        assertEquals(expectedOutput, actualOutput);
    }
}