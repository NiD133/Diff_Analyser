package org.jfree.data;

import org.jfree.chart.TestUtils;
import org.jfree.chart.internal.CloneUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Readable, intention-revealing tests for ComparableObjectItem.
 */
class ComparableObjectItemTest {

    private static ComparableObjectItem item(int x, Object y) {
        return new ComparableObjectItem(x, y);
    }

    @Test
    @DisplayName("Constructor rejects null x-value")
    void constructor_nullX_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new ComparableObjectItem(null, "XYZ"));
    }

    @Test
    @DisplayName("equals: uses both x and y (object) for equality")
    void equals_comparesXAndObject() {
        // same x and same y -> equal
        ComparableObjectItem a = item(1, "XYZ");
        ComparableObjectItem b = item(1, "XYZ");
        assertEquals(a, b);

        // different x, same y -> not equal
        a = item(2, "XYZ");
        assertNotEquals(a, b);

        // same x, different y (null vs non-null) -> not equal
        b = item(2, "XYZ");
        a = item(2, null);
        assertNotEquals(a, b);

        // same x and both y null -> equal
        b = item(2, null);
        assertEquals(a, b);
    }

    @Test
    @DisplayName("clone: produces equal but distinct instance of same type")
    void cloning_producesEqualButDistinctCopy() throws CloneNotSupportedException {
        ComparableObjectItem original = item(1, "XYZ");
        ComparableObjectItem copy = CloneUtils.clone(original);

        assertNotSame(original, copy, "Clone should be a different instance");
        assertSame(original.getClass(), copy.getClass(), "Clone should be same runtime type");
        assertEquals(original, copy, "Clone should be equal to original");
    }

    @Test
    @DisplayName("serialization: round-trip preserves equality")
    void serialization_roundTripPreservesEquality() {
        ComparableObjectItem original = item(1, "XYZ");
        ComparableObjectItem restored = TestUtils.serialised(original);

        assertEquals(original, restored);
    }

    @Test
    @DisplayName("compareTo: orders by x only")
    void compareTo_ordersByXOnly() {
        ComparableObjectItem one = item(1, "XYZ");
        ComparableObjectItem two = item(2, "XYZ");
        ComparableObjectItem three = item(3, "XYZ");
        ComparableObjectItem anotherOne = item(1, "XYZ");

        // increasing order
        assertTrue(two.compareTo(one) > 0, "2 should be greater than 1");
        assertTrue(three.compareTo(one) > 0, "3 should be greater than 1");

        // equality by x
        assertEquals(0, anotherOne.compareTo(one), "Items with same x should compare equal");

        // decreasing order
        assertTrue(one.compareTo(two) < 0, "1 should be less than 2");
    }
}