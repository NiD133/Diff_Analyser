package org.jfree.chart.entity;

import org.jfree.data.category.CategoryDataset;
import org.junit.Test;
import java.awt.Rectangle;
import java.awt.Shape;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for the {@link CategoryItemEntity} class, focusing on constructor validation.
 */
public class CategoryItemEntityTest {

    /**
     * Verifies that the constructor throws an IllegalArgumentException
     * when the 'dataset' argument is null.
     */
    @Test
    public void constructorShouldThrowExceptionForNullDataset() {
        // Arrange: Create valid arguments for the constructor, except for the dataset.
        Shape dummyArea = new Rectangle(10, 20);
        String dummyTooltip = "Test Tooltip";
        String dummyUrl = "http://example.com";
        Integer rowKey = 1;
        Integer columnKey = 2;
        CategoryDataset<Integer, Integer> nullDataset = null;

        // Act & Assert: Attempt to create an entity with a null dataset and verify the exception.
        try {
            new CategoryItemEntity<>(
                    dummyArea,
                    dummyTooltip,
                    dummyUrl,
                    nullDataset, // The invalid argument being tested
                    rowKey,
                    columnKey);
            fail("Expected an IllegalArgumentException to be thrown, but no exception was caught.");
        } catch (IllegalArgumentException e) {
            // Verify that the correct exception was thrown with the expected message.
            assertEquals("Null 'dataset' argument.", e.getMessage());
        }
    }
}