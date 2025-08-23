package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.io.File;

/**
 * Tests for {@link PatternOptionBuilder}.
 */
public class PatternOptionBuilderTest {

    /**
     * Tests that the '>' character, used to denote a file type in a pattern string,
     * correctly maps to the {@link java.io.File} class.
     */
    @Test
    public void getValueTypeShouldReturnFileClassForGreaterThanCharacter() {
        // Arrange: The '>' character represents a File type according to the class documentation.
        final char fileTypeIndicator = '>';
        final Class<?> expectedClass = File.class;

        // Act: Retrieve the value type associated with the character.
        final Class<?> actualClass = PatternOptionBuilder.getValueType(fileTypeIndicator);

        // Assert: The returned class should be File.class.
        assertEquals(expectedClass, actualClass);
    }
}