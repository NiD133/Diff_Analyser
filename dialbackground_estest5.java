package org.jfree.chart.plot.dial;

import org.jfree.chart.util.GradientPaintTransformType;
import org.jfree.chart.util.StandardGradientPaintTransformer;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Contains tests for the equals() method of the {@link DialBackground} class.
 */
// The original test class name is kept to match the user's request context.
public class DialBackground_ESTestTest5 extends DialBackground_ESTest_scaffolding {

    /**
     * Verifies that the equals() method returns false if two DialBackground
     * objects have different GradientPaintTransformer properties.
     */
    @Test
    public void equals_shouldReturnFalse_whenGradientPaintTransformersDiffer() {
        // Arrange: Create two identical DialBackground objects.
        DialBackground background1 = new DialBackground();
        DialBackground background2 = new DialBackground();

        // Sanity check: Ensure the two objects are equal initially.
        assertEquals("Initially, two default DialBackground objects should be equal.", background1, background2);

        // Create a non-default transformer to introduce a difference.
        StandardGradientPaintTransformer differentTransformer =
                new StandardGradientPaintTransformer(GradientPaintTransformType.CENTER_VERTICAL);

        // Act: Modify the gradient paint transformer of the second object.
        background2.setGradientPaintTransformer(differentTransformer);

        // Assert: The two objects should no longer be considered equal.
        assertNotEquals("After modifying the transformer, the objects should not be equal.", background1, background2);
    }
}