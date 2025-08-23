package org.jfree.chart.labels;

import org.jfree.data.xy.DefaultTableXYDataset;
import org.jfree.data.xy.XYDataset;
import org.junit.Test;

/**
 * Tests for the {@link SymbolicXYItemLabelGenerator} class, focusing on its
 * interaction with data sets.
 */
public class SymbolicXYItemLabelGeneratorTest {

    /**
     * Verifies that generateToolTip throws an IndexOutOfBoundsException when the
     * dataset is empty and accessed with any series/item index. The exception
     * is expected to originate from the dataset itself.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void generateToolTipWithEmptyDatasetShouldThrowIndexOutOfBoundsException() {
        // Arrange: Create a generator and an empty dataset.
        SymbolicXYItemLabelGenerator generator = new SymbolicXYItemLabelGenerator();
        XYDataset emptyDataset = new DefaultTableXYDataset();

        // Act: Attempt to generate a tooltip for an item that does not exist.
        // The series and item indices (0, 0) are out of bounds for an empty dataset.
        generator.generateToolTip(emptyDataset, 0, 0);

        // Assert: The @Test(expected) annotation handles the exception assertion.
        // The test will fail if an IndexOutOfBoundsException is not thrown.
    }
}