package org.jsoup.nodes;

import org.junit.Test;

/**
 * Test suite for the {@link Entities} class, focusing on its static utility methods.
 */
public class EntitiesTest {

    /**
     * Verifies that the {@code isNamedEntity} method throws a {@link NullPointerException}
     * when passed a null input. This is the expected behavior, as the method contract
     * does not support null arguments.
     */
    @Test(expected = NullPointerException.class)
    public void isNamedEntityShouldThrowNullPointerExceptionForNullInput() {
        Entities.isNamedEntity(null);
    }
}