package org.jfree.chart.annotations;

import org.jfree.chart.Drawable;
import org.jfree.chart.block.BlockContainer;
import org.junit.Test;

import static org.junit.Assert.assertFalse;

/**
 * Unit tests for the {@link XYDrawableAnnotation} class, focusing on the equals() method.
 */
public class XYDrawableAnnotationTest {

    /**
     * Verifies that the equals() method returns false when comparing two annotations
     * that have different properties.
     */
    @Test
    public void testEqualsReturnsFalseForDifferentProperties() {
        // Arrange
        Drawable dummyDrawable = new BlockContainer();

        // Create a base annotation for comparison.
        XYDrawableAnnotation baseAnnotation = new XYDrawableAnnotation(
                10.0,   // x
                20.0,   // y
                100.0,  // displayWidth
                50.0,   // displayHeight
                1.0,    // drawScaleFactor
                dummyDrawable);

        // Create another annotation with a different displayWidth and drawScaleFactor.
        XYDrawableAnnotation differentAnnotation = new XYDrawableAnnotation(
                10.0,   // x (same)
                20.0,   // y (same)
                200.0,  // displayWidth (different)
                50.0,   // displayHeight (same)
                2.0,    // drawScaleFactor (different)
                dummyDrawable);

        // Act & Assert
        // The two annotations should not be considered equal.
        boolean areEqual = baseAnnotation.equals(differentAnnotation);
        assertFalse("Annotations with different properties should not be equal.", areEqual);

        // It is also good practice to test for symmetry in the equals contract.
        boolean areEqualSymmetric = differentAnnotation.equals(baseAnnotation);
        assertFalse("The equals() method should be symmetric.", areEqualSymmetric);
    }
}