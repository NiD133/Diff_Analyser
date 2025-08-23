package org.apache.commons.collections4.comparators;

import org.junit.Test;
import java.util.Comparator;

/**
 * Contains tests for the {@link ComparatorChain} class, focusing on exception handling.
 */
public class ComparatorChain_ESTestTest16 extends ComparatorChain_ESTest_scaffolding {

    /**
     * Tests that a NullPointerException is thrown when compare() is called on a
     * ComparatorChain that was initialized with a null Comparator.
     */
    @Test(expected = NullPointerException.class)
    public void compareShouldThrowNullPointerExceptionWhenChainContainsNullComparator() {
        // Arrange: Create a ComparatorChain with a single, null comparator.
        final Comparator<Object> nullComparator = null;
        final ComparatorChain<Object> chainWithNullComparator = new ComparatorChain<>(nullComparator);

        final Object object1 = new Object();
        final Object object2 = new Object();

        // Act: Attempt to compare two objects. This should throw a NullPointerException
        // because the chain will try to delegate the comparison to the null comparator.
        chainWithNullComparator.compare(object1, object2);

        // Assert: The test passes if a NullPointerException is thrown,
        // as specified by the @Test(expected) annotation.
    }
}