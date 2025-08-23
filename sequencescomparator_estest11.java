package org.apache.commons.collections4.sequence;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.util.LinkedList;
import java.util.List;

/**
 * Contains tests for the {@link SequencesComparator} class.
 */
public class SequencesComparatorTest {

    @Test
    public void getScript_shouldCalculateCorrectModifications_forListsWithCircularReference() {
        // Arrange: Create two lists, listA and listB, that reference each other,
        // forming a circular dependency.
        final List<Object> listA = new LinkedList<>();
        final List<Object> listB = new LinkedList<>();
        final Object sharedObject = new Object();

        // listA will contain only listB.
        // listA: [ listB ]
        listA.add(listB);

        // listB will contain a shared object, listA, and the shared object again.
        // listB: [ sharedObject, listA, sharedObject ]
        listB.add(sharedObject);
        listB.add(listA);
        listB.add(sharedObject);

        // The comparison algorithm should handle this circular reference gracefully.
        // Since the lists have no equal elements, the transformation from listA to listB
        // requires deleting the single element of listA and inserting all three
        // elements of listB.
        //
        // 1. Delete listB from listA (1 modification)
        // 2. Insert sharedObject (1 modification)
        // 3. Insert listA (1 modification)
        // 4. Insert sharedObject (1 modification)
        // Total modifications = 1 delete + 3 inserts = 4.
        final int expectedModifications = 4;
        final SequencesComparator<Object> comparator = new SequencesComparator<>(listA, listB);

        // Act
        final EditScript<Object> editScript = comparator.getScript();

        // Assert
        assertEquals("The number of modifications should be the sum of deletions and insertions.",
                     expectedModifications, editScript.getModifications());
    }
}