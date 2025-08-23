package com.itextpdf.text.pdf;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import java.io.IOException;

/**
 * Contains tests for the {@link RandomAccessFileOrArray} class, focusing on specific edge cases.
 */
public class RandomAccessFileOrArrayTest {

    /**
     * Verifies that readLine() returns an empty string if a carriage return ('\r')
     * is pushed back before the read operation.
     *
     * The readLine() method should treat the pushed-back line terminator as the end of an
     * empty line and stop reading immediately.
     */
    @Test
    public void readLine_whenCarriageReturnIsPushedBack_returnsEmptyString() throws IOException {
        // Arrange: Create a RandomAccessFileOrArray instance. The initial data is irrelevant
        // as the pushed-back character will be read first.
        byte[] irrelevantData = new byte[0];
        RandomAccessFileOrArray fileOrArray = new RandomAccessFileOrArray(irrelevantData);

        // Push back a carriage return character, which acts as a line terminator.
        fileOrArray.pushBack((byte) '\r');

        // Act: Read the next line from the source.
        String result = fileOrArray.readLine();

        // Assert: The line should be empty because the first character encountered was a line terminator.
        assertEquals("Expected an empty string when a line terminator is pushed back.", "", result);
    }
}