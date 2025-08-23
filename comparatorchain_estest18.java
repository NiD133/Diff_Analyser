package org.apache.commons.collections4.comparators;

import org.junit.Test;

import java.util.Comparator;
import java.util.List;

/**
 * Tests for {@link ComparatorChain}.
 */
public class ComparatorChainTest {

    /**
     * Tests that the constructor throws a NullPointerException when initialized with a null list of comparators.
     */
    @Test(expected = NullPointerException.class)
    public void constructorWithNullListThrowsNullPointerException() {
        // When: a ComparatorChain is constructed with a null list
        // Then: a NullPointerException should be thrown.
        new ComparatorChain<>((List<Comparator<Object>>) null);
    }
}