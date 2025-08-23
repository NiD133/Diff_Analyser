package org.jfree.data.general;

import org.jfree.data.UnknownKeyException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * This test class contains an improved version of a test for the DefaultPieDataset class.
 * The original test was auto-generated and lacked clarity.
 */
// The original class name 'DefaultPieDataset_ESTestTest42' is kept for context.
// In a real-world scenario, this would be part of a 'DefaultPieDatasetTest' class.
public class DefaultPieDataset_ESTestTest42 {

    /**
     * Verifies that calling getValue() with a key that does not exist in the dataset
     * throws an UnknownKeyException.
     */
    @Test
    public void getValue_whenKeyDoesNotExist_throwsUnknownKeyException() {
        // Arrange: Create an empty dataset and define a key that is not present.
        DefaultPieDataset<Integer> dataset = new DefaultPieDataset<>();
        final Integer nonExistentKey = 400;

        // Act & Assert: Expect an UnknownKeyException when trying to access the value for the non-existent key.
        try {
            dataset.getValue(nonExistentKey);
            fail("Expected an UnknownKeyException because the key '" + nonExistentKey + "' does not exist in the dataset.");
        } catch (UnknownKeyException e) {
            // Success: The expected exception was thrown.
            // Verify the exception message for correctness and clarity.
            String expectedMessage = "Key not found: " + nonExistentKey;
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}