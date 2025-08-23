package org.jfree.chart.entity;

import org.jfree.chart.TestUtils;
import org.jfree.data.category.DefaultCategoryDataset;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.awt.geom.Rectangle2D;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

/**
 * A test suite for the {@link LegendItemEntity} class, focusing on serialization.
 */
class LegendItemEntityTest {

    /**
     * Verifies that serializing and then deserializing a LegendItemEntity instance
     * results in an object that is equal to the original. This test ensures that
     * all key properties are correctly preserved during the process.
     */
    @Test
    @DisplayName("A deserialized entity should be equal to the original")
    void serializationOfPopulatedInstanceShouldSucceed() {
        // Arrange: Create a fully configured LegendItemEntity instance.
        var area = new Rectangle2D.Double(1.0, 2.0, 3.0, 4.0);
        var dataset = new DefaultCategoryDataset();
        dataset.addValue(100.0, "Series 1", "Category 1");
        String seriesKey = "Series 1";

        var originalEntity = new LegendItemEntity<String>(area);
        originalEntity.setDataset(dataset);
        originalEntity.setSeriesKey(seriesKey);

        // Act: Serialize the original entity and then deserialize it back into an object.
        LegendItemEntity<String> deserializedEntity = TestUtils.serialised(originalEntity);

        // Assert: The deserialized entity should be a new instance, but logically equal to the original.
        assertNotSame(originalEntity, deserializedEntity, "Serialization should create a new object instance.");
        assertEquals(originalEntity, deserializedEntity, "Deserialized entity should be equal to the original.");
    }
}