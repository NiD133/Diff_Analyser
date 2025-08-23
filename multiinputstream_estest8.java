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
 * Tests for {@link MultiInputStream}.
 */
public class MultiInputStreamTest {

    /**
     * Verifies that the constructor throws a ConcurrentModificationException if the underlying
     * collection for the iterator is modified after the iterator is created. This is the expected
     * behavior for iterators that are not fail-safe.
     */
    @Test(expected = ConcurrentModificationException.class)
    public void constructor_whenIteratorIsModified_throwsConcurrentModificationException() throws IOException {
        // Arrange: Create a list of ByteSources and get an iterator for it.
        List<ByteSource> byteSources = new ArrayList<>();
        byteSources.add(ByteSource.empty());
        Iterator<ByteSource> iterator = byteSources.iterator();

        // Act: Modify the underlying list after creating the iterator. This invalidates it.
        byteSources.add(ByteSource.empty());

        // The MultiInputStream constructor attempts to advance the iterator (by calling hasNext()),
        // which will trigger the exception because the iterator has been invalidated.
        new MultiInputStream(iterator);

        // Assert: The test succeeds if the expected ConcurrentModificationException is thrown.
    }
}