package com.fasterxml.jackson.core.io;

import org.junit.Test;
import java.nio.charset.StandardCharsets;
import static org.junit.Assert.*;

/**
 * Contains tests for the {@link SerializedString} class, focusing on its caching behavior.
 */
public class SerializedStringTest {

    /**
     * Verifies that the {@code asUnquotedUTF8()} method caches its result.
     * The test ensures that subsequent calls to the method return the exact same
     * byte array instance, which confirms the lazy-initialization and caching mechanism is working correctly.
     */
    @Test
    public void asUnquotedUTF8_whenCalledMultipleTimes_returnsSameCachedInstance() {
        // Arrange
        final String originalValue = "test-string-for-caching";
        SerializedString serializedString = new SerializedString(originalValue);

        // Act
        // The first call computes and caches the UTF-8 byte array.
        byte[] firstResult = serializedString.asUnquotedUTF8();
        // The second call should retrieve the value from the cache.
        byte[] secondResult = serializedString.asUnquotedUTF8();

        // Assert
        // 1. Verify the initial result is correct and not null.
        assertNotNull("The byte array should not be null", firstResult);
        assertArrayEquals("The byte array content should match the original string's UTF-8 representation",
                originalValue.getBytes(StandardCharsets.UTF_8), firstResult);

        // 2. The core assertion: Confirm that both calls return the same object instance.
        // This proves that the result was cached.
        assertSame("Subsequent calls should return the same cached byte array instance",
                firstResult, secondResult);
    }
}