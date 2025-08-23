package com.google.common.io;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

import java.io.IOException;

/**
 * Tests for {@link CharSequenceReader}.
 */
public class CharSequenceReaderTest {

    /**
     * This test was refactored from an auto-generated EvoSuite test.
     * The original test verified that after reading all characters from the sequence,
     * a subsequent read would indicate the end of the stream.
     *
     * The refactored version improves upon the original by:
     * 1. Using a descriptive test method name.
     * 2. Using meaningful variable names and human-readable test data ("abcdef").
     * 3. Structuring the test logic clearly with the Arrange-Act-Assert pattern.
     * 4. Correcting a flaw where the test tried to check for end-of-stream by reading
     *    zero bytes, which is not the correct way to trigger the -1 return value.
     * 5. Adding an assertion to verify the content read, making the test more robust.
     */
    @Test
    public void read_whenStreamIsExhausted_returnsNegativeOne() throws IOException {
        // Arrange
        String sourceText = "abcdef";
        CharSequenceReader reader = new CharSequenceReader(sourceText);
        char[] readBuffer = new char[sourceText.length()];

        // Act & Assert: First, read the entire sequence to exhaust the reader.
        int charsRead = reader.read(readBuffer);
        assertEquals("Should read all characters from the sequence", sourceText.length(), charsRead);
        assertArrayEquals("Buffer should contain the exact characters from the source",
                sourceText.toCharArray(), readBuffer);

        // Act & Assert: Now, attempt to read again from the exhausted reader.
        int endOfStreamResult = reader.read(readBuffer);
        assertEquals("A subsequent read should return -1 to signal the end of the stream",
                -1, endOfStreamResult);
    }
}