package com.google.common.hash;

import static org.junit.Assert.assertSame;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import org.junit.Test;

/**
 * Tests for the abstract base class {@link AbstractStreamingHasher}.
 *
 * <p>Since {@link AbstractStreamingHasher} is abstract, these tests use a concrete implementation,
 * {@link Crc32cHashFunction.Crc32cHasher}, to verify the base class's functionality.
 */
public class AbstractStreamingHasherTest {

    @Test
    public void putBytes_withPartiallyReadBuffer_returnsSameHasherInstanceForChaining() {
        // Arrange
        // Use a concrete implementation of the abstract class under test.
        Hasher hasher = new Crc32cHashFunction.Crc32cHasher();

        // Create a ByteBuffer with some data.
        ByteBuffer inputBuffer = Charset.defaultCharset().encode("some test data");

        // Advance the buffer's position to simulate a scenario where the buffer has already
        // been partially read. The putBytes method is expected to process bytes only from
        // the current position to the limit.
        inputBuffer.getShort(); // Advances position by 2 bytes.

        // Act
        Hasher returnedHasher = hasher.putBytes(inputBuffer);

        // Assert
        // Verify that the method returns the same Hasher instance to support a fluent API.
        assertSame(
                "putBytes(ByteBuffer) should return the same hasher instance.",
                hasher,
                returnedHasher);
    }
}