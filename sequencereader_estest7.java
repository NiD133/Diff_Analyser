package org.apache.commons.io.input;

import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.util.ConcurrentModificationException;
import java.util.Vector;

/**
 * This test suite contains tests for the {@link SequenceReader} class.
 * This particular test was improved for clarity and adherence to modern testing practices.
 */
public class SequenceReader_ESTestTest7 extends SequenceReader_ESTest_scaffolding {

    /**
     * Tests that modifying the underlying reader collection after the SequenceReader
     * is created causes a ConcurrentModificationException when close() is called.
     *
     * This is because the SequenceReader holds an iterator to the collection,
     * which becomes invalid if the collection is structurally modified.
     *
     * @throws IOException Not expected to be thrown in this test.
     */
    @Test(expected = ConcurrentModificationException.class)
    public void closeShouldThrowConcurrentModificationExceptionWhenReaderCollectionIsModified() throws IOException {
        // Arrange: Create a SequenceReader with a modifiable collection of readers.
        Vector<StringReader> readers = new Vector<>();
        SequenceReader sequenceReader = new SequenceReader(readers);

        // Act: Modify the underlying collection *after* the SequenceReader's internal
        // iterator has been created.
        readers.add(new StringReader("new data"));

        // Assert: Calling close() attempts to use the now-invalidated iterator,
        // which is expected to throw a ConcurrentModificationException.
        sequenceReader.close();
    }
}