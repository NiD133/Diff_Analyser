package org.jfree.chart.annotations;

import org.jfree.chart.Drawable;
import org.jfree.chart.block.BlockContainer;
import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Contains unit tests for the equals() method of the {@link XYDrawableAnnotation} class.
 */
public class XYDrawableAnnotationTest {

    /**
     * Verifies that the equals() method returns false when comparing two
     * XYDrawableAnnotation objects that differ only by their display height.
     */
    @Test
    public void equals_ReturnsFalse_WhenDisplayHeightIsDifferent() {
        // Arrange: Create two annotations that are identical except for their display height.
        Drawable drawable = new BlockContainer();
        double x = 10.0;
        double y = 20.0;
        double width = 30.0;
        
        XYDrawableAnnotation annotation1 = new XYDrawableAnnotation(x, y, width, 40.0, drawable);
        XYDrawableAnnotation annotation2 = new XYDrawableAnnotation(x, y, width, 50.0, drawable);

        // Act: Compare the two annotations for equality.
        boolean areEqual = annotation1.equals(annotation2);

        // Assert: The result should be false, as the heights are different.
        assertFalse("Annotations with different display heights should not be equal.", areEqual);
    }
}