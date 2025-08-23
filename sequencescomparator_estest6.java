package org.apache.commons.collections4.sequence;

import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

/**
 * Test suite for {@link SequencesComparator}.
 * This class focuses on specific edge cases.
 */
// The original test extended a scaffolding class. We assume it's for setup/teardown
// and keep it for compatibility, though it's often better to make dependencies explicit.
public class SequencesComparator_ESTestTest6 extends SequencesComparator_ESTest_scaffolding {

    /**
     * Tests that getScript() throws a StackOverflowError when comparing two lists
     * that contain each other, creating a circular reference.
     *
     * <p>The comparison algorithm relies on the elements' .equals() method. For lists,
     * .equals() compares elements recursively. A circular reference will cause
     * infinite recursion, which should result in a StackOverflowError.</p>
     */
    @Test(timeout = 4000, expected = StackOverflowError.class)
    public void getScriptShouldThrowStackOverflowErrorForCircularlyReferencedLists() {
        // Arrange: Create two lists, listA and listB, where listA contains listB
        // and listB contains listA. This establishes a circular reference.
        List<Object> listA = new LinkedList<>();
        List<Object> listB = new LinkedList<>();
        listA.add(listB);
        listB.add(listA);

        SequencesComparator<Object> comparator = new SequencesComparator<>(listA, listB);

        // Act: Attempt to compute the difference script. This will trigger the
        // recursive .equals() calls between the lists.
        comparator.getScript();

        // Assert: The test expects a StackOverflowError, which is declared in the
        // @Test annotation's 'expected' attribute.
    }
}