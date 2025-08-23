package org.jsoup.nodes;

import org.junit.Test;

/**
 * Tests for the {@link Entities#unescape(String, boolean)} method.
 */
public class EntitiesTest {

    /**
     * Verifies that calling unescape with a null input string throws a NullPointerException.
     */
    @Test(expected = NullPointerException.class)
    public void unescapeWithNullInputShouldThrowNullPointerException() {
        // The 'strict' parameter (false in this case) is irrelevant;
        // the method should perform a null check before any other processing.
        Entities.unescape(null, false);
    }
}