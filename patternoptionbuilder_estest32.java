package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.assertNull;

/**
 * Tests for {@link PatternOptionBuilder}.
 */
public class PatternOptionBuilderTest {

    @Test
    public void getValueTypeShouldReturnNullForUnrecognizedCharacter() {
        // Arrange: According to the PatternOptionBuilder documentation, value type
        // specifiers are characters like '@', '>', '%', etc. The semicolon ';'
        // is not a recognized specifier.
        final char unrecognizedChar = ';';

        // Act: Attempt to get the value type for the unrecognized character.
        final Class<?> valueType = PatternOptionBuilder.getValueType(unrecognizedChar);

        // Assert: The method should return null when given a character that does not
        // map to a known value type.
        assertNull("Expected null for an unrecognized value type character", valueType);
    }
}