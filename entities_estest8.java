package org.jsoup.nodes;

import org.junit.Test;

/**
 * Test suite for the {@link Entities} class, focusing on exception handling.
 */
public class EntitiesTest {

    /**
     * Verifies that calling {@link Entities#unescape(String)} with a null input
     * correctly throws a {@link NullPointerException}. This is the expected behavior
     * for methods that do not explicitly support null arguments.
     */
    @Test(expected = NullPointerException.class)
    public void unescapeWithNullInputShouldThrowNullPointerException() {
        Entities.unescape(null);
    }
}