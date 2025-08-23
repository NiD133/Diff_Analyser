package com.itextpdf.text.xml.xmp;

import com.itextpdf.xmp.XMPException;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static org.junit.Assert.assertTrue;

/**
 * Contains tests for the {@link XmpWriter} class.
 */
public class XmpWriterTest {

    /**
     * Verifies that the serialize method writes default XMP metadata to a given
     * output stream without throwing an exception.
     */
    @Test
    public void serialize_withValidOutputStream_writesDataSuccessfully() throws IOException, XMPException {
        // Arrange: Create an XmpWriter. The constructor's OutputStream is not used by the
        // serialize(OutputStream) method being tested, so passing null is acceptable here.
        XmpWriter xmpWriter = new XmpWriter((OutputStream) null);
        ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();

        // Act: Serialize the default XMP metadata to our byte stream.
        xmpWriter.serialize(byteOutputStream);

        // Assert: Verify that some data was actually written to the stream.
        // This is a stronger check than just ensuring no exception was thrown.
        assertTrue("The output stream should not be empty after serialization.", byteOutputStream.size() > 0);
    }
}