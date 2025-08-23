package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.nodes.Entities;

/**
 * This test suite is responsible for verifying the behavior of the {@link Entities} class.
 */
public class EntitiesTest {

    /**
     * Verifies that the unescape method throws a NullPointerException when the input string is null.
     * This is the expected behavior, as the method does not perform a null check before processing the input.
     */
    @Test(expected = NullPointerException.class)
    public void unescape_withNullInput_shouldThrowNullPointerException() {
        Entities.unescape(null);
    }
}