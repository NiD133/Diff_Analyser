package com.google.common.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import org.junit.Test;

/**
 * This class contains improved tests for {@link CharSequenceReader}.
 * The original test was auto-generated and has been rewritten for clarity.
 */
public class CharSequenceReader_ESTestTest21 {

    /**
     * Verifies that the mark() method rejects negative arguments, as specified by its contract.
     */
    @Test
    public void mark_throwsIllegalArgumentException_forNegativeReadAheadLimit() throws IOException {
        // Arrange: Create a reader and define an invalid input value.
        // The actual content of the sequence is not relevant for this test.
        CharSequenceReader reader = new CharSequenceReader("any-sequence");
        int invalidReadAheadLimit = -1;

        // Act & Assert: Verify that calling mark() with a negative value throws an exception.
        try {
            reader.mark(invalidReadAheadLimit);
            fail("Expected mark() to throw IllegalArgumentException for a negative readAheadLimit.");
        } catch (IllegalArgumentException expected) {
            // The implementation uses Guava's Preconditions, which generates a specific message.
            // Verifying this message ensures the correct validation is being triggered.
            assertEquals("readAheadLimit may not be negative", expected.getMessage());
        }
    }
}