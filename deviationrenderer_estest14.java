package org.jfree.chart.renderer.xy;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Contains tests for the {@link DeviationRenderer} class and its subclasses,
 * focusing on its default state and pass management logic.
 */
public class DeviationRendererTest {

    /**
     * Verifies that the constructor of a {@link DeviationStepRenderer}
     * (a subclass of DeviationRenderer) correctly initializes its default properties.
     * The alpha value should be 0.5f, and drawing the series line as a path
     * should be enabled by default.
     */
    @Test
    public void constructor_shouldSetDefaultProperties() {
        // Arrange: Create a new renderer instance.
        DeviationStepRenderer renderer = new DeviationStepRenderer();

        // Assert: Verify the renderer's default state.
        // 1. Check the default alpha transparency for the deviation shading.
        assertEquals("Default alpha should be 0.5F", 0.5F, renderer.getAlpha(), 0.01F);

        // 2. Check that drawing the series line as a path is enabled, as this is
        // a required behavior for DeviationRenderer.
        assertTrue("drawSeriesLineAsPath should be true by default", renderer.getDrawSeriesLineAsPath());
    }

    /**
     * Verifies that the isItemPass() method returns true for pass 2.
     * According to the DeviationRenderer's implementation, pass 2 is
     * designated for drawing item shapes (the "item pass").
     */
    @Test
    public void isItemPass_whenPassIsTwo_shouldReturnTrue() {
        // Arrange: Create a renderer and define the pass number for drawing items.
        DeviationStepRenderer renderer = new DeviationStepRenderer();
        int itemDrawingPass = 2;

        // Act: Check if the given pass is the item drawing pass.
        boolean result = renderer.isItemPass(itemDrawingPass);

        // Assert: The result should be true.
        assertTrue("isItemPass(2) should return true, as it's the designated item drawing pass.", result);
    }
}