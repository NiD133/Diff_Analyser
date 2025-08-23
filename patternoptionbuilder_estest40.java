package org.apache.commons.cli;

import static org.junit.Assert.assertNull;

import org.junit.Test;

/**
 * Tests for {@link PatternOptionBuilder}.
 */
public class PatternOptionBuilderTest {

    /**
     * Tests that getValueType() returns null for a character that is not a
     * recognized value code.
     */
    @Test
    public void getValueTypeShouldReturnNullForUnrecognizedCode() {
        // Arrange
        // The character '2' is not a special code representing a value type
        // like '@' (Object), ':' (String), or '%' (Number).
        final char unrecognizedCode = '2';

        // Act
        final Class<?> valueType = PatternOptionBuilder.getValueType(unrecognizedCode);

        // Assert
        assertNull("The returned type for an unrecognized code should be null.", valueType);
    }
}