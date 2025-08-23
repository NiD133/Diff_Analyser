package com.itextpdf.text.xml.xmp;

import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.xmp.XMPConst;
import com.itextpdf.xmp.XMPException;
import com.itextpdf.xmp.XMPMeta;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link XmpWriter} class, focusing on document information properties.
 */
public class XmpWriterTest {

    /**
     * Verifies that calling addDocInfoProperty with the 'Title' key correctly sets
     * the corresponding Dublin Core 'title' property in the XMP metadata.
     */
    @Test
    public void addDocInfoProperty_withTitleKey_shouldSetDublinCoreTitle() throws IOException, XMPException {
        // Arrange: Create an XmpWriter and define the property to be added.
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        XmpWriter xmpWriter = new XmpWriter(outputStream);
        PdfName titleKey = PdfName.TITLE;
        String expectedTitle = "The Adventures of iText";

        // Act: Add the document title property.
        xmpWriter.addDocInfoProperty(titleKey, expectedTitle);

        // Assert: Verify that the correct XMP property was set.
        XMPMeta xmpMeta = xmpWriter.getXmpMeta();
        String actualTitle = xmpMeta.getPropertyString(XMPConst.NS_DC, "title");
        
        assertEquals("The XMP title property should match the provided value.", expectedTitle, actualTitle);
    }
}