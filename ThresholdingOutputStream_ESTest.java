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

import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link ThresholdingOutputStream}.
 */
public class ThresholdingOutputStreamTest {

    /**
     * A test subclass of {@link ThresholdingOutputStream} to verify that protected
     * methods are called correctly.
     */
    private static class TestableThresholdingOutputStream extends ThresholdingOutputStream {
        private boolean thresholdReachedCalled = false;
        private final OutputStream underlyingStream = new ByteArrayOutputStream();

        TestableThresholdingOutputStream(final int threshold) {
            super(threshold);
        }

        @Override
        protected void thresholdReached() {
            this.thresholdReachedCalled = true;
        }

        @Override
        protected OutputStream getStream() {
            return underlyingStream;
        }

        public boolean wasThresholdReachedCalled() {
            return thresholdReachedCalled;
        }
    }

    @Test
    public void constructor_shouldSetPositiveThreshold() {
        // Given
        final int threshold = 123;

        // When
        final ThresholdingOutputStream stream = new ThresholdingOutputStream(threshold);

        // Then
        assertEquals("Threshold should match the constructor value", threshold, stream.getThreshold());
    }

    @Test
    public void constructor_shouldTreatNegativeThresholdAsZero() {
        // Given a negative threshold
        final int negativeThreshold = -100;

        // When
        final ThresholdingOutputStream stream = new ThresholdingOutputStream(negativeThreshold);

        // Then
        assertEquals("A negative threshold should be normalized to 0", 0, stream.getThreshold());
    }

    @Test
    public void getByteCount_shouldBeZero_onNewStream() {
        // Given
        final ThresholdingOutputStream stream = new ThresholdingOutputStream(10);

        // Then
        assertEquals("Byte count should be 0 for a new stream", 0L, stream.getByteCount());
    }

    @Test
    public void write_singleByte_shouldIncrementByteCount() throws IOException {
        // Given
        final ThresholdingOutputStream stream = new ThresholdingOutputStream(10);

        // When
        stream.write(1);

        // Then
        assertEquals("Byte count should be 1 after writing a single byte", 1L, stream.getByteCount());
    }

    @Test
    public void write_byteArray_shouldIncrementByteCount() throws IOException {
        // Given
        final ThresholdingOutputStream stream = new ThresholdingOutputStream(10);
        final byte[] data = new byte[5];

        // When
        stream.write(data);

        // Then
        assertEquals("Byte count should equal the length of the written array", 5L, stream.getByteCount());
    }

    @Test
    public void write_byteArrayWithOffset_shouldIncrementByteCountByLength() throws IOException {
        // Given
        final ThresholdingOutputStream stream = new ThresholdingOutputStream(20);
        final byte[] data = new byte[10];

        // When
        stream.write(data, 2, 5);

        // Then
        assertEquals("Byte count should be incremented by the length argument", 5L, stream.getByteCount());
    }

    @Test
    public void write_zeroLengthByteArray_shouldNotChangeByteCount() throws IOException {
        // Given
        final ThresholdingOutputStream stream = new ThresholdingOutputStream(10);

        // When
        stream.write(new byte[5], 0, 0);

        // Then
        assertEquals("Writing zero bytes should not change the byte count", 0L, stream.getByteCount());
    }

    @Test
    public void isThresholdExceeded_shouldBeFalse_whenBelowThreshold() throws IOException {
        // Given
        final ThresholdingOutputStream stream = new ThresholdingOutputStream(10);

        // When
        stream.write(new byte[9]);

        // Then
        assertFalse("Threshold should not be exceeded when byte count is below threshold", stream.isThresholdExceeded());
    }

    @Test
    public void isThresholdExceeded_shouldBeTrue_whenThresholdIsMet() throws IOException {
        // Given
        final ThresholdingOutputStream stream = new ThresholdingOutputStream(10);

        // When
        stream.write(new byte[10]);

        // Then
        assertTrue("Threshold should be exceeded when byte count equals threshold", stream.isThresholdExceeded());
    }

    @Test
    public void isThresholdExceeded_shouldBeTrue_whenThresholdIsExceeded() throws IOException {
        // Given
        final ThresholdingOutputStream stream = new ThresholdingOutputStream(10);

        // When
        stream.write(new byte[11]);

        // Then
        assertTrue("Threshold should be exceeded when byte count is over threshold", stream.isThresholdExceeded());
    }

    @Test
    public void write_shouldTriggerThresholdReached_whenThresholdIsExceeded() throws IOException {
        // Given
        final int threshold = 10;
        final TestableThresholdingOutputStream stream = new TestableThresholdingOutputStream(threshold);

        // When writing enough bytes to exceed the threshold
        stream.write(new byte[threshold + 1]);

        // Then
        assertTrue("thresholdReached() should have been called", stream.wasThresholdReachedCalled());
        assertTrue("isThresholdExceeded() should return true", stream.isThresholdExceeded());
    }

    @Test
    public void write_shouldNotTriggerThresholdReached_whenBelowThreshold() throws IOException {
        // Given
        final int threshold = 10;
        final TestableThresholdingOutputStream stream = new TestableThresholdingOutputStream(threshold);

        // When writing fewer bytes than the threshold
        stream.write(new byte[threshold - 1]);

        // Then
        assertFalse("thresholdReached() should not have been called", stream.wasThresholdReachedCalled());
        assertFalse("isThresholdExceeded() should return false", stream.isThresholdExceeded());
    }

    @Test
    public void resetByteCount_shouldSetByteCountToZero() throws IOException {
        // Given
        final TestableThresholdingOutputStream stream = new TestableThresholdingOutputStream(10);
        stream.write(new byte[5]);
        assertEquals(5L, stream.getByteCount());

        // When
        stream.resetByteCount();

        // Then
        assertEquals("Byte count should be 0 after reset", 0L, stream.getByteCount());
    }

    @Test
    public void setByteCount_shouldUpdateByteCount() {
        // Given
        final TestableThresholdingOutputStream stream = new TestableThresholdingOutputStream(10);

        // When
        stream.setByteCount(5L);

        // Then
        assertEquals("Byte count should be updated to the new value", 5L, stream.getByteCount());
    }

    @Test
    public void getOutputStream_shouldReturnNonNullStreamByDefault() throws IOException {
        // Given
        final ThresholdingOutputStream stream = new ThresholdingOutputStream(10);

        // When
        final OutputStream underlying = stream.getOutputStream();

        // Then
        assertNotNull("Default underlying stream should not be null", underlying);
        // The default is NullOutputStream, which is a singleton
        assertEquals("Default stream should be NullOutputStream.INSTANCE", NullOutputStream.INSTANCE, underlying);
    }

    @Test
    public void flushAndClose_shouldBeNoOps_onDefaultStream() throws IOException {
        // Given
        final ThresholdingOutputStream stream = new ThresholdingOutputStream(10);

        // When/Then: No exceptions should be thrown
        stream.flush();
        stream.close();
    }
}