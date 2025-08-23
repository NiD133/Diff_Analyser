package org.apache.commons.collections4.bag;

import org.apache.commons.collections4.SortedBag;
import org.junit.Test;

import java.util.NoSuchElementException;

/**
 * Unit tests for {@link TreeBag}.
 */
// The original class name 'TreeBag_ESTestTest17' is kept for context,
// but in a real-world scenario, it would be part of a single 'TreeBagTest' class.
public class TreeBag_ESTestTest17 {

    /**
     * Tests that calling last() on an empty TreeBag throws a NoSuchElementException.
     */
    @Test(expected = NoSuchElementException.class)
    public void lastShouldThrowNoSuchElementExceptionWhenBagIsEmpty() {
        // Arrange: Create an empty TreeBag.
        // Using the interface 'SortedBag' is a good practice.
        SortedBag<String> emptyBag = new TreeBag<>();

        // Act & Assert: Calling last() on the empty bag should throw the expected exception.
        emptyBag.last();
    }
}