package org.jfree.chart.entity;

import org.jfree.data.category.CategoryDataset;
import org.jfree.data.statistics.DefaultMultiValueCategoryDataset;
import org.junit.Test;

import java.awt.Rectangle;
import java.awt.Shape;

import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the {@link CategoryItemEntity} class.
 */
public class CategoryItemEntityTest {

    /**
     * Verifies the reflexivity of the equals() method.
     * An object must always be equal to itself.
     */
    @Test
    public void testEquals_onSameInstance_shouldReturnTrue() {
        // Arrange: Create a CategoryItemEntity instance with typical test data.
        Shape area = new Rectangle(10, 20, 30, 40);
        CategoryDataset<String, String> dataset = new DefaultMultiValueCategoryDataset<>();
        
        CategoryItemEntity<String, String> entity = new CategoryItemEntity<>(
                area,
                "Test ToolTip",
                "test.html",
                dataset,
                "Row 1",
                "Column 1");

        // Act & Assert: An object must be equal to itself.
        assertTrue("An entity instance should be equal to itself.", entity.equals(entity));
    }
}