package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.assertNull;

/**
 * Tests for {@link PatternOptionBuilder}.
 */
public class PatternOptionBuilderTest {

    /**
     * Tests that getValueType() returns null when provided with a character
     * that does not correspond to a known value type.
     */
    @Test
    public void getValueType_shouldReturnNull_forUnrecognizedTypeCode() {
        // Arrange: The '$' character is not a recognized value type code.
        // Other recognized codes include '@', ':', '%', '+', '#', '<', '>', '/', '!', '*'.
        final char unrecognizedCode = '$';

        // Act: Call the method with the unrecognized character.
        final Class<?> valueType = PatternOptionBuilder.getValueType(unrecognizedCode);

        // Assert: The method should return null for any unrecognized code.
        assertNull("Expected null for an unrecognized type code", valueType);
    }
}