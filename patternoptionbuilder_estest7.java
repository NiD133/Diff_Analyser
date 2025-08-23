package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link PatternOptionBuilder}.
 */
public class PatternOptionBuilderTest {

    /**
     * Tests that the character '#' is correctly identified as a value code.
     * In the pattern string, '#' represents a {@link java.util.Date} type.
     */
    @Test
    public void isValueCodeShouldReturnTrueForDateValueCode() {
        // The '#' character is the value code used to specify an option argument of type Date.
        final char dateValueCode = '#';
        
        assertTrue("The character '#' should be recognized as a valid value code.",
                   PatternOptionBuilder.isValueCode(dateValueCode));
    }
}