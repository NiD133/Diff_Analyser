package org.apache.commons.collections4.properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;

import org.junit.jupiter.api.Test;

/**
 * Tests for OrderedProperties that focus on clarity and maintainability.
 */
class OrderedPropertiesTest {

    // Common test data
    private static final String KEY_PREFIX = "key";
    private static final String VALUE_PREFIX = "value";
    private static final int MIN_INDEX = 1;
    private static final int MAX_INDEX = 11;

    private static final char ALPHA_START = 'Z';
    private static final char ALPHA_END = 'A';

    private static final String RES_ASC = "src/test/resources/org/apache/commons/collections4/properties/test.properties";
    private static final String RES_DESC = "src/test/resources/org/apache/commons/collections4/properties/test-reverse.properties";

    // Key/value helpers
    private static String k(final int i) {
        return KEY_PREFIX + i;
    }

    private static String v(final int i) {
        return VALUE_PREFIX + i;
    }

    private static List<String> numericKeysAsc() {
        final List<String> keys = new ArrayList<>(MAX_INDEX - MIN_INDEX + 1);
        for (int i = MIN_INDEX; i <= MAX_INDEX; i++) {
            keys.add(k(i));
        }
        return keys;
    }

    private static List<String> numericKeysDesc() {
        final List<String> keys = new ArrayList<>(MAX_INDEX - MIN_INDEX + 1);
        for (int i = MAX_INDEX; i >= MIN_INDEX; i--) {
            keys.add(k(i));
        }
        return keys;
    }

    private static List<String> alphaKeysDesc() {
        final List<String> keys = new ArrayList<>(ALPHA_START - ALPHA_END + 1);
        for (char c = ALPHA_START; c >= ALPHA_END; c--) {
            keys.add(String.valueOf(c));
        }
        return keys;
    }

    /**
     * Asserts ordering across all key/entry iterators of Properties.
     * Optionally also asserts expected values if valueForKey is provided.
     */
    private static void assertOrder(final OrderedProperties props,
                                    final List<String> expectedKeys,
                                    final Function<String, String> valueForKeyOrNull) {
        // keys()
        final Enumeration<Object> eKeys = props.keys();
        for (final String expected : expectedKeys) {
            assertEquals(expected, eKeys.nextElement());
        }

        // keySet().iterator()
        final Iterator<Object> keyIter = props.keySet().iterator();
        for (final String expected : expectedKeys) {
            assertEquals(expected, keyIter.next());
        }

        // entrySet().iterator()
        final Iterator<Entry<Object, Object>> entryIter = props.entrySet().iterator();
        for (final String expected : expectedKeys) {
            final Entry<Object, Object> entry = entryIter.next();
            assertEquals(expected, entry.getKey());
            if (valueForKeyOrNull != null) {
                assertEquals(valueForKeyOrNull.apply(expected), entry.getValue());
            }
        }

        // propertyNames()
        final Enumeration<?> pNames = props.propertyNames();
        for (final String expected : expectedKeys) {
            assertEquals(expected, pNames.nextElement());
        }
    }

    private static void assertNumericAsc(final OrderedProperties props) {
        assertOrder(props, numericKeysAsc(), key -> {
            final int idx = Integer.parseInt(key.substring(KEY_PREFIX.length()));
            return v(idx);
        });
    }

    private static void assertNumericDesc(final OrderedProperties props) {
        assertOrder(props, numericKeysDesc(), key -> {
            final int idx = Integer.parseInt(key.substring(KEY_PREFIX.length()));
            return v(idx);
        });
    }

    private static void assertAlphaDesc(final OrderedProperties props) {
        assertOrder(props, alphaKeysDesc(), key -> "Value" + key);
    }

    private static OrderedProperties loadFromFile(final String path) throws IOException {
        final OrderedProperties props = new OrderedProperties();
        try (FileReader reader = new FileReader(path)) {
            props.load(reader);
        }
        return props;
    }

    private static void assertKeyAbsentEverywhere(final OrderedProperties props, final String key) {
        assertFalse(props.contains(key));
        assertFalse(props.containsKey(key));
        assertFalse(Collections.list(props.keys()).contains(key));
        assertFalse(Collections.list(props.propertyNames()).contains(key));
    }

    @Test
    void testPut() {
        final OrderedProperties props = new OrderedProperties();

        // Ascending inserts
        for (int i = MIN_INDEX; i <= MAX_INDEX; i++) {
            props.put(k(i), v(i));
        }
        assertNumericAsc(props);

        // Descending inserts
        props.clear();
        for (int i = MAX_INDEX; i >= MIN_INDEX; i--) {
            props.put(k(i), v(i));
        }
        assertNumericDesc(props);
    }

    @Test
    void testPutIfAbsent() {
        final OrderedProperties props = new OrderedProperties();

        for (int i = MIN_INDEX; i <= MAX_INDEX; i++) {
            props.putIfAbsent(k(i), v(i));
        }
        assertNumericAsc(props);

        props.clear();
        for (int i = MAX_INDEX; i >= MIN_INDEX; i--) {
            props.putIfAbsent(k(i), v(i));
        }
        assertNumericDesc(props);
    }

    @Test
    void testPutAll() {
        final OrderedProperties source = new OrderedProperties();
        for (int i = MIN_INDEX; i <= MAX_INDEX; i++) {
            source.put(k(i), v(i));
        }

        final OrderedProperties target = new OrderedProperties();
        target.putAll(source);
        assertNumericAsc(target);

        target.clear();
        for (int i = MAX_INDEX; i >= MIN_INDEX; i--) {
            target.put(k(i), v(i));
        }
        assertNumericDesc(target);
    }

    @Test
    void testCompute() {
        final OrderedProperties props = new OrderedProperties();

        for (int i = MIN_INDEX; i <= MAX_INDEX; i++) {
            final int idx = i;
            props.compute(k(i), (k, old) -> v(idx));
        }
        assertNumericAsc(props);

        props.clear();
        for (int i = MAX_INDEX; i >= MIN_INDEX; i--) {
            final int idx = i;
            props.compute(k(i), (k, old) -> v(idx));
        }
        assertNumericDesc(props);
    }

    @Test
    void testComputeIfAbsent() {
        final OrderedProperties props = new OrderedProperties();

        for (int i = MIN_INDEX; i <= MAX_INDEX; i++) {
            final int idx = i;
            props.computeIfAbsent(k(i), k -> v(idx));
        }
        assertNumericAsc(props);

        props.clear();
        for (int i = MAX_INDEX; i >= MIN_INDEX; i--) {
            final int idx = i;
            props.computeIfAbsent(k(i), k -> v(idx));
        }
        assertNumericDesc(props);
    }

    @Test
    void testMerge() {
        final OrderedProperties props = new OrderedProperties();

        for (int i = MIN_INDEX; i <= MAX_INDEX; i++) {
            props.merge(k(i), v(i), (existing, newVal) -> newVal);
        }
        assertNumericAsc(props);

        props.clear();
        for (int i = MAX_INDEX; i >= MIN_INDEX; i--) {
            props.merge(k(i), v(i), (existing, newVal) -> newVal);
        }
        assertNumericDesc(props);
    }

    @Test
    void testEntrySet() {
        final OrderedProperties props = new OrderedProperties();
        for (char c = ALPHA_START; c >= ALPHA_END; c--) {
            props.put(String.valueOf(c), "Value" + c);
        }

        final Iterator<Map.Entry<Object, Object>> it = props.entrySet().iterator();
        for (char c = ALPHA_START; c >= ALPHA_END; c--) {
            final Map.Entry<Object, Object> e = it.next();
            assertEquals(String.valueOf(c), e.getKey());
            assertEquals("Value" + c, e.getValue());
        }
    }

    @Test
    void testForEach() {
        final OrderedProperties props = new OrderedProperties();
        for (char c = ALPHA_START; c >= ALPHA_END; c--) {
            props.put(String.valueOf(c), "Value" + c);
        }

        final Iterator<String> expectedKeys = alphaKeysDesc().iterator();
        props.forEach((k, v) -> {
            final String expectedKey = expectedKeys.next();
            assertEquals(expectedKey, k);
            assertEquals("Value" + expectedKey, v);
        });
    }

    @Test
    void testKeys() {
        final OrderedProperties props = new OrderedProperties();
        for (char c = ALPHA_START; c >= ALPHA_END; c--) {
            props.put(String.valueOf(c), "Value" + c);
        }

        final Enumeration<Object> keys = props.keys();
        for (final String expected : alphaKeysDesc()) {
            assertEquals(expected, keys.nextElement());
        }
    }

    @Test
    void testLoadOrderedKeys() throws IOException {
        final OrderedProperties props = loadFromFile(RES_ASC);
        assertNumericAsc(props);
    }

    @Test
    void testLoadOrderedKeysReverse() throws IOException {
        final OrderedProperties props = loadFromFile(RES_DESC);
        assertNumericDesc(props);
    }

    @Test
    void testRemoveKey() throws IOException {
        final OrderedProperties props = loadFromFile(RES_DESC);
        final String keyToRemove = k(MIN_INDEX);

        props.remove(keyToRemove);
        assertKeyAbsentEverywhere(props, keyToRemove);
    }

    @Test
    void testRemoveKeyValue() throws IOException {
        final OrderedProperties props = loadFromFile(RES_DESC);
        final String keyToRemove = k(MIN_INDEX);

        props.remove(keyToRemove, v(MIN_INDEX));
        assertKeyAbsentEverywhere(props, keyToRemove);
    }

    @Test
    void testToString() {
        final OrderedProperties props = new OrderedProperties();
        for (char c = ALPHA_START; c >= ALPHA_END; c--) {
            props.put(String.valueOf(c), "Value" + c);
        }
        assertEquals(
            "{Z=ValueZ, Y=ValueY, X=ValueX, W=ValueW, V=ValueV, U=ValueU, T=ValueT, S=ValueS, R=ValueR, Q=ValueQ, P=ValueP, O=ValueO, N=ValueN, M=ValueM, L=ValueL, K=ValueK, J=ValueJ, I=ValueI, H=ValueH, G=ValueG, F=ValueF, E=ValueE, D=ValueD, C=ValueC, B=ValueB, A=ValueA}",
            props.toString()
        );
    }
}