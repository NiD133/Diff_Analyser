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
  
  private static final int DEFAULT_CHUNK_SIZE = 4;
  private static final int LARGE_CHUNK_SIZE = 8;
  
  public void testPutByte_AccumulatesCorrectly() {
    TestStreamingHasher hasher = new TestStreamingHasher(DEFAULT_CHUNK_SIZE);
    byte[] expectedBytes = {1, 2, 3, 4, 5, 6, 7, 8};
    
    // Put bytes individually and in arrays
    hasher.putByte((byte) 1);
    hasher.putBytes(new byte[] {2, 3, 4, 5, 6});
    hasher.putByte((byte) 7);
    hasher.putBytes(new byte[] {}); // empty array should be handled
    hasher.putBytes(new byte[] {8});
    
    hasher.hash();
    hasher.assertProcessedBytes(expectedBytes);
    hasher.assertChunkProcessingCorrect(totalBytes = 8);
  }

  public void testPutShort_UsesLittleEndianByteOrder() {
    TestStreamingHasher hasher = new TestStreamingHasher(DEFAULT_CHUNK_SIZE);
    
    hasher.putShort((short) 0x0201); // 0x0201 in big-endian
    hasher.hash();
    
    // Should be stored as little-endian: 0x01, 0x02
    hasher.assertProcessedBytes(new byte[] {1, 2, 0, 0}); // padded to chunk size
    hasher.assertChunkProcessingCorrect(totalBytes = 2);
  }

  public void testPutInt_UsesLittleEndianByteOrder() {
    TestStreamingHasher hasher = new TestStreamingHasher(DEFAULT_CHUNK_SIZE);
    
    hasher.putInt(0x04030201); // 0x04030201 in big-endian
    hasher.hash();
    
    // Should be stored as little-endian: 0x01, 0x02, 0x03, 0x04
    hasher.assertProcessedBytes(new byte[] {1, 2, 3, 4});
    hasher.assertChunkProcessingCorrect(totalBytes = 4);
  }

  public void testPutLong_UsesLittleEndianByteOrder() {
    TestStreamingHasher hasher = new TestStreamingHasher(LARGE_CHUNK_SIZE);
    
    hasher.putLong(0x0807060504030201L); // 0x0807060504030201 in big-endian
    hasher.hash();
    
    // Should be stored as little-endian: 0x01, 0x02, ..., 0x08
    hasher.assertProcessedBytes(new byte[] {1, 2, 3, 4, 5, 6, 7, 8});
    hasher.assertChunkProcessingCorrect(totalBytes = 8);
  }

  public void testPutChar_UsesLittleEndianByteOrder() {
    TestStreamingHasher hasher = new TestStreamingHasher(DEFAULT_CHUNK_SIZE);
    
    hasher.putChar((char) 0x0201);
    hasher.hash();
    
    hasher.assertProcessedBytes(new byte[] {1, 2, 0, 0}); // padded to chunk size
    hasher.assertChunkProcessingCorrect(totalBytes = 2);
  }

  public void testPutString_ProducesSameHashAsDirectByteEncoding() {
    Random random = new Random(0);
    
    for (int i = 0; i < 100; i++) {
      byte[] randomBytes = new byte[64];
      random.nextBytes(randomBytes);
      String testString = new String(randomBytes, UTF_16LE); // ensures valid UTF-16LE string
      
      // All three methods should produce the same hash
      HashCode hashFromUnencodedChars = new TestStreamingHasher(DEFAULT_CHUNK_SIZE)
          .putUnencodedChars(testString)
          .hash();
      HashCode hashFromBytes = new TestStreamingHasher(DEFAULT_CHUNK_SIZE)
          .putBytes(testString.getBytes(UTF_16LE))
          .hash();
      HashCode hashFromString = new TestStreamingHasher(DEFAULT_CHUNK_SIZE)
          .putString(testString, UTF_16LE)
          .hash();
      
      assertEquals(hashFromUnencodedChars, hashFromBytes);
      assertEquals(hashFromUnencodedChars, hashFromString);
    }
  }

  public void testPutFloat_UsesIntBitRepresentation() {
    TestStreamingHasher hasher = new TestStreamingHasher(DEFAULT_CHUNK_SIZE);
    
    // Float with specific bit pattern
    hasher.putFloat(Float.intBitsToFloat(0x04030201));
    hasher.hash();
    
    hasher.assertProcessedBytes(new byte[] {1, 2, 3, 4});
    hasher.assertChunkProcessingCorrect(totalBytes = 4);
  }

  public void testPutDouble_UsesLongBitRepresentation() {
    TestStreamingHasher hasher = new TestStreamingHasher(LARGE_CHUNK_SIZE);
    
    // Double with specific bit pattern
    hasher.putDouble(Double.longBitsToDouble(0x0807060504030201L));
    hasher.hash();
    
    hasher.assertProcessedBytes(new byte[] {1, 2, 3, 4, 5, 6, 7, 8});
    hasher.assertChunkProcessingCorrect(totalBytes = 8);
  }

  public void testPutBytes_ValidatesArrayBounds() {
    TestStreamingHasher hasher = new TestStreamingHasher(DEFAULT_CHUNK_SIZE);
    byte[] array = new byte[8];
    
    // Negative offset
    assertThrows(IndexOutOfBoundsException.class, 
        () -> hasher.putBytes(array, -1, 4));
    
    // Length exceeds array bounds
    assertThrows(IndexOutOfBoundsException.class, 
        () -> hasher.putBytes(array, 0, 16));
    
    // Negative length
    assertThrows(IndexOutOfBoundsException.class, 
        () -> hasher.putBytes(array, 0, -1));
  }

  /**
   * Comprehensive test that verifies different chunk and buffer size configurations
   * produce identical hash results when processing the same random input sequence.
   */
  @AndroidIncompatible // slow. TODO(cpovirk): Maybe just reduce iterations under Android.
  public void testDifferentChunkSizes_ProduceSameHash() throws Exception {
    Random random = new Random(0);
    final int NUM_RANDOM_OPERATIONS = 200;
    
    for (int trial = 0; trial < NUM_RANDOM_OPERATIONS; trial++) {
      // Create hashers with different configurations
      List<TestStreamingHasher> testHashers = createHashersWithDifferentConfigurations();
      
      // Create a reference hasher that doesn't use streaming
      ReferenceNonStreamingHasher referenceHasher = new ReferenceNonStreamingHasher();
      Hasher referenceHasherInstance = referenceHasher.newHasher(1024);
      
      // Apply the same random operations to all hashers
      Iterable<Hasher> allHashers = Iterables.concat(
          testHashers, 
          Collections.singleton(referenceHasherInstance)
      );
      
      for (int operation = 0; operation < trial; operation++) {
        RandomHasherAction.pickAtRandom(random).performAction(random, allHashers);
      }
      
      // Ensure at least 4 bytes are hashed (required by implementation)
      int finalInt = random.nextInt();
      for (Hasher hasher : allHashers) {
        hasher.putInt(finalInt);
      }
      
      // Get expected result from reference implementation
      byte[] expectedBytes = referenceHasherInstance.hash().asBytes();
      
      // Verify all streaming hashers produced the same result
      for (TestStreamingHasher testHasher : testHashers) {
        testHasher.hash();
        testHasher.assertProcessedBytes(expectedBytes);
        testHasher.assertChunkProcessingCorrect(expectedBytes.length);
      }
    }
  }
  
  private List<TestStreamingHasher> createHashersWithDifferentConfigurations() {
    List<TestStreamingHasher> hashers = new ArrayList<>();
    
    // Test various chunk sizes from 4 to 32 bytes
    for (int chunkSize = 4; chunkSize <= 32; chunkSize *= 2) {
      // Test various buffer sizes (must be multiples of chunk size)
      for (int bufferSize = chunkSize; bufferSize <= chunkSize * 4; bufferSize += chunkSize) {
        hashers.add(new TestStreamingHasher(chunkSize, bufferSize));
      }
    }
    
    return hashers;
  }

  /**
   * Test implementation of AbstractStreamingHasher that records all processed bytes
   * and validates chunk processing behavior.
   */
  private static class TestStreamingHasher extends AbstractStreamingHasher {
    private final int chunkSize;
    private final int bufferSize;
    private final ByteArrayOutputStream processedBytes = new ByteArrayOutputStream();
    
    private int processCallCount = 0;
    private boolean processRemainingCalled = false;

    TestStreamingHasher(int chunkSize, int bufferSize) {
      super(chunkSize, bufferSize);
      this.chunkSize = chunkSize;
      this.bufferSize = bufferSize;
    }

    TestStreamingHasher(int chunkSize) {
      this(chunkSize, chunkSize);
    }

    @Override
    protected HashCode makeHash() {
      return HashCode.fromBytes(processedBytes.toByteArray());
    }

    @Override
    protected void process(ByteBuffer bb) {
      processCallCount++;
      
      // Verify buffer properties
      assertEquals("Buffer should use little-endian byte order", 
          ByteOrder.LITTLE_ENDIAN, bb.order());
      assertTrue("Buffer should have at least chunkSize bytes remaining", 
          bb.remaining() >= chunkSize);
      
      // Record processed bytes
      for (int i = 0; i < chunkSize; i++) {
        processedBytes.write(bb.get());
      }
    }

    @Override
    protected void processRemaining(ByteBuffer bb) {
      assertFalse("processRemaining should only be called once", processRemainingCalled);
      processRemainingCalled = true;
      
      // Verify buffer properties
      assertEquals("Buffer should use little-endian byte order", 
          ByteOrder.LITTLE_ENDIAN, bb.order());
      assertTrue("Buffer should have remaining bytes", bb.remaining() > 0);
      assertTrue("Buffer should have less than bufferSize bytes", 
          bb.remaining() < bufferSize);
      
      // Default implementation pads and calls process()
      int processCallsBefore = processCallCount;
      super.processRemaining(bb);
      int processCallsAfter = processCallCount;
      
      assertEquals("processRemaining should trigger exactly one process() call", 
          processCallsBefore + 1, processCallsAfter);
      
      // Don't count the padding-induced process() call in our metrics
      processCallCount--;
    }

    void assertChunkProcessingCorrect(int expectedTotalBytes) {
      int actualBytes = processedBytes.toByteArray().length;
      int expectedPaddedBytes = roundUpToMultiple(expectedTotalBytes, chunkSize);
      
      assertEquals("Total processed bytes should be padded to chunk boundary", 
          expectedPaddedBytes, actualBytes);
      assertEquals("Number of full chunks processed", 
          expectedTotalBytes / chunkSize, processCallCount);
      assertEquals("processRemaining should be called iff bytes don't align to chunk boundary", 
          expectedTotalBytes % chunkSize != 0, processRemainingCalled);
    }

    void assertProcessedBytes(byte[] expected) {
      byte[] actual = processedBytes.toByteArray();
      
      for (int i = 0; i < expected.length; i++) {
        assertEquals("Byte at position " + i, expected[i], actual[i]);
      }
    }
    
    private static int roundUpToMultiple(int value, int multiple) {
      int remainder = value % multiple;
      return remainder == 0 ? value : value + multiple - remainder;
    }
  }

  /**
   * Reference implementation that doesn't use streaming, for comparison testing.
   * Assumes AbstractNonStreamingHashFunction is correctly implemented.
   */
  private static class ReferenceNonStreamingHasher extends AbstractNonStreamingHashFunction {
    @Override
    public HashCode hashBytes(byte[] input, int off, int len) {
      return HashCode.fromBytes(Arrays.copyOfRange(input, off, off + len));
    }

    @Override
    public int bits() {
      throw new UnsupportedOperationException("Not needed for testing");
    }
  }
}