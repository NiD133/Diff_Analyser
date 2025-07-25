package org.jfree.chart.axis;

import org.jfree.chart.TestUtils;
import org.jfree.chart.text.TextBlockAnchor;
import org.jfree.chart.api.RectangleAnchor;
import org.jfree.chart.text.TextAnchor;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link CategoryLabelPosition} class.
 */
public class CategoryLabelPositionTest {

    /**
     * Tests the equals() method to ensure it correctly identifies equal and unequal objects.
     */
    @Test
    public void testEquals() {
        // Create two identical CategoryLabelPosition objects
        CategoryLabelPosition position1 = new CategoryLabelPosition(
                RectangleAnchor.BOTTOM_LEFT, TextBlockAnchor.CENTER_RIGHT,
                TextAnchor.BASELINE_LEFT, Math.PI / 4.0,
                CategoryLabelWidthType.RANGE, 0.44f);
        CategoryLabelPosition position2 = new CategoryLabelPosition(
                RectangleAnchor.BOTTOM_LEFT, TextBlockAnchor.CENTER_RIGHT,
                TextAnchor.BASELINE_LEFT, Math.PI / 4.0,
                CategoryLabelWidthType.RANGE, 0.44f);

        // Test that they are equal
        assertEquals(position1, position2);
        assertEquals(position2, position1);

        // Modify position1 and test inequality
        position1 = new CategoryLabelPosition(RectangleAnchor.TOP,
                TextBlockAnchor.CENTER_RIGHT, TextAnchor.BASELINE_LEFT,
                Math.PI / 4.0, CategoryLabelWidthType.RANGE, 0.44f);
        assertNotEquals(position1, position2);

        // Update position2 to match position1 and test equality
        position2 = new CategoryLabelPosition(RectangleAnchor.TOP,
                TextBlockAnchor.CENTER_RIGHT, TextAnchor.BASELINE_LEFT,
                Math.PI / 4.0, CategoryLabelWidthType.RANGE, 0.44f);
        assertEquals(position1, position2);

        // Continue modifying and testing each field
        position1 = new CategoryLabelPosition(RectangleAnchor.TOP,
                TextBlockAnchor.CENTER, TextAnchor.BASELINE_LEFT, Math.PI / 4.0,
                CategoryLabelWidthType.RANGE, 0.44f);
        assertNotEquals(position1, position2);
        position2 = new CategoryLabelPosition(RectangleAnchor.TOP,
                TextBlockAnchor.CENTER, TextAnchor.BASELINE_LEFT, Math.PI / 4.0,
                CategoryLabelWidthType.RANGE, 0.44f);
        assertEquals(position1, position2);

        position1 = new CategoryLabelPosition(RectangleAnchor.TOP,
                TextBlockAnchor.CENTER, TextAnchor.CENTER, Math.PI / 4.0,
                CategoryLabelWidthType.RANGE, 0.44f);
        assertNotEquals(position1, position2);
        position2 = new CategoryLabelPosition(RectangleAnchor.TOP,
                TextBlockAnchor.CENTER, TextAnchor.CENTER, Math.PI / 4.0,
                CategoryLabelWidthType.RANGE, 0.44f);
        assertEquals(position1, position2);

        position1 = new CategoryLabelPosition(RectangleAnchor.TOP,
                TextBlockAnchor.CENTER, TextAnchor.CENTER, Math.PI / 6.0,
                CategoryLabelWidthType.RANGE, 0.44f);
        assertNotEquals(position1, position2);
        position2 = new CategoryLabelPosition(RectangleAnchor.TOP,
                TextBlockAnchor.CENTER, TextAnchor.CENTER, Math.PI / 6.0,
                CategoryLabelWidthType.RANGE, 0.44f);
        assertEquals(position1, position2);

        position1 = new CategoryLabelPosition(RectangleAnchor.TOP,
                TextBlockAnchor.CENTER, TextAnchor.CENTER, Math.PI / 6.0,
                CategoryLabelWidthType.CATEGORY, 0.44f);
        assertNotEquals(position1, position2);
        position2 = new CategoryLabelPosition(RectangleAnchor.TOP,
                TextBlockAnchor.CENTER, TextAnchor.CENTER, Math.PI / 6.0,
                CategoryLabelWidthType.CATEGORY, 0.44f);
        assertEquals(position1, position2);

        position1 = new CategoryLabelPosition(RectangleAnchor.TOP,
                TextBlockAnchor.CENTER, TextAnchor.CENTER, Math.PI / 6.0,
                CategoryLabelWidthType.CATEGORY, 0.55f);
        assertNotEquals(position1, position2);
        position2 = new CategoryLabelPosition(RectangleAnchor.TOP,
                TextBlockAnchor.CENTER, TextAnchor.CENTER, Math.PI / 6.0,
                CategoryLabelWidthType.CATEGORY, 0.55f);
        assertEquals(position1, position2);
    }

    /**
     * Tests that two equal objects have the same hash code.
     */
    @Test
    public void testHashCode() {
        // Create two identical CategoryLabelPosition objects
        CategoryLabelPosition position1 = new CategoryLabelPosition();
        CategoryLabelPosition position2 = new CategoryLabelPosition();

        // Test that they are equal and have the same hash code
        assertEquals(position1, position2);
        assertEquals(position1.hashCode(), position2.hashCode());
    }

    /**
     * Tests the serialization and deserialization of a CategoryLabelPosition object.
     */
    @Test
    public void testSerialization() {
        // Create a CategoryLabelPosition object
        CategoryLabelPosition originalPosition = new CategoryLabelPosition();

        // Serialize and deserialize the object
        CategoryLabelPosition deserializedPosition = TestUtils.serialised(originalPosition);

        // Test that the original and deserialized objects are equal
        assertEquals(originalPosition, deserializedPosition);
    }
}