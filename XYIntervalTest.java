package org.jfree.data.xy;

import org.jfree.chart.TestUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link XYInterval} class with an emphasis on readability and intent.
 */
public class XYIntervalTest {

    // Default, easy-to-read test data used across tests
    private static final double X_LOW = 1.0;
    private static final double X_HIGH = 2.0;
    private static final double Y = 3.0;
    private static final double Y_LOW = 2.5;
    private static final double Y_HIGH = 3.5;

    private static XYInterval base() {
        return new XYInterval(X_LOW, X_HIGH, Y, Y_LOW, Y_HIGH);
    }

    private static XYInterval interval(double xLow, double xHigh, double y, double yLow, double yHigh) {
        return new XYInterval(xLow, xHigh, y, yLow, yHigh);
    }

    // ------------------------------------------------------------------------
    // equals(): same state => equal
    // ------------------------------------------------------------------------

    @Test
    void equals_whenAllFieldsMatch_returnsTrue() {
        XYInterval a = base();
        XYInterval b = base();

        assertEquals(a, b, "Intervals with identical values should be equal");
        // Also check reflexivity explicitly
        assertEquals(a, a, "equals must be reflexive");
    }

    // ------------------------------------------------------------------------
    // equals(): each field difference => not equal
    // These tests make it obvious which field differentiates equality.
    // ------------------------------------------------------------------------

    @Test
    void equals_whenXLowDiffers_returnsFalse() {
        XYInterval a = base();
        XYInterval b = interval(1.1, X_HIGH, Y, Y_LOW, Y_HIGH);

        assertNotEquals(a, b, "Different xLow should make intervals not equal");
    }

    @Test
    void equals_whenXHighDiffers_returnsFalse() {
        XYInterval a = base();
        XYInterval b = interval(X_LOW, 2.2, Y, Y_LOW, Y_HIGH);

        assertNotEquals(a, b, "Different xHigh should make intervals not equal");
    }

    @Test
    void equals_whenYDiffers_returnsFalse() {
        XYInterval a = base();
        XYInterval b = interval(X_LOW, X_HIGH, 3.3, Y_LOW, Y_HIGH);

        assertNotEquals(a, b, "Different y should make intervals not equal");
    }

    @Test
    void equals_whenYLowDiffers_returnsFalse() {
        XYInterval a = base();
        XYInterval b = interval(X_LOW, X_HIGH, Y, 2.6, Y_HIGH);

        assertNotEquals(a, b, "Different yLow should make intervals not equal");
    }

    @Test
    void equals_whenYHighDiffers_returnsFalse() {
        XYInterval a = base();
        XYInterval b = interval(X_LOW, X_HIGH, Y, Y_LOW, 3.6);

        assertNotEquals(a, b, "Different yHigh should make intervals not equal");
    }

    // ------------------------------------------------------------------------
    // equals(): basic contract checks
    // ------------------------------------------------------------------------

    @Test
    void equals_contract_reflexiveSymmetricTransitiveAndNonNull() {
        XYInterval a = base();
        XYInterval b = base();
        XYInterval c = base();

        // reflexive
        assertEquals(a, a, "equals must be reflexive");

        // symmetric
        assertEquals(a, b, "equals must be symmetric (a equals b)");
        assertEquals(b, a, "equals must be symmetric (b equals a)");

        // transitive
        assertEquals(b, c, "Precondition for transitivity: b equals c");
        assertEquals(a, c, "equals must be transitive (a equals c)");

        // non-null
        assertNotEquals(a, null, "Any non-null instance must not be equal to null");
    }

    // ------------------------------------------------------------------------
    // Cloning: class is not Cloneable (treated as immutable)
    // ------------------------------------------------------------------------

    @Test
    void notCloneable_byDesign() {
        XYInterval interval = base();
        assertFalse(interval instanceof Cloneable, "XYInterval is immutable and should not implement Cloneable");
    }

    // ------------------------------------------------------------------------
    // Serialization: round-trip should preserve equality and state
    // ------------------------------------------------------------------------

    @Test
    void serialization_roundTrip_preservesEqualityAndState() {
        XYInterval original = base();
        XYInterval copy = TestUtils.serialised(original);

        assertNotSame(original, copy, "Deserialization should create a distinct object");
        assertEquals(original, copy, "Serialized/deserialized instance should be equal to the original");

        // Additionally validate each field to make failures easier to diagnose
        assertIntervalStateEquals(original, copy);
    }

    // Helper to make state comparisons explicit and readable
    private static void assertIntervalStateEquals(XYInterval expected, XYInterval actual) {
        assertEquals(expected.getXLow(),  actual.getXLow(),  "xLow differs after serialization");
        assertEquals(expected.getXHigh(), actual.getXHigh(), "xHigh differs after serialization");
        assertEquals(expected.getY(),     actual.getY(),     "y differs after serialization");
        assertEquals(expected.getYLow(),  actual.getYLow(),  "yLow differs after serialization");
        assertEquals(expected.getYHigh(), actual.getYHigh(), "yHigh differs after serialization");
    }
}