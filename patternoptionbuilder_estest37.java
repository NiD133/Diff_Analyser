package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.assertNull;

/**
 * Tests for {@link PatternOptionBuilder}.
 */
public class PatternOptionBuilderTest {

    /**
     * Tests that getValueType() returns null for a character that is not a
     * recognized special pattern code.
     */
    @Test
    public void getValueTypeShouldReturnNullForUnrecognizedCharacter() {
        // Arrange: The character '5' is not a defined value code in PatternOptionBuilder.
        // Recognized codes include '@', '>', '+', '%', '/', and ':'.
        final char unrecognizedChar = '5';

        // Act: Call the method with the unrecognized character.
        final Class<?> valueType = PatternOptionBuilder.getValueType(unrecognizedChar);

        // Assert: The result should be null, as the character does not map to a type.
        assertNull("Expected null for an unrecognized pattern character", valueType);
    }
}