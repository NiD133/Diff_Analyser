package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.assertNull;

import org.evosuite.runtime.mock.java.lang.MockThrowable;

/**
 * Tests for {@link TypeHandler}.
 */
public class TypeHandlerTest {

    /**
     * Tests that {@link TypeHandler#createValue(String, Class)} returns null
     * when the input string is null.
     */
    @Test
    public void createValue_withNullString_shouldReturnNull() throws ParseException {
        // Arrange: Define the input string as null and specify a target type.
        // The specific target type (MockThrowable) does not matter for this test case,
        // as the null check should happen before any type-specific conversion.
        final String inputValue = null;
        final Class<MockThrowable> targetType = MockThrowable.class;

        // Act: Call the method under test with the null input.
        final MockThrowable result = TypeHandler.createValue(inputValue, targetType);

        // Assert: Verify that the result is null.
        assertNull("The method should return null when the input string is null.", result);
    }
}