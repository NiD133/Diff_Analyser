package org.jfree.chart.entity;

import org.junit.Test;
import java.awt.Rectangle;
import java.awt.Shape;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.statistics.DefaultMultiValueCategoryDataset;

/**
 * Tests for the {@link CategoryItemEntity} class, focusing on edge cases.
 */
public class CategoryItemEntity_ESTestTest6 {

    /**
     * This test verifies that calling equals() on a CategoryItemEntity
     * with null keys against its clone throws a NullPointerException.
     * This behavior occurs because the equals() method does not correctly
     * handle null checks for its row and column keys before comparison.
     */
    @Test(expected = NullPointerException.class)
    public void equals_onClonedEntityWithNullKeys_shouldThrowNullPointerException() {
        // Arrange: Create an entity with null row and column keys.
        Shape area = new Rectangle(10, 20, 30, 40);
        CategoryDataset<Integer, Integer> dataset = new DefaultMultiValueCategoryDataset<>();
        CategoryItemEntity<Integer, Integer> originalEntity = new CategoryItemEntity<>(
                area, 
                "Test ToolTip", 
                "http://test.url", 
                dataset, 
                null, // rowKey is null
                null  // columnKey is null
        );

        // Act: Clone the entity. The clone will also have null keys.
        Object clonedEntity = originalEntity.clone();

        // Assert: The equals() method is called.
        // We expect a NullPointerException because the method attempts to
        // dereference the null rowKey or columnKey. The assertion is handled
        // by the @Test(expected=...) annotation.
        originalEntity.equals(clonedEntity);
    }
}