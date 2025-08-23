package org.apache.commons.lang3.exception;

import static org.junit.Assert.assertNull;
import org.junit.Test;

/**
 * Unit tests for {@link DefaultExceptionContext}.
 */
public class DefaultExceptionContextTest {

    /**
     * Tests that getFirstContextValue() returns null when the requested label does not exist in the context.
     */
    @Test
    public void getFirstContextValue_shouldReturnNull_whenLabelDoesNotExist() {
        // Arrange: Create an empty exception context.
        final DefaultExceptionContext context = new DefaultExceptionContext();
        final String nonExistentLabel = "NON_EXISTENT_LABEL";

        // Act: Attempt to retrieve a value for a label that has not been added.
        final Object result = context.getFirstContextValue(nonExistentLabel);

        // Assert: Verify that the result is null, as expected.
        assertNull("Expected null when retrieving a value for a non-existent label.", result);
    }
}