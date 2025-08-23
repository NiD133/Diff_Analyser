package org.jfree.data.general;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for the {@link DefaultPieDataset} class.
 */
public class DefaultPieDatasetTest {

    /**
     * Verifies that if a null value is associated with a key,
     * the getValue() method correctly returns null for that key.
     */
    @Test
    public void getValue_whenKeyHasNullValue_shouldReturnNull() {
        // Arrange
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
        String key = "Test Key";

        // Act
        // Set the value for the key to null.
        dataset.setValue(key, null);
        Number retrievedValue = dataset.getValue(key);

        // Assert
        assertNull("The value retrieved should be null, as that was the value set.", retrievedValue);
    }
}