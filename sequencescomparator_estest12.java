package org.apache.commons.collections4.sequence;

import org.junit.Test;
import java.util.LinkedList;
import java.util.List;
import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link SequencesComparator} class.
 */
public class SequencesComparatorTest {

    /**
     * Tests that the SequencesComparator reflects modifications made to the input lists
     * after the comparator has been instantiated. This is because the comparator
     * does not create a defensive copy of the lists and operates on the live references.
     */
    @Test
    public void getScriptShouldReflectModificationsMadeToSequenceAfterConstruction() {
        // Arrange
        // Define two simple sequences to be compared.
        List<String> originalSequence = new LinkedList<>();
        originalSequence.add("A");

        List<String> modifiedSequence = new LinkedList<>();
        modifiedSequence.add("A"); // Common element
        modifiedSequence.add("B");
        modifiedSequence.add("C");

        // Create the comparator. At this point, it holds direct references to the lists.
        SequencesComparator<String> comparator = new SequencesComparator<>(originalSequence, modifiedSequence);

        // Act
        // Modify the second sequence *after* the comparator has been created.
        // This is the behavior under test.
        modifiedSequence.add("D");
        modifiedSequence.add("E");

        // When getScript() is called, the comparator will see the final state of the lists:
        // originalSequence: ["A"]
        // modifiedSequence: ["A", "B", "C", "D", "E"]
        EditScript<String> script = comparator.getScript();

        // Assert
        // The comparison should result in one "keep" command for the common element "A"
        // and four "insert" commands for "B", "C", "D", and "E".
        // The total number of modifications (inserts + deletes) is therefore 4.
        final int expectedModifications = 4;
        assertEquals("The number of modifications should reflect changes made to the list after construction",
                     expectedModifications, script.getModifications());
    }
}