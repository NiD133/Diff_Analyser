package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.assertNull;

/**
 * Tests for the {@link PatternOptionBuilder} class.
 */
public class PatternOptionBuilderTest {

    /**
     * Tests that getValueType() returns null for a character that is not a
     * recognized value type code.
     */
    @Test
    public void getValueTypeShouldReturnNullForUnrecognizedCharacter() {
        // Arrange: The character '9' is not a special character that represents a value type
        // (like '@' for Object, ':' for String, etc.).
        final char unrecognizedCode = '9';

        // Act: Attempt to retrieve the value type for the unrecognized character.
        final Class<?> valueType = PatternOptionBuilder.getValueType(unrecognizedCode);

        // Assert: The method should return null, indicating the character is not a valid code.
        assertNull("Expected null for an unrecognized value type character", valueType);
    }
}