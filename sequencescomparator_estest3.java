package org.apache.commons.collections4.sequence;

import org.apache.commons.collections4.Equator;
import org.apache.commons.collections4.functors.DefaultEquator;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link SequencesComparator} class.
 */
public class SequencesComparatorTest {

    /**
     * Tests that the comparison is based on the state of the lists at the time
     * getScript() is called, not at the time of the comparator's construction.
     * This confirms that the comparator uses live references to the lists.
     */
    @Test
    public void testComparisonUsesListStateAtTimeOfExecutionNotConstruction() {
        // Arrange
        // Create a single list instance that will be used for both sequences.
        List<Object> sequence = new LinkedList<>();
        sequence.add("initial element");

        // Instantiate the comparator. At this point, it holds a reference
        // to the list containing one element.
        SequencesComparator<Object> comparator = new SequencesComparator<>(sequence, sequence);

        // Act
        // Modify the list *after* the comparator has been constructed.
        sequence.add("added element");

        // Now, when getScript() is called, the comparator will see the modified list
        // for both the 'left' and 'right' sequences. It will be comparing
        // ["initial element", "added element"] to itself.
        EditScript<Object> script = comparator.getScript();

        // Assert
        // Since the comparator is effectively comparing a list to itself,
        // there should be no differences and therefore no modifications.
        final int expectedModifications = 0;
        assertEquals("There should be no modifications when comparing a list to itself, " +
                     "even if it was modified after the comparator was created.",
                     expectedModifications, script.getModifications());
    }
}