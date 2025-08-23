package org.apache.commons.collections4.sequence;

import org.junit.Test;

import java.util.ConcurrentModificationException;
import java.util.LinkedList;
import java.util.List;

/**
 * Contains tests for {@link SequencesComparator} focusing on its interaction with list modifications.
 */
public class SequencesComparator_ESTestTest10 { // Original class name retained

    /**
     * Tests that the SequencesComparator constructor throws a ConcurrentModificationException
     * if one of the provided lists is a sublist view, and its backing list is
     * structurally modified after the sublist is created. This is the expected
     * behavior of Java's sublist implementation, which the comparator must handle.
     */
    @Test(expected = ConcurrentModificationException.class, timeout = 4000)
    public void constructorShouldThrowCMEWhenBackingListOfSublistIsModified() {
        // Arrange: Create a list and a sublist view of it.
        final List<String> originalList = new LinkedList<>();
        final List<String> sublistView = originalList.subList(0, 0);

        // Act: Structurally modify the original list. This invalidates the sublist view,
        // making it "stale".
        originalList.add("a new element");

        // Assert: Instantiating the comparator will cause it to access the stale
        // sublist view, which is expected to throw a ConcurrentModificationException.
        new SequencesComparator<>(originalList, sublistView);
    }
}