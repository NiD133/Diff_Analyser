package org.jfree.chart.entity;

import org.jfree.data.category.CategoryDataset;
import org.jfree.data.statistics.DefaultMultiValueCategoryDataset;
import org.junit.Test;

import java.awt.Rectangle;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

/**
 * Tests for the {@link CategoryItemEntity} class, focusing on the getColumnKey() method.
 */
// The original test class name and inheritance are preserved for context.
public class CategoryItemEntity_ESTestTest4 extends CategoryItemEntity_ESTest_scaffolding {

    /**
     * Verifies that getColumnKey() returns the exact column key that was provided
     * to the constructor.
     */
    @Test
    public void getColumnKey_shouldReturnColumnKeyProvidedInConstructor() {
        // Arrange: Set up the necessary objects for the test.
        // We use simple, clear types (Rectangle, String) to make the test's intent obvious.
        Rectangle area = new Rectangle(10, 20, 30, 40);
        CategoryDataset<String, String> dataset = new DefaultMultiValueCategoryDataset<>();
        String rowKey = "TestRowKey";
        String expectedColumnKey = "TestColumnKey";

        CategoryItemEntity<String, String> entity = new CategoryItemEntity<>(
                area,
                "Test ToolTip",
                "http://www.jfree.org/jfreechart/",
                dataset,
                rowKey,
                expectedColumnKey
        );

        // Act: Call the method under test.
        String actualColumnKey = entity.getColumnKey();

        // Assert: Verify the outcome.
        // We check that the returned key is the same instance as the one we passed in.
        assertSame("The column key should be the same object instance provided to the constructor.",
                expectedColumnKey, actualColumnKey);
        assertEquals("The column key should be equal to the value provided to the constructor.",
                expectedColumnKey, actualColumnKey);
    }
}