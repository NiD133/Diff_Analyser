package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the {@link PatternOptionBuilder} class.
 * This class focuses on verifying the behavior of its static utility methods.
 */
public class PatternOptionBuilderTest {

    /**
     * Verifies that the isValueCode() method correctly identifies the '%' character
     * as a valid value code. According to the PatternOptionBuilder documentation,
     * '%' signifies that an option expects a numeric argument.
     */
    @Test
    public void isValueCode_shouldReturnTrue_forNumberTypeCharacter() {
        // Arrange: The '%' character is the designated code for a Number type.
        final char numberValueCode = '%';

        // Act: Check if the character is recognized as a value code.
        final boolean isValue = PatternOptionBuilder.isValueCode(numberValueCode);

        // Assert: The method should return true for this valid code.
        assertTrue("The '%' character should be recognized as a value code for a Number type.", isValue);
    }
}