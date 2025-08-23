package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.assertNull;

/**
 * Tests for {@link PatternOptionBuilder}.
 */
public class PatternOptionBuilderTest {

    /**
     * Tests that getValueType returns null when provided with a character that
     * is not a recognized value code (e.g., '@', ':', '%').
     */
    @Test
    public void getValueTypeShouldReturnNullForUnrecognizedCharacter() {
        // Arrange: '3' is not a defined value code character.
        final char unrecognizedCode = '3';

        // Act: Call the method with the unrecognized character.
        final Class<?> valueType = PatternOptionBuilder.getValueType(unrecognizedCode);

        // Assert: The result should be null, as the character does not map to a known type.
        assertNull("Expected null for an unrecognized value code character", valueType);
    }
}