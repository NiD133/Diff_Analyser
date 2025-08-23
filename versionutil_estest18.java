package com.fasterxml.jackson.core.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for the utility methods in {@link VersionUtil}.
 */
public class VersionUtilTest {

    /**
     * Verifies that calling {@code VersionUtil.throwInternal()} always throws a
     * {@link RuntimeException} with a specific, predefined message. This method
     * is designed to signal an illegal state that should never be reached.
     */
    @Test
    public void throwInternal_shouldAlwaysThrowRuntimeExceptionWithSpecificMessage() {
        // Arrange
        String expectedMessage = "Internal error: this code path should never get executed";

        try {
            // Act
            VersionUtil.throwInternal();

            // Assert: Fail the test if the method does not throw an exception as expected.
            fail("Expected VersionUtil.throwInternal() to throw a RuntimeException, but it did not.");
        } catch (RuntimeException e) {
            // Assert: Verify that the thrown exception has the correct message.
            assertEquals("The exception message does not match the expected value.",
                         expectedMessage, e.getMessage());
        }
    }
}