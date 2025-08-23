package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link PatternOptionBuilder}.
 */
public class PatternOptionBuilderTest {

    /**
     * Tests that isValueCode() correctly identifies the character for a file type.
     */
    @Test
    public void isValueCodeShouldReturnTrueForFileTypeCode() {
        // In PatternOptionBuilder, special characters denote an option argument's type.
        // The '>' character is the code for a file (e.g., "f>").
        final char fileTypeCode = '>';

        // Act: Check if the character is a recognized value code.
        boolean isCode = PatternOptionBuilder.isValueCode(fileTypeCode);

        // Assert: The method should return true for the file type code.
        assertTrue("The character '>' should be recognized as a valid value code.", isCode);
    }
}