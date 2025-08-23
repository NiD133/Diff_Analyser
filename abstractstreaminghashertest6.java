package com.google.common.hash;

import static java.nio.charset.StandardCharsets.UTF_16LE;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.common.hash.HashCode;
import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.Random;
import junit.framework.TestCase;

/**
 * Tests for {@link AbstractStreamingHasher}, focusing on the equivalence of different string and
 * character input methods.
 */
public class AbstractStreamingHasherTestTest6 extends TestCase {

    /**
     * A test-specific implementation of {@link AbstractStreamingHasher} that acts as a "spy". It
     * captures all processed bytes into a stream, allowing us to verify what data the hasher
     * processed without needing a real hash algorithm. It also tracks invocations of the abstract
     * methods.
     */
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
    }

    /**
     * Verifies that hashing a string via putUnencodedChars, putString, and putBytes (with the
     * corresponding charset) all produce the same result.
     */
    public void testStringInputMethods_produceEquivalentHashes() {
        // Use a fixed seed for reproducible test runs.
        Random random = new Random(1);

        for (int i = 0; i < 100; i++) {
            // ARRANGE: Create a test string.
            // We generate random bytes and interpret them as UTF-16LE. This is a convenient way to
            // create a string where we know its exact byte representation in a 2-byte-per-char
            // encoding. This is important because putUnencodedChars processes Java chars (2 bytes)
            // directly, which is equivalent to UTF-16LE encoding.
            byte[] randomBytes = new byte[64];
            random.nextBytes(randomBytes);
            String testString = new String(randomBytes, UTF_16LE);

            // ACT: Hash the same string using three different but supposedly equivalent methods.
            HashCode hashFromPutUnencodedChars = new Sink(4).putUnencodedChars(testString).hash();
            HashCode hashFromPutString = new Sink(4).putString(testString, UTF_16LE).hash();
            HashCode hashFromPutBytes = new Sink(4).putBytes(testString.getBytes(UTF_16LE)).hash();

            // ASSERT: All three methods should produce the same hash, confirming they processed
            // the exact same sequence of bytes. We use the result from putBytes as the baseline.
            assertEquals(
                "putUnencodedChars should be equivalent to putBytes with UTF_16LE bytes",
                hashFromPutBytes,
                hashFromPutUnencodedChars);

            assertEquals(
                "putString with UTF_16LE should be equivalent to putBytes with UTF_16LE bytes",
                hashFromPutBytes,
                hashFromPutString);
        }
    }
}