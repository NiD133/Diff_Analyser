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
  // Used to define expected buffer sizes in tests
  private static final int STANDARD_CHUNK_SIZE = 4;
  private static final int LARGE_CHUNK_SIZE = 8;
  private static final int ITERATIONS = 100;

  public void testBytes() {
    TestingStreamingHasher hasher = new TestingStreamingHasher(STANDARD_CHUNK_SIZE);
    byte[] expected = {1, 2, 3, 4, 5, 6, 7, 8};
    
    hasher.putByte((byte) 1);
    hasher.putBytes(new byte[] {2, 3, 4, 5, 6});
    hasher.putByte((byte) 7);
    hasher.putBytes(new byte[] {}); // Test empty array
    hasher.putBytes(new byte[] {8});
    
    HashCode unused = hasher.hash();
    hasher.assertInvariants(8);
    hasher.assertBytesEqual(expected);
  }

  public void testShort() {
    TestingStreamingHasher hasher = new TestingStreamingHasher(STANDARD_CHUNK_SIZE);
    hasher.putShort((short) 0x0201);
    HashCode unused = hasher.hash();
    
    hasher.assertInvariants(2);
    // Verify little-endian encoding and zero padding
    hasher.assertBytesEqual(new byte[] {1, 2, 0, 0});
  }

  public void testInt() {
    TestingStreamingHasher hasher = new TestingStreamingHasher(STANDARD_CHUNK_SIZE);
    hasher.putInt(0x04030201);
    HashCode unused = hasher.hash();
    
    hasher.assertInvariants(4);
    hasher.assertBytesEqual(new byte[] {1, 2, 3, 4});
  }

  public void testLong() {
    TestingStreamingHasher hasher = new TestingStreamingHasher(LARGE_CHUNK_SIZE);
    hasher.putLong(0x0807060504030201L);
    HashCode unused = hasher.hash();
    
    hasher.assertInvariants(8);
    hasher.assertBytesEqual(new byte[] {1, 2, 3, 4, 5, 6, 7, 8});
  }

  public void testChar() {
    TestingStreamingHasher hasher = new TestingStreamingHasher(STANDARD_CHUNK_SIZE);
    hasher.putChar((char) 0x0201);
    HashCode unused = hasher.hash();
    
    hasher.assertInvariants(2);
    // Verify little-endian encoding and zero padding
    hasher.assertBytesEqual(new byte[] {1, 2, 0, 0});
  }

  public void testString() {
    Random random = new Random();
    for (int i = 0; i < ITERATIONS; i++) {
      byte[] bytes = new byte[64];
      random.nextBytes(bytes);
      String s = new String(bytes, UTF_16LE); // All random strings are valid in UTF_16LE
      
      // Verify different string insertion methods produce same hash
      assertEquals(
          new TestingStreamingHasher(STANDARD_CHUNK_SIZE).putUnencodedChars(s).hash(),
          new TestingStreamingHasher(STANDARD_CHUNK_SIZE).putBytes(s.getBytes(UTF_16LE)).hash());
      assertEquals(
          new TestingStreamingHasher(STANDARD_CHUNK_SIZE).putUnencodedChars(s).hash(), 
          new TestingStreamingHasher(STANDARD_CHUNK_SIZE).putString(s, UTF_16LE).hash());
    }
  }

  public void testFloat() {
    TestingStreamingHasher hasher = new TestingStreamingHasher(STANDARD_CHUNK_SIZE);
    hasher.putFloat(Float.intBitsToFloat(0x04030201));
    HashCode unused = hasher.hash();
    
    hasher.assertInvariants(4);
    hasher.assertBytesEqual(new byte[] {1, 2, 3, 4});
  }

  public void testDouble() {
    TestingStreamingHasher hasher = new TestingStreamingHasher(LARGE_CHUNK_SIZE);
    hasher.putDouble(Double.longBitsToDouble(0x0807060504030201L));
    HashCode unused = hasher.hash();
    
    hasher.assertInvariants(8);
    hasher.assertBytesEqual(new byte[] {1, 2, 3, 4, 5, 6, 7, 8});
  }

  public void testCorrectExceptions() {
    TestingStreamingHasher hasher = new TestingStreamingHasher(STANDARD_CHUNK_SIZE);
    byte[] data = new byte[8];
    
    // Verify invalid inputs throw expected exceptions
    assertThrows(IndexOutOfBoundsException.class, () -> hasher.putBytes(data, -1, 4));
    assertThrows(IndexOutOfBoundsException.class, () -> hasher.putBytes(data, 0, 16));
    assertThrows(IndexOutOfBoundsException.class, () -> hasher.putBytes(data, 0, -1));
  }

  /**
   * Tests hasher behavior with various buffer configurations. Creates multiple hashers with different
   * chunk/buffer sizes, feeds them identical random data, and verifies consistent output.
   */
  @AndroidIncompatible // slow. TODO(cpovirk): Maybe just reduce iterations under Android.
  public void testExhaustive() {
    Random random = new Random(0); // Fixed seed for determinism
    int maxInsertions = 200;
    
    for (int totalInsertions = 0; totalInsertions < maxInsertions; totalInsertions++) {
      List<TestingStreamingHasher> hashers = createHashersForExhaustiveTest();
      ControlHashFunction control = new ControlHashFunction();
      Hasher controlHasher = control.newHasher(1024);

      // Combine all test hashers with control hasher
      Iterable<Hasher> allHashers = Iterables.concat(hashers, Collections.singleton(controlHasher));
      
      // Perform random operations on all hashers
      performRandomOperations(random, allHashers, totalInsertions);
      
      // Ensure minimum data for hashing
      int randomInt = random.nextInt();
      for (Hasher hasher : allHashers) {
        hasher.putInt(randomInt);
      }
      
      // Get control hash before verifying test hashers
      byte[] expectedHash = controlHasher.hash().asBytes();
      
      // Verify all test hashers match control
      for (TestingStreamingHasher hasher : hashers) {
        HashCode unused = hasher.hash();
        hasher.assertInvariants(expectedHash.length);
        hasher.assertBytesEqual(expectedHash);
      }
    }
  }

  /** Creates test hashers with various chunk/buffer size combinations */
  private List<TestingStreamingHasher> createHashersForExhaustiveTest() {
    List<TestingStreamingHasher> hashers = new ArrayList<>();
    for (int chunkSize = 4; chunkSize <= 32; chunkSize++) {
      for (int bufferSize = chunkSize; bufferSize <= chunkSize * 4; bufferSize += chunkSize) {
        hashers.add(new TestingStreamingHasher(chunkSize, bufferSize));
      }
    }
    return hashers;
  }

  /** Performs random operations on a group of hashers */
  private void performRandomOperations(Random random, Iterable<Hasher> hashers, int operations) {
    for (int i = 0; i < operations; i++) {
      RandomHasherAction.pickAtRandom(random).performAction(random, hashers);
    }
  }

  /**
   * Test implementation of AbstractStreamingHasher that records processed bytes.
   * Allows inspection of internal buffer states for verification.
   */
  private static class TestingStreamingHasher extends AbstractStreamingHasher {
    final int chunkSize;
    final int bufferSize;
    final ByteArrayOutputStream processedBytes = new ByteArrayOutputStream();

    int processCalled = 0;
    boolean processRemainingCalled = false;

    TestingStreamingHasher(int chunkSize, int bufferSize) {
      super(chunkSize, bufferSize);
      this.chunkSize = chunkSize;
      this.bufferSize = bufferSize;
    }

    TestingStreamingHasher(int chunkSize) {
      super(chunkSize);
      this.chunkSize = chunkSize;
      this.bufferSize = chunkSize;
    }

    @Override
    protected HashCode makeHash() {
      return HashCode.fromBytes(processedBytes.toByteArray());
    }

    @Override
    protected void process(ByteBuffer bb) {
      processCalled++;
      assertEquals(ByteOrder.LITTLE_ENDIAN, bb.order());
      assertTrue("Buffer too small", bb.remaining() >= chunkSize);
      
      byte[] chunk = new byte[chunkSize];
      bb.get(chunk);
      processedBytes.writeBytes(chunk);
    }

    @Override
    protected void processRemaining(ByteBuffer bb) {
      assertFalse("processRemaining called multiple times", processRemainingCalled);
      processRemainingCalled = true;
      assertEquals(ByteOrder.LITTLE_ENDIAN, bb.order());
      assertTrue("Buffer should have remaining", bb.remaining() > 0);
      assertTrue("Remaining exceeds buffer", bb.remaining() < bufferSize);
      
      int before = processCalled;
      super.processRemaining(bb); // Default implementation pads and processes
      int after = processCalled;
      assertEquals("process should be called once", before + 1, after);
      processCalled--; // Adjust for test clarity
    }

    /**
     * Verifies internal state after hashing:
     * - Total bytes processed matches next multiple of chunk size
     * - Correct number of process() calls occurred
     * - processRemaining() called only when needed
     */
    void assertInvariants(int expectedBytes) {
      int actualBytes = processedBytes.size();
      int expectedProcessedBytes = ceilToMultiple(expectedBytes, chunkSize);
      assertEquals("Total bytes processed", expectedProcessedBytes, actualBytes);
      
      int expectedProcessCalls = expectedBytes / chunkSize;
      assertEquals("process() call count", expectedProcessCalls, processCalled);
      
      boolean shouldHaveRemaining = (expectedBytes % chunkSize != 0);
      assertEquals("processRemaining() called", shouldHaveRemaining, processRemainingCalled);
    }

    /** Rounds up to nearest multiple of chunk size */
    private static int ceilToMultiple(int value, int multiple) {
      int remainder = value % multiple;
      return remainder == 0 ? value : value + multiple - remainder;
    }

    /** Verifies first N bytes match expected */
    void assertBytesEqual(byte[] expected) {
      byte[] actual = processedBytes.toByteArray();
      for (int i = 0; i < expected.length; i++) {
        assertEquals("Byte mismatch at index " + i, expected[i], actual[i]);
      }
    }
  }

  /** Reference implementation that simply returns input bytes as hash */
  private static class ControlHashFunction extends AbstractNonStreamingHashFunction {
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