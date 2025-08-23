package org.jfree.chart.entity;

import org.jfree.data.xy.XIntervalSeriesCollection;
import org.junit.Test;

import java.awt.geom.Rectangle2D;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Tests for the {@link LegendItemEntity} class, focusing on the equals() method.
 */
public class LegendItemEntityTest {

    /**
     * Verifies that two LegendItemEntity objects are not equal if one has a
     * different dataset.
     */
    @Test
    public void equals_shouldReturnFalse_whenDatasetIsDifferent() throws CloneNotSupportedException {
        // Arrange: Create an entity and a perfect clone of it.
        Rectangle2D.Double area = new Rectangle2D.Double(1.0, 2.0, 3.0, 4.0);
        LegendItemEntity<String> originalEntity = new LegendItemEntity<>(area);
        LegendItemEntity<String> clonedEntity = (LegendItemEntity<String>) originalEntity.clone();

        // Sanity check: the fresh clone should be equal to the original.
        assertEquals("A fresh clone should be equal to the original", originalEntity, clonedEntity);

        // Act: Modify the dataset of the original entity.
        XIntervalSeriesCollection<String> differentDataset = new XIntervalSeriesCollection<>();
        originalEntity.setDataset(differentDataset);

        // Assert: The original entity should no longer be equal to the unchanged clone.
        assertNotEquals("Entity should not be equal to its clone after its dataset is modified",
                originalEntity, clonedEntity);
    }
}