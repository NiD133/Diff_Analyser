package org.jsoup.nodes;

import org.junit.Test;

/**
 * Tests for the {@link Entities} class, focusing on edge cases and exception handling.
 */
public class EntitiesTest {

    /**
     * Verifies that findPrefix() throws a NullPointerException when given a null input.
     * The method is not designed to handle nulls, so this exception is the expected behavior.
     */
    @Test(expected = NullPointerException.class)
    public void findPrefixShouldThrowNullPointerExceptionForNullInput() {
        Entities.findPrefix(null);
    }
}