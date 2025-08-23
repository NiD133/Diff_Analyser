package org.jfree.data.flow;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * This test class contains an improved version of an auto-generated test case.
 * The original class name and inheritance from the scaffolding have been preserved
 * for context.
 */
public class DefaultFlowDataset_ESTestTest18 extends DefaultFlowDataset_ESTest_scaffolding {

    /**
     * Verifies that the getFlow() method throws an IllegalArgumentException
     * when the 'source' argument is null. The JFreeChart library uses a
     * utility class to check for null arguments, and this test ensures that
     * the check for the 'source' parameter is correctly implemented.
     */
    @Test
    public void getFlow_withNullSource_shouldThrowIllegalArgumentException() {
        // Arrange: Create an empty dataset. The other parameters for getFlow() are
        // set to valid or plausible values to isolate the null 'source' as the
        // specific cause of the exception.
        DefaultFlowDataset<String> dataset = new DefaultFlowDataset<>();
        int validStage = 0;
        String nullSource = null;
        String validDestination = "DestinationNode";

        // Act & Assert: Attempt to call getFlow() with a null source and verify
        // that the correct exception is thrown with the expected message.
        try {
            dataset.getFlow(validStage, nullSource, validDestination);
            fail("Expected an IllegalArgumentException for the null 'source' argument, but it was not thrown.");
        } catch (IllegalArgumentException e) {
            // Assert that the exception message clearly indicates the cause.
            assertEquals("Null 'source' argument.", e.getMessage());
        }
    }
}