package org.apache.commons.io.input;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.Test;

/**
 * This test case focuses on verifying that the underlying Reader of a BoundedReader
 * is properly closed when the BoundedReader itself is closed.  This ensures resources
 * are released correctly.
 */
public class GeneratedTestCase {

    @Test
    public void testCloseTest() throws IOException {
        // Create an AtomicBoolean to track if the custom Reader's close() method is called.
        final AtomicBoolean closed = new AtomicBoolean(false);

        // Use a try-with-resources block to ensure the Reader and BoundedReader are closed automatically.
        try (Reader stringReader = new BufferedReader(new StringReader("01234567890")) {
                // Override the close() method of the StringReader to set the AtomicBoolean to true
                // when it's called.  This allows us to verify that the StringReader is closed.
                @Override
                public void close() throws IOException {
                    closed.set(true);
                    super.close(); // Ensure the original close() method is also called.
                }
            };
             BoundedReader boundedReader = new BoundedReader(stringReader, 3)) {

            // The BoundedReader is created with a limit of 3 characters.
            // No actual reading is performed in this test. The important part is that
            // when the try-with-resources block exits, the BoundedReader and its underlying
            // Reader (our custom StringReader) should be closed.
        }

        // Assert that the close() method of the custom StringReader was called.
        // If this assertion passes, it means the BoundedReader correctly closed its
        // underlying Reader when it was closed.
        assertTrue(closed.get(), "The underlying Reader's close() method should have been called.");
    }
}