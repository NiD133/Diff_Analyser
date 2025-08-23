package com.itextpdf.text.xml.xmp;

import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.xmp.XMPConst;
import com.itextpdf.xmp.XMPException;
import com.itextpdf.xmp.XMPMeta;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

/**
 * Contains tests for the {@link XmpWriter} class.
 */
public class XmpWriterTest {

    /**
     * Verifies that calling addDocInfoProperty with the 'Keywords' key and an empty string
     * does not add a 'dc:subject' property to the XMP metadata.
     *
     * The underlying implementation splits the keywords string by commas and only adds
     * non-empty values. An empty input string should therefore result in no properties being added.
     */
    @Test
    public void addDocInfoPropertyWithEmptyKeywordsShouldNotCreateSubjectProperty() throws IOException, XMPException {
        // Arrange: Create an XmpWriter and define the input property.
        // We use a ByteArrayOutputStream as a valid, non-null stream.
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        XmpWriter xmpWriter = new XmpWriter(outputStream);
        
        PdfName keywordsKey = PdfName.KEYWORDS;
        String emptyKeywordsValue = "";

        // Act: Add the 'Keywords' property with an empty string value.
        xmpWriter.addDocInfoProperty(keywordsKey, emptyKeywordsValue);

        // Assert: Verify that no corresponding 'dc:subject' property was created in the XMP metadata.
        XMPMeta xmpMeta = xmpWriter.getXmpMeta();
        assertNotNull("The XMPMeta object should not be null.", xmpMeta);

        boolean subjectPropertyExists = xmpMeta.doesPropertyExist(XMPConst.NS_DC, "subject");
        assertFalse("The 'dc:subject' property should not be created for an empty Keywords value.", subjectPropertyExists);
    }
}