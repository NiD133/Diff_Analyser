package org.apache.commons.collections4.sequence;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.util.LinkedList;
import java.util.List;

/**
 * Contains tests for {@link SequencesComparator}.
 * This refactored test focuses on the understandability of a specific test case.
 */
public class SequencesComparator_ESTestTest14 {

    /**
     * Tests that the comparator correctly identifies the number of modifications (insertions)
     * required to transform a shorter sequence into a longer one.
     */
    @Test
    public void getScriptShouldReturnCorrectModificationCountForTwoInsertions() {
        // Arrange: Define two sequences. The 'to' sequence is created by
        // keeping the 'from' sequence and appending two new elements.
        // from: ["A"]
        // to:   ["A", "B", "C"]
        final List<String> fromSequence = new LinkedList<>(List.of("A"));
        final List<String> toSequence = new LinkedList<>(List.of("A", "B", "C"));

        // Act: Generate the edit script by comparing the two sequences.
        final SequencesComparator<String> comparator = new SequencesComparator<>(fromSequence, toSequence);
        final EditScript<String> script = comparator.getScript();

        // Assert: The transformation requires keeping one element ("A") and inserting two ("B", "C").
        // The number of modifications (inserts + deletes) should therefore be 2.
        final int expectedModifications = 2;
        assertEquals("The number of modifications should be 2 for two insertions.",
                     expectedModifications, script.getModifications());
    }
}