package org.apache.commons.io.input;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import org.junit.Test;

/**
 * Unit tests for the {@link ProxyReader} class.
 */
public class ProxyReaderTest {

    /**
     * Tests that the skip() method throws an IllegalArgumentException when called with a negative
     * value, which is the expected behavior inherited from {@link java.io.Reader}.
     */
    @Test
    public void skip_shouldThrowIllegalArgumentException_whenArgumentIsNegative() {
        // Arrange: Since ProxyReader is abstract, we use a concrete subclass (TaggedReader)
        // for testing. The underlying reader's content is irrelevant for this test.
        final Reader proxyReader = new TaggedReader(new StringReader("test-data"));
        final long negativeSkipValue = -1L;

        // Act & Assert
        try {
            proxyReader.skip(negativeSkipValue);
            fail("Expected an IllegalArgumentException to be thrown for a negative skip value.");
        } catch (final IllegalArgumentException e) {
            // This is the expected outcome.
            // We verify the message to ensure it's the specific exception we expect from Reader.skip().
            assertEquals("skip value is negative", e.getMessage());
        } catch (final IOException e) {
            // An IOException is a possible but unexpected failure path for this test case.
            fail("An unexpected IOException was thrown: " + e.getMessage());
        }
    }
}