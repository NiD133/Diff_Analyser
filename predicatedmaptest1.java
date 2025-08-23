package org.apache.commons.collections4.map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.collections4.IterableMap;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.functors.TruePredicate;
import org.junit.jupiter.api.Test;

public class PredicatedMapTestTest1<K, V> extends AbstractIterableMapTest<K, V> {

    protected static final Predicate<Object> truePredicate = TruePredicate.<Object>truePredicate();

    protected static final Predicate<Object> testPredicate = String.class::isInstance;

    protected IterableMap<K, V> decorateMap(final Map<K, V> map, final Predicate<? super K> keyPredicate, final Predicate<? super V> valuePredicate) {
        return PredicatedMap.predicatedMap(map, keyPredicate, valuePredicate);
    }

    @Override
    public String getCompatibilityVersion() {
        return "4";
    }

    @Override
    public IterableMap<K, V> makeObject() {
        return decorateMap(new HashMap<>(), truePredicate, truePredicate);
    }

    public IterableMap<K, V> makeTestMap() {
        return decorateMap(new HashMap<>(), testPredicate, testPredicate);
    }

    @Test
    @SuppressWarnings("unchecked")
    void testEntrySet() {
        Map<K, V> map = makeTestMap();
        assertNotNull(map.entrySet(), "returned entryset should not be null");
        map = decorateMap(new HashMap<>(), null, null);
        map.put((K) "oneKey", (V) "oneValue");
        assertEquals(1, map.entrySet().size(), "returned entryset should contain one entry");
        map = decorateMap(map, null, null);
    }
}
