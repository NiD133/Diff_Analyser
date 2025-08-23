package org.jfree.chart.plot;

import org.jfree.data.Range;
import org.junit.Test;

import java.awt.Color;
import java.awt.Paint;
import java.awt.Stroke;

import static org.junit.Assert.assertNull;

/**
 * Unit tests for the {@link MeterInterval} class.
 */
public class MeterIntervalTest {

    /**
     * Verifies that getOutlineStroke() returns null when the MeterInterval
     * is constructed with a null outline stroke.
     */
    @Test
    public void getOutlineStroke_shouldReturnNull_whenConstructedWithNullStroke() {
        // Arrange: Create a MeterInterval, explicitly passing null for the outline stroke.
        // The other parameters are simple placeholders, as they are not relevant to this test.
        String label = "Test Interval";
        Range range = new Range(0.0, 50.0);
        Paint dummyPaint = Color.RED;
        
        MeterInterval meterInterval = new MeterInterval(label, range, dummyPaint, null, dummyPaint);

        // Act: Retrieve the outline stroke from the object.
        Stroke retrievedStroke = meterInterval.getOutlineStroke();

        // Assert: The retrieved stroke should be the same null value passed to the constructor.
        assertNull("The outline stroke should be null as it was set in the constructor.", retrievedStroke);
    }
}