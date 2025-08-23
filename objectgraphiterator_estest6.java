package org.apache.commons.collections4.iterators;

import org.junit.Test;

import java.util.ConcurrentModificationException;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/**
 * Contains test cases for the ObjectGraphIterator, focusing on its interaction
 * with underlying iterators, especially regarding fail-fast behavior.
 */
// The original test class name and hierarchy are preserved.
public class ObjectGraphIterator_ESTestTest6 extends ObjectGraphIterator_ESTest_scaffolding {

    /**
     * Tests that the ObjectGraphIterator correctly propagates a ConcurrentModificationException
     * from its underlying iterator if the source collection is modified after the
     * iterator has been created.
     */
    @Test(expected = ConcurrentModificationException.class)
    public void nextShouldThrowConcurrentModificationExceptionWhenUnderlyingCollectionIsModified() {
        // Arrange: Create a list, obtain a "fail-fast" iterator from it,
        // and then wrap it with an ObjectGraphIterator.
        final List<Integer> underlyingList = new LinkedList<>();
        underlyingList.add(100);

        final ListIterator<Integer> failFastIterator = underlyingList.listIterator();
        final ObjectGraphIterator<Integer> graphIterator = new ObjectGraphIterator<>(failFastIterator);

        // Act: Modify the underlying list directly. This action invalidates the
        // failFastIterator, which should cause subsequent operations to fail.
        underlyingList.add(200);

        // Assert: Calling next() on the graphIterator is expected to trigger the
        // ConcurrentModificationException from the underlying iterator.
        // The exception is verified by the @Test(expected=...) annotation.
        graphIterator.next();
    }
}