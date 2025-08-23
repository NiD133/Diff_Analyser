package com.google.common.io;

import static com.google.common.io.IoTestCase.newPreFilledByteArray;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.jspecify.annotations.NullUnmarked;

/**
 * Tests for {@link ByteSource#concat(Iterable)}.
 *
 * <p>While {@code ByteSource.concat} uses a {@link MultiInputStream} internally, these tests
 * verify the public API behavior from the perspective of a {@code ByteSource} user.
 */
@NullUnmarked
public class ByteSourceConcatTest extends IoTestCase {

  public void testConcat_noSources_isEmpty() throws IOException {
    assertConcatContentIsCorrect(); // No arguments means concatenating zero sources
  }

  public void testConcat_singleEmptySource_isEmpty() throws IOException {
    assertConcatContentIsCorrect(0);
  }

  public void testConcat_singleNonEmptySource_hasSameContent() throws IOException {
    assertConcatContentIsCorrect(100);
  }

  public void testConcat_multipleEmptySources_isEmpty() throws IOException {
    assertConcatContentIsCorrect(0, 0, 0);
  }

  public void testConcat_multipleNonEmptySources_hasCombinedContent() throws IOException {
    assertConcatContentIsCorrect(10, 20, 30);
  }

  public void testConcat_includesEmptySourceAtStart_hasCombinedContent() throws IOException {
    assertConcatContentIsCorrect(0, 10, 20);
  }

  public void testConcat_includesEmptySourceInMiddle_hasCombinedContent() throws IOException {
    assertConcatContentIsCorrect(10, 0, 20);
  }

  public void testConcat_includesEmptySourceAtEnd_hasCombinedContent() throws IOException {
    assertConcatContentIsCorrect(10, 20, 0);
  }

  public void testConcat_manySmallSources_hasCombinedContent() throws IOException {
    assertConcatContentIsCorrect(1, 1, 1, 1, 1, 1, 1, 1);
  }

  public void testConcat_alternatingEmptyAndNonEmptySources_hasCombinedContent() throws IOException {
    assertConcatContentIsCorrect(1, 0, 1, 0, 1, 0, 1, 0);
  }

  /**
   * Asserts that concatenating sources with the given lengths produces the correct combined
   * content.
   *
   * @param sourceLengths the lengths of the byte sources to create and concatenate
   */
  private void assertConcatContentIsCorrect(Integer... sourceLengths) throws IOException {
    // Arrange: Create a list of byte sources. The content of each source is a sequence of bytes
    // that continues numerically from where the previous source's content ended.
    List<ByteSource> sources = new ArrayList<>();
    int totalSize = 0;
    for (Integer length : sourceLengths) {
      sources.add(newByteSourceWithSequentialContent(totalSize, length));
      totalSize += length;
    }
    byte[] expectedBytes = newPreFilledByteArray(0, totalSize);

    // Act: Concatenate the sources and read the resulting content into a byte array.
    ByteSource concatenatedSource = ByteSource.concat(sources);
    byte[] actualBytes = concatenatedSource.read();

    // Assert: The concatenated content should be identical to the expected combined content.
    assertEquals(expectedBytes, actualBytes);
  }

  /**
   * Creates a {@link ByteSource} that provides a stream of bytes of a specified {@code size}, with
   * byte values starting from {@code start}.
   */
  private static ByteSource newByteSourceWithSequentialContent(int start, int size) {
    return new ByteSource() {
      @Override
      public InputStream openStream() {
        return new ByteArrayInputStream(newPreFilledByteArray(start, size));
      }

      @Override
      public String toString() {
        return "newByteSource(" + start + ", " + size + ")";
      }
    };
  }

  /**
   * Creates a {@link MultiInputStream} from a large number of empty sources. This is a helper for
   * performance or resource-related tests, unused by the concatenation tests in this file.
   */
  @SuppressWarnings("unused") // Used by other tests in the suite
  private static MultiInputStream tenMillionEmptySources() throws IOException {
    return new MultiInputStream(Collections.nCopies(10_000_000, ByteSource.empty()).iterator());
  }
}