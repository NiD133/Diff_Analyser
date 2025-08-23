package org.apache.commons.io.input;

import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.ConcurrentModificationException;

// The original test class and its inheritance are preserved.
public class SequenceReader_ESTestTest5 extends SequenceReader_ESTest_scaffolding {

    /**
     * Tests that a {@link ConcurrentModificationException} is thrown when reading
     * from a {@link SequenceReader} after its underlying collection of readers
     * has been modified.
     * <p>
     * The {@link SequenceReader} obtains an iterator from the collection upon
     * construction. Most standard Java collections provide "fail-fast" iterators,
     * which throw this exception if the collection is structurally modified
     * after the iterator is created. This test verifies that behavior.
     * </p>
     * @throws IOException if an I/O error occurs, though not expected in this test.
     */
    @Test(timeout = 4000, expected = ConcurrentModificationException.class)
    public void readThrowsConcurrentModificationExceptionWhenCollectionIsModified() throws IOException {
        // Arrange: Create a collection and add a couple of readers to it.
        final Collection<StringReader> readers = new ArrayDeque<>();
        // The content of the readers is irrelevant for this test.
        readers.add(new StringReader(""));
        readers.add(new StringReader(""));

        // The SequenceReader is initialized, which creates its internal iterator.
        final SequenceReader sequenceReader = new SequenceReader(readers);

        // Act: Modify the underlying collection *after* the SequenceReader was created.
        // This invalidates the internal iterator.
        readers.add(new StringReader(""));

        // Assert: The next call to read() is expected to trigger the fail-fast
        // iterator and throw a ConcurrentModificationException. This is handled
        // by the @Test(expected=...) annotation.
        sequenceReader.read();
    }
}