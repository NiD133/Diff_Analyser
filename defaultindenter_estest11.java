package com.fasterxml.jackson.core.util;

import org.junit.Test;

/**
 * Unit tests for the {@link DefaultIndenter} class, focusing on its configuration methods.
 */
public class DefaultIndenterTest {

    /**
     * Verifies that the {@code withLinefeed} method throws a NullPointerException
     * when a null value is provided for the line feed string. A line feed string
     * is a required component and cannot be null.
     */
    @Test(expected = NullPointerException.class)
    public void withLinefeed_shouldThrowNullPointerException_whenLinefeedIsNull() {
        // The test uses a pre-existing static instance for simplicity.
        // The goal is to confirm that the method correctly rejects a null argument.
        DefaultIndenter.SYSTEM_LINEFEED_INSTANCE.withLinefeed(null);
    }
}