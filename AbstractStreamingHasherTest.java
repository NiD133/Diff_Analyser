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
 * Unit tests for AbstractStreamingHasher.
 * 
 * This test suite verifies the behavior of the AbstractStreamingHasher class by testing
 * various data types and ensuring that the hashing process works correctly.
 * 
 * Author: Dimitris Andreou
 */
@NullUnmarked
public class AbstractStreamingHasherTest extends TestCase {

  // Test for putting bytes into the hasher
  public void testPutBytes() {
    Sink sink = new Sink(4); // byte order insignificant here
    byte[] expectedBytes = {1, 2, 3, 4, 5, 6, 7, 8};

    sink.putByte((byte) 1);
    sink.putBytes(new byte[] {2, 3, 4, 5, 6});
    sink.putByte((byte) 7);
    sink.putBytes(new byte[] {});
    sink.putBytes(new byte[] {8});

    HashCode unusedHash = sink.hash();
    sink.assertInvariants(8);
    sink.assertBytes(expectedBytes);
  }

  // Test for putting a short value into the hasher
  public void testPutShort() {
    Sink sink = new Sink(4);
    sink.putShort((short) 0x0201);

    HashCode unusedHash = sink.hash();
    sink.assertInvariants(2);
    sink.assertBytes(new byte[] {1, 2, 0, 0}); // padded with zeros
  }

  // Test for putting an int value into the hasher
  public void testPutInt() {
    Sink sink = new Sink(4);
    sink.putInt(0x04030201);

    HashCode unusedHash = sink.hash();
    sink.assertInvariants(4);
    sink.assertBytes(new byte[] {1, 2, 3, 4});
  }

  // Test for putting a long value into the hasher
  public void testPutLong() {
    Sink sink = new Sink(8);
    sink.putLong(0x0807060504030201L);

    HashCode unusedHash = sink.hash();
    sink.assertInvariants(8);
    sink.assertBytes(new byte[] {1, 2, 3, 4, 5, 6, 7, 8});
  }

  // Test for putting a char value into the hasher
  public void testPutChar() {
    Sink sink = new Sink(4);
    sink.putChar((char) 0x0201);

    HashCode unusedHash = sink.hash();
    sink.assertInvariants(2);
    sink.assertBytes(new byte[] {1, 2, 0, 0}); // padded with zeros
  }

  // Test for putting a string value into the hasher
  public void testPutString() {
    Random random = new Random();
    for (int i = 0; i < 100; i++) {
      byte[] randomBytes = new byte[64];
      random.nextBytes(randomBytes);
      String randomString = new String(randomBytes, UTF_16LE);

      assertEquals(
          new Sink(4).putUnencodedChars(randomString).hash(),
          new Sink(4).putBytes(randomString.getBytes(UTF_16LE)).hash()
      );
      assertEquals(
          new Sink(4).putUnencodedChars(randomString).hash(),
          new Sink(4).putString(randomString, UTF_16LE).hash()
      );
    }
  }

  // Test for putting a float value into the hasher
  public void testPutFloat() {
    Sink sink = new Sink(4);
    sink.putFloat(Float.intBitsToFloat(0x04030201));

    HashCode unusedHash = sink.hash();
    sink.assertInvariants(4);
    sink.assertBytes(new byte[] {1, 2, 3, 4});
  }

  // Test for putting a double value into the hasher
  public void testPutDouble() {
    Sink sink = new Sink(8);
    sink.putDouble(Double.longBitsToDouble(0x0807060504030201L));

    HashCode unusedHash = sink.hash();
    sink.assertInvariants(8);
    sink.assertBytes(new byte[] {1, 2, 3, 4, 5, 6, 7, 8});
  }

  // Test for exception handling in putBytes method
  public void testPutBytesExceptions() {
    Sink sink = new Sink(4);
    assertThrows(IndexOutOfBoundsException.class, () -> sink.putBytes(new byte[8], -1, 4));
    assertThrows(IndexOutOfBoundsException.class, () -> sink.putBytes(new byte[8], 0, 16));
    assertThrows(IndexOutOfBoundsException.class, () -> sink.putBytes(new byte[8], 0, -1));
  }

  /**
   * Exhaustive test to ensure that different sink configurations produce the same hash result.
   * This test creates a long random sequence of inputs and processes it with various sinks.
   */
  @AndroidIncompatible // slow. TODO: Maybe reduce iterations under Android.
  public void testExhaustiveHashing() throws Exception {
    Random random = new Random(0);
    for (int totalInsertions = 0; totalInsertions < 200; totalInsertions++) {
      List<Sink> sinks = createSinks();

      Control control = new Control();
      Hasher controlHasher = control.newHasher(1024);

      Iterable<Hasher> allHashers = Iterables.concat(sinks, Collections.singleton(controlHasher));
      for (int insertion = 0; insertion < totalInsertions; insertion++) {
        RandomHasherAction.pickAtRandom(random).performAction(random, allHashers);
      }

      // Ensure at least 4 bytes are put into the hasher
      int randomInt = random.nextInt();
      for (Hasher hasher : allHashers) {
        hasher.putInt(randomInt);
      }

      byte[] expectedHash = controlHasher.hash().asBytes();
      for (Sink sink : sinks) {
        sink.hash();
        sink.assertInvariants(expectedHash.length);
        sink.assertBytes(expectedHash);
      }
    }
  }

  private List<Sink> createSinks() {
    List<Sink> sinks = new ArrayList<>();
    for (int chunkSize = 4; chunkSize <= 32; chunkSize++) {
      for (int bufferSize = chunkSize; bufferSize <= chunkSize * 4; bufferSize += chunkSize) {
        sinks.add(new Sink(chunkSize, bufferSize));
      }
    }
    return sinks;
  }

  // Sink class for testing the AbstractStreamingHasher
  private static class Sink extends AbstractStreamingHasher {
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private int processCallCount = 0;
    private boolean processRemainingCalled = false;

    Sink(int chunkSize, int bufferSize) {
      super(chunkSize, bufferSize);
    }

    Sink(int chunkSize) {
      super(chunkSize);
    }

    @Override
    protected HashCode makeHash() {
      return HashCode.fromBytes(outputStream.toByteArray());
    }

    @Override
    protected void process(ByteBuffer byteBuffer) {
      processCallCount++;
      assertEquals(ByteOrder.LITTLE_ENDIAN, byteBuffer.order());
      assertTrue(byteBuffer.remaining() >= chunkSize);

      for (int i = 0; i < chunkSize; i++) {
        outputStream.write(byteBuffer.get());
      }
    }

    @Override
    protected void processRemaining(ByteBuffer byteBuffer) {
      assertFalse(processRemainingCalled);
      processRemainingCalled = true;
      assertEquals(ByteOrder.LITTLE_ENDIAN, byteBuffer.order());
      assertTrue(byteBuffer.remaining() > 0 && byteBuffer.remaining() < bufferSize);

      int beforeProcessCallCount = processCallCount;
      super.processRemaining(byteBuffer);
      int afterProcessCallCount = processCallCount;
      assertEquals(beforeProcessCallCount + 1, afterProcessCallCount);

      processCallCount--; // Adjust for the tail invocation
    }

    void assertInvariants(int expectedBytes) {
      assertEquals(outputStream.toByteArray().length, ceilToMultiple(expectedBytes, chunkSize));
      assertEquals(expectedBytes / chunkSize, processCallCount);
      assertEquals(expectedBytes % chunkSize != 0, processRemainingCalled);
    }

    private static int ceilToMultiple(int value, int multiple) {
      int remainder = value % multiple;
      return remainder == 0 ? value : value + multiple - remainder;
    }

    void assertBytes(byte[] expectedBytes) {
      byte[] actualBytes = outputStream.toByteArray();
      for (int i = 0; i < expectedBytes.length; i++) {
        assertEquals(expectedBytes[i], actualBytes[i]);
      }
    }
  }

  // Control class for testing the AbstractNonStreamingHashFunction
  private static class Control extends AbstractNonStreamingHashFunction {
    @Override
    public HashCode hashBytes(byte[] input, int offset, int length) {
      return HashCode.fromBytes(Arrays.copyOfRange(input, offset, offset + length));
    }

    @Override
    public int bits() {
      throw new UnsupportedOperationException();
    }
  }
}