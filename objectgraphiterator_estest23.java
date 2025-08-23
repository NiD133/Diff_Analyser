package org.apache.commons.collections4.iterators;

import org.junit.Test;

import java.util.ConcurrentModificationException;
import java.util.LinkedList;
import java.util.ListIterator;

/**
 * Tests the behavior of {@link ObjectGraphIterator} when dealing with concurrent modifications.
 */
public class ObjectGraphIterator_ESTestTest23 {

    /**
     * Verifies that findNextByIterator throws a ConcurrentModificationException
     * if the underlying collection is modified after the iterator has been created.
     * This is the expected behavior of standard Java iterators.
     */
    @Test(expected = ConcurrentModificationException.class)
    public void findNextByIteratorShouldThrowExceptionOnConcurrentModification() {
        // Arrange: Create a list and an iterator for it.
        final LinkedList<Integer> list = new LinkedList<>();
        list.add(1);
        final ListIterator<Integer> iterator = list.listIterator();

        // The root and transformer are not relevant for this test, as we are directly
        // calling the protected method findNextByIterator.
        final ObjectGraphIterator<Integer> objectGraphIterator = new ObjectGraphIterator<>(null, null);

        // Act: Modify the list after the iterator has been created. This invalidates the iterator.
        list.add(2);

        // Assert: Calling a method that uses the invalidated iterator is expected
        // to throw a ConcurrentModificationException. The @Test(expected=...) annotation
        // handles the assertion.
        objectGraphIterator.findNextByIterator(iterator);
    }
}