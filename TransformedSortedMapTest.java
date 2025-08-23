package org.apache.commons.collections4.map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.TransformerUtils;
import org.apache.commons.collections4.collection.TransformedCollectionTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for TransformedSortedMap.
 *
 * Notes on generics and casts:
 * - The map's transformers may change the runtime type of keys/values (e.g., String -> Integer).
 *   However, the TransformedSortedMap API is declared as Transformer<? super V, ? extends V>,
 *   which implies input and output are both of type V. To express transformations that actually
 *   change the runtime type in these tests, we use @SuppressWarnings("unchecked") at narrow
 *   points with explanatory comments.
 *
 * @param <K> the key type.
 * @param <V> the value type.
 */
public class TransformedSortedMapTest<K, V> extends AbstractSortedMapTest<K, V> {

    // Common numeric Strings used across tests (also exercises key ordering)
    private static final String[] NUMERIC_STRINGS = { "1", "3", "5", "7", "2", "4", "6" };

    private static Integer asInt(final String s) {
        return Integer.valueOf(s);
    }

    @Override
    public String getCompatibilityVersion() {
        return "4";
    }

    @Override
    public boolean isSubMapViewsSerializable() {
        // TreeMap sub map views have a bug in deserialization.
        return false;
    }

    @Override
    @SuppressWarnings("unchecked")
    public SortedMap<K, V> makeObject() {
        // Identity transform for both keys and values.
        return TransformedSortedMap.transformingSortedMap(
                new TreeMap<>(),
                (Transformer<? super K, ? extends K>) TransformerUtils.nopTransformer(),
                (Transformer<? super V, ? extends V>) TransformerUtils.nopTransformer()
        );
    }

    @Test
    @SuppressWarnings("unchecked") // We intentionally transform String values to Integer at runtime.
    void transformingSortedMap_doesNotTransformExistingEntries_butTransformsNewPuts() {
        final SortedMap<K, V> base = new TreeMap<>();
        base.put((K) "A", (V) "1");
        base.put((K) "B", (V) "2");
        base.put((K) "C", (V) "3");

        // transformingSortedMap: existing entries are NOT transformed
        final SortedMap<K, V> map = TransformedSortedMap.transformingSortedMap(
                base,
                null,
                (Transformer<? super V, ? extends V>) TransformedCollectionTest.STRING_TO_INTEGER_TRANSFORMER
        );

        assertEquals(3, map.size());
        assertEquals("1", map.get("A"));
        assertEquals("2", map.get("B"));
        assertEquals("3", map.get("C"));

        // New writes are transformed on put
        map.put((K) "D", (V) "4");
        assertEquals(Integer.valueOf(4), map.get("D"));
    }

    @Test
    @SuppressWarnings("unchecked") // We intentionally transform String values to Integer at runtime.
    void transformedSortedMap_transformsExistingEntries_andTransformsNewPuts() {
        final SortedMap<K, V> base = new TreeMap<>();
        base.put((K) "A", (V) "1");
        base.put((K) "B", (V) "2");
        base.put((K) "C", (V) "3");

        // transformedSortedMap: existing entries ARE transformed immediately
        final SortedMap<K, V> map = TransformedSortedMap.transformedSortedMap(
                base,
                null,
                (Transformer<? super V, ? extends V>) TransformedCollectionTest.STRING_TO_INTEGER_TRANSFORMER
        );

        assertEquals(3, map.size());
        assertEquals(Integer.valueOf(1), map.get("A"));
        assertEquals(Integer.valueOf(2), map.get("B"));
        assertEquals(Integer.valueOf(3), map.get("C"));

        // New writes are also transformed
        map.put((K) "D", (V) "4");
        assertEquals(Integer.valueOf(4), map.get("D"));
    }

    @Test
    @SuppressWarnings("unchecked") // We intentionally transform String keys to Integer at runtime.
    void keyTransformation_affectsLookupAndRemoval() {
        // Keys are transformed from String to Integer on write.
        final SortedMap<K, V> map = TransformedSortedMap.transformingSortedMap(
                new TreeMap<>(),
                (Transformer<? super K, ? extends K>) TransformedCollectionTest.STRING_TO_INTEGER_TRANSFORMER,
                null
        );

        assertEquals(0, map.size());

        for (int i = 0; i < NUMERIC_STRINGS.length; i++) {
            final String s = NUMERIC_STRINGS[i];

            // Put using String key; runtime key becomes Integer
            map.put((K) s, (V) s);
            assertEquals(i + 1, map.size());

            // Lookups must use the transformed (Integer) form of the key
            assertTrue(map.containsKey(asInt(s)));
            assertThrows(ClassCastException.class, () -> map.containsKey(s));

            // Values are not transformed here, so they remain Strings
            assertTrue(map.containsValue(s));
            assertEquals(s, map.get(asInt(s)));
        }

        final String first = NUMERIC_STRINGS[0];

        // Removing with an untransformed key fails (TreeMap comparator sees wrong key type)
        assertThrows(ClassCastException.class, () -> map.remove(first));

        // Removing with the transformed key succeeds and returns the original String value
        assertEquals(first, map.remove(asInt(first)));
    }

    @Test
    @SuppressWarnings("unchecked") // We intentionally transform String values to Integer at runtime.
    void valueTransformation_affectsContainsGetAndRemove() {
        // Values are transformed from String to Integer on write.
        final SortedMap<K, V> map = TransformedSortedMap.transformingSortedMap(
                new TreeMap<>(),
                null,
                (Transformer<? super V, ? extends V>) TransformedCollectionTest.STRING_TO_INTEGER_TRANSFORMER
        );

        assertEquals(0, map.size());

        for (int i = 0; i < NUMERIC_STRINGS.length; i++) {
            final String s = NUMERIC_STRINGS[i];

            map.put((K) s, (V) s);
            assertEquals(i + 1, map.size());

            // Values are now Integers
            assertTrue(map.containsValue(asInt(s)));
            assertFalse(map.containsValue(s));

            // Keys remain Strings
            assertTrue(map.containsKey(s));
            assertEquals(asInt(s), map.get(s));
        }

        // Removal returns the transformed value (Integer)
        assertEquals(asInt(NUMERIC_STRINGS[0]), map.remove(NUMERIC_STRINGS[0]));
    }

    @Test
    @SuppressWarnings("unchecked") // We intentionally transform String values to Integer at runtime.
    void entrySet_setValue_appliesValueTransformer() {
        final SortedMap<K, V> map = TransformedSortedMap.transformingSortedMap(
                new TreeMap<>(),
                null,
                (Transformer<? super V, ? extends V>) TransformedCollectionTest.STRING_TO_INTEGER_TRANSFORMER
        );

        // Seed with a couple of entries
        map.put((K) "A", (V) "1");
        map.put((K) "B", (V) "2");

        final Set<Map.Entry<K, V>> entrySet = map.entrySet();

        // Using array snapshot then setValue() should apply the transformer
        final Map.Entry<K, V>[] array = entrySet.toArray(new Map.Entry[0]);
        array[0].setValue((V) "66");
        assertEquals(Integer.valueOf(66), array[0].getValue());
        assertEquals(Integer.valueOf(66), map.get(array[0].getKey()));

        // Using iterator view then setValue() should also apply the transformer
        final Map.Entry<K, V> entry = entrySet.iterator().next();
        entry.setValue((V) "88");
        assertEquals(Integer.valueOf(88), entry.getValue());
        assertEquals(Integer.valueOf(88), map.get(entry.getKey()));
    }

//    // Helper to regenerate serialized forms if needed:
//    // void testCreate() throws Exception {
//    //     resetEmpty();
//    //     writeExternalFormToDisk(
//    //         (java.io.Serializable) map,
//    //         "src/test/resources/data/test/TransformedSortedMap.emptyCollection.version4.obj");
//    //     resetFull();
//    //     writeExternalFormToDisk(
//    //         (java.io.Serializable) map,
//    //         "src/test/resources/data/test/TransformedSortedMap.fullCollection.version4.obj");
//    // }
}