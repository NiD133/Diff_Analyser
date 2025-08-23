package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import java.util.Date;

/**
 * Tests for {@link PatternOptionBuilder}.
 */
public class PatternOptionBuilderTest {

    /**
     * Tests that the pattern character '#' correctly maps to the java.util.Date class.
     */
    @Test
    public void getValueTypeShouldReturnDateClassForHashCharacter() {
        // Arrange
        final char datePatternChar = '#';

        // Act
        final Class<?> valueType = PatternOptionBuilder.getValueType(datePatternChar);

        // Assert
        assertEquals("The '#' character should represent the Date class.", Date.class, valueType);
    }
}