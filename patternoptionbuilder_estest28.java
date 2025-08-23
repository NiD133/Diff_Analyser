package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.assertNull;

/**
 * Tests for the static methods of {@link PatternOptionBuilder}.
 */
public class PatternOptionBuilderTest {

    /**
     * Tests that getValueType() returns null when provided with a character
     * that is not a recognized value code.
     */
    @Test
    public void getValueTypeShouldReturnNullForUnrecognizedCode() {
        // Arrange: The '?' character is not a defined value code in PatternOptionBuilder.
        // According to the class documentation, valid codes include '@', '>', '+', '%', '/', and ':'.
        char unrecognizedCode = '?';

        // Act: Call the method with the unrecognized code.
        Class<?> valueType = PatternOptionBuilder.getValueType(unrecognizedCode);

        // Assert: The result should be null, as the code is not associated with any type.
        assertNull("Expected null for an unrecognized value code", valueType);
    }
}