package org.joda.time.convert;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for the {@link StringConverter} class, focusing on its exception-handling capabilities.
 * This version has been refactored from an auto-generated test for improved clarity.
 */
public class StringConverter_ESTestTest1 extends StringConverter_ESTest_scaffolding {

    /**
     * Verifies that getDurationMillis() throws an IllegalArgumentException when provided
     * with a string that does not conform to the ISO 8601 duration format.
     * <p>
     * The standard format must start with 'P', optionally followed by date components,
     * then 'T', followed by time components (e.g., "PT5S" for 5 seconds). The input
     * string "Pt,v.y" is invalid because it uses a lowercase 't'.
     */
    @Test(timeout = 4000)
    public void getDurationMillis_shouldThrowIllegalArgumentException_forInvalidFormatString() {
        // Arrange
        StringConverter converter = StringConverter.INSTANCE;
        String invalidDurationString = "Pt,v.y";

        // Act & Assert
        try {
            converter.getDurationMillis(invalidDurationString);
            fail("Expected an IllegalArgumentException to be thrown for the invalid format.");
        } catch (IllegalArgumentException e) {
            // Verify that the exception message is clear and correctly identifies the invalid input.
            String expectedMessage = "Invalid format: \"" + invalidDurationString + "\"";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}