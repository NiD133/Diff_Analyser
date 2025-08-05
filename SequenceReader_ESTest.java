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

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.io.PipedReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ConcurrentModificationException;

/**
 * Unit tests for {@link SequenceReader}.
 */
public class SequenceReaderTest {

    // A simple custom reader to verify that the close() method is called.
    private static class CloseTrackingReader extends StringReader {
        private boolean isClosed = false;

        CloseTrackingReader(String content) {
            super(content);
        }

        @Override
        public void close() {
            super.close();
            isClosed = true;
        }

        public boolean isClosed() {
            return isClosed;
        }
    }

    @Test
    public void constructorWithNullReaderArrayShouldThrowNullPointerException() {
        assertThrows(NullPointerException.class, () -> new SequenceReader((Reader[]) null));
    }

    @Test
    public void constructorWithNullIterableShouldThrowNullPointerException() {
        assertThrows(NullPointerException.class, () -> new SequenceReader((Iterable<? extends Reader>) null));
    }
    
    @Test
    public void readOnEmptySequenceShouldReturnEOF() throws IOException {
        try (SequenceReader sequenceReader = new SequenceReader(Collections.emptyList())) {
            assertEquals("Reading from an empty sequence should return EOF", -1, sequenceReader.read());
        }
    }

    @Test
    public void readShouldReturnFirstCharacterFromFirstReader() throws IOException {
        Reader reader = new StringReader("test");
        try (SequenceReader sequenceReader = new SequenceReader(reader)) {
            assertEquals('t', sequenceReader.read());
        }
    }

    @Test
    public void readShouldTransitionToNextReaderWhenCurrentOneIsExhausted() throws IOException {
        Reader reader1 = new StringReader("a");
        Reader reader2 = new StringReader("b");

        try (SequenceReader sequenceReader = new SequenceReader(reader1, reader2)) {
            assertEquals('a', sequenceReader.read());
            assertEquals("Should read from the second reader after the first is exhausted", 'b', sequenceReader.read());
            assertEquals(-1, sequenceReader.read());
        }
    }

    @Test
    public void readIntoBufferShouldReadAcrossReaderBoundaries() throws IOException {
        Reader reader1 = new StringReader("ab");
        Reader reader2 = new StringReader("cd");
        
        try (SequenceReader sequenceReader = new SequenceReader(reader1, reader2)) {
            char[] buffer = new char[4];
            int charsRead = sequenceReader.read(buffer, 0, buffer.length);
            
            assertEquals(4, charsRead);
            assertArrayEquals(new char[]{'a', 'b', 'c', 'd'}, buffer);
        }
    }

    @Test
    public void readIntoBufferShouldFillPortionOfBufferAndReturnBytesRead() throws IOException {
        Reader reader = new StringReader("test");
        try (SequenceReader sequenceReader = new SequenceReader(reader)) {
            char[] buffer = new char[3];
            int charsRead = sequenceReader.read(buffer, 1, 1);

            assertEquals(1, charsRead);
            assertArrayEquals(new char[]{'\0', 't', '\0'}, buffer);
        }
    }

    @Test
    public void readIntoBufferWithZeroLengthShouldReturnZero() throws IOException {
        try (SequenceReader sequenceReader = new SequenceReader(new StringReader("test"))) {
            char[] buffer = new char[5];
            assertEquals("Reading 0 characters should return 0", 0, sequenceReader.read(buffer, 1, 0));
        }
    }

    @Test(expected = NullPointerException.class)
    public void readWithNullBufferShouldThrowNullPointerException() throws IOException {
        try (SequenceReader sequenceReader = new SequenceReader(Collections.emptyList())) {
            sequenceReader.read(null, 0, 1);
        }
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void readWithNegativeOffsetShouldThrowIndexOutOfBounds() throws IOException {
        try (SequenceReader sequenceReader = new SequenceReader(new StringReader("test"))) {
            sequenceReader.read(new char[10], -1, 5);
        }
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void readWithNegativeLengthShouldThrowIndexOutOfBounds() throws IOException {
        try (SequenceReader sequenceReader = new SequenceReader(new StringReader("test"))) {
            sequenceReader.read(new char[10], 0, -1);
        }
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void readWithOffsetAndLengthExceedingBufferShouldThrowIndexOutOfBounds() throws IOException {
        try (SequenceReader sequenceReader = new SequenceReader(new StringReader("test"))) {
            sequenceReader.read(new char[10], 5, 6); // offset + length > buffer.length
        }
    }

    @Test
    public void skipShouldSkipAcrossMultipleReaders() throws IOException {
        Reader reader1 = new StringReader("abc"); // length 3
        Reader reader2 = new StringReader("defg"); // length 4
        
        try (SequenceReader sequenceReader = new SequenceReader(reader1, reader2)) {
            long skipped = sequenceReader.skip(10); // Try to skip more than available
            assertEquals("Should skip all characters from all readers", 7, skipped);
            assertEquals("Stream should be at the end after skipping all characters", -1, sequenceReader.read());
        }
    }


    @Test
    public void closeShouldCloseAllUnderlyingReaders() throws IOException {
        CloseTrackingReader reader1 = new CloseTrackingReader("a");
        CloseTrackingReader reader2 = new CloseTrackingReader("b");

        SequenceReader sequenceReader = new SequenceReader(reader1, reader2);
        sequenceReader.close();

        assertTrue("First reader should be closed", reader1.isClosed());
        assertTrue("Second reader should be closed", reader2.isClosed());
    }

    @Test(expected = IOException.class)
    public void readWhenUnderlyingReaderThrowsIOExceptionShouldPropagateException() throws IOException {
        // A PipedReader not connected to a PipedWriter will throw IOException on read.
        Reader faultyReader = new PipedReader();
        try (SequenceReader sequenceReader = new SequenceReader(faultyReader)) {
            sequenceReader.read();
        }
    }

    @Test(expected = ConcurrentModificationException.class)
    public void modifyingReaderListDuringReadShouldThrowConcurrentModificationException() throws IOException {
        List<Reader> readers = new ArrayList<>(Arrays.asList(new StringReader("a"), new StringReader("b")));
        
        try (SequenceReader sequenceReader = new SequenceReader(readers)) {
            sequenceReader.read(); // Reads 'a', moves to next reader
            readers.add(new StringReader("c")); // Modify the list while iterating
            sequenceReader.read(); // This should throw
        }
    }

    @Test(expected = ConcurrentModificationException.class)
    public void modifyingReaderListDuringCloseShouldThrowConcurrentModificationException() throws IOException {
        List<Reader> readers = new ArrayList<>(Arrays.asList(new StringReader("a"), new StringReader("b")));
        
        SequenceReader sequenceReader = new SequenceReader(readers);
        readers.add(new StringReader("c")); // Modify before close starts iterating
        sequenceReader.close(); // This should throw
    }
}