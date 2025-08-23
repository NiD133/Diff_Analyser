package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.assertNull;

/**
 * Tests for {@link PatternOptionBuilder}.
 */
public class PatternOptionBuilderTest {

    @Test
    public void getValueType_shouldReturnNullForUnrecognizedCode() {
        // Arrange: The character '.' is not a recognized value type code.
        // Valid codes include '@', '>', '+', '%', '/', and ':'.
        final char unrecognizedCode = '.';

        // Act: Call the method with the unrecognized code.
        final Class<?> valueType = PatternOptionBuilder.getValueType(unrecognizedCode);

        // Assert: The method should return null, as the code is not mapped to any type.
        assertNull("Expected null for an unrecognized value type character", valueType);
    }
}