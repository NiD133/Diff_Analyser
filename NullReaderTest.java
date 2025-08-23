/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.io.input;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.EOFException;
import java.io.IOException;
import java.io.Reader;

import org.junit.jupiter.api.Test;

/**
 * Tests {@link NullReader}.
 */
class NullReaderTest {

    /**
     * A NullReader that returns predictable, position-based values to make assertions easy:
     * - read(): returns the previous position (0-based).
     * - read(char[], off, len): fills chars[off + i] with a char value equal to the position that will be read,
     *   i.e., startPos + i where startPos = (int) (getPosition() - len).
     */
    private static final class TestNullReader extends NullReader {
        TestNullReader(final int size) {
            super(size);
        }

        TestNullReader(final int size, final boolean markSupported, final boolean throwEofException) {
            super(size, markSupported, throwEofException);
        }

        @Override
        protected int processChar() {
            // Position is incremented before returning, so subtract 1 to get the char value.
            return (int) getPosition() - 1;
        }

        @Override
        protected void processChars(final char[] chars, final int offset, final int length) {
            final int startPos = (int) getPosition() - length;
            for (int i = 0; i < length; i++) {
                chars[offset + i] = (char) (startPos + i);
            }
        }
    }

    // Messages used by NullReader
    private static final String MARK_RESET_NOT_SUPPORTED = "mark/reset not supported"; // From JDK InputStream
    private static final String NO_MARK_MESSAGE = "No position has been marked";
    private static final String READ_AFTER_EOF_MESSAGE = "Read after end of file";
    private static final String SKIP_AFTER_EOF_MESSAGE = "Skip after end of file";

    private static String readLimitExceededMessage(final long markPos, final long readLimit) {
        return "Marked position [" + markPos + "] is no longer valid - passed the read limit [" + readLimit + "]";
    }

    @Test
    void readThrowsEOFExceptionWhenConfigured() throws Exception {
        try (Reader reader = new TestNullReader(2, false, true)) {
            assertEquals(0, reader.read());
            assertEquals(1, reader.read());
            assertThrows(EOFException.class, reader::read);
        }
    }

    @Test
    void markAndReset_whenSupported_behavesAsDocumented() throws Exception {
        final int readLimit = 10;
        int position = 0;

        try (Reader reader = new TestNullReader(100, true, false)) {
            assertTrue(reader.markSupported());

            // Reset without mark
            final IOException resetWithoutMark = assertThrows(IOException.class, reader::reset);
            assertEquals(NO_MARK_MESSAGE, resetWithoutMark.getMessage());

            // Read a few chars to advance position
            for (; position < 3; position++) {
                assertEquals(position, reader.read());
            }

            // Mark current position
            reader.mark(readLimit);
            final int markPosition = position;

            // Read a little more
            for (int i = 0; i < 3; i++) {
                assertEquals(position + i, reader.read());
            }
            position += 3;

            // Reset back to mark
            reader.reset();

            // Read up to and just beyond the read limit; reset should then fail
            for (int i = 0; i < readLimit + 1; i++) {
                assertEquals(markPosition + i, reader.read());
            }

            final IOException afterLimit = assertThrows(IOException.class, reader::reset);
            assertEquals(readLimitExceededMessage(markPosition, readLimit), afterLimit.getMessage());
        }
    }

    @Test
    void markAndReset_notSupported_throwsConsistentMessages() throws Exception {
        try (Reader reader = new TestNullReader(100, false, true)) {
            assertFalse(reader.markSupported());

            final UnsupportedOperationException markEx =
                    assertThrows(UnsupportedOperationException.class, () -> reader.mark(5));
            assertEquals(MARK_RESET_NOT_SUPPORTED, markEx.getMessage());

            final UnsupportedOperationException resetEx =
                    assertThrows(UnsupportedOperationException.class, reader::reset);
            assertEquals(MARK_RESET_NOT_SUPPORTED, resetEx.getMessage());
        }
    }

    @Test
    void read_singleChars_toEof_thenReadingPastEofThrows() throws Exception {
        final int size = 5;
        final TestNullReader reader = new TestNullReader(size);

        for (int i = 0; i < size; i++) {
            assertEquals(i, reader.read());
        }

        // At EOF
        assertEquals(-1, reader.read());

        // Further reads after EOF throw
        final IOException ex = assertThrows(IOException.class, reader::read);
        assertEquals(READ_AFTER_EOF_MESSAGE, ex.getMessage());

        // Close resets internal state
        reader.close();
        assertEquals(0, reader.getPosition());
    }

    @Test
    void read_charArrays_withAndWithoutOffset() throws Exception {
        final char[] chars = new char[10];

        try (Reader reader = new TestNullReader(15)) {
            // 1) Fill array completely
            final int count1 = reader.read(chars);
            assertEquals(chars.length, count1);
            for (int i = 0; i < count1; i++) {
                assertEquals(i, chars[i]);
            }

            // 2) Read remaining 5 chars
            final int count2 = reader.read(chars);
            assertEquals(5, count2);
            for (int i = 0; i < count2; i++) {
                assertEquals(count1 + i, chars[i]);
            }

            // 3) EOF
            assertEquals(-1, reader.read(chars));

            // 4) Reading after EOF throws
            final IOException readAfterEof = assertThrows(IOException.class, () -> reader.read(chars));
            assertEquals(READ_AFTER_EOF_MESSAGE, readAfterEof.getMessage());

            // 5) Reset by closing, then read with offset/length
            reader.close();

            final int offset = 2;
            final int length = 4;
            final int count5 = reader.read(chars, offset, length);
            assertEquals(length, count5);
            for (int i = offset; i < offset + length; i++) {
                assertEquals(i, chars[i]);
            }
        }
    }

    @Test
    void skip_advancesPosition_andHandlesEof() throws Exception {
        try (Reader reader = new TestNullReader(10, true, false)) {
            assertEquals(0, reader.read());     // pos=1
            assertEquals(1, reader.read());     // pos=2
            assertEquals(5, reader.skip(5));    // pos=7
            assertEquals(7, reader.read());     // pos=8
            assertEquals(2, reader.skip(5));    // only 2 left to skip, pos=10 (EOF)
            assertEquals(-1, reader.skip(5));   // at EOF and not throwing -> -1

            final IOException skipAfterEof = assertThrows(IOException.class, () -> reader.skip(5));
            assertEquals(SKIP_AFTER_EOF_MESSAGE, skipAfterEof.getMessage());
        }
    }
}