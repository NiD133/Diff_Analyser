/* ======================================================
 * JFreeChart : a chart library for the Java(tm) platform
 * ======================================================
 * Tests for MeterInterval with improved readability and intent-revealing names.
 * The tests are organized per-concern and use small helpers to reduce noise.
 * ======================================================
 */

package org.jfree.chart.plot;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Paint;
import java.awt.Stroke;

import org.jfree.chart.TestUtils;
import org.jfree.data.Range;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Readable, intention-revealing tests for {@link MeterInterval}.
 */
public class MeterIntervalTest {

    // Common test data (kept expressive and centralized).
    private static final String LABEL_A = "Label A";
    private static final String LABEL_B = "Label B";

    private static final Range RANGE_12_34 = new Range(1.2, 3.4);
    private static final Range RANGE_56_78 = new Range(5.6, 7.8);

    private static final Paint OUTLINE_RED = Color.RED;
    private static final Paint OUTLINE_GREEN = Color.GREEN;

    private static final Stroke STROKE_THIN = new BasicStroke(1.0f);
    private static final Stroke STROKE_THICK = new BasicStroke(2.0f);

    private static final Paint BG_BLUE = Color.BLUE;
    private static final Paint BG_ORANGE = Color.ORANGE;

    // Factory for a canonical baseline instance used across tests.
    private static MeterInterval baseline() {
        return new MeterInterval(LABEL_A, RANGE_12_34, OUTLINE_RED, STROKE_THIN, BG_BLUE);
    }

    // -------------
    // equals(...) tests
    // -------------

    @Test
    @DisplayName("equals: two identical instances are equal (symmetry check)")
    public void equals_whenAllFieldsMatch() {
        MeterInterval m1 = baseline();
        MeterInterval m2 = baseline();

        assertEquals(m1, m2);
        assertEquals(m2, m1);
    }

    @Test
    @DisplayName("equals: differing label -> not equal")
    public void equals_detectsDifferentLabel() {
        MeterInterval m1 = baseline();
        MeterInterval m2 = new MeterInterval(LABEL_B, RANGE_12_34, OUTLINE_RED, STROKE_THIN, BG_BLUE);

        assertNotEquals(m1, m2);
    }

    @Test
    @DisplayName("equals: differing range -> not equal")
    public void equals_detectsDifferentRange() {
        MeterInterval m1 = baseline();
        MeterInterval m2 = new MeterInterval(LABEL_A, RANGE_56_78, OUTLINE_RED, STROKE_THIN, BG_BLUE);

        assertNotEquals(m1, m2);
    }

    @Test
    @DisplayName("equals: differing outline paint -> not equal")
    public void equals_detectsDifferentOutlinePaint() {
        MeterInterval m1 = baseline();
        MeterInterval m2 = new MeterInterval(LABEL_A, RANGE_12_34, OUTLINE_GREEN, STROKE_THIN, BG_BLUE);

        assertNotEquals(m1, m2);
    }

    @Test
    @DisplayName("equals: differing outline stroke -> not equal")
    public void equals_detectsDifferentOutlineStroke() {
        MeterInterval m1 = baseline();
        MeterInterval m2 = new MeterInterval(LABEL_A, RANGE_12_34, OUTLINE_RED, STROKE_THICK, BG_BLUE);

        assertNotEquals(m1, m2);
    }

    @Test
    @DisplayName("equals: differing background paint -> not equal")
    public void equals_detectsDifferentBackgroundPaint() {
        MeterInterval m1 = baseline();
        MeterInterval m2 = new MeterInterval(LABEL_A, RANGE_12_34, OUTLINE_RED, STROKE_THIN, BG_ORANGE);

        assertNotEquals(m1, m2);
    }

    // -------------
    // Constructor argument validation
    // -------------

    @Test
    @DisplayName("constructor: null label -> IllegalArgumentException")
    public void constructor_nullLabel_throws() {
        assertThrows(IllegalArgumentException.class,
                () -> new MeterInterval(null, RANGE_12_34));
    }

    @Test
    @DisplayName("constructor: null range -> IllegalArgumentException")
    public void constructor_nullRange_throws() {
        assertThrows(IllegalArgumentException.class,
                () -> new MeterInterval(LABEL_A, null));
    }

    // -------------
    // Getter behavior
    // -------------

    @Test
    @DisplayName("getters return the values supplied to the constructor")
    public void getters_returnSuppliedValues() {
        MeterInterval m = baseline();

        assertEquals(LABEL_A, m.getLabel());
        assertEquals(RANGE_12_34, m.getRange());

        // For paints/strokes we use equals (Color and BasicStroke implement it)
        assertEquals(OUTLINE_RED, m.getOutlinePaint());
        assertEquals(STROKE_THIN, m.getOutlineStroke());
        assertEquals(BG_BLUE, m.getBackgroundPaint());
    }

    // -------------
    // Cloning & Serialization
    // -------------

    @Test
    @DisplayName("cloning: class is immutable and not Cloneable")
    public void cloning_notSupported() {
        MeterInterval m = new MeterInterval("X", new Range(1.0, 2.0));
        assertFalse(m instanceof Cloneable, "MeterInterval should not implement Cloneable");
    }

    @Test
    @DisplayName("serialization: round-trip preserves equality")
    public void serialization_roundTripPreservesEquality() {
        MeterInterval original = new MeterInterval("X", new Range(1.0, 2.0));
        MeterInterval restored = TestUtils.serialised(original);

        assertEquals(original, restored);
    }
}