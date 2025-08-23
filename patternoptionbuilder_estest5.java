package org.apache.commons.cli;

import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * Tests for the {@link PatternOptionBuilder} class.
 */
public class PatternOptionBuilderTest {

    /**
     * Tests that isValueCode() correctly identifies the character for an existing file ('<')
     * as a valid value code.
     */
    @Test
    public void testIsValueCodeRecognizesExistingFileCode() {
        // According to PatternOptionBuilder's specification, the '<' character
        // is a value code that represents an existing file (FileInputStream).
        final char existingFileCode = '<';

        // Verify that the method correctly identifies '<' as a value code.
        assertTrue("The character '<' should be recognized as a valid value code.",
                   PatternOptionBuilder.isValueCode(existingFileCode));
    }
}