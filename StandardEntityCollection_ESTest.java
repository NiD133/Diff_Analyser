package org.jfree.chart.entity;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
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
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, 
                     useVNET = true, resetStaticState = true, separateClassLoader = true) 
public class StandardEntityCollection_ESTest extends StandardEntityCollection_ESTest_scaffolding {

    // Tests for add() method
    @Test(timeout = 4000)
    public void testAddThrowsExceptionForNullEntity() {
        StandardEntityCollection collection = new StandardEntityCollection();
        try {
            collection.add(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Null 'entity' argument.", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testAddEntityIncreasesCount() {
        StandardEntityCollection collection = new StandardEntityCollection();
        Rectangle rect = new Rectangle(new Point(380, 4376));
        ChartEntity entity = new ChartEntity(rect, "TestTooltip", "TestURL");
        collection.add(entity);
        assertEquals(1, collection.getEntityCount());
    }

    // Tests for addAll() method
    @Test(timeout = 4000)
    public void testAddAllThrowsExceptionForNullCollection() {
        StandardEntityCollection collection = new StandardEntityCollection();
        try {
            collection.addAll(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected behavior
        }
    }

    @Test(timeout = 4000)
    public void testAddAllEmptyCollectionDoesNothing() {
        StandardEntityCollection source = new StandardEntityCollection();
        StandardEntityCollection target = new StandardEntityCollection();
        target.addAll(source);
        assertEquals(0, target.getEntityCount());
    }

    // Tests for getEntityCount() method
    @Test(timeout = 4000)
    public void testGetEntityCountInitiallyZero() {
        StandardEntityCollection collection = new StandardEntityCollection();
        assertEquals(0, collection.getEntityCount());
    }

    // Tests for getEntity(int) method
    @Test(timeout = 4000)
    public void testGetEntityByIndexReturnsCorrectEntity() {
        StandardEntityCollection collection = new StandardEntityCollection();
        Rectangle rect = new Rectangle(new Dimension(-1, 0));
        ChartEntity entity = new ChartEntity(rect, "TestTooltip", null);
        collection.add(entity);
        assertSame(entity, collection.getEntity(0));
    }

    @Test(timeout = 4000)
    public void testGetEntityByIndexThrowsForInvalidIndex() {
        StandardEntityCollection collection = new StandardEntityCollection();
        try {
            collection.getEntity(0);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Expected behavior
        }
    }

    // Tests for getEntity(double, double) method
    @Test(timeout = 4000)
    public void testGetEntityByCoordinatesReturnsNullWhenNoMatch() {
        StandardEntityCollection collection = new StandardEntityCollection();
        Rectangle rect = new Rectangle(new Dimension(-1, 0));
        ChartEntity entity = new ChartEntity(rect, "TestTooltip", null);
        collection.add(entity);
        assertNull(collection.getEntity(1.0, 674.58));
    }

    @Test(timeout = 4000)
    public void testGetEntityByCoordinatesReturnsNullForZeroAreaEntity() {
        StandardEntityCollection collection = new StandardEntityCollection();
        Line2D.Double line = new Line2D.Double();
        ChartEntity entity = new ChartEntity(line);
        collection.add(entity);
        collection.add(entity); // Add duplicate
        assertNull(collection.getEntity(0.0, 0.0));
    }

    @Test(timeout = 4000)
    public void testGetEntityByCoordinatesReturnsMatchingEntity() {
        StandardEntityCollection collection = new StandardEntityCollection();
        Rectangle rect = new Rectangle(new Dimension(-1, 0));
        rect.setFrameFromDiagonal(-1.0, -3226.30, -1490.29, 0.0);
        ChartEntity entity = new ChartEntity(rect, "TestTooltip", "TestURL");
        collection.add(entity);
        ChartEntity found = collection.getEntity(-1491.0, -1491.0);
        assertNotNull(found);
        assertEquals("rect", found.getShapeType());
    }

    // Tests for getEntities() method
    @Test(timeout = 4000)
    public void testGetEntitiesReturnsNonNullCollection() {
        StandardEntityCollection collection = new StandardEntityCollection();
        Collection<ChartEntity> entities = collection.getEntities();
        assertNotNull(entities);
    }

    // Tests for iterator() method
    @Test(timeout = 4000)
    public void testIteratorReturnsNonNullIterator() {
        StandardEntityCollection collection = new StandardEntityCollection();
        Iterator<ChartEntity> iterator = collection.iterator();
        assertNotNull(iterator);
    }

    // Tests for clear() method
    @Test(timeout = 4000)
    public void testClearRemovesAllEntities() {
        StandardEntityCollection collection = new StandardEntityCollection();
        collection.clear();
        assertEquals(0, collection.getEntityCount());
    }

    // Tests for clone() method
    @Test(timeout = 4000)
    public void testCloneMaintainsEntityCount() throws Exception {
        StandardEntityCollection collection = new StandardEntityCollection();
        Line2D.Float line = new Line2D.Float(0.0F, 0.0F, 0.0F, 0.0F);
        ChartEntity entity = new ChartEntity(line, "TestTooltip", "TestURL");
        collection.add(entity);
        collection.clone();
        assertEquals(1, collection.getEntityCount());
    }

    // Tests for equals() method
    @Test(timeout = 4000)
    public void testEqualsReturnsTrueForSameObject() {
        StandardEntityCollection collection = new StandardEntityCollection();
        assertTrue(collection.equals(collection));
    }

    @Test(timeout = 4000)
    public void testEqualsReturnsTrueForEqualCollections() {
        StandardEntityCollection coll1 = new StandardEntityCollection();
        StandardEntityCollection coll2 = new StandardEntityCollection();
        assertTrue(coll1.equals(coll2));
    }

    @Test(timeout = 4000)
    public void testEqualsReturnsFalseForDifferentType() {
        StandardEntityCollection collection = new StandardEntityCollection();
        assertFalse(collection.equals(new Object()));
    }

    // Test for hashCode()
    @Test(timeout = 4000)
    public void testHashCodeDoesNotThrowException() {
        StandardEntityCollection collection = new StandardEntityCollection();
        collection.hashCode();
    }
}