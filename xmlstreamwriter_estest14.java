package org.apache.commons.io.output;

import org.junit.Test;
import java.io.File;
import java.io.FileNotFoundException;

/**
 * Tests for {@link XmlStreamWriter}.
 * This test focuses on the constructor behavior with invalid arguments.
 */
public class XmlStreamWriter_ESTestTest14 { // Note: Scaffolding inheritance removed for a standalone, minimal example.

    /**
     * Tests that the {@link XmlStreamWriter#XmlStreamWriter(File, String)} constructor
     * throws a NullPointerException when the file argument is null.
     *
     * @throws FileNotFoundException because the constructor signature declares it.
     */
    @Test(expected = NullPointerException.class)
    public void constructorWithNullFileShouldThrowNullPointerException() throws FileNotFoundException {
        // Arrange: A null file object. The encoding is irrelevant for this test case.
        final String irrelevantEncoding = "UTF-8";

        // Act & Assert: Attempt to construct an XmlStreamWriter with a null file.
        // This should fail with a NullPointerException, as verified by the @Test annotation.
        new XmlStreamWriter((File) null, irrelevantEncoding);
    }
}