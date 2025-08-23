package org.apache.commons.io.input;

import static org.junit.Assert.assertThrows;

import java.io.IOException;
import java.io.Reader;
import java.util.Collections;
import org.junit.Test;

/**
 * Tests for the {@link SequenceReader} class, focusing on edge cases for the read method.
 */
public class SequenceReaderTest {

    /**
     * Verifies that the read(buffer, offset, length) method throws an
     * IndexOutOfBoundsException when a negative offset is provided,
     * which is consistent with the contract of {@link java.io.Reader}.
     */
    @Test
    public void testReadWithNegativeOffsetThrowsIndexOutOfBoundsException() {
        // Arrange: Create a SequenceReader. Its internal state (e.g., being empty)
        // is not relevant for this specific boundary check.
        final SequenceReader sequenceReader = new SequenceReader(Collections.<Reader>emptyList());
        final char[] buffer = new char[10];
        final int negativeOffset = -1;
        final int validLength = 5;

        // Act & Assert: Expect an IndexOutOfBoundsException when calling read with the invalid offset.
        // Using assertThrows is a modern and clear way to test for expected exceptions.
        assertThrows(IndexOutOfBoundsException.class, () -> {
            sequenceReader.read(buffer, negativeOffset, validLength);
        });
    }

    /**
     * Below is an alternative implementation using the @Test(expected) attribute,
     * which is common in older JUnit 4 codebases.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithNegativeOffsetThrowsIndexOutOfBoundsException_alternative() throws IOException {
        // Arrange
        final SequenceReader sequenceReader = new SequenceReader(Collections.<Reader>emptyList());
        final char[] buffer = new char[10];
        final int negativeOffset = -1;
        final int validLength = 5;

        // Act: This call is expected to throw the exception.
        sequenceReader.read(buffer, negativeOffset, validLength);
    }
}