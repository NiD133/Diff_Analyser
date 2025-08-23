package org.apache.commons.collections4.properties;

import org.junit.Test;

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;

import static org.junit.Assert.*;

/**
 * Readable, behavior-focused tests for OrderedProperties.
 * 
 * Goals:
 * - Use clear names and simple assertions.
 * - Avoid mocks and obscure constructs.
 * - Cover the most important behaviors: ordering, basic Map operations,
 *   Properties-specific APIs, and null checks consistent with Properties/Hashtable.
 */
public class OrderedPropertiesTest {

    // ----------------------------------------------------------------------
    // Ordering behavior
    // ----------------------------------------------------------------------

    @Test
    public void preservesInsertionOrderInKeySetAndEntrySet() {
        OrderedProperties props = new OrderedProperties();

        props.put("k1", "v1");
        props.put("k2", "v2");
        props.put("k3", "v3");

        // keySet order
        List<Object> keys = new ArrayList<>(props.keySet());
        assertEquals(List.of("k1", "k2", "k3"), keys);

        // entrySet order (iterate and capture keys)
        List<Object> entryKeys = new ArrayList<>();
        for (Map.Entry<Object, Object> e : props.entrySet()) {
            entryKeys.add(e.getKey());
        }
        assertEquals(List.of("k1", "k2", "k3"), entryKeys);
    }

    @Test
    public void putAllFromLinkedHashMapPreservesOrder() {
        OrderedProperties props = new OrderedProperties();

        LinkedHashMap<Object, Object> source = new LinkedHashMap<>();
        source.put("a", 1);
        source.put("b", 2);
        source.put("c", 3);

        props.putAll(source);

        assertEquals(List.of("a", "b", "c"), new ArrayList<>(props.keySet()));
    }

    @Test
    public void propertyNamesAndKeysEnumerationsAreOrdered() {
        OrderedProperties props = new OrderedProperties();
        props.setProperty("k1", "v1");
        props.setProperty("k2", "v2");
        props.setProperty("k3", "v3");

        List<Object> keys = enumerationToList(props.keys());
        assertEquals(List.of("k1", "k2", "k3"), keys);

        List<Object> propNames = enumerationToList(props.propertyNames());
        assertEquals(List.of("k1", "k2", "k3"), propNames);
    }

    @Test
    public void loadFromReaderMaintainsOrder() throws Exception {
        OrderedProperties props = new OrderedProperties();
        Reader r = new StringReader("a=1\nb=2\nc=3\n");

        props.load(r);

        assertEquals(3, props.size());
        assertEquals(List.of("a", "b", "c"), new ArrayList<>(props.keySet()));
    }

    // ----------------------------------------------------------------------
    // Basic Map-like behaviors
    // ----------------------------------------------------------------------

    @Test
    public void putIfAbsentDoesNotOverrideExisting() {
        OrderedProperties props = new OrderedProperties();
        props.setProperty("existing", "value");

        Object previous = props.putIfAbsent("existing", "new");
        assertEquals("value", previous);
        assertEquals("value", props.get("existing"));
    }

    @Test
    public void removeWithMatchingValueRemovesEntry() {
        OrderedProperties props = new OrderedProperties();
        props.put("k", "v");

        assertTrue(props.remove("k", "v"));    // removed
        assertFalse(props.containsKey("k"));
    }

    @Test
    public void removeWithNonMatchingValueDoesNothing() {
        OrderedProperties props = new OrderedProperties();
        props.put("k", "v");

        assertFalse(props.remove("k", "x"));   // not removed
        assertTrue(props.containsKey("k"));
    }

    @Test
    public void clearEmptiesTheMap() {
        OrderedProperties props = new OrderedProperties();
        props.put("a", 1);
        props.put("b", 2);

        props.clear();

        assertTrue(props.isEmpty());
        assertTrue(props.entrySet().isEmpty());
        assertTrue(props.keySet().isEmpty());
    }

    // ----------------------------------------------------------------------
    // compute / computeIfAbsent / merge
    // ----------------------------------------------------------------------

    @Test
    public void computeIfAbsentCreatesValueOnce() {
        OrderedProperties props = new OrderedProperties();

        Function<Object, Object> mapping = k -> "val-" + k;

        Object first = props.computeIfAbsent("a", mapping);
        Object second = props.computeIfAbsent("a", mapping);

        assertEquals("val-a", first);
        assertEquals("val-a", second); // not recreated
        assertEquals("val-a", props.get("a"));
    }

    @Test
    public void computeUpdatesExistingValue() {
        OrderedProperties props = new OrderedProperties();
        props.put("a", 1);

        Object result = props.compute("a", (k, v) -> String.valueOf((int) v + 1));

        assertEquals("2", result);
        assertEquals("2", props.get("a"));
    }

    @Test
    public void mergeAddsWhenAbsentAndRemapsWhenPresent() {
        OrderedProperties props = new OrderedProperties();

        // When absent: value should be inserted
        Object r1 = props.merge("x", "1", (oldV, newV) -> oldV.toString() + "+" + newV);
        assertEquals("1", r1);
        assertEquals("1", props.get("x"));

        // When present: use the remapping function
        Object r2 = props.merge("x", "2", (oldV, newV) -> oldV + "+" + newV);
        assertEquals("1+2", r2);
        assertEquals("1+2", props.get("x"));
    }

    @Test
    public void mergeRemappingReturningNullRemovesEntry() {
        OrderedProperties props = new OrderedProperties();
        props.put("k", "v");

        Object r = props.merge("k", "ignored", (oldV, newV) -> null);
        assertNull(r);
        assertFalse(props.containsKey("k"));
    }

    // ----------------------------------------------------------------------
    // forEach
    // ----------------------------------------------------------------------

    @Test
    public void forEachVisitsEntriesInOrder() {
        OrderedProperties props = new OrderedProperties();
        props.put("a", 1);
        props.put("b", 2);
        props.put("c", 3);

        List<String> seen = new ArrayList<>();
        props.forEach((k, v) -> seen.add(k + "=" + v));

        assertEquals(List.of("a=1", "b=2", "c=3"), seen);
    }

    @Test
    public void forEachOnEmptyDoesNothing() {
        OrderedProperties props = new OrderedProperties();
        List<String> seen = new ArrayList<>();

        props.forEach((k, v) -> seen.add("should-not-happen"));

        assertTrue(seen.isEmpty());
        assertTrue(props.isEmpty());
    }

    // ----------------------------------------------------------------------
    // toString
    // ----------------------------------------------------------------------

    @Test
    public void toStringIsEmptyForEmptyMap() {
        OrderedProperties props = new OrderedProperties();
        assertEquals("{}", props.toString());
    }

    @Test
    public void toStringContainsEntries() {
        OrderedProperties props = new OrderedProperties();
        props.put("a", 1);
        props.put("b", 2);

        String s = props.toString();
        assertTrue(s.startsWith("{"));
        assertTrue(s.endsWith("}"));
        assertTrue(s.contains("a=1"));
        assertTrue(s.contains("b=2"));
    }

    // ----------------------------------------------------------------------
    // Null handling (consistent with Properties/Hashtable semantics)
    // ----------------------------------------------------------------------

    @Test(expected = NullPointerException.class)
    public void putNullKeyThrows() {
        OrderedProperties props = new OrderedProperties();
        props.put(null, "v");
    }

    @Test(expected = NullPointerException.class)
    public void putNullValueThrows() {
        OrderedProperties props = new OrderedProperties();
        props.put("k", null);
    }

    @Test(expected = NullPointerException.class)
    public void removeNullKeyThrows() {
        OrderedProperties props = new OrderedProperties();
        props.remove(null);
    }

    @Test(expected = NullPointerException.class)
    public void removeNullKeyValueThrows() {
        OrderedProperties props = new OrderedProperties();
        props.remove(null, null);
    }

    @Test(expected = NullPointerException.class)
    public void putIfAbsentWithNullsThrows() {
        OrderedProperties props = new OrderedProperties();
        props.putIfAbsent(null, null);
    }

    @Test(expected = NullPointerException.class)
    public void computeIfAbsentWithNullArgsThrows() {
        OrderedProperties props = new OrderedProperties();
        props.computeIfAbsent(null, null);
    }

    @Test(expected = NullPointerException.class)
    public void forEachWithNullConsumerThrows() {
        OrderedProperties props = new OrderedProperties();
        props.forEach(null);
    }

    @Test(expected = NullPointerException.class)
    public void putAllNullMapThrows() {
        OrderedProperties props = new OrderedProperties();
        props.putAll(null);
    }

    @Test(expected = NullPointerException.class)
    public void mergeWithNullRemappingFunctionThrows() {
        OrderedProperties props = new OrderedProperties();
        props.merge("k", "v", null);
    }

    // ----------------------------------------------------------------------
    // Helpers
    // ----------------------------------------------------------------------

    private static List<Object> enumerationToList(Enumeration<?> e) {
        List<Object> list = new ArrayList<>();
        while (e.hasMoreElements()) {
            list.add(e.nextElement());
        }
        return list;
    }
}