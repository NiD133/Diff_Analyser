package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link PatternOptionBuilder}.
 */
public class PatternOptionBuilderTest {

    @Test
    public void getValueTypeShouldReturnClassTypeForPlusCharacter() {
        // The '+' character in a pattern string signifies that the option's argument
        // should be treated as a class name.

        // Act: Call the method under test with the '+' character.
        Class<?> valueType = PatternOptionBuilder.getValueType('+');

        // Assert: Verify that the returned type is indeed java.lang.Class.
        assertEquals(Class.class, valueType);
    }
}