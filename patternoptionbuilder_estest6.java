package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Tests for the {@link PatternOptionBuilder} class.
 */
public class PatternOptionBuilderTest {

    /**
     * Tests that isValueCode() returns false for a character that is not a defined
     * value type indicator. According to the PatternOptionBuilder documentation,
     * value codes are special characters like '@', ':', or '/'. This test verifies
     * that a common punctuation mark like a comma is correctly identified as not
     * being a value code.
     */
    @Test
    public void isValueCodeShouldReturnFalseForNonValueCodeCharacter() {
        // Arrange: A comma is not a special character representing an option's value type.
        final char nonValueCodeChar = ',';

        // Act: Check if the character is considered a value code.
        final boolean result = PatternOptionBuilder.isValueCode(nonValueCodeChar);

        // Assert: The result should be false.
        assertFalse("A comma should not be identified as a value code.", result);
    }
}