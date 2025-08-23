package org.apache.commons.collections4.sequence;

import org.junit.Test;

import java.util.ConcurrentModificationException;
import java.util.LinkedList;
import java.util.List;

/**
 * Test suite for {@link SequencesComparator}.
 * This class demonstrates how to test for exceptions when list preconditions are violated.
 */
public class SequencesComparatorUnderstandabilityTest {

    /**
     * Tests that getScript() throws a ConcurrentModificationException if one of the
     * sequences contains a sublist whose backing list was structurally modified
     * after the sublist was created.
     *
     * The {@link SequencesComparator} iterates over the provided lists to compare them.
     * If an element in one list is a sublist view, any attempt to access it (e.g., via
     * its equals() method) after its backing list has been modified will result in a
     * ConcurrentModificationException.
     */
    @Test(expected = ConcurrentModificationException.class)
    public void getScriptShouldThrowCMEWhenBackingListIsModified() {
        // --- Arrange ---

        // 1. Create a list that will serve as the backing list for a sublist.
        List<Object> backingList = new LinkedList<>();

        // 2. Create a sublist view of the backing list. At this point, the view is valid.
        List<Object> sublistView = backingList.subList(0, 0);

        // 3. Create the first sequence to be compared. This list contains the sublist
        //    view as one of its elements.
        List<Object> sequence1 = new LinkedList<>();
        sequence1.add(sublistView);

        // 4. Perform a structural modification on the original backing list.
        //    This action invalidates the 'sublistView'.
        backingList.add("structural modification");

        // 5. The second sequence for the comparator is the now-modified backing list.
        List<Object> sequence2 = backingList;

        // 6. Instantiate the comparator with the two sequences.
        SequencesComparator<Object> comparator = new SequencesComparator<>(sequence1, sequence2);

        // --- Act ---
        // Attempt to generate the edit script. This will require the comparator to
        // access the elements of both sequences, including the now-invalid sublistView.
        // This access is what triggers the expected ConcurrentModificationException.
        comparator.getScript();

        // --- Assert ---
        // The exception is expected and verified by the @Test(expected=...) annotation.
    }
}