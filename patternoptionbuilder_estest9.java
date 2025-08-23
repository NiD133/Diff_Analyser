package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link PatternOptionBuilder}.
 */
public class PatternOptionBuilderTest {

    /**
     * Tests that the '@' character is correctly identified as a value code.
     * According to the PatternOptionBuilder documentation, '@' represents an object type.
     */
    @Test
    public void isValueCode_shouldReturnTrue_forAtSymbol() {
        // The '@' character is a value code representing an Object type argument.
        assertTrue("The '@' character should be recognized as a value code.",
                   PatternOptionBuilder.isValueCode('@'));
    }
}