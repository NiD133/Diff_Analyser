package org.apache.commons.collections4.bag;

import org.junit.Test;
import java.util.Comparator;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

// The test class name is kept for consistency with the original file structure.
public class TreeBag_ESTestTest7 extends TreeBag_ESTest_scaffolding {

    /**
     * Tests that adding two distinct objects to a TreeBag, which are considered
     * equal by its custom comparator, results in a single unique entry with a count of two.
     */
    @Test
    public void addShouldIncrementCountForElementsDeemedEqualByComparator() {
        // --- Arrange ---

        // Create a custom comparator that treats all objects as equal.
        // This is the key condition for this test case.
        @SuppressWarnings("unchecked")
        final Comparator<Object> allElementsAreEqualComparator = mock(Comparator.class);
        doReturn(0).when(allElementsAreEqualComparator).compare(any(), any());

        final TreeBag<Object> bag = new TreeBag<>(allElementsAreEqualComparator);
        final String firstElement = "element_A";
        final String secondElement = "element_B";

        // --- Act ---

        // Add the first element. The bag is modified, so this should return true.
        final boolean firstAddResult = bag.add(firstElement);

        // Add a second, different element. Because of the comparator, it will be
        // treated as an instance of the first element. The bag is still modified
        // (count is incremented), so this should also return true.
        final boolean secondAddResult = bag.add(secondElement);

        // --- Assert ---

        // 1. Verify the return values from the add calls.
        assertTrue("First add() call should return true as the bag was modified.", firstAddResult);
        assertTrue("Second add() call should return true as the element's count was incremented.", secondAddResult);

        // 2. Verify the state of the bag.
        assertEquals("Total size of the bag should be 2.", 2, bag.size());
        assertEquals("Bag should contain only 1 unique element.", 1, bag.uniqueSet().size());

        // 3. Verify the counts.
        // Since the comparator equates the two elements, the bag should report a count of 2
        // for the first element added, which is the one stored internally.
        assertEquals("The count of the first element should be 2.", 2, bag.getCount(firstElement));
        assertEquals("Querying the count of the second element should also return 2.", 2, bag.getCount(secondElement));
    }
}