package org.jfree.chart.entity;

import org.junit.Test;
import static org.junit.Assert.*;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.util.Collection;
import java.util.Iterator;
import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.entity.EntityCollection;
import org.jfree.chart.entity.StandardEntityCollection;

/**
 * Test suite for StandardEntityCollection class.
 * Tests basic functionality including adding entities, retrieving entities,
 * and collection operations.
 */
public class StandardEntityCollectionTest {

    @Test
    public void testGetEntityByCoordinates_WhenEntityDoesNotContainPoint_ReturnsNull() {
        // Given: A collection with an entity that has a negative width rectangle
        StandardEntityCollection collection = new StandardEntityCollection();
        Rectangle invalidRectangle = new Rectangle(new Dimension(-1, 0)); // negative width
        ChartEntity entity = new ChartEntity(invalidRectangle, "tooltip", null);
        collection.add(entity);
        
        // When: Searching for an entity at coordinates outside the rectangle
        ChartEntity foundEntity = collection.getEntity(1.0, 674.587836228879);
        
        // Then: No entity should be found
        assertNull("Should return null when no entity contains the specified coordinates", foundEntity);
    }

    @Test
    public void testGetEntityByCoordinates_WhenPointIsOnLineEdge_ReturnsNull() {
        // Given: A collection with a line entity (zero-width shape)
        StandardEntityCollection collection = new StandardEntityCollection();
        Line2D.Double line = new Line2D.Double(); // line from (0,0) to (0,0)
        ChartEntity lineEntity = new ChartEntity(line);
        collection.add(lineEntity);
        collection.add(lineEntity); // add same entity twice
        
        // When: Searching at the line coordinates
        ChartEntity foundEntity = collection.getEntity(0.0, 0.0);
        
        // Then: Should return null (lines typically don't contain points in hit testing)
        assertNull("Should return null for line entities at edge coordinates", foundEntity);
    }

    @Test
    public void testAddEntity_IncreasesEntityCount() {
        // Given: An empty collection
        StandardEntityCollection collection = new StandardEntityCollection();
        Rectangle rectangle = new Rectangle(new Point(380, 4376));
        ChartEntity entity = new ChartEntity(rectangle, "tooltip", "url");
        
        // When: Adding an entity
        collection.add(entity);
        
        // Then: Entity count should be 1
        assertEquals("Entity count should increase after adding an entity", 1, collection.getEntityCount());
    }

    @Test
    public void testGetEntityByIndex_ReturnsCorrectEntity() {
        // Given: A collection with one entity
        StandardEntityCollection collection = new StandardEntityCollection();
        Rectangle rectangle = new Rectangle(new Dimension(-1, 0));
        ChartEntity expectedEntity = new ChartEntity(rectangle, "tooltip", null);
        collection.add(expectedEntity);
        
        // When: Retrieving entity by index
        ChartEntity actualEntity = collection.getEntity(0);
        
        // Then: Should return the same entity
        assertSame("Should return the exact entity that was added", expectedEntity, actualEntity);
    }

    @Test(expected = NullPointerException.class)
    public void testAddAll_WithNullCollection_ThrowsNullPointerException() {
        // Given: An empty collection
        StandardEntityCollection collection = new StandardEntityCollection();
        
        // When & Then: Adding null collection should throw exception
        collection.addAll(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAdd_WithNullEntity_ThrowsIllegalArgumentException() {
        // Given: An empty collection
        StandardEntityCollection collection = new StandardEntityCollection();
        
        // When & Then: Adding null entity should throw exception
        collection.add(null);
    }

    @Test
    public void testGetEntities_ReturnsNonNullCollection() {
        // Given: An empty collection
        StandardEntityCollection collection = new StandardEntityCollection();
        
        // When: Getting entities collection
        Collection<ChartEntity> entities = collection.getEntities();
        
        // Then: Should return a non-null collection
        assertNotNull("getEntities() should never return null", entities);
    }

    @Test
    public void testClone_WithEntity_CreatesValidCopy() throws CloneNotSupportedException {
        // Given: A collection with one entity
        StandardEntityCollection originalCollection = new StandardEntityCollection();
        Line2D.Float line = new Line2D.Float(0.0F, 0.0F, 0.0F, 0.0F);
        ChartEntity entity = new ChartEntity(line, "tooltip", "url");
        originalCollection.add(entity);
        
        // When: Cloning the collection
        Object clonedObject = originalCollection.clone();
        
        // Then: Original should still have the entity
        assertEquals("Original collection should maintain its entity count after cloning", 
                     1, originalCollection.getEntityCount());
        assertNotNull("Clone operation should succeed", clonedObject);
    }

    @Test
    public void testEquals_WithTwoEmptyCollections_ReturnsTrue() {
        // Given: Two empty collections
        StandardEntityCollection collection1 = new StandardEntityCollection();
        StandardEntityCollection collection2 = new StandardEntityCollection();
        
        // When & Then: They should be equal
        assertTrue("Two empty collections should be equal", collection1.equals(collection2));
    }

    @Test
    public void testEquals_WithSameInstance_ReturnsTrue() {
        // Given: A collection
        StandardEntityCollection collection = new StandardEntityCollection();
        
        // When & Then: Should equal itself
        assertTrue("Collection should equal itself", collection.equals(collection));
    }

    @Test
    public void testEquals_WithDifferentObjectType_ReturnsFalse() {
        // Given: A collection and a different type of object
        StandardEntityCollection collection = new StandardEntityCollection();
        Object differentObject = new Object();
        
        // When & Then: Should not be equal
        assertFalse("Collection should not equal objects of different types", 
                   collection.equals(differentObject));
    }

    @Test
    public void testGetEntityByCoordinates_WhenEntityContainsPoint_ReturnsEntity() {
        // Given: A collection with an entity that has a large rectangle
        StandardEntityCollection collection = new StandardEntityCollection();
        Rectangle rectangle = new Rectangle(new Dimension(-1, 0));
        // Expand rectangle to cover a large area
        rectangle.setFrameFromDiagonal(-1, -3226.3016161567016, -1490.292, 0);
        ChartEntity entity = new ChartEntity(rectangle, "tooltip", "url");
        collection.add(entity);
        
        // When: Searching for an entity at coordinates within the rectangle
        ChartEntity foundEntity = collection.getEntity(-1491, -1491);
        
        // Then: Should find the entity and it should be a rectangle type
        assertNotNull("Should find entity when coordinates are within its bounds", foundEntity);
        assertEquals("Found entity should be of rectangle type", "rect", foundEntity.getShapeType());
    }

    @Test
    public void testIterator_ReturnsValidIterator() {
        // Given: An empty collection
        StandardEntityCollection collection = new StandardEntityCollection();
        
        // When: Getting iterator
        Iterator<ChartEntity> iterator = collection.iterator();
        
        // Then: Should return a valid iterator
        assertNotNull("iterator() should return a non-null iterator", iterator);
    }

    @Test
    public void testGetEntityCount_OnEmptyCollection_ReturnsZero() {
        // Given: An empty collection
        StandardEntityCollection collection = new StandardEntityCollection();
        
        // When & Then: Entity count should be zero
        assertEquals("Empty collection should have zero entities", 0, collection.getEntityCount());
    }

    @Test
    public void testAddAll_WithEmptyCollection_DoesNotChangeCount() {
        // Given: Two empty collections
        StandardEntityCollection collection1 = new StandardEntityCollection();
        StandardEntityCollection collection2 = new StandardEntityCollection();
        
        // When: Adding empty collection to empty collection
        collection1.addAll(collection2);
        
        // Then: Count should remain zero
        assertEquals("Adding empty collection should not change entity count", 
                     0, collection1.getEntityCount());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetEntity_WithInvalidIndex_ThrowsIndexOutOfBoundsException() {
        // Given: An empty collection
        StandardEntityCollection collection = new StandardEntityCollection();
        
        // When & Then: Accessing invalid index should throw exception
        collection.getEntity(0);
    }

    @Test
    public void testClear_OnEmptyCollection_MaintainsZeroCount() {
        // Given: An empty collection
        StandardEntityCollection collection = new StandardEntityCollection();
        
        // When: Clearing the collection
        collection.clear();
        
        // Then: Should still have zero entities
        assertEquals("Clearing empty collection should maintain zero count", 
                     0, collection.getEntityCount());
    }

    @Test
    public void testHashCode_DoesNotThrowException() {
        // Given: An empty collection
        StandardEntityCollection collection = new StandardEntityCollection();
        
        // When & Then: hashCode should execute without throwing exception
        collection.hashCode(); // Should not throw any exception
    }
}