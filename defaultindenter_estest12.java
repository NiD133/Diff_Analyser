package com.fasterxml.jackson.core.util;

import org.junit.Test;

/**
 * Unit tests for the {@link DefaultIndenter} class.
 */
public class DefaultIndenterTest {

    /**
     * Verifies that the withIndent() method throws a NullPointerException
     * when a null indent string is provided, as this is an invalid configuration.
     */
    @Test(expected = NullPointerException.class)
    public void withIndent_whenGivenNull_shouldThrowNullPointerException() {
        // Arrange: Create a standard indenter instance.
        DefaultIndenter indenter = new DefaultIndenter();

        // Act & Assert: Calling withIndent(null) should immediately throw.
        // The @Test(expected) annotation handles the assertion, failing the test
        // if a NullPointerException is not thrown.
        indenter.withIndent(null);
    }
}