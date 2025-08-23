package org.jfree.chart.renderer.xy;

import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link DeviationRenderer} class.
 */
public class DeviationRenderer_ESTestTest15 { // Scaffolding dependency removed for clarity

    /**
     * Verifies the default state of a DeviationRenderer instance after creation.
     * A new renderer should have a default alpha of 0.5f and have path drawing enabled.
     */
    @Test
    public void constructor_ShouldSetDefaultProperties() {
        // Arrange: Create a new renderer instance.
        DeviationRenderer renderer = new DeviationRenderer(false, false);

        // Assert: Check that the default properties are set as expected.
        assertEquals("Default alpha value should be 0.5f.", 0.5F, renderer.getAlpha(), 0.01F);
        assertTrue("Drawing series line as a path should be true by default.", renderer.getDrawSeriesLineAsPath());
    }

    /**
     * Verifies that isItemPass() returns false for a pass index that is not
     * the designated item-drawing pass. According to the implementation, the
     * item-drawing pass is pass 2.
     */
    @Test
    public void isItemPass_ForNonItemPassIndex_ShouldReturnFalse() {
        // Arrange
        DeviationRenderer renderer = new DeviationRenderer(false, false);
        int nonItemPassIndex = 3; // The item pass is 2, so this should be false.

        // Act
        boolean isItemPass = renderer.isItemPass(nonItemPassIndex);

        // Assert
        assertFalse("isItemPass() should return false for a non-item pass index.", isItemPass);
    }
}