package org.apache.commons.collections4.comparators;

import static org.junit.Assert.assertThrows;

import java.util.BitSet;
import java.util.Comparator;
import java.util.List;
import org.junit.Test;

/**
 * Contains tests for exceptional behavior in the {@link ComparatorChain} class.
 */
public class ComparatorChainTest {

    /**
     * Tests that calling setComparator() on a chain initialized with a null
     * comparator list throws a NullPointerException.
     *
     * The constructor {@code ComparatorChain(List, BitSet)} accepts a null list,
     * but any subsequent attempt to modify that list should fail, as the class
     * does not create a defensive copy.
     */
    @Test
    public void setComparatorOnChainWithNullListShouldThrowNullPointerException() {
        // Arrange: Create a ComparatorChain, passing a null list to its constructor.
        final List<Comparator<String>> nullComparatorList = null;
        final ComparatorChain<String> chain = new ComparatorChain<>(nullComparatorList, new BitSet());

        // Act & Assert: Verify that attempting to set a comparator on the null internal
        // list results in a NullPointerException. The index and comparator are arbitrary.
        assertThrows(NullPointerException.class, () -> {
            chain.setComparator(0, Comparator.naturalOrder());
        });
    }
}