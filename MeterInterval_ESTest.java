package org.jfree.chart.plot;

import org.junit.Test;
import static org.junit.Assert.*;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Paint;
import java.awt.Stroke;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.jfree.data.Range;

/**
 * A set of tests for the {@link MeterInterval} class, focusing on its construction,
 * property access, equality, and serialization.
 */
public class MeterIntervalTest {

    private static final Stroke TEST_STROKE_1 = new BasicStroke(1.0f);
    private static final Stroke TEST_STROKE_2 = new BasicStroke(2.0f);
    private static final Paint TEST_PAINT_1 = Color.RED;
    private static final Paint TEST_PAINT_2 = Color.BLUE;

    // =================================================================================
    // Constructor and Getter Tests
    // =================================================================================

    /**
     * Tests the full constructor to ensure all properties are correctly initialized.
     */
    @Test
    public void fullConstructorShouldSetAllProperties() {
        Range range = new Range(0.0, 50.0);
        MeterInterval interval = new MeterInterval("Normal", range, TEST_PAINT_1, TEST_STROKE_1, TEST_PAINT_2);

        assertEquals("Normal", interval.getLabel());
        assertSame(range, interval.getRange());
        assertEquals(TEST_PAINT_1, interval.getOutlinePaint());
        assertEquals(TEST_STROKE_1, interval.getOutlineStroke());
        assertEquals(TEST_PAINT_2, interval.getBackgroundPaint());
    }

    /**
     * Tests the simple constructor to ensure it correctly sets the label and range,
     * and applies the documented default values for paint and stroke.
     */
    @Test
    public void simpleConstructorShouldSetPropertiesWithDefaults() {
        Range range = new Range(50.0, 75.0);
        MeterInterval interval = new MeterInterval("High", range);

        assertEquals("High", interval.getLabel());
        assertEquals(range, interval.getRange());
        assertEquals(Color.YELLOW, interval.getOutlinePaint());
        assertEquals(new BasicStroke(2.0f), interval.getOutlineStroke());
        assertNull(interval.getBackgroundPaint());
    }

    /**
     * Verifies that the constructor throws an IllegalArgumentException when the 'range' argument is null.
     */
    @Test(expected = IllegalArgumentException.class)
    public void constructorShouldThrowExceptionForNullRange() {
        new MeterInterval("Test", null);
    }

    /**
     * Verifies that the constructor throws an IllegalArgumentException when the 'label' argument is null.
     */
    @Test(expected = IllegalArgumentException.class)
    public void constructorShouldThrowExceptionForNullLabel() {
        new MeterInterval(null, new Range(0, 10));
    }

    // =================================================================================
    // Equality and HashCode Tests
    // =================================================================================

    /**
     * Tests the equals() method for reflexivity: an object must equal itself.
     */
    @Test
    public void equalsShouldBeReflexive() {
        MeterInterval interval = new MeterInterval("Test", new Range(10, 20));
        assertTrue(interval.equals(interval));
    }

    /**
     * Tests that two identical but distinct instances are considered equal, and that
     * their hash codes are also equal.
     */
    @Test
    public void equalsShouldReturnTrueForIdenticalInstances() {
        MeterInterval interval1 = new MeterInterval("Normal", new Range(0, 50), TEST_PAINT_1, TEST_STROKE_1, TEST_PAINT_2);
        MeterInterval interval2 = new MeterInterval("Normal", new Range(0, 50), TEST_PAINT_1, TEST_STROKE_1, TEST_PAINT_2);

        assertTrue(interval1.equals(interval2));
        assertEquals(interval1.hashCode(), interval2.hashCode());
    }

    /**
     * An interval should not be equal to an object of a different type.
     */
    @Test
    public void equalsShouldReturnFalseForDifferentObjectType() {
        MeterInterval interval = new MeterInterval("Test", new Range(10, 20));
        assertFalse(interval.equals("Not an interval"));
    }

    /**
     * An interval should not be equal to null.
     */
    @Test
    public void equalsShouldReturnFalseForNull() {
        MeterInterval interval = new MeterInterval("Test", new Range(10, 20));
        assertFalse(interval.equals(null));
    }

    /**
     * Tests that intervals with different labels are not equal.
     */
    @Test
    public void equalsShouldReturnFalseWhenLabelIsDifferent() {
        MeterInterval interval1 = new MeterInterval("A", new Range(0, 50));
        MeterInterval interval2 = new MeterInterval("B", new Range(0, 50));
        assertFalse(interval1.equals(interval2));
    }

    /**
     * Tests that intervals with different ranges are not equal.
     */
    @Test
    public void equalsShouldReturnFalseWhenRangeIsDifferent() {
        MeterInterval interval1 = new MeterInterval("A", new Range(0, 50));
        MeterInterval interval2 = new MeterInterval("A", new Range(0, 60));
        assertFalse(interval1.equals(interval2));
    }

    /**
     * Tests that intervals with different outline paints are not equal.
     */
    @Test
    public void equalsShouldReturnFalseWhenOutlinePaintIsDifferent() {
        Range range = new Range(0, 50);
        MeterInterval interval1 = new MeterInterval("A", range, TEST_PAINT_1, TEST_STROKE_1, TEST_PAINT_2);
        MeterInterval interval2 = new MeterInterval("A", range, TEST_PAINT_2, TEST_STROKE_1, TEST_PAINT_2);
        assertFalse(interval1.equals(interval2));
    }

    /**
     * Tests that intervals with different outline strokes are not equal.
     */
    @Test
    public void equalsShouldReturnFalseWhenOutlineStrokeIsDifferent() {
        Range range = new Range(0, 50);
        MeterInterval interval1 = new MeterInterval("A", range, TEST_PAINT_1, TEST_STROKE_1, TEST_PAINT_2);
        MeterInterval interval2 = new MeterInterval("A", range, TEST_PAINT_1, TEST_STROKE_2, TEST_PAINT_2);
        assertFalse(interval1.equals(interval2));
    }

    /**
     * Tests that intervals with different background paints are not equal.
     */
    @Test
    public void equalsShouldReturnFalseWhenBackgroundPaintIsDifferent() {
        Range range = new Range(0, 50);
        MeterInterval interval1 = new MeterInterval("A", range, TEST_PAINT_1, TEST_STROKE_1, TEST_PAINT_1);
        MeterInterval interval2 = new MeterInterval("A", range, TEST_PAINT_1, TEST_STROKE_1, TEST_PAINT_2);
        assertFalse(interval1.equals(interval2));
    }

    /**
     * The Range.equals() method returns false for ranges containing NaN. This test
     * confirms that MeterInterval inherits this behavior.
     */
    @Test
    public void equalsShouldReturnFalseForRangesWithNaN() {
        Range rangeWithNaN1 = new Range(Double.NaN, Double.NaN);
        Range rangeWithNaN2 = new Range(Double.NaN, Double.NaN);

        MeterInterval interval1 = new MeterInterval("NaN Range", rangeWithNaN1);
        MeterInterval interval2 = new MeterInterval("NaN Range", rangeWithNaN2);

        // This is false because Range.equals() returns false for NaN values.
        assertFalse(interval1.equals(interval2));
    }

    // =================================================================================
    // Serialization Tests
    // =================================================================================

    /**
     * Verifies that a MeterInterval instance can be serialized and then deserialized
     * back into an equal object.
     */
    @Test
    public void serializationShouldPreserveObjectState() throws Exception {
        MeterInterval originalInterval = new MeterInterval("Normal", new Range(0, 50), Color.GREEN, new BasicStroke(3.0f), Color.ORANGE);
        MeterInterval deserializedInterval;

        // Serialize the object
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream objectStream = new ObjectOutputStream(byteStream);
        objectStream.writeObject(originalInterval);
        objectStream.close();

        // Deserialize the object
        ByteArrayInputStream inStream = new ByteArrayInputStream(byteStream.toByteArray());
        ObjectInputStream objectInStream = new ObjectInputStream(inStream);
        deserializedInterval = (MeterInterval) objectInStream.readObject();
        objectInStream.close();

        assertEquals(originalInterval, deserializedInterval);
    }
}