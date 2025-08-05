package org.apache.commons.collections4.bag;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.apache.commons.collections4.Bag;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.collection.TransformedCollectionTest;
import org.junit.jupiter.api.Test;

/**
 * Test suite for the {@link TransformedBag} implementation.
 */
public class TransformedBagTest<T> extends AbstractBagTest<T> {

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
        return TransformedBag.transformingBag(new HashBag<>(),
                (Transformer<T, T>) TransformedCollectionTest.NOOP_TRANSFORMER);
    }

    @Test
    @SuppressWarnings("unchecked")
    void testEmptyTransformedBag() {
        Bag<T> transformedBag = TransformedBag.transformingBag(new HashBag<>(),
                (Transformer<T, T>) TransformedCollectionTest.STRING_TO_INTEGER_TRANSFORMER);
        assertTrue(transformedBag.isEmpty());

        Object[] elements = {"1", "3", "5", "7", "2", "4", "6"};
        for (int i = 0; i < elements.length; i++) {
            transformedBag.add((T) elements[i]);
            assertEquals(i + 1, transformedBag.size());
            assertTrue(transformedBag.contains(Integer.valueOf((String) elements[i])));
            assertFalse(transformedBag.contains(elements[i]));
        }

        assertFalse(transformedBag.remove(elements[0]));
        assertTrue(transformedBag.remove(Integer.valueOf((String) elements[0])));
    }

    @Test
    @SuppressWarnings("unchecked")
    void testDecorateAndTransformExistingBag() {
        Bag<T> originalBag = new HashBag<>();
        Object[] elements = {"1", "3", "5", "7", "2", "4", "6"};
        for (Object element : elements) {
            originalBag.add((T) element);
        }

        Bag<T> transformedBag = TransformedBag.transformedBag(originalBag,
                (Transformer<T, T>) TransformedCollectionTest.STRING_TO_INTEGER_TRANSFORMER);
        assertEquals(elements.length, transformedBag.size());

        for (Object element : elements) {
            assertTrue(transformedBag.contains(Integer.valueOf((String) element)));
            assertFalse(transformedBag.contains(element));
        }

        assertFalse(transformedBag.remove(elements[0]));
        assertTrue(transformedBag.remove(Integer.valueOf((String) elements[0])));
    }
}