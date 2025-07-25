package org.jfree.data;

import org.jfree.chart.TestUtils;
import org.jfree.chart.internal.CloneUtils;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link ComparableObjectItem} class.
 */
public class ComparableObjectItemTest {

    /**
     * Tests the constructor of {@link ComparableObjectItem} to ensure it throws
     * an IllegalArgumentException when the x-value is null.
     */
    @Test
    public void testConstructorWithNullXValue() {
        try {
            new ComparableObjectItem(null, "XYZ");
            fail("Expected IllegalArgumentException for null x-value.");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    /**
     * Tests the equals method to verify it correctly compares all fields.
     */
    @Test
    public void testEqualsMethod() {
        ComparableObjectItem item1 = new ComparableObjectItem(1, "XYZ");
        ComparableObjectItem item2 = new ComparableObjectItem(1, "XYZ");
        assertEquals(item1, item2, "Items with same x and y should be equal.");

        item1 = new ComparableObjectItem(2, "XYZ");
        assertNotEquals(item1, item2, "Items with different x should not be equal.");
        item2 = new ComparableObjectItem(2, "XYZ");
        assertEquals(item1, item2, "Items with same x and y should be equal.");

        item1 = new ComparableObjectItem(2, null);
        assertNotEquals(item1, item2, "Items with different y should not be equal.");
        item2 = new ComparableObjectItem(2, null);
        assertEquals(item1, item2, "Items with same x and y should be equal.");
    }

    /**
     * Tests the clone method to ensure it creates a distinct but equal copy.
     */
    @Test
    public void testCloning() throws CloneNotSupportedException {
        ComparableObjectItem originalItem = new ComparableObjectItem(1, "XYZ");
        ComparableObjectItem clonedItem = CloneUtils.clone(originalItem);
        assertNotSame(originalItem, clonedItem, "Cloned item should be a different instance.");
        assertSame(originalItem.getClass(), clonedItem.getClass(), "Cloned item should be of the same class.");
        assertEquals(originalItem, clonedItem, "Cloned item should be equal to the original.");
    }

    /**
     * Tests serialization and deserialization to ensure the object remains equal.
     */
    @Test
    public void testSerialization() {
        ComparableObjectItem originalItem = new ComparableObjectItem(1, "XYZ");
        ComparableObjectItem deserializedItem = TestUtils.serialised(originalItem);
        assertEquals(originalItem, deserializedItem, "Deserialized item should be equal to the original.");
    }

    /**
     * Tests the compareTo method to ensure it correctly orders items based on x-value.
     */
    @Test
    public void testCompareToMethod() {
        ComparableObjectItem item1 = new ComparableObjectItem(1, "XYZ");
        ComparableObjectItem item2 = new ComparableObjectItem(2, "XYZ");
        ComparableObjectItem item3 = new ComparableObjectItem(3, "XYZ");
        ComparableObjectItem item4 = new ComparableObjectItem(1, "XYZ");

        assertTrue(item2.compareTo(item1) > 0, "Item2 should be greater than Item1.");
        assertTrue(item3.compareTo(item1) > 0, "Item3 should be greater than Item1.");
        assertEquals(0, item4.compareTo(item1), "Item4 should be equal to Item1.");
        assertTrue(item1.compareTo(item2) < 0, "Item1 should be less than Item2.");
    }
}