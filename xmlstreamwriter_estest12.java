package org.apache.commons.io.output;

import org.junit.Test;
import org.evosuite.runtime.mock.java.io.MockFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.UnsupportedCharsetException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for {@link XmlStreamWriter}.
 * This test case focuses on the constructor's behavior with invalid parameters.
 */
public class XmlStreamWriter_ESTestTest12 extends XmlStreamWriter_ESTest_scaffolding {

    /**
     * Tests that the XmlStreamWriter constructor throws an UnsupportedCharsetException
     * when an invalid or unsupported character encoding is provided.
     */
    @Test(timeout = 4000)
    public void constructorWithUnsupportedEncodingShouldThrowException() throws FileNotFoundException {
        // Arrange: Define a dummy file and an invalid encoding name.
        final File dummyFile = new MockFile("test.xml");
        final String unsupportedEncoding = "z";

        // Act & Assert: Attempt to create the writer and verify the correct exception is thrown.
        try {
            new XmlStreamWriter(dummyFile, unsupportedEncoding);
            fail("Expected an UnsupportedCharsetException because the encoding is invalid.");
        } catch (final UnsupportedCharsetException e) {
            // Verify that the exception message correctly identifies the invalid encoding.
            assertEquals(unsupportedEncoding, e.getMessage());
        }
    }
}