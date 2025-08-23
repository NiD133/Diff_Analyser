package org.apache.commons.collections4.iterators;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests the constructor of {@link BoundedIterator}, particularly its argument validation.
 * <p>
 * This class also provides a concrete implementation for {@link AbstractIteratorTest}
 * to verify the general iterator contract of {@link BoundedIterator}.
 * </p>
 *
 * @param <E> the type of element in the iterator.
 */
public class BoundedIteratorConstructorTest<E> extends AbstractIteratorTest<E> {

    private final String[] sourceArray = {"a", "b", "c", "d", "e", "f", "g"};

    private List<E> sourceList;

    @Override
    public Iterator<E> makeEmptyIterator() {
        // A BoundedIterator over an empty iterator should also be empty.
        return new BoundedIterator<>(Collections.<E>emptyList().iterator(), 0, 10);
    }

    @Override
    public Iterator<E> makeObject() {
        // Creates a BoundedIterator that skips the first element and includes the rest.
        final long offset = 1;
        final long maxElements = sourceList.size() - offset;
        return new BoundedIterator<>(new ArrayList<>(sourceList).iterator(), offset, maxElements);
    }

    @SuppressWarnings("unchecked")
    @BeforeEach
    public void setUp() {
        sourceList = Arrays.asList((E[]) sourceArray);
    }

    /**
     * Tests that the constructor throws an IllegalArgumentException
     * when the offset parameter is negative.
     */
    @Test
    void constructorShouldThrowExceptionWhenOffsetIsNegative() {
        final long negativeOffset = -1;
        final long anyValidMax = 4;
        // The content of the iterator is irrelevant for this constructor check.
        final Iterator<E> dummyIterator = Collections.emptyIterator();

        final IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> new BoundedIterator<>(dummyIterator, negativeOffset, anyValidMax)
        );

        assertEquals("Offset parameter must not be negative.", thrown.getMessage());
    }
}