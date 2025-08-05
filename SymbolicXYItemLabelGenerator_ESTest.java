package org.jfree.chart.labels;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import org.jfree.data.time.DynamicTimeSeriesCollection;
import org.jfree.data.xy.DefaultHighLowDataset;
import org.jfree.data.xy.DefaultTableXYDataset;
import org.jfree.data.xy.XYDataset;
import org.junit.Test;

/**
 * Unit tests for the {@link SymbolicXYItemLabelGenerator} class.
 * These tests cover the generation of tooltips and labels, object equality, and cloning.
 */
public class SymbolicXYItemLabelGeneratorTest {

    /**
     * Tests that the default tooltip is generated correctly for a valid data point.
     * The expected format is "X: {x-value}, Y: {y-value}".
     */
    @Test
    public void generateToolTip_withValidData_shouldReturnDefaultFormattedString() {
        // Arrange
        SymbolicXYItemLabelGenerator generator = new SymbolicXYItemLabelGenerator();
        // Create a dataset where the first item has X=102.0 and Y=50.0
        Date[] dates = {new Date(102L)}; // X-value is derived from the date's timestamp
        double[] highValues = {50.0};    // Y-value is the high value in this dataset
        double[] lowValues = {25.0};
        double[] openValues = {30.0};
        double[] closeValues = {45.0};
        double[] volumeValues = {1000};
        XYDataset dataset = new DefaultHighLowDataset("Series 1", dates, highValues, lowValues, openValues, closeValues, volumeValues);

        // Act
        String toolTip = generator.generateToolTip(dataset, 0, 0);

        // Assert
        assertEquals("X: 102.0, Y: 50.0", toolTip);
    }

    /**
     * Verifies that passing a null dataset to the generator results in an
     * IllegalArgumentException, as per the method's contract.
     */
    @Test(expected = IllegalArgumentException.class)
    public void generateToolTip_withNullDataset_shouldThrowIllegalArgumentException() {
        // Arrange
        SymbolicXYItemLabelGenerator generator = new SymbolicXYItemLabelGenerator();

        // Act
        generator.generateToolTip(null, 0, 0);

        // Assert (handled by expected exception)
    }

    /**
     * Tests that an exception from the dataset due to out-of-bounds indices
     * is propagated by the generator.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void generateToolTip_onEmptyDataset_shouldPropagateException() {
        // Arrange
        SymbolicXYItemLabelGenerator generator = new SymbolicXYItemLabelGenerator();
        XYDataset emptyDataset = new DefaultTableXYDataset();

        // Act
        // Accessing any item in an empty dataset should cause an exception.
        generator.generateToolTip(emptyDataset, 0, 0);

        // Assert (handled by expected exception)
    }

    /**
     * Tests that an exception from the dataset due to an invalid series index
     * is propagated by the generator.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void generateToolTip_withInvalidSeriesIndex_shouldPropagateException() {
        // Arrange
        SymbolicXYItemLabelGenerator generator = new SymbolicXYItemLabelGenerator();
        // Dataset with 2 series, so valid series indices are 0 and 1.
        XYDataset dataset = new DynamicTimeSeriesCollection(2, 10);

        // Act
        // Accessing series index 2 is out of bounds and should cause an exception.
        generator.generateToolTip(dataset, 2, 0);

        // Assert (handled by expected exception)
    }

    /**
     * Verifies that generateLabel propagates exceptions for invalid indices,
     * consistent with the behavior of generateToolTip.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void generateLabel_onEmptyDataset_shouldPropagateException() {
        // Arrange
        SymbolicXYItemLabelGenerator generator = new SymbolicXYItemLabelGenerator();
        XYDataset emptyDataset = new DefaultTableXYDataset();

        // Act
        generator.generateLabel(emptyDataset, 0, 0);

        // Assert (handled by expected exception)
    }

    /**
     * Verifies the contract for the equals() and hashCode() methods.
     * Since the class is stateless, all instances should be equal.
     */
    @Test
    public void testEqualsAndHashCode_contract() {
        // Arrange
        SymbolicXYItemLabelGenerator generator1 = new SymbolicXYItemLabelGenerator();
        SymbolicXYItemLabelGenerator generator2 = new SymbolicXYItemLabelGenerator();

        // Assert
        // 1. Reflexive: an object must equal itself.
        assertTrue(generator1.equals(generator1));

        // 2. Symmetric: if a.equals(b) is true, then b.equals(a) must be true.
        assertTrue(generator1.equals(generator2));
        assertTrue(generator2.equals(generator1));

        // 3. Hash code: equal objects must have equal hash codes.
        assertEquals(generator1.hashCode(), generator2.hashCode());

        // 4. Inequality: should not be equal to null or objects of different types.
        assertFalse(generator1.equals(null));
        assertFalse(generator1.equals("a string"));
    }

    /**
     * Tests that cloning the generator produces a new instance that is
     * independent but logically equal to the original.
     */
    @Test
    public void clone_shouldProduceIndependentAndEqualInstance() throws CloneNotSupportedException {
        // Arrange
        SymbolicXYItemLabelGenerator original = new SymbolicXYItemLabelGenerator();

        // Act
        SymbolicXYItemLabelGenerator clone = (SymbolicXYItemLabelGenerator) original.clone();

        // Assert
        // The clone should be a different object in memory.
        assertNotSame(original, clone);
        // The clone should be logically equal to the original.
        assertEquals(original, clone);
    }
}