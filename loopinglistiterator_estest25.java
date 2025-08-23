package org.apache.commons.collections4.iterators;

import org.junit.Test;
import static org.junit.Assert.assertNull;

import java.util.LinkedList;
import java.util.List;

/**
 * Contains tests for the {@link LoopingListIterator}.
 */
public class LoopingListIteratorTest {

    /**
     * Tests that the iterator can correctly handle and return a null element
     * from the underlying list.
     */
    @Test
    public void testNextShouldReturnNullWhenListContainsNull() {
        // Arrange: Create a list containing a single null element.
        final List<Integer> listWithNull = new LinkedList<>();
        listWithNull.add(null);
        
        final LoopingListIterator<Integer> iterator = new LoopingListIterator<>(listWithNull);

        // Act: Retrieve the first element from the iterator.
        final Integer firstElement = iterator.next();

        // Assert: The retrieved element should be the null that was added.
        assertNull("The iterator should return the null element present in the list.", firstElement);
    }
}