package com.google.common.base;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Unit tests for {@link CaseFormat}.
 */
public class CaseFormatTest {

    /**
     * Tests the conversion from LOWER_UNDERSCORE to UPPER_CAMEL for a string
     * that doesn't strictly conform to the source format (as it contains no underscores).
     * The expected behavior is a "best effort" conversion, treating the entire
     * string as a single word and capitalizing it according to UPPER_CAMEL rules.
     */
    @Test
    public void to_fromLowerUnderscoreToUpperCamel_withNonConformingSingleWord() {
        // Arrange: Define the input string and the expected outcome.
        // The input "bDU\"5" is treated as a single word because it lacks the '_' separator.
        String input = "bDU\"5";
        String expectedOutput = "Bdu\"5"; // UPPER_CAMEL capitalizes the first letter and lowercases the rest.

        // Act: Perform the conversion from LOWER_UNDERSCORE to UPPER_CAMEL.
        String result = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, input);

        // Assert: Verify that the result matches the expected format.
        assertEquals(expectedOutput, result);
    }
}