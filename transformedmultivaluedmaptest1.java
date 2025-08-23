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

public class TransformedMultiValuedMapTestTest1<K, V> extends AbstractMultiValuedMapTest<K, V> {

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
    void testFactory_Decorate() {
        final MultiValuedMap<K, V> base = new ArrayListValuedHashMap<>();
        base.put((K) "A", (V) "1");
        base.put((K) "B", (V) "2");
        base.put((K) "C", (V) "3");
        final MultiValuedMap<K, V> trans = TransformedMultiValuedMap.transformingMap(base, null, (Transformer<? super V, ? extends V>) TransformedCollectionTest.STRING_TO_INTEGER_TRANSFORMER);
        assertEquals(3, trans.size());
        assertTrue(trans.get((K) "A").contains("1"));
        assertTrue(trans.get((K) "B").contains("2"));
        assertTrue(trans.get((K) "C").contains("3"));
        trans.put((K) "D", (V) "4");
        assertTrue(trans.get((K) "D").contains(Integer.valueOf(4)));
    }
}
