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

    /**
     * Asserts initial state without changing it.
     *
     * @param out the stream to test.
     * @param expectedThreshold the expected threshold.
     * @param expectedByteCount the expected byte count.
     */
    static void assertInitialState(final ThresholdingOutputStream out, final int expectedThreshold, final long expectedByteCount) {
        assertFalse(out.isThresholdExceeded(), "Threshold should not be exceeded initially");
        assertEquals(expectedThreshold, out.getThreshold(), "Incorrect threshold value");
        assertEquals(expectedByteCount, out.getByteCount(), "Incorrect initial byte count");
    }

    @Test
    void resetByteCount_ShouldResetCounterAndAllowReTriggeringThresholdEvent() throws IOException {
        final int threshold = 1;
        final AtomicInteger eventCount = new AtomicInteger();
        try (ByteArrayOutputStream os = new ByteArrayOutputStream();
             ThresholdingOutputStream out = new ThresholdingOutputStream(threshold, tos -> {
                 eventCount.incrementAndGet();
                 tos.resetByteCount();
             }, o -> os)) {

            assertInitialState(out, threshold, 0);

            // First write (count=1): reaches threshold but reset immediately
            out.write('a');
            assertFalse(out.isThresholdExceeded(), "Threshold should not be exceeded after reset");
            assertEquals(0, eventCount.get(), "Event should not be triggered until next write");

            // Second write (count=1 again): should re-trigger threshold event
            out.write('a');
            assertEquals(1, eventCount.get(), "Threshold event should be triggered");
            assertFalse(out.isThresholdExceeded(), "Threshold should not be exceeded after reset");

            // Subsequent writes continue to trigger events
            out.write('a');
            out.write('a');
            assertEquals(3, eventCount.get(), "All threshold events should be counted");
        }
    }

    @Test
    void resetByteCount_WithBrokenOutputStream_ShouldFailGracefully() {
        final int threshold = 1;
        final AtomicInteger eventCount = new AtomicInteger();
        final IOException expected = assertThrows(IOException.class, () -> {
            try (ThresholdingOutputStream out = new ThresholdingOutputStream(threshold, tos -> {
                eventCount.incrementAndGet();
                tos.resetByteCount();
            }, o -> BrokenOutputStream.INSTANCE)) {

                assertInitialState(out, threshold, 0);

                // First write fails (broken stream)
                assertThrows(IOException.class, () -> out.write('a'));
                assertFalse(out.isThresholdExceeded(), "Threshold should not be exceeded on broken stream");
                
                // Second write fails without triggering threshold event
                assertThrows(IOException.class, () -> out.write('a'));
                assertEquals(0, eventCount.get(), "Threshold event should not be triggered");
            }
        });

        assertEquals("Broken output stream: close()", expected.getMessage());
    }

    @Test
    void setByteCount_WhenUsingGetOutputStream_ShouldStartCountingFromInitialValue() throws IOException {
        final AtomicBoolean thresholdReached = new AtomicBoolean();
        final int initialCount = 2;
        final int threshold = 3;
        
        try (ThresholdingOutputStream out = new ThresholdingOutputStream(threshold) {
            {
                setByteCount(initialCount);
            }

            @Override
            protected OutputStream getOutputStream() throws IOException {
                return new ByteArrayOutputStream(4);
            }

            @Override
            protected void thresholdReached() throws IOException {
                thresholdReached.set(true);
            }
        }) {
            assertInitialState(out, threshold, initialCount);
            
            // Write 1 byte (total=3): reaches threshold
            out.write('a');
            assertTrue(thresholdReached.get(), "Threshold should be reached");
            assertTrue(out.isThresholdExceeded(), "Threshold should be exceeded");
        }
    }

    @Test
    void setByteCount_WhenUsingGetStream_ShouldStartCountingFromInitialValue() throws IOException {
        final AtomicBoolean thresholdReached = new AtomicBoolean();
        final int initialCount = 2;
        final int threshold = 3;
        
        try (ThresholdingOutputStream out = new ThresholdingOutputStream(threshold) {
            {
                setByteCount(initialCount);
            }

            @Override
            protected OutputStream getStream() throws IOException {
                return new ByteArrayOutputStream(4);
            }

            @Override
            protected void thresholdReached() throws IOException {
                thresholdReached.set(true);
            }
        }) {
            assertInitialState(out, threshold, initialCount);
            
            // Write 1 byte (total=3): reaches threshold
            out.write('a');
            assertTrue(thresholdReached.get(), "Threshold should be reached");
            assertTrue(out.isThresholdExceeded(), "Threshold should be exceeded");
        }
    }

    @Test
    void thresholdConsumer_WhenNull_ShouldStillExceedThreshold() throws IOException {
        final int threshold = 1;
        try (ThresholdingOutputStream out = new ThresholdingOutputStream(threshold, null, 
            os -> new ByteArrayOutputStream(4))) {
            
            assertInitialState(out, threshold, 0);
            
            // First write doesn't exceed threshold
            out.write('a');
            assertFalse(out.isThresholdExceeded(), "Threshold should not be exceeded");
            
            // Second write exceeds threshold
            out.write('a');
            assertTrue(out.isThresholdExceeded(), "Threshold should be exceeded");
        }
    }

    @Test
    void thresholdConsumer_WhenOutputStreamGetterIsNull_ShouldStillTriggerEvent() throws IOException {
        final AtomicBoolean eventTriggered = new AtomicBoolean();
        final int threshold = 1;
        
        try (ThresholdingOutputStream out = new ThresholdingOutputStream(threshold, 
            os -> eventTriggered.set(true), 
            null)) {
            
            assertInitialState(out, threshold, 0);
            
            // First write doesn't exceed threshold
            out.write('a');
            assertFalse(eventTriggered.get(), "Event should not be triggered");
            assertFalse(out.isThresholdExceeded(), "Threshold should not be exceeded");
            
            // Second write exceeds threshold and triggers event
            out.write('a');
            assertTrue(eventTriggered.get(), "Event should be triggered");
            assertTrue(out.isThresholdExceeded(), "Threshold should be exceeded");
        }
    }

    @Test
    void thresholdConsumer_WithValidConsumers_ShouldTriggerEventWhenThresholdExceeded() throws IOException {
        final AtomicBoolean eventTriggered = new AtomicBoolean();
        final int threshold = 1;
        
        try (ThresholdingOutputStream out = new ThresholdingOutputStream(threshold, 
            os -> eventTriggered.set(true),
            os -> new ByteArrayOutputStream(4))) {
            
            assertInitialState(out, threshold, 0);
            
            // First write doesn't exceed threshold
            out.write('a');
            assertFalse(eventTriggered.get(), "Event should not be triggered");
            assertFalse(out.isThresholdExceeded(), "Threshold should not be exceeded");
            
            // Second write exceeds threshold and triggers event
            out.write('a');
            assertTrue(eventTriggered.get(), "Event should be triggered");
            assertTrue(out.isThresholdExceeded(), "Threshold should be exceeded");
        }
    }

    @Test
    void thresholdConsumer_ThrowingIOException_ShouldPropagateException() throws IOException {
        final int threshold = 1;
        try (ThresholdingOutputStream out = new ThresholdingOutputStream(threshold, 
            os -> { throw new IOException("Threshold reached"); },
            os -> new ByteArrayOutputStream(4))) {
            
            assertInitialState(out, threshold, 0);
            
            // First write doesn't exceed threshold
            out.write('a');
            assertFalse(out.isThresholdExceeded(), "Threshold should not be exceeded");
            
            // Second write should throw during threshold event
            assertThrows(IOException.class, () -> out.write('a'), "Should throw during threshold event");
            assertFalse(out.isThresholdExceeded(), "Threshold should remain unexceeded after error");
        }
    }

    @Test
    void thresholdConsumer_ThrowingUncheckedException_ShouldPropagateException() throws IOException {
        final int threshold = 1;
        try (ThresholdingOutputStream out = new ThresholdingOutputStream(threshold, 
            os -> { throw new IllegalStateException("Threshold reached"); },
            os -> new ByteArrayOutputStream(4))) {
            
            assertInitialState(out, threshold, 0);
            
            // First write doesn't exceed threshold
            out.write('a');
            assertFalse(out.isThresholdExceeded(), "Threshold should not be exceeded");
            
            // Second write should throw during threshold event
            assertThrows(IllegalStateException.class, () -> out.write('a'), "Should throw during threshold event");
            assertFalse(out.isThresholdExceeded(), "Threshold should remain unexceeded after error");
            assertInstanceOf(ByteArrayOutputStream.class, out.getOutputStream());
        }
    }

    @Test
    void negativeThreshold_ShouldTriggerEventImmediatelyOnFirstWrite() throws IOException {
        final AtomicBoolean eventTriggered = new AtomicBoolean();
        try (ThresholdingOutputStream out = new ThresholdingOutputStream(-1) {
            @Override
            protected void thresholdReached() throws IOException {
                eventTriggered.set(true);
            }
        }) {
            assertInitialState(out, 0, 0); // Negative threshold becomes 0
            
            // First write should immediately trigger threshold
            out.write(89);
            assertTrue(eventTriggered.get(), "Threshold event should be triggered");
            assertTrue(out.isThresholdExceeded(), "Threshold should be exceeded");
            assertInstanceOf(NullOutputStream.class, out.getOutputStream());
        }
    }

    @Test
    void zeroThreshold_ShouldTriggerEventImmediatelyOnFirstWrite() throws IOException {
        final AtomicBoolean eventTriggered = new AtomicBoolean();
        final int threshold = 0;
        try (ThresholdingOutputStream out = new ThresholdingOutputStream(threshold) {
            @Override
            protected void thresholdReached() throws IOException {
                eventTriggered.set(true);
            }
        }) {
            assertInitialState(out, threshold, 0);
            
            // First write should immediately trigger threshold
            out.write(89);
            assertTrue(eventTriggered.get(), "Threshold event should be triggered");
            assertTrue(out.isThresholdExceeded(), "Threshold should be exceeded");
            assertInstanceOf(NullOutputStream.class, out.getOutputStream());
        }
    }

    @Test
    void writingZeroBytes_ShouldNotTriggerThresholdEvent() throws IOException {
        final AtomicBoolean eventTriggered = new AtomicBoolean();
        final int threshold = 7;
        try (ThresholdingOutputStream out = new ThresholdingOutputStream(threshold) {
            @Override
            protected void thresholdReached() throws IOException {
                eventTriggered.set(true);
            }
        }) {
            assertInitialState(out, threshold, 0);
            
            // Writing empty array shouldn't trigger threshold
            out.write(new byte[0]);
            assertFalse(eventTriggered.get(), "Threshold event should not be triggered");
            assertFalse(out.isThresholdExceeded(), "Threshold should not be exceeded");
            assertInstanceOf(NullOutputStream.class, out.getOutputStream());
        }
    }
}