package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.assertNull;

/**
 * Tests for {@link PatternOptionBuilder}.
 */
public class PatternOptionBuilderTest {

    /**
     * Tests that getValueType() returns null when provided with a character
     * that does not correspond to a known value type. The PatternOptionBuilder
     * only recognizes specific characters like '@', ':', '/', etc., as valid
     * type codes.
     */
    @Test
    public void getValueTypeShouldReturnNullForUnrecognizedCharacter() {
        // Arrange: Define an input character that is not a valid type identifier.
        // According to the PatternOptionBuilder documentation, '7' has no special meaning.
        final char unrecognizedChar = '7';

        // Act: Call the method under test with the unrecognized character.
        final Class<?> resultType = PatternOptionBuilder.getValueType(unrecognizedChar);

        // Assert: Verify that the method returns null, as expected for an invalid code.
        assertNull("Expected null for an unrecognized value type character", resultType);
    }
}