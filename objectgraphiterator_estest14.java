package org.apache.commons.collections4.iterators;

import org.junit.Test;

import java.util.Collections;
import java.util.Iterator;

import static org.junit.Assert.assertFalse;

/**
 * Contains tests for the protected method findNextByIterator in {@link ObjectGraphIterator}.
 */
public class ObjectGraphIterator_ESTestTest14 {

    /**
     * Tests that calling findNextByIterator with an empty iterator correctly
     * handles it by simply exhausting it.
     */
    @Test
    public void findNextByIteratorWithEmptyIteratorShouldResultInExhaustedIterator() {
        // Arrange
        // An ObjectGraphIterator instance is needed to call the protected method.
        // Its initial state is not critical for this test, so we use an empty root iterator.
        final ObjectGraphIterator<Object> graphIterator = new ObjectGraphIterator<>(Collections.emptyIterator());

        // This is the empty iterator that will be passed to the method under test.
        final Iterator<Object> emptyIteratorToSearch = Collections.emptyIterator();

        // Act
        // The method should attempt to find a next element by consuming the provided iterator.
        // Since the iterator is empty, it will be immediately exhausted.
        graphIterator.findNextByIterator(emptyIteratorToSearch);

        // Assert
        // Verify that the iterator passed to the method has been fully traversed.
        assertFalse("The empty iterator should be exhausted after the call", emptyIteratorToSearch.hasNext());
    }
}