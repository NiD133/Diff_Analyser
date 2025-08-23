package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.assertNull;

/**
 * Tests for {@link PatternOptionBuilder}.
 */
public class PatternOptionBuilderTest {

    /**
     * Tests that getValueType() returns null for a character that is not a
     * recognized special value code (e.g., '@', '>', '+', etc.).
     */
    @Test
    public void getValueTypeShouldReturnNullForUnrecognizedCode() {
        // Arrange: Define a character that does not map to a specific value type.
        // According to the PatternOptionBuilder documentation, '4' is not a special code.
        final char unrecognizedCode = '4';

        // Act: Call the method under test.
        final Class<?> valueType = PatternOptionBuilder.getValueType(unrecognizedCode);

        // Assert: Verify that the result is null, as the character is not a defined type indicator.
        assertNull("Expected null for an unrecognized value code character", valueType);
    }
}