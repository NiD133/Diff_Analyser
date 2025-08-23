package com.itextpdf.text.pdf;

import com.itextpdf.text.io.RandomAccessSource;
import com.itextpdf.text.io.RandomAccessSourceFactory;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * Tests the reading behavior of the {@link RandomAccessFileOrArray} class.
 */
public class RandomAccessFileOrArrayTest {

    /**
     * Verifies that reading a zero-length UTF string consumes the two-byte length prefix
     * and correctly positions the file pointer at the end of the stream. A subsequent
     * read should then indicate that the end of the stream has been reached.
     */
    @Test
    public void readUTF_withZeroLengthString_advancesPointerAndReachesEOF() throws IOException {
        // Arrange: Create a RandomAccessFileOrArray with data representing a zero-length
        // UTF string. In the modified UTF-8 format used by DataInput, this is encoded
        // as a two-byte length prefix of 0.
        byte[] inputData = new byte[]{0, 0};
        RandomAccessSource source = new RandomAccessSourceFactory().createSource(inputData);
        RandomAccessFileOrArray reader = new RandomAccessFileOrArray(source);

        // Act:
        // 1. Read the UTF string. This is expected to read the 2-byte length and return an empty string.
        String utfString = reader.readUTF();

        // 2. Attempt to read from the stream again. Since the pointer should be at the end,
        //    this read operation is expected to return -1.
        byte[] bufferForNextRead = new byte[2];
        int bytesRead = reader.read(bufferForNextRead);

        // Assert:
        // Verify that the correct (empty) string was read.
        assertEquals("The decoded UTF string should be empty.", "", utfString);

        // Verify that the file pointer has advanced by 2 bytes to the end of the stream.
        assertEquals("File pointer should be at position 2 after reading the UTF length prefix.", 2L, reader.getFilePointer());

        // Verify that the subsequent read attempt correctly indicated the end of the stream.
        assertEquals("Reading after reaching the end of the stream should return -1.", -1, bytesRead);
    }
}