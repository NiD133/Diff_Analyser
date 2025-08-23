package org.apache.commons.io.input;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import org.junit.Test;

/**
 * Tests for the {@link ProxyReader} class, using {@link CloseShieldReader} as a
 * concrete implementation for testing the proxying behavior.
 */
public class ProxyReaderTest {

    /**
     * Tests that the read() method correctly delegates to the underlying reader
     * and returns the integer value of the first character.
     */
    @Test
    public void readShouldReturnFirstCharacterAsInt() throws IOException {
        // Arrange
        final String inputString = "$^pT";
        final Reader underlyingReader = new StringReader(inputString);
        // Use CloseShieldReader as a concrete subclass of the abstract ProxyReader
        final Reader proxyReader = CloseShieldReader.wrap(underlyingReader);

        // Act
        final int firstCharRead = proxyReader.read();

        // Assert
        assertEquals("The first character should be read correctly.", (int) '$', firstCharRead);
    }
}