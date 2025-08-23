package org.jfree.data.general;

import org.jfree.chart.internal.CloneUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;

/**
 * Tests for the cloning behavior of the {@link DefaultPieDataset} class.
 */
@DisplayName("DefaultPieDataset Cloning")
class DefaultPieDatasetTest {

    @Test
    @DisplayName("A cloned dataset should be an independent and equal copy of the original")
    void cloneShouldCreateIndependentAndEqualInstance() {
        // Arrange: Create an original dataset with some values, including a null.
        DefaultPieDataset<String> originalDataset = new DefaultPieDataset<>();
        originalDataset.setValue("Category A", 1.0);
        originalDataset.setValue("Category B", null);
        originalDataset.setValue("Category C", 3.0);

        // Act: Clone the original dataset.
        DefaultPieDataset<String> clonedDataset = CloneUtils.clone(originalDataset);

        // Assert: The clone should be a separate object but logically equal to the original.
        assertNotSame(originalDataset, clonedDataset,
                "A cloned dataset must be a different object instance from the original.");

        assertSame(originalDataset.getClass(), clonedDataset.getClass(),
                "A cloned dataset must be of the exact same class as the original.");

        assertEquals(originalDataset, clonedDataset,
                "A cloned dataset must have content equal to the original.");
    }
}