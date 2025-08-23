package org.jfree.data.general;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the {@link DefaultPieDataset} class.
 */
public class DefaultPieDatasetTest {

    @Test
    public void equals_returnsTrueForSameInstance() {
        // Arrange
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();

        // Act & Assert
        // According to the contract of Object.equals(), an object must be equal to itself (reflexivity).
        assertTrue(dataset.equals(dataset));
    }
}