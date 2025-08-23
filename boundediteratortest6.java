package org.apache.commons.collections4.iterators;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link BoundedIterator}.
 * This class focuses on specific edge cases and behaviors.
 */
// Class name improved for clarity.
// Generic <E> replaced with concrete <String> for type safety and readability.
public class BoundedIteratorTest extends AbstractIteratorTest<String> {

    // Renamed for clarity and made static final as it's a constant.
    private static final String[] SOURCE_DATA = { "a", "b", "c", "d", "e", "f", "g" };

    // Renamed for consistency.
    private List<String> sourceList;

    @Override
    public Iterator<String> makeEmptyIterator() {
        return new BoundedIterator<>(Collections.<String>emptyList().iterator(), 0, 10);
    }

    @Override
    public Iterator<String> makeObject() {
        // Use the consistently named sourceList.
        return new BoundedIterator<>(new ArrayList<>(sourceList).iterator(), 1, sourceList.size() - 1);
    }

    @BeforeEach
    public void setUp() {
        // Simplified initialization without the unsafe cast.
        sourceList = Arrays.asList(SOURCE_DATA);
    }

    /**
     * Tests that the iterator is empty if the offset is greater than the
     * size of the decorated iterator.
     */
    @Test
    void whenOffsetIsGreaterThanSourceSize_thenIteratorIsEmpty() {
        // Arrange: Use descriptive constants and make the offset relative to the
        // source size to make the test's intent clear.
        final long offset = sourceList.size() + 3L; // An offset (10) clearly larger than the list size (7)
        final long maxElements = 4L;
        final Iterator<String> boundedIterator = new BoundedIterator<>(sourceList.iterator(), offset, maxElements);

        // Act & Assert: Verify the iterator behaves as if it's exhausted.
        assertFalse(boundedIterator.hasNext(), "Iterator should be empty when offset exceeds source size.");

        assertThrows(NoSuchElementException.class,
                boundedIterator::next,
                "next() should throw NoSuchElementException for an exhausted iterator.");
    }
}