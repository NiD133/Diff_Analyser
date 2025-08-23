package org.apache.commons.lang3.exception;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

/**
 * Unit tests for {@link DefaultExceptionContext}.
 */
public class DefaultExceptionContextTest {

    /**
     * Tests that getContextValues() returns a list containing the single value
     * that was added for a specific label.
     */
    @Test
    public void getContextValues_whenSingleValueIsAddedForLabel_returnsListContainingThatValue() {
        // Arrange
        final DefaultExceptionContext context = new DefaultExceptionContext();
        final String testLabel = "Error Code";
        final Object testValue = 404;

        context.addContextValue(testLabel, testValue);

        // Act
        final List<Object> actualValues = context.getContextValues(testLabel);

        // Assert
        assertNotNull("The returned list of values should not be null.", actualValues);
        assertEquals("The list should contain exactly one value.", 1, actualValues.size());
        assertEquals("The value in the list should match the one that was added.", testValue, actualValues.get(0));
    }
}