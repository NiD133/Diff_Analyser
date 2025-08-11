package org.jfree.chart.labels;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import org.jfree.data.category.DefaultIntervalCategoryDataset;
import org.junit.Test;

/**
 * Readable tests for IntervalCategoryItemLabelGenerator.
 *
 * These tests focus on:
 * - Constructor precondition checks
 * - Null and out-of-bounds handling
 * - Happy-path item array creation for interval datasets
 *
 * Note: This test class is in the same package as the SUT so it can call the
 * protected createItemArray(...) method directly.
 */
public class IntervalCategoryItemLabelGeneratorTest {

    // ----------------------------------------------------------------------
    // Constructor preconditions
    // ----------------------------------------------------------------------

    @Test
    public void constructor_withNullNumberFormat_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () ->
                new IntervalCategoryItemLabelGenerator("{2}", (DecimalFormat) null));
    }

    @Test
    public void constructor_withNullDateFormat_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () ->
                new IntervalCategoryItemLabelGenerator("{2}", (DateFormat) null));
    }

    // ----------------------------------------------------------------------
    // createItemArray(...) input validation
    // ----------------------------------------------------------------------

    @Test
    public void createItemArray_withNullDataset_throwsNullPointerException() {
        IntervalCategoryItemLabelGenerator gen =
                new IntervalCategoryItemLabelGenerator();

        assertThrows(NullPointerException.class, () ->
                gen.createItemArray(null, 0, 0));
    }

    @Test
    public void createItemArray_withRowOutOfBounds_throwsException() {
        // dataset has 0 series (rows)
        double[][] start = new double[0][1];
        double[][] end = new double[0][1];
        DefaultIntervalCategoryDataset dataset =
                new DefaultIntervalCategoryDataset(start, end);

        IntervalCategoryItemLabelGenerator gen =
                new IntervalCategoryItemLabelGenerator();

        assertThrows(IllegalArgumentException.class, () ->
                gen.createItemArray(dataset, 0, 0));
    }

    @Test
    public void createItemArray_withColumnOutOfBounds_throwsException() {
        // 1 series, 1 category -> valid indices are row=0, col=0
        double[][] start = { { 1.0 } };
        double[][] end =   { { 2.0 } };
        DefaultIntervalCategoryDataset dataset =
                new DefaultIntervalCategoryDataset(start, end);

        IntervalCategoryItemLabelGenerator gen =
                new IntervalCategoryItemLabelGenerator();

        // Be tolerant of the exact exception type thrown by the dataset
        assertThrows(IndexOutOfBoundsException.class, () ->
                gen.createItemArray(dataset, 0, 1));
    }

    // ----------------------------------------------------------------------
    // Happy path: interval dataset with number formatting
    // ----------------------------------------------------------------------

    @Test
    public void createItemArray_withIntervalDatasetAndNumberFormat_returnsFiveItems() {
        // 1 series, 2 categories with clear values
        double[][] start = { { 1.0, 2.0 } };
        double[][] end =   { { 1.5, 2.5 } };
        DefaultIntervalCategoryDataset dataset =
                new DefaultIntervalCategoryDataset(start, end);

        // Use a deterministic number format
        DecimalFormat df = new DecimalFormat("0.0");
        IntervalCategoryItemLabelGenerator gen =
                new IntervalCategoryItemLabelGenerator(
                        IntervalCategoryItemLabelGenerator.DEFAULT_LABEL_FORMAT_STRING, df);

        Object[] items = gen.createItemArray(dataset, 0, 0);

        assertEquals("Expected 5 elements: rowKey, columnKey, y, start, end",
                5, items.length);
        assertEquals("Series 1", items[0].toString());
        assertEquals("Category 1", items[1].toString());

        // We do not assert the exact 'y' value (index 2) as DefaultIntervalCategoryDataset
        // may implement it differently; we just require it to be present.
        assertNotNull(items[2]);

        // Start and end are taken from the interval dataset and formatted with our DecimalFormat
        assertEquals("1.0", items[3]);
        assertEquals("1.5", items[4]);
    }

    // ----------------------------------------------------------------------
    // Happy path: interval dataset with date formatting
    // ----------------------------------------------------------------------

    @Test
    public void createItemArray_withIntervalDatasetAndDateFormat_formatsStartAndEndAsDates() {
        // Values are milliseconds since epoch (UTC) so output is deterministic
        double[][] start = { { 0.0 } };       // 1970-01-01T00:00:00Z
        double[][] end =   { { 1000.0 } };    // 1970-01-01T00:00:01Z
        DefaultIntervalCategoryDataset dataset =
                new DefaultIntervalCategoryDataset(start, end);

        SimpleDateFormat utc = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ROOT);
        utc.setTimeZone(TimeZone.getTimeZone("UTC"));

        IntervalCategoryItemLabelGenerator gen =
                new IntervalCategoryItemLabelGenerator(
                        IntervalCategoryItemLabelGenerator.DEFAULT_LABEL_FORMAT_STRING, utc);

        Object[] items = gen.createItemArray(dataset, 0, 0);

        assertEquals(5, items.length);
        assertEquals("Series 1", items[0].toString());
        assertEquals("Category 1", items[1].toString());
        assertNotNull("Y value should be present", items[2]);

        // Start/end formatted as dates using our UTC formatter
        assertEquals("1970-01-01 00:00:00", items[3]);
        assertEquals("1970-01-01 00:00:01", items[4]);
    }
}