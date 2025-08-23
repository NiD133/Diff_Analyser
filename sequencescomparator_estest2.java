package org.apache.commons.collections4.sequence;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Contains tests for the {@link SequencesComparator} class.
 */
public class SequencesComparatorTest {

    /**
     * Tests that the generated EditScript has the correct number of modifications
     * when comparing two sequences that have no elements in common.
     * <p>
     * In this scenario, the total number of modifications should be the sum of the
     * lengths of the two sequences (i.e., all elements from the first sequence
     * are deleted, and all elements from the second sequence are inserted).
     */
    @Test
    public void getScriptShouldReturnCorrectModificationCountForDisjointSequences() {
        // Arrange: Create two distinct sequences with no common elements.
        final List<String> sequenceA = Arrays.asList("A", "B", "C", "D");
        final List<String> sequenceB = Collections.singletonList("E");

        final SequencesComparator<String> comparator = new SequencesComparator<>(sequenceA, sequenceB);

        // Act: Generate the edit script to transform sequenceA into sequenceB.
        final EditScript<String> script = comparator.getScript();

        // Assert: The total modifications should be 4 deletions and 1 insertion.
        final int expectedModifications = sequenceA.size() + sequenceB.size(); // 4 + 1 = 5
        assertEquals(expectedModifications, script.getModifications());
    }
}