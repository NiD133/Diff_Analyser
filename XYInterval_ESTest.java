package org.jfree.data.xy;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Readable and focused unit tests for XYInterval.
 *
 * Goals:
 * - Verify constructor stores values as-is.
 * - Verify each getter returns the expected value.
 * - Verify equals() contract (reflexive, symmetric, null, different type).
 * - Verify equals() considers all five fields.
 */
public class XYIntervalTest {

    private static final double EPS = 1e-12;

    private static void assertInterval(XYInterval i,
                                       double xLow, double xHigh,
                                       double y, double yLow, double yHigh) {
        assertEquals(xLow, i.getXLow(), EPS);
        assertEquals(xHigh, i.getXHigh(), EPS);
        assertEquals(y, i.getY(), EPS);
        assertEquals(yLow, i.getYLow(), EPS);
        assertEquals(yHigh, i.getYHigh(), EPS);
    }

    // Constructor and getters

    @Test
    public void constructorStoresAllValues_zero() {
        // Arrange + Act
        XYInterval interval = new XYInterval(0.0, 0.0, 0.0, 0.0, 0.0);

        // Assert
        assertInterval(interval, 0.0, 0.0, 0.0, 0.0, 0.0);
    }

    @Test
    public void constructorStoresAllValues_positive() {
        // Arrange + Act
        XYInterval interval = new XYInterval(1.0, 2.0, 3.0, 4.0, 5.0);

        // Assert
        assertInterval(interval, 1.0, 2.0, 3.0, 4.0, 5.0);
    }

    @Test
    public void constructorStoresAllValues_negativeAndMixed() {
        // Arrange + Act
        XYInterval interval = new XYInterval(-10.5, -2.0, 0.0, -100.0, 50.0);

        // Assert
        assertInterval(interval, -10.5, -2.0, 0.0, -100.0, 50.0);
    }

    // equals(): basic contract

    @Test
    public void equals_isReflexive() {
        XYInterval interval = new XYInterval(1, 2, 3, 4, 5);
        assertTrue(interval.equals(interval));
    }

    @Test
    public void equals_isSymmetricAndConsistent_whenAllFieldsMatch() {
        XYInterval a = new XYInterval(1, 2, 3, 4, 5);
        XYInterval b = new XYInterval(1, 2, 3, 4, 5);

        assertTrue(a.equals(b));
        assertTrue(b.equals(a));

        // Consistency check (repeated calls)
        assertTrue(a.equals(b));
        assertTrue(b.equals(a));
    }

    @Test
    public void equals_returnsFalseForNullAndDifferentType() {
        XYInterval interval = new XYInterval(1, 2, 3, 4, 5);

        assertFalse(interval.equals(null));
        assertFalse(interval.equals(new Object()));
    }

    // equals(): differences in individual fields

    @Test
    public void equals_returnsFalse_whenXLowDiffers() {
        XYInterval a = new XYInterval(1, 2, 3, 4, 5);
        XYInterval b = new XYInterval(9, 2, 3, 4, 5);
        assertFalse(a.equals(b));
        assertFalse(b.equals(a));
    }

    @Test
    public void equals_returnsFalse_whenXHighDiffers() {
        XYInterval a = new XYInterval(1, 2, 3, 4, 5);
        XYInterval b = new XYInterval(1, 9, 3, 4, 5);
        assertFalse(a.equals(b));
        assertFalse(b.equals(a));
    }

    @Test
    public void equals_returnsFalse_whenYDiffers() {
        XYInterval a = new XYInterval(1, 2, 3, 4, 5);
        XYInterval b = new XYInterval(1, 2, 9, 4, 5);
        assertFalse(a.equals(b));
        assertFalse(b.equals(a));
    }

    @Test
    public void equals_returnsFalse_whenYLowDiffers() {
        XYInterval a = new XYInterval(1, 2, 3, 4, 5);
        XYInterval b = new XYInterval(1, 2, 3, 9, 5);
        assertFalse(a.equals(b));
        assertFalse(b.equals(a));
    }

    @Test
    public void equals_returnsFalse_whenYHighDiffers() {
        XYInterval a = new XYInterval(1, 2, 3, 4, 5);
        XYInterval b = new XYInterval(1, 2, 3, 4, 9);
        assertFalse(a.equals(b));
        assertFalse(b.equals(a));
    }

    // A couple of representative edge-ish values

    @Test
    public void getters_workWithLargeMagnitudeValues() {
        double xl = -4479.95885;
        double xh = -4479.95885;
        double y = -446.52402887375;
        double yl = -482.4691016522;
        double yh = 1.0;

        XYInterval interval = new XYInterval(xl, xh, y, yl, yh);
        assertInterval(interval, xl, xh, y, yl, yh);
    }

    @Test
    public void getters_workWhenYHighLessThanYLow_orEqualValues() {
        // Although unusual, the class stores values as given without validation.
        XYInterval a = new XYInterval(2, 2, 1, 5, -1);
        XYInterval b = new XYInterval(10, 10, 10, 10, 10);

        assertInterval(a, 2, 2, 1, 5, -1);
        assertInterval(b, 10, 10, 10, 10, 10);
    }
}