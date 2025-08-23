package org.apache.commons.lang3.exception;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link DefaultExceptionContext}.
 * This test focuses on adding and retrieving a context value.
 */
public class DefaultExceptionContext_ESTestTest1 extends DefaultExceptionContext_ESTest_scaffolding {

    @Test
    public void getFirstContextValue_shouldReturnCorrectValue_whenSingleValueIsAddedForLabel() {
        // Arrange
        final DefaultExceptionContext context = new DefaultExceptionContext();
        final String testLabel = "Error Code";
        final Integer testValue = 404;

        context.addContextValue(testLabel, testValue);

        // Act
        final Object retrievedValue = context.getFirstContextValue(testLabel);

        // Assert
        assertEquals("The retrieved value should match the one that was added.", testValue, retrievedValue);
    }
}