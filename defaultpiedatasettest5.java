package org.jfree.data.general;

import org.jfree.chart.TestUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Serialization tests for the {@link DefaultPieDataset} class.
 */
class DefaultPieDatasetSerializationTest {

    /**
     * Verifies that a DefaultPieDataset instance can be serialized and
     * that the deserialized instance is equal to the original. This test
     * includes a null value to ensure edge cases are handled correctly.
     */
    @Test
    void testSerialization() {
        // Arrange: Create a dataset with a mix of values, including a null.
        DefaultPieDataset<String> originalDataset = new DefaultPieDataset<>();
        originalDataset.setValue("Key 1", 234.2);
        originalDataset.setValue("Key 2", null);
        originalDataset.setValue("Key 3", 345.9);
        originalDataset.setValue("Key 4", 452.7);

        // Act: Serialize and then deserialize the dataset using a utility method.
        DefaultPieDataset<String> deserializedDataset = TestUtils.serialised(originalDataset);

        // Assert: The deserialized dataset should be equal to the original.
        assertEquals(originalDataset, deserializedDataset);
    }
}