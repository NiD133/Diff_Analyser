package com.itextpdf.text.xml.xmp;

import com.itextpdf.xmp.XMPException;
import org.junit.Test;

import java.io.IOException;
import java.io.OutputStream;

/**
 * This test verifies the behavior of the {@link XmpWriter#addDocInfoProperty(Object, String)} method.
 */
public class XmpWriterTest {

    /**
     * Tests that setting the "Producer" document information property
     * executes successfully without throwing an exception.
     * <p>
     * This ensures that the method correctly handles one of the standard
     * document information dictionary keys.
     * </p>
     */
    @Test
    public void addDocInfoProperty_withProducerKey_shouldNotThrowException() throws XMPException, IOException {
        // Arrange: Create an XmpWriter instance.
        // A null OutputStream is acceptable here because the test does not serialize the XMP data.
        // The encoding ("Producer") and extraSpace (-1) values are unusual but retained from the
        // original auto-generated test case.
        XmpWriter xmpWriter = new XmpWriter((OutputStream) null, "Producer", -1);
        String producerKey = "Producer";
        String producerValue = "My Custom PDF Producer";

        // Act: Call the method under test to set the "Producer" property.
        xmpWriter.addDocInfoProperty(producerKey, producerValue);

        // Assert: The test passes if the 'Act' phase completes without throwing an exception.
        // No explicit assertion is needed for this type of test.
    }
}