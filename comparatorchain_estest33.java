package org.apache.commons.collections4.comparators;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for {@link ComparatorChain}.
 */
public class ComparatorChainTest {

    /**
     * Tests the reflexive property of the equals() method.
     * An object must always be equal to itself.
     */
    @Test
    public void testEquals_returnsTrue_forSameInstance() {
        // Arrange: Create an empty comparator chain.
        // The no-arg constructor is used for simplicity.
        final ComparatorChain<Object> chain = new ComparatorChain<>();

        // Act & Assert: Verify that the instance is equal to itself.
        // This fulfills the reflexive contract of Object.equals().
        assertTrue("An instance of ComparatorChain should be equal to itself.", chain.equals(chain));
    }
}