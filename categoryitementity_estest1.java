package org.jfree.chart.entity;

import org.jfree.data.category.CategoryDataset;
import org.jfree.data.statistics.DefaultMultiValueCategoryDataset;
import org.junit.Test;

import java.awt.Rectangle;

import static org.junit.Assert.assertNull;

/**
 * Tests for the {@link CategoryItemEntity} class, focusing on its property setters and getters.
 */
public class CategoryItemEntityTest {

    /**
     * Verifies that the row key can be set to null and the getter will subsequently return null.
     * This tests the actual behavior, even though the method's Javadoc suggests null is not permitted.
     */
    @Test
    public void setRowKey_whenSetToNull_getterReturnsNull() {
        // Arrange: Create a CategoryItemEntity instance with standard initial values.
        Rectangle area = new Rectangle(10, 20, 30, 40);
        CategoryDataset<Integer, Integer> dataset = new DefaultMultiValueCategoryDataset<>();
        CategoryItemEntity<Integer, Integer> entity = new CategoryItemEntity<>(
                area, "Test ToolTip", "http://test.url", dataset, 1, 1);

        // Act: Set the row key to null.
        entity.setRowKey(null);

        // Assert: Verify that the retrieved row key is now null.
        Integer retrievedRowKey = entity.getRowKey();
        assertNull("The row key should be null after being explicitly set to null.", retrievedRowKey);
    }
}