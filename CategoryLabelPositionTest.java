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
        CategoryLabelPosition position1 = createPosition(RectangleAnchor.BOTTOM_LEFT, TextBlockAnchor.CENTER_RIGHT, TextAnchor.BASELINE_LEFT, Math.PI / 4.0, CategoryLabelWidthType.RANGE, 0.44f);
        CategoryLabelPosition position2 = createPosition(RectangleAnchor.BOTTOM_LEFT, TextBlockAnchor.CENTER_RIGHT, TextAnchor.BASELINE_LEFT, Math.PI / 4.0, CategoryLabelWidthType.RANGE, 0.44f);
        
        // Test for equality
        assertEquals(position1, position2);
        assertEquals(position2, position1);

        // Change category anchor and test for inequality
        position1 = createPosition(RectangleAnchor.TOP, TextBlockAnchor.CENTER_RIGHT, TextAnchor.BASELINE_LEFT, Math.PI / 4.0, CategoryLabelWidthType.RANGE, 0.44f);
        assertNotEquals(position1, position2);
        
        // Update position2 to match position1 and test for equality
        position2 = createPosition(RectangleAnchor.TOP, TextBlockAnchor.CENTER_RIGHT, TextAnchor.BASELINE_LEFT, Math.PI / 4.0, CategoryLabelWidthType.RANGE, 0.44f);
        assertEquals(position1, position2);

        // Change label anchor and test for inequality
        position1 = createPosition(RectangleAnchor.TOP, TextBlockAnchor.CENTER, TextAnchor.BASELINE_LEFT, Math.PI / 4.0, CategoryLabelWidthType.RANGE, 0.44f);
        assertNotEquals(position1, position2);
        
        // Update position2 to match position1 and test for equality
        position2 = createPosition(RectangleAnchor.TOP, TextBlockAnchor.CENTER, TextAnchor.BASELINE_LEFT, Math.PI / 4.0, CategoryLabelWidthType.RANGE, 0.44f);
        assertEquals(position1, position2);

        // Change rotation anchor and test for inequality
        position1 = createPosition(RectangleAnchor.TOP, TextBlockAnchor.CENTER, TextAnchor.CENTER, Math.PI / 4.0, CategoryLabelWidthType.RANGE, 0.44f);
        assertNotEquals(position1, position2);
        
        // Update position2 to match position1 and test for equality
        position2 = createPosition(RectangleAnchor.TOP, TextBlockAnchor.CENTER, TextAnchor.CENTER, Math.PI / 4.0, CategoryLabelWidthType.RANGE, 0.44f);
        assertEquals(position1, position2);

        // Change rotation angle and test for inequality
        position1 = createPosition(RectangleAnchor.TOP, TextBlockAnchor.CENTER, TextAnchor.CENTER, Math.PI / 6.0, CategoryLabelWidthType.RANGE, 0.44f);
        assertNotEquals(position1, position2);
        
        // Update position2 to match position1 and test for equality
        position2 = createPosition(RectangleAnchor.TOP, TextBlockAnchor.CENTER, TextAnchor.CENTER, Math.PI / 6.0, CategoryLabelWidthType.RANGE, 0.44f);
        assertEquals(position1, position2);

        // Change width type and test for inequality
        position1 = createPosition(RectangleAnchor.TOP, TextBlockAnchor.CENTER, TextAnchor.CENTER, Math.PI / 6.0, CategoryLabelWidthType.CATEGORY, 0.44f);
        assertNotEquals(position1, position2);
        
        // Update position2 to match position1 and test for equality
        position2 = createPosition(RectangleAnchor.TOP, TextBlockAnchor.CENTER, TextAnchor.CENTER, Math.PI / 6.0, CategoryLabelWidthType.CATEGORY, 0.44f);
        assertEquals(position1, position2);

        // Change width ratio and test for inequality
        position1 = createPosition(RectangleAnchor.TOP, TextBlockAnchor.CENTER, TextAnchor.CENTER, Math.PI / 6.0, CategoryLabelWidthType.CATEGORY, 0.55f);
        assertNotEquals(position1, position2);
        
        // Update position2 to match position1 and test for equality
        position2 = createPosition(RectangleAnchor.TOP, TextBlockAnchor.CENTER, TextAnchor.CENTER, Math.PI / 6.0, CategoryLabelWidthType.CATEGORY, 0.55f);
        assertEquals(position1, position2);
    }

    /**
     * Tests that two equal objects have the same hash code.
     */
    @Test
    public void testHashCode() {
        CategoryLabelPosition position1 = new CategoryLabelPosition();
        CategoryLabelPosition position2 = new CategoryLabelPosition();
        
        // Ensure both objects are equal
        assertEquals(position1, position2);
        
        // Ensure hash codes are equal
        assertEquals(position1.hashCode(), position2.hashCode());
    }

    /**
     * Tests the serialization and deserialization of a CategoryLabelPosition object.
     */
    @Test
    public void testSerialization() {
        CategoryLabelPosition originalPosition = new CategoryLabelPosition();
        
        // Serialize and deserialize the object
        CategoryLabelPosition deserializedPosition = TestUtils.serialised(originalPosition);
        
        // Ensure the original and deserialized objects are equal
        assertEquals(originalPosition, deserializedPosition);
    }

    /**
     * Helper method to create a CategoryLabelPosition with specified parameters.
     */
    private CategoryLabelPosition createPosition(RectangleAnchor categoryAnchor, TextBlockAnchor labelAnchor, TextAnchor rotationAnchor, double angle, CategoryLabelWidthType widthType, float widthRatio) {
        return new CategoryLabelPosition(categoryAnchor, labelAnchor, rotationAnchor, angle, widthType, widthRatio);
    }
}