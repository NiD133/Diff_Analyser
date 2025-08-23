package org.jfree.chart.annotations;

import org.jfree.chart.block.BlockContainer;
import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * A test suite for the {@link XYDrawableAnnotation} class, focusing on the equals() method.
 */
public class XYDrawableAnnotation_ESTestTest28 {

    /**
     * Verifies that the equals() method returns false when an XYDrawableAnnotation
     * is compared with an object of a different, unrelated class.
     */
    @Test
    public void equals_whenComparedWithDifferentType_shouldReturnFalse() {
        // Arrange
        // A BlockContainer is used as a simple implementation of the Drawable interface.
        BlockContainer drawable = new BlockContainer();

        // Create an instance of the class under test. The specific constructor
        // values are not critical for this test's objective.
        XYDrawableAnnotation drawableAnnotation = new XYDrawableAnnotation(
                10.0, 20.0, 30.0, 40.0, 1.0, drawable);

        // Create an instance of a different annotation class for comparison.
        Object otherObject = new XYBoxAnnotation(10.0, 20.0, 30.0, 40.0);

        // Act
        // Compare the XYDrawableAnnotation with the other object.
        boolean result = drawableAnnotation.equals(otherObject);

        // Assert
        // The result should be false because the objects are of different types.
        assertFalse(result);
    }
}