package org.jfree.chart.axis;

import org.jfree.chart.TestUtils;
import org.jfree.chart.api.RectangleAnchor;
import org.jfree.chart.text.TextBlockAnchor;
import org.jfree.chart.text.TextAnchor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * Tests for the {@link CategoryLabelPosition} class.
 *
 * <p>This test suite focuses on verifying the correctness of the
 * {@link CategoryLabelPosition} class, particularly its {@code equals()} and
 * {@code hashCode()} methods, as well as its serialization capabilities.</p>
 */
public class CategoryLabelPositionTest {

    /**
     * Tests the {@code equals()} method to ensure that it correctly compares
     * different {@link CategoryLabelPosition} instances based on their field
     * values.
     *
     * <p>This test creates multiple {@link CategoryLabelPosition} instances
     * with varying field values (RectangleAnchor, TextBlockAnchor,
     * TextAnchor, angle, CategoryLabelWidthType, and widthRatio) and asserts
     * that the {@code equals()} method returns the correct result (true if
     * all fields are equal, false otherwise).</p>
     */
    @Test
    public void testEquals() {
        // Base CategoryLabelPosition instance for comparison
        CategoryLabelPosition basePosition = new CategoryLabelPosition(
                RectangleAnchor.BOTTOM_LEFT, TextBlockAnchor.CENTER_RIGHT,
                TextAnchor.BASELINE_LEFT, Math.PI / 4.0,
                CategoryLabelWidthType.RANGE, 0.44f);

        // Create an identical CategoryLabelPosition instance and verify equality
        CategoryLabelPosition identicalPosition = new CategoryLabelPosition(
                RectangleAnchor.BOTTOM_LEFT, TextBlockAnchor.CENTER_RIGHT,
                TextAnchor.BASELINE_LEFT, Math.PI / 4.0,
                CategoryLabelWidthType.RANGE, 0.44f);
        assertEquals(basePosition, identicalPosition, "Instances with identical fields should be equal.");
        assertEquals(identicalPosition, basePosition, "Equals should be reflexive.");

        // Test inequality with different RectangleAnchor
        CategoryLabelPosition differentRectangleAnchor = new CategoryLabelPosition(RectangleAnchor.TOP,
                TextBlockAnchor.CENTER_RIGHT, TextAnchor.BASELINE_LEFT,
                Math.PI / 4.0, CategoryLabelWidthType.RANGE, 0.44f);
        assertNotEquals(basePosition, differentRectangleAnchor, "Instances with different RectangleAnchor should not be equal.");

        // Test inequality with different TextBlockAnchor
        CategoryLabelPosition differentTextBlockAnchor = new CategoryLabelPosition(RectangleAnchor.TOP,
                TextBlockAnchor.CENTER, TextAnchor.BASELINE_LEFT, Math.PI / 4.0,
                CategoryLabelWidthType.RANGE, 0.44f);
        assertNotEquals(differentRectangleAnchor, differentTextBlockAnchor, "Instances with different TextBlockAnchor should not be equal.");

        // Test inequality with different TextAnchor
        CategoryLabelPosition differentTextAnchor = new CategoryLabelPosition(RectangleAnchor.TOP,
                TextBlockAnchor.CENTER, TextAnchor.CENTER, Math.PI / 4.0,
                CategoryLabelWidthType.RANGE, 0.44f);
        assertNotEquals(differentTextBlockAnchor, differentTextAnchor, "Instances with different TextAnchor should not be equal.");

        // Test inequality with different angle
        CategoryLabelPosition differentAngle = new CategoryLabelPosition(RectangleAnchor.TOP,
                TextBlockAnchor.CENTER, TextAnchor.CENTER, Math.PI / 6.0,
                CategoryLabelWidthType.RANGE, 0.44f);
        assertNotEquals(differentTextAnchor, differentAngle, "Instances with different angle should not be equal.");

        // Test inequality with different CategoryLabelWidthType
        CategoryLabelPosition differentWidthType = new CategoryLabelPosition(RectangleAnchor.TOP,
                TextBlockAnchor.CENTER, TextAnchor.CENTER, Math.PI / 6.0,
                CategoryLabelWidthType.CATEGORY, 0.44f);
        assertNotEquals(differentAngle, differentWidthType, "Instances with different CategoryLabelWidthType should not be equal.");

        // Test inequality with different widthRatio
        CategoryLabelPosition differentWidthRatio = new CategoryLabelPosition(RectangleAnchor.TOP,
                TextBlockAnchor.CENTER, TextAnchor.CENTER, Math.PI / 6.0,
                CategoryLabelWidthType.CATEGORY, 0.55f);
        assertNotEquals(differentWidthType, differentWidthRatio, "Instances with different widthRatio should not be equal.");
    }

    /**
     * Tests the {@code hashCode()} method to ensure that equal objects return
     * the same hash code.
     *
     * <p>This test creates two {@link CategoryLabelPosition} instances with
     * default values, asserts that they are equal using the {@code equals()}
     * method, and then verifies that their hash codes are also equal.</p>
     */
    @Test
    public void testHashCode() {
        // Create two CategoryLabelPosition instances with default values
        CategoryLabelPosition position1 = new CategoryLabelPosition();
        CategoryLabelPosition position2 = new CategoryLabelPosition();

        // Assert that the two instances are equal
        assertEquals(position1, position2, "Instances with default values should be equal.");

        // Get the hash codes of the two instances
        int hashCode1 = position1.hashCode();
        int hashCode2 = position2.hashCode();

        // Assert that the hash codes are equal
        assertEquals(hashCode1, hashCode2, "Equal instances should have the same hash code.");
    }

    /**
     * Tests the serialization and deserialization of a
     * {@link CategoryLabelPosition} instance.
     *
     * <p>This test creates a {@link CategoryLabelPosition} instance, serializes
     * it to a byte array, deserializes it back into a
     * {@link CategoryLabelPosition} instance, and then asserts that the
     * original and deserialized instances are equal.</p>
     */
    @Test
    public void testSerialization() {
        // Create a CategoryLabelPosition instance
        CategoryLabelPosition originalPosition = new CategoryLabelPosition();

        // Serialize and deserialize the instance
        CategoryLabelPosition deserializedPosition = TestUtils.serialised(originalPosition);

        // Assert that the original and deserialized instances are equal
        assertEquals(originalPosition, deserializedPosition, "The deserialized instance should be equal to the original instance.");
    }

}