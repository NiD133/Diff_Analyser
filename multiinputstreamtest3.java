package com.google.common.io;

import static com.google.common.truth.Truth.assertThat;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.jspecify.annotations.NullUnmarked;

/**
 * Tests for {@link MultiInputStream}, primarily focusing on its behavior when
 * used via {@link ByteSource#concat(Iterable)}.
 */
@NullUnmarked
public class MultiInputStreamTestTest3 extends IoTestCase {

    /**
     * Verifies that concatenating several {@link ByteSource} instances produces a source
     * with the correct combined content.
     */
    private void assertConcatenatedSourcesHaveCorrectContent(Integer... sourceSizes) throws Exception {
        List<ByteSource> sources = new ArrayList<>();
        int totalSize = 0;
        for (Integer size : sourceSizes) {
            sources.add(newPreFilledByteSource(totalSize, size));
            totalSize += size;
        }

        ByteSource concatenatedSource = ByteSource.concat(sources);
        ByteSource expectedSource = newPreFilledByteSource(0, totalSize);

        assertThat(concatenatedSource.contentEquals(expectedSource)).isTrue();
    }

    /**
     * Creates a {@link MultiInputStream} from a very large number of empty sources,
     * useful for performance or resource-related tests.
     */
    private static MultiInputStream streamOfTenMillionEmptySources() throws IOException {
        int tenMillion = 10_000_000;
        return new MultiInputStream(Collections.nCopies(tenMillion, ByteSource.empty()).iterator());
    }

    /** Creates a {@link ByteSource} containing a predictable sequence of bytes. */
    private static ByteSource newPreFilledByteSource(int start, int size) {
        return new ByteSource() {
            @Override
            public InputStream openStream() {
                return new ByteArrayInputStream(newPreFilledByteArray(start, size));
            }
        };
    }

    /**
     * Tests that an InputStream from a concatenated ByteSource reads across the
     * boundary of the underlying streams and that `available()` behaves as expected.
     */
    public void testConcatenatedStream_readsAcrossBoundariesAndReportsAvailableCorrectly() throws Exception {
        // Arrange
        final int sourceSize = 10;
        final int totalSize = sourceSize * 2;
        ByteSource singleSource = newPreFilledByteSource(0, sourceSize);

        // Concatenate the source with itself to create a source with two underlying streams.
        ByteSource concatenatedSource = ByteSource.concat(singleSource, singleSource);
        assertThat(concatenatedSource.size()).isEqualTo(totalSize);

        // Act: Open the stream, which will be a MultiInputStream
        InputStream stream = concatenatedSource.openStream();

        // Assert: Check initial state of the stream
        assertThat(stream.markSupported()).isFalse();
        // available() should only report the size of the *first* stream, not the total size.
        // This is the specified behavior for MultiInputStream.
        assertThat(stream.available()).isEqualTo(sourceSize);

        // Act: Read all bytes from the stream until it's exhausted.
        int bytesReadCount = 0;
        while (stream.read() != -1) {
            bytesReadCount++;
        }

        // Assert: Check the final state of the stream
        assertThat(bytesReadCount).isEqualTo(totalSize);
        assertThat(stream.available()).isEqualTo(0);
    }
}