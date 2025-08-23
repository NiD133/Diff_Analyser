package com.itextpdf.text.xml.xmp;

import com.itextpdf.xmp.XMPException;
import com.itextpdf.xmp.XMPMeta;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;

import static org.junit.Assert.assertEquals;

/**
 * Test suite for the {@link XmpWriter} class.
 */
public class XmpWriterTest {

    /**
     * Verifies that calling setProperty() correctly adds a new property
     * to the internal XMP metadata object.
     */
    @Test
    public void setPropertyShouldAddPropertyToXmpMetadata() throws IOException, XMPException {
        // Arrange: Set up the XmpWriter and define the property to be added.
        // A ByteArrayOutputStream is used as a lightweight, in-memory stream.
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        
        // The original test used a complex method to create a map.
        // For this test's purpose, an empty map is sufficient and much clearer.
        XmpWriter xmpWriter = new XmpWriter(outputStream, Collections.emptyMap());

        final String schemaNamespace = "http://ns.adobe.com/pdf/1.3/";
        final String propertyName = "CustomProducer";
        final String expectedValue = "My Custom Application";

        // Act: Call the method under test.
        xmpWriter.setProperty(schemaNamespace, propertyName, expectedValue);

        // Assert: Verify that the property was correctly set in the XMP metadata.
        XMPMeta internalXmpMeta = xmpWriter.getXmpMeta();
        String actualValue = internalXmpMeta.getPropertyString(schemaNamespace, propertyName);

        assertEquals("The property value should be correctly set in the XMP metadata.", expectedValue, actualValue);
    }
}