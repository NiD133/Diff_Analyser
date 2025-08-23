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

public class AbstractStreamingHasherTestTest3 extends TestCase {

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
            // default implementation pads and calls process()
            assertEquals(before + 1, after);
            // don't count the tail invocation (makes tests a bit more understandable)
            processCalled--;
        }

        // ensures that the number of invocations looks sane
        void assertInvariants(int expectedBytes) {
            // we should have seen as many bytes as the next multiple of chunk after expectedBytes - 1
            assertEquals(out.toByteArray().length, ceilToMultiple(expectedBytes, chunkSize));
            assertEquals(expectedBytes / chunkSize, processCalled);
            assertEquals(expectedBytes % chunkSize != 0, remainingCalled);
        }

        // returns the minimum x such as x >= a && (x % b) == 0
        private static int ceilToMultiple(int a, int b) {
            int remainder = a % b;
            return remainder == 0 ? a : a + b - remainder;
        }

        void assertBytes(byte[] expected) {
            byte[] got = out.toByteArray();
            for (int i = 0; i < expected.length; i++) {
                assertEquals(expected[i], got[i]);
            }
        }
    }

    // Assumes that AbstractNonStreamingHashFunction works properly (must be tested elsewhere!)
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

    public void testInt() {
        Sink sink = new Sink(4);
        sink.putInt(0x04030201);
        HashCode unused = sink.hash();
        sink.assertInvariants(4);
        sink.assertBytes(new byte[] { 1, 2, 3, 4 });
    }
}
