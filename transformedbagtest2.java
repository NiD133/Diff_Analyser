package org.apache.commons.collections4.bag;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.apache.commons.collections4.Bag;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.collection.TransformedCollectionTest;
import org.junit.jupiter.api.Test;

public class TransformedBagTestTest2<T> extends AbstractBagTest<T> {

    @Override
    public String getCompatibilityVersion() {
        return "4";
    }

    @Override
    protected int getIterationBehaviour() {
        return UNORDERED;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Bag<T> makeObject() {
        return TransformedBag.transformingBag(new HashBag<>(), (Transformer<T, T>) TransformedCollectionTest.NOOP_TRANSFORMER);
    }

    @Test
    @SuppressWarnings("unchecked")
    void testTransformedBag_decorateTransform() {
        final Bag<T> originalBag = new HashBag<>();
        final Object[] els = { "1", "3", "5", "7", "2", "4", "6" };
        for (final Object el : els) {
            originalBag.add((T) el);
        }
        final Bag<T> bag = TransformedBag.transformedBag(originalBag, (Transformer<T, T>) TransformedCollectionTest.STRING_TO_INTEGER_TRANSFORMER);
        assertEquals(els.length, bag.size());
        for (final Object el : els) {
            assertTrue(bag.contains(Integer.valueOf((String) el)));
            assertFalse(bag.contains(el));
        }
        assertFalse(bag.remove(els[0]));
        assertTrue(bag.remove(Integer.valueOf((String) els[0])));
    }
}
