package com.google.common.io;

import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;

/**
 * Unit tests for {@link MultiInputStream}.
 */
public class MultiInputStreamTest {

    /**
     * Verifies that the constructor throws a NullPointerException if the provided
     * iterator contains a null ByteSource. The constructor attempts to advance to the
     * first stream immediately, which will fail when it encounters the null element.
     */
    @Test(expected = NullPointerException.class)
    public void constructor_whenIteratorContainsNullByteSource_throwsNullPointerException() throws IOException {
        // ARRANGE: Create an iterator that contains a single null ByteSource.
        Iterator<ByteSource> sources = Arrays.asList((ByteSource) null).iterator();

        // ACT & ASSERT: Constructing a MultiInputStream with this iterator should
        // immediately throw a NullPointerException.
        new MultiInputStream(sources);
    }
}