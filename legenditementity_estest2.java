package org.jfree.chart.entity;

import org.jfree.data.general.Dataset;
import org.jfree.data.xy.DefaultTableXYDataset;
import org.junit.Test;

import java.awt.Rectangle;

import static org.junit.Assert.assertSame;

/**
 * Tests for the {@link LegendItemEntity} class, focusing on its data handling.
 */
public class LegendItemEntityTest {

    /**
     * Verifies that the getDataset() method returns the exact same instance
     * that was previously set via the setDataset() method.
     */
    @Test
    public void setDataset_shouldStoreAndReturnTheSameDatasetInstance() {
        // Arrange: Create a legend entity and a dataset instance.
        Rectangle area = new Rectangle();
        LegendItemEntity<String> legendItemEntity = new LegendItemEntity<>(area);
        Dataset expectedDataset = new DefaultTableXYDataset();

        // Act: Associate the dataset with the entity.
        legendItemEntity.setDataset(expectedDataset);

        // Assert: The retrieved dataset should be the same instance as the one set.
        Dataset actualDataset = legendItemEntity.getDataset();
        assertSame("The dataset retrieved should be the exact same instance that was set.",
                expectedDataset, actualDataset);
    }
}