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

package org.apache.commons.io.output;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;

/**
 * Tests {@link ThresholdingOutputStream}. See also the subclass {@link DeferredFileOutputStream}.
 *
 * @see DeferredFileOutputStream
 */
class ThresholdingOutputStreamTest {

    private static final int SINGLE_BYTE_THRESHOLD = 1;
    private static final int ZERO_THRESHOLD = 0;
    private static final int NEGATIVE_THRESHOLD = -1;
    private static final int SEVEN_BYTE_THRESHOLD = 7;
    private static final int THREE_BYTE_THRESHOLD = 3;
    
    private static final byte SAMPLE_BYTE = 'a';
    private static final byte ANOTHER_SAMPLE_BYTE = 89;

    /**
     * Asserts initial state without changing it.
     *
     * @param outputStream the stream to test.
     * @param expectedThreshold the expected threshold.
     * @param expectedByteCount the expected byte count.
     */
    static void assertThresholdingInitialState(final ThresholdingOutputStream outputStream, 
                                             final int expectedThreshold, 
                                             final int expectedByteCount) {
        assertFalse(outputStream.isThresholdExceeded(), "Threshold should not be exceeded initially");
        assertEquals(expectedThreshold, outputStream.getThreshold(), "Threshold should match expected value");
        assertEquals(expectedByteCount, outputStream.getByteCount(), "Byte count should match expected value");
    }

    @Test
    void testResetByteCount_ShouldAllowThresholdToBeTriggeredMultipleTimes() throws IOException {
        // Given: A stream with threshold of 1 byte that resets its counter when threshold is reached
        final AtomicInteger thresholdReachedCount = new AtomicInteger(0);
        
        try (ByteArrayOutputStream underlyingStream = new ByteArrayOutputStream(); 
             ThresholdingOutputStream thresholdingStream = createStreamThatResetsCounterOnThreshold(
                 SINGLE_BYTE_THRESHOLD, thresholdReachedCount, underlyingStream)) {
            
            // Initially no threshold reached
            assertThresholdingInitialState(thresholdingStream, SINGLE_BYTE_THRESHOLD, 0);
            assertEquals(0, thresholdReachedCount.get(), "Threshold should not have been reached yet");
            
            // When: Writing first byte (at threshold but not exceeding)
            thresholdingStream.write(SAMPLE_BYTE);
            
            // Then: Threshold not exceeded yet
            assertFalse(thresholdingStream.isThresholdExceeded(), "Threshold should not be exceeded after first byte");
            
            // When: Writing second byte (would exceed threshold, triggering reset)
            thresholdingStream.write(SAMPLE_BYTE);
            
            // Then: Counter was reset, so threshold not exceeded
            assertEquals(1, thresholdReachedCount.get(), "Threshold should have been reached once");
            assertFalse(thresholdingStream.isThresholdExceeded(), "Threshold should not be exceeded after reset");
            
            // When: Writing more bytes to trigger threshold multiple times
            thresholdingStream.write(SAMPLE_BYTE);
            thresholdingStream.write(SAMPLE_BYTE);
            
            // Then: Threshold reached multiple times due to resets
            assertEquals(3, thresholdReachedCount.get(), "Threshold should have been reached three times");
        }
    }

    @Test
    void testResetByteCount_WithBrokenOutputStream_ShouldHandleIOExceptionsProperly() {
        // Given: A stream that resets counter but uses a broken output stream
        final AtomicInteger thresholdReachedCount = new AtomicInteger(0);
        
        // When & Then: IOException should be thrown during close operation
        final IOException thrownException = assertThrows(IOException.class, () -> {
            try (ByteArrayOutputStream workingStream = new ByteArrayOutputStream(); 
                 ThresholdingOutputStream thresholdingStream = createStreamThatResetsCounterOnThreshold(
                     SINGLE_BYTE_THRESHOLD, thresholdReachedCount, BrokenOutputStream.INSTANCE)) {
                
                assertThresholdingInitialState(thresholdingStream, SINGLE_BYTE_THRESHOLD, 0);
                assertEquals(0, thresholdReachedCount.get());
                
                // All write operations should fail
                assertThrows(IOException.class, () -> thresholdingStream.write(SAMPLE_BYTE));
                assertFalse(thresholdingStream.isThresholdExceeded());
                
                assertThrows(IOException.class, () -> thresholdingStream.write(SAMPLE_BYTE));
                assertEquals(0, thresholdReachedCount.get(), "Threshold should not be reached due to write failures");
                assertFalse(thresholdingStream.isThresholdExceeded());
                
                assertThrows(IOException.class, () -> thresholdingStream.write(SAMPLE_BYTE));
                assertThrows(IOException.class, () -> thresholdingStream.write(SAMPLE_BYTE));
                assertEquals(0, thresholdReachedCount.get());
            }
        });
        
        assertEquals("Broken output stream: close()", thrownException.getMessage(), 
                    "Exception should occur during close operation");
    }

    @Test
    void testSetByteCount_WithGetOutputStream_ShouldRespectInitialByteCount() throws IOException {
        testSetByteCountBehavior(true);
    }

    @Test
    void testSetByteCount_WithGetStream_ShouldRespectInitialByteCount() throws IOException {
        testSetByteCountBehavior(false);
    }

    private void testSetByteCountBehavior(boolean useGetOutputStream) throws IOException {
        // Given: A stream with initial byte count set to 2 and threshold of 3
        final AtomicBoolean thresholdReached = new AtomicBoolean(false);
        final int initialByteCount = 2;
        
        try (ThresholdingOutputStream thresholdingStream = createStreamWithInitialByteCount(
                THREE_BYTE_THRESHOLD, initialByteCount, thresholdReached, useGetOutputStream)) {
            
            // Initially: 2 bytes already "written", threshold at 3
            assertThresholdingInitialState(thresholdingStream, THREE_BYTE_THRESHOLD, initialByteCount);
            
            // When: Writing one more byte (total = 3, at threshold but not exceeded)
            thresholdingStream.write(SAMPLE_BYTE);
            
            // Then: Threshold not reached yet
            assertFalse(thresholdReached.get(), "Threshold callback should not be triggered yet");
            assertFalse(thresholdingStream.isThresholdExceeded(), "Threshold should not be exceeded yet");
            
            // When: Writing another byte (total = 4, exceeds threshold of 3)
            thresholdingStream.write(SAMPLE_BYTE);
            
            // Then: Threshold exceeded
            assertTrue(thresholdReached.get(), "Threshold callback should be triggered");
            assertTrue(thresholdingStream.isThresholdExceeded(), "Threshold should be exceeded");
        }
    }

    @Test
    void testThresholdIOConsumer_WithVariousConsumerConfigurations() throws IOException {
        // Test Case 1: Null threshold consumer should not cause issues
        try (ThresholdingOutputStream streamWithNullConsumer = new ThresholdingOutputStream(
                SINGLE_BYTE_THRESHOLD, null, os -> new ByteArrayOutputStream(4))) {
            
            assertThresholdingInitialState(streamWithNullConsumer, SINGLE_BYTE_THRESHOLD, 0);
            
            streamWithNullConsumer.write(SAMPLE_BYTE);
            assertFalse(streamWithNullConsumer.isThresholdExceeded());
            
            streamWithNullConsumer.write(SAMPLE_BYTE);
            assertTrue(streamWithNullConsumer.isThresholdExceeded());
        }
        
        // Test Case 2: Null output stream function should work with default behavior
        final AtomicBoolean thresholdReached = new AtomicBoolean(false);
        try (ThresholdingOutputStream streamWithNullOutputFunction = new ThresholdingOutputStream(
                SINGLE_BYTE_THRESHOLD, os -> thresholdReached.set(true), null)) {
            
            assertThresholdingInitialState(streamWithNullOutputFunction, SINGLE_BYTE_THRESHOLD, 0);
            
            streamWithNullOutputFunction.write(SAMPLE_BYTE);
            assertFalse(thresholdReached.get());
            assertFalse(streamWithNullOutputFunction.isThresholdExceeded());
            
            streamWithNullOutputFunction.write(SAMPLE_BYTE);
            assertTrue(thresholdReached.get());
            assertTrue(streamWithNullOutputFunction.isThresholdExceeded());
        }
        
        // Test Case 3: Both consumer and output function provided
        thresholdReached.set(false);
        try (ThresholdingOutputStream streamWithBothProvided = new ThresholdingOutputStream(
                SINGLE_BYTE_THRESHOLD, 
                os -> thresholdReached.set(true),
                os -> new ByteArrayOutputStream(4))) {
            
            assertThresholdingInitialState(streamWithBothProvided, SINGLE_BYTE_THRESHOLD, 0);
            
            streamWithBothProvided.write(SAMPLE_BYTE);
            assertFalse(thresholdReached.get());
            assertFalse(streamWithBothProvided.isThresholdExceeded());
            
            streamWithBothProvided.write(SAMPLE_BYTE);
            assertTrue(thresholdReached.get());
            assertTrue(streamWithBothProvided.isThresholdExceeded());
        }
    }

    @Test
    void testThresholdIOConsumer_WithIOException_ShouldPropagateException() throws IOException {
        // Given: A stream that throws IOException when threshold is reached
        try (ThresholdingOutputStream streamThatThrowsIOException = new ThresholdingOutputStream(
                SINGLE_BYTE_THRESHOLD, 
                os -> { throw new IOException("Threshold reached."); }, 
                os -> new ByteArrayOutputStream(4))) {
            
            assertThresholdingInitialState(streamThatThrowsIOException, SINGLE_BYTE_THRESHOLD, 0);
            
            // When: Writing first byte (at threshold, no exception yet)
            streamThatThrowsIOException.write(SAMPLE_BYTE);
            assertFalse(streamThatThrowsIOException.isThresholdExceeded());
            
            // When & Then: Writing second byte should trigger IOException
            assertThrows(IOException.class, () -> streamThatThrowsIOException.write(SAMPLE_BYTE));
            assertFalse(streamThatThrowsIOException.isThresholdExceeded(), 
                       "Threshold should not be marked as exceeded when exception occurs");
        }
    }

    @Test
    void testThresholdIOConsumer_WithUncheckedException_ShouldPropagateException() throws IOException {
        // Given: A stream that throws unchecked exception when threshold is reached
        try (ThresholdingOutputStream streamThatThrowsRuntimeException = new ThresholdingOutputStream(
                SINGLE_BYTE_THRESHOLD, 
                os -> { throw new IllegalStateException("Threshold reached."); }, 
                os -> new ByteArrayOutputStream(4))) {
            
            assertThresholdingInitialState(streamThatThrowsRuntimeException, SINGLE_BYTE_THRESHOLD, 0);
            
            // When: Writing first byte
            streamThatThrowsRuntimeException.write(SAMPLE_BYTE);
            assertFalse(streamThatThrowsRuntimeException.isThresholdExceeded());
            
            // When & Then: Writing second byte should trigger IllegalStateException
            assertThrows(IllegalStateException.class, () -> streamThatThrowsRuntimeException.write(SAMPLE_BYTE));
            assertFalse(streamThatThrowsRuntimeException.isThresholdExceeded());
            
            // Output stream should still be accessible and of correct type
            assertInstanceOf(ByteArrayOutputStream.class, streamThatThrowsRuntimeException.getOutputStream());
            assertFalse(streamThatThrowsRuntimeException.isThresholdExceeded());
        }
    }

    @Test
    void testThreshold_WithNegativeValue_ShouldBeTreatedAsZero() throws IOException {
        // Given: A stream with negative threshold (should be treated as 0)
        final AtomicBoolean thresholdReached = new AtomicBoolean(false);
        
        try (ThresholdingOutputStream streamWithNegativeThreshold = createSimpleThresholdingStream(
                NEGATIVE_THRESHOLD, thresholdReached)) {
            
            // Then: Negative threshold is normalized to 0
            assertThresholdingInitialState(streamWithNegativeThreshold, ZERO_THRESHOLD, 0);
            assertFalse(thresholdReached.get());
            
            // When: Writing any byte should immediately exceed threshold of 0
            streamWithNegativeThreshold.write(ANOTHER_SAMPLE_BYTE);
            
            // Then: Threshold exceeded immediately
            assertTrue(thresholdReached.get());
            assertTrue(streamWithNegativeThreshold.isThresholdExceeded());
            assertInstanceOf(NullOutputStream.class, streamWithNegativeThreshold.getOutputStream());
        }
    }

    @Test
    void testThreshold_WithZeroValue_ShouldBeExceededOnFirstWrite() throws IOException {
        // Given: A stream with zero threshold
        final AtomicBoolean thresholdReached = new AtomicBoolean(false);
        
        try (ThresholdingOutputStream streamWithZeroThreshold = createSimpleThresholdingStream(
                ZERO_THRESHOLD, thresholdReached)) {
            
            assertThresholdingInitialState(streamWithZeroThreshold, ZERO_THRESHOLD, 0);
            
            // When: Writing first byte should immediately exceed threshold of 0
            streamWithZeroThreshold.write(ANOTHER_SAMPLE_BYTE);
            
            // Then: Threshold exceeded immediately
            assertTrue(thresholdReached.get());
            assertTrue(streamWithZeroThreshold.isThresholdExceeded());
            assertInstanceOf(NullOutputStream.class, streamWithZeroThreshold.getOutputStream());
        }
    }

    @Test
    void testThreshold_WithEmptyWrite_ShouldNotTriggerThreshold() throws IOException {
        // Given: A stream with reasonable threshold
        final AtomicBoolean thresholdReached = new AtomicBoolean(false);
        
        try (ThresholdingOutputStream streamForEmptyWrite = createSimpleThresholdingStream(
                SEVEN_BYTE_THRESHOLD, thresholdReached)) {
            
            assertThresholdingInitialState(streamForEmptyWrite, SEVEN_BYTE_THRESHOLD, 0);
            assertFalse(thresholdReached.get());
            
            // When: Writing empty byte array
            streamForEmptyWrite.write(new byte[0]);
            
            // Then: Threshold should not be triggered by empty write
            assertFalse(streamForEmptyWrite.isThresholdExceeded());
            assertFalse(thresholdReached.get());
            
            // Output stream should still be default (NullOutputStream)
            assertInstanceOf(NullOutputStream.class, streamForEmptyWrite.getOutputStream());
            assertFalse(streamForEmptyWrite.isThresholdExceeded());
        }
    }

    // Helper methods for creating test streams

    private ThresholdingOutputStream createStreamThatResetsCounterOnThreshold(
            int threshold, AtomicInteger counter, OutputStream targetStream) {
        return new ThresholdingOutputStream(threshold, 
            thresholdingStream -> {
                counter.incrementAndGet();
                thresholdingStream.resetByteCount();
            }, 
            outputStream -> targetStream);
    }

    private ThresholdingOutputStream createStreamWithInitialByteCount(
            int threshold, int initialByteCount, AtomicBoolean thresholdReached, boolean useGetOutputStream) {
        return new ThresholdingOutputStream(threshold) {
            {
                setByteCount(initialByteCount);
            }

            @Override
            protected OutputStream getOutputStream() throws IOException {
                return new ByteArrayOutputStream(4);
            }

            @Override
            protected OutputStream getStream() throws IOException {
                return useGetOutputStream ? getOutputStream() : new ByteArrayOutputStream(4);
            }

            @Override
            protected void thresholdReached() throws IOException {
                thresholdReached.set(true);
            }
        };
    }

    private ThresholdingOutputStream createSimpleThresholdingStream(int threshold, AtomicBoolean thresholdReached) {
        return new ThresholdingOutputStream(threshold) {
            @Override
            protected void thresholdReached() throws IOException {
                super.thresholdReached();
                thresholdReached.set(true);
            }
        };
    }
}