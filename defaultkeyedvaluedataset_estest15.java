package org.jfree.data.general;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the {@link DefaultKeyedValueDataset} class.
 */
public class DefaultKeyedValueDatasetTest {

    /**
     * Verifies the reflexive property of the equals() method.
     * An object must always be equal to itself.
     */
    @Test
    public void equals_whenComparedToItself_returnsTrue() {
        // Arrange: Create an instance of the dataset.
        DefaultKeyedValueDataset dataset = new DefaultKeyedValueDataset();

        // Act & Assert: The object must be equal to itself.
        assertTrue("A dataset instance should be equal to itself.", dataset.equals(dataset));
    }
}