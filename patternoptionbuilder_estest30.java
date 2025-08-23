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
        // Arrange: The '=' character is not a defined value type code.
        final char unrecognizedCode = '=';

        // Act: Attempt to retrieve the value type for the unrecognized character.
        final Class<?> valueType = PatternOptionBuilder.getValueType(unrecognizedCode);

        // Assert: The result should be null, as the character does not map to any known type.
        assertNull("Expected null for an unrecognized value code character", valueType);
    }
}