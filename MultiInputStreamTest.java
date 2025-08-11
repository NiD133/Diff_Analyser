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
 * Test class for {@link MultiInputStream}.
 *
 * @author Chris Nokleberg
 */
@NullUnmarked
public class MultiInputStreamTest extends IoTestCase {

  public void testConcatenatesMultipleStreamsCorrectly() throws Exception {
    // Test various combinations of stream sizes to ensure proper concatenation
    testConcatenationWithStreamSizes(0);
    testConcatenationWithStreamSizes(1);
    testConcatenationWithStreamSizes(0, 0, 0);
    testConcatenationWithStreamSizes(10, 20);
    testConcatenationWithStreamSizes(10, 0, 20);
    testConcatenationWithStreamSizes(0, 10, 20);
    testConcatenationWithStreamSizes(10, 20, 0);
    testConcatenationWithStreamSizes(10, 20, 1);
    testConcatenationWithStreamSizes(1, 1, 1, 1, 1, 1, 1, 1);
    testConcatenationWithStreamSizes(1, 0, 1, 0, 1, 0, 1, 0);
  }

  public void testOnlyOneStreamIsOpenAtATime() throws Exception {
    // Create a source that tracks how many streams are open simultaneously
    ByteSource originalSource = createByteSourceWithContent(0, 50);
    int[] openStreamCounter = new int[1];
    
    ByteSource streamTrackingSource = new ByteSource() {
      @Override
      public InputStream openStream() throws IOException {
        if (openStreamCounter[0]++ != 0) {
          throw new IllegalStateException("More than one source open simultaneously");
        }
        return new FilterInputStream(originalSource.openStream()) {
          @Override
          public void close() throws IOException {
            super.close();
            openStreamCounter[0]--;
          }
        };
      }
    };
    
    // Concatenate multiple instances of the tracking source
    byte[] result = ByteSource.concat(streamTrackingSource, streamTrackingSource, streamTrackingSource).read();
    
    // Verify the total length is correct (3 sources × 50 bytes each)
    assertEquals(150, result.length);
  }

  /**
   * Tests that streams are properly concatenated by comparing the result
   * with an equivalent single stream containing the same data.
   */
  private void testConcatenationWithStreamSizes(Integer... streamSizes) throws Exception {
    List<ByteSource> sourcesToConcatenate = new ArrayList<>();
    int currentStartByte = 0;
    
    // Create individual byte sources with sequential content
    for (Integer streamSize : streamSizes) {
      sourcesToConcatenate.add(createByteSourceWithContent(currentStartByte, streamSize));
      currentStartByte += streamSize;
    }
    
    // Concatenate all sources
    ByteSource concatenatedSource = ByteSource.concat(sourcesToConcatenate);
    
    // Create expected result: a single source with all the data
    ByteSource expectedSource = createByteSourceWithContent(0, currentStartByte);
    
    // Verify the concatenated result matches the expected content
    assertTrue("Concatenated stream should match expected content", 
               expectedSource.contentEquals(concatenatedSource));
  }

  public void testReadingSingleBytesWorksCorrectly() throws Exception {
    ByteSource singleSource = createByteSourceWithContent(0, 10);
    ByteSource concatenatedSource = ByteSource.concat(singleSource, singleSource);
    
    // Verify total size
    assertEquals("Concatenated source should have combined size", 20, concatenatedSource.size());
    
    try (InputStream inputStream = concatenatedSource.openStream()) {
      // MultiInputStream should not support mark/reset
      assertFalse("MultiInputStream should not support mark", inputStream.markSupported());
      
      // Initially should show available bytes from first stream
      assertEquals("Should show available bytes from first stream", 10, inputStream.available());
      
      // Read all bytes one by one
      int totalBytesRead = 0;
      while (inputStream.read() != -1) {
        totalBytesRead++;
      }
      
      // Verify we read all bytes and stream is exhausted
      assertEquals("Should have no bytes available after reading all", 0, inputStream.available());
      assertEquals("Should have read all bytes from both streams", 20, totalBytesRead);
    }
  }

  @SuppressWarnings("CheckReturnValue") // skip() calls intentionally return 0 in this test
  public void testSkipBehaviorWithNonSkippableStream() throws Exception {
    // Create a MultiInputStream with a source that doesn't support skipping
    MultiInputStream multiStream = new MultiInputStream(
        Collections.singleton(createNonSkippableByteSource()).iterator());
    
    // Test skip with negative values (should return 0)
    assertEquals("Skip with negative value should return 0", 0, multiStream.skip(-1));
    assertEquals("Skip with negative value should return 0", 0, multiStream.skip(-1));
    
    // Test skip with zero (should return 0)
    assertEquals("Skip with zero should return 0", 0, multiStream.skip(0));
    
    // Use ByteStreams.skipFully to skip 20 bytes (this will read and discard)
    ByteStreams.skipFully(multiStream, 20);
    
    // Verify we're now at the 21st byte (value 20, since we start from 0)
    assertEquals("After skipping 20 bytes, should read byte with value 20", 20, multiStream.read());
  }

  public void testReadSingleByteWithManyEmptyStreams_noStackOverflow() throws IOException {
    // Regression test for https://github.com/google/guava/issues/2996
    // Ensures that having many empty streams doesn't cause StackOverflowException
    MultiInputStream streamWithManyEmptySources = createStreamWithManyEmptySources();
    
    // Should return -1 (end of stream) without throwing StackOverflowException
    assertEquals("Stream with only empty sources should return -1", 
                 -1, streamWithManyEmptySources.read());
  }

  public void testReadByteArrayWithManyEmptyStreams_noStackOverflow() throws IOException {
    // Regression test for https://github.com/google/guava/issues/2996
    // Ensures that having many empty streams doesn't cause StackOverflowException
    MultiInputStream streamWithManyEmptySources = createStreamWithManyEmptySources();
    
    // Should return -1 (end of stream) without throwing StackOverflowException
    assertEquals("Stream with only empty sources should return -1", 
                 -1, streamWithManyEmptySources.read(new byte[1]));
  }

  /**
   * Creates a MultiInputStream with 10 million empty sources to test stack overflow scenarios.
   */
  private static MultiInputStream createStreamWithManyEmptySources() throws IOException {
    return new MultiInputStream(Collections.nCopies(10_000_000, ByteSource.empty()).iterator());
  }

  /**
   * Creates a ByteSource that contains sequential byte values starting from the given start value.
   */
  private static ByteSource createByteSourceWithContent(int startValue, int size) {
    return new ByteSource() {
      @Override
      public InputStream openStream() {
        return new ByteArrayInputStream(newPreFilledByteArray(startValue, size));
      }
    };
  }

  /**
   * Creates a ByteSource that wraps a stream which always returns 0 from skip() calls.
   */
  private static ByteSource createNonSkippableByteSource() {
    return new ByteSource() {
      @Override
      public InputStream openStream() {
        return new ByteArrayInputStream(newPreFilledByteArray(0, 50)) {
          @Override
          public long skip(long n) {
            return 0; // Simulate a stream that doesn't support skipping
          }
        };
      }
    };
  }
}