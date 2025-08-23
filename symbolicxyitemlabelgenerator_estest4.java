package org.jfree.chart.labels;

import org.jfree.data.xy.XYDataset;
import org.junit.Test;

/**
 * Tests for the {@link SymbolicXYItemLabelGenerator} class.
 */
public class SymbolicXYItemLabelGeneratorTest {

    /**
     * Verifies that the generateToolTip method throws an IllegalArgumentException
     * when the dataset argument is null. This is the expected behavior as the
     * method contract requires a non-null dataset.
     */
    @Test(expected = IllegalArgumentException.class)
    public void generateToolTip_withNullDataset_shouldThrowIllegalArgumentException() {
        // Arrange: Create an instance of the label generator.
        SymbolicXYItemLabelGenerator generator = new SymbolicXYItemLabelGenerator();
        
        // The specific values for series and item indices are irrelevant for this test,
        // as the null dataset check should occur first.
        int series = 0;
        int item = 0;

        // Act & Assert: Call the method with a null dataset.
        // The @Test(expected=...) annotation handles the assertion,
        // ensuring that an IllegalArgumentException is thrown.
        generator.generateToolTip((XYDataset) null, series, item);
    }
}