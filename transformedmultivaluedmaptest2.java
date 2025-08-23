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

public class TransformedMultiValuedMapTestTest2<K, V> extends AbstractMultiValuedMapTest<K, V> {

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
    void testFactory_decorateTransform() {
        final MultiValuedMap<K, V> base = new ArrayListValuedHashMap<>();
        base.put((K) "A", (V) "1");
        base.put((K) "B", (V) "2");
        base.put((K) "C", (V) "3");
        final MultiValuedMap<K, V> trans = TransformedMultiValuedMap.transformedMap(base, null, (Transformer<? super V, ? extends V>) TransformedCollectionTest.STRING_TO_INTEGER_TRANSFORMER);
        assertEquals(3, trans.size());
        assertTrue(trans.get((K) "A").contains(Integer.valueOf(1)));
        assertTrue(trans.get((K) "B").contains(Integer.valueOf(2)));
        assertTrue(trans.get((K) "C").contains(Integer.valueOf(3)));
        trans.put((K) "D", (V) "4");
        assertTrue(trans.get((K) "D").contains(Integer.valueOf(4)));
        final MultiValuedMap<K, V> baseMap = new ArrayListValuedHashMap<>();
        final MultiValuedMap<K, V> transMap = TransformedMultiValuedMap.transformedMap(baseMap, null, (Transformer<? super V, ? extends V>) TransformedCollectionTest.STRING_TO_INTEGER_TRANSFORMER);
        assertEquals(0, transMap.size());
        transMap.put((K) "D", (V) "4");
        assertEquals(1, transMap.size());
        assertTrue(transMap.get((K) "D").contains(Integer.valueOf(4)));
    }
}
