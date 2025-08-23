package org.jfree.chart.entity;

import org.jfree.data.statistics.DefaultMultiValueCategoryDataset;
import org.junit.Test;

import javax.swing.text.DefaultCaret;
import java.awt.Shape;

import static org.junit.Assert.assertFalse;

/**
 * Tests for the {@link CategoryItemEntity} class, focusing on the equals() method.
 */
public class CategoryItemEntityTest {

    /**
     * Verifies that the equals() method returns false when a CategoryItemEntity
     * is compared to an object of a different type.
     */
    @Test
    public void equals_whenComparedWithDifferentObjectType_shouldReturnFalse() {
        // Arrange
        Shape area = new DefaultCaret();
        DefaultMultiValueCategoryDataset<Integer, Integer> dataset = new DefaultMultiValueCategoryDataset<>();
        
        CategoryItemEntity<Integer, Integer> entity = new CategoryItemEntity<>(
                area,
                "Test ToolTip",
                "Test URL",
                dataset,
                0, // rowKey
                0  // columnKey
        );

        Object objectOfDifferentType = "";

        // Act & Assert
        // The result should be false because the entity is being compared to a String.
        assertFalse(entity.equals(objectOfDifferentType));
    }
}