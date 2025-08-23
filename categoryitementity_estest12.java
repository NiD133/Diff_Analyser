package org.jfree.chart.entity;

import org.jfree.data.category.CategoryDataset;
import org.jfree.data.statistics.DefaultMultiValueCategoryDataset;
import org.junit.Test;

import java.awt.Rectangle;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link CategoryItemEntity} class, focusing on the equals() method.
 */
public class CategoryItemEntityTest {

    /**
     * Verifies that two CategoryItemEntity instances with identical properties are considered equal.
     * <p>
     * The original test created two entities with the same row key (the integer 0),
     * but one was created directly while the other was derived from a component's height,
     * making the test's intent obscure. This version clarifies the purpose by creating
     * two identical objects in a straightforward manner.
     */
    @Test
    public void testEquals_withIdenticalEntities_shouldReturnTrue() {
        // Arrange: Create two CategoryItemEntity objects with the exact same state.
        Rectangle area = new Rectangle(10, 20, 30, 40);
        String toolTipText = "Test ToolTip";
        String urlText = "http://www.jfree.org/jfreechart/";
        CategoryDataset<String, String> dataset = new DefaultMultiValueCategoryDataset<>();
        String rowKey = "Row 1";
        String columnKey = "Column 1";

        CategoryItemEntity<String, String> entity1 = new CategoryItemEntity<>(
                area, toolTipText, urlText, dataset, rowKey, columnKey
        );

        CategoryItemEntity<String, String> entity2 = new CategoryItemEntity<>(
                area, toolTipText, urlText, dataset, rowKey, columnKey
        );

        // Act & Assert: The two entities should be equal.
        // Using assertEquals provides a more informative message on failure than assertTrue.
        assertEquals(entity1, entity2);

        // For completeness, also check the symmetric property of equals().
        assertEquals(entity2, entity1);
    }
}