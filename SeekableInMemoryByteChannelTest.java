/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.commons.compress.utils;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SeekableByteChannel;
import java.util.Arrays;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link SeekableInMemoryByteChannel}.
 *
 * The tests are structured by behavior (close, read, write, position, truncate)
 * and use small helpers to make the intent of each assertion clearer.
 */
class SeekableInMemoryByteChannelTest {

    private static final String TEST_TEXT = "Some data";
    private static final byte[] TEST_BYTES = TEST_TEXT.getBytes(UTF_8);
    private static final int TEST_LEN = TEST_BYTES.length;

    // ---------------- Helpers ----------------

    private static SeekableInMemoryByteChannel newChannel() {
        return new SeekableInMemoryByteChannel();
    }

    private static SeekableInMemoryByteChannel newChannel(final byte[] initial) {
        return new SeekableInMemoryByteChannel(initial);
    }

    private static ByteBuffer buf(final int capacity) {
        return ByteBuffer.allocate(capacity);
    }

    private static ByteBuffer wrap(final byte[] bytes) {
        return ByteBuffer.wrap(bytes);
    }

    private static byte[] firstN(final ByteBuffer buffer, final int n) {
        return Arrays.copyOf(buffer.array(), n);
    }

    private static byte[] firstN(final byte[] array, final int n) {
        return Arrays.copyOf(array, n);
    }

    // ---------------- Close ----------------

    /**
     * Close should be idempotent: closing an already-closed channel has no effect.
     */
    @Test
    void close_isIdempotent() throws Exception {
        try (SeekableByteChannel c = newChannel()) {
            c.close();
            assertFalse(c.isOpen());
            c.close();
            assertFalse(c.isOpen());
        }
    }

    // ---------------- Read ----------------

    /**
     * "Setting the position to a value that is greater than the current size is legal but does not change the size of the entity.
     * A later attempt to read bytes at such a position will immediately return an end-of-file indication."
     */
    @Test
    void read_positionBeyondSize_returnsEOF() throws Exception {
        try (SeekableByteChannel c = newChannel()) {
            c.position(2);
            assertEquals(2, c.position());
            final ByteBuffer readBuffer = buf(5);
            assertEquals(-1, c.read(readBuffer));
        }
    }

    @Test
    void read_exactBuffer_readsAllAndAdvancesPosition() throws IOException {
        try (SeekableInMemoryByteChannel c = newChannel(TEST_BYTES)) {
            final ByteBuffer readBuffer = buf(TEST_LEN);

            final int readCount = c.read(readBuffer);

            assertEquals(TEST_LEN, readCount);
            assertArrayEquals(TEST_BYTES, readBuffer.array());
            assertEquals(TEST_LEN, c.position());
        }
    }

    @Test
    void read_largerBuffer_readsAllKeepsExtraUntouched() throws IOException {
        try (SeekableInMemoryByteChannel c = newChannel(TEST_BYTES)) {
            final ByteBuffer readBuffer = buf(TEST_LEN + 1);

            final int readCount = c.read(readBuffer);

            assertEquals(TEST_LEN, readCount);
            assertArrayEquals(TEST_BYTES, firstN(readBuffer, TEST_LEN));
            assertEquals(TEST_LEN, c.position());
        }
    }

    @Test
    void read_fromSetPosition_readsRemainder() throws IOException {
        try (SeekableInMemoryByteChannel c = newChannel(TEST_BYTES)) {
            final ByteBuffer readBuffer = buf(4);

            c.position(5L);
            final int readCount = c.read(readBuffer);

            assertEquals(4, readCount);
            assertEquals("data", new String(readBuffer.array(), UTF_8));
            assertEquals(TEST_LEN, c.position());
        }
    }

    @Test
    void read_positionPastEnd_returnsEOFAndDoesNotMoveBuffer() throws IOException {
        try (SeekableInMemoryByteChannel c = newChannel(TEST_BYTES)) {
            final ByteBuffer readBuffer = buf(TEST_LEN);

            c.position(TEST_LEN + 1);
            final int readCount = c.read(readBuffer);

            assertEquals(0, readBuffer.position());
            assertEquals(-1, readCount);
            assertEquals(-1, c.read(readBuffer));
        }
    }

    @Test
    void read_onClosed_throwsClosedChannelException() {
        final SeekableInMemoryByteChannel c = newChannel();
        c.close();
        assertThrows(ClosedChannelException.class, () -> c.read(buf(1)));
    }

    // ---------------- Write ----------------

    @Test
    void write_fromStart_writesAllAndAdvancesPosition() throws IOException {
        try (SeekableInMemoryByteChannel c = newChannel()) {
            final ByteBuffer inData = wrap(TEST_BYTES);

            final int writeCount = c.write(inData);

            assertEquals(TEST_LEN, writeCount);
            assertArrayEquals(TEST_BYTES, firstN(c.array(), (int) c.size()));
            assertEquals(TEST_LEN, c.position());
        }
    }

    @Test
    void write_afterPositionSet_appendsAtPositionAndGrows() throws IOException {
        try (SeekableInMemoryByteChannel c = newChannel(TEST_BYTES)) {
            final ByteBuffer inData = wrap(TEST_BYTES);
            final ByteBuffer expected = ByteBuffer.allocate(TEST_LEN + 5)
                                                  .put(TEST_BYTES, 0, 5)
                                                  .put(TEST_BYTES);

            c.position(5L);
            final int writeCount = c.write(inData);

            assertEquals(TEST_LEN, writeCount);
            assertArrayEquals(expected.array(), firstN(c.array(), (int) c.size()));
            assertEquals(TEST_LEN + 5, c.position());
        }
    }

    @Test
    void write_onClosed_throwsClosedChannelException() {
        final SeekableInMemoryByteChannel c = newChannel();
        c.close();
        assertThrows(ClosedChannelException.class, () -> c.write(buf(1)));
    }

    // ---------------- Position ----------------

    @Test
    void position_setWithinAndBeyondSize_updatesPositionButNotSize() throws IOException {
        try (SeekableInMemoryByteChannel c = newChannel(TEST_BYTES)) {
            final long p4 = c.position(4L).position();
            final long pEnd = c.position(TEST_LEN).position();
            final long pPastEnd = c.position(TEST_LEN + 1L).position();

            assertEquals(4L, p4);
            assertEquals(c.size(), pEnd);
            assertEquals(TEST_LEN + 1L, pPastEnd);
        }
    }

    /**
     * "ClosedChannelException - If this channel is closed"
     */
    @Test
    void position_setOnClosed_throwsClosedChannelException() throws Exception {
        try (SeekableByteChannel c = newChannel()) {
            c.close();
            assertThrows(ClosedChannelException.class, () -> c.position(0));
        }
    }

    @Test
    void position_negative_throwsIOException() throws Exception {
        try (SeekableByteChannel c = newChannel()) {
            assertThrows(IOException.class, () -> c.position(-1));
        }
    }

    @Test
    void position_beyondIntegerMax_throwsIOException() {
        try (SeekableInMemoryByteChannel c = newChannel()) {
            assertThrows(IOException.class, () -> c.position(Integer.MAX_VALUE + 1L));
        }
    }

    // ---------------- Truncate ----------------

    @Test
    void truncate_reducesSizeAndData() {
        try (SeekableInMemoryByteChannel c = newChannel(TEST_BYTES)) {
            c.truncate(4);
            final byte[] bytes = firstN(c.array(), (int) c.size());
            assertEquals("Some", new String(bytes, UTF_8));
        }
    }

    @Test
    void truncate_smallerThanPosition_movesPositionAndSize() throws IOException {
        try (SeekableInMemoryByteChannel c = newChannel(TEST_BYTES)) {
            c.position(TEST_LEN);
            c.truncate(4L);
            assertEquals(4L, c.position());
            assertEquals(4L, c.size());
        }
    }

    /**
     * "In either case, if the current position is greater than the given size then it is set to that size."
     */
    @Test
    void truncate_toLessThanSize_keepsSmallerPosition() throws Exception {
        try (SeekableByteChannel c = newChannel(TEST_BYTES)) {
            c.position(1);
            c.truncate(TEST_LEN - 1);
            assertEquals(TEST_LEN - 1, c.size());
            assertEquals(1, c.position());
        }
    }

    /**
     * "In either case, if the current position is greater than the given size then it is set to that size."
     */
    @Test
    void truncate_notResizingButPositionBiggerThanSize_movesPositionToSize() throws Exception {
        try (SeekableByteChannel c = newChannel(TEST_BYTES)) {
            c.position(2 * TEST_LEN);
            c.truncate(TEST_LEN);
            assertEquals(TEST_LEN, c.size());
            assertEquals(TEST_LEN, c.position());
        }
    }

    /**
     * "In either case, if the current position is greater than the given size then it is set to that size."
     */
    @Test
    void truncate_shrinkingBeyondPosition_movesPositionToNewSize() throws Exception {
        try (SeekableByteChannel c = newChannel(TEST_BYTES)) {
            c.position(4);
            c.truncate(3);
            assertEquals(3, c.size());
            assertEquals(3, c.position());
        }
    }

    /**
     * "If the given size is greater than or equal to the current size then the entity is not modified."
     */
    @Test
    void truncate_toBiggerSize_doesNothing() throws Exception {
        try (SeekableByteChannel c = newChannel(TEST_BYTES)) {
            assertEquals(TEST_LEN, c.size());
            c.truncate(TEST_LEN + 1);
            assertEquals(TEST_LEN, c.size());

            final ByteBuffer readBuffer = buf(TEST_LEN);
            assertEquals(TEST_LEN, c.read(readBuffer));
            assertArrayEquals(TEST_BYTES, firstN(readBuffer, TEST_LEN));
        }
    }

    /**
     * "If the given size is greater than or equal to the current size then the entity is not modified."
     */
    @Test
    void truncate_toCurrentSize_doesNothing() throws Exception {
        try (SeekableByteChannel c = newChannel(TEST_BYTES)) {
            assertEquals(TEST_LEN, c.size());
            c.truncate(TEST_LEN);
            assertEquals(TEST_LEN, c.size());

            final ByteBuffer readBuffer = buf(TEST_LEN);
            assertEquals(TEST_LEN, c.read(readBuffer));
            assertArrayEquals(TEST_BYTES, firstN(readBuffer, TEST_LEN));
        }
    }

    /**
     * "IllegalArgumentException - If the new size is negative"
     */
    @Test
    void truncate_negative_throwsIllegalArgumentException() throws Exception {
        try (SeekableByteChannel c = newChannel()) {
            assertThrows(IllegalArgumentException.class, () -> c.truncate(-1));
        }
    }

    @Test
    void truncate_beyondIntegerMax_throwsIllegalArgumentException() {
        try (SeekableInMemoryByteChannel c = newChannel()) {
            assertThrows(IllegalArgumentException.class, () -> c.truncate(Integer.MAX_VALUE + 1L));
        }
    }

    /**
     * "Setting the position to a value that is greater than the current size is legal but does not change the size of the entity.
     * A later attempt to write bytes at such a position will cause the entity to grow to accommodate the new bytes; the values of any bytes
     * between the previous end-of-file and the newly-written bytes are unspecified."
     *
     * Note: Missing @Test in original code; kept as-is to preserve behavior of the original test suite.
     */
    public void writingToAPositionAfterEndGrowsChannel() throws Exception {
        try (SeekableByteChannel c = newChannel()) {
            c.position(2);
            assertEquals(2, c.position());

            final ByteBuffer inData = wrap(TEST_BYTES);
            assertEquals(TEST_LEN, c.write(inData));
            assertEquals(TEST_LEN + 2, c.size());

            c.position(2);
            final ByteBuffer readBuffer = buf(TEST_LEN);
            c.read(readBuffer);
            assertArrayEquals(TEST_BYTES, firstN(readBuffer, TEST_LEN));
        }
    }

    // ---------------- Spec-violation acknowledgements ----------------

    // The implementation deliberately violates the SeekableByteChannel spec for the following methods when called on a closed channel:
    // - position() (getter)
    // - size()
    // - truncate(long)
    // The next tests are disabled to document this intentional deviation.

    @Test
    @Disabled("we deliberately violate the spec")
    void throwsClosedChannelExceptionWhenPositionIsReadOnClosedChannel() throws Exception {
        try (SeekableByteChannel c = newChannel()) {
            c.close();
            c.position();
        }
    }

    @Test
    @Disabled("we deliberately violate the spec")
    void throwsClosedChannelExceptionWhenSizeIsReadOnClosedChannel() throws Exception {
        try (SeekableByteChannel c = newChannel()) {
            c.close();
            c.size();
        }
    }

    @Test
    @Disabled("we deliberately violate the spec")
    void throwsClosedChannelExceptionWhenTruncateIsCalledOnClosedChannel() throws Exception {
        try (SeekableByteChannel c = newChannel()) {
            c.close();
            c.truncate(0);
        }
    }
}