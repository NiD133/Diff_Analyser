package com.google.common.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.jspecify.annotations.NullUnmarked;
import org.junit.Test;

/**
 * Tests for {@link MultiInputStream}, primarily through the {@link ByteSource#concat(Iterable)}
 * factory method.
 *
 * <p>This class extends {@code IoTestCase}, a custom base class that provides helper methods like
 * {@code newPreFilledByteArray} for creating test data.
 */
@NullUnmarked
public class MultiInputStreamTest extends IoTestCase {

  @Test
  public void concat_withVaryingSizes_contentIsCorrect() throws Exception {
    // Test with multiple non-empty sources
    assertConcatenatedSourcesContentIsCorrect(10, 20, 30);
    // Test with a mix of empty and non-empty sources
    assertConcatenatedSourcesContentIsCorrect(0, 10, 0);
    // Test with only empty sources
    assertConcatenatedSourcesContentIsCorrect(0, 0, 0);
    // Test with a single source
    assertConcatenatedSourcesContentIsCorrect(100);
    // Test with no sources (edge case)
    assertConcatenatedSourcesContentIsCorrect();
  }

  @Test
  public void concat_withManyEmptySources_readsToEndOfStream() throws IOException {
    // This test ensures that advancing through a large number of empty sources
    // is handled efficiently and correctly, without issues like StackOverflowError.
    try (InputStream stream = openStreamFromTenMillionEmptySources()) {
      assertEquals("Reading from a stream of empty sources should result in EOF", -1, stream.read());
    }
  }

  @Test
  public void concat_onlyOpensOneStreamAtATime() throws Exception {
    // A ByteSource that wraps another and counts how many of its streams are currently open.
    // This is used to verify that MultiInputStream opens and closes streams sequentially.
    class OpenStreamCountingByteSource extends ByteSource {
      private final AtomicInteger openStreamCount = new AtomicInteger(0);
      private final ByteSource delegate;

      OpenStreamCountingByteSource(ByteSource delegate) {
        this.delegate = delegate;
      }

      @Override
      public InputStream openStream() throws IOException {
        // If a stream from this source is already open, it's a failure.
        if (openStreamCount.getAndIncrement() != 0) {
          throw new IllegalStateException("More than one stream was open at once.");
        }

        // Return a stream that decrements the counter when it's closed.
        return new FilterInputStream(delegate.openStream()) {
          @Override
          public void close() throws IOException {
            super.close();
            openStreamCount.decrementAndGet();
          }
        };
      }
    }

    ByteSource underlyingSource = newByteSource(0, 50);
    ByteSource countingSource = new OpenStreamCountingByteSource(underlyingSource);

    // Concatenate the same counting source three times. MultiInputStream should
    // open a stream, read it, close it, and then move to the next one.
    ByteSource concatenated = ByteSource.concat(countingSource, countingSource, countingSource);

    // Reading the concatenated source should succeed without throwing the IllegalStateException.
    byte[] result = concatenated.read();

    // Also verify that the content was read correctly.
    assertEquals(150, result.length);
  }

  /**
   * Asserts that concatenating sources of the given sizes produces a source with the correct
   * combined content.
   */
  private void assertConcatenatedSourcesContentIsCorrect(Integer... sourceSizes) throws Exception {
    List<ByteSource> sources = new ArrayList<>();
    int totalSize = 0;
    for (Integer size : sourceSizes) {
      // Each source has unique content based on its position in the final stream.
      sources.add(newByteSource(totalSize, size));
      totalSize += size;
    }

    ByteSource concatenated = ByteSource.concat(sources);

    // The expected source contains all the bytes from 0 to totalSize.
    ByteSource expected = newByteSource(0, totalSize);

    assertTrue(
        "Concatenated content should match the expected content",
        expected.contentEquals(concatenated));
  }

  /**
   * Creates a {@link MultiInputStream} from a very large number of empty sources. Useful for
   * performance and edge-case testing.
   */
  private static MultiInputStream openStreamFromTenMillionEmptySources() throws IOException {
    int sourceCount = 10_000_000;
    return new MultiInputStream(Collections.nCopies(sourceCount, ByteSource.empty()).iterator());
  }

  /**
   * Creates a {@link ByteSource} that supplies a byte array of the given {@code size}, with byte
   * values starting from {@code start}.
   */
  private static ByteSource newByteSource(int start, int size) {
    return new ByteSource() {
      @Override
      public InputStream openStream() {
        // IoTestCase.newPreFilledByteArray is a helper for creating predictable test data.
        return new ByteArrayInputStream(newPreFilledByteArray(start, size));
      }
    };
  }
}