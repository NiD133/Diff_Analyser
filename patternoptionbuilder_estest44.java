package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.assertNull;

/**
 * Tests for {@link PatternOptionBuilder}.
 */
public class PatternOptionBuilderTest {

    /**
     * Tests that {@link PatternOptionBuilder#getValueType(char)} returns null
     * when passed a character that does not represent a supported value type.
     */
    @Test
    public void getValueTypeShouldReturnNullForUnrecognizedCharacter() {
        // The ')' character is not a special code that maps to a value type
        // (like '@' for Object or ':' for String).
        char unrecognizedChar = ')';

        Class<?> valueType = PatternOptionBuilder.getValueType(unrecognizedChar);

        assertNull("Expected null for an unrecognized pattern character", valueType);
    }
}