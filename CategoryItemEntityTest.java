package org.jfree.chart.entity;

import java.awt.geom.Rectangle2D;
import org.jfree.chart.TestUtils;
import org.jfree.chart.internal.CloneUtils;
import org.jfree.data.category.DefaultCategoryDataset;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link CategoryItemEntity} class.
 */
public class CategoryItemEntityTest {

    /**
     * Tests the {@code equals} method to ensure it correctly distinguishes between different field values.
     */
    @Test
    public void testEquals() {
        // Create a sample dataset
        DefaultCategoryDataset<String, String> dataset = createSampleDataset();

        // Create two identical CategoryItemEntity instances
        CategoryItemEntity<String, String> entity1 = createCategoryItemEntity(dataset, "R2", "C2");
        CategoryItemEntity<String, String> entity2 = createCategoryItemEntity(dataset, "R2", "C2");

        // Verify that the two entities are equal
        assertEquals(entity1, entity2);

        // Modify the area of entity1 and verify inequality
        entity1.setArea(new Rectangle2D.Double(4.0, 3.0, 2.0, 1.0));
        assertNotEquals(entity1, entity2);

        // Update entity2 to match entity1 and verify equality
        entity2.setArea(new Rectangle2D.Double(4.0, 3.0, 2.0, 1.0));
        assertEquals(entity1, entity2);

        // Modify the tooltip text of entity1 and verify inequality
        entity1.setToolTipText("New ToolTip");
        assertNotEquals(entity1, entity2);

        // Update entity2 to match entity1 and verify equality
        entity2.setToolTipText("New ToolTip");
        assertEquals(entity1, entity2);

        // Modify the URL text of entity1 and verify inequality
        entity1.setURLText("New URL");
        assertNotEquals(entity1, entity2);

        // Update entity2 to match entity1 and verify equality
        entity2.setURLText("New URL");
        assertEquals(entity1, entity2);
    }

    /**
     * Tests the cloning functionality of the {@link CategoryItemEntity} class.
     */
    @Test
    public void testCloning() throws CloneNotSupportedException {
        // Create a sample dataset
        DefaultCategoryDataset<String, String> dataset = createSampleDataset();

        // Create a CategoryItemEntity instance
        CategoryItemEntity<String, String> originalEntity = createCategoryItemEntity(dataset, "R2", "C2");

        // Clone the original entity
        CategoryItemEntity<String, String> clonedEntity = CloneUtils.clone(originalEntity);

        // Verify that the cloned entity is a different instance but equal to the original
        assertNotSame(originalEntity, clonedEntity);
        assertSame(originalEntity.getClass(), clonedEntity.getClass());
        assertEquals(originalEntity, clonedEntity);
    }

    /**
     * Tests the serialization and deserialization of the {@link CategoryItemEntity} class.
     */
    @Test
    public void testSerialization() {
        // Create a sample dataset
        DefaultCategoryDataset<String, String> dataset = createSampleDataset();

        // Create a CategoryItemEntity instance
        CategoryItemEntity<String, String> originalEntity = createCategoryItemEntity(dataset, "R2", "C2");

        // Serialize and deserialize the entity
        CategoryItemEntity<String, String> deserializedEntity = TestUtils.serialised(originalEntity);

        // Verify that the deserialized entity is equal to the original
        assertEquals(originalEntity, deserializedEntity);
    }

    /**
     * Helper method to create a sample dataset.
     *
     * @return a sample {@link DefaultCategoryDataset}.
     */
    private DefaultCategoryDataset<String, String> createSampleDataset() {
        DefaultCategoryDataset<String, String> dataset = new DefaultCategoryDataset<>();
        dataset.addValue(1.0, "R1", "C1");
        dataset.addValue(2.0, "R1", "C2");
        dataset.addValue(3.0, "R2", "C1");
        dataset.addValue(4.0, "R2", "C2");
        return dataset;
    }

    /**
     * Helper method to create a {@link CategoryItemEntity} instance.
     *
     * @param dataset the dataset.
     * @param rowKey the row key.
     * @param columnKey the column key.
     * @return a new {@link CategoryItemEntity} instance.
     */
    private CategoryItemEntity<String, String> createCategoryItemEntity(DefaultCategoryDataset<String, String> dataset, String rowKey, String columnKey) {
        return new CategoryItemEntity<>(
                new Rectangle2D.Double(1.0, 2.0, 3.0, 4.0), "ToolTip", "URL", dataset, rowKey, columnKey);
    }
}