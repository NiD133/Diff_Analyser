package org.jfree.data.general;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for the cloning and equality implementation of the {@link DefaultKeyedValueDataset} class.
 */
public class DefaultKeyedValueDatasetTest {

    /**
     * Verifies that cloning an empty dataset produces a new instance that is
     * equal to the original, fulfilling the general contract for Object.clone().
     */
    @Test
    public void clone_onEmptyDataset_returnsEqualButNotSameInstance() throws CloneNotSupportedException {
        // Arrange: Create an empty dataset instance.
        DefaultKeyedValueDataset originalDataset = new DefaultKeyedValueDataset();

        // Act: Clone the dataset.
        DefaultKeyedValueDataset clonedDataset = (DefaultKeyedValueDataset) originalDataset.clone();

        // Assert: The cloned object should be a separate instance but logically equal.
        // 1. Check that they are not the same object in memory.
        assertNotSame("A cloned dataset should be a new object instance.", originalDataset, clonedDataset);

        // 2. Check that the contents are logically equal.
        assertEquals("A cloned dataset should be equal to the original.", originalDataset, clonedDataset);
        
        // 3. As per the equals/hashCode contract, equal objects must have equal hash codes.
        assertEquals("Equal datasets must have the same hash code.", originalDataset.hashCode(), clonedDataset.hashCode());
    }
}