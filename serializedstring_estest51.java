package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

/**
 * Unit tests for the {@link SerializedString} class, focusing on its caching behavior.
 */
public class SerializedStringTest {

    /**
     * Verifies that the {@code asQuotedUTF8()} method caches its result after the first call.
     * <p>
     * This test ensures that subsequent calls to the method return the exact same
     * byte array instance, confirming the intended lazy-initialization and performance optimization.
     */
    @Test
    public void asQuotedUTF8ShouldReturnCachedByteArrayOnSubsequentCalls() {
        // Arrange: Create a SerializedString instance with a sample value.
        SerializedString serializedString = new SerializedString("A test string");

        // Act: Call the method twice to trigger the caching mechanism.
        byte[] firstCallResult = serializedString.asQuotedUTF8();
        byte[] secondCallResult = serializedString.asQuotedUTF8();

        // Assert: Verify that the same instance is returned on both calls.
        assertNotNull("The result of the first call should not be null.", firstCallResult);
        
        // The core assertion: check that the second call returned the cached object instance.
        assertSame("Subsequent calls should return the same cached byte array instance.",
                firstCallResult, secondCallResult);
    }
}