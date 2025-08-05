package org.jfree.chart.entity;

import org.jfree.data.general.Dataset;
import org.jfree.data.xy.DefaultTableXYDataset;
import org.junit.Test;

import java.awt.Rectangle;

import static org.junit.Assert.*;

/**
 * A set of unit tests for the {@link LegendItemEntity} class.
 */
public class LegendItemEntityTest {

    private static final Rectangle DEFAULT_AREA = new Rectangle(10, 20, 30, 40);

    /**
     * Verifies that the constructor throws an IllegalArgumentException when the
     * provided shape area is null.
     */
    @Test(expected = IllegalArgumentException.class)
    public void constructor_withNullArea_throwsIllegalArgumentException() {
        new LegendItemEntity<>(null);
    }

    /**
     * Tests that a newly instantiated LegendItemEntity has null for its
     * dataset and series key.
     */
    @Test
    public void newLegendItemEntity_hasNullDatasetAndSeriesKey() {
        // Arrange
        LegendItemEntity<String> entity = new LegendItemEntity<>(DEFAULT_AREA);

        // Assert
        assertNull("A new entity should have a null dataset.", entity.getDataset());
        assertNull("A new entity should have a null series key.", entity.getSeriesKey());
    }

    /**
     * Verifies that the setDataset() and getDataset() methods work correctly.
     */
    @Test
    public void setAndGetDataset_shouldStoreAndReturnTheDataset() {
        // Arrange
        LegendItemEntity<String> entity = new LegendItemEntity<>(DEFAULT_AREA);
        Dataset dataset = new DefaultTableXYDataset();

        // Act
        entity.setDataset(dataset);

        // Assert
        assertSame("The returned dataset should be the same instance that was set.", dataset, entity.getDataset());
    }

    /**
     * Verifies that the setSeriesKey() and getSeriesKey() methods work correctly.
     */
    @Test
    public void setAndGetSeriesKey_shouldStoreAndReturnTheKey() {
        // Arrange
        LegendItemEntity<String> entity = new LegendItemEntity<>(DEFAULT_AREA);
        String seriesKey = "Test Series Key";

        // Act
        entity.setSeriesKey(seriesKey);

        // Assert
        assertSame("The returned series key should be the same instance that was set.", seriesKey, entity.getSeriesKey());
    }

    /**
     * Tests the equals() method for reflexivity: an object must equal itself.
     */
    @Test
    public void equals_withSameInstance_returnsTrue() {
        // Arrange
        LegendItemEntity<String> entity = new LegendItemEntity<>(DEFAULT_AREA);

        // Act & Assert
        assertTrue("An entity must be equal to itself.", entity.equals(entity));
    }

    /**
     * Tests that two distinct instances with identical properties are considered equal.
     */
    @Test
    public void equals_withIdenticalInstances_returnsTrue() {
        // Arrange
        Dataset dataset = new DefaultTableXYDataset();
        LegendItemEntity<String> entity1 = new LegendItemEntity<>(DEFAULT_AREA);
        entity1.setDataset(dataset);
        entity1.setSeriesKey("Key1");

        LegendItemEntity<String> entity2 = new LegendItemEntity<>(DEFAULT_AREA);
        entity2.setDataset(dataset);
        entity2.setSeriesKey("Key1");

        // Act & Assert
        assertTrue("Entities with the same properties should be equal.", entity1.equals(entity2));
    }

    /**
     * Tests that an entity is not equal to an object of a different type.
     */
    @Test
    public void equals_withDifferentObjectType_returnsFalse() {
        // Arrange
        LegendItemEntity<String> entity = new LegendItemEntity<>(DEFAULT_AREA);

        // Act & Assert
        assertFalse("An entity should not be equal to an object of a different type.", entity.equals("Not an entity"));
    }

    /**
     * Tests that two entities with different series keys are not equal.
     */
    @Test
    public void equals_withDifferentSeriesKey_returnsFalse() {
        // Arrange
        LegendItemEntity<String> entity1 = new LegendItemEntity<>(DEFAULT_AREA);
        entity1.setSeriesKey("Key A");

        LegendItemEntity<String> entity2 = new LegendItemEntity<>(DEFAULT_AREA);
        entity2.setSeriesKey("Key B");

        // Act & Assert
        assertFalse("Entities with different series keys should not be equal.", entity1.equals(entity2));
    }

    /**
     * Tests that two entities are not equal if one has a null series key.
     */
    @Test
    public void equals_withOneNullSeriesKey_returnsFalse() {
        // Arrange
        LegendItemEntity<String> entity1 = new LegendItemEntity<>(DEFAULT_AREA);
        entity1.setSeriesKey("Key A");

        LegendItemEntity<String> entity2 = new LegendItemEntity<>(DEFAULT_AREA); // seriesKey is null

        // Act & Assert
        assertFalse("An entity with a series key should not equal one with a null key.", entity1.equals(entity2));
        assertFalse("An entity with a null series key should not equal one with a key.", entity2.equals(entity1));
    }

    /**
     * Tests that two entities with different datasets are not equal.
     */
    @Test
    public void equals_withDifferentDataset_returnsFalse() {
        // Arrange
        LegendItemEntity<String> entity1 = new LegendItemEntity<>(DEFAULT_AREA);
        entity1.setDataset(new DefaultTableXYDataset());

        LegendItemEntity<String> entity2 = new LegendItemEntity<>(DEFAULT_AREA);
        entity2.setDataset(new DefaultTableXYDataset()); // A different instance

        // Act & Assert
        assertFalse("Entities with different datasets should not be equal.", entity1.equals(entity2));
    }

    /**
     * Tests that the clone() method creates an independent and equal copy of the entity.
     */
    @Test
    public void clone_createsIndependentAndEqualCopy() throws CloneNotSupportedException {
        // Arrange
        LegendItemEntity<String> original = new LegendItemEntity<>(DEFAULT_AREA);
        original.setSeriesKey("Series 1");
        original.setDataset(new DefaultTableXYDataset());

        // Act
        LegendItemEntity<String> clone = (LegendItemEntity<String>) original.clone();

        // Assert
        assertNotSame("Clone should be a different object instance.", original, clone);
        assertEquals("Clone should be equal to the original object.", original, clone);
    }

    /**
     * Tests that the toString() method produces a meaningful string representation.
     */
    @Test
    public void toString_returnsMeaningfulString() {
        // Arrange
        LegendItemEntity<String> entity = new LegendItemEntity<>(DEFAULT_AREA);
        entity.setSeriesKey("My Series");

        // Act
        String entityString = entity.toString();

        // Assert
        assertTrue("toString() should contain the series key.", entityString.contains("seriesKey=My Series"));
        assertTrue("toString() should indicate a null dataset.", entityString.contains("dataset=null"));
    }
}