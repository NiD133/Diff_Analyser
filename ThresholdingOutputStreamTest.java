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
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ThresholdingOutputStream}.
 * These tests verify the core thresholding logic, including functional constructors and byte count management.
 *
 * @see DeferredFileOutputStreamTest
 */
class ThresholdingOutputStreamTest {

    /**
     * Asserts the initial state of a ThresholdingOutputStream without altering it.
     *
     * @param stream The stream to test.
     * @param expectedThreshold The expected threshold.
     * @param expectedByteCount The expected byte count.
     */
    private void assertInitialState(final ThresholdingOutputStream stream, final int expectedThreshold, final long expectedByteCount) {
        assertFalse(stream.isThresholdExceeded(), "Initial state: threshold should not be exceeded.");
        assertEquals(expectedThreshold, stream.getThreshold(), "Initial state: threshold should match constructor.");
        assertEquals(expectedByteCount, stream.getByteCount(), "Initial state: byte count should be as expected.");
    }

    /**
     * Tests that a negative threshold in the constructor is treated as zero.
     */
    @Test
    @DisplayName("Constructor with negative threshold should treat it as zero")
    void constructor_withNegativeThreshold_treatsThresholdAsZero() throws IOException {
        // Arrange
        final AtomicBoolean thresholdReached = new AtomicBoolean(false);
        try (final ThresholdingOutputStream tos = new ThresholdingOutputStream(-1) {
            @Override
            protected void thresholdReached() {
                thresholdReached.set(true);
            }
        }) {
            // Assert initial state: threshold is corrected to 0
            assertInitialState(tos, 0, 0);
            assertFalse(thresholdReached.get());

            // Act: Write one byte. Since the effective threshold is 0, any write will exceed it.
            tos.write(1);

            // Assert
            assertTrue(thresholdReached.get(), "thresholdReached() should have been called.");
            assertTrue(tos.isThresholdExceeded());
            assertInstanceOf(NullOutputStream.class, tos.getOutputStream(), "Default stream should be NullOutputStream.");
        }
    }

    /**
     * Tests that when the threshold is zero, the very first write triggers the threshold event.
     */
    @Test
    @DisplayName("When threshold is zero, the first write should trigger the threshold")
    void write_whenThresholdIsZero_triggersThresholdImmediately() throws IOException {
        // Arrange
        final int threshold = 0;
        final AtomicBoolean thresholdReached = new AtomicBoolean(false);
        try (final ThresholdingOutputStream tos = new ThresholdingOutputStream(threshold) {
            @Override
            protected void thresholdReached() {
                thresholdReached.set(true);
            }
        }) {
            assertInitialState(tos, threshold, 0);
            assertFalse(thresholdReached.get());

            // Act: Write one byte. Since threshold is 0, this will exceed it.
            tos.write(1);

            // Assert
            assertTrue(thresholdReached.get(), "thresholdReached() should have been called.");
            assertTrue(tos.isThresholdExceeded());
            assertInstanceOf(NullOutputStream.class, tos.getOutputStream());
        }
    }

    /**
     * Tests that writing a zero-length byte array does not increment the byte count
     * or trigger the threshold.
     */
    @Test
    @DisplayName("Writing an empty byte array should not increment byte count or trigger threshold")
    void write_emptyByteArray_doesNotTriggerThreshold() throws IOException {
        // Arrange
        final int threshold = 7;
        final AtomicBoolean thresholdReached = new AtomicBoolean(false);
        try (final ThresholdingOutputStream tos = new ThresholdingOutputStream(threshold) {
            @Override
            protected void thresholdReached() {
                super.thresholdReached(); // In this test, this calls a no-op consumer.
                thresholdReached.set(true);
            }
        }) {
            assertInitialState(tos, threshold, 0);
            assertFalse(thresholdReached.get());

            // Act: Write a zero-length byte array.
            tos.write(new byte[0]);

            // Assert: The state should remain unchanged.
            assertFalse(tos.isThresholdExceeded(), "Threshold should not be exceeded by a zero-byte write.");
            assertFalse(thresholdReached.get(), "thresholdReached() should not have been called.");
            assertEquals(0, tos.getByteCount());
        }
    }

    /**
     * Tests that setting an initial byte count is correctly factored into the threshold calculation.
     * This version uses the modern {@code getOutputStream()} method.
     */
    @Test
    @DisplayName("setByteCount should correctly initialize byte count for threshold check")
    void setByteCount_withInitialCount_triggersThresholdOnSubsequentWrite() throws IOException {
        // Arrange
        final int threshold = 3;
        final int initialByteCount = 2;
        final AtomicBoolean thresholdReached = new AtomicBoolean(false);

        try (final ThresholdingOutputStream tos = new ThresholdingOutputStream(threshold) {
            // Anonymous inner class to set initial state and override protected methods
            {
                setByteCount(initialByteCount);
            }

            @Override
            protected OutputStream getOutputStream() {
                return new ByteArrayOutputStream();
            }

            @Override
            protected void thresholdReached() {
                thresholdReached.set(true);
            }
        }) {
            assertInitialState(tos, threshold, initialByteCount);
            assertFalse(thresholdReached.get());

            // Act: Write one byte. Total bytes (2+1=3) will not exceed threshold (3 > 3 is false).
            tos.write('a');

            // Assert
            assertFalse(thresholdReached.get(), "Threshold should not be reached at exactly the threshold value.");
            assertFalse(tos.isThresholdExceeded());
            assertEquals(initialByteCount + 1, tos.getByteCount());

            // Act: Write another byte. Total bytes (3+1=4) will exceed threshold (3).
            tos.write('a');

            // Assert
            assertTrue(thresholdReached.get(), "thresholdReached() should have been called.");
            assertTrue(tos.isThresholdExceeded());
            assertEquals(initialByteCount + 2, tos.getByteCount());
        }
    }

    /**
     * Tests that setting an initial byte count works with the deprecated {@code getStream()} method
     * for backward compatibility.
     */
    @Test
    @DisplayName("setByteCount should work with deprecated getStream()")
    void setByteCount_withDeprecatedGetStream_triggersThresholdOnSubsequentWrite() throws IOException {
        // Arrange
        final int threshold = 3;
        final int initialByteCount = 2;
        final AtomicBoolean thresholdReached = new AtomicBoolean(false);

        try (final ThresholdingOutputStream tos = new ThresholdingOutputStream(threshold) {
            {
                setByteCount(initialByteCount);
            }

            @Override
            protected OutputStream getStream() { // Testing deprecated method
                return new ByteArrayOutputStream();
            }

            @Override
            protected void thresholdReached() {
                thresholdReached.set(true);
            }
        }) {
            assertInitialState(tos, threshold, initialByteCount);

            // Act & Assert
            tos.write('a'); // count = 3
            assertFalse(thresholdReached.get());
            tos.write('a'); // count = 4, exceeds threshold
            assertTrue(thresholdReached.get());
        }
    }

    /**
     * Tests that calling {@code resetByteCount()} within the threshold consumer allows the
     * threshold to be triggered multiple times.
     */
    @Test
    @DisplayName("resetByteCount in threshold consumer allows the threshold to be triggered multiple times")
    void resetByteCount_inThresholdConsumer_allowsMultipleTriggers() throws IOException {
        // Arrange
        final int threshold = 1;
        final AtomicInteger thresholdReachedCount = new AtomicInteger();
        // This consumer is called when the threshold is exceeded. It increments a counter
        // and resets the stream's byte count, which also resets the 'thresholdExceeded' flag.
        final IOConsumer<ThresholdingOutputStream> consumer = tos -> {
            thresholdReachedCount.incrementAndGet();
            tos.resetByteCount();
        };

        try (final ByteArrayOutputStream underlyingStream = new ByteArrayOutputStream();
             final ThresholdingOutputStream tos = new ThresholdingOutputStream(threshold, consumer, o -> underlyingStream)) {

            assertInitialState(tos, threshold, 0);
            assertEquals(0, thresholdReachedCount.get());

            // Act & Assert
            // 1st write: count becomes 1. Not > threshold.
            tos.write('a');
            assertFalse(tos.isThresholdExceeded());
            assertEquals(0, thresholdReachedCount.get());

            // 2nd write: count would be 2. (2 > 1) is true. Threshold reached.
            // Consumer runs: counter becomes 1, count is reset to 0. Then write happens, count becomes 1.
            tos.write('a');
            assertEquals(1, thresholdReachedCount.get());
            assertFalse(tos.isThresholdExceeded(), "Flag should be reset by consumer");

            // 3rd write: count is 1, would be 2. (2 > 1) is true. Threshold reached again.
            // Consumer runs: counter becomes 2, count is reset. Then write happens, count becomes 1.
            tos.write('a');
            assertEquals(2, thresholdReachedCount.get());

            // 4th write: count is 1, would be 2. (2 > 1) is true. Threshold reached again.
            tos.write('a');
            assertEquals(3, thresholdReachedCount.get());
        }
    }

    /**
     * Tests that the functional constructor works as expected with non-null arguments.
     */
    @Test
    @DisplayName("Functional constructor should trigger consumer when threshold is exceeded")
    void constructor_withFunctionalArgs_triggersConsumerWhenThresholdExceeded() throws IOException {
        // Arrange
        final int threshold = 1;
        final AtomicBoolean reached = new AtomicBoolean(false);
        try (final ThresholdingOutputStream tos = new ThresholdingOutputStream(threshold, os -> reached.set(true), os -> new ByteArrayOutputStream())) {
            assertInitialState(tos, threshold, 0);

            // Act: Write until just before the threshold is exceeded.
            tos.write('a'); // count = 1
            assertFalse(reached.get());
            assertFalse(tos.isThresholdExceeded());

            // Act: Exceed the threshold.
            tos.write('a'); // count = 2

            // Assert
            assertTrue(reached.get(), "Threshold consumer should have been called.");
            assertTrue(tos.isThresholdExceeded());
        }
    }

    /**
     * Tests that a null threshold consumer defaults to a no-op and does not cause an error.
     */
    @Test
    @DisplayName("Constructor with null threshold consumer should default to no-op")
    void constructor_withNullConsumer_usesNoOpConsumer() throws IOException {
        // Arrange
        final int threshold = 1;
        try (final ThresholdingOutputStream tos = new ThresholdingOutputStream(threshold, null, os -> new ByteArrayOutputStream())) {
            assertInitialState(tos, threshold, 0);

            // Act: Write enough to exceed the threshold.
            tos.write('a'); // count = 1
            tos.write('a'); // count = 2, exceeds threshold

            // Assert
            assertTrue(tos.isThresholdExceeded(), "Threshold should be exceeded, and no exception should be thrown.");
        }
    }

    /**
     * Tests that a null output stream getter defaults to using a {@link NullOutputStream}.
     */
    @Test
    @DisplayName("Constructor with null output stream getter should default to NullOutputStream")
    void constructor_withNullOutputStreamGetter_usesNullOutputStream() throws IOException {
        // Arrange
        final int threshold = 1;
        final AtomicBoolean reached = new AtomicBoolean(false);
        try (final ThresholdingOutputStream tos = new ThresholdingOutputStream(threshold, os -> reached.set(true), null)) {
            assertInitialState(tos, threshold, 0);

            // Act: Exceed the threshold.
            tos.write('a'); // count = 1
            tos.write('a'); // count = 2

            // Assert
            assertTrue(reached.get(), "Threshold consumer should have been called.");
            assertTrue(tos.isThresholdExceeded());
            assertInstanceOf(NullOutputStream.class, tos.getOutputStream(), "Underlying stream should be NullOutputStream.");
        }
    }

    /**
     * Tests that an {@link IOException} thrown by the threshold consumer propagates correctly.
     */
    @Test
    @DisplayName("When threshold consumer throws IOException, it should propagate")
    void thresholdConsumer_throwingIOException_propagatesException() throws IOException {
        // Arrange
        final int threshold = 1;
        final IOException expectedException = new IOException("Threshold reached.");
        try (final ThresholdingOutputStream tos = new ThresholdingOutputStream(threshold, os -> {
            throw expectedException;
        }, os -> new ByteArrayOutputStream())) {

            assertInitialState(tos, threshold, 0);
            tos.write('a'); // Does not throw.
            assertFalse(tos.isThresholdExceeded());

            // Act & Assert: This write triggers the consumer, which throws the exception.
            final IOException thrown = assertThrows(IOException.class, () -> tos.write('a'));
            assertEquals(expectedException, thrown);

            // The 'thresholdExceeded' flag is set *after* the consumer is called.
            // If the consumer throws, the flag should remain false.
            assertFalse(tos.isThresholdExceeded(), "Flag should not be set if consumer throws.");
        }
    }

    /**
     * Tests that an unchecked exception thrown by the threshold consumer propagates correctly.
     */
    @Test
    @DisplayName("When threshold consumer throws UncheckedException, it should propagate")
    void thresholdConsumer_throwingUncheckedException_propagatesException() {
        // Arrange
        final int threshold = 1;
        final IllegalStateException expectedException = new IllegalStateException("Threshold reached.");
        try (final ThresholdingOutputStream tos = new ThresholdingOutputStream(threshold, os -> {
            throw expectedException;
        }, os -> new ByteArrayOutputStream())) {

            assertInitialState(tos, threshold, 0);
            tos.write('a'); // Does not throw.
            assertFalse(tos.isThresholdExceeded());

            // Act & Assert
            final IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> tos.write('a'));
            assertEquals(expectedException, thrown);
            assertFalse(tos.isThresholdExceeded(), "Flag should not be set if consumer throws.");

        } catch (final IOException e) {
            fail("Test should not have thrown an IOException", e);
        }
    }

    /**
     * Tests that when the underlying stream is broken, writes fail, the threshold consumer
     * is not called, and an exception is thrown upon closing the stream.
     */
    @Test
    @DisplayName("When underlying stream is broken, write fails and threshold consumer is not called")
    void write_toBrokenStream_doesNotTriggerConsumerAndThrowsOnClose() {
        // Arrange
        final int threshold = 1;
        final AtomicInteger thresholdReachedCount = new AtomicInteger();
        final IOConsumer<ThresholdingOutputStream> consumer = tos -> thresholdReachedCount.incrementAndGet();

        // Act & Assert
        // The whole try-with-resources block is expected to throw an IOException upon closing the broken stream.
        final IOException e = assertThrows(IOException.class, () -> {
            try (final ThresholdingOutputStream tos = new ThresholdingOutputStream(threshold, consumer, o -> BrokenOutputStream.INSTANCE)) {
                assertInitialState(tos, threshold, 0);

                // This write will fail because the underlying stream throws on write.
                // The byte count is not incremented, so the threshold is not reached.
                assertThrows(IOException.class, () -> tos.write('a'));
                assertEquals(0, thresholdReachedCount.get(), "Threshold consumer should not be called on a failed write.");
                assertFalse(tos.isThresholdExceeded());
            } // tos.close() is called here, which calls BrokenOutputStream.close(), throwing the final exception.
        });

        // Assert that the exception came from the close() method of the broken stream.
        assertEquals("Broken output stream: close()", e.getMessage());
        assertEquals(0, thresholdReachedCount.get(), "Threshold consumer should never have been called.");
    }
}