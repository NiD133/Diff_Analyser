package com.google.common.io;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.CharBuffer;
import java.nio.ReadOnlyBufferException;
import org.junit.Test;

/**
 * Readable, behavior-focused tests for CharSequenceReader.
 *
 * Notes:
 * - Tests use simple, descriptive scenarios.
 * - Arrange-Act-Assert structure with clear names.
 * - Avoids EvoSuite-specific scaffolding and timeouts.
 */
public class CharSequenceReaderTest {

  // -- Construction ------------------------------------------------------------------------------

  @Test(expected = NullPointerException.class)
  public void constructor_nullCharSequence_throwsNPE() {
    new CharSequenceReader(null);
  }

  // -- Basic reading ------------------------------------------------------------------------------

  @Test
  public void read_singleChars_thenEof() throws IOException {
    CharSequenceReader r = new CharSequenceReader("ab");

    assertEquals('a', r.read());
    assertEquals('b', r.read());
    assertEquals(-1, r.read());
  }

  @Test
  public void read_intoCharArray_readsUpToLen() throws IOException {
    CharSequenceReader r = new CharSequenceReader("abcdef");
    char[] buf = new char[4];

    int n = r.read(buf, 0, 4);

    assertEquals(4, n);
    assertArrayEquals(new char[] {'a', 'b', 'c', 'd'}, buf);
  }

  @Test
  public void read_zeroLength_returnsZero_evenAtEof() throws IOException {
    CharSequenceReader r = new CharSequenceReader("x");
    char[] buf = new char[2];

    // Consume all input.
    assertEquals(1, r.read(buf, 0, 1));
    assertEquals(-1, r.read(buf, 0, 1));

    // Zero-length read always returns 0 per Reader contract.
    assertEquals(0, r.read(buf, 0, 0));
  }

  @Test
  public void read_intoCharBuffer_readsToCapacity_thenEof() throws IOException {
    CharSequenceReader r = new CharSequenceReader("hi");
    CharBuffer target = CharBuffer.allocate(2);

    int first = r.read(target);
    int second = r.read(target); // target is full

    assertEquals(2, first);
    assertEquals(0, second); // target has no remaining space, so nothing is read
    assertArrayEquals(new char[] {'h', 'i'}, target.array());
    assertEquals(-1, r.read(CharBuffer.allocate(1))); // fresh target, but source is at EOF
  }

  @Test
  public void read_intoCharBuffer_noCapacity_returnsZero() throws IOException {
    CharSequenceReader r = new CharSequenceReader("data");
    CharBuffer full = CharBuffer.allocate(0); // no remaining

    assertEquals(0, r.read(full));
  }

  // -- Bounds and argument validation -------------------------------------------------------------

  @Test(expected = NullPointerException.class)
  public void read_intoNullArray_throwsNPE() throws IOException {
    CharSequenceReader r = new CharSequenceReader("abc");
    r.read(null, 0, 1);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void read_withInvalidOffsets_throwsIOOBE() throws IOException {
    CharSequenceReader r = new CharSequenceReader("abc");
    char[] buf = new char[3];
    r.read(buf, -1, -1);
  }

  @Test(expected = ReadOnlyBufferException.class)
  public void read_intoReadOnlyCharBuffer_throwsReadOnlyBufferException() throws IOException {
    CharSequenceReader r = new CharSequenceReader("ro");
    CharBuffer readOnly = CharBuffer.wrap("....").asReadOnlyBuffer();
    r.read(readOnly);
  }

  // -- Skip ---------------------------------------------------------------------------------------

  @Test
  public void skip_skipsUpToRemaining() throws IOException {
    CharSequenceReader r = new CharSequenceReader("abcd");

    assertEquals(2, r.skip(2));
    assertEquals('c', r.read());
    assertEquals(1, r.skip(10)); // only 'd' remains
    assertEquals(-1, r.read());
  }

  @Test
  public void skip_zero_returnsZero() throws IOException {
    CharSequenceReader r = new CharSequenceReader("x");
    assertEquals(0, r.skip(0));
  }

  @Test(expected = IllegalArgumentException.class)
  public void skip_negative_throwsIAE() throws IOException {
    CharSequenceReader r = new CharSequenceReader("abc");
    r.skip(-1);
  }

  // -- Ready --------------------------------------------------------------------------------------

  @Test
  public void ready_onOpenReader_returnsTrue() throws IOException {
    CharSequenceReader r = new CharSequenceReader("abc");
    assertTrue(r.ready());
  }

  // -- Mark/reset ---------------------------------------------------------------------------------

  @Test
  public void markSupported_isTrue() {
    CharSequenceReader r = new CharSequenceReader("anything");
    assertTrue(r.markSupported());
  }

  @Test
  public void mark_and_reset_restorePosition() throws IOException {
    CharSequenceReader r = new CharSequenceReader("wxyz");

    assertEquals('w', r.read());
    r.mark(100);

    assertEquals('x', r.read());
    assertEquals('y', r.read());

    r.reset(); // back to after 'w'
    assertEquals('x', r.read());
    assertEquals('y', r.read());
    assertEquals('z', r.read());
    assertEquals(-1, r.read());
  }

  @Test(expected = IllegalArgumentException.class)
  public void mark_negativeReadAhead_throwsIAE() throws IOException {
    CharSequenceReader r = new CharSequenceReader("abc");
    r.mark(-1);
  }

  // -- Close semantics ----------------------------------------------------------------------------

  @Test
  public void close_thenRead_throwsIOException() throws IOException {
    CharSequenceReader r = new CharSequenceReader("abc");
    r.close();

    try {
      r.read();
      fail("Expected IOException after close");
    } catch (IOException expected) {
      // ok
    }
  }

  @Test
  public void close_thenReadArray_throwsIOException() throws IOException {
    CharSequenceReader r = new CharSequenceReader("abc");
    r.close();

    try {
      r.read(new char[1], 0, 1);
      fail("Expected IOException after close");
    } catch (IOException expected) {
      // ok
    }
  }

  @Test
  public void close_thenReadCharBuffer_throwsIOException() throws IOException {
    CharSequenceReader r = new CharSequenceReader("abc");
    r.close();

    try {
      r.read(CharBuffer.allocate(1));
      fail("Expected IOException after close");
    } catch (IOException expected) {
      // ok
    }
  }

  @Test
  public void close_thenSkip_throwsIOException() throws IOException {
    CharSequenceReader r = new CharSequenceReader("abc");
    r.close();

    try {
      r.skip(1);
      fail("Expected IOException after close");
    } catch (IOException expected) {
      // ok
    }
  }

  @Test
  public void close_thenReady_throwsIOException() throws IOException {
    CharSequenceReader r = new CharSequenceReader("abc");
    r.close();

    try {
      r.ready();
      fail("Expected IOException after close");
    } catch (IOException expected) {
      // ok
    }
  }

  @Test
  public void close_thenMarkOrReset_throwsIOException() throws IOException {
    CharSequenceReader r = new CharSequenceReader("abc");
    r.close();

    try {
      r.mark(1);
      fail("Expected IOException after close");
    } catch (IOException expected) {
      // ok
    }

    try {
      r.reset();
      fail("Expected IOException after close");
    } catch (IOException expected) {
      // ok
    }
  }
}