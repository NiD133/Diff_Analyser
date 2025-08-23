package org.jfree.chart.entity;

import java.awt.geom.Rectangle2D;
import org.jfree.chart.internal.CloneUtils;
import org.jfree.data.category.DefaultCategoryDataset;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

// Renamed from LegendItemEntityTestTest2 to follow standard naming conventions.
public class LegendItemEntityTest {

    /**
     * Verifies that cloning a LegendItemEntity results in a new, distinct
     * object that is logically equal to the original. This test also confirms
     * that the clone is a shallow copy, meaning mutable fields like 'area'
     * and 'dataset' are shared between the original and the clone.
     */
    @Test
    public void testCloning() {
        // Arrange: Create a fully-populated entity to ensure all properties
        // are correctly handled during cloning.
        Rectangle2D area = new Rectangle2D.Double(1.0, 2.0, 3.0, 4.0);
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(100.0, "Series 1", "Category 1");
        String seriesKey = "Series 1";

        LegendItemEntity<String> original = new LegendItemEntity<>(area);
        original.setDataset(dataset);
        original.setSeriesKey(seriesKey);
        original.setToolTipText("Test ToolTip");
        original.setURLText("http://www.jfree.org");

        // Act: Clone the entity using the project's utility method.
        LegendItemEntity<String> clone = CloneUtils.clone(original);

        // Assert: Verify the clone's state and its relationship to the original.

        // 1. The clone must be a different object in memory.
        assertNotSame(original, clone, "The clone should be a new object instance.");
        
        // 2. The clone must be of the exact same class.
        assertSame(original.getClass(), clone.getClass(), "The clone should be of the same class.");

        // 3. The clone must be logically equal to the original.
        assertEquals(original, clone, "The clone should be equal to the original.");
        
        // 4. Verify the shallow copy behavior: internal object references
        // should be the same, confirming that the underlying objects are shared.
        assertSame(original.getArea(), clone.getArea(), "Area should be a shared instance (shallow copy).");
        assertSame(original.getDataset(), clone.getDataset(), "Dataset should be a shared instance (shallow copy).");
        assertSame(original.getSeriesKey(), clone.getSeriesKey(), "SeriesKey should be a shared instance.");
    }
}