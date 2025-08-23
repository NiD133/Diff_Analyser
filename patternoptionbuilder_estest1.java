package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link PatternOptionBuilder}.
 */
public class PatternOptionBuilderTest {

    @Test
    public void getValueType_shouldReturnStringClass_forStringPatternCharacter() {
        // The Javadoc for PatternOptionBuilder specifies that the ':' character
        // represents an option argument of type String.

        // Arrange
        final char stringPatternChar = ':';

        // Act
        // Note: The original test used the deprecated `getValueClass`.
        // This improved test uses the current, type-safe `getValueType` method.
        Class<?> actualValueType = PatternOptionBuilder.getValueType(stringPatternChar);

        // Assert
        assertEquals(String.class, actualValueType);
    }
}