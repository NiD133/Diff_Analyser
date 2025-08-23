package org.jfree.chart.renderer.xy;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

/**
 * Unit tests for the {@link ClusteredXYBarRenderer} class, focusing on core
 * object behaviors like cloning and equality.
 */
public class ClusteredXYBarRendererTest {

    /**
     * Verifies that the clone() method creates a new object that is equal
     * to the original, but is a different object instance. This test validates
     * the contract between the clone() and equals() methods.
     */
    @Test
    public void clone_shouldProduceEqualButDistinctInstance() throws CloneNotSupportedException {
        // Arrange: Create an instance of the renderer.
        ClusteredXYBarRenderer originalRenderer = new ClusteredXYBarRenderer();

        // Act: Create a clone of the original instance.
        ClusteredXYBarRenderer clonedRenderer = (ClusteredXYBarRenderer) originalRenderer.clone();

        // Assert: The clone should be a different object instance...
        assertNotSame("The cloned object should be a new instance.", originalRenderer, clonedRenderer);
        
        // ...but it should be equal in value to the original.
        assertEquals("The cloned object should be equal to the original.", originalRenderer, clonedRenderer);
    }
}