package org.apache.commons.io.input;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringReader;

/**
 * Tests for {@link BoundedReader}.
 */
public class BoundedReaderTest {

    /**
     * Tests that read() returns the first character from the underlying reader
     * when the bound is set to one.
     */
    @Test
    public void testReadSingleCharWhenBoundIsOne() throws IOException {
        // Arrange
        final String testData = "v-rest-of-string";
        final StringReader underlyingReader = new StringReader(testData);
        
        // Create a BoundedReader limited to reading just one character.
        final BoundedReader boundedReader = new BoundedReader(underlyingReader, 1);

        // Act
        final int firstChar = boundedReader.read();

        // Assert
        // Verify that the character read is 'v', not its obscure integer value 118.
        assertEquals('v', firstChar);
    }
}