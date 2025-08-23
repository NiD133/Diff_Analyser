package org.apache.commons.lang3.exception;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.util.Set;

/**
 * Unit tests for {@link DefaultExceptionContext}.
 */
public class DefaultExceptionContextTest {

    /**
     * Tests that a label added to the context is returned by getContextLabels().
     */
    @Test
    public void getContextLabels_shouldContainLabel_afterAddingContextValue() {
        // Arrange: Create a new context and define a label/value pair to add.
        final DefaultExceptionContext context = new DefaultExceptionContext();
        final String testLabel = "Error Code";
        final int testValue = 404;

        // Act: Add the value to the context and retrieve the set of labels.
        context.addContextValue(testLabel, testValue);
        final Set<String> labels = context.getContextLabels();

        // Assert: Verify that the set of labels is not empty and contains the added label.
        assertFalse("The set of labels should not be empty after adding a value.", labels.isEmpty());
        assertEquals("The set should contain exactly one label.", 1, labels.size());
        assertTrue("The set of labels should contain the label that was added.", labels.contains(testLabel));
    }
}