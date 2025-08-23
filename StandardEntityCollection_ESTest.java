package org.jfree.chart.entity;

import static org.junit.Assert.*;

import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.util.Collection;
import java.util.Iterator;

import org.junit.Test;

/**
 * Readable, behavior-focused tests for StandardEntityCollection.
 * 
 * These tests cover:
 * - basic collection operations (add, addAll, clear, iterator)
 * - lookups by index and by point
 * - error handling for invalid inputs
 * - equality, hashCode, and cloning behavior
 * - unmodifiable view returned by getEntities()
 */
public class StandardEntityCollectionTest {

    // Helpers
    private static ChartEntity rectEntity(int x, int y, int w, int h, String id) {
        return new ChartEntity(new Rectangle(x, y, w, h), "tooltip-" + id, "url-" + id);
    }

    @Test
    public void initialCollectionIsEmpty() {
        StandardEntityCollection c = new StandardEntityCollection();

        assertEquals(0, c.getEntityCount());
        assertNotNull(c.iterator());
        assertFalse(c.iterator().hasNext());
        assertNotNull(c.getEntities());
        assertTrue(c.getEntities().isEmpty());
    }

    @Test
    public void add_increasesCount_andAllowsIndexAndPointLookup() {
        StandardEntityCollection c = new StandardEntityCollection();
        ChartEntity e = rectEntity(0, 0, 10, 10, "A");

        c.add(e);

        assertEquals(1, c.getEntityCount());
        assertSame(e, c.getEntity(0));
        // Point (5,5) is inside the rectangle
        assertSame(e, c.getEntity(5.0, 5.0));
    }

    @Test
    public void getEntity_byPoint_returnsLastMatchingEntityWhenOverlapping() {
        StandardEntityCollection c = new StandardEntityCollection();
        ChartEntity first = rectEntity(0, 0, 10, 10, "first");
        ChartEntity second = rectEntity(0, 0, 10, 10, "second"); // overlaps first completely

        c.add(first);
        c.add(second);

        // Both rectangles contain (1,1); the collection should return the last added (second).
        assertSame(second, c.getEntity(1.0, 1.0));
    }

    @Test
    public void getEntity_byPoint_returnsNullWhenNoEntityContainsPoint() {
        StandardEntityCollection c = new StandardEntityCollection();
        ChartEntity e = rectEntity(0, 0, 10, 10, "A");
        c.add(e);

        assertNull(c.getEntity(100.0, 100.0)); // clearly outside the rectangle
    }

    @Test
    public void getEntity_byPoint_ignoresNonAreaShapes() {
        StandardEntityCollection c = new StandardEntityCollection();
        // A line has no area, so it should not be matched by point lookup
        ChartEntity lineEntity = new ChartEntity(new Line2D.Double(0, 0, 10, 10));
        c.add(lineEntity);

        assertNull(c.getEntity(0.0, 0.0));
    }

    @Test
    public void add_null_throwsIllegalArgumentException() {
        StandardEntityCollection c = new StandardEntityCollection();

        assertThrows(IllegalArgumentException.class, () -> c.add(null));
    }

    @Test
    public void addAll_null_throwsNullPointerException() {
        StandardEntityCollection c = new StandardEntityCollection();

        assertThrows(NullPointerException.class, () -> c.addAll(null));
    }

    @Test
    public void addAll_appendsEntitiesFromAnotherCollection() {
        StandardEntityCollection source = new StandardEntityCollection();
        ChartEntity e1 = rectEntity(0, 0, 5, 5, "source1");
        ChartEntity e2 = rectEntity(10, 10, 5, 5, "source2");
        source.add(e1);
        source.add(e2);

        StandardEntityCollection target = new StandardEntityCollection();
        ChartEntity e0 = rectEntity(-10, -10, 5, 5, "target0");
        target.add(e0);

        target.addAll(source);

        assertEquals(3, target.getEntityCount());
        assertSame(e0, target.getEntity(0));
        assertSame(e1, target.getEntity(1));
        assertSame(e2, target.getEntity(2));
    }

    @Test
    public void getEntities_returnsUnmodifiableView() {
        StandardEntityCollection c = new StandardEntityCollection();
        ChartEntity e = rectEntity(0, 0, 10, 10, "A");
        c.add(e);

        Collection<ChartEntity> view = c.getEntities();
        assertEquals(1, view.size());
        assertTrue(view.contains(e));
        assertThrows(UnsupportedOperationException.class, () -> view.add(rectEntity(1, 1, 1, 1, "B")));
    }

    @Test
    public void getEntity_byIndex_outOfBounds_throws() {
        StandardEntityCollection c = new StandardEntityCollection();

        assertThrows(IndexOutOfBoundsException.class, () -> c.getEntity(0));
    }

    @Test
    public void clear_removesAllEntities() {
        StandardEntityCollection c = new StandardEntityCollection();
        c.add(rectEntity(0, 0, 10, 10, "A"));

        c.clear();

        assertEquals(0, c.getEntityCount());
        assertTrue(c.getEntities().isEmpty());
        assertFalse(c.iterator().hasNext());
    }

    @Test
    public void iterator_returnsEntitiesInInsertionOrder() {
        StandardEntityCollection c = new StandardEntityCollection();
        ChartEntity e1 = rectEntity(0, 0, 10, 10, "1");
        ChartEntity e2 = rectEntity(1, 1, 10, 10, "2");
        c.add(e1);
        c.add(e2);

        Iterator<ChartEntity> it = c.iterator();
        assertTrue(it.hasNext());
        assertSame(e1, it.next());
        assertTrue(it.hasNext());
        assertSame(e2, it.next());
        assertFalse(it.hasNext());
    }

    @Test
    public void equalsAndHashCode_basicContract() {
        StandardEntityCollection a = new StandardEntityCollection();
        StandardEntityCollection b = new StandardEntityCollection();

        // Reflexive
        assertTrue(a.equals(a));
        // Symmetric on empty collections
        assertTrue(a.equals(b));
        assertTrue(b.equals(a));
        assertEquals(a.hashCode(), b.hashCode());

        // Not equal to null or different type
        assertFalse(a.equals(null));
        assertFalse(a.equals("not-a-collection"));

        // State change breaks equality
        a.add(rectEntity(0, 0, 1, 1, "X"));
        assertFalse(a.equals(b));
    }

    @Test
    public void clone_createsIndependentList() throws Exception {
        StandardEntityCollection original = new StandardEntityCollection();
        ChartEntity e1 = rectEntity(0, 0, 10, 10, "1");
        original.add(e1);

        StandardEntityCollection clone = (StandardEntityCollection) original.clone();

        assertNotSame(original, clone);
        assertEquals(1, clone.getEntityCount());
        assertSame(e1, clone.getEntity(0)); // entities themselves are not deep-cloned

        // Mutating original's list doesn't affect the clone's list
        original.add(rectEntity(1, 1, 5, 5, "2"));
        assertEquals(2, original.getEntityCount());
        assertEquals(1, clone.getEntityCount());
    }
}