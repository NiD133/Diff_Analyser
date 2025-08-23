package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.assertNull;

/**
 * Tests for {@link PatternOptionBuilder}.
 */
public class PatternOptionBuilderTest {

    /**
     * Tests that {@link PatternOptionBuilder#getValueType(char)} returns null
     * when provided with a character that does not represent a valid value type.
     */
    @Test
    public void getValueTypeShouldReturnNullForUnrecognizedCharacter() {
        // Arrange: The character '(' is not a recognized value type code.
        // The recognized codes are: '@', ':', '%', '+', '#', '<', '>', '/', and '!'.
        final char unrecognizedChar = '(';

        // Act: Attempt to get the value type for the unrecognized character.
        final Class<?> valueType = PatternOptionBuilder.getValueType(unrecognizedChar);

        // Assert: The result should be null, as the character is not mapped to any type.
        assertNull("The value type for an unrecognized character should be null.", valueType);
    }
}