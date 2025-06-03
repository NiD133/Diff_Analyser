package org.apache.commons.io.input;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTimeout;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;
import java.io.StringReader;
import java.time.Duration;

public class LineNumberReaderStringEolTest {

    // Define a timeout to prevent tests from running indefinitely
    private static final Duration TIMEOUT = Duration.ofSeconds(5);

    // Define a sample string that ends with a newline character (\n)
    private static final String STRING_WITH_TRAILING_NEWLINE = "This is a test string.\n";

    @Test
    public void testLineNumberReaderHandlesTrailingNewlineCorrectly() throws IOException {
        //GIVEN
        // Create a StringReader from the sample string.  This allows us to treat the string
        // as an input stream of characters that the LineNumberReader can read.
        StringReader stringReader = new StringReader(STRING_WITH_TRAILING_NEWLINE);

        //WHEN
        // Wrap the StringReader in a LineNumberReader.  LineNumberReader keeps track of the
        // current line number as it reads from the underlying reader.
        LineNumberReader lineNumberReader = new LineNumberReader(stringReader);

        //Read the first line
        String line1 = lineNumberReader.readLine();

        //Read the second line which should be null
        String line2 = lineNumberReader.readLine();

        //THEN
        // Assert that reading the first line works
        assertEquals("This is a test string.", line1, "The first line should be read correctly without the newline character.");

        //Assert that the next read is null because we read all of the line
        assertEquals(null, line2, "There should be nothing more to read");

        // Assert that the line number is 1 after reading the line
        assertEquals(1, lineNumberReader.getLineNumber(), "The line number should be 1 after reading one line.");

        // Close the reader to release resources (important!)
        lineNumberReader.close();
    }
}