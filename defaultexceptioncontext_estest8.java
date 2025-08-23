package org.apache.commons.lang3.exception;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link DefaultExceptionContext}.
 */
public class DefaultExceptionContextTest {

    /**
     * Tests that getContextValues() returns a non-null, empty list
     * when queried with a label that does not exist in the context.
     */
    @Test
    public void getContextValues_forNonExistentLabel_shouldReturnEmptyList() {
        // Arrange: Create an empty context and define a label that has not been added.
        final DefaultExceptionContext context = new DefaultExceptionContext();
        final String nonExistentLabel = "com.example.NonExistentLabel";

        // Act: Request the values associated with the non-existent label.
        final List<Object> values = context.getContextValues(nonExistentLabel);

        // Assert: Verify that the returned list is empty, not null.
        assertNotNull("The returned list should never be null.", values);
        assertTrue("The list of values for a non-existent label should be empty.", values.isEmpty());
    }
}