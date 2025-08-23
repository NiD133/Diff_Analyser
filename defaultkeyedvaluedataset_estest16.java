package org.jfree.data.general;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit tests for the {@link DefaultKeyedValueDataset} class.
 */
class DefaultKeyedValueDatasetTest {

    /**
     * Verifies that calling updateValue() on a dataset that has not been
     * initialized with a key-value pair throws a RuntimeException.
     */
    @Test
    void updateValueOnEmptyDatasetShouldThrowRuntimeException() {
        // Arrange: Create an empty dataset, which has no initial key or value.
        DefaultKeyedValueDataset emptyDataset = new DefaultKeyedValueDataset();
        String expectedErrorMessage = "updateValue: can't update null.";

        // Act & Assert: Attempting to update the value should fail because the
        // internal data object is null. We use assertThrows to verify this.
        RuntimeException thrownException = assertThrows(RuntimeException.class, () -> {
            // The value passed here (100.0) is irrelevant; the exception is due to
            // the dataset's uninitialized state.
            emptyDataset.updateValue(100.0);
        });

        // Verify that the exception has the expected message.
        assertEquals(expectedErrorMessage, thrownException.getMessage());
    }
}