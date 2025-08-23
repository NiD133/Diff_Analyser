package org.apache.commons.io.input;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

// The test class name is kept for context, but in a real-world scenario,
// it would be renamed to something like BoundedReaderTest.
public class BoundedReader_ESTestTest21 extends BoundedReader_ESTest_scaffolding {

    /**
     * Tests that the skip() method stops at the end of the underlying reader's content,
     * even if the requested number of characters to skip is larger.
     */
    @Test
    public void skipShouldBeLimitedByUnderlyingReaderContent() throws IOException {
        // Arrange
        final String inputData = "wa"; // Underlying reader has only 2 characters.
        final int readerBound = 10;    // The BoundedReader's limit is larger than the content.
        final long requestedSkipCount = 10L;

        final Reader underlyingReader = new StringReader(inputData);
        final BoundedReader boundedReader = new BoundedReader(underlyingReader, readerBound);

        // Act
        final long actualSkippedCount = boundedReader.skip(requestedSkipCount);

        // Assert
        // The number of skipped characters should equal the total characters available in the
        // underlying reader, as it's less than the requested skip count and the reader's bound.
        final long expectedSkippedCount = inputData.length();
        assertEquals(expectedSkippedCount, actualSkippedCount);
    }
}