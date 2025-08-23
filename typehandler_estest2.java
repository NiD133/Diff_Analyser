package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link TypeHandler}.
 */
public class TypeHandlerTest {

    /**
     * Tests that the deprecated {@code createValue(String, Object)} method returns the
     * input string itself when the provided type object is null. This behavior
     * indicates that a null type defaults to treating the value as a String.
     */
    @Test
    public void createValueWithNullTypeShouldReturnTheStringUnchanged() {
        // Arrange: Define the input values for the test.
        final String inputValue = "";
        final Object nullType = null;

        // Act: Call the method under test.
        // This method is deprecated, but its behavior is still being verified.
        @SuppressWarnings("deprecation")
        final Object result = TypeHandler.createValue(inputValue, nullType);

        // Assert: Verify the outcome is as expected.
        // When the type is null, the method should return the original string.
        assertEquals("The returned value should be the input string", inputValue, result);
    }
}