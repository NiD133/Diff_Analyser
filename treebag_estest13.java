package org.apache.commons.collections4.bag;

import org.junit.Test;

/**
 * Unit tests for {@link TreeBag}.
 */
public class TreeBagTest {

    /**
     * Tests that adding a null element to a TreeBag throws a NullPointerException,
     * as the underlying TreeMap does not permit null keys by default.
     */
    @Test(expected = NullPointerException.class)
    public void add_withNullObject_shouldThrowNullPointerException() {
        // Given: An empty TreeBag that uses natural ordering.
        final TreeBag<String> treeBag = new TreeBag<>();

        // When: A null element is added.
        treeBag.add(null);

        // Then: A NullPointerException is expected.
    }
}