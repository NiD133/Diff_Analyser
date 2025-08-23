package com.google.common.io;

import static org.junit.Assert.fail;

import com.google.common.io.ByteSource;
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
     * Verifies that the MultiInputStream fails with a ConcurrentModificationException
     * if the underlying collection that provides the iterator is modified after the
     * stream has been created. This demonstrates the stream's adherence to the
     * fail-fast behavior of Java's collection iterators.
     */
    @Test
    public void read_whenBackingCollectionIsModified_throwsConcurrentModificationException() throws IOException {
        // Arrange: Create a MultiInputStream from an iterator over a list of ByteSources.
        List<ByteSource> byteSources = new ArrayList<>();
        byteSources.add(ByteSource.empty());

        Iterator<ByteSource> iterator = byteSources.iterator();
        // The MultiInputStream constructor calls iterator.next() to open the first stream.
        MultiInputStream multiStream = new MultiInputStream(iterator);

        // Act: Modify the underlying list *after* the iterator has been created and used.
        // This invalidates the iterator's state for any subsequent operations.
        byteSources.add(ByteSource.empty());

        // Assert: Expect a ConcurrentModificationException when trying to read from the stream.
        // Reading from the initial empty source returns -1, causing MultiInputStream to advance
        // to the next source. The subsequent call to iterator.hasNext() will then fail.
        try {
            multiStream.read();
            fail("Expected ConcurrentModificationException was not thrown.");
        } catch (ConcurrentModificationException expected) {
            // Test passes: the expected exception was caught.
        }
    }
}