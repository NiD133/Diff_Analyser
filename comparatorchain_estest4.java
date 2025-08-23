package org.apache.commons.collections4.comparators;

import org.junit.Test;
import java.util.Comparator;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

/**
 * This test suite contains tests for the {@link ComparatorChain} class.
 * This specific test focuses on the locking behavior of the chain.
 */
public class ComparatorChainLockingTest {

    /**
     * Tests that the ComparatorChain becomes locked after the compare() method is called for the first time.
     * A locked chain prevents any further modifications, such as adding new comparators.
     */
    @Test
    public void shouldBeLockedAfterFirstComparison() {
        // Arrange
        // A simple comparator is needed to create a valid, non-empty chain.
        @SuppressWarnings("unchecked") // Safe to suppress for a mock of a generic type
        final Comparator<Object> mockComparator = mock(Comparator.class);
        final ComparatorChain<Object> comparatorChain = new ComparatorChain<>(mockComparator);

        // Pre-condition: The chain should not be locked upon creation.
        assertFalse("ComparatorChain should not be locked before compare() is called.", comparatorChain.isLocked());

        // Act
        // Calling compare() should trigger the internal state to become "locked".
        // The actual objects being compared and the result are irrelevant for this test.
        comparatorChain.compare(new Object(), new Object());

        // Assert
        // The chain should now be locked.
        assertTrue("ComparatorChain should be locked after compare() has been called.", comparatorChain.isLocked());
    }
}