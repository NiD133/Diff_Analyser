package org.jfree.data.general;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Contains tests for the {@link DefaultPieDataset} class, focusing on value handling.
 */
public class DefaultPieDatasetTest {

    @Test
    public void getValueByIndex_shouldReturnNull_whenNullValueIsSet() {
        // Arrange: Create a new dataset and define a key.
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
        String key = "Test Key";

        // Act: Set the value for the key to null.
        dataset.setValue(key, (Number) null);
        
        // Retrieve the value using its index (0, as it's the first item).
        Number retrievedValue = dataset.getValue(0);

        // Assert: Verify that the retrieved value is indeed null.
        assertNull("The value retrieved by index should be null when a null value was set.", retrievedValue);
    }
}