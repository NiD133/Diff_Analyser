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
import java.util.List;
import org.jspecify.annotations.NullUnmarked;

/**
 * Tests for {@link MultiInputStream}.
 *
 * These tests focus on:
 * - Correct concatenation behavior for various source sizes and orders
 * - Guarantee that only one substream is open at a time
 * - Reading semantics (single-byte, array, available, mark support)
 * - Skipping behavior when underlying streams don't support skipping well
 * - Robustness against very large numbers of empty sources (no stack overflow)
 */
@NullUnmarked
public class MultiInputStreamTest extends IoTestCase {

  /**
   * Verifies that concatenating sources produces the same content as a single sequential source,
   * across a variety of segment sizes and orders (including zeros).
   */
  public void testConcatenation_producesSequentialBytes() throws Exception {
    assertConcatenatedContentMatches(); // No segments -> empty
    assertConcatenatedContentMatches(1);
    assertConcatenatedContentMatches(0, 0, 0);
    assertConcatenatedContentMatches(10, 20);
    assertConcatenatedContentMatches(10, 0, 20);
    assertConcatenatedContentMatches(0, 10, 20);
    assertConcatenatedContentMatches(10, 20, 0);
    assertConcatenatedContentMatches(10, 20, 1);
    assertConcatenatedContentMatches(1, 1, 1, 1, 1, 1, 1, 1);
    assertConcatenatedContentMatches(1, 0, 1, 0, 1, 0, 1, 0);
  }

  /**
   * Asserts that at most one substream is open at any time while reading a concatenated stream.
   */
  public void testConcatenation_opensAtMostOneSubstreamAtATime() throws Exception {
    ByteSource base = byteSourceWithSequentialBytes(0, 50);

    final int[] openStreamCount = new int[1];
    ByteSource countingSource =
        new ByteSource() {
          @Override
          public InputStream openStream() throws IOException {
            if (openStreamCount[0]++ != 0) {
              throw new IllegalStateException("More than one source open at the same time");
            }
            return new FilterInputStream(base.openStream()) {
              @Override
              public void close() throws IOException {
                try {
                  super.close();
                } finally {
                  openStreamCount[0]--;
                }
              }
            };
          }
        };

    byte[] result = ByteSource.concat(countingSource, countingSource, countingSource).read();
    assertEquals(150, result.length);
  }

  /**
   * Verifies that single-byte reads iterate across substreams correctly and expose expected
   * InputStream properties.
   */
  public void testSingleByteRead_overMultipleSources() throws Exception {
    ByteSource source = byteSourceWithSequentialBytes(0, 10);
    ByteSource concatenated = ByteSource.concat(source, source);

    assertEquals(20, concatenated.size());

    try (InputStream in = concatenated.openStream()) {
      assertFalse(in.markSupported()); // MultiInputStream does not support mark/reset
      assertEquals(10, in.available()); // from the first substream

      int count = 0;
      while (in.read() != -1) {
        count++;
      }

      assertEquals(0, in.available());
      assertEquals(20, count);
    }
  }

  /**
   * Verifies skip behavior when the underlying stream's skip method always returns 0.
   * - skip with negative or zero should return 0
   * - ByteStreams.skipFully should still advance using reads
   */
  @SuppressWarnings("CheckReturnValue") // these calls to skip always return 0 by design
  public void testSkip_handlesNonSkippingUnderlyingStream() throws Exception {
    ByteSource nonSkippingSource =
        new ByteSource() {
          @Override
          public InputStream openStream() {
            return new ByteArrayInputStream(sequentialBytes(0, 50)) {
              @Override
              public long skip(long n) {
                return 0; // simulate an InputStream that doesn't support skipping
              }
            };
          }
        };

    try (MultiInputStream multi =
        new MultiInputStream(Collections.singleton(nonSkippingSource).iterator())) {

      assertEquals(0, multi.skip(-1));
      assertEquals(0, multi.skip(-1));
      assertEquals(0, multi.skip(0));

      ByteStreams.skipFully(multi, 20);
      assertEquals(20, multi.read()); // now positioned at byte value 20
    }
  }

  /**
   * Regression test for https://github.com/google/guava/issues/2996.
   * Ensures single-byte read does not cause a StackOverflowError with many empty sources.
   */
  public void testReadSingle_noStackOverflow() throws IOException {
    assertEquals(-1, tenMillionEmptySources().read());
  }

  /**
   * Regression test for https://github.com/google/guava/issues/2996.
   * Ensures array read does not cause a StackOverflowError with many empty sources.
   */
  public void testReadArray_noStackOverflow() throws IOException {
    assertEquals(-1, tenMillionEmptySources().read(new byte[1]));
  }

  // --- Helpers -------------------------------------------------------------------------------

  /**
   * Concatenates a set of ByteSources whose contents are sequential byte ranges, then verifies that
   * the concatenated content matches a single sequential source of the combined length.
   */
  private void assertConcatenatedContentMatches(Integer... spans) throws Exception {
    List<ByteSource> sources = new ArrayList<>();
    int start = 0;
    for (int span : spans) {
      sources.add(byteSourceWithSequentialBytes(start, span));
      start += span;
    }
    ByteSource concatenated = ByteSource.concat(sources);
    assertTrue(byteSourceWithSequentialBytes(0, start).contentEquals(concatenated));
  }

  private static MultiInputStream tenMillionEmptySources() throws IOException {
    return new MultiInputStream(Collections.nCopies(10_000_000, ByteSource.empty()).iterator());
  }

  /**
   * Returns a ByteSource whose content is a sequence of bytes starting at 'start' of length 'size'.
   * This uses IoTestCase.newPreFilledByteArray to produce predictable content.
   */
  private static ByteSource byteSourceWithSequentialBytes(int start, int size) {
    return new ByteSource() {
      @Override
      public InputStream openStream() {
        return new ByteArrayInputStream(sequentialBytes(start, size));
      }
    };
  }

  private static byte[] sequentialBytes(int start, int size) {
    return newPreFilledByteArray(start, size);
  }
}