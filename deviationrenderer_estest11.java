package org.jfree.chart.renderer.xy;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * A test suite for the {@link DeviationRenderer} class, focusing on its
 * cloning and equality behavior.
 */
public class DeviationRendererTest {

    /**
     * Verifies that a cloned DeviationRenderer is a separate instance but is
     * equal in value to the original object.
     */
    @Test
    public void clone_shouldProduceEqualButDistinctInstance() throws CloneNotSupportedException {
        // Arrange: Create an original renderer instance.
        DeviationRenderer originalRenderer = new DeviationRenderer(false, false);

        // Act: Clone the original renderer.
        DeviationRenderer clonedRenderer = (DeviationRenderer) originalRenderer.clone();

        // Assert: The clone should be a different object instance...
        assertNotSame("The cloned object should be a new instance.", originalRenderer, clonedRenderer);

        // ...but it should be logically equal to the original.
        assertEquals("The cloned object should be equal to the original.", originalRenderer, clonedRenderer);

        // Further, explicitly verify that key properties were copied correctly.
        assertTrue("The 'drawSeriesLineAsPath' property should be true for the clone.",
                clonedRenderer.getDrawSeriesLineAsPath());
        assertEquals("The alpha value should be correctly copied to the clone.",
                0.5F, clonedRenderer.getAlpha(), 0.01F);
    }
}