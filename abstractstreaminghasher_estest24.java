package com.google.common.hash;

import static org.junit.Assert.assertSame;

import com.google.common.hash.Crc32cHashFunction.Crc32cHasher;
import org.junit.Test;

/**
 * Tests for the base functionality of {@link AbstractStreamingHasher}.
 */
public class AbstractStreamingHasherTest {

    /**
     * Verifies that the {@code putByte} method returns the same {@link Hasher} instance,
     * which is essential for supporting a fluent API (method chaining).
     */
    @Test
    public void putByte_shouldReturnSameInstance_forMethodChaining() {
        // Arrange: Create a concrete instance of an AbstractStreamingHasher.
        Hasher hasher = new Crc32cHasher();
        byte testByte = (byte) 0xAB;

        // Act: Call the putByte method.
        Hasher returnedHasher = hasher.putByte(testByte);

        // Assert: The returned hasher must be the same instance as the original.
        // This fluent-style return is crucial for chaining calls (e.g., hasher.putByte(a).putInt(b)).
        assertSame(
                "The put...() methods should return the same Hasher instance to allow for method chaining.",
                hasher,
                returnedHasher);
    }
}