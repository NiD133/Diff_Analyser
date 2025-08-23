package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Tests for the {@link PatternOptionBuilder} class.
 */
public class PatternOptionBuilderTest {

    /**
     * Tests that the character representing a String value type (':') is correctly
     * identified as a value code by the isValueCode method.
     */
    @Test
    public void isValueCode_shouldReturnTrue_forStringValueCode() {
        // Arrange: According to the PatternOptionBuilder documentation, the ':'
        // character is the value code for an option requiring a String argument.
        final char stringValueCode = ':';

        // Act: Call the method under test.
        final boolean isValue = PatternOptionBuilder.isValueCode(stringValueCode);

        // Assert: The method should correctly identify ':' as a value code.
        assertTrue("The colon character ':' should be recognized as a value code.", isValue);
    }
}