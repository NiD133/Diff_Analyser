package org.apache.commons.io.input;

import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.List;

/**
 * Tests for {@link SequenceReader}.
 */
public class SequenceReaderTest {

    /**
     * Tests that closing a SequenceReader succeeds even if the underlying collection
     * contains the same Reader instance multiple times.
     * <p>
     * According to the {@link java.io.Reader#close()} contract, closing an already-closed
     * stream has no effect. This test ensures that SequenceReader correctly handles
     * this scenario without throwing an exception.
     * </p>
     */
    @Test
    public void closeShouldSucceedWhenCollectionContainsDuplicateReaderInstances() throws IOException {
        // Arrange: Create a list containing the same reader instance twice.
        StringReader sharedReader = new StringReader("test data");
        List<StringReader> readersWithDuplicates = Arrays.asList(sharedReader, sharedReader);

        SequenceReader sequenceReader = new SequenceReader(readersWithDuplicates);

        // Act & Assert: The close() method should execute without throwing an exception.
        // The test will pass if no exception is thrown, verifying idempotent behavior.
        sequenceReader.close();
    }
}