package org.jfree.chart.plot;

import static org.junit.Assert.*;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Paint;
import java.awt.Stroke;

import org.jfree.data.Range;
import org.junit.Test;

/**
 * Readable tests for MeterInterval.
 *
 * Focus areas:
 * - Constructor argument validation
 * - Getter behavior (including defaults)
 * - Equality semantics for label, range, outline paint/stroke, and background paint
 */
public class MeterIntervalTest {

    // ---------------------------------------------------------------------
    // Constructor validation
    // ---------------------------------------------------------------------

    @Test(expected = IllegalArgumentException.class)
    public void constructor_rejectsNullRange_minimalCtor() {
        new MeterInterval("label", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructor_rejectsNullRange_fullCtor() {
        new MeterInterval("label", null, Color.RED, new BasicStroke(2f), Color.GRAY);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructor_rejectsNullLabel_minimalCtor() {
        new MeterInterval(null, new Range(0.0, 1.0));
    }

    // ---------------------------------------------------------------------
    // Getter behavior
    // ---------------------------------------------------------------------

    @Test
    public void minimalCtor_returnsDefaultsAndSameRangeInstance() {
        // Arrange
        String label = "Speed";
        Range range = new Range(0.0, 100.0);

        // Act
        MeterInterval interval = new MeterInterval(label, range);

        // Assert
        assertEquals("label", label, interval.getLabel());
        assertSame("range instance should be stored as-is", range, interval.getRange());

        // Defaults from minimal constructor:
        Paint outlinePaint = interval.getOutlinePaint();
        assertEquals("default outline paint", Color.YELLOW, outlinePaint);

        Stroke outlineStroke = interval.getOutlineStroke();
        assertNotNull("default outline stroke must be non-null", outlineStroke);
        assertTrue("default outline stroke should be a BasicStroke",
                outlineStroke instanceof BasicStroke);
        assertEquals("default outline stroke width", 2.0f,
                ((BasicStroke) outlineStroke).getLineWidth(), 0.0f);

        assertNull("default background should be null", interval.getBackgroundPaint());
    }

    @Test
    public void fullCtor_allowsNullPaintsAndStroke() {
        // Arrange
        Range range = new Range(10.0, 20.0);

        // Act
        MeterInterval interval = new MeterInterval("Any", range, null, null, null);

        // Assert
        assertNull(interval.getOutlinePaint());
        assertNull(interval.getOutlineStroke());
        assertNull(interval.getBackgroundPaint());
    }

    @Test
    public void fullCtor_usesProvidedPaintsAndStroke() {
        // Arrange
        Range range = new Range(10.0, 20.0);
        Paint outlinePaint = Color.RED;
        Stroke outlineStroke = new BasicStroke(3.0f);
        Paint background = Color.LIGHT_GRAY;

        // Act
        MeterInterval interval = new MeterInterval("Custom", range, outlinePaint, outlineStroke, background);

        // Assert
        assertSame(outlinePaint, interval.getOutlinePaint());
        assertSame(outlineStroke, interval.getOutlineStroke());
        assertSame(background, interval.getBackgroundPaint());
    }

    // ---------------------------------------------------------------------
    // Equality
    // ---------------------------------------------------------------------

    @Test
    public void equals_isReflexive() {
        MeterInterval interval = new MeterInterval("X", new Range(0, 1));
        assertTrue(interval.equals(interval));
    }

    @Test
    public void equals_sameValues_true() {
        Range range = new Range(0, 100);
        MeterInterval a = new MeterInterval("Speed", range, Color.BLUE, new BasicStroke(2f), Color.GRAY);
        MeterInterval b = new MeterInterval("Speed", range, Color.BLUE, new BasicStroke(2f), Color.GRAY);
        assertTrue(a.equals(b));
        assertTrue(b.equals(a));
    }

    @Test
    public void equals_differentLabel_false() {
        Range range = new Range(0, 100);
        MeterInterval a = new MeterInterval("A", range);
        MeterInterval b = new MeterInterval("B", range);
        assertFalse(a.equals(b));
    }

    @Test
    public void equals_differentRange_false() {
        MeterInterval a = new MeterInterval("Same", new Range(0, 100));
        MeterInterval b = new MeterInterval("Same", new Range(0, 200));
        assertFalse(a.equals(b));
    }

    @Test
    public void equals_differentOutlinePaint_false() {
        Range range = new Range(0, 100);
        MeterInterval a = new MeterInterval("Same", range, Color.RED, new BasicStroke(2f), null);
        MeterInterval b = new MeterInterval("Same", range, Color.BLUE, new BasicStroke(2f), null);
        assertFalse(a.equals(b));
    }

    @Test
    public void equals_differentOutlineStroke_false() {
        Range range = new Range(0, 100);
        MeterInterval a = new MeterInterval("Same", range, Color.BLACK, new BasicStroke(2f), null);
        MeterInterval b = new MeterInterval("Same", range, Color.BLACK, new BasicStroke(3f), null);
        assertFalse(a.equals(b));
    }

    @Test
    public void equals_differentBackgroundPaint_false() {
        Range range = new Range(0, 100);
        MeterInterval a = new MeterInterval("Same", range, Color.BLACK, new BasicStroke(2f), null);
        MeterInterval b = new MeterInterval("Same", range, Color.BLACK, new BasicStroke(2f), Color.GRAY);
        assertFalse(a.equals(b));
    }

    @Test
    public void equals_differentType_false() {
        MeterInterval interval = new MeterInterval("X", new Range(0, 1));
        assertFalse(interval.equals("X"));
    }

    // ---------------------------------------------------------------------
    // Misc
    // ---------------------------------------------------------------------

    @Test
    public void getRange_returnsSameInstanceNotCopy() {
        Range original = new Range(5, 10);
        MeterInterval interval = new MeterInterval("Y", original);
        assertSame(original, interval.getRange());
    }
}