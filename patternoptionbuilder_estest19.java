package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link PatternOptionBuilder}.
 */
public class PatternOptionBuilderTest {

    /**
     * Tests that the getValueType method correctly identifies the '%' character
     * as representing the Number class.
     */
    @Test
    public void getValueType_forNumberChar_shouldReturnNumberClass() {
        // Arrange: The pattern character for a Number type is '%'.
        final char numberPatternChar = '%';

        // Act: Retrieve the class associated with the pattern character.
        Class<?> valueType = PatternOptionBuilder.getValueType(numberPatternChar);

        // Assert: The returned type should be Number.class.
        assertEquals(Number.class, valueType);
    }
}