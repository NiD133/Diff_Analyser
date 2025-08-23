package org.apache.commons.collections4.iterators;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BoundedIteratorTestTest11<E> extends AbstractIteratorTest<E> {

    /**
     * Test array of size 7
     */
    private final String[] testArray = { "a", "b", "c", "d", "e", "f", "g" };

    private List<E> testList;

    @Override
    public Iterator<E> makeEmptyIterator() {
        return new BoundedIterator<>(Collections.<E>emptyList().iterator(), 0, 10);
    }

    @Override
    public Iterator<E> makeObject() {
        return new BoundedIterator<>(new ArrayList<>(testList).iterator(), 1, testList.size() - 1);
    }

    @SuppressWarnings("unchecked")
    @BeforeEach
    public void setUp() throws Exception {
        testList = Arrays.asList((E[]) testArray);
    }

    /**
     * Test the case if the decorated iterator does not support the
     * {@code remove()} method and throws an {@link UnsupportedOperationException}.
     */
    @Test
    void testRemoveUnsupported() {
        final Iterator<E> mockIterator = new AbstractIteratorDecorator<E>(testList.iterator()) {

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
        final Iterator<E> iter = new BoundedIterator<>(mockIterator, 1, 5);
        assertTrue(iter.hasNext());
        assertEquals("b", iter.next());
        final UnsupportedOperationException thrown = assertThrows(UnsupportedOperationException.class, () -> iter.remove());
        assertNull(thrown.getMessage());
    }
}
