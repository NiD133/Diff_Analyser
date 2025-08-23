package org.jfree.chart.plot.dial;

import org.jfree.chart.util.GradientPaintTransformType;
import org.jfree.chart.util.GradientPaintTransformer;
import org.jfree.chart.util.StandardGradientPaintTransformer;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests for the {@link DialBackground} class, focusing on its default state.
 */
public class DialBackground_ESTestTest13 extends DialBackground_ESTest_scaffolding {

    /**
     * Verifies that the default constructor initializes a StandardGradientPaintTransformer
     * with a VERTICAL transform type.
     */
    @Test
    public void constructor_shouldSetDefaultGradientPaintTransformerToVertical() {
        // Arrange: Create a DialBackground instance using its default constructor.
        DialBackground dialBackground = new DialBackground();

        // Act: Retrieve the gradient paint transformer created by default.
        GradientPaintTransformer transformer = dialBackground.getGradientPaintTransformer();

        // Assert: Check that the transformer is the expected type and has the correct default setting.
        assertTrue("The transformer should be an instance of StandardGradientPaintTransformer.",
                transformer instanceof StandardGradientPaintTransformer);

        StandardGradientPaintTransformer standardTransformer = (StandardGradientPaintTransformer) transformer;
        assertEquals("The default transformer type should be VERTICAL.",
                GradientPaintTransformType.VERTICAL, standardTransformer.getType());
    }
}