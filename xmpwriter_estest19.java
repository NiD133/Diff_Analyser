package com.itextpdf.text.xml.xmp;

import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.xmp.XMPConst;
import com.itextpdf.xmp.XMPException;
import com.itextpdf.xmp.XMPMeta;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test suite for the {@link XmpWriter} class.
 */
public class XmpWriterTest {

    /**
     * Verifies that calling addDocInfoProperty with the key 'CreationDate'
     * correctly sets the corresponding 'xmp:CreateDate' property in the XMP metadata.
     */
    @Test
    public void addDocInfoProperty_withCreationDateKey_setsXmpCreateDateProperty() throws IOException, XMPException {
        // Arrange
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        XmpWriter xmpWriter = new XmpWriter(outputStream);
        
        PdfName propertyKey = PdfName.CREATIONDATE;
        String propertyValue = "2023-10-27T10:00:00Z"; // A more representative value

        // Act
        xmpWriter.addDocInfoProperty(propertyKey, propertyValue);

        // Assert
        XMPMeta xmpMeta = xmpWriter.getXmpMeta();
        String actualValue = xmpMeta.getPropertyString(XMPConst.NS_XMP, "CreateDate");

        assertNotNull("The 'CreateDate' property should not be null.", actualValue);
        assertEquals("The 'CreateDate' property should match the provided value.", propertyValue, actualValue);
    }
}