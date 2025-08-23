package org.jfree.chart.plot;

import org.jfree.data.Range;
import org.junit.Test;

import java.awt.BasicStroke;
import java.awt.Paint;
import java.awt.Stroke;

import static org.junit.Assert.assertNull;

/**
 * Unit tests for the {@link MeterInterval} class.
 */
public class MeterIntervalTest {

    /**
     * Verifies that getOutlinePaint() returns null when the MeterInterval
     * is constructed with a null outline paint.
     */
    @Test
    public void getOutlinePaint_whenConstructedWithNull_returnsNull() {
        // Arrange: Create a MeterInterval with a null outline paint.
        String label = "Normal";
        Range range = new Range(0.0, 50.0);
        Stroke stroke = new BasicStroke(1.0f);
        Paint backgroundPaint = null; // Not relevant to this test
        
        // The outlinePaint is explicitly set to null for this test case.
        MeterInterval interval = new MeterInterval(label, range, null, stroke, backgroundPaint);

        // Act: Retrieve the outline paint from the interval.
        Paint actualOutlinePaint = interval.getOutlinePaint();

        // Assert: The retrieved paint should be null.
        assertNull("The outline paint should be null as provided in the constructor.", actualOutlinePaint);
    }
}