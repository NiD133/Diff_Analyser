package org.apache.commons.collections4.map;

import org.apache.commons.collections4.Transformer;
import org.junit.Test;

import java.util.Comparator;
import java.util.SortedMap;
import java.util.TreeMap;

import static org.junit.Assert.*;

/**
 * Readable and focused tests for TransformedSortedMap.
 *
 * Key ideas covered:
 * - transformedSortedMap(...) transforms existing entries
 * - transformingSortedMap(...) does NOT transform existing entries
 * - puts through the wrapper are transformed
 * - sub views (headMap/subMap/tailMap) preserve transformations
 * - comparator delegation and key order
 * - null and empty map behaviors
 */
public class TransformedSortedMapTest {

    // --- Simple test transformers used across tests ---

    private static final Transformer<String, String> TRIM = new Transformer<String, String>() {
        @Override public String transform(final String input) {
            return input == null ? null : input.trim();
        }
    };

    private static final Transformer<String, String> TO_UPPER = new Transformer<String, String>() {
        @Override public String transform(final String input) {
            return input == null ? null : input.toUpperCase();
        }
    };

    private static final Transformer<Integer, Integer> TIMES_10 = new Transformer<Integer, Integer>() {
        @Override public Integer transform(final Integer input) {
            return input == null ? null : input * 10;
        }
    };

    // --- Factory behavior ---

    @Test
    public void transformedSortedMap_transformsExistingContents() {
        // Arrange: map has an existing entry
        final TreeMap<String, String> base = new TreeMap<>();
        base.put("a", "x");

        // Act: wrap with value transformer (uppercase) that transforms existing contents
        final TransformedSortedMap<String, String> map =
            TransformedSortedMap.transformedSortedMap(base, null, TO_UPPER);

        // Assert: existing entry is transformed
        assertEquals("X", map.get("a"));
        // Also ensure identity of keys is preserved (no key transformer)
        assertTrue(map.containsKey("a"));
        assertFalse(map.containsKey("A"));
    }

    @Test
    public void transformingSortedMap_doesNotTransformExistingContents() {
        // Arrange: map has an existing entry
        final TreeMap<String, String> base = new TreeMap<>();
        base.put("a", "x");

        // Act: wrap with value transformer (uppercase) that does NOT transform existing contents
        final TransformedSortedMap<String, String> map =
            TransformedSortedMap.transformingSortedMap(base, null, TO_UPPER);

        // Assert: existing entry remains as-is
        assertEquals("x", map.get("a"));

        // But future puts are transformed (see next test)
        map.put("b", "y");
        assertEquals("Y", map.get("b"));
    }

    // --- Put behavior ---

    @Test
    public void put_appliesKeyAndValueTransformers() {
        final TreeMap<String, String> base = new TreeMap<>();

        // Trim keys and uppercase values
        final TransformedSortedMap<String, String> map =
            TransformedSortedMap.transformingSortedMap(base, TRIM, TO_UPPER);

        // Act: put with leading/trailing spaces in key
        map.put("  key  ", "value");

        // Assert: transformed key/value visible in map
        assertEquals(1, map.size());
        assertTrue(map.containsKey("key"));
        assertEquals("VALUE", map.get("key"));

        // Original (untrimmed) key no longer matches
        assertNull(map.get("  key  "));
    }

    // --- Sub views preserve transformations ---

    @Test
    public void subViews_preserveTransformers_andRespectBounds() {
        final TreeMap<Integer, Integer> base = new TreeMap<>();
        final TransformedSortedMap<Integer, Integer> map =
            TransformedSortedMap.transformingSortedMap(base, null, TIMES_10);

        // Put through top-level map is transformed
        map.put(1, 2);
        assertEquals(Integer.valueOf(20), map.get(1));

        // A headMap view should preserve the same transformation behavior
        final SortedMap<Integer, Integer> head = map.headMap(2);
        assertEquals(1, head.size()); // contains key 1

        // Put via the view is also transformed and respects bounds
        head.put(0, 3);
        assertEquals(Integer.valueOf(30), map.get(0));
        assertEquals(2, map.size());

        // Ensure the view does not accept out-of-range keys
        try {
            head.put(5, 9); // 5 is not < 2
            fail("Expected IllegalArgumentException for out-of-range key");
        } catch (IllegalArgumentException expected) {
            // expected
        }
    }

    // --- Comparator delegation and key order ---

    @Test
    public void comparator_isDelegated_andOrderIsRespected() {
        // A reverse-order comparator
        final Comparator<Integer> reverse = new Comparator<Integer>() {
            @Override public int compare(final Integer a, final Integer b) {
                return -Integer.compare(a, b);
            }
        };
        final TreeMap<Integer, Integer> base = new TreeMap<>(reverse);

        final TransformedSortedMap<Integer, Integer> map =
            TransformedSortedMap.transformingSortedMap(base, null, TIMES_10);

        // Comparator is delegated from the underlying TreeMap
        assertSame(reverse, map.comparator());

        // With reverse order, the firstKey is the numerically largest key
        map.put(1, 1);
        map.put(3, 1);
        map.put(2, 1);
        assertEquals(Integer.valueOf(3), map.firstKey());
        assertEquals(Integer.valueOf(1), map.lastKey());
    }

    @Test
    public void comparator_isNullForNaturalOrder() {
        final TreeMap<String, String> base = new TreeMap<>(); // natural ordering
        final TransformedSortedMap<String, String> map =
            TransformedSortedMap.transformingSortedMap(base, null, null);

        assertNull(map.comparator());
    }

    // --- Nulls and empty map behaviors ---

    @Test(expected = NullPointerException.class)
    public void transformedSortedMap_throwsOnNullMap() {
        TransformedSortedMap.transformedSortedMap(null, null, null);
    }

    @Test(expected = NullPointerException.class)
    public void transformingSortedMap_throwsOnNullMap() {
        TransformedSortedMap.transformingSortedMap(null, null, null);
    }

    @Test(expected = NullPointerException.class)
    public void headMap_withNullKey_throwsNPE() {
        final TransformedSortedMap<String, String> map =
            TransformedSortedMap.transformingSortedMap(new TreeMap<String, String>(), null, null);

        map.headMap(null);
    }

    @Test(expected = java.util.NoSuchElementException.class)
    public void firstKey_onEmptyMap_throwsNoSuchElementException() {
        final TransformedSortedMap<Integer, Integer> map =
            TransformedSortedMap.transformingSortedMap(new TreeMap<Integer, Integer>(), null, null);

        map.firstKey();
    }

    @Test(expected = java.util.NoSuchElementException.class)
    public void lastKey_onEmptyMap_throwsNoSuchElementException() {
        final TransformedSortedMap<Integer, Integer> map =
            TransformedSortedMap.transformingSortedMap(new TreeMap<Integer, Integer>(), null, null);

        map.lastKey();
    }

    // --- Basic range operations sanity checks ---

    @Test
    public void subMap_headMap_tailMap_onEmptyAreEmpty() {
        final TransformedSortedMap<Integer, Integer> map =
            TransformedSortedMap.transformingSortedMap(new TreeMap<Integer, Integer>(), null, null);

        assertTrue(map.subMap(1, 1).isEmpty());
        assertTrue(map.headMap(10).isEmpty());
        assertTrue(map.tailMap(0).isEmpty());
    }
}