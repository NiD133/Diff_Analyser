package org.jfree.chart.renderer.xy;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the property accessors of the {@link DeviationRenderer} class.
 *
 * Note: The original test class name 'DeviationRenderer_ESTestTest3' and its
 * scaffolding are preserved as they might be part of a larger, automatically
 * generated test suite.
 */
public class DeviationRenderer_ESTestTest3 extends DeviationRenderer_ESTest_scaffolding {

    /**
     * Verifies that the getAlpha() and setAlpha() methods for the renderer's
     * transparency level function correctly.
     * <p>
     * This test first checks the default alpha value of a newly instantiated renderer.
     * It then sets a new value and confirms that the getter returns the updated value.
     */
    @Test
    public void testAlphaPropertyGetterAndSetter() {
        // Arrange: Create an instance of a DeviationRenderer subclass.
        // The original test uses DeviationStepRenderer, which is a valid way
        // to test the inherited functionality from the DeviationRenderer base class.
        DeviationRenderer renderer = new DeviationStepRenderer(false, false);

        // Assert: Check that the default alpha value is 0.5F.
        assertEquals("The default alpha value should be 0.5F.",
                0.5F, renderer.getAlpha(), 0.01F);

        // Act: Set a new alpha value.
        renderer.setAlpha(0.0F);

        // Assert: Verify that the alpha value was updated correctly.
        assertEquals("The alpha value should have been updated to 0.0F.",
                0.0F, renderer.getAlpha(), 0.01F);
    }
}