package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.assertNull;

/**
 * Tests for {@link PatternOptionBuilder}.
 */
public class PatternOptionBuilderTest {

    /**
     * Tests that getValueType() returns null for a character that is not a
     * recognized value code. The documentation for PatternOptionBuilder lists
     * specific characters (@, >, +, %, /, :) that map to value types. Any other
     * character should be considered unrecognized.
     */
    @Test
    public void getValueTypeShouldReturnNullForUnrecognizedCharacter() {
        // Arrange: The single quote character (') is not a defined value code.
        final char unrecognizedCode = '\'';

        // Act: Call the method with the unrecognized character.
        final Class<?> valueType = PatternOptionBuilder.getValueType(unrecognizedCode);

        // Assert: The result should be null, indicating no type is associated with the character.
        assertNull("Expected null for an unrecognized value code character", valueType);
    }
}