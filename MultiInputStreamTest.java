/*
 * Copyright (C) 2007 The Guava Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.common.io;

import java.io.ByteArrayInputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.jspecify.annotations.NullUnmarked;

/**
 * Test class for {@link MultiInputStream}.
 *
 * @author Chris Nokleberg
 */
@NullUnmarked
public class MultiInputStreamTest extends IoTestCase {

    // Test constants for better readability
    private static final int EMPTY_SOURCE_COUNT = 10_000_000;
    private static final int TEST_DATA_SIZE_SMALL = 10;
    private static final int TEST_DATA_SIZE_MEDIUM = 20;
    private static final int TEST_DATA_SIZE_LARGE = 50;

    /**
     * Tests concatenation of multiple ByteSources with different configurations.
     * Verifies that joined stream content matches expected sequence.
     */
    public void testConcatenationWithVariousSourceSizes() throws Exception {
        assertConcatenationForSpans(0);
        assertConcatenationForSpans(1);
        assertConcatenationForSpans(0, 0, 0);
        assertConcatenationForSpans(TEST_DATA_SIZE_SMALL, TEST_DATA_SIZE_MEDIUM);
        assertConcatenationForSpans(TEST_DATA_SIZE_SMALL, 0, TEST_DATA_SIZE_MEDIUM);
        assertConcatenationForSpans(0, TEST_DATA_SIZE_SMALL, TEST_DATA_SIZE_MEDIUM);
        assertConcatenationForSpans(TEST_DATA_SIZE_SMALL, TEST_DATA_SIZE_MEDIUM, 0);
        assertConcatenationForSpans(TEST_DATA_SIZE_SMALL, TEST_DATA_SIZE_MEDIUM, 1);
        assertConcatenationForSpans(1, 1, 1, 1, 1, 1, 1, 1);
        assertConcatenationForSpans(1, 0, 1, 0, 1, 0, 1, 0);
    }

    /**
     * Verifies that only one source stream is open at any given time during
     * concatenation. Uses a state-tracking ByteSource to monitor open streams.
     */
    public void testOnlyOneSourceOpenAtATime() throws Exception {
        // Create a source that tracks open/close operations
        final int[] openCounter = new int[1];
        ByteSource trackedSource = new OpenTrackingByteSource(
            newByteSource(0, TEST_DATA_SIZE_LARGE), openCounter);

        // Concatenate multiple instances
        ByteSource concatenated = ByteSource.concat(trackedSource, trackedSource, trackedSource);
        byte[] result = concatenated.read();

        // Verify correct data length and proper open/close management
        assertEquals("Concatenated stream should have correct length", 
                     3 * TEST_DATA_SIZE_LARGE, result.length);
        assertEquals("All streams should be closed after read", 0, openCounter[0]);
    }

    /**
     * Tests reading individual bytes from concatenated streams.
     * Verifies correct byte count and stream state transitions.
     */
    public void testSingleByteReads() throws Exception {
        ByteSource source = newByteSource(0, TEST_DATA_SIZE_SMALL);
        ByteSource concatenated = ByteSource.concat(source, source);
        InputStream in = concatenated.openStream();

        // Verify initial state
        assertFalse("Mark should not be supported", in.markSupported());
        assertEquals("Initial available bytes should match first source", 
                     TEST_DATA_SIZE_SMALL, in.available());

        // Read all bytes
        int totalBytes = 0;
        while (in.read() != -1) {
            totalBytes++;
        }

        // Verify final state
        assertEquals("Total bytes read should match concatenated length", 
                     2 * TEST_DATA_SIZE_SMALL, totalBytes);
        assertEquals("No bytes should remain after read", 0, in.available());
    }

    /**
     * Tests skip behavior with an underlying stream that always returns 0 for skip().
     * Verifies that skip operations correctly fall back to reading when skipping is not supported.
     */
    public void testSkipWithUnderlyingStreamReturningZero() throws Exception {
        // Create source with zero-skip behavior
        ByteSource source = new ByteSource() {
            @Override
            public InputStream openStream() {
                return new ZeroSkipByteArrayInputStream(newPreFilledByteArray(0, TEST_DATA_SIZE_LARGE));
            }
        };

        MultiInputStream multi = new MultiInputStream(Collections.singleton(source).iterator());

        // Test invalid skip arguments
        assertEquals("Skip negative should return 0", 0, multi.skip(-1));
        assertEquals("Skip zero should return 0", 0, multi.skip(0));

        // Test forced skip via utility method
        ByteStreams.skipFully(multi, 20);
        assertEquals("Should read correct byte after skip", 20, multi.read());
    }

    /**
     * Tests that reading single bytes from many empty sources doesn't cause stack overflow.
     * Verifies stability with large number of empty streams.
     */
    public void testReadSingleByteFromManyEmptySources_noStackOverflow() throws IOException {
        InputStream in = createMultiInputStreamWithEmptySources(EMPTY_SOURCE_COUNT);
        assertEquals("Read should return -1 for all empty sources", -1, in.read());
    }

    /**
     * Tests that reading into a buffer from many empty sources doesn't cause stack overflow.
     * Verifies stability with large number of empty streams.
     */
    public void testReadArrayFromManyEmptySources_noStackOverflow() throws IOException {
        InputStream in = createMultiInputStreamWithEmptySources(EMPTY_SOURCE_COUNT);
        assertEquals("Read into array should return -1 for all empty sources", 
                     -1, in.read(new byte[1]));
    }

    // Helper Methods ----------------------------------------------------------

    /**
     * Asserts that concatenated streams with specified sizes produce expected content.
     * @param spans sizes of individual ByteSources to concatenate
     */
    private void assertConcatenationForSpans(Integer... spans) throws Exception {
        List<ByteSource> sources = new ArrayList<>();
        int expectedSize = 0;
        for (int span : spans) {
            sources.add(newByteSource(expectedSize, span));
            expectedSize += span;
        }

        ByteSource concatenated = ByteSource.concat(sources);
        String config = Arrays.toString(spans);
        assertTrue("Concatenation failed for configuration: " + config,
                   newByteSource(0, expectedSize).contentEquals(concatenated));
    }

    /**
     * Creates a ByteSource generating test data starting at given value.
     * @param start first byte value
     * @param size number of bytes to generate
     */
    private static ByteSource newByteSource(int start, int size) {
        return new ByteSource() {
            @Override
            public InputStream openStream() {
                return new ByteArrayInputStream(newPreFilledByteArray(start, size));
            }
        };
    }

    /**
     * Creates a MultiInputStream containing many empty ByteSources.
     * @param count number of empty sources to create
     */
    private static MultiInputStream createMultiInputStreamWithEmptySources(int count) 
        throws IOException {
        return new MultiInputStream(Collections.nCopies(count, ByteSource.empty()).iterator());
    }

    // Specialized Stream Classes ----------------------------------------------

    /**
     * InputStream that always returns 0 for skip operations.
     * Used to test fallback to read operations.
     */
    private static final class ZeroSkipByteArrayInputStream extends ByteArrayInputStream {
        ZeroSkipByteArrayInputStream(byte[] buf) {
            super(buf);
        }

        @Override
        public long skip(long n) {
            // Explicitly disable skipping
            return 0;
        }
    }

    /**
     * ByteSource that tracks open/close operations using a counter.
     * Verifies only one stream is open at a time.
     */
    private static final class OpenTrackingByteSource extends ByteSource {
        private final ByteSource delegate;
        private final int[] openCounter;

        OpenTrackingByteSource(ByteSource delegate, int[] openCounter) {
            this.delegate = delegate;
            this.openCounter = openCounter;
        }

        @Override
        public InputStream openStream() throws IOException {
            if (openCounter[0]++ != 0) {
                throw new IllegalStateException("Only one source should be open at a time");
            }
            return new FilterInputStream(delegate.openStream()) {
                @Override
                public void close() throws IOException {
                    super.close();
                    openCounter[0]--;
                }
            };
        }
    }
}