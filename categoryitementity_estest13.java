package org.jfree.chart.entity;

import org.jfree.data.category.CategoryDataset;
import org.jfree.data.statistics.DefaultBoxAndWhiskerCategoryDataset;
import org.jfree.data.statistics.DefaultMultiValueCategoryDataset;
import org.junit.Test;

import java.awt.Rectangle;
import java.awt.Shape;

import static org.junit.Assert.assertFalse;

/**
 * Contains tests for the equals() method in the {@link CategoryItemEntity} class.
 */
public class CategoryItemEntityEqualsTest {

    /**
     * Verifies that the equals() method returns false for two entities that
     * are identical in all aspects except for their underlying dataset.
     */
    @Test
    public void equals_shouldReturnFalse_whenDatasetsAreDifferent() {
        // Arrange
        Shape commonArea = new Rectangle();
        Integer commonKey = 0;

        // Create two different types of datasets to ensure they are not equal.
        CategoryDataset<Integer, Integer> dataset1 = new DefaultMultiValueCategoryDataset<>();
        CategoryDataset<Integer, Integer> dataset2 = new DefaultBoxAndWhiskerCategoryDataset<>();

        // Create the first entity using the first dataset.
        CategoryItemEntity<Integer, Integer> entity1 = new CategoryItemEntity<>(
                commonArea, null, null, dataset1, commonKey, commonKey);

        // Create a second entity that is identical to the first, but uses the second dataset.
        CategoryItemEntity<Integer, Integer> entity2 = new CategoryItemEntity<>(
                commonArea, null, null, dataset2, commonKey, commonKey);

        // Act
        boolean areEqual = entity1.equals(entity2);

        // Assert
        assertFalse("Entities with different datasets should not be equal", areEqual);
    }
}