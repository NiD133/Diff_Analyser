package org.apache.commons.io.output;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.nio.charset.IllegalCharsetNameException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/**
 * Tests for {@link XmlStreamWriter} to ensure it handles invalid constructor arguments correctly.
 */
public class XmlStreamWriterTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    /**
     * Tests that the XmlStreamWriter constructor throws an IllegalCharsetNameException
     * when provided with an syntactically invalid encoding name.
     */
    @Test
    public void constructorWithInvalidEncodingNameShouldThrowException() throws IOException {
        // Arrange: Create a temporary file and define an invalid charset name.
        final File testFile = temporaryFolder.newFile("test.xml");
        final String invalidEncodingName = "O`7";

        // Act & Assert: Attempt to create the writer and verify the correct exception is thrown.
        try {
            new XmlStreamWriter(testFile, invalidEncodingName);
            fail("Expected an IllegalCharsetNameException to be thrown for an invalid encoding name.");
        } catch (final IllegalCharsetNameException e) {
            // Verify that the exception message correctly identifies the invalid encoding.
            assertEquals("The exception message should contain the invalid encoding name.",
                    invalidEncodingName, e.getMessage());
        }
    }
}