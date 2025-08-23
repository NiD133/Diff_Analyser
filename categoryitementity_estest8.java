package org.jfree.chart.entity;

import org.jfree.data.statistics.DefaultStatisticalCategoryDataset;
import org.junit.Test;

import java.awt.geom.Rectangle2D;

import static org.junit.Assert.assertNotEquals;

/**
 * Tests for the {@link CategoryItemEntity} class, focusing on the equals() method.
 */
public class CategoryItemEntityTest {

    /**
     * Tests that two CategoryItemEntity objects with different column keys are not considered equal.
     * This also implicitly tests that an entity is not equal to a clone of a different entity.
     */
    @Test
    public void testEqualsShouldReturnFalseForDifferentColumnKeys() {
        // Arrange
        Rectangle2D.Double area = new Rectangle2D.Double(1.0, 2.0, 3.0, 4.0);
        DefaultStatisticalCategoryDataset<String, String> dataset = new DefaultStatisticalCategoryDataset<>();
        String rowKey = "Row 1";

        // Create a base entity.
        CategoryItemEntity<String, String> entity1 = new CategoryItemEntity<>(
                area, "Tooltip", "URL", dataset, rowKey, "Column 1");

        // Create a second entity that differs only by its column key.
        CategoryItemEntity<String, String> entity2 = new CategoryItemEntity<>(
                area, "Tooltip", "URL", dataset, rowKey, "Column 2");

        // Act & Assert
        // The two entities should not be equal because their column keys are different.
        assertNotEquals(entity1, entity2);
    }

    /**
     * This test preserves the logic of the original test (comparing one entity to a clone of another)
     * but with improved readability.
     */
    @Test
    public void testEqualsShouldReturnFalseWhenComparingToCloneOfDifferentEntity() {
        // Arrange
        Rectangle2D.Double area = new Rectangle2D.Double(1.0, 2.0, 3.0, 4.0);
        DefaultStatisticalCategoryDataset<Integer, Integer> dataset = new DefaultStatisticalCategoryDataset<>();
        Integer rowKey = 1;

        // Create the first entity.
        CategoryItemEntity<Integer, Integer> entity1 = new CategoryItemEntity<>(
                area, "Tooltip 1", "URL 1", dataset, rowKey, 10);

        // Create a second, different entity.
        CategoryItemEntity<Integer, Integer> entity2 = new CategoryItemEntity<>(
                area, "Tooltip 2", "URL 2", dataset, rowKey, 20);

        // Act
        // Clone the first entity. The clone() method returns an Object.
        Object clonedEntity1 = entity1.clone();

        // Assert
        // The second entity should not be equal to the clone of the first entity.
        assertNotEquals(entity2, clonedEntity1);
    }
}