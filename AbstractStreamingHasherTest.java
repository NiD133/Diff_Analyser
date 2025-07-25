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
 */
@NullUnmarked
public class AbstractStreamingHasherTest extends TestCase {

  /**
   * Tests the putByte and putBytes methods of the Sink class.
   */
  public void testBytes() {
    Sink sink = new Sink(4);
    byte[] expectedBytes = {1, 2, 3, 4, 5, 6, 7, 8};
    
    sink.putByte((byte) 1);
    sink.putBytes(new byte[] {2, 3, 4, 5, 6});
    sink.putByte((byte) 7);
    sink.putBytes(new byte[] {});
    sink.putBytes(new byte[] {8});
    
    sink.hash(); // Calculate hash to finalize the state
    sink.assertInvariants(8);
    sink.assertBytes(expectedBytes);
  }

  /**
   * Tests the putShort method of the Sink class.
   */
  public void testShort() {
    Sink sink = new Sink(4);
    sink.putShort((short) 0x0201);
    
    sink.hash();
    sink.assertInvariants(2);
    sink.assertBytes(new byte[] {1, 2, 0, 0}); // Padded with zeros
  }

  /**
   * Tests the putInt method of the Sink class.
   */
  public void testInt() {
    Sink sink = new Sink(4);
    sink.putInt(0x04030201);
    
    sink.hash();
    sink.assertInvariants(4);
    sink.assertBytes(new byte[] {1, 2, 3, 4});
  }

  /**
   * Tests the putLong method of the Sink class.
   */
  public void testLong() {
    Sink sink = new Sink(8);
    sink.putLong(0x0807060504030201L);
    
    sink.hash();
    sink.assertInvariants(8);
    sink.assertBytes(new byte[] {1, 2, 3, 4, 5, 6, 7, 8});
  }

  /**
   * Tests the putChar method of the Sink class.
   */
  public void testChar() {
    Sink sink = new Sink(4);
    sink.putChar((char) 0x0201);
    
    sink.hash();
    sink.assertInvariants(2);
    sink.assertBytes(new byte[] {1, 2, 0, 0}); // Padded with zeros
  }

  /**
   * Tests the putUnencodedChars and putString methods of the Sink class.
   */
  public void testString() {
    Random random = new Random();
    for (int i = 0; i < 100; i++) {
      byte[] randomBytes = new byte[64];
      random.nextBytes(randomBytes);
      String randomString = new String(randomBytes, UTF_16LE);
      
      assertEquals(
          new Sink(4).putUnencodedChars(randomString).hash(),
          new Sink(4).putBytes(randomString.getBytes(UTF_16LE)).hash());
      assertEquals(
          new Sink(4).putUnencodedChars(randomString).hash(),
          new Sink(4).putString(randomString, UTF_16LE).hash());
    }
  }

  /**
   * Tests the putFloat method of the Sink class.
   */
  public void testFloat() {
    Sink sink = new Sink(4);
    sink.putFloat(Float.intBitsToFloat(0x04030201));
    
    sink.hash();
    sink.assertInvariants(4);
    sink.assertBytes(new byte[] {1, 2, 3, 4});
  }

  /**
   * Tests the putDouble method of the Sink class.
   */
  public void testDouble() {
    Sink sink = new Sink(8);
    sink.putDouble(Double.longBitsToDouble(0x0807060504030201L));
    
    sink.hash();
    sink.assertInvariants(8);
    sink.assertBytes(new byte[] {1, 2, 3, 4, 5, 6, 7, 8});
  }

  /**
   * Tests that the correct exceptions are thrown for invalid input.
   */
  public void testCorrectExceptions() {
    Sink sink = new Sink(4);
    assertThrows(IndexOutOfBoundsException.class, () -> sink.putBytes(new byte[8], -1, 4));
    assertThrows(IndexOutOfBoundsException.class, () -> sink.putBytes(new byte[8], 0, 16));
    assertThrows(IndexOutOfBoundsException.class, () -> sink.putBytes(new byte[8], 0, -1));
  }

  /**
   * Tests the consistency of hash results across different configurations of the Sink class.
   */
  @AndroidIncompatible // slow. TODO: Maybe reduce iterations under Android.
  public void testExhaustive() throws Exception {
    Random random = new Random(0);
    for (int totalInsertions = 0; totalInsertions < 200; totalInsertions++) {
      List<Sink> sinks = createSinks();
      Control control = new Control();
      Hasher controlSink = control.newHasher(1024);

      Iterable<Hasher> allHashers = Iterables.concat(sinks, Collections.singleton(controlSink));
      for (int insertion = 0; insertion < totalInsertions; insertion++) {
        RandomHasherAction.pickAtRandom(random).performAction(random, allHashers);
      }

      int randomInt = random.nextInt();
      for (Hasher hasher : allHashers) {
        hasher.putInt(randomInt);
      }
      for (Sink sink : sinks) {
        sink.hash();
      }

      byte[] expectedHash = controlSink.hash().asBytes();
      for (Sink sink : sinks) {
        sink.assertInvariants(expectedHash.length);
        sink.assertBytes(expectedHash);
      }
    }
  }

  /**
   * Creates a list of Sink instances with varying configurations.
   */
  private List<Sink> createSinks() {
    List<Sink> sinks = new ArrayList<>();
    for (int chunkSize = 4; chunkSize <= 32; chunkSize++) {
      for (int bufferSize = chunkSize; bufferSize <= chunkSize * 4; bufferSize += chunkSize) {
        sinks.add(new Sink(chunkSize, bufferSize));
      }
    }
    return sinks;
  }

  /**
   * A test implementation of AbstractStreamingHasher for testing purposes.
   */
  private static class Sink extends AbstractStreamingHasher {
    final int chunkSize;
    final int bufferSize;
    final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    int processCallCount = 0;
    boolean processRemainingCalled = false;

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
      return HashCode.fromBytes(outputStream.toByteArray());
    }

    @Override
    protected void process(ByteBuffer buffer) {
      processCallCount++;
      assertEquals(ByteOrder.LITTLE_ENDIAN, buffer.order());
      assertTrue(buffer.remaining() >= chunkSize);
      for (int i = 0; i < chunkSize; i++) {
        outputStream.write(buffer.get());
      }
    }

    @Override
    protected void processRemaining(ByteBuffer buffer) {
      assertFalse(processRemainingCalled);
      processRemainingCalled = true;
      assertEquals(ByteOrder.LITTLE_ENDIAN, buffer.order());
      assertTrue(buffer.remaining() > 0);
      assertTrue(buffer.remaining() < bufferSize);
      int beforeProcessCallCount = processCallCount;
      super.processRemaining(buffer);
      int afterProcessCallCount = processCallCount;
      assertEquals(beforeProcessCallCount + 1, afterProcessCallCount);
      processCallCount--;
    }

    void assertInvariants(int expectedBytes) {
      assertEquals(outputStream.toByteArray().length, ceilToMultiple(expectedBytes, chunkSize));
      assertEquals(expectedBytes / chunkSize, processCallCount);
      assertEquals(expectedBytes % chunkSize != 0, processRemainingCalled);
    }

    private static int ceilToMultiple(int a, int b) {
      int remainder = a % b;
      return remainder == 0 ? a : a + b - remainder;
    }

    void assertBytes(byte[] expected) {
      byte[] actual = outputStream.toByteArray();
      for (int i = 0; i < expected.length; i++) {
        assertEquals(expected[i], actual[i]);
      }
    }
  }

  /**
   * A control implementation of AbstractNonStreamingHashFunction for testing purposes.
   */
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