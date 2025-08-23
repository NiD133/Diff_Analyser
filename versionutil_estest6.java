package com.fasterxml.jackson.core.util;

import org.junit.Test;

/**
 * Unit tests for the {@link VersionUtil} class.
 */
public class VersionUtilTest {

    /**
     * Verifies that calling {@code parseVersionPart} with a null input
     * results in a {@code NullPointerException}. This is the expected behavior
     * as the method does not handle null arguments.
     */
    @Test(expected = NullPointerException.class)
    public void parseVersionPart_withNullInput_shouldThrowNullPointerException() {
        // Act: Call the method under test with a null argument.
        VersionUtil.parseVersionPart(null);

        // Assert: The @Test(expected) annotation handles the verification that
        // a NullPointerException was thrown.
    }
}