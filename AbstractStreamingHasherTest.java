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
 * Tests for {@link AbstractStreamingHasher}.
 *
 * <p>This class verifies that {@link AbstractStreamingHasher} correctly processes data in chunks
 * and produces the expected hash codes. It tests various input types (bytes, short, int, long,
 * char, string, float, double) and also covers edge cases such as incorrect usage of the putBytes
 * method. The exhaustive test ensures that different configurations of chunk size and buffer size
 * produce the same hash code for a long random sequence of inputs.
 */
@NullUnmarked
public class AbstractStreamingHasherTest extends TestCase {

  /** Tests that {@code putByte} and {@code putBytes} methods work correctly. */
  public void testBytes() {
    int chunkSize = 4;
    Sink sink = new Sink(chunkSize);
    byte[] expectedBytes = {1, 2, 3, 4, 5, 6, 7, 8};

    sink.putByte((byte) 1);
    sink.putBytes(new byte[] {2, 3, 4, 5, 6});
    sink.putByte((byte) 7);
    sink.putBytes(new byte[] {}); // Empty array should be handled gracefully
    sink.putBytes(new byte[] {8});

    HashCode unused = sink.hash(); // Force hashing to ensure all bytes are processed

    sink.assertInvariants(expectedBytes.length); // Check internal state
    sink.assertBytes(expectedBytes); // Verify the bytes written to the sink
  }

  /** Tests that {@code putShort} method works correctly. */
  public void testShort() {
    int chunkSize = 4;
    Sink sink = new Sink(chunkSize);
    sink.putShort((short) 0x0201); // Little-endian representation

    HashCode unused = sink.hash();

    sink.assertInvariants(2); // 2 bytes should have been processed
    sink.assertBytes(new byte[] {1, 2, 0, 0}); // Check little-endian byte order and padding
  }

  /** Tests that {@code putInt} method works correctly. */
  public void testInt() {
    int chunkSize = 4;
    Sink sink = new Sink(chunkSize);
    sink.putInt(0x04030201); // Little-endian representation

    HashCode unused = sink.hash();

    sink.assertInvariants(4); // 4 bytes should have been processed
    sink.assertBytes(new byte[] {1, 2, 3, 4}); // Check little-endian byte order
  }

  /** Tests that {@code putLong} method works correctly. */
  public void testLong() {
    int chunkSize = 8;
    Sink sink = new Sink(chunkSize);
    sink.putLong(0x0807060504030201L); // Little-endian representation

    HashCode unused = sink.hash();

    sink.assertInvariants(8); // 8 bytes should have been processed
    sink.assertBytes(new byte[] {1, 2, 3, 4, 5, 6, 7, 8}); // Check little-endian byte order
  }

  /** Tests that {@code putChar} method works correctly. */
  public void testChar() {
    int chunkSize = 4;
    Sink sink = new Sink(chunkSize);
    sink.putChar((char) 0x0201); // Little-endian representation

    HashCode unused = sink.hash();

    sink.assertInvariants(2); // 2 bytes should have been processed
    sink.assertBytes(new byte[] {1, 2, 0, 0}); // Check little-endian byte order and padding
  }

  /**
   * Tests that {@code putString} and {@code putUnencodedChars} methods produce the same hash code.
   */
  public void testString() {
    Random random = new Random();
    int chunkSize = 4;
    for (int i = 0; i < 100; i++) {
      byte[] bytes = new byte[64];
      random.nextBytes(bytes);
      String s = new String(bytes, UTF_16LE); // Create a valid UTF-16LE string from random bytes

      Sink sink1 = new Sink(chunkSize);
      sink1.putUnencodedChars(s);
      HashCode hashCodeFromUnencodedChars = sink1.hash();

      Sink sink2 = new Sink(chunkSize);
      sink2.putBytes(s.getBytes(UTF_16LE));
      HashCode hashCodeFromBytes = sink2.hash();

      Sink sink3 = new Sink(chunkSize);
      sink3.putString(s, UTF_16LE);
      HashCode hashCodeFromString = sink3.hash();

      assertEquals(
          "Hash code from putUnencodedChars should be equal to hash code from putBytes",
          hashCodeFromUnencodedChars,
          hashCodeFromBytes);
      assertEquals(
          "Hash code from putUnencodedChars should be equal to hash code from putString",
          hashCodeFromUnencodedChars,
          hashCodeFromString);
    }
  }

  /** Tests that {@code putFloat} method works correctly. */
  public void testFloat() {
    int chunkSize = 4;
    Sink sink = new Sink(chunkSize);
    sink.putFloat(Float.intBitsToFloat(0x04030201)); // Little-endian representation of the int bits

    HashCode unused = sink.hash();

    sink.assertInvariants(4); // 4 bytes should have been processed
    sink.assertBytes(new byte[] {1, 2, 3, 4}); // Check little-endian byte order
  }

  /** Tests that {@code putDouble} method works correctly. */
  public void testDouble() {
    int chunkSize = 8;
    Sink sink = new Sink(chunkSize);
    sink.putDouble(Double.longBitsToDouble(0x0807060504030201L)); // Little-endian representation

    HashCode unused = sink.hash();

    sink.assertInvariants(8); // 8 bytes should have been processed
    sink.assertBytes(new byte[] {1, 2, 3, 4, 5, 6, 7, 8}); // Check little-endian byte order
  }

  /** Tests that the correct exceptions are thrown for invalid arguments to {@code putBytes}. */
  public void testCorrectExceptions() {
    int chunkSize = 4;
    Sink sink = new Sink(chunkSize);

    // Test negative offset
    assertThrows(
        IndexOutOfBoundsException.class,
        () -> sink.putBytes(new byte[8], -1, 4),
        "Expected IndexOutOfBoundsException for negative offset");

    // Test length exceeding array bounds
    assertThrows(
        IndexOutOfBoundsException.class,
        () -> sink.putBytes(new byte[8], 0, 16),
        "Expected IndexOutOfBoundsException for length exceeding array bounds");

    // Test negative length
    assertThrows(
        IndexOutOfBoundsException.class,
        () -> sink.putBytes(new byte[8], 0, -1),
        "Expected IndexOutOfBoundsException for negative length");
  }

  /**
   * Exhaustively tests that the hasher produces the same output for different chunk sizes and
   * buffer sizes. It generates a long random sequence of inputs and processes it using multiple
   * sinks with different configurations.
   */
  @AndroidIncompatible // slow. TODO(cpovirk): Maybe just reduce iterations under Android.
  public void testExhaustive() throws Exception {
    Random random = new Random(0); // Use a fixed seed for repeatable tests
    int maxInsertions = 200;

    for (int totalInsertions = 0; totalInsertions < maxInsertions; totalInsertions++) {
      List<Sink> sinks = new ArrayList<>();
      // Iterate through various chunk sizes and buffer sizes.  Buffer size must be a multiple of
      // chunk size.
      for (int chunkSize = 4; chunkSize <= 32; chunkSize++) {
        for (int bufferSize = chunkSize; bufferSize <= chunkSize * 4; bufferSize += chunkSize) {
          sinks.add(new Sink(chunkSize, bufferSize));
        }
      }

      // Create a control sink to compare the output against
      Control control = new Control();
      Hasher controlSink = control.newHasher(1024);

      Iterable<Hasher> sinksAndControl =
          Iterables.concat(sinks, Collections.singleton(controlSink));

      // Perform random actions on the sinks and the control sink.
      for (int insertion = 0; insertion < totalInsertions; insertion++) {
        RandomHasherAction.pickAtRandom(random).performAction(random, sinksAndControl);
      }

      // Ensure that at least 4 bytes have been put into the hasher to avoid exceptions.
      int intToPut = random.nextInt();
      for (Hasher hasher : sinksAndControl) {
        hasher.putInt(intToPut);
      }

      // Compute the expected hash code from the control sink.
      byte[] expectedBytes = controlSink.hash().asBytes();

      // Verify that all sinks produce the same hash code as the control sink.
      for (Sink sink : sinks) {
        HashCode unused = sink.hash();
        sink.assertInvariants(expectedBytes.length);
        sink.assertBytes(expectedBytes);
      }
    }
  }

  /**
   * A custom {@link AbstractStreamingHasher} implementation for testing purposes. It stores the
   * processed bytes in a {@link ByteArrayOutputStream} and provides methods for asserting the
   * internal state.
   */
  private static class Sink extends AbstractStreamingHasher {
    final int chunkSize;
    final int bufferSize;
    final ByteArrayOutputStream out = new ByteArrayOutputStream();

    int processCalled = 0; // Number of times process() was called
    boolean remainingCalled = false; // Whether processRemaining() was called

    /**
     * Constructs a new {@code Sink} with the given chunk size and buffer size.
     *
     * @param chunkSize The chunk size.
     * @param bufferSize The buffer size. Must be a multiple of the chunk size.
     */
    Sink(int chunkSize, int bufferSize) {
      super(chunkSize, bufferSize);
      this.chunkSize = chunkSize;
      this.bufferSize = bufferSize;
    }

    /**
     * Constructs a new {@code Sink} with the given chunk size. The buffer size will be equal to the
     * chunk size.
     *
     * @param chunkSize The chunk size.
     */
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
      assertEquals(ByteOrder.LITTLE_ENDIAN, bb.order()); // Verify byte order
      assertTrue(bb.remaining() >= chunkSize); // Verify that enough bytes are available
      for (int i = 0; i < chunkSize; i++) {
        out.write(bb.get()); // Write the bytes to the output stream
      }
    }

    @Override
    protected void processRemaining(ByteBuffer bb) {
      assertFalse(remainingCalled); // Should only be called once
      remainingCalled = true;
      assertEquals(ByteOrder.LITTLE_ENDIAN, bb.order()); // Verify byte order
      assertTrue(bb.remaining() > 0); // Verify that there are bytes remaining
      assertTrue(bb.remaining() < bufferSize); // Verify that it's less than the buffer size
      int before = processCalled;
      super.processRemaining(bb); // Call the default implementation (pads and calls process())
      int after = processCalled;
      assertEquals(
          before + 1, after); // Verify default implementation pads and calls process at least once
      processCalled--; // don't count the tail invocation (makes tests a bit more understandable)
    }

    /**
     * Asserts internal invariants of the sink.
     *
     * @param expectedBytes The expected number of bytes that have been processed.
     */
    void assertInvariants(int expectedBytes) {
      // we should have seen as many bytes as the next multiple of chunk after expectedBytes - 1
      assertEquals(out.toByteArray().length, ceilToMultiple(expectedBytes, chunkSize));
      assertEquals(expectedBytes / chunkSize, processCalled);
      assertEquals(expectedBytes % chunkSize != 0, remainingCalled);
    }

    /**
     * Returns the minimum x such as x >= a && (x % b) == 0.
     *
     * @param a The input value.
     * @param b The multiple.
     * @return The smallest multiple of {@code b} that is greater than or equal to {@code a}.
     */
    private static int ceilToMultiple(int a, int b) {
      int remainder = a % b;
      return remainder == 0 ? a : a + b - remainder;
    }

    /**
     * Asserts that the bytes written to the sink are equal to the expected bytes.
     *
     * @param expected The expected bytes.
     */
    void assertBytes(byte[] expected) {
      byte[] got = out.toByteArray();
      assertEquals(expected.length, Arrays.copyOf(got, expected.length).length);
      assertTrue(Arrays.equals(expected, Arrays.copyOf(got, expected.length)));
    }
  }

  /**
   * A control class that directly copies the input bytes to produce the hash code. This is used to
   * compare the output of the {@link AbstractStreamingHasher} implementation against a known good
   * implementation.
   */
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