package org.apache.commons.collections4.multimap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.Collection;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.TransformerUtils;
import org.apache.commons.collections4.collection.AbstractCollectionTest;
import org.apache.commons.collections4.collection.TransformedCollectionTest;
import org.junit.jupiter.api.Test;

public class TransformedMultiValuedMapTestTest4<K, V> extends AbstractMultiValuedMapTest<K, V> {

    @Override
    protected int getIterationBehaviour() {
        return AbstractCollectionTest.UNORDERED;
    }

    @Override
    public MultiValuedMap<K, V> makeObject() {
        return TransformedMultiValuedMap.transformingMap(new ArrayListValuedHashMap<>(), TransformerUtils.<K>nopTransformer(), TransformerUtils.<V>nopTransformer());
    }

    @Test
    @SuppressWarnings("unchecked")
    void testValueTransformedMap() {
        final Object[] els = { "1", "3", "5", "7", "2", "4", "6" };
        final MultiValuedMap<K, V> map = TransformedMultiValuedMap.transformingMap(new ArrayListValuedHashMap<>(), null, (Transformer<? super V, ? extends V>) TransformedCollectionTest.STRING_TO_INTEGER_TRANSFORMER);
        assertEquals(0, map.size());
        for (int i = 0; i < els.length; i++) {
            map.put((K) els[i], (V) els[i]);
            assertEquals(i + 1, map.size());
            assertTrue(map.containsValue(Integer.valueOf((String) els[i])));
            assertFalse(map.containsValue(els[i]));
            assertTrue(map.containsKey(els[i]));
            assertTrue(map.get((K) els[i]).contains(Integer.valueOf((String) els[i])));
        }
        assertTrue(map.remove(els[0]).contains(Integer.valueOf((String) els[0])));
    }
}
