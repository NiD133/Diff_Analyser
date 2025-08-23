package org.jfree.chart.axis;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.jfree.chart.api.RectangleAnchor;
import org.jfree.chart.text.TextAnchor;
import org.jfree.chart.text.TextBlockAnchor;
import org.junit.Test;

/**
 * Unit tests for the {@link CategoryLabelPosition} class.
 */
public class CategoryLabelPositionTest {

    /**
     * Tests that the default constructor initializes a CategoryLabelPosition
     * instance with the correct default values.
     */
    @Test
    public void testDefaultConstructor_initializesWithDefaultValues() {
        // Arrange
        CategoryLabelPosition position = new CategoryLabelPosition();

        // Assert
        // The source code specifies these as the default values.
        assertEquals(RectangleAnchor.CENTER, position.getCategoryAnchor());
        assertEquals(TextBlockAnchor.BOTTOM_CENTER, position.getLabelAnchor());
        assertEquals(TextAnchor.CENTER, position.getRotationAnchor());
        assertEquals(CategoryLabelWidthType.CATEGORY, position.getWidthType());
        assertEquals(0.0, position.getAngle(), 0.001);
        assertEquals(0.95f, position.getWidthRatio(), 0.001f);
    }

    /**
     * Verifies the main contract for the equals() method:
     * 1. An object is equal to itself (reflexive).
     * 2. Two objects with the same state are equal.
     * 3. An object is not equal to null or an object of a different type.
     */
    @Test
    public void testEquals() {
        // Arrange
        CategoryLabelPosition position1 = new CategoryLabelPosition();
        CategoryLabelPosition position2 = new CategoryLabelPosition();
        CategoryLabelPosition position3 = new CategoryLabelPosition(
                RectangleAnchor.TOP, TextBlockAnchor.TOP_CENTER);

        // Assert
        // An object must be equal to itself.
        assertEquals(position1, position1);

        // Two separate instances with the same default values should be equal.
        assertEquals(position1, position2);

        // An instance should not be equal to one with different properties.
        assertNotEquals(position1, position3);

        // An instance should not be equal to null.
        assertNotEquals(null, position1);

        // An instance should not be equal to an object of a different type.
        assertNotEquals("A String object", position1);
    }
    
    /**
     * Verifies that two equal objects produce the same hash code, as required
     * by the Object.hashCode() contract.
     */
    @Test
    public void testHashCode_forEqualObjects() {
        // Arrange
        CategoryLabelPosition position1 = new CategoryLabelPosition();
        CategoryLabelPosition position2 = new CategoryLabelPosition();

        // Assert
        assertEquals(position1, position2); // Sanity check that they are equal
        assertEquals(position1.hashCode(), position2.hashCode());
    }
}