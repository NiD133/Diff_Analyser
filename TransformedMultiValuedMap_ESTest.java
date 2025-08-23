package org.apache.commons.collections4.multimap;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.ExceptionTransformer;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.apache.commons.collections4.multimap.UnmodifiableMultiValuedMap;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

import static org.junit.Assert.*;

public class TransformedMultiValuedMap_ReadableTest {

    // --- Helper transformers -------------------------------------------------

    private static Transformer<String, String> upper() {
        return input -> input == null ? null : input.toUpperCase();
    }

    private static Transformer<String, String> suffix(String suffix) {
        return input -> input == null ? null : input + suffix;
    }

    // --- Factory method behavior --------------------------------------------

    @Test
    public void transformedMap_transformsExistingEntries() {
        ArrayListValuedHashMap<String, String> base = new ArrayListValuedHashMap<>();
        base.put("a", "x");

        MultiValuedMap<String, String> decorated =
                TransformedMultiValuedMap.transformedMap(base, upper(), suffix("!"));

        assertTrue("Existing entries should be transformed",
                decorated.containsMapping("A", "x!"));
        assertFalse("Original (untransformed) entries should be gone",
                decorated.containsMapping("a", "x"));
    }

    @Test
    public void transformingMap_doesNotTransformExistingEntries() {
        ArrayListValuedHashMap<String, String> base = new ArrayListValuedHashMap<>();
        base.put("a", "x");

        MultiValuedMap<String, String> decorated =
                TransformedMultiValuedMap.transformingMap(base, upper(), suffix("!"));

        assertTrue("Existing entries must remain untransformed",
                decorated.containsMapping("a", "x"));
        assertFalse("No transformed copy should be created for existing entries",
                decorated.containsMapping("A", "x!"));
    }

    @Test
    public void factoryMethods_nullMap_throwsNPE() {
        try {
            TransformedMultiValuedMap.transformedMap(null, upper(), suffix("!"));
            fail("Expected NullPointerException for transformedMap(null, ...)");
        } catch (NullPointerException expected) {
            // ok
        }

        try {
            TransformedMultiValuedMap.transformingMap(null, upper(), suffix("!"));
            fail("Expected NullPointerException for transformingMap(null, ...)");
        } catch (NullPointerException expected) {
            // ok
        }
    }

    // --- Put semantics -------------------------------------------------------

    @Test
    public void put_appliesKeyAndValueTransformers() {
        ArrayListValuedHashMap<String, String> base = new ArrayListValuedHashMap<>();
        MultiValuedMap<String, String> map =
                TransformedMultiValuedMap.transformingMap(base, upper(), suffix("!"));

        map.put("a", "x");

        assertTrue(map.containsMapping("A", "x!"));
        assertFalse(map.containsMapping("a", "x"));
    }

    @Test
    public void putAllIterable_appliesTransformers() {
        ArrayListValuedHashMap<String, String> base = new ArrayListValuedHashMap<>();
        MultiValuedMap<String, String> map =
                TransformedMultiValuedMap.transformingMap(base, upper(), suffix("!"));

        map.putAll("a", Arrays.asList("x", "y"));

        assertTrue(map.containsMapping("A", "x!"));
        assertTrue(map.containsMapping("A", "y!"));
        assertFalse(map.containsMapping("a", "x"));
        assertFalse(map.containsMapping("a", "y"));
    }

    @Test
    public void putAll_mapOverload_appliesTransformers() {
        ArrayListValuedHashMap<String, String> base = new ArrayListValuedHashMap<>();
        MultiValuedMap<String, String> map =
                TransformedMultiValuedMap.transformingMap(base, upper(), suffix("!"));

        HashMap<String, String> toAdd = new HashMap<>();
        toAdd.put("a", "x");
        toAdd.put("b", "y");

        map.putAll(toAdd);

        assertTrue(map.containsMapping("A", "x!"));
        assertTrue(map.containsMapping("B", "y!"));
        assertFalse(map.containsMapping("a", "x"));
        assertFalse(map.containsMapping("b", "y"));
    }

    @Test
    public void putAll_multiValuedMapOverload_appliesTransformers() {
        ArrayListValuedHashMap<String, String> source = new ArrayListValuedHashMap<>();
        source.put("a", "x");
        source.put("b", "y");

        ArrayListValuedHashMap<String, String> base = new ArrayListValuedHashMap<>();
        MultiValuedMap<String, String> target =
                TransformedMultiValuedMap.transformingMap(base, upper(), suffix("!"));

        target.putAll(source);

        assertTrue(target.containsMapping("A", "x!"));
        assertTrue(target.containsMapping("B", "y!"));
    }

    // --- Identity behavior when transformers are null -----------------------

    @Test
    public void nullTransformers_actAsIdentity() {
        ArrayListValuedHashMap<String, String> base = new ArrayListValuedHashMap<>();
        TransformedMultiValuedMap<String, String> map =
                new TransformedMultiValuedMap<>(base, null, null);

        // Access protected methods directly (same package)
        assertEquals("a", map.transformKey("a"));
        assertEquals("x", map.transformValue("x"));

        map.put("a", "x");
        assertTrue(map.containsMapping("a", "x"));
    }

    // --- Error handling ------------------------------------------------------

    @Test
    public void transformValue_exceptionFromTransformerBubblesUp() {
        ArrayListValuedHashMap<String, String> base = new ArrayListValuedHashMap<>();
        TransformedMultiValuedMap<String, String> map =
                new TransformedMultiValuedMap<>(base, null, ExceptionTransformer.exceptionTransformer());

        try {
            map.transformValue("anything");
            fail("Expected RuntimeException from ExceptionTransformer");
        } catch (RuntimeException expected) {
            // message comes from ExceptionTransformer; existence of exception is what matters
        }
    }

    @Test
    public void putAllIterable_withNullIterable_throwsNPE() {
        ArrayListValuedHashMap<String, String> base = new ArrayListValuedHashMap<>();
        MultiValuedMap<String, String> map =
                TransformedMultiValuedMap.transformingMap(base, upper(), suffix("!"));

        try {
            map.putAll("key", null);
            fail("Expected NullPointerException for null Iterable");
        } catch (NullPointerException expected) {
            // ok
        }
    }

    @Test
    public void putAll_withNullMapArguments_throwsNPE() {
        ArrayListValuedHashMap<String, String> base = new ArrayListValuedHashMap<>();
        MultiValuedMap<String, String> map =
                TransformedMultiValuedMap.transformingMap(base, upper(), suffix("!"));

        try {
            map.putAll((java.util.Map<String, String>) null);
            fail("Expected NullPointerException for putAll(Map) with null");
        } catch (NullPointerException expected) {
            // ok
        }

        try {
            map.putAll((MultiValuedMap<String, String>) null);
            fail("Expected NullPointerException for putAll(MultiValuedMap) with null");
        } catch (NullPointerException expected) {
            // ok
        }
    }

    // --- Behavior with unmodifiable underlying map --------------------------

    @Test
    public void put_onUnmodifiableUnderlying_throwsUOE() {
        ArrayListValuedHashMap<String, String> base = new ArrayListValuedHashMap<>();
        MultiValuedMap<String, String> unmodifiable = UnmodifiableMultiValuedMap.unmodifiableMultiValuedMap(base);

        TransformedMultiValuedMap<String, String> map =
                new TransformedMultiValuedMap<>(unmodifiable, upper(), suffix("!"));

        try {
            map.put("a", "x");
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException expected) {
            // ok
        }
    }

    // --- A small sanity check combining operations --------------------------

    @Test
    public void endToEnd_scenario() {
        MultiValuedMap<String, String> map =
                TransformedMultiValuedMap.transformingMap(new ArrayListValuedHashMap<>(), upper(), suffix("!"));

        assertTrue(map.put("a", "x"));
        assertTrue(map.putAll("b", Arrays.asList("y", "z")));
        assertTrue(map.putAll(new HashMap<String, String>() {{
            put("c", "w");
        }}));

        assertTrue(map.containsMapping("A", "x!"));
        assertTrue(map.containsMapping("B", "y!"));
        assertTrue(map.containsMapping("B", "z!"));
        assertTrue(map.containsMapping("C", "w!"));

        // Original (untransformed) pairs should not be present
        assertFalse(map.containsMapping("a", "x"));
        assertFalse(map.containsMapping("b", "y"));
        assertFalse(map.containsMapping("b", "z"));
        assertFalse(map.containsMapping("c", "w"));
    }
}