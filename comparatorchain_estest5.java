package org.apache.commons.collections4.comparators;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.util.BitSet;
import java.util.Comparator;
import java.util.List;
import org.junit.Test;

/**
 * Tests for {@link ComparatorChain}.
 */
public class ComparatorChainTest {

    /**
     * Tests that calling size() on a ComparatorChain constructed with a null list
     * throws a NullPointerException.
     */
    @Test
    public void sizeShouldThrowNullPointerExceptionWhenConstructedWithNullList() {
        // Arrange: Create a ComparatorChain with a null list of comparators.
        // The constructor accepts a null list, but methods that access it are expected to fail.
        final BitSet bitSet = new BitSet();
        final ComparatorChain<Object> chain = new ComparatorChain<>((List<Comparator<Object>>) null, bitSet);

        // Act & Assert: Verify that calling size() throws a NullPointerException.
        try {
            chain.size();
            fail("Expected a NullPointerException because the internal comparator list is null.");
        } catch (final NullPointerException e) {
            // This is the expected behavior.
            // We can also verify that the exception has no message, as was hinted
            // at in the original auto-generated test.
            assertNull("The NullPointerException should not have a message.", e.getMessage());
        }
    }
}