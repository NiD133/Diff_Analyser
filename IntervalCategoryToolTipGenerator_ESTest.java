package org.jfree.chart.labels;

import org.junit.Test;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Locale;

import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultIntervalCategoryDataset;

import static org.junit.Assert.*;

/**
 * Readable, maintainable tests for IntervalCategoryToolTipGenerator.
 * 
 * Focus areas:
 * - Constructor preconditions
 * - Basic equals/hashCode contracts
 * - createItemArray happy path and error handling
 */
public class IntervalCategoryToolTipGeneratorTest {

    // Helpers

    private static DefaultIntervalCategoryDataset dataset(double[][] starts, double[][] ends) {
        return new DefaultIntervalCategoryDataset(starts, ends);
    }

    private static IntervalCategoryToolTipGenerator generatorWithUSNumberFormat(String labelFormat) {
        NumberFormat nf = NumberFormat.getNumberInstance(Locale.US);
        return new IntervalCategoryToolTipGenerator(labelFormat, nf);
    }

    // Constructor preconditions

    @Test
    public void constructor_withNullNumberFormat_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () ->
                new IntervalCategoryToolTipGenerator("({0}, {1}) = {2}", (NumberFormat) null));
    }

    @Test
    public void constructor_withNullDateFormat_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () ->
                new IntervalCategoryToolTipGenerator("({0}, {1}) = {2}", (DateFormat) null));
    }

    // createItemArray: argument validation

    @Test
    public void createItemArray_withNullDataset_throwsNullPointerException() {
        IntervalCategoryToolTipGenerator gen = new IntervalCategoryToolTipGenerator();
        assertThrows(NullPointerException.class, () -> gen.createItemArray((CategoryDataset) null, 0, 0));
    }

    @Test
    public void createItemArray_withRowOutOfBounds_throwsIllegalArgumentException() {
        // Dataset with 0 rows (series)
        double[][] starts = new double[0][1];
        double[][] ends = new double[0][1];
        DefaultIntervalCategoryDataset ds = dataset(starts, ends);

        IntervalCategoryToolTipGenerator gen = new IntervalCategoryToolTipGenerator();
        assertThrows(IllegalArgumentException.class, () -> gen.createItemArray(ds, 0, 0));
    }

    @Test
    public void createItemArray_withColumnOutOfBounds_throwsIndexOutOfBoundsException() {
        // 2 rows, 1 column -> any column index >= 1 is out-of-bounds
        double[][] starts = new double[][] { {1.0}, {2.0} };
        double[][] ends   = new double[][] { {1.5}, {2.5} };
        DefaultIntervalCategoryDataset ds = dataset(starts, ends);

        IntervalCategoryToolTipGenerator gen = new IntervalCategoryToolTipGenerator();
        assertThrows(IndexOutOfBoundsException.class, () -> gen.createItemArray(ds, 0, 1));
    }

    // createItemArray: happy path

    @Test
    public void createItemArray_returnsFiveItems_andIncludesKeys() {
        // 2 rows (series), 2 columns (categories)
        double[][] starts = new double[][] { {1.0, 2.0}, {10.0, 20.0} };
        double[][] ends   = new double[][] { {1.5, 2.5}, {10.5, 20.5} };
        DefaultIntervalCategoryDataset ds = dataset(starts, ends);

        IntervalCategoryToolTipGenerator gen =
                generatorWithUSNumberFormat(IntervalCategoryToolTipGenerator.DEFAULT_TOOL_TIP_FORMAT_STRING);

        Object[] items = gen.createItemArray(ds, 1, 0);

        assertEquals("Expected five items: rowKey, columnKey, value, start, end", 5, items.length);
        assertEquals(ds.getRowKey(1), items[0]);
        assertEquals(ds.getColumnKey(0), items[1]);
        // Values are formatted using NumberFormat, so we only assert they are strings
        assertTrue(items[2] instanceof String);
        assertTrue(items[3] instanceof String);
        assertTrue(items[4] instanceof String);
    }

    @Test
    public void createItemArray_handlesLastValidColumnIndex() {
        // 1 row, 3 columns -> last valid column index is 2
        double[][] starts = new double[][] { {1.0, 2.0, 3.0} };
        double[][] ends   = new double[][] { {1.5, 2.5, 3.5} };
        DefaultIntervalCategoryDataset ds = dataset(starts, ends);

        IntervalCategoryToolTipGenerator gen =
                generatorWithUSNumberFormat(IntervalCategoryToolTipGenerator.DEFAULT_TOOL_TIP_FORMAT_STRING);

        Object[] items = gen.createItemArray(ds, 0, 2);
        assertEquals(5, items.length);
    }

    // equals/hashCode

    @Test
    public void equals_isReflexive() {
        IntervalCategoryToolTipGenerator gen = new IntervalCategoryToolTipGenerator();
        assertTrue(gen.equals(gen));
    }

    @Test
    public void equals_withDifferentLabelFormat_returnsFalse() {
        DateFormat df = DateFormat.getTimeInstance();
        IntervalCategoryToolTipGenerator a = new IntervalCategoryToolTipGenerator("", df);
        IntervalCategoryToolTipGenerator b =
                new IntervalCategoryToolTipGenerator(IntervalCategoryToolTipGenerator.DEFAULT_TOOL_TIP_FORMAT_STRING, df);
        assertFalse(a.equals(b));
    }

    @Test
    public void equals_withDifferentType_returnsFalse() {
        IntervalCategoryToolTipGenerator gen =
                new IntervalCategoryToolTipGenerator("format", NumberFormat.getInstance());
        assertFalse(gen.equals("format"));
    }

    @Test
    public void hashCode_doesNotThrow() {
        IntervalCategoryToolTipGenerator gen = new IntervalCategoryToolTipGenerator();
        // No behavioral contract asserted here, just that it is implemented and callable
        gen.hashCode();
    }
}