package org.jfree.data.general;

import org.jfree.chart.date.SerialDate;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Contains unit tests for the {@link DefaultKeyedValueDataset} class.
 */
public class DefaultKeyedValueDatasetTest {

    /**
     * Verifies that the updateValue() method correctly changes the dataset's value
     * while preserving the original key.
     */
    @Test
    public void updateValue_shouldChangeTheValueAndPreserveTheKey() {
        // Arrange: Create a dataset with an initial key and value.
        SerialDate initialKey = SerialDate.createInstance(1670);
        Number initialValue = 2958465;
        DefaultKeyedValueDataset dataset = new DefaultKeyedValueDataset(initialKey, initialValue);

        // Act: Update the value in the dataset.
        Number updatedValue = 0;
        dataset.updateValue(updatedValue);

        // Assert: Verify that the value was updated and the key remains unchanged.
        assertEquals("The value should be updated to the new value.", updatedValue, dataset.getValue());
        assertEquals("The key should not change after an update.", initialKey, dataset.getKey());
    }
}