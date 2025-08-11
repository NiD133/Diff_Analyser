package org.apache.commons.collections4.collection;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import org.apache.commons.collections4.Transformer;
import org.junit.Test;

/**
 * Readable, behavior-focused tests for IndexedCollection.
 *
 * These tests focus on the key behaviors described in the class Javadoc:
 * - Map-like, index-backed operations
 * - Unique vs. non-unique index behavior
 * - Index staying in sync with the decorated collection only when modified via the decorator
 * - Rebuilding the index via reindex()
 * - Correct index updates on add/remove/removeAll/retainAll/clear/removeIf
 */
public class IndexedCollectionTest {

    private static final Transformer<String, Integer> LENGTH_KEY = String::length;

    // Helpers

    private static <T> Set<T> asSet(Collection<T> c) {
        return c == null ? null : new HashSet<>(c);
    }

    // Non-unique index tests

    @Test
    public void nonUnique_addAndRetrieveByKey() {
        List<String> data = new ArrayList<>();
        IndexedCollection<Integer, String> idx =
                IndexedCollection.nonUniqueIndexedCollection(data, LENGTH_KEY);

        assertTrue(idx.add("a"));   // len 1
        assertTrue(idx.add("b"));   // len 1
        assertTrue(idx.add("cc"));  // len 2

        assertTrue(idx.contains("a"));
        assertTrue(idx.contains("b"));
        assertTrue(idx.contains("cc"));

        // All values for key=1
        Collection<String> key1 = idx.values(1);
        assertNotNull(key1);
        assertEquals(asSet(Arrays.asList("a", "b")), asSet(key1));

        // First value for key=1 (get returns any one, typically first)
        assertEquals("a", idx.get(1));

        // Missing key
        assertNull(idx.values(3));
        assertNull(idx.get(3));
    }

    @Test
    public void nonUnique_removeUpdatesIndex() {
        List<String> data = new ArrayList<>();
        IndexedCollection<Integer, String> idx =
                IndexedCollection.nonUniqueIndexedCollection(data, LENGTH_KEY);

        idx.add("a");
        idx.add("b");
        idx.add("cc");

        assertTrue(idx.remove("b"));

        assertFalse(idx.contains("b"));
        assertEquals(asSet(Arrays.asList("a")), asSet(idx.values(1)));
        assertEquals(asSet(Arrays.asList("cc")), asSet(idx.values(2)));
    }

    @Test
    public void nonUnique_removeIfUpdatesIndex() {
        List<String> data = new ArrayList<>();
        IndexedCollection<Integer, String> idx =
                IndexedCollection.nonUniqueIndexedCollection(data, LENGTH_KEY);

        idx.add("a");
        idx.add("bb");
        idx.add("ccc");

        Predicate<String> startsWithC = s -> s.startsWith("c");
        assertTrue(idx.removeIf(startsWithC));

        assertTrue(idx.contains("a"));
        assertTrue(idx.contains("bb"));
        assertFalse(idx.contains("ccc"));

        assertEquals(asSet(Arrays.asList("a")), asSet(idx.values(1)));
        assertEquals(asSet(Arrays.asList("bb")), asSet(idx.values(2)));
        assertNull(idx.values(3));
    }

    @Test
    public void nonUnique_removeAllAndRetainAllUpdateIndex() {
        List<String> data = new ArrayList<>();
        IndexedCollection<Integer, String> idx =
                IndexedCollection.nonUniqueIndexedCollection(data, LENGTH_KEY);

        idx.addAll(Arrays.asList("a", "b", "cc", "dd")); // keys: 1,1,2,2

        // Remove some
        assertTrue(idx.removeAll(Arrays.asList("a", "dd", "xx"))); // "xx" ignored
        assertFalse(idx.contains("a"));
        assertFalse(idx.contains("dd"));

        // Retain only "b"
        assertTrue(idx.retainAll(Arrays.asList("b", "yy"))); // "yy" ignored
        assertTrue(idx.contains("b"));
        assertFalse(idx.contains("cc"));

        assertEquals(asSet(Arrays.asList("b")), asSet(idx.values(1)));
        assertNull(idx.values(2));
    }

    @Test
    public void nonUnique_clearClearsCollectionAndIndex() {
        List<String> data = new ArrayList<>();
        IndexedCollection<Integer, String> idx =
                IndexedCollection.nonUniqueIndexedCollection(data, LENGTH_KEY);

        idx.addAll(Arrays.asList("a", "bb"));
        idx.clear();

        assertTrue(data.isEmpty());
        assertNull(idx.values(1));
        assertNull(idx.values(2));
        assertFalse(idx.contains("a"));
        assertFalse(idx.contains("bb"));
    }

    @Test
    public void nonUnique_reindexCatchesExternalModifications() {
        // Modify underlying collection directly to get the index out of sync.
        List<String> data = new ArrayList<>();
        IndexedCollection<Integer, String> idx =
                IndexedCollection.nonUniqueIndexedCollection(data, LENGTH_KEY);

        idx.add("a");     // indexed
        data.add("bb");   // bypasses decorator; index doesn't see this yet

        // Index-based operations don't see "bb" before reindex()
        assertFalse(idx.contains("bb"));
        assertNull(idx.values(2));

        // After reindex, the index matches the collection
        idx.reindex();
        assertTrue(idx.contains("bb"));
        assertEquals(asSet(Arrays.asList("bb")), asSet(idx.values(2)));
    }

    // Unique index tests

    @Test
    public void unique_allowsSingleValuePerKey() {
        List<String> data = new ArrayList<>();
        IndexedCollection<Integer, String> idx =
                IndexedCollection.uniqueIndexedCollection(data, LENGTH_KEY);

        assertTrue(idx.add("aa")); // key=2

        assertEquals("aa", idx.get(2));
        assertEquals(asSet(Arrays.asList("aa")), asSet(idx.values(2)));
        assertNull(idx.values(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void unique_preventsDuplicateKeysOnAdd() {
        List<String> data = new ArrayList<>();
        IndexedCollection<Integer, String> idx =
                IndexedCollection.uniqueIndexedCollection(data, LENGTH_KEY);

        idx.add("aa"); // key=2
        idx.add("bb"); // same key=2 -> should fail
    }

    @Test(expected = IllegalArgumentException.class)
    public void unique_preventsDuplicateKeysOnAddAll() {
        List<String> data = new ArrayList<>();
        IndexedCollection<Integer, String> idx =
                IndexedCollection.uniqueIndexedCollection(data, LENGTH_KEY);

        // "aa" and "bb" both map to key=2; unique index must reject this batch
        idx.addAll(Arrays.asList("aa", "bb"));
    }

    @Test
    public void unique_removeAndReAddWithSameKey() {
        List<String> data = new ArrayList<>();
        IndexedCollection<Integer, String> idx =
                IndexedCollection.uniqueIndexedCollection(data, LENGTH_KEY);

        assertTrue(idx.add("aa"));    // key=2
        assertTrue(idx.remove("aa")); // frees key=2
        assertTrue(idx.add("bb"));    // now allowed (same key, previous value removed)
        assertEquals("bb", idx.get(2));
    }

    @Test
    public void unique_containsAndContainsAllUseIndex() {
        List<String> data = new ArrayList<>();
        IndexedCollection<Integer, String> idx =
                IndexedCollection.uniqueIndexedCollection(data, LENGTH_KEY);

        assertFalse(idx.contains("aa"));
        assertTrue(idx.add("aa"));
        assertTrue(idx.contains("aa"));

        assertFalse(idx.containsAll(Arrays.asList("aa", "bb")));
        assertTrue(idx.add("bbb")); // different key=3
        assertTrue(idx.containsAll(Arrays.asList("aa", "bbb")));
    }

    @Test
    public void valuesReturnsNullForMissingKey() {
        List<String> data = new ArrayList<>();
        IndexedCollection<Integer, String> idx =
                IndexedCollection.uniqueIndexedCollection(data, LENGTH_KEY);

        assertNull(idx.values(42));
        assertNull(idx.get(42));
    }
}