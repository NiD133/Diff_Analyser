package org.jfree.chart.entity;

import org.junit.Test;
import static org.junit.Assert.*;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import org.jfree.chart.entity.LegendItemEntity;
import org.jfree.data.general.Dataset;
import org.jfree.data.xy.DefaultTableXYDataset;
import org.jfree.data.xy.XIntervalSeriesCollection;

/**
 * Test suite for LegendItemEntity class.
 * Tests the core functionality including construction, property management, 
 * equality, cloning, and string representation.
 */
public class LegendItemEntity_ESTest {

    // Test data constants
    private static final String SERIES_KEY_1 = "Series1";
    private static final String SERIES_KEY_2 = "Series2";
    private static final Rectangle TEST_AREA = new Rectangle(0, 0, 100, 50);

    // ========== Constructor Tests ==========

    @Test(timeout = 4000)
    public void constructor_WithValidShape_ShouldCreateEntity() {
        // Given
        Rectangle2D area = new Rectangle2D.Double(10, 20, 100, 50);
        
        // When
        LegendItemEntity<String> entity = new LegendItemEntity<>(area);
        
        // Then
        assertNotNull("Entity should be created", entity);
        assertNull("Series key should be null by default", entity.getSeriesKey());
        assertNull("Dataset should be null by default", entity.getDataset());
    }

    @Test(timeout = 4000, expected = IllegalArgumentException.class)
    public void constructor_WithNullShape_ShouldThrowException() {
        // When & Then
        new LegendItemEntity<String>((Shape) null);
    }

    // ========== Series Key Management Tests ==========

    @Test(timeout = 4000)
    public void setSeriesKey_WithValidKey_ShouldStoreAndRetrieveKey() {
        // Given
        LegendItemEntity<String> entity = createTestEntity();
        
        // When
        entity.setSeriesKey(SERIES_KEY_1);
        
        // Then
        assertEquals("Should return the same series key", SERIES_KEY_1, entity.getSeriesKey());
    }

    @Test(timeout = 4000)
    public void getSeriesKey_WhenNotSet_ShouldReturnNull() {
        // Given
        LegendItemEntity<String> entity = createTestEntity();
        
        // When & Then
        assertNull("Series key should be null when not set", entity.getSeriesKey());
    }

    // ========== Dataset Management Tests ==========

    @Test(timeout = 4000)
    public void setDataset_WithValidDataset_ShouldStoreAndRetrieveDataset() {
        // Given
        LegendItemEntity<String> entity = createTestEntity();
        DefaultTableXYDataset<String> dataset = new DefaultTableXYDataset<>(false);
        
        // When
        entity.setDataset(dataset);
        
        // Then
        assertSame("Should return the same dataset instance", dataset, entity.getDataset());
        assertTrue("Dataset should have notify enabled", 
                  ((DefaultTableXYDataset) entity.getDataset()).getNotify());
    }

    @Test(timeout = 4000)
    public void getDataset_WhenNotSet_ShouldReturnNull() {
        // Given
        LegendItemEntity<String> entity = createTestEntity();
        
        // When & Then
        assertNull("Dataset should be null when not set", entity.getDataset());
    }

    // ========== Equality Tests ==========

    @Test(timeout = 4000)
    public void equals_WithSameInstance_ShouldReturnTrue() {
        // Given
        LegendItemEntity<String> entity = createTestEntity();
        
        // When & Then
        assertTrue("Entity should equal itself", entity.equals(entity));
    }

    @Test(timeout = 4000)
    public void equals_WithClonedEntity_ShouldReturnTrue() throws CloneNotSupportedException {
        // Given
        LegendItemEntity<String> original = createTestEntity();
        LegendItemEntity<String> clone = (LegendItemEntity<String>) original.clone();
        
        // When & Then
        assertTrue("Entity should equal its clone", original.equals(clone));
    }

    @Test(timeout = 4000)
    public void equals_WithDifferentSeriesKey_ShouldReturnFalse() {
        // Given
        LegendItemEntity<String> entity1 = createTestEntity();
        LegendItemEntity<String> entity2 = createTestEntity();
        entity1.setSeriesKey(SERIES_KEY_1);
        entity2.setSeriesKey(SERIES_KEY_2);
        
        // When & Then
        assertFalse("Entities with different series keys should not be equal", 
                   entity1.equals(entity2));
    }

    @Test(timeout = 4000)
    public void equals_WithDifferentDataset_ShouldReturnFalse() throws CloneNotSupportedException {
        // Given
        LegendItemEntity<String> original = createTestEntity();
        LegendItemEntity<String> clone = (LegendItemEntity<String>) original.clone();
        XIntervalSeriesCollection<String> dataset = new XIntervalSeriesCollection<>();
        original.setDataset(dataset);
        
        // When & Then
        assertFalse("Entities with different datasets should not be equal", 
                   original.equals(clone));
    }

    @Test(timeout = 4000)
    public void equals_WithDifferentObjectType_ShouldReturnFalse() {
        // Given
        LegendItemEntity<String> entity = createTestEntity();
        Object otherObject = new Object();
        
        // When & Then
        assertFalse("Entity should not equal object of different type", 
                   entity.equals(otherObject));
    }

    // ========== String Representation Tests ==========

    @Test(timeout = 4000)
    public void toString_WithDefaultValues_ShouldShowNullValues() {
        // Given
        LegendItemEntity<String> entity = createTestEntity();
        
        // When
        String result = entity.toString();
        
        // Then
        assertEquals("Should show null values for unset properties",
                    "LegendItemEntity: seriesKey=null, dataset=null", result);
    }

    // ========== Helper Methods ==========

    /**
     * Creates a test entity with a standard test area for consistent testing.
     */
    private LegendItemEntity<String> createTestEntity() {
        return new LegendItemEntity<>(TEST_AREA);
    }
}