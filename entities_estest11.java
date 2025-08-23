package org.jsoup.nodes;

import org.junit.Test;

/**
 * Tests for the {@link Entities} class, focusing on its exception-handling capabilities.
 */
public class EntitiesTest {

    /**
     * Verifies that calling {@link Entities#getByName(String)} with a null input
     * correctly throws a {@link NullPointerException}. This test ensures the method
     * adheres to the common contract of rejecting null arguments when not explicitly
     * designed to handle them.
     */
    @Test(expected = NullPointerException.class)
    public void getByNameShouldThrowNullPointerExceptionForNullInput() {
        Entities.getByName(null);
    }
}