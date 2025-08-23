package org.jfree.data.general;

import org.jfree.chart.date.SerialDate;
import org.junit.Test;
import static org.junit.Assert.assertSame;

/**
 * Unit tests for the {@link DefaultKeyedValueDataset} class.
 */
public class DefaultKeyedValueDatasetTest {

    /**
     * Verifies that getKey() returns the same key object that was provided
     * to the constructor.
     */
    @Test
    public void getKey_whenDatasetIsConstructedWithKey_returnsTheSameKeyInstance() {
        // Arrange: Create a dataset with a specific key and a value.
        SerialDate expectedKey = SerialDate.createInstance(1670);
        Number value = 2958465;
        DefaultKeyedValueDataset dataset = new DefaultKeyedValueDataset(expectedKey, value);

        // Act: Retrieve the key from the dataset.
        Comparable<?> actualKey = dataset.getKey();

        // Assert: The retrieved key should be the exact same instance as the original key.
        assertSame("The key returned by getKey() should be the same instance "
                   + "provided to the constructor.", expectedKey, actualKey);
    }
}