package com.itextpdf.text.xml.xmp;

import com.itextpdf.xmp.XMPException;
import com.itextpdf.xmp.XMPMeta;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * Contains unit tests for the {@link XmpWriter} class.
 */
public class XmpWriterTest {

    /**
     * Tests that the setAbout method correctly updates the 'rdf:about' attribute
     * in the underlying XMP metadata object.
     */
    @Test
    public void setAbout_shouldUpdateRdfAboutAttributeInXmpMetadata() throws IOException, XMPException {
        // Arrange: Create an XmpWriter and define the expected 'about' attribute.
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        XmpWriter xmpWriter = new XmpWriter(outputStream);
        String expectedAboutAttribute = "uuid:a2d81f83-4841-428a-8b13-39b1252a3c1f";

        // Act: Call the method under test.
        xmpWriter.setAbout(expectedAboutAttribute);

        // Assert: Verify that the XMP metadata object was updated correctly.
        XMPMeta actualXmpMeta = xmpWriter.getXmpMeta();
        assertEquals("The 'rdf:about' attribute should be set to the provided value.",
                expectedAboutAttribute, actualXmpMeta.getObjectName());
    }
}