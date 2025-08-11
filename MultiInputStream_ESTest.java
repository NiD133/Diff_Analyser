/*
 * Copyright (C) 2007 The Guava Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.google.common.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import com.google.common.collect.ImmutableList;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import org.junit.Test;

/**
 * Tests for {@link MultiInputStream}.
 */
public class MultiInputStreamTest {

  @Test(expected = NullPointerException.class)
  public void constructor_withNullIterator_throwsNullPointerException() throws IOException {
    new MultiInputStream(null);
  }

  @Test(expected = NullPointerException.class)
  public void constructor_withNullByteSourceInIterator_throwsNullPointerException() throws IOException {
    List<ByteSource> sources = Arrays.asList(ByteSource.empty(), null);
    new MultiInputStream(sources.iterator());
  }

  @Test(expected = ConcurrentModificationException.class)
  public void constructor_whenSourceIteratorIsModified_throwsCME() throws IOException {
    List<ByteSource> sources = new ArrayList<>();
    sources.add(ByteSource.empty());
    Iterator<ByteSource> iterator = sources.iterator();

    // Modify the list after creating the iterator but before passing it to the constructor.
    sources.add(ByteSource.empty());

    // The constructor immediately tries to advance the iterator, which detects the modification.
    new MultiInputStream(iterator);
  }

  @Test
  public void read_fromEmptySourceIterator_returnsMinusOne() throws IOException {
    InputStream multiStream = new MultiInputStream(Collections.emptyIterator());
    assertEquals(-1, multiStream.read());
  }

  @Test
  public void read_intoBufferFromEmptyStream_returnsMinusOne() throws IOException {
    InputStream multiStream = new MultiInputStream(Collections.emptyIterator());
    byte[] buffer = new byte[10];
    assertEquals(-1, multiStream.read(buffer, 0, 10));
  }

  @Test
  public void read_fromMultipleSources_readsSequentially() throws IOException {
    // Arrange
    byte[] data1 = {1, 2};
    byte[] data2 = {3, 4, 5};
    List<ByteSource> sources = ImmutableList.of(
        ByteSource.wrap(data1),
        ByteSource.empty(), // Should be skipped automatically
        ByteSource.wrap(data2));
    InputStream multiStream = new MultiInputStream(sources.iterator());

    // Act & Assert
    assertEquals(1, multiStream.read());
    assertEquals(2, multiStream.read());
    // Stream should advance past the empty source to the next one
    assertEquals(3, multiStream.read());
    assertEquals(4, multiStream.read());
    assertEquals(5, multiStream.read());
    assertEquals(-1, multiStream.read());
  }

  @Test(expected = ConcurrentModificationException.class)
  public void read_whenSourceIteratorIsModified_throwsCME() throws IOException {
    List<ByteSource> sources = new ArrayList<>();
    sources.add(ByteSource.empty());
    InputStream multiStream = new MultiInputStream(sources.iterator());

    // Modify the underlying collection after the stream is created
    sources.add(ByteSource.wrap(new byte[1]));

    // Reading from the empty source returns -1, which triggers an attempt to advance
    // to the next source, causing the fail-fast iterator to throw.
    multiStream.read();
  }

  @Test
  public void skip_acrossMultipleSources_skipsCorrectNumberOfBytes() throws IOException {
    // Arrange
    byte[] data1 = {0, 1, 2};
    byte[] data2 = {3, 4, 5, 6, 7};
    List<ByteSource> sources = ImmutableList.of(ByteSource.wrap(data1), ByteSource.wrap(data2));
    InputStream multiStream = new MultiInputStream(sources.iterator());

    // Act: Skip 5 bytes (3 from the first stream, 2 from the second)
    long bytesSkipped = multiStream.skip(5);

    // Assert
    assertEquals(5L, bytesSkipped);
    // The next byte read should be the 6th byte overall (value 5)
    assertEquals(5, multiStream.read());
  }

  @Test
  public void skip_advancesPastEmptySources() throws IOException {
    // Arrange
    ByteSource nonEmptySource = ByteSource.wrap(new byte[] {1, 2, 3});
    List<ByteSource> sources = ImmutableList.of(
        ByteSource.empty(),
        ByteSource.empty(),
        nonEmptySource);
    InputStream multiStream = new MultiInputStream(sources.iterator());

    // Act: This should skip over the two empty sources and skip 2 bytes from the third source.
    long bytesSkipped = multiStream.skip(2);

    // Assert
    assertEquals(2L, bytesSkipped);
    assertEquals(3, multiStream.read()); // Next byte should be '3'
  }

  @Test
  public void skip_onFinalEmptySource_returnsZero() throws IOException {
    InputStream multiStream = new MultiInputStream(ImmutableList.of(ByteSource.empty()).iterator());
    assertEquals(0L, multiStream.skip(100));
    assertEquals(-1, multiStream.read()); // Verify it's at the end
  }

  @Test
  public void skip_withNegativeValue_returnsZero() throws IOException {
    InputStream multiStream = new MultiInputStream(
        ImmutableList.of(ByteSource.wrap(new byte[3])).iterator());
    assertEquals(0L, multiStream.skip(-10));
  }

  @Test
  public void available_returnsAvailableBytesAndAdvancesToNextStream() throws IOException {
    // Arrange
    byte[] data1 = {1, 2};
    byte[] data2 = {3, 4, 5};
    List<ByteSource> sources = ImmutableList.of(ByteSource.wrap(data1), ByteSource.wrap(data2));
    InputStream multiStream = new MultiInputStream(sources.iterator());

    // Assert on first stream
    assertEquals(2, multiStream.available());
    multiStream.read(); // Read '1'
    assertEquals(1, multiStream.available());
    multiStream.read(); // Read '2'
    assertEquals(0, multiStream.available());

    // Read to advance to the next stream
    assertEquals(3, multiStream.read());

    // Assert on second stream
    assertEquals(2, multiStream.available());
  }

  @Test
  public void close_closesCurrentStreamAndFurtherReadsFail() throws IOException {
    InputStream multiStream = new MultiInputStream(
        ImmutableList.of(ByteSource.wrap(new byte[3])).iterator());

    multiStream.close();

    try {
      multiStream.read();
      fail("Expected an IOException after closing the stream");
    } catch (IOException expected) {
      // Expected behavior
    }
  }

  @Test
  public void close_canBeCalledMultipleTimes() throws IOException {
    InputStream multiStream = new MultiInputStream(
        ImmutableList.of(ByteSource.wrap(new byte[3])).iterator());
    multiStream.close();
    multiStream.close(); // Should not throw an exception
  }

  @Test
  public void markSupported_isFalse() throws IOException {
    InputStream multiStream = new MultiInputStream(Collections.emptyIterator());
    assertFalse(multiStream.markSupported());
  }
}