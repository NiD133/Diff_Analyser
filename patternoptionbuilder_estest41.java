package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.assertNull;

/**
 * Tests for {@link PatternOptionBuilder}.
 */
public class PatternOptionBuilderTest {

    /**
     * Tests that getValueType() returns null when the input character is not one of the
     * special, recognized type codes (e.g., '@', '>', '%').
     */
    @Test
    public void getValueTypeShouldReturnNullForUnrecognizedTypeCode() {
        // The character '1' is not a defined value code in PatternOptionBuilder.
        // Therefore, the method is expected to return null, indicating no associated type.
        Class<?> valueType = PatternOptionBuilder.getValueType('1');

        assertNull("Expected null for a character that is not a value code", valueType);
    }
}