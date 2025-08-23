package com.itextpdf.text.xml.xmp;

import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import static org.junit.Assert.assertThrows;

/**
 * Contains tests for the {@link XmpWriter} class.
 */
public class XmpWriterTest {

    /**
     * Verifies that the serialize(OutputStream) method throws a NullPointerException
     * if the XmpWriter was constructed with a null OutputStream.
     *
     * This test case is based on an auto-generated test which identified that
     * providing a null stream to the constructor causes a subsequent call to
     * serialize() to fail, even when a valid stream is passed to serialize() itself.
     * This suggests an unexpected internal state dependency.
     */
    @Test
    public void serialize_whenConstructedWithNullOutputStream_throwsNullPointerException() throws IOException {
        // Arrange
        // Use arbitrary, non-standard encoding and padding values as found in the original test.
        String arbitraryEncoding = "3Y'G95KH";
        int arbitraryPadding = 10633;

        // Create an XmpWriter instance with a null OutputStream, which is the
        // specific condition under test.
        XmpWriter xmpWriter = new XmpWriter(null, arbitraryEncoding, arbitraryPadding);
        ByteArrayOutputStream validOutputStream = new ByteArrayOutputStream();

        // Act & Assert
        // The serialize method is expected to throw a NullPointerException because of
        // how the writer was initialized, despite being passed a valid output stream.
        assertThrows(NullPointerException.class, () -> {
            xmpWriter.serialize(validOutputStream);
        });
    }
}