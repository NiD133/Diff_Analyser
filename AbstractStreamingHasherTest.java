/*
 * Copyright (C) 2011 The Guava Authors
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

package com.google.common.hash;

import static java.nio.charset.StandardCharsets.UTF_16LE;
import static org.junit.Assert.assertThrows;

import com.google.common.collect.Iterables;
import com.google.common.hash.HashTestUtils.RandomHasherAction;
import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import junit.framework.TestCase;
import org.jspecify.annotations.NullUnmarked;

/**
 * Tests for AbstractStreamingHasher.
 *
 * @author Dimitris Andreou
 */
@NullUnmarked
public class AbstractStreamingHasherTest extends TestCase {

    // Test putting byte arrays
    public void testPutBytes() {
        // Arrange
        Sink sink = new Sink(4); // byte order insignificant here
        byte[] expected = {1, 2, 3, 4, 5, 6, 7, 8};
        
        // Act
        sink.putByte((byte) 1);
        sink.putBytes(new byte[] {2, 3, 4, 5, 6});
        sink.putByte((byte) 7);
        sink.putBytes(new byte[] {});
        sink.putBytes(new byte[] {8});
        HashCode unused = sink.hash();
        
        // Assert
        sink.assertInvariants(8);
        sink.assertBytes(expected);
    }

    // Test putting shorts
    public void testPutShort() {
        // Arrange
        Sink sink = new Sink(4);
        
        // Act
        sink.putShort((short) 0x0201);
        HashCode unused = sink.hash();
        
        // Assert
        sink.assertInvariants(2);
        sink.assertBytes(new byte[] {1, 2, 0, 0}); // padded with zeros
    }

    // Test putting integers
    public void testPutInt() {
        // Arrange
        Sink sink = new Sink(4);
        
        // Act
        sink.putInt(0x04030201);
        HashCode unused = sink.hash();
        
        // Assert
        sink.assertInvariants(4);
        sink.assertBytes(new byte[] {1, 2, 3, 4});
    }

    // Test putting longs
    public void testPutLong() {
        // Arrange
        Sink sink = new Sink(8);
        
        // Act
        sink.putLong(0x0807060504030201L);
        HashCode unused = sink.hash();
        
        // Assert
        sink.assertInvariants(8);
        sink.assertBytes(new byte[] {1, 2, 3, 4, 5, 6, 7, 8});
    }

    // Test putting characters
    public void testPutChar() {
        // Arrange
        Sink sink = new Sink(4);
        
        // Act
        sink.putChar((char) 0x0201);
        HashCode unused = sink.hash();
        
        // Assert
        sink.assertInvariants(2);
        sink.assertBytes(new byte[] {1, 2, 0, 0}); // padded with zeros
    }

    // Test putting strings
    public void testPutString() {
        // Arrange
        Random random = new Random();
        
        // Act and Assert
        for (int i = 0; i < 100; i++) {
            byte[] bytes = new byte[64];
            random.nextBytes(bytes);
            String s = new String(bytes, UTF_16LE); // so all random strings are valid
            assertEquals(
                    new Sink(4).putUnencodedChars(s).hash(),
                    new Sink(4).putBytes(s.getBytes(UTF_16LE)).hash());
            assertEquals(
                    new Sink(4).putUnencodedChars(s).hash(), new Sink(4).putString(s, UTF_16LE).hash());
        }
    }

    // Test putting floats
    public void testPutFloat() {
        // Arrange
        Sink sink = new Sink(4);
        
        // Act
        sink.putFloat(Float.intBitsToFloat(0x04030201));
        HashCode unused = sink.hash();
        
        // Assert
        sink.assertInvariants(4);
        sink.assertBytes(new byte[] {1, 2, 3, 4});
    }

    // Test putting doubles
    public void testPutDouble() {
        // Arrange
        Sink sink = new Sink(8);
        
        // Act
        sink.putDouble(Double.longBitsToDouble(0x0807060504030201L));
        HashCode unused = sink.hash();
        
        // Assert
        sink.assertInvariants(8);
        sink.assertBytes(new byte[] {1, 2, 3, 4, 5, 6, 7, 8});
    }

    // Test correct exception throwing
    public void testCorrectExceptions() {
        // Arrange
        Sink sink = new Sink(4);
        
        // Act and Assert
        assertThrows(IndexOutOfBoundsException.class, () -> sink.putBytes(new byte[8], -1, 4));
        assertThrows(IndexOutOfBoundsException.class, () -> sink.putBytes(new byte[8], 0, 16));
        assertThrows(IndexOutOfBoundsException.class, () -> sink.putBytes(new byte[8], 0, -1));
    }

    /**
     * This test creates a long random sequence of inputs, then a lot of differently configured sinks
     * process it; all should produce the same answer, the only difference should be the number of
     * process()/processRemaining() invocations, due to alignment.
     */
    @AndroidIncompatible // slow. TODO(cpovirk): Maybe just reduce iterations under Android.
    public void testExhaustive() throws Exception {
        // Arrange
        Random random = new Random(0); // will iteratively make more debuggable, each time it breaks
        List<Sink> sinks = new ArrayList<>();
        
        // Create sinks with different chunk sizes and buffer sizes
        for (int chunkSize = 4; chunkSize <= 32; chunkSize++) {
            for (int bufferSize = chunkSize; bufferSize <= chunkSize * 4; bufferSize += chunkSize) {
                sinks.add(new Sink(chunkSize, bufferSize));
            }
        }
        
        Control control = new Control();
        Hasher controlSink = control.newHasher(1024);
        
        // Perform random actions on sinks and control
        for (int totalInsertions = 0; totalInsertions < 200; totalInsertions++) {
            Iterable<Hasher> sinksAndControl =
                    Iterables.concat(sinks, Collections.singleton(controlSink));
            for (int insertion = 0; insertion < totalInsertions; insertion++) {
                RandomHasherAction.pickAtRandom(random).performAction(random, sinksAndControl);
            }
            
            // Ensure at least 4 bytes have been put into the hasher
            int intToPut = random.nextInt();
            for (Hasher hasher : sinksAndControl) {
                hasher.putInt(intToPut);
            }
            
            // Hash and assert invariants for sinks
            for (Sink sink : sinks) {
                HashCode unused = sink.hash();
                sink.assertInvariants(controlSink.hash().asBytes().length);
                sink.assertBytes(controlSink.hash().asBytes());
            }
        }
    }

    private static class Sink extends AbstractStreamingHasher {
        final int chunkSize;
        final int bufferSize;
        final ByteArrayOutputStream out = new ByteArrayOutputStream();

        int processCalled = 0;
        boolean remainingCalled = false;

        Sink(int chunkSize, int bufferSize) {
            super(chunkSize, bufferSize);
            this.chunkSize = chunkSize;
            this.bufferSize = bufferSize;
        }

        Sink(int chunkSize) {
            super(chunkSize);
            this.chunkSize = chunkSize;
            this.bufferSize = chunkSize;
        }

        @Override
        protected HashCode makeHash() {
            return HashCode.fromBytes(out.toByteArray());
        }

        @Override
        protected void process(ByteBuffer bb) {
            processCalled++;
            assertEquals(ByteOrder.LITTLE_ENDIAN, bb.order());
            assertTrue(bb.remaining() >= chunkSize);
            for (int i = 0; i < chunkSize; i++) {
                out.write(bb.get());
            }
        }

        @Override
        protected void processRemaining(ByteBuffer bb) {
            assertFalse(remainingCalled);
            remainingCalled = true;
            assertEquals(ByteOrder.LITTLE_ENDIAN, bb.order());
            assertTrue(bb.remaining() > 0);
            assertTrue(bb.remaining() < bufferSize);
            int before = processCalled;
            super.processRemaining(bb);
            int after = processCalled;
            assertEquals(before + 1, after); // default implementation pads and calls process()
            processCalled--; // don't count the tail invocation (makes tests a bit more understandable)
        }

        // Assert invariants and bytes
        void assertInvariants(int expectedBytes) {
            assertEquals(out.toByteArray().length, ceilToMultiple(expectedBytes, chunkSize));
            assertEquals(expectedBytes / chunkSize, processCalled);
            assertEquals(expectedBytes % chunkSize != 0, remainingCalled);
        }

        void assertBytes(byte[] expected) {
            byte[] got = out.toByteArray();
            for (int i = 0; i < expected.length; i++) {
                assertEquals(expected[i], got[i]);
            }
        }

        // Calculate the minimum x such that x >= a && (x % b) == 0
        private static int ceilToMultiple(int a, int b) {
            int remainder = a % b;
            return remainder == 0 ? a : a + b - remainder;
        }
    }

    private static class Control extends AbstractNonStreamingHashFunction {
        @Override
        public HashCode hashBytes(byte[] input, int off, int len) {
            return HashCode.fromBytes(Arrays.copyOfRange(input, off, off + len));
        }

        @Override
        public int bits() {
            throw new UnsupportedOperationException();
        }
    }
}