package com.google.common.io;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import org.junit.Test;

/**
 * Tests for {@link MultiInputStream}.
 */
public class MultiInputStreamTest {

    @Test
    public void read_withSingleByteInSource_returnsUnsignedByteValue() throws IOException {
        // Arrange
        // Use a byte with a negative value to test that it's correctly converted to an unsigned int.
        byte testByte = (byte) -98;
        // InputStream.read() returns an int from 0-255. The unsigned value of (byte)-98 is 158.
        int expectedIntValue = 158;

        ByteSource byteSource = ByteSource.wrap(new byte[] {testByte});
        Iterator<ByteSource> sources = Collections.singletonList(byteSource).iterator();

        // Act & Assert
        try (MultiInputStream multiInputStream = new MultiInputStream(sources)) {
            int actualValue = multiInputStream.read();

            // Assert that the correct byte value was read.
            assertEquals(
                "The read byte should match the expected unsigned integer value.",
                expectedIntValue,
                actualValue);

            // Assert that the stream is now empty.
            assertEquals(
                "The stream should be exhausted after reading the single byte.",
                -1,
                multiInputStream.read());
        }
    }
}