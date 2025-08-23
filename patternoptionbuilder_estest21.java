package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Tests for {@link PatternOptionBuilder}.
 */
public class PatternOptionBuilderTest {

    /**
     * Tests that the pattern character for a String argument (':')
     * correctly resolves to the String class.
     */
    @Test
    public void testGetValueTypeForStringArgument() {
        // According to the PatternOptionBuilder documentation, ':' represents a String value.
        final char stringTypeIndicator = ':';

        // Execute the method under test
        final Class<?> valueType = PatternOptionBuilder.getValueType(stringTypeIndicator);

        // Verify that the returned class is indeed String.class
        assertEquals(String.class, valueType);
    }
}