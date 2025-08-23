package org.apache.commons.lang3.math;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

// Note: The EvoSuite runner and scaffolding are kept to maintain the existing test suite structure.
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class IEEE754rUtils_ESTestTest34 extends IEEE754rUtils_ESTest_scaffolding {

    /**
     * Tests that calling the min() method with an empty float array
     * correctly throws an IllegalArgumentException.
     */
    @Test(timeout = 4000)
    public void min_onEmptyFloatArray_shouldThrowIllegalArgumentException() {
        // Arrange: Define the input, which is an empty float array.
        final float[] emptyArray = new float[0];

        // Act & Assert: Call the method and verify that the expected exception is thrown.
        try {
            IEEE754rUtils.min(emptyArray);
            fail("Expected an IllegalArgumentException to be thrown, but no exception occurred.");
        } catch (final IllegalArgumentException e) {
            // Verify that the exception message is correct, confirming the right validation failed.
            final String expectedMessage = "Array cannot be empty.";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}