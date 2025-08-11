package org.jfree.chart.labels;

import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.category.DefaultIntervalCategoryDataset;
import org.junit.Test;

import java.text.DateFormat;
import java.text.NumberFormat;

import static org.junit.Assert.*;

/**
 * A set of tests for the {@link IntervalCategoryToolTipGenerator} class, focusing on its
 * constructors, item array creation, and object equality.
 */
public class IntervalCategoryToolTipGeneratorTest {

    /**
     * Creates a simple 1x1 dataset for testing purposes.
     *
     * @return A sample {@link DefaultIntervalCategoryDataset}.
     */
    private DefaultIntervalCategoryDataset createSampleDataset() {
        String[] seriesKeys = {"Series 1"};
        String[] categoryKeys = {"Category 1"};
        Number[][] startValues = {{10.0}};
        Number[][] endValues = {{20.0}};
        return new DefaultIntervalCategoryDataset(seriesKeys, categoryKeys, startValues, endValues);
    }

    // --- CONSTRUCTOR TESTS ---

    @Test(expected = IllegalArgumentException.class)
    public void constructor_withNullNumberFormat_throwsException() {
        // A null NumberFormat should trigger an IllegalArgumentException.
        new IntervalCategoryToolTipGenerator("test format", (NumberFormat) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructor_withNullDateFormat_throwsException() {
        // A null DateFormat should trigger an IllegalArgumentException.
        new IntervalCategoryToolTipGenerator("test format", (DateFormat) null);
    }

    // --- createItemArray() TESTS ---

    @Test
    public void createItemArray_returnsCorrectItems() {
        // Arrange
        IntervalCategoryToolTipGenerator generator = new IntervalCategoryToolTipGenerator();
        DefaultIntervalCategoryDataset dataset = createSampleDataset();

        // Act
        Object[] items = generator.createItemArray(dataset, 0, 0);

        // Assert
        // The array should contain: {seriesName, categoryName, value, startValue, endValue}
        assertNotNull("The item array should not be null.", items);
        assertEquals("The item array should have 5 elements.", 5, items.length);
        assertEquals("Item 0 (Series Name) should be correct.", "Series 1", items[0]);
        assertEquals("Item 1 (Category Name) should be correct.", "Category 1", items[1]);
        assertEquals("Item 2 (Value) should be the interval midpoint.", 15.0, (Double) items[2], 0.001);
        assertEquals("Item 3 (Start Value) should be correct.", 10.0, items[3]);
        assertEquals("Item 4 (End Value) should be correct.", 20.0, items[4]);
    }

    @Test(expected = NullPointerException.class)
    public void createItemArray_withNullDataset_throwsException() {
        // Arrange
        IntervalCategoryToolTipGenerator generator = new IntervalCategoryToolTipGenerator();

        // Act & Assert
        // A NullPointerException is expected when the dataset is null.
        generator.createItemArray(null, 0, 0);
    }

    @Test(expected = ClassCastException.class)
    public void createItemArray_withNonIntervalDataset_throwsException() {
        // Arrange
        IntervalCategoryToolTipGenerator generator = new IntervalCategoryToolTipGenerator();
        // Use a dataset that is not an IntervalCategoryDataset
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(1.0, "S1", "C1"); // Add data to prevent other exceptions

        // Act & Assert
        // A ClassCastException is expected when the dataset is not an instance of IntervalCategoryDataset.
        generator.createItemArray(dataset, 0, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createItemArray_withInvalidRow_throwsException() {
        // Arrange
        IntervalCategoryToolTipGenerator generator = new IntervalCategoryToolTipGenerator();
        DefaultIntervalCategoryDataset dataset = createSampleDataset(); // 1x1 dataset

        // Act & Assert
        // An exception is expected for an out-of-bounds row index.
        generator.createItemArray(dataset, 1, 0);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void createItemArray_withInvalidColumn_throwsException() {
        // Arrange
        IntervalCategoryToolTipGenerator generator = new IntervalCategoryToolTipGenerator();
        DefaultIntervalCategoryDataset dataset = createSampleDataset(); // 1x1 dataset

        // Act & Assert
        // An exception is expected for an out-of-bounds column index.
        generator.createItemArray(dataset, 0, 1);
    }

    // --- equals() and hashCode() TESTS ---

    @Test
    public void testEquals() {
        // Arrange
        IntervalCategoryToolTipGenerator g1 = new IntervalCategoryToolTipGenerator();
        IntervalCategoryToolTipGenerator g2 = new IntervalCategoryToolTipGenerator();

        String customFormat = "({0}, {1}) = {2}";
        NumberFormat customNumberFormat = NumberFormat.getPercentInstance();
        IntervalCategoryToolTipGenerator g3 = new IntervalCategoryToolTipGenerator(customFormat, customNumberFormat);
        IntervalCategoryToolTipGenerator g4 = new IntervalCategoryToolTipGenerator(customFormat, customNumberFormat);

        DateFormat customDateFormat = DateFormat.getDateInstance();
        IntervalCategoryToolTipGenerator g5 = new IntervalCategoryToolTipGenerator(
                IntervalCategoryToolTipGenerator.DEFAULT_TOOL_TIP_FORMAT_STRING, customDateFormat);

        // Assert
        assertTrue("An object should be equal to itself.", g1.equals(g1));
        assertTrue("Two default generators should be equal.", g1.equals(g2));
        assertTrue("Two custom generators with the same configuration should be equal.", g3.equals(g4));

        assertFalse("Generators with different formatters should not be equal.", g1.equals(g3));
        assertFalse("Generators with different formatter types should not be equal.", g1.equals(g5));
        assertFalse("A generator should not be equal to null.", g1.equals(null));
        assertFalse("A generator should not be equal to an object of a different type.", g1.equals("A string object"));
    }

    @Test
    public void testHashCode() {
        // Arrange
        IntervalCategoryToolTipGenerator g1 = new IntervalCategoryToolTipGenerator();
        IntervalCategoryToolTipGenerator g2 = new IntervalCategoryToolTipGenerator();
        IntervalCategoryToolTipGenerator g3 = new IntervalCategoryToolTipGenerator("different", NumberFormat.getInstance());

        // Assert
        assertEquals("Hash codes of two equal objects must be the same.", g1.hashCode(), g2.hashCode());
        assertNotEquals("Hash codes of two unequal objects should ideally be different.", g1.hashCode(), g3.hashCode());
    }
}