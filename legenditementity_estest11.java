package org.jfree.chart.entity;

import org.jfree.data.general.Dataset;
import org.junit.Test;
import java.awt.geom.Rectangle2D;
import static org.junit.Assert.assertNull;

/**
 * Unit tests for the {@link LegendItemEntity} class.
 */
public class LegendItemEntityTest {

    /**
     * Verifies that the dataset is null by default when a LegendItemEntity is
     * created without one being explicitly set.
     */
    @Test
    public void getDatasetShouldReturnNullByDefault() {
        // Arrange: Create a LegendItemEntity with a shape but no dataset.
        Rectangle2D.Double area = new Rectangle2D.Double();
        LegendItemEntity<String> legendItemEntity = new LegendItemEntity<>(area);

        // Act: Retrieve the dataset from the entity.
        Dataset dataset = legendItemEntity.getDataset();

        // Assert: The retrieved dataset should be null.
        assertNull("The dataset should be null for a newly created entity", dataset);
    }
}