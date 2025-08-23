package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.assertSame;

/**
 * Unit tests for the {@link SerializedString} class.
 */
public class SerializedStringTest {

    /**
     * Verifies that the {@code asQuotedChars()} method caches its result after the first call.
     * The {@link SerializedString} is designed to perform expensive serialization work only once
     * and then reuse the result. This test ensures that subsequent calls return the exact same
     * char array instance, confirming the caching mechanism is working as expected.
     */
    @Test
    public void asQuotedCharsShouldReturnCachedArrayOnSubsequentCalls() {
        // Arrange: Create a SerializedString instance.
        SerializedString serializedString = new SerializedString("a test string");

        // Act: Call the method twice to trigger and then use the cache.
        char[] firstCallResult = serializedString.asQuotedChars();
        char[] secondCallResult = serializedString.asQuotedChars();

        // Assert: Verify that the same object instance is returned on both calls.
        assertSame("Subsequent calls to asQuotedChars() should return the cached array instance",
                firstCallResult, secondCallResult);
    }
}