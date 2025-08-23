package org.apache.commons.io.input;

import static org.junit.jupiter.api.Assertions.assertFalse;

import java.io.IOException;
import java.io.Reader;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link SequenceReader}.
 */
class SequenceReaderTest {

    /**
     * Tests that SequenceReader correctly reports that it does not support the mark/reset functionality.
     * The default implementation in java.io.Reader returns false, and SequenceReader does not override this behavior.
     */
    @Test
    void markSupportedShouldReturnFalse() throws IOException {
        // The behavior of markSupported() is a property of the class,
        // so testing with an empty SequenceReader is sufficient.
        try (Reader reader = new SequenceReader()) {
            assertFalse(reader.markSupported(), "SequenceReader should not support mark/reset operations.");
        }
    }
}