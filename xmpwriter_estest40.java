package com.itextpdf.text.xml.xmp;

import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Contains unit tests for the {@link XmpWriter} class.
 */
public class XmpWriterTest {

    /**
     * Verifies that the setReadOnly() method can be called without causing an exception.
     * This type of test is useful for ensuring basic method stability.
     */
    @Test
    public void setReadOnly_shouldExecuteWithoutThrowingException() throws IOException {
        // Arrange: Create an instance of XmpWriter.
        // A ByteArrayOutputStream is a simple, in-memory stream suitable for testing.
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        XmpWriter xmpWriter = new XmpWriter(outputStream);

        // Act: Call the method under test.
        xmpWriter.setReadOnly();

        // Assert: The test passes if the 'Act' phase completes without throwing an exception.
        // No explicit assertion is needed for this verification.
    }
}