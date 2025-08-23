package com.google.common.io;

import com.google.common.io.ByteSource;
import com.google.common.io.MultiInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import org.junit.Test;

/**
 * Tests for {@link MultiInputStream}, focusing on specific behavioral scenarios.
 */
public class MultiInputStreamTest {

    /**
     * Verifies that if the underlying collection for the iterator is modified after the
     * MultiInputStream is created, a subsequent stream operation that advances to the
     * next source will fail with a ConcurrentModificationException.
     */
    @Test(expected = ConcurrentModificationException.class)
    public void streamFailsWhenBackingIteratorSourceIsModified() throws IOException {
        // Arrange: Create a MultiInputStream from an iterator backed by a list.
        // The list contains two empty sources. The constructor will advance to the first one.
        List<ByteSource> byteSources = new ArrayList<>();
        byteSources.add(ByteSource.empty());
        byteSources.add(ByteSource.empty());

        Iterator<ByteSource> iterator = byteSources.iterator();
        MultiInputStream multiInputStream = new MultiInputStream(iterator);

        // Act: Modify the backing list after the iterator has been created.
        // This invalidates the iterator, causing it to fail on the next operation.
        byteSources.add(ByteSource.empty());

        // Assert: Skipping on an empty stream forces it to advance to the next source.
        // This triggers a call to the invalid iterator's next() method, which throws
        // the expected ConcurrentModificationException. The exception is verified by the
        // @Test(expected=...) annotation.
        multiInputStream.skip(1L);
    }
}