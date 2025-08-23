package org.apache.commons.compress.utils;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SeekableByteChannel;
import java.util.Arrays;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class SeekableInMemoryByteChannelTestTest23 {

    private final byte[] testData = "Some data".getBytes(UTF_8);

    /*
     * <q>Setting the position to a value that is greater than the current size is legal but does not change the size of the entity. A later attempt to write
     * bytes at such a position will cause the entity to grow to accommodate the new bytes; the values of any bytes between the previous end-of-file and the
     * newly-written bytes are unspecified.</q>
     */
    public void writingToAPositionAfterEndGrowsChannel() throws Exception {
        try (SeekableByteChannel c = new SeekableInMemoryByteChannel()) {
            c.position(2);
            assertEquals(2, c.position());
            final ByteBuffer inData = ByteBuffer.wrap(testData);
            assertEquals(testData.length, c.write(inData));
            assertEquals(testData.length + 2, c.size());
            c.position(2);
            final ByteBuffer readBuffer = ByteBuffer.allocate(testData.length);
            c.read(readBuffer);
            assertArrayEquals(testData, Arrays.copyOf(readBuffer.array(), testData.length));
        }
    }

    /*
     * <q>If the given size is greater than or equal to the current size then the entity is not modified.</q>
     */
    @Test
    void testTruncateToBiggerSizeDoesntChangeAnything() throws Exception {
        try (SeekableByteChannel c = new SeekableInMemoryByteChannel(testData)) {
            assertEquals(testData.length, c.size());
            c.truncate(testData.length + 1);
            assertEquals(testData.length, c.size());
            final ByteBuffer readBuffer = ByteBuffer.allocate(testData.length);
            assertEquals(testData.length, c.read(readBuffer));
            assertArrayEquals(testData, Arrays.copyOf(readBuffer.array(), testData.length));
        }
    }
}
