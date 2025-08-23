package org.apache.commons.collections4.iterators;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link BoundedIterator}.
 * <p>
 * This class focuses on specific behaviors of the BoundedIterator,
 * complementing the inherited tests from {@link AbstractIteratorTest}.
 * </p>
 */
public class BoundedIteratorTest<E> extends AbstractIteratorTest<E> {

    private static final String[] SOURCE_ARRAY = { "a", "b", "c", "d", "e", "f", "g" };

    private List<E> sourceList;

    @Override
    public Iterator<E> makeEmptyIterator() {
        // BoundedIterator over an empty source, with arbitrary non-zero bounds.
        return new BoundedIterator<>(Collections.<E>emptyList().iterator(), 0, 10);
    }

    @Override
    public Iterator<E> makeObject() {
        // Creates an iterator that is bounded to a sub-section of the source list.
        // It will skip the first element and iterate over the next (size - 1) elements.
        final int offset = 1;
        final int maxElements = sourceList.size() - 1;
        return new BoundedIterator<>(new ArrayList<>(sourceList).iterator(), offset, maxElements);
    }

    @BeforeEach
    @SuppressWarnings("unchecked")
    public void setUp() {
        // This unchecked cast is necessary because generics are erased at runtime,
        // and we cannot create a generic array new E[].
        sourceList = Arrays.asList((E[]) SOURCE_ARRAY);
    }

    @Test
    @DisplayName("remove() should throw IllegalStateException if next() has not been called")
    void removeShouldThrowExceptionWhenCalledBeforeNext() {
        // Arrange
        final List<E> listForTesting = new ArrayList<>(sourceList);
        // Create an iterator with an offset, which advances the underlying iterator internally.
        final Iterator<E> boundedIterator = new BoundedIterator<>(listForTesting.iterator(), 1, 5);

        // Act & Assert
        // The BoundedIterator's contract states that remove() is illegal before a call to next(),
        // even if the underlying iterator was advanced during construction (due to the offset).
        final IllegalStateException thrown = assertThrows(
            IllegalStateException.class,
            () -> boundedIterator.remove(),
            "Calling remove() before next() should throw IllegalStateException."
        );

        assertEquals("remove() cannot be called before calling next()", thrown.getMessage(),
            "The exception message should match the expected contract.");
    }
}