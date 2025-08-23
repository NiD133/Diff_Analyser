package org.apache.commons.collections4.comparators;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Comparator;
import org.junit.Test;

/**
 * Tests for {@link ComparatorChain#setComparator(int, Comparator, boolean)}.
 */
public class ComparatorChainTest {

    /**
     * Tests that calling setComparator() on an empty chain throws an IndexOutOfBoundsException.
     * The method should fail because index 0 is out of bounds for a chain of size 0.
     */
    @Test
    public void setComparatorOnEmptyChainShouldThrowIndexOutOfBoundsException() {
        // Arrange: Create an empty ComparatorChain.
        final ComparatorChain<Object> emptyChain = new ComparatorChain<>();
        
        // A dummy comparator to pass to the method. Its behavior is irrelevant for this test.
        final Comparator<Object> dummyComparator = (o1, o2) -> 0;

        // Act & Assert: Attempt to set a comparator at an invalid index.
        try {
            emptyChain.setComparator(0, dummyComparator, false);
            fail("Expected an IndexOutOfBoundsException to be thrown, but it was not.");
        } catch (final IndexOutOfBoundsException e) {
            // Verify that the exception was thrown with the expected, informative message.
            final String expectedMessage = "Index: 0, Size: 0";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}