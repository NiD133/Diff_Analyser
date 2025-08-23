package com.google.common.io;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

/**
 * Readable, behavior-focused tests for MultiInputStream.
 *
 * These tests favor clarity over exhaustive auto-generated edge cases. They verify:
 * - Basic reading from empty and non-empty iterators
 * - Skipping behavior (including negatives and multi-source skipping)
 * - available() and markSupported()
 * - Proper propagation of exceptions (NPE, IOBE, CME)
 * - close() behavior
 */
public class MultiInputStreamTest {

  // ---------------------------------------------------------------------------
  // Helpers
  // ---------------------------------------------------------------------------

  private static MultiInputStream mis(Iterator<? extends ByteSource> it) throws IOException {
    return new MultiInputStream(it);
  }

  private static MultiInputStream mis(ByteSource... sources) throws IOException {
    return mis(Arrays.asList(sources).iterator());
  }

  private static ByteSource bytes(int... unsignedBytes) {
    byte[] arr = new byte[unsignedBytes.length];
    for (int i = 0; i < unsignedBytes.length; i++) {
      arr[i] = (byte) (unsignedBytes[i] & 0xFF);
    }
    return ByteSource.wrap(arr);
  }

  // ---------------------------------------------------------------------------
  // Empty iterator / no data
  // ---------------------------------------------------------------------------

  @Test
  public void read_returnsMinusOne_whenIteratorHasNoSources() throws Exception {
    MultiInputStream in = mis(new LinkedList<ByteSource>().iterator());
    assertEquals(-1, in.read());
  }

  @Test
  public void readIntoBuffer_returnsMinusOne_whenIteratorHasNoSources() throws Exception {
    MultiInputStream in = mis(new LinkedList<ByteSource>().iterator());
    byte[] buffer = new byte[8];

    int n = in.read(buffer, 0, buffer.length);
    assertEquals(-1, n);
  }

  @Test
  public void available_isZero_whenIteratorHasNoSources() throws Exception {
    MultiInputStream in = mis(new LinkedList<ByteSource>().iterator());
    assertEquals(0, in.available());
  }

  @Test
  public void skip_returnsZero_whenAskedToSkipZeroOrNegative() throws Exception {
    MultiInputStream withData = mis(bytes(1, 2, 3));

    assertEquals(0L, withData.skip(0L));
    assertEquals(0L, withData.skip(-10L));
  }

  // ---------------------------------------------------------------------------
  // Reading and skipping from actual data
  // ---------------------------------------------------------------------------

  @Test
  public void read_singleBytes_fromSingleSource() throws Exception {
    MultiInputStream in = mis(bytes(1, 2, 3));
    assertEquals(1, in.read());
    assertEquals(2, in.read());
    assertEquals(3, in.read());
    assertEquals(-1, in.read());
  }

  @Test
  public void readIntoBuffer_readsZerosFromWrappedArray() throws Exception {
    byte[] data = new byte[4]; // all zeros
    MultiInputStream in = mis(ByteSource.wrap(data));

    byte[] buf = new byte[4];
    int n = in.read(buf, 0, buf.length);

    assertEquals(4, n);
    assertArrayEquals(data, buf);
  }

  @Test
  public void skip_skipsAcrossMultipleSources() throws Exception {
    // Total data length is 6 bytes across two sources.
    MultiInputStream in = mis(bytes(0, 0, 0), bytes(1, 1, 1));

    long skipped = in.skip(6);
    assertEquals(6L, skipped);
    assertEquals(-1, in.read());
  }

  @Test
  public void available_reflectsCurrentlyOpenSubstream() throws Exception {
    MultiInputStream in = mis(ByteSource.wrap(new byte[8]));
    assertEquals(8, in.available());
  }

  // ---------------------------------------------------------------------------
  // Stream features
  // ---------------------------------------------------------------------------

  @Test
  public void markIsNotSupported() throws Exception {
    MultiInputStream in = mis(new LinkedList<ByteSource>().iterator());
    assertFalse(in.markSupported());
  }

  @Test
  public void close_isIdempotent_andMarkRemainsUnsupported() throws Exception {
    MultiInputStream in = mis(ByteSource.empty());
    in.close();
    in.close(); // should not throw
    assertFalse(in.markSupported());
  }

  // ---------------------------------------------------------------------------
  // Argument/contract validation
  // ---------------------------------------------------------------------------

  @Test
  public void constructor_nullIterator_throwsNPE() {
    assertThrows(NullPointerException.class, () -> new MultiInputStream((Iterator<? extends ByteSource>) null));
  }

  @Test
  public void constructor_iteratorContainingNull_throwsNPE() {
    List<ByteSource> sources = new ArrayList<>();
    sources.add(null); // first element is null

    assertThrows(NullPointerException.class, () -> mis(sources.iterator()));
  }

  @Test
  public void read_nullBuffer_throwsNPE() throws Exception {
    // Ensure an underlying stream exists so argument checks come from the delegate.
    MultiInputStream in = mis(ByteSource.empty());
    assertThrows(NullPointerException.class, () -> in.read(null, 0, 1));
  }

  @Test
  public void read_invalidOffsetAndLength_throwsIndexOutOfBounds() throws Exception {
    // Ensure an underlying stream exists so the bounds are validated.
    MultiInputStream in = mis(ByteSource.wrap(new byte[0]));
    byte[] empty = new byte[0];

    assertThrows(IndexOutOfBoundsException.class, () -> in.read(empty, 1, 1));
  }

  // ---------------------------------------------------------------------------
  // Iterator fail-fast behavior is propagated
  // ---------------------------------------------------------------------------

  @Test
  public void constructor_propagatesConcurrentModificationException() {
    List<ByteSource> list = new ArrayList<>();
    list.add(ByteSource.empty());

    Iterator<ByteSource> it = list.iterator();
    // Mutate after creating the iterator, before using it:
    list.add(ByteSource.empty());

    assertThrows(ConcurrentModificationException.class, () -> mis(it));
  }

  @Test
  public void read_propagatesConcurrentModification_whenAdvancingToNextStream() throws Exception {
    List<ByteSource> list = new ArrayList<>();
    list.add(ByteSource.empty()); // first stream is empty

    MultiInputStream in = mis(list.iterator());
    // Mutate backing list so iterator becomes fail-fast on next access:
    list.add(ByteSource.empty());

    // First read will hit EOF on current substream and attempt to advance,
    // which should trigger the iterator's ConcurrentModificationException.
    assertThrows(ConcurrentModificationException.class, () -> in.read());
  }

  // ---------------------------------------------------------------------------
  // Additional small behaviors
  // ---------------------------------------------------------------------------

  @Test
  public void skip_fromEmptySource_returnsZero() throws Exception {
    Deque<ByteSource> deque = new LinkedList<>();
    deque.add(ByteSource.empty());
    MultiInputStream in = mis(deque.iterator());

    assertEquals(0L, in.skip(1234));
  }
}