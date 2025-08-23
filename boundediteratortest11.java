package org.apache.commons.collections4.iterators;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link BoundedIterator}.
 * Note: The original class name "BoundedIteratorTestTest11" was simplified to "BoundedIteratorTest"
 * to remove redundancy and improve clarity.
 */
public class BoundedIteratorTest<E> extends AbstractIteratorTest<E> {

    private final String[] testArray = {"a", "b", "c", "d", "e", "f", "g"};
    private List<E> testList;

    @Override
    public Iterator<E> makeEmptyIterator() {
        return new BoundedIterator<>(Collections.<E>emptyList().iterator(), 0, 10);
    }

    @Override
    public Iterator<E> makeObject() {
        // BoundedIterator that skips the first element and includes the rest.
        return new BoundedIterator<>(new ArrayList<>(testList).iterator(), 1, testList.size() - 1);
    }

    @SuppressWarnings("unchecked")
    @BeforeEach
    public void setUp() {
        testList = Arrays.asList((E[]) testArray);
    }

    /**
     * Tests that BoundedIterator correctly propagates an UnsupportedOperationException
     * from the decorated iterator's remove() method.
     */
    @Test
    void removeShouldPropagateExceptionWhenDecoratedIteratorIsUnmodifiable() {
        // Arrange
        // 1. Create a decorated iterator that does not support the remove() operation.
        final Iterator<E> unmodifiableIterator = new AbstractIteratorDecorator<E>(testList.iterator()) {
            @Override
            public void remove() {
                throw new UnsupportedOperationException("remove() is not supported on the decorated iterator");
            }
        };

        // 2. Create the BoundedIterator instance to test.
        // It will skip 1 element ("a") and is bounded to a max of 5 elements.
        final Iterator<E> boundedIterator = new BoundedIterator<>(unmodifiableIterator, 1, 5);

        // 3. Advance the iterator past the first element to enable the remove() call.
        assertTrue(boundedIterator.hasNext(), "Iterator should have elements before calling next()");
        assertEquals(testList.get(1), boundedIterator.next(), "Iterator should return the second element of the list ('b')");

        // Act & Assert
        // Check that calling remove() on the BoundedIterator propagates the exception from the decorated iterator.
        final UnsupportedOperationException thrown = assertThrows(
            UnsupportedOperationException.class,
            boundedIterator::remove,
            "BoundedIterator should throw UnsupportedOperationException."
        );

        // Verify that the propagated exception is the one we expect.
        assertEquals("remove() is not supported on the decorated iterator", thrown.getMessage(),
            "The exception message should be propagated from the decorated iterator.");
    }
}