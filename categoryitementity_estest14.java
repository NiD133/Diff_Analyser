package org.jfree.chart.entity;

import org.jfree.data.category.CategoryDataset;
import org.jfree.data.statistics.DefaultMultiValueCategoryDataset;
import org.junit.Test;

import java.awt.Rectangle;
import java.awt.Shape;

import static org.junit.Assert.assertSame;

/**
 * Tests for the {@link CategoryItemEntity} class, focusing on the getDataset method.
 */
public class CategoryItemEntityTest {

    /**
     * Verifies that the getDataset() method returns the same dataset instance
     * that was provided in the constructor.
     */
    @Test
    public void getDataset_shouldReturnDatasetFromConstructor() {
        // Arrange: Create the necessary objects for the entity.
        Shape testArea = new Rectangle(10, 20, 30, 40);
        String tooltipText = "Test Tooltip";
        String urlText = "http://www.jfree.org/jfreechart/";
        CategoryDataset<String, String> expectedDataset = new DefaultMultiValueCategoryDataset<>();
        String rowKey = "Row 1";
        String columnKey = "Column A";

        CategoryItemEntity<String, String> entity = new CategoryItemEntity<>(
                testArea,
                tooltipText,
                urlText,
                expectedDataset,
                rowKey,
                columnKey
        );

        // Act: Call the method under test.
        CategoryDataset<String, String> actualDataset = entity.getDataset();

        // Assert: Verify that the returned dataset is the exact same instance.
        assertSame("The dataset returned by getDataset() should be the same instance provided to the constructor.",
                expectedDataset, actualDataset);
    }
}