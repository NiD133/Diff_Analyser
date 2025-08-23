package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link PatternOptionBuilder}.
 */
public class PatternOptionBuilderTest {

    /**
     * Tests that the '@' character, which represents an object type in an option
     * pattern, correctly maps to the {@code Object.class}.
     */
    @Test
    public void getValueType_shouldReturnObjectClass_forAtSymbol() {
        // Arrange
        // The '@' character signifies that the option argument is a class name
        // from which an object should be instantiated.
        final char objectTypeIndicator = '@';
        final Class<?> expectedClass = Object.class;

        // Act
        final Class<?> actualClass = PatternOptionBuilder.getValueType(objectTypeIndicator);

        // Assert
        // Verify that the method returns the correct class type.
        assertEquals(expectedClass, actualClass);
    }
}