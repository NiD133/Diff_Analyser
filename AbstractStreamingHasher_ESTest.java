package com.google.common.hash;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.Assert.*;

import java.nio.ByteBuffer;
import org.junit.Test;

/**
 * Readable tests for the streaming hasher behavior using the concrete CRC32C hasher.
 *
 * These tests focus on:
 * - Basic, successful usage (putting data, chaining calls, producing a hash)
 * - Clear, minimal validation of error handling (nulls, index bounds)
 * - Observable behavior of ByteBuffer-based input (consumes remaining bytes)
 *
 * They avoid relying on subtle internal states (e.g., calling internal methods or
 * depending on specific exceptions after finalization), making them easier to
 * understand and maintain.
 */
public class AbstractStreamingHasherTest {

  @Test
  public void putBytes_withByteBuffer_consumesInputAndReturnsSelf() {
    Crc32cHashFunction.Crc32cHasher hasher = new Crc32cHashFunction.Crc32cHasher();

    // Prepare a buffer with a window [pos=2, limit=5] to ensure only remaining bytes are read
    byte[] data = "abcdef".getBytes(UTF_8); // length = 6
    ByteBuffer buf = ByteBuffer.wrap(data);
    buf.position(2);
    buf.limit(5); // will expose bytes "cde"

    Hasher returned = hasher.putBytes(buf);

    assertSame("putBytes(ByteBuffer) should be chainable and return the same instance", hasher, returned);
    assertEquals("putBytes should consume all remaining bytes from the buffer", buf.limit(), buf.position());
  }

  @Test
  public void putBytes_withArraySubset_validRange_returnsSelf() {
    Crc32cHashFunction.Crc32cHasher hasher = new Crc32cHashFunction.Crc32cHasher();

    byte[] data = "hello".getBytes(UTF_8);
    Hasher returned = hasher.putBytes(data, 1, 3); // "ell"

    assertSame("putBytes(byte[], off, len) should be chainable", hasher, returned);

    // Also verify we can successfully produce a hash
    assertNotNull(hasher.hash());
  }

  @Test
  public void putBytes_withArray_outOfBounds_throwsIndexOutOfBoundsException() {
    Crc32cHashFunction.Crc32cHasher hasher = new Crc32cHashFunction.Crc32cHasher();
    byte[] data = new byte[3];

    assertThrows(IndexOutOfBoundsException.class, () -> hasher.putBytes(data, 0, 67));
  }

  @Test
  public void putBytes_withNullArray_throwsNullPointerException() {
    Crc32cHashFunction.Crc32cHasher hasher = new Crc32cHashFunction.Crc32cHasher();

    assertThrows(NullPointerException.class, () -> hasher.putBytes((byte[]) null, 0, 1));
  }

  @Test
  public void putBytes_withNullByteBuffer_throwsNullPointerException() {
    Crc32cHashFunction.Crc32cHasher hasher = new Crc32cHashFunction.Crc32cHasher();

    assertThrows(NullPointerException.class, () -> hasher.putBytes((ByteBuffer) null));
  }

  @Test
  public void methods_areChainable() {
    Crc32cHashFunction.Crc32cHasher hasher = new Crc32cHashFunction.Crc32cHasher();

    Hasher afterByte = hasher.putByte((byte) 0x01);
    Hasher afterShort = hasher.putShort((short) 0x0203);
    Hasher afterChar = hasher.putChar('Z');
    Hasher afterInt = hasher.putInt(0x04050607);
    Hasher afterLong = hasher.putLong(0x08090A0B0C0D0E0FL);

    assertSame(hasher, afterByte);
    assertSame(hasher, afterShort);
    assertSame(hasher, afterChar);
    assertSame(hasher, afterInt);
    assertSame(hasher, afterLong);
    assertNotNull(hasher.hash());
  }

  @Test
  public void equalInputs_produceSameHash() {
    byte[] data = "consistent input".getBytes(UTF_8);

    Crc32cHashFunction.Crc32cHasher h1 = new Crc32cHashFunction.Crc32cHasher();
    Crc32cHashFunction.Crc32cHasher h2 = new Crc32cHashFunction.Crc32cHasher();

    HashCode c1 = h1.putBytes(data).hash();
    HashCode c2 = h2.putBytes(data).hash();

    assertEquals("Same input should produce the same hash", c1, c2);
  }

  @Test
  public void hash_isIdempotent() {
    Crc32cHashFunction.Crc32cHasher hasher = new Crc32cHashFunction.Crc32cHasher();
    hasher.putBytes("idempotent".getBytes(UTF_8));

    HashCode first = hasher.hash();
    HashCode second = hasher.hash();

    assertEquals("Calling hash() multiple times should return the same result", first, second);
  }
}