package org.apache.commons.collections4.comparators;

import org.junit.Test;
import java.util.Comparator;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for {@link ComparatorChain}.
 */
public class ComparatorChainTest {

    /**
     * Tests that attempting to modify a ComparatorChain after a comparison has been
     * performed throws an UnsupportedOperationException.
     */
    @Test
    public void testSetComparatorOnLockedChainThrowsUnsupportedOperationException() {
        // Arrange: Create a comparator chain with a single, simple comparator.
        final Comparator<Integer> naturalOrderComparator = Comparator.naturalOrder();
        final ComparatorChain<Integer> comparatorChain = new ComparatorChain<>(naturalOrderComparator);

        // Act: Perform a comparison. According to the class Javadoc, this "locks"
        // the chain, making it immutable.
        comparatorChain.compare(10, 20);

        // Assert: Any subsequent attempt to modify the chain should fail.
        try {
            // We use a valid index (0) to ensure the test fails due to the locked
            // state, not an out-of-bounds error.
            comparatorChain.setComparator(0, naturalOrderComparator, true);
            fail("Expected an UnsupportedOperationException because the chain is locked.");
        } catch (final UnsupportedOperationException e) {
            // Verify that the correct exception was thrown with the expected message.
            final String expectedMessage = "Comparator ordering cannot be changed after the first comparison is performed";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}