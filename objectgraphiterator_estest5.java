package org.apache.commons.collections4.iterators;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/**
 * Test class for ObjectGraphIterator.
 * This class contains a refactored test case for the findNextByIterator method.
 */
public class ObjectGraphIteratorTest {

    /**
     * Tests that calling findNextByIterator with an empty iterator does not find a
     * next element or alter the state of the iterators.
     */
    @Test
    public void findNextByIteratorWithEmptyIteratorShouldNotFindNextElement() {
        // Arrange: Create an ObjectGraphIterator initialized with an empty root iterator.
        List<String> emptyList = Collections.emptyList();
        Iterator<String> emptyRootIterator = emptyList.iterator();
        ObjectGraphIterator<String> graphIterator = new ObjectGraphIterator<>(emptyRootIterator);

        // Create another empty iterator to pass to the method under test.
        LinkedList<String> anotherEmptyList = new LinkedList<>();
        ListIterator<String> emptyIteratorToSearch = anotherEmptyList.listIterator();

        // Act: Attempt to find the next element using the empty iterator.
        // Note: findNextByIterator is a protected method, so this test must be
        // in the same package to access it.
        graphIterator.findNextByIterator(emptyIteratorToSearch);

        // Assert: Verify that the state of both iterators remains unchanged.
        // The main graph iterator should still report that it has no next element.
        assertFalse("The graph iterator should not have a next element after the search", graphIterator.hasNext());

        // The passed-in iterator should also remain at its beginning.
        assertFalse("The searched iterator should not have a next element", emptyIteratorToSearch.hasNext());
        assertFalse("The searched iterator should not have a previous element", emptyIteratorToSearch.hasPrevious());
    }
}