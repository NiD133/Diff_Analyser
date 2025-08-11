package org.jfree.chart.entity;

import org.junit.Test;
import static org.junit.Assert.*;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import javax.swing.text.DefaultCaret;
import org.jfree.chart.entity.CategoryItemEntity;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.statistics.DefaultBoxAndWhiskerCategoryDataset;
import org.jfree.data.statistics.DefaultMultiValueCategoryDataset;
import org.jfree.data.statistics.DefaultStatisticalCategoryDataset;

/**
 * Test suite for CategoryItemEntity class functionality.
 * Tests cover constructor validation, getter/setter operations, 
 * equals/hashCode behavior, and cloning.
 */
public class CategoryItemEntityTest {

    // Test data constants
    private static final String TOOLTIP_TEXT = "Sample tooltip";
    private static final String URL_TEXT = "http://example.com";
    private static final Integer ROW_KEY = 1;
    private static final Integer COLUMN_KEY = 2;

    // Helper method to create a basic dataset
    private DefaultMultiValueCategoryDataset<Integer, Integer> createTestDataset() {
        return new DefaultMultiValueCategoryDataset<Integer, Integer>();
    }

    // Helper method to create a basic entity
    private CategoryItemEntity<Integer, Integer> createTestEntity() {
        Rectangle area = new Rectangle(0, 0, 10, 10);
        return new CategoryItemEntity<Integer, Integer>(
            area, TOOLTIP_TEXT, URL_TEXT, createTestDataset(), ROW_KEY, COLUMN_KEY);
    }

    @Test
    public void testConstructor_WithValidParameters_ShouldCreateEntity() {
        // Given
        Rectangle area = new Rectangle(0, 0, 10, 10);
        DefaultMultiValueCategoryDataset<Integer, Integer> dataset = createTestDataset();
        
        // When
        CategoryItemEntity<Integer, Integer> entity = new CategoryItemEntity<Integer, Integer>(
            area, TOOLTIP_TEXT, URL_TEXT, dataset, ROW_KEY, COLUMN_KEY);
        
        // Then
        assertNotNull("Entity should be created", entity);
        assertEquals("Row key should match", ROW_KEY, entity.getRowKey());
        assertEquals("Column key should match", COLUMN_KEY, entity.getColumnKey());
        assertSame("Dataset should match", dataset, entity.getDataset());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_WithNullDataset_ShouldThrowException() {
        // Given
        Rectangle area = new Rectangle(0, 0, 10, 10);
        
        // When & Then
        new CategoryItemEntity<Integer, Integer>(
            area, TOOLTIP_TEXT, URL_TEXT, null, ROW_KEY, COLUMN_KEY);
    }

    @Test
    public void testSetRowKey_WithNullValue_ShouldAllowNull() {
        // Given
        CategoryItemEntity<Integer, Integer> entity = createTestEntity();
        
        // When
        entity.setRowKey(null);
        
        // Then
        assertNull("Row key should be null", entity.getRowKey());
    }

    @Test
    public void testGetRowKey_AfterSettingValue_ShouldReturnCorrectValue() {
        // Given
        CategoryItemEntity<Integer, Integer> entity = createTestEntity();
        Integer newRowKey = 99;
        
        // When
        entity.setRowKey(newRowKey);
        
        // Then
        assertEquals("Row key should match set value", newRowKey, entity.getRowKey());
    }

    @Test
    public void testSetColumnKey_WithNullValue_ShouldAllowNull() {
        // Given
        CategoryItemEntity<Integer, Integer> entity = createTestEntity();
        
        // When
        entity.setColumnKey(null);
        
        // Then
        assertNull("Column key should be null", entity.getColumnKey());
    }

    @Test
    public void testGetColumnKey_AfterSettingValue_ShouldReturnCorrectValue() {
        // Given
        CategoryItemEntity<Integer, Integer> entity = createTestEntity();
        Integer newColumnKey = 88;
        
        // When
        entity.setColumnKey(newColumnKey);
        
        // Then
        assertEquals("Column key should match set value", newColumnKey, entity.getColumnKey());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetDataset_WithNullValue_ShouldThrowException() {
        // Given
        CategoryItemEntity<Integer, Integer> entity = createTestEntity();
        
        // When & Then
        entity.setDataset(null);
    }

    @Test
    public void testSetDataset_WithValidDataset_ShouldUpdateDataset() {
        // Given
        CategoryItemEntity<Integer, Integer> entity = createTestEntity();
        DefaultBoxAndWhiskerCategoryDataset<Integer, Integer> newDataset = 
            new DefaultBoxAndWhiskerCategoryDataset<Integer, Integer>();
        
        // When
        entity.setDataset(newDataset);
        
        // Then
        assertSame("Dataset should be updated", newDataset, entity.getDataset());
    }

    @Test
    public void testEquals_WithSameInstance_ShouldReturnTrue() {
        // Given
        CategoryItemEntity<Integer, Integer> entity = createTestEntity();
        
        // When & Then
        assertTrue("Entity should equal itself", entity.equals(entity));
    }

    @Test
    public void testEquals_WithDifferentClass_ShouldReturnFalse() {
        // Given
        CategoryItemEntity<Integer, Integer> entity = createTestEntity();
        String differentObject = "not an entity";
        
        // When & Then
        assertFalse("Entity should not equal different class", entity.equals(differentObject));
    }

    @Test
    public void testEquals_WithDifferentRowKey_ShouldReturnFalse() {
        // Given
        Rectangle area = new Rectangle(0, 0, 10, 10);
        DefaultMultiValueCategoryDataset<Integer, Integer> dataset = createTestDataset();
        
        CategoryItemEntity<Integer, Integer> entity1 = new CategoryItemEntity<Integer, Integer>(
            area, TOOLTIP_TEXT, URL_TEXT, dataset, 1, COLUMN_KEY);
        CategoryItemEntity<Integer, Integer> entity2 = new CategoryItemEntity<Integer, Integer>(
            area, TOOLTIP_TEXT, URL_TEXT, dataset, 2, COLUMN_KEY);
        
        // When & Then
        assertFalse("Entities with different row keys should not be equal", entity1.equals(entity2));
    }

    @Test
    public void testEquals_WithDifferentColumnKey_ShouldReturnFalse() {
        // Given
        Rectangle area = new Rectangle(0, 0, 10, 10);
        DefaultMultiValueCategoryDataset<Integer, Integer> dataset = createTestDataset();
        
        CategoryItemEntity<Integer, Integer> entity1 = new CategoryItemEntity<Integer, Integer>(
            area, TOOLTIP_TEXT, URL_TEXT, dataset, ROW_KEY, 1);
        CategoryItemEntity<Integer, Integer> entity2 = new CategoryItemEntity<Integer, Integer>(
            area, TOOLTIP_TEXT, URL_TEXT, dataset, ROW_KEY, 2);
        
        // When & Then
        assertFalse("Entities with different column keys should not be equal", entity1.equals(entity2));
    }

    @Test
    public void testEquals_WithDifferentDataset_ShouldReturnFalse() {
        // Given
        Rectangle area = new Rectangle(0, 0, 10, 10);
        DefaultMultiValueCategoryDataset<Integer, Integer> dataset1 = createTestDataset();
        DefaultBoxAndWhiskerCategoryDataset<Integer, Integer> dataset2 = 
            new DefaultBoxAndWhiskerCategoryDataset<Integer, Integer>();
        
        CategoryItemEntity<Integer, Integer> entity1 = new CategoryItemEntity<Integer, Integer>(
            area, TOOLTIP_TEXT, URL_TEXT, dataset1, ROW_KEY, COLUMN_KEY);
        CategoryItemEntity<Integer, Integer> entity2 = new CategoryItemEntity<Integer, Integer>(
            area, TOOLTIP_TEXT, URL_TEXT, dataset2, ROW_KEY, COLUMN_KEY);
        
        // When & Then
        assertFalse("Entities with different datasets should not be equal", entity1.equals(entity2));
    }

    @Test
    public void testEquals_WithEquivalentEntities_ShouldReturnTrue() {
        // Given
        Rectangle area = new Rectangle(0, 0, 10, 10);
        DefaultMultiValueCategoryDataset<Integer, Integer> dataset = createTestDataset();
        
        CategoryItemEntity<Integer, Integer> entity1 = new CategoryItemEntity<Integer, Integer>(
            area, TOOLTIP_TEXT, URL_TEXT, dataset, ROW_KEY, COLUMN_KEY);
        CategoryItemEntity<Integer, Integer> entity2 = new CategoryItemEntity<Integer, Integer>(
            area, TOOLTIP_TEXT, URL_TEXT, dataset, ROW_KEY, COLUMN_KEY);
        
        // When & Then
        assertTrue("Equivalent entities should be equal", entity1.equals(entity2));
    }

    @Test
    public void testToString_ShouldReturnNonNullString() {
        // Given
        CategoryItemEntity<Integer, Integer> entity = createTestEntity();
        
        // When
        String result = entity.toString();
        
        // Then
        assertNotNull("toString should return non-null string", result);
        assertFalse("toString should return non-empty string", result.isEmpty());
    }

    @Test
    public void testClone_ShouldCreateEqualButSeparateInstance() throws CloneNotSupportedException {
        // Given
        CategoryItemEntity<Integer, Integer> original = createTestEntity();
        
        // When
        Object cloned = original.clone();
        
        // Then
        assertNotNull("Cloned object should not be null", cloned);
        assertTrue("Cloned object should be instance of CategoryItemEntity", 
                  cloned instanceof CategoryItemEntity);
        assertNotSame("Cloned object should be different instance", original, cloned);
        assertEquals("Cloned object should be equal to original", original, cloned);
    }
}