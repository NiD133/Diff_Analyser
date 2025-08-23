package org.jfree.chart.renderer.xy;

import org.jfree.data.Range;
import org.jfree.data.xy.DefaultIntervalXYDataset;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the {@link DeviationRenderer} class.
 */
public class DeviationRendererTest {

    /**
     * Verifies that the default constructor initializes the renderer's properties
     * to their expected default values.
     */
    @Test
    public void constructor_shouldInitializePropertiesToDefaults() {
        // Arrange & Act: Create a new instance of the renderer.
        DeviationRenderer renderer = new DeviationRenderer();

        // Assert: Check that the properties have the correct default values.
        
        // The default alpha transparency for the deviation shading should be 0.5.
        assertEquals("Default alpha", 0.5F, renderer.getAlpha(), 0.0f);

        // This renderer requires drawing the series line as a path.
        assertTrue("drawSeriesLineAsPath should default to true", renderer.getDrawSeriesLineAsPath());
    }

    /**
     * Verifies that findRangeBounds() returns null when the dataset is empty,
     * as there is no data from which to determine a range.
     */
    @Test
    public void findRangeBounds_withEmptyDataset_shouldReturnNull() {
        // Arrange
        DeviationRenderer renderer = new DeviationRenderer();
        DefaultIntervalXYDataset emptyDataset = new DefaultIntervalXYDataset();

        // Act
        Range result = renderer.findRangeBounds(emptyDataset);

        // Assert
        assertNull("The range for an empty dataset should be null", result);
    }
}