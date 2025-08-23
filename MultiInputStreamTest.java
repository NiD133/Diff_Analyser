package com.google.common.io;

import java.io.ByteArrayInputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.jspecify.annotations.NullUnmarked;

/**
 * Test class for {@link MultiInputStream}.
 * This class contains unit tests to verify the functionality of the MultiInputStream class.
 */
@NullUnmarked
public class MultiInputStreamTest extends IoTestCase {

  /**
   * Tests the joining of multiple byte sources into a single stream.
   */
  public void testJoin() throws Exception {
    // Test cases with different span configurations
    verifyJoin(0);
    verifyJoin(1);
    verifyJoin(0, 0, 0);
    verifyJoin(10, 20);
    verifyJoin(10, 0, 20);
    verifyJoin(0, 10, 20);
    verifyJoin(10, 20, 0);
    verifyJoin(10, 20, 1);
    verifyJoin(1, 1, 1, 1, 1, 1, 1, 1);
    verifyJoin(1, 0, 1, 0, 1, 0, 1, 0);
  }

  /**
   * Tests that only one input stream is open at a time.
   */
  public void testOnlyOneOpen() throws Exception {
    ByteSource source = createByteSource(0, 50);
    int[] openStreamCounter = new int[1];

    ByteSource singleOpenChecker = new ByteSource() {
      @Override
      public InputStream openStream() throws IOException {
        if (openStreamCounter[0]++ != 0) {
          throw new IllegalStateException("More than one source open");
        }
        return new FilterInputStream(source.openStream()) {
          @Override
          public void close() throws IOException {
            super.close();
            openStreamCounter[0]--;
          }
        };
      }
    };

    byte[] result = ByteSource.concat(singleOpenChecker, singleOpenChecker, singleOpenChecker).read();
    assertEquals(150, result.length);
  }

  /**
   * Helper method to verify the joining of byte sources.
   */
  private void verifyJoin(Integer... spans) throws Exception {
    List<ByteSource> sources = new ArrayList<>();
    int totalSize = 0;

    for (Integer span : spans) {
      sources.add(createByteSource(totalSize, span));
      totalSize += span;
    }

    ByteSource joinedSource = ByteSource.concat(sources);
    assertTrue(createByteSource(0, totalSize).contentEquals(joinedSource));
  }

  /**
   * Tests reading a single byte from a concatenated byte source.
   */
  public void testReadSingleByte() throws Exception {
    ByteSource source = createByteSource(0, 10);
    ByteSource concatenatedSource = ByteSource.concat(source, source);

    assertEquals(20, concatenatedSource.size());

    InputStream inputStream = concatenatedSource.openStream();
    assertFalse(inputStream.markSupported());
    assertEquals(10, inputStream.available());

    int bytesRead = 0;
    while (inputStream.read() != -1) {
      bytesRead++;
    }

    assertEquals(0, inputStream.available());
    assertEquals(20, bytesRead);
  }

  /**
   * Tests the skip functionality of the MultiInputStream.
   */
  @SuppressWarnings("CheckReturnValue") // these calls to skip always return 0
  public void testSkip() throws Exception {
    MultiInputStream multiInputStream = new MultiInputStream(
        Collections.singleton(createByteSourceWithSkip(0, 50)).iterator()
    );

    assertEquals(0, multiInputStream.skip(-1));
    assertEquals(0, multiInputStream.skip(-1));
    assertEquals(0, multiInputStream.skip(0));

    ByteStreams.skipFully(multiInputStream, 20);
    assertEquals(20, multiInputStream.read());
  }

  /**
   * Tests that reading from a large number of empty sources does not cause a stack overflow.
   */
  public void testReadSingle_noStackOverflow() throws IOException {
    // Verifies no StackOverflowException occurs with a large number of empty sources
    assertEquals(-1, createMultiInputStreamWithEmptySources().read());
  }

  /**
   * Tests that reading an array from a large number of empty sources does not cause a stack overflow.
   */
  public void testReadArray_noStackOverflow() throws IOException {
    // Verifies no StackOverflowException occurs with a large number of empty sources
    assertEquals(-1, createMultiInputStreamWithEmptySources().read(new byte[1]));
  }

  /**
   * Creates a MultiInputStream with ten million empty sources.
   */
  private static MultiInputStream createMultiInputStreamWithEmptySources() throws IOException {
    return new MultiInputStream(Collections.nCopies(10_000_000, ByteSource.empty()).iterator());
  }

  /**
   * Creates a ByteSource with a specified start and size.
   */
  private static ByteSource createByteSource(int start, int size) {
    return new ByteSource() {
      @Override
      public InputStream openStream() {
        return new ByteArrayInputStream(newPreFilledByteArray(start, size));
      }
    };
  }

  /**
   * Creates a ByteSource with a specified start and size that overrides the skip method.
   */
  private static ByteSource createByteSourceWithSkip(int start, int size) {
    return new ByteSource() {
      @Override
      public InputStream openStream() {
        return new ByteArrayInputStream(newPreFilledByteArray(start, size)) {
          @Override
          public long skip(long n) {
            return 0;
          }
        };
      }
    };
  }
}