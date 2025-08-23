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
    public void getValueTypeShouldReturnNullForUnrecognizedCharacter() {
        // Arrange: The Javadoc for PatternOptionBuilder lists the valid value type
        // characters (e.g., '@', '>', '%'). The comma character is not one of them.
        final char unrecognizedChar = ',';

        // Act: Call the method with the unrecognized character.
        final Class<?> valueType = PatternOptionBuilder.getValueType(unrecognizedChar);

        // Assert: The method should return null, as the character is not a valid
        // type identifier.
        assertNull("Expected null for an unrecognized value type character", valueType);
    }
}