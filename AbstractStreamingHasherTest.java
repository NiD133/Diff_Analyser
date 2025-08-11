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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Iterables;
import com.google.common.hash.HashTestUtils.RandomHasherAction;
import com.google.common.testing.AndroidIncompatible;
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

/**
 * Unit tests for AbstractStreamingHasher.
 *
 * These tests verify:
 * - Little-endian ordering for primitive types
 * - Correct chunking behavior and zero-padding for tails
 * - Consistency between different input methods (e.g. putUnencodedChars vs putString/putBytes)
 * - Exception behavior for invalid putBytes ranges
 * - Cross-check against a simple "control" hash that just returns the concatenated input bytes
 */
@NullUnmarked
public class AbstractStreamingHasherTest {

  @Test
  public void bytes_areConcatenatedInOrder() {
    Sink sink = new Sink(4); // Little-endian order is enforced inside Sink
    byte[] expected = {1, 2, 3, 4, 5, 6, 7, 8};

    sink.putByte((byte) 1);
    sink.putBytes(new byte[] {2, 3, 4, 5, 6});
    sink.putByte((byte) 7);
    sink.putBytes(new byte[] {});
    sink.putBytes(new byte[] {8});
    sink.hash();

    sink.assertChunkingInvariants(8);
    sink.assertOutputPrefixEquals(expected);
  }

  @Test
  public void short_isLittleEndianAndTailIsZeroPaddedToChunk() {
    Sink sink = new Sink(4);

    sink.putShort((short) 0x0201);
    sink.hash();

    sink.assertChunkingInvariants(2);
    sink.assertOutputPrefixEquals(new byte[] {1, 2, 0, 0}); // zero padding up to chunk size
  }

  @Test
  public void int_isLittleEndian() {
    Sink sink = new Sink(4);

    sink.putInt(0x04030201);
    sink.hash();

    sink.assertChunkingInvariants(4);
    sink.assertOutputPrefixEquals(new byte[] {1, 2, 3, 4});
  }

  @Test
  public void long_isLittleEndian() {
    Sink sink = new Sink(8);

    sink.putLong(0x0807060504030201L);
    sink.hash();

    sink.assertChunkingInvariants(8);
    sink.assertOutputPrefixEquals(new byte[] {1, 2, 3, 4, 5, 6, 7, 8});
  }

  @Test
  public void char_isLittleEndianAndTailIsZeroPaddedToChunk() {
    Sink sink = new Sink(4);

    sink.putChar((char) 0x0201);
    sink.hash();

    sink.assertChunkingInvariants(2);
    sink.assertOutputPrefixEquals(new byte[] {1, 2, 0, 0}); // zero padding up to chunk size
  }

  @Test
  public void string_putUnencodedChars_matches_putBytes_and_putString_forUtf16Le() {
    Random random = new Random(0); // fixed seed for deterministic failures
    for (int i = 0; i < 100; i++) {
      byte[] randomBytes = new byte[64];
      random.nextBytes(randomBytes);

      // UTF-16LE ensures every random byte sequence decodes to valid chars.
      String s = new String(randomBytes, UTF_16LE);

      assertEquals(
          new Sink(4).putUnencodedChars(s).hash(),
          new Sink(4).putBytes(s.getBytes(UTF_16LE)).hash());

      assertEquals(
          new Sink(4).putUnencodedChars(s).hash(),
          new Sink(4).putString(s, UTF_16LE).hash());
    }
  }

  @Test
  public void float_isLittleEndian() {
    Sink sink = new Sink(4);

    sink.putFloat(Float.intBitsToFloat(0x04030201));
    sink.hash();

    sink.assertChunkingInvariants(4);
    sink.assertOutputPrefixEquals(new byte[] {1, 2, 3, 4});
  }

  @Test
  public void double_isLittleEndian() {
    Sink sink = new Sink(8);

    sink.putDouble(Double.longBitsToDouble(0x0807060504030201L));
    sink.hash();

    sink.assertChunkingInvariants(8);
    sink.assertOutputPrefixEquals(new byte[] {1, 2, 3, 4, 5, 6, 7, 8});
  }

  @Test
  public void putBytes_throwsForInvalidRanges() {
    Sink sink = new Sink(4);

    assertThrows(IndexOutOfBoundsException.class, () -> sink.putBytes(new byte[8], -1, 4));
    assertThrows(IndexOutOfBoundsException.class, () -> sink.putBytes(new byte[8], 0, 16));
    assertThrows(IndexOutOfBoundsException.class, () -> sink.putBytes(new byte[8], 0, -1));
  }

  /**
   * Creates a long random sequence of inputs, then feeds it to many differently configured sinks.
   * All sinks must produce the exact same output as the control hasher (which returns the raw bytes
   * it saw). The only difference among sinks should be how many times process()/processRemaining()
   * are called (due to chunk and buffer alignment).
   */
  @Test
  @AndroidIncompatible // slow. Consider reducing iterations on Android.
  public void randomized_streamingConsistency_acrossChunkAndBufferSizes() throws Exception {
    Random random = new Random(0); // deterministic for easier debugging

    for (int totalInsertions = 0; totalInsertions < 200; totalInsertions++) {
      // Build a wide variety of sink configurations.
      List<Sink> sinks = new ArrayList<>();
      for (int chunkSize = 4; chunkSize <= 32; chunkSize++) {
        for (int bufferSize = chunkSize; bufferSize <= chunkSize * 4; bufferSize += chunkSize) {
          sinks.add(new Sink(chunkSize, bufferSize));
          // We keep little-endian only here for simplicity (DataOutputStream compatibility).
        }
      }

      // Control hasher echoes input bytes back as the "hash" for easy comparison.
      Control control = new Control();
      Hasher controlHasher = control.newHasher(1024);

      Iterable<Hasher> allHashers = Iterables.concat(sinks, Collections.singleton(controlHasher));

      // Perform a random sequence of hasher operations.
      for (int insertion = 0; insertion < totalInsertions; insertion++) {
        RandomHasherAction.pickAtRandom(random).performAction(random, allHashers);
      }

      // Ensure at least 4 bytes were added so that Hasher#hash won't throw.
      int extraInt = random.nextInt();
      for (Hasher h : allHashers) {
        h.putInt(extraInt);
      }

      // Finalize sinks first, then capture the control output.
      for (Sink sink : sinks) {
        sink.hash();
      }
      byte[] expected = controlHasher.hash().asBytes();

      // Each sink should have captured the same bytes as the control.
      for (Sink sink : sinks) {
        sink.assertChunkingInvariants(expected.length);
        sink.assertOutputPrefixEquals(expected);
      }
    }
  }

  /**
   * A concrete AbstractStreamingHasher used only for testing. It:
   * - Forces LITTLE_ENDIAN byte order
   * - Captures bytes passed through process()/processRemaining()
   * - Verifies that tail handling zero-pads up to chunk size and then delegates to process()
   */
  private static final class Sink extends AbstractStreamingHasher {
    final int chunkSize;
    final int bufferSize;
    final ByteArrayOutputStream captured = new ByteArrayOutputStream();

    int processChunkCalls = 0;
    boolean processedTail = false;

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
      return HashCode.fromBytes(captured.toByteArray());
    }

    @Override
    protected void process(ByteBuffer bb) {
      processChunkCalls++;
      assertEquals(ByteOrder.LITTLE_ENDIAN, bb.order());
      assertTrue(bb.remaining() >= chunkSize);
      for (int i = 0; i < chunkSize; i++) {
        captured.write(bb.get());
      }
    }

    @Override
    protected void processRemaining(ByteBuffer bb) {
      assertFalse(processedTail);
      processedTail = true;

      assertEquals(ByteOrder.LITTLE_ENDIAN, bb.order());
      assertTrue(bb.remaining() > 0);
      assertTrue(bb.remaining() < bufferSize);

      // The default implementation pads with zeros and calls process().
      int before = processChunkCalls;
      super.processRemaining(bb);
      int after = processChunkCalls;
      assertEquals(before + 1, after);

      // Do not count the synthesized tail chunk in processChunkCalls to make expectations clearer.
      processChunkCalls--;
    }

    void assertChunkingInvariants(int expectedUnpaddedBytes) {
      // Total captured length should be ceil(expected, chunkSize).
      assertEquals(captured.size(), ceilToMultiple(expectedUnpaddedBytes, chunkSize));
      // Number of full process() calls equals the number of whole chunks observed.
      assertEquals(expectedUnpaddedBytes / chunkSize, processChunkCalls);
      // Tail should be processed if and only if there was a non-zero remainder.
      assertEquals(expectedUnpaddedBytes % chunkSize != 0, processedTail);
    }

    void assertOutputPrefixEquals(byte[] expectedPrefix) {
      byte[] got = captured.toByteArray();
      for (int i = 0; i < expectedPrefix.length; i++) {
        assertEquals("byte[" + i + "]", expectedPrefix[i], got[i]);
      }
    }

    private static int ceilToMultiple(int a, int b) {
      int remainder = a % b;
      return remainder == 0 ? a : a + b - remainder;
    }
  }

  /**
   * A control hash function for verification:
   * - Simply returns all input bytes (from off to off + len) as the "hash" value.
   * - Relies on AbstractNonStreamingHashFunction to accumulate provided bytes.
   */
  private static final class Control extends AbstractNonStreamingHashFunction {
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