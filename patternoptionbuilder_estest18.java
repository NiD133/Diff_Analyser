package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.io.FileInputStream;

/**
 * Tests for {@link PatternOptionBuilder}.
 */
public class PatternOptionBuilderTest {

    /**
     * Tests that the special character for an existing file ('<') correctly maps to the
     * FileInputStream class, which is used for reading from a file that must exist.
     */
    @Test
    public void getValueTypeShouldReturnFileInputStreamForExistingFileCharacter() {
        // Arrange
        final char existingFileCode = '<';
        final Class<?> expectedType = FileInputStream.class;

        // Act
        final Class<?> actualType = PatternOptionBuilder.getValueType(existingFileCode);

        // Assert
        assertEquals("The '<' character should represent the FileInputStream class.", expectedType, actualType);
    }
}