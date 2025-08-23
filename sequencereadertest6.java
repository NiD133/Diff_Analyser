package org.apache.commons.io.input;

import static org.apache.commons.io.IOUtils.EOF;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link SequenceReader}.
 */
class SequenceReaderTest {

    @Test
    void testReadIntoCharArray_readsSequentiallyAcrossReaders() throws IOException {
        // Arrange: Create a SequenceReader from two StringReaders.
        // The combined content is "FooBar".
        try (Reader sequenceReader = new SequenceReader(new StringReader("Foo"), new StringReader("Bar"))) {

            // Act & Assert: First read, consuming part of the first reader ("Foo").
            final char[] buffer1 = new char[2];
            final int charsRead1 = sequenceReader.read(buffer1);
            assertEquals(2, charsRead1, "Should read 2 chars");
            assertArrayEquals(new char[]{'F', 'o'}, buffer1, "First 2 chars should be 'Fo'");

            // Act & Assert: Second read, consuming the rest of the first reader and part of the second.
            // This is the key test to ensure it reads across the boundary.
            final char[] buffer2 = new char[3];
            final int charsRead2 = sequenceReader.read(buffer2);
            assertEquals(3, charsRead2, "Should read 3 chars across the boundary");
            assertArrayEquals(new char[]{'o', 'B', 'a'}, buffer2, "Next 3 chars should be 'oBa'");

            // Act & Assert: Third read, consuming the rest of the second reader ("Bar").
            // This is a partial read, as the buffer is larger than the remaining content.
            final char[] buffer3 = new char[3];
            final int charsRead3 = sequenceReader.read(buffer3);
            assertEquals(1, charsRead3, "Should read the final char");
            
            // To verify a partial read, compare only the portion of the buffer that was filled.
            final char[] expectedPartial = {'r'};
            final char[] actualPartial = Arrays.copyOf(buffer3, charsRead3);
            assertArrayEquals(expectedPartial, actualPartial, "The last char should be 'r'");

            // Act & Assert: Attempt to read again at the end of the stream.
            assertEquals(EOF, sequenceReader.read(new char[3]), "Should be at the end of the stream");
        }
    }
}