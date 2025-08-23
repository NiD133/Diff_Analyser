package org.apache.commons.lang3.exception;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * This class contains test cases for the DefaultExceptionContext class.
 * The focus is on verifying the correct handling of context values,
 * ensuring that they can be added and retrieved as expected.
 */
public class DefaultExceptionContextTest {

    /**
     * Tests that getFirstContextValue() correctly retrieves the first value
     * associated with a specific label after it has been added.
     */
    @Test
    public void testGetFirstContextValueShouldReturnTheFirstValueAddedForAGivenLabel() {
        // Arrange: Set up the test by creating a context and adding a value.
        final DefaultExceptionContext context = new DefaultExceptionContext();
        final String testLabel = "test_label";
        final String testValue = "test_value";
        context.addContextValue(testLabel, testValue);

        // Act: Perform the action to be tested.
        final Object retrievedValue = context.getFirstContextValue(testLabel);

        // Assert: Verify that the action produced the expected outcome.
        assertEquals("The retrieved value should match the value that was added.", testValue, retrievedValue);
    }
}