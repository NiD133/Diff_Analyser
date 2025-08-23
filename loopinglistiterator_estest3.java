package org.apache.commons.collections4.iterators;

import org.junit.Test;
import java.util.List;
import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link LoopingListIterator}.
 */
public class LoopingListIteratorTest {

    /**
     * Tests that calling previousIndex() on a new iterator, which is positioned
     * at the beginning of the list, correctly "loops around" and returns the
     * index of the last element.
     */
    @Test
    public void previousIndex_atStartOfList_returnsLastIndex() {
        // Arrange: Create a list and a LoopingListIterator for it.
        // The iterator is positioned before the first element by default.
        List<String> underlyingList = List.of("A", "B", "C");
        LoopingListIterator<String> loopingIterator = new LoopingListIterator<>(underlyingList);

        // The expected index for a 'previous' operation is the last element's index.
        int expectedIndex = underlyingList.size() - 1; // Should be 2

        // Act: Get the previous index from the newly created iterator.
        int actualIndex = loopingIterator.previousIndex();

        // Assert: Verify that the returned index is indeed the last index of the list.
        assertEquals("When at the beginning, previousIndex() should loop to the last index.",
                     expectedIndex, actualIndex);
    }
}