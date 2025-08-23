package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.assertNull;

/**
 * Tests for {@link PatternOptionBuilder}.
 */
public class PatternOptionBuilderTest {

    /**
     * Tests that getValueType returns null for a character that is not a recognized
     * value type identifier. The PatternOptionBuilder class documentation specifies
     * characters like '@', '>', '%', etc., as valid type codes.
     */
    @Test
    public void getValueTypeShouldReturnNullForUnrecognizedCharacter() {
        // Arrange: The hyphen character '-' is not a defined value type code.
        final char unrecognizedChar = '-';

        // Act: Call the method with the unrecognized character.
        Class<?> valueType = PatternOptionBuilder.getValueType(unrecognizedChar);

        // Assert: The result should be null, as the character does not map to a type.
        assertNull("Expected null for a character that is not a value code.", valueType);
    }
}