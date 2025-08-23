package org.apache.commons.collections4.sequence;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link SequencesComparator} class.
 * This class focuses on verifying the correctness of the generated {@link EditScript}.
 */
// The original test class name and scaffolding are kept to match the original context.
public class SequencesComparator_ESTestTest4 extends SequencesComparator_ESTest_scaffolding {

    /**
     * Tests that comparing two sequences where the second has one additional element
     * at the end results in an EditScript with exactly one modification.
     */
    @Test
    public void testGetScriptShouldReportOneModificationForSingleInsertionAtEnd() {
        // Arrange: Create two sequences, where the "after" sequence has one extra element.
        // Using strings makes the sequences' contents easy to understand at a glance.
        final List<String> sequenceBefore = Arrays.asList("A", "B");
        final List<String> sequenceAfter = Arrays.asList("A", "B", "C");

        final SequencesComparator<String> comparator = new SequencesComparator<>(sequenceBefore, sequenceAfter);

        // Act: Generate the edit script to transform the "before" sequence into the "after" one.
        final EditScript<String> script = comparator.getScript();

        // Assert: The script should report a single modification, which corresponds to the insertion of "C".
        assertEquals("Expected one modification for a single insertion", 1, script.getModifications());
    }
}