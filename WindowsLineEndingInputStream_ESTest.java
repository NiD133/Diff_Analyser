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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

/**
 * Tests for {@link WindowsLineEndingInputStream}.
 */
public class WindowsLineEndingInputStreamTest {

    /**
     * Helper method to read the entire content of a stream after applying the
     * WindowsLineEndingInputStream filter.
     *
     * @param input The string to be used as input.
     * @param lineFeedAtEos The flag for the stream constructor.
     * @return The filtered content as a string.
     * @throws IOException If an I/O error occurs.
     */
    private String readStreamContent(final String input, final boolean lineFeedAtEos) throws IOException {
        final byte[] inputBytes = input.getBytes(StandardCharsets.UTF_8);
        final InputStream originalStream = new ByteArrayInputStream(inputBytes);
        try (final InputStream stream = new WindowsLineEndingInputStream(originalStream, lineFeedAtEos)) {
            return IOUtils.toString(stream, StandardCharsets.UTF_8);
        }
    }

    @Test
    public void testShouldNotAlterStreamWithNoLineEndings() throws IOException {
        // Arrange
        final String input = "Hello World";
        
        // Act
        final String result = readStreamContent(input, false);

        // Assert
        assertEquals(input, result);
    }

    @Test
    public void testShouldNotAlterExistingCRLF() throws IOException {
        // Arrange
        final String input = "Line 1\r\nLine 2";
        
        // Act
        final String result = readStreamContent(input, false);

        // Assert
        assertEquals(input, result);
    }

    @Test
    public void testShouldConvertStandaloneLFToCRLF() throws IOException {
        // Arrange
        final String input = "Line 1\nLine 2\n";
        final String expected = "Line 1\r\nLine 2\r\n";
        
        // Act
        final String result = readStreamContent(input, false);

        // Assert
        assertEquals(expected, result);
    }

    @Test
    public void testShouldConvertStandaloneCRToCRLF() throws IOException {
        // Arrange
        final String input = "Line 1\rLine 2\r";
        final String expected = "Line 1\r\nLine 2\r\n";
        
        // Act
        final String result = readStreamContent(input, false);

        // Assert
        assertEquals(expected, result);
    }

    @Test
    public void testEmptyStreamWithLineFeedAtEosShouldInjectCRLF() throws IOException {
        // Arrange
        final String input = "";
        final String expected = "\r\n";
        
        // Act
        final String result = readStreamContent(input, true);

        // Assert
        assertEquals(expected, result);
    }

    @Test
    public void testEmptyStreamWithoutLineFeedAtEosShouldRemainEmpty() throws IOException {
        // Arrange
        final String input = "";
        
        // Act
        final String result = readStreamContent(input, false);

        // Assert
        assertEquals(input, result);
    }

    @Test
    public void testStreamEndingWithCRWithLineFeedAtEosShouldAddLF() throws IOException {
        // Arrange
        final String input = "Ends with CR\r";
        final String expected = "Ends with CR\r\n";
        
        // Act
        final String result = readStreamContent(input, true);

        // Assert
        assertEquals(expected, result);
    }

    @Test
    public void testStreamEndingWithLFWithLineFeedAtEosShouldAddCR() throws IOException {
        // Arrange
        final String input = "Ends with LF\n";
        final String expected = "Ends with LF\r\n";
        
        // Act
        final String result = readStreamContent(input, true);

        // Assert
        assertEquals(expected, result);
    }

    @Test
    public void testStreamEndingWithCRLFWithLineFeedAtEosShouldNotChange() throws IOException {
        // Arrange
        final String input = "Ends with CRLF\r\n";
        
        // Act
        final String result = readStreamContent(input, true);

        // Assert
        assertEquals(input, result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testMarkShouldThrowUnsupportedOperationException() throws IOException {
        // Arrange
        try (InputStream stream = new WindowsLineEndingInputStream(new ByteArrayInputStream(new byte[0]), false)) {
            // Act
            stream.mark(1);
        }
    }

    @Test
    public void testResetShouldThrowUnsupportedOperationException() throws IOException {
        // Arrange
        try (InputStream stream = new WindowsLineEndingInputStream(new ByteArrayInputStream(new byte[0]), false)) {
            // Act & Assert
            try {
                stream.reset();
                fail("Expected UnsupportedOperationException");
            } catch (final UnsupportedOperationException e) {
                // Expected
            }
        }
    }

    @Test(expected = IOException.class)
    public void testReadShouldPropagateIOExceptionFromUnderlyingStream() throws IOException {
        // Arrange
        final InputStream failingStream = new InputStream() {
            @Override
            public int read() throws IOException {
                throw new IOException("Test Exception");
            }
        };
        try (InputStream stream = new WindowsLineEndingInputStream(failingStream, false)) {
            // Act
            stream.read();
        }
    }

    @Test
    public void testCloseShouldCloseUnderlyingStream() throws IOException {
        // Arrange
        final boolean[] closed = {false};
        final InputStream underlying = new ByteArrayInputStream(new byte[0]) {
            @Override
            public void close() throws IOException {
                closed[0] = true;
                super.close();
            }
        };
        
        final InputStream stream = new WindowsLineEndingInputStream(underlying, false);

        // Act
        stream.close();

        // Assert
        assertTrue("The underlying stream should have been closed.", closed[0]);
    }
}