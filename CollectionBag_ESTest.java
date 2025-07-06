package org.apache.commons.collections4.bag;

import org.apache.commons.collections4.Bag;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class CollectionBagTest {

    @Test
    public void testCreateCollectionBag() {
        HashBag<String> hashBag = new HashBag<>();
        Bag<String> collectionBag = CollectionBag.collectionBag(hashBag);
        assertNotNull(collectionBag);
        assertEquals(0, collectionBag.size());
    }

    @Test(expected = NullPointerException.class)
    public void testCreateCollectionBagWithNullBag() {
        CollectionBag.collectionBag(null);
    }

    @Test
    public void testAddOneCopy() {
        HashBag<String> hashBag = new HashBag<>();
        CollectionBag<String> collectionBag = new CollectionBag<>(hashBag);
        assertTrue(collectionBag.add("element"));
        assertEquals(1, collectionBag.getCount("element"));
    }

    @Test
    public void testAddMultipleCopies() {
        HashBag<String> hashBag = new HashBag<>();
        CollectionBag<String> collectionBag = new CollectionBag<>(hashBag);
        assertTrue(collectionBag.add("element", 3));
        assertEquals(3, collectionBag.getCount("element"));
    }

    @Test
    public void testAddAll() {
        HashBag<String> hashBag = new HashBag<>();
        CollectionBag<String> collectionBag = new CollectionBag<>(hashBag);
        List<String> elementsToAdd = Arrays.asList("a", "b", "a", "c");
        assertTrue(collectionBag.addAll(elementsToAdd));
        assertEquals(2, collectionBag.getCount("a"));
        assertEquals(1, collectionBag.getCount("b"));
        assertEquals(1, collectionBag.getCount("c"));
        assertEquals(4, collectionBag.size());
    }

    @Test
    public void testAddAllEmpty() {
        HashBag<String> hashBag = new HashBag<>();
        CollectionBag<String> collectionBag = new CollectionBag<>(hashBag);
        List<String> elementsToAdd = Collections.emptyList();
        assertFalse(collectionBag.addAll(elementsToAdd));
        assertEquals(0, collectionBag.size());
    }

    @Test
    public void testContainsAll() {
        HashBag<String> hashBag = new HashBag<>();
        CollectionBag<String> collectionBag = new CollectionBag<>(hashBag);
        collectionBag.addAll(Arrays.asList("a", "b", "c"));
        List<String> elementsToCheck = Arrays.asList("a", "b");
        assertTrue(collectionBag.containsAll(elementsToCheck));
    }

    @Test
    public void testContainsAllNotPresent() {
        HashBag<String> hashBag = new HashBag<>();
        CollectionBag<String> collectionBag = new CollectionBag<>(hashBag);
        collectionBag.addAll(Arrays.asList("a", "b", "c"));
        List<String> elementsToCheck = Arrays.asList("a", "d");
        assertFalse(collectionBag.containsAll(elementsToCheck));
    }

    @Test
    public void testRemove() {
        HashBag<String> hashBag = new HashBag<>();
        CollectionBag<String> collectionBag = new CollectionBag<>(hashBag);
        collectionBag.addAll(Arrays.asList("a", "b", "c", "a"));
        assertTrue(collectionBag.remove("a"));
        assertEquals(1, collectionBag.getCount("a"));
        assertEquals(3, collectionBag.size());
    }

    @Test
    public void testRemoveNonExisting() {
        HashBag<String> hashBag = new HashBag<>();
        CollectionBag<String> collectionBag = new CollectionBag<>(hashBag);
        collectionBag.addAll(Arrays.asList("a", "b", "c"));
        assertFalse(collectionBag.remove("d"));
        assertEquals(1, collectionBag.getCount("a"));
        assertEquals(1, collectionBag.getCount("b"));
        assertEquals(1, collectionBag.getCount("c"));
        assertEquals(3, collectionBag.size());
    }

    @Test
    public void testRemoveAll() {
        HashBag<String> hashBag = new HashBag<>();
        CollectionBag<String> collectionBag = new CollectionBag<>(hashBag);
        collectionBag.addAll(Arrays.asList("a", "b", "c", "a"));
        List<String> elementsToRemove = Arrays.asList("a", "b");
        assertTrue(collectionBag.removeAll(elementsToRemove));
        assertEquals(0, collectionBag.getCount("a"));
        assertEquals(0, collectionBag.getCount("b"));
        assertEquals(1, collectionBag.getCount("c"));
        assertEquals(1, collectionBag.size());
    }

    @Test
    public void testRemoveAllNonExisting() {
        HashBag<String> hashBag = new HashBag<>();
        CollectionBag<String> collectionBag = new CollectionBag<>(hashBag);
        collectionBag.addAll(Arrays.asList("a", "b", "c"));
        List<String> elementsToRemove = Arrays.asList("d", "e");
        assertFalse(collectionBag.removeAll(elementsToRemove));
        assertEquals(1, collectionBag.getCount("a"));
        assertEquals(1, collectionBag.getCount("b"));
        assertEquals(1, collectionBag.getCount("c"));
        assertEquals(3, collectionBag.size());
    }

    @Test
    public void testRetainAll() {
        HashBag<String> hashBag = new HashBag<>();
        CollectionBag<String> collectionBag = new CollectionBag<>(hashBag);
        collectionBag.addAll(Arrays.asList("a", "b", "c", "a"));
        List<String> elementsToRetain = Arrays.asList("a", "b");
        assertTrue(collectionBag.retainAll(elementsToRetain));
        assertEquals(2, collectionBag.getCount("a"));
        assertEquals(1, collectionBag.getCount("b"));
        assertEquals(0, collectionBag.getCount("c"));
        assertEquals(3, collectionBag.size());
    }

    @Test
    public void testRetainAllAllPresent() {
        HashBag<String> hashBag = new HashBag<>();
        CollectionBag<String> collectionBag = new CollectionBag<>(hashBag);
        collectionBag.addAll(Arrays.asList("a", "b", "c"));
        List<String> elementsToRetain = Arrays.asList("a", "b", "c");
        assertFalse(collectionBag.retainAll(elementsToRetain));
        assertEquals(1, collectionBag.getCount("a"));
        assertEquals(1, collectionBag.getCount("b"));
        assertEquals(1, collectionBag.getCount("c"));
        assertEquals(3, collectionBag.size());
    }

    @Test
    public void testRetainAllEmpty() {
        HashBag<String> hashBag = new HashBag<>();
        CollectionBag<String> collectionBag = new CollectionBag<>(hashBag);
        collectionBag.addAll(Arrays.asList("a", "b", "c"));
        List<String> elementsToRetain = Collections.emptyList();
        assertTrue(collectionBag.retainAll(elementsToRetain));
        assertTrue(collectionBag.isEmpty());
    }
}