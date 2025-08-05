package org.jfree.chart.entity;

import java.awt.geom.Rectangle2D;
import org.jfree.chart.TestUtils;
import org.jfree.chart.internal.CloneUtils;
import org.jfree.data.category.DefaultCategoryDataset;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link CategoryItemEntity} class.
 */
public class CategoryItemEntityTest {

    /**
     * Helper method to create a sample dataset for testing.
     */
    private DefaultCategoryDataset<String, String> createSampleDataset() {
        DefaultCategoryDataset<String, String> dataset = new DefaultCategoryDataset<>();
        dataset.addValue(1.0, "Row1", "Col1");
        dataset.addValue(2.0, "Row1", "Col2");
        dataset.addValue(3.0, "Row2", "Col1");
        dataset.addValue(4.0, "Row2", "Col2");
        return dataset;
    }

    /**
     * Helper method to create a CategoryItemEntity with specified parameters.
     */
    private CategoryItemEntity<String, String> createCategoryItemEntity(
            Rectangle2D area, String toolTip, String url, 
            DefaultCategoryDataset<String, String> dataset, 
            String rowKey, String columnKey) {
        return new CategoryItemEntity<>(area, toolTip, url, dataset, rowKey, columnKey);
    }

    /**
     * Test that the equals method can distinguish all the required fields.
     */
    @Test
    public void testEquals() {
        DefaultCategoryDataset<String, String> dataset = createSampleDataset();
        CategoryItemEntity<String, String> entity1 = createCategoryItemEntity(
                new Rectangle2D.Double(1.0, 2.0, 3.0, 4.0), "ToolTip", "URL", dataset, "Row2", "Col2");
        CategoryItemEntity<String, String> entity2 = createCategoryItemEntity(
                new Rectangle2D.Double(1.0, 2.0, 3.0, 4.0), "ToolTip", "URL", dataset, "Row2", "Col2");

        // Initial equality check
        assertEquals(entity1, entity2);

        // Test area change
        entity1.setArea(new Rectangle2D.Double(4.0, 3.0, 2.0, 1.0));
        assertNotEquals(entity1, entity2);
        entity2.setArea(new Rectangle2D.Double(4.0, 3.0, 2.0, 1.0));
        assertEquals(entity1, entity2);

        // Test tooltip change
        entity1.setToolTipText("New ToolTip");
        assertNotEquals(entity1, entity2);
        entity2.setToolTipText("New ToolTip");
        assertEquals(entity1, entity2);

        // Test URL change
        entity1.setURLText("New URL");
        assertNotEquals(entity1, entity2);
        entity2.setURLText("New URL");
        assertEquals(entity1, entity2);
    }

    /**
     * Test that cloning a CategoryItemEntity works as expected.
     */
    @Test
    public void testCloning() throws CloneNotSupportedException {
        DefaultCategoryDataset<String, String> dataset = createSampleDataset();
        CategoryItemEntity<String, String> originalEntity = createCategoryItemEntity(
                new Rectangle2D.Double(1.0, 2.0, 3.0, 4.0), "ToolTip", "URL", dataset, "Row2", "Col2");

        CategoryItemEntity<String, String> clonedEntity = CloneUtils.clone(originalEntity);

        // Ensure the cloned entity is a separate instance but equal
        assertNotSame(originalEntity, clonedEntity);
        assertSame(originalEntity.getClass(), clonedEntity.getClass());
        assertEquals(originalEntity, clonedEntity);
    }

    /**
     * Test that serialization and deserialization of a CategoryItemEntity works as expected.
     */
    @Test
    public void testSerialization() {
        DefaultCategoryDataset<String, String> dataset = createSampleDataset();
        CategoryItemEntity<String, String> originalEntity = createCategoryItemEntity(
                new Rectangle2D.Double(1.0, 2.0, 3.0, 4.0), "ToolTip", "URL", dataset, "Row2", "Col2");

        CategoryItemEntity<String, String> deserializedEntity = TestUtils.serialised(originalEntity);

        // Ensure the deserialized entity is equal to the original
        assertEquals(originalEntity, deserializedEntity);
    }
}