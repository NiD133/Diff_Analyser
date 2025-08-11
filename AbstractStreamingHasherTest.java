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
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

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
import org.jspecify.annotations.NullUnmarked;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Tests for {@link AbstractStreamingHasher}.
 *
 * @author Dimitris Andreou
 */
@NullUnmarked
@RunWith(JUnit4.class)
public class AbstractStreamingHasherTest {

  @Test
  public void putBytes_withVariousInputs_writesAllBytesInOrder() {
    StreamingHasherSpy hasher = new StreamingHasherSpy(4); // chunk size is arbitrary here
    byte[] expected = {1, 2, 3, 4, 5, 6, 7, 8};

    hasher.putByte((byte) 1);
    hasher.putBytes(new byte[] {2, 3, 4, 5, 6});
    hasher.putByte((byte) 7);
    hasher.putBytes(new byte[] {}); // empty array should be a no-op
    hasher.putBytes(new byte[] {8});
    hasher.hash();

    hasher.assertInvariants(8);
    hasher.assertOutput(expected);
  }

  @Test
  public void putShort_writesLittleEndianBytes() {
    StreamingHasherSpy hasher = new StreamingHasherSpy(4);
    hasher.putShort((short) 0x0201);
    hasher.hash();

    // Should be processed by processRemaining, as 2 bytes < 4-byte chunk size.
    hasher.assertInvariants(2);
    // The default processRemaining pads with zeros to the chunk size.
    byte[] expected = {0x01, 0x02, 0, 0};
    hasher.assertOutput(expected);
  }

  @Test
  public void putChar_writesLittleEndianBytes() {
    StreamingHasherSpy hasher = new StreamingHasherSpy(4);
    hasher.putChar((char) 0x0201);
    hasher.hash();

    // Should be processed by processRemaining, as 2 bytes < 4-byte chunk size.
    hasher.assertInvariants(2);
    // The default processRemaining pads with zeros to the chunk size.
    byte[] expected = {0x01, 0x02, 0, 0};
    hasher.assertOutput(expected);
  }

  @Test
  public void putInt_writesLittleEndianBytes() {
    StreamingHasherSpy hasher = new StreamingHasherSpy(4);
    hasher.putInt(0x04030201);
    hasher.hash();

    // Should be processed by process(), as 4 bytes == 4-byte chunk size.
    hasher.assertInvariants(4);
    byte[] expected = {0x01, 0x02, 0x03, 0x04};
    hasher.assertOutput(expected);
  }

  @Test
  public void putFloat_writesLittleEndianBytes() {
    StreamingHasherSpy hasher = new StreamingHasherSpy(4);
    // A float is just its 4-byte integer representation.
    hasher.putFloat(Float.intBitsToFloat(0x04030201));
    hasher.hash();

    // Should be processed by process(), as 4 bytes == 4-byte chunk size.
    hasher.assertInvariants(4);
    byte[] expected = {0x01, 0x02, 0x03, 0x04};
    hasher.assertOutput(expected);
  }

  @Test
  public void putLong_writesLittleEndianBytes() {
    StreamingHasherSpy hasher = new StreamingHasherSpy(8);
    hasher.putLong(0x0807060504030201L);
    hasher.hash();

    // Should be processed by process(), as 8 bytes == 8-byte chunk size.
    hasher.assertInvariants(8);
    byte[] expected = {0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08};
    hasher.assertOutput(expected);
  }

  @Test
  public void putDouble_writesLittleEndianBytes() {
    StreamingHasherSpy hasher = new StreamingHasherSpy(8);
    // A double is just its 8-byte long representation.
    hasher.putDouble(Double.longBitsToDouble(0x0807060504030201L));
    hasher.hash();

    // Should be processed by process(), as 8 bytes == 8-byte chunk size.
    hasher.assertInvariants(8);
    byte[] expected = {0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08};
    hasher.assertOutput(expected);
  }

  @Test
  public void putString_isEquivalentToPutBytesWithSameCharset() {
    Random random = new Random(123);
    for (int i = 0; i < 100; i++) {
      byte[] bytes = new byte[64];
      random.nextBytes(bytes);
      // Use an encoding that can represent any random byte sequence to avoid invalid characters.
      String s = new String(bytes, UTF_16LE);

      HashCode hashFromString = new StreamingHasherSpy(4).putString(s, UTF_16LE).hash();
      HashCode hashFromBytes = new StreamingHasherSpy(4).putBytes(s.getBytes(UTF_16LE)).hash();
      HashCode hashFromUnencodedChars = new StreamingHasherSpy(4).putUnencodedChars(s).hash();

      assertEquals(hashFromBytes, hashFromString);
      assertEquals(hashFromBytes, hashFromUnencodedChars);
    }
  }

  @Test
  public void putBytes_withInvalidParameters_throwsIndexOutOfBoundsException() {
    StreamingHasherSpy hasher = new StreamingHasherSpy(4);
    byte[] bytes = new byte[8];
    assertThrows(IndexOutOfBoundsException.class, () -> hasher.putBytes(bytes, -1, 4));
    assertThrows(IndexOutOfBoundsException.class, () -> hasher.putBytes(bytes, 0, 16));
    assertThrows(IndexOutOfBoundsException.class, () -> hasher.putBytes(bytes, 0, -1));
  }

  /**
   * This is a comprehensive test to ensure that the buffering and chunking logic of
   * AbstractStreamingHasher is correct, regardless of the chunk and buffer sizes.
   *
   * <p>It works by:
   *
   * <ol>
   *   <li>Creating a large number of streaming hashers with various chunk/buffer size
   *       configurations.
   *   <li>Creating a simple, non-streaming "reference" hasher that computes the hash directly from
   *       the final byte array.
   *   <li>Applying an identical, long, and random sequence of `put` operations to all hashers.
   *   <li>Asserting that every streaming hasher produces the exact same hash code as the reference
   *       hasher.
   * </ol>
   *
   * This should catch any bugs related to buffering, chunk processing, and handling of remaining
   * bytes.
   */
  @Test
  @AndroidIncompatible // slow. TODO(cpovirk): Maybe just reduce iterations under Android.
  public void hashingWithVariousChunkAndBufferSizes_producesConsistentResult() {
    Random random = new Random(0); // Fixed seed for reproducible tests.
    int maxActionsPerSession = 200;

    for (int numActions = 0; numActions < maxActionsPerSession; numActions++) {
      List<StreamingHasherSpy> streamingHashers = new ArrayList<>();
      // Test various chunk and buffer sizes to cover different alignment scenarios.
      for (int chunkSize = 4; chunkSize <= 32; chunkSize++) {
        for (int bufferSize = chunkSize; bufferSize <= chunkSize * 4; bufferSize += chunkSize) {
          streamingHashers.add(new StreamingHasherSpy(chunkSize, bufferSize));
        }
      }

      // The reference hasher provides the "correct" result by simply concatenating all inputs.
      ReferenceHashFunction referenceHashFunction = new ReferenceHashFunction();
      Hasher referenceHasher = referenceHashFunction.newHasher();

      Iterable<Hasher> allHashers =
          Iterables.concat(streamingHashers, Collections.singleton(referenceHasher));

      // Perform a random sequence of hashing actions on all hashers.
      for (int i = 0; i < numActions; i++) {
        RandomHasherAction.pickAtRandom(random).performAction(random, allHashers);
      }

      // Ensure at least 4 bytes are hashed, as some hash functions might require it.
      int finalInt = random.nextInt();
      for (Hasher hasher : allHashers) {
        hasher.putInt(finalInt);
      }

      // Finalize all hashes and get the expected result from the reference.
      byte[] expectedHashBytes = referenceHasher.hash().asBytes();

      // Verify that every streaming hasher produced the same result.
      for (StreamingHasherSpy hasher : streamingHashers) {
        hasher.hash(); // Finalize the hash
        hasher.assertInvariants(expectedHashBytes.length);
        hasher.assertOutput(expectedHashBytes);
      }
    }
  }

  /**
   * A test spy implementation of {@link AbstractStreamingHasher}. It captures all processed bytes
   * into a stream and records invocations of {@code process} and {@code processRemaining} to allow
   * for verifying the abstract class's internal behavior.
   */
  private static class StreamingHasherSpy extends AbstractStreamingHasher {
    private final ByteArrayOutputStream processedBytesStream = new ByteArrayOutputStream();
    private int processCallCount = 0;
    private boolean processRemainingCalled = false;

    StreamingHasherSpy(int chunkSize) {
      super(chunkSize);
    }

    StreamingHasherSpy(int chunkSize, int bufferSize) {
      super(chunkSize, bufferSize);
    }

    @Override
    protected void process(ByteBuffer bb) {
      processCallCount++;
      assertEquals("Buffer must be in little-endian order", ByteOrder.LITTLE_ENDIAN, bb.order());
      assertTrue(
          "Buffer must have at least one full chunk remaining", bb.remaining() >= chunkSize);
      for (int i = 0; i < chunkSize; i++) {
        processedBytesStream.write(bb.get());
      }
    }

    @Override
    protected void processRemaining(ByteBuffer bb) {
      assertFalse("processRemaining should only be called once", processRemainingCalled);
      processRemainingCalled = true;
      assertEquals("Buffer must be in little-endian order", ByteOrder.LITTLE_ENDIAN, bb.order());
      assertTrue("Buffer for remaining bytes must not be empty", bb.remaining() > 0);

      // The default implementation of processRemaining pads the buffer and calls process().
      // We verify this behavior here.
      int before = processCallCount;
      super.processRemaining(bb);
      int after = processCallCount;
      assertEquals(
          "super.processRemaining() should delegate to process() exactly once", before + 1, after);

      // We decrement the count here so our invariant checks only count "full" chunk processes.
      processCallCount--;
    }



    @Override
    protected HashCode makeHash() {
      return HashCode.fromBytes(processedBytesStream.toByteArray());
    }

    /**
     * Asserts that the internal state (number of process calls, etc.) is consistent with the number
     * of bytes that were hashed.
     *
     * @param totalBytesHashed the total number of bytes fed into the hasher
     */
    void assertInvariants(int totalBytesHashed) {
      // The number of full chunks processed.
      assertEquals(totalBytesHashed / chunkSize, processCallCount);
      // Whether there was a partial chunk left over.
      assertEquals(totalBytesHashed % chunkSize != 0, processRemainingCalled);

      // The total output bytes should be padded up to the next multiple of the chunk size.
      int expectedOutputSize = ceilToMultiple(totalBytesHashed, chunkSize);
      assertEquals(expectedOutputSize, processedBytesStream.size());
    }

    /** Asserts that the bytes captured by the spy match the expected byte array. */
    void assertOutput(byte[] expected) {
      byte[] actual = processedBytesStream.toByteArray();
      // Only compare up to the length of the *expected* array, since the actual
      // output may be padded with extra zeros to fill a chunk.
      byte[] actualTruncated = Arrays.copyOf(actual, expected.length);
      assertArrayEquals(expected, actualTruncated);
    }

    /** Returns the smallest integer x such that x >= a and x is a multiple of b. */
    private static int ceilToMultiple(int a, int b) {
      int remainder = a % b;
      return (remainder == 0) ? a : a + b - remainder;
    }
  }

  /**
   * A simple, non-streaming hash function used as a reference or "control" in tests. It
   * concatenates all input bytes and uses that as the "hash". This provides a correct baseline to
   * compare against the more complex streaming implementations.
   */
  private static class ReferenceHashFunction extends AbstractNonStreamingHashFunction {
    @Override
    public HashCode hashBytes(byte[] input, int off, int len) {
      return HashCode.fromBytes(Arrays.copyOfRange(input, off, off + len));
    }

    @Override
    public int bits() {
      // The "hash" is the byte array itself, so bits() is not a fixed size.
      throw new UnsupportedOperationException();
    }
  }
}