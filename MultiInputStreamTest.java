/*
 * Copyright (C) 2007 The Guava Authors
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

package com.google.common.io;

import java.io.ByteArrayInputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.jspecify.annotations.NullUnmarked;

/**
 * Test class for {@link MultiInputStream}.
 *
 * @author Chris Nokleberg
 */
@NullUnmarked
public class MultiInputStreamTest extends IoTestCase {

  /**
   * Tests that concatenating multiple sources of varying sizes produces a source that contains the
   * data of all sources in order.
   */
  public void testConcat_withMultipleSources_readsAllBytes() throws IOException {
    // Arrange
    List<ByteSource> sources = new ArrayList<>();
    sources.add(newByteSource(0, 10));
    sources.add(newByteSource(10, 20));
    sources.add(newByteSource(30, 0)); // empty source
    sources.add(newByteSource(30, 15));

    ByteSource concatenated = ByteSource.concat(sources);
    ByteSource expected = newByteSource(0, 45);

    // Act & Assert
    assertTrue(
        "Concatenated stream content should match the expected content",
        expected.contentEquals(concatenated));
  }

  /** Tests that concatenating a single source results in a source equivalent to the original. */
  public void testConcat_withSingleSource_isEquivalentToOriginal() throws IOException {
    // Arrange
    ByteSource source = newByteSource(0, 100);

    // Act
    ByteSource concatenated = ByteSource.concat(source);

    // Assert
    assertTrue(
        "Concatenating a single source should be equivalent to the source itself",
        source.contentEquals(concatenated));
  }

  /** Tests that concatenating only empty sources results in an empty source. */
  public void testConcat_withOnlyEmptySources_resultsInEmptySource() throws IOException {
    // Arrange
    ByteSource concatenated =
        ByteSource.concat(ByteSource.empty(), ByteSource.empty(), ByteSource.empty());

    // Act & Assert
    assertEquals("Concatenating empty sources should result in a size of 0", 0, concatenated.size());
    assertTrue("Concatenating empty sources should result in an empty source", concatenated.isEmpty());
  }

  /**
   * Verifies that {@link MultiInputStream} (via {@code ByteSource.concat}) closes one underlying
   * stream before opening the next one.
   */
  public void testConcat_opensOnlyOneStreamAtATime() throws Exception {
    // Arrange
    AtomicInteger openStreams = new AtomicInteger(0);
    ByteSource sourceTemplate = newByteSource(0, 50);

    // Create three sources that use the counter to verify that at most one is open.
    ByteSource source1 = new OpenStreamCounter(sourceTemplate, openStreams);
    ByteSource source2 = new OpenStreamCounter(sourceTemplate, openStreams);
    ByteSource source3 = new OpenStreamCounter(sourceTemplate, openStreams);

    ByteSource concatenated = ByteSource.concat(source1, source2, source3);

    // Act
    byte[] result = concatenated.read();

    // Assert
    assertEquals("Total bytes read should be the sum of all source sizes", 150, result.length);
    assertEquals("All streams should be closed after reading", 0, openStreams.get());
  }

  /**
   * Tests reading byte-by-byte from a concatenated stream, and also verifies {@code available()}
   * and {@code markSupported()}.
   */
  public void testRead_byteByByte_readsAllBytes() throws Exception {
    // Arrange
    final int sourceSize = 10;
    ByteSource source = newByteSource(0, sourceSize);
    ByteSource concatenated = ByteSource.concat(source, source);
    InputStream in = concatenated.openStream();

    // Assert initial state
    assertEquals(
        "Concatenated size should be sum of source sizes", sourceSize * 2, concatenated.size());
    assertFalse("MultiInputStream should not support mark", in.markSupported());
    // available() returns the size of the *current* stream
    assertEquals("available() should report size of the first stream", sourceSize, in.available());

    // Act: Read all bytes one by one
    int bytesRead = 0;
    while (in.read() != -1) {
      bytesRead++;
    }

    // Assert final state
    assertEquals("Should have read all bytes from both sources", sourceSize * 2, bytesRead);
    assertEquals("Stream should be exhausted", -1, in.read());
    assertEquals("available() should be 0 after reading all data", 0, in.available());
    in.close();
  }

  /** Tests that skip() with a negative argument is a no-op and returns 0. */
  public void testSkip_withNegativeArgument_returnsZero() throws IOException {
    InputStream stream = ByteSource.concat(newByteSource(0, 10)).openStream();
    assertEquals(0, stream.skip(-1));
    assertEquals("Position should not advance after a negative skip", 0, stream.read());
  }

  /** Tests that skip() with a zero argument is a no-op and returns 0. */
  public void testSkip_withZeroArgument_returnsZero() throws IOException {
    InputStream stream = ByteSource.concat(newByteSource(0, 10)).openStream();
    assertEquals(0, stream.skip(0));
    assertEquals("Position should not advance after a zero skip", 0, stream.read());
  }

  /**
   * Tests that {@link MultiInputStream#skip} can skip bytes by reading when the underlying stream's
   * {@code skip} method is ineffective (always returns 0).
   */
  public void testSkip_whenUnderlyingStreamIsIneffective_skipsByReading() throws Exception {
    // Arrange: Create a source whose stream's skip() method does nothing.
    final int totalSize = 50;
    final int bytesToSkip = 20;
    ByteSource ineffectiveSkipSource = createSourceWithIneffectiveSkip(totalSize);

    // Note: We test MultiInputStream directly here to control the underlying stream's behavior.
    InputStream multiStream =
        new MultiInputStream(Collections.singleton(ineffectiveSkipSource).iterator());

    // Act: Try to skip bytes. Since the underlying skip() is ineffective,
    // MultiInputStream should fall back to reading and discarding bytes.
    ByteStreams.skipFully(multiStream, bytesToSkip);

    // Assert: The next byte read should be the one after the skipped section.
    // newPreFilledByteArray fills with bytes 0, 1, 2, ..., so byte 20 should have value 20.
    assertEquals(
        "Should have skipped the correct number of bytes", bytesToSkip, multiStream.read());
  }

  /**
   * Verifies that reading from a MultiInputStream with a huge number of empty sources does not
   * cause a StackOverflowError due to recursive calls to {@code advance()}.
   *
   * @see <a href="https://github.com/google/guava/issues/2996">Guava Issue #2996</a>
   */
  public void testRead_withManyEmptySources_doesNotCauseStackOverflow() throws IOException {
    // This test has no data; it just verifies that creating and reading from a stream
    // with millions of empty sources doesn't blow the stack.
    InputStream stream = createStreamWithTenMillionEmptySources();
    assertEquals(-1, stream.read());
  }

  /**
   * Verifies that reading into a byte array from a MultiInputStream with a huge number of empty
   * sources does not cause a StackOverflowError.
   *
   * @see <a href="https://github.com/google/guava/issues/2996">Guava Issue #2996</a>
   */
  public void testReadIntoByteArray_withManyEmptySources_doesNotCauseStackOverflow()
      throws IOException {
    InputStream stream = createStreamWithTenMillionEmptySources();
    assertEquals(-1, stream.read(new byte[1]));
  }

  private static InputStream createStreamWithTenMillionEmptySources() throws IOException {
    // Using a large number of empty sources is a good way to test for deep recursion
    // in the advance() method.
    int numEmptySources = 10_000_000;
    Iterator<ByteSource> sources =
        Collections.nCopies(numEmptySources, ByteSource.empty()).iterator();
    return new MultiInputStream(sources);
  }

  /**
   * A {@link ByteSource} that wraps another source to count how many streams are concurrently open.
   */
  private static class OpenStreamCounter extends ByteSource {
    private final ByteSource delegate;
    private final AtomicInteger openStreams;

    OpenStreamCounter(ByteSource delegate, AtomicInteger openStreams) {
      this.delegate = delegate;
      this.openStreams = openStreams;
    }

    @Override
    public InputStream openStream() throws IOException {
      if (openStreams.getAndIncrement() != 0) {
        throw new IllegalStateException("Expected only one stream to be open at a time.");
      }
      return new FilterInputStream(delegate.openStream()) {
        @Override
        public void close() throws IOException {
          super.close();
          openStreams.decrementAndGet();
        }
      };
    }
  }

  /**
   * Creates a {@link ByteSource} whose {@link InputStream#skip} method is ineffective, always
   * returning 0. This helps test fallback logic in consumers.
   */
  private static ByteSource createSourceWithIneffectiveSkip(int size) {
    byte[] bytes = newPreFilledByteArray(0, size);
    return new ByteSource() {
      @Override
      public InputStream openStream() {
        return new ByteArrayInputStream(bytes) {
          @Override
          public long skip(long n) {
            // This simulates a stream that can't skip, forcing the caller to read.
            return 0;
          }
        };
      }
    };
  }

  private static ByteSource newByteSource(int start, int size) {
    return new ByteSource() {
      @Override
      public InputStream openStream() {
        return new ByteArrayInputStream(newPreFilledByteArray(start, size));
      }
    };
  }
}