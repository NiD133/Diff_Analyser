package org.jfree.data;

import org.jfree.chart.TestUtils;
import org.jfree.chart.internal.CloneUtils;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link ComparableObjectItem} class.
 */
public class ComparableObjectItemTest {

    @Test
    public void constructor_ThrowsException_WhenXIsNull() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new ComparableObjectItem(null, "XYZ")
        );
        assertEquals("Null 'x' argument.", exception.getMessage());
    }

    @Test
    public void equals_ReturnsTrue_WhenItemsHaveSameXAndY() {
        ComparableObjectItem item1 = new ComparableObjectItem(1, "XYZ");
        ComparableObjectItem item2 = new ComparableObjectItem(1, "XYZ");
        assertEquals(item1, item2, "Items with same x and y should be equal");
    }

    @Test
    public void equals_ReturnsFalse_WhenItemsHaveDifferentX() {
        ComparableObjectItem item1 = new ComparableObjectItem(1, "XYZ");
        ComparableObjectItem item2 = new ComparableObjectItem(2, "XYZ");
        assertNotEquals(item1, item2, "Items with different x should not be equal");
    }

    @Test
    public void equals_ReturnsFalse_WhenItemsHaveNullYVsNonNullY() {
        ComparableObjectItem item1 = new ComparableObjectItem(2, null);
        ComparableObjectItem item2 = new ComparableObjectItem(2, "XYZ");
        assertNotEquals(item1, item2, "Item with null y should not equal item with non-null y");
    }

    @Test
    public void equals_ReturnsTrue_WhenBothItemsHaveNullY() {
        ComparableObjectItem item1 = new ComparableObjectItem(2, null);
        ComparableObjectItem item2 = new ComparableObjectItem(2, null);
        assertEquals(item1, item2, "Items with same x and both null y should be equal");
    }

    @Test
    public void cloning_CreatesEqualButDistinctInstance() throws CloneNotSupportedException {
        ComparableObjectItem original = new ComparableObjectItem(1, "XYZ");
        ComparableObjectItem clone = CloneUtils.clone(original);
        
        assertNotSame(original, clone, "Clone should be a different instance");
        assertSame(original.getClass(), clone.getClass(), "Clone should have same class");
        assertEquals(original, clone, "Clone should be equal to original");
    }

    @Test
    public void serialization_DeserializesToEqualInstance() {
        ComparableObjectItem original = new ComparableObjectItem(1, "XYZ");
        ComparableObjectItem deserialized = TestUtils.serialised(original);
        assertEquals(original, deserialized, "Deserialized item should equal original");
    }

    @Test
    public void compareTo_ReturnsPositive_WhenCurrentXIsGreater() {
        ComparableObjectItem larger = new ComparableObjectItem(2, "XYZ");
        ComparableObjectItem smaller = new ComparableObjectItem(1, "XYZ");
        assertTrue(larger.compareTo(smaller) > 0, "Larger x should return positive when compared to smaller x");
    }

    @Test
    public void compareTo_ReturnsNegative_WhenCurrentXIsSmaller() {
        ComparableObjectItem smaller = new ComparableObjectItem(1, "XYZ");
        ComparableObjectItem larger = new ComparableObjectItem(2, "XYZ");
        assertTrue(smaller.compareTo(larger) < 0, "Smaller x should return negative when compared to larger x");
    }

    @Test
    public void compareTo_ReturnsZero_WhenItemsHaveSameX() {
        ComparableObjectItem item1 = new ComparableObjectItem(1, "XYZ");
        ComparableObjectItem item2 = new ComparableObjectItem(1, "XYZ");
        assertEquals(0, item1.compareTo(item2), "Items with same x should return zero");
    }
}