package org.jfree.chart.entity;

import org.jfree.data.category.CategoryDataset;
import org.jfree.data.statistics.DefaultMultiValueCategoryDataset;
import org.junit.Test;

import java.awt.Rectangle;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Unit tests for the {@link CategoryItemEntity} class, focusing on exception handling.
 *
 * Note: The original test class name 'CategoryItemEntity_ESTestTest5' was renamed for clarity.
 */
public class CategoryItemEntityTest {

    /**
     * Verifies that calling setDataset() with a null argument throws an
     * IllegalArgumentException, as the dataset is a required component.
     */
    @Test
    public void setDataset_whenDatasetIsNull_throwsIllegalArgumentException() {
        // Arrange: Create a CategoryItemEntity with valid initial parameters.
        Rectangle area = new Rectangle(10, 20, 30, 40);
        CategoryDataset<String, String> initialDataset = new DefaultMultiValueCategoryDataset<>();
        CategoryItemEntity<String, String> entity = new CategoryItemEntity<>(
                area,
                "Test ToolTip",
                "http://test.url",
                initialDataset,
                "Row 1",
                "Column 1");

        // Act & Assert: Attempt to set a null dataset and verify the expected exception.
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> entity.setDataset(null)
        );

        // Further verify that the exception message is correct.
        assertEquals("Null 'dataset' argument.", exception.getMessage());
    }
}