package org.jfree.chart.renderer.xy;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link ClusteredXYBarRenderer} class.
 */
public class ClusteredXYBarRendererTest {

    /**
     * Verifies that getPassCount() consistently returns 2.
     * <p>
     * The ClusteredXYBarRenderer requires two rendering passes: the first to draw
     * the shadows (if visible) and the second to draw the bars themselves. This
     * test confirms that the method correctly reports this requirement.
     */
    @Test
    public void getPassCount_shouldAlwaysReturnTwo() {
        // Arrange
        ClusteredXYBarRenderer renderer = new ClusteredXYBarRenderer();
        int expectedPassCount = 2;

        // Act
        int actualPassCount = renderer.getPassCount();

        // Assert
        assertEquals("The renderer should require two passes.", expectedPassCount, actualPassCount);
    }
}