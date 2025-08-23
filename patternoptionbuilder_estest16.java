package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.io.File;

/**
 * Tests for {@link PatternOptionBuilder}.
 */
public class PatternOptionBuilderTest {

    /**
     * Tests that the getValueType method correctly identifies the '*' character
     * as representing an array of files (File[]).
     */
    @Test
    public void getValueType_shouldReturnArrayOfFiles_forStarCharacter() {
        // Arrange: The '*' character in a pattern string represents an array of files.
        final char fileArrayCode = '*';

        // Act: Retrieve the class type associated with the character code.
        final Class<?> valueType = PatternOptionBuilder.getValueType(fileArrayCode);

        // Assert: The returned type should be exactly File[].class.
        // This is more specific and readable than checking for not-null and isArray().
        assertEquals("The '*' character should map to the File[] type", File[].class, valueType);
    }
}