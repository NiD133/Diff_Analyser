package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.assertNull;

/**
 * Tests for {@link PatternOptionBuilder}.
 */
public class PatternOptionBuilderTest {

    /**
     * Tests that getValueType() returns null for a character that is not a
     * recognized special value code. The recognized codes are characters like
     * '@', ':', '%', etc., which map to specific class types.
     */
    @Test
    public void getValueTypeShouldReturnNullForUnrecognizedCharacter() {
        // Arrange: The character '6' is not a defined value code in PatternOptionBuilder.
        final char unrecognizedChar = '6';

        // Act: Attempt to get the value type for the unrecognized character.
        final Class<?> valueType = PatternOptionBuilder.getValueType(unrecognizedChar);

        // Assert: The method should return null, as the character does not map to any type.
        assertNull("Expected null for a character that is not a value code", valueType);
    }
}