package com.itextpdf.text.xml.xmp;

import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.xmp.XMPConst;
import com.itextpdf.xmp.XMPException;
import com.itextpdf.xmp.XMPMeta;
import com.itextpdf.xmp.properties.XMPProperty; // Note: For DublinCoreProperties.TITLE constant
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;

import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link XmpWriter} class, focusing on document info properties.
 */
public class XmpWriterTest {

    /**
     * Verifies that calling addDocInfoProperty with a PdfName key (e.g., PdfName.TITLE)
     * correctly sets the corresponding property in the underlying XMP metadata.
     */
    @Test
    public void addDocInfoProperty_withPdfNameKey_shouldSetCorrespondingXmpProperty() throws IOException, XMPException {
        // Arrange: Create an XmpWriter with a valid output stream and no initial info.
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        XmpWriter xmpWriter = new XmpWriter(outputStream, Collections.emptyMap());

        PdfName titleKey = PdfName.TITLE;
        String expectedTitleValue = "My Document Title";

        // Act: Add the 'Title' property using its PdfName key.
        xmpWriter.addDocInfoProperty(titleKey, expectedTitleValue);

        // Assert: Retrieve the XMP metadata and verify that the Dublin Core title property
        // was set to the expected value.
        XMPMeta xmpMeta = xmpWriter.getXmpMeta();
        String actualTitleValue = xmpMeta.getPropertyString(XMPConst.NS_DC, "title");

        assertEquals("The document title should be correctly set in the XMP metadata.",
                expectedTitleValue, actualTitleValue);
    }
}