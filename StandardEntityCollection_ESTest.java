package org.jfree.chart.entity;

import org.junit.Test;
import static org.junit.Assert.*;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.util.Collection;
import java.util.Iterator;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.entity.EntityCollection;
import org.jfree.chart.entity.StandardEntityCollection;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(
    mockJVMNonDeterminism = true, 
    useVFS = true, 
    useVNET = true, 
    resetStaticState = true, 
    separateClassLoader = true
)
public class StandardEntityCollectionTest extends StandardEntityCollection_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testGetEntityByCoordinatesReturnsNullWhenNoEntityMatches() {
        StandardEntityCollection collection = new StandardEntityCollection();
        Dimension dimension = new Dimension(-1, 0);
        Rectangle rectangle = new Rectangle(dimension);
        ChartEntity entity = new ChartEntity(rectangle, ">", null);
        collection.add(entity);

        // Attempt to retrieve an entity at coordinates where no entity exists
        ChartEntity result = collection.getEntity(1.0, 674.587836228879);
        assertNull(result);
    }

    @Test(timeout = 4000)
    public void testGetEntityByCoordinatesReturnsNullWhenEntityDoesNotEnclosePoint() {
        StandardEntityCollection collection = new StandardEntityCollection();
        Line2D.Double line = new Line2D.Double();
        ChartEntity entity = new ChartEntity(line);
        collection.add(entity);
        collection.add(entity);

        // Attempt to retrieve an entity at coordinates where no entity encloses the point
        ChartEntity result = collection.getEntity(0.0, 0.0);
        assertNull(result);
    }

    @Test(timeout = 4000)
    public void testGetEntityCountReturnsCorrectCount() {
        StandardEntityCollection collection = new StandardEntityCollection();
        Point point = new Point(380, 4376);
        Rectangle rectangle = new Rectangle(point);
        ChartEntity entity = new ChartEntity(rectangle, "E',gl8z/3O[2/[", "E',gl8z/3O[2/[");
        collection.add(entity);

        // Verify that the entity count is correct
        int count = collection.getEntityCount();
        assertEquals(1, count);
    }

    @Test(timeout = 4000)
    public void testGetEntityByIndexReturnsCorrectEntity() {
        StandardEntityCollection collection = new StandardEntityCollection();
        Dimension dimension = new Dimension(-1, 0);
        Rectangle rectangle = new Rectangle(dimension);
        ChartEntity entity = new ChartEntity(rectangle, ">", null);
        collection.add(entity);

        // Retrieve the entity by index and verify it's the same as added
        ChartEntity result = collection.getEntity(0);
        assertSame(result, entity);
    }

    @Test(timeout = 4000)
    public void testAddAllThrowsNullPointerExceptionWhenCollectionIsNull() {
        StandardEntityCollection collection = new StandardEntityCollection();

        // Attempt to add all entities from a null collection
        try {
            collection.addAll(null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testAddThrowsIllegalArgumentExceptionWhenEntityIsNull() {
        StandardEntityCollection collection = new StandardEntityCollection();

        // Attempt to add a null entity
        try {
            collection.add(null);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testGetEntitiesReturnsNonNullCollection() {
        StandardEntityCollection collection = new StandardEntityCollection();

        // Verify that the collection of entities is not null
        Collection<ChartEntity> entities = collection.getEntities();
        assertNotNull(entities);
    }

    @Test(timeout = 4000)
    public void testCloneDoesNotAffectOriginalCollection() {
        StandardEntityCollection collection = new StandardEntityCollection();
        Line2D.Float line = new Line2D.Float(0.0F, 0.0F, 0.0F, 0.0F);
        ChartEntity entity = new ChartEntity(line, " -ZHr,Q8I`#,X{EAHK", " -ZHr,Q8I`#,X{EAHK");
        collection.add(entity);

        // Clone the collection and verify the original count remains unchanged
        collection.clone();
        assertEquals(1, collection.getEntityCount());
    }

    @Test(timeout = 4000)
    public void testEqualsReturnsTrueForIdenticalCollections() {
        StandardEntityCollection collection1 = new StandardEntityCollection();
        StandardEntityCollection collection2 = new StandardEntityCollection();

        // Verify that two identical collections are equal
        assertTrue(collection1.equals(collection2));
    }

    @Test(timeout = 4000)
    public void testEqualsReturnsTrueForSameInstance() {
        StandardEntityCollection collection = new StandardEntityCollection();

        // Verify that a collection is equal to itself
        assertTrue(collection.equals(collection));
    }

    @Test(timeout = 4000)
    public void testEqualsReturnsFalseForDifferentObjectTypes() {
        StandardEntityCollection collection = new StandardEntityCollection();
        Object other = new Object();

        // Verify that a collection is not equal to an object of a different type
        assertFalse(collection.equals(other));
    }

    @Test(timeout = 4000)
    public void testGetEntityByCoordinatesReturnsCorrectEntity() {
        StandardEntityCollection collection = new StandardEntityCollection();
        Dimension dimension = new Dimension(-1, 0);
        Rectangle rectangle = new Rectangle(dimension);
        rectangle.setFrameFromDiagonal(-1, -3226.3016161567016, -1490.292, 0);
        ChartEntity entity = new ChartEntity(rectangle, ">", ">");
        collection.add(entity);

        // Retrieve the entity by coordinates and verify its shape type
        ChartEntity result = collection.getEntity(-1491, -1491);
        assertEquals("rect", result.getShapeType());
    }

    @Test(timeout = 4000)
    public void testIteratorReturnsNonNullIterator() {
        StandardEntityCollection collection = new StandardEntityCollection();

        // Verify that the iterator is not null
        Iterator<ChartEntity> iterator = collection.iterator();
        assertNotNull(iterator);
    }

    @Test(timeout = 4000)
    public void testGetEntityCountReturnsZeroForEmptyCollection() {
        StandardEntityCollection collection = new StandardEntityCollection();

        // Verify that the entity count is zero for an empty collection
        int count = collection.getEntityCount();
        assertEquals(0, count);
    }

    @Test(timeout = 4000)
    public void testAddAllWithSelfDoesNotDuplicateEntities() {
        StandardEntityCollection collection = new StandardEntityCollection();

        // Add all entities from the collection to itself and verify no duplication
        collection.addAll(collection);
        assertEquals(0, collection.getEntityCount());
    }

    @Test(timeout = 4000)
    public void testGetEntityByIndexThrowsIndexOutOfBoundsExceptionForEmptyCollection() {
        StandardEntityCollection collection = new StandardEntityCollection();

        // Attempt to retrieve an entity by index from an empty collection
        try {
            collection.getEntity(0);
            fail("Expecting exception: IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testClearEmptiesTheCollection() {
        StandardEntityCollection collection = new StandardEntityCollection();

        // Clear the collection and verify the entity count is zero
        collection.clear();
        assertEquals(0, collection.getEntityCount());
    }

    @Test(timeout = 4000)
    public void testHashCodeCanBeCalled() {
        StandardEntityCollection collection = new StandardEntityCollection();

        // Verify that hashCode can be called without exceptions
        collection.hashCode();
    }
}