package org.jfree.chart.entity;

import org.jfree.data.category.CategoryDataset;
import org.jfree.data.statistics.DefaultMultiValueCategoryDataset;
import org.junit.Test;

import java.awt.Rectangle;
import java.awt.Shape;

import static org.junit.Assert.assertNull;

/**
 * Unit tests for the {@link CategoryItemEntity} class.
 *
 * Note: The original test class name 'CategoryItemEntity_ESTestTest3' and its scaffolding
 * are artifacts from the EvoSuite test generation tool. In a real-world scenario,
 * this would be part of a single, more comprehensive 'CategoryItemEntityTest' class.
 */
public class CategoryItemEntity_ESTestTest3 extends CategoryItemEntity_ESTest_scaffolding {

    /**
     * Verifies that the column key can be set to null and is retrieved as such.
     * This test checks the actual behavior of the setter, which allows a null value
     * even though the method's Javadoc documentation suggests it is not permitted.
     */
    @Test
    public void getColumnKey_afterSettingToNull_returnsNull() {
        // Arrange
        Shape testArea = new Rectangle();
        CategoryDataset<Integer, Integer> testDataset = new DefaultMultiValueCategoryDataset<>();
        CategoryItemEntity<Integer, Integer> entity = new CategoryItemEntity<>(
                testArea, "Test Tooltip", "Test URL", testDataset, 1, 2);

        // Act
        entity.setColumnKey(null);
        Integer actualColumnKey = entity.getColumnKey();

        // Assert
        assertNull("The column key should be null after being explicitly set to null.", actualColumnKey);
    }
}