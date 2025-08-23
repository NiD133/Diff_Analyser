package org.apache.commons.collections4.comparators;

import org.junit.Test;

import java.util.BitSet;
import java.util.Comparator;
import java.util.List;

import static org.junit.Assert.fail;

/**
 * Contains tests for the {@link ComparatorChain} class.
 */
public class ComparatorChainTest {

    /**
     * Tests that calling setComparator() on a ComparatorChain initialized with a null
     * list of comparators throws a NullPointerException.
     * <p>
     * The constructor {@link ComparatorChain#ComparatorChain(List, BitSet)} allows a null
     * list to be passed, but any subsequent attempt to modify the chain's internal
     * state is expected to fail.
     */
    @Test
    public void setComparatorOnChainConstructedWithNullListShouldThrowNPE() {
        // Arrange: Create a ComparatorChain with a null internal list of comparators.
        final List<Comparator<Object>> nullComparatorList = null;
        final ComparatorChain<Object> chain = new ComparatorChain<>(nullComparatorList, new BitSet());

        // Act & Assert: Attempting to set a comparator should throw a NullPointerException
        // because the method tries to operate on the null internal list.
        try {
            // The index, comparator, and reverse flag are arbitrary, as the call will
            // fail before they are meaningfully used.
            chain.setComparator(0, (o1, o2) -> 0, false);
            fail("Expected a NullPointerException to be thrown due to the null internal list.");
        } catch (final NullPointerException e) {
            // This is the expected behavior. The test passes.
        }
    }
}