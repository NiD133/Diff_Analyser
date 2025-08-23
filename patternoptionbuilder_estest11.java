package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Tests for the {@link PatternOptionBuilder} class.
 */
public class PatternOptionBuilderTest {

    /**
     * Tests that the isValueCode() method correctly identifies the '*' character
     * as a valid value code. In PatternOptionBuilder, the '*' character is used
     * to specify an option that accepts an array of files (File[]).
     */
    @Test
    public void testIsValueCodeRecognizesStarAsValueCode() {
        // The '*' character is a code representing the File[] type.
        final char filesArrayCode = '*';

        // The isValueCode method should return true for this character.
        final boolean isValue = PatternOptionBuilder.isValueCode(filesArrayCode);

        assertTrue("The '*' character should be recognized as a value code.", isValue);
    }
}