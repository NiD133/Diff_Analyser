package com.itextpdf.text.xml.xmp;

import com.itextpdf.xmp.XMPConst;
import com.itextpdf.xmp.XMPException;
import com.itextpdf.xmp.XMPMeta;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Tests for the {@link XmpWriter} constructor that accepts a Map of document information.
 */
public class XmpWriterTest {

    /**
     * Verifies that the XmpWriter constructor correctly handles entries with null values
     * in the provided info map by simply ignoring them.
     */
    @Test
    public void constructorWithInfoMap_shouldIgnoreEntriesWithNullValues() throws IOException, XMPException {
        // Arrange: Create a document info map containing one valid entry and one entry with a null value.
        Map<String, String> documentInfo = new HashMap<>();
        documentInfo.put("Author", "Test Author"); // This property should be added.
        documentInfo.put("Subject", null);         // This property should be ignored.

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // Act: Instantiate the XmpWriter with the map.
        XmpWriter xmpWriter = new XmpWriter(outputStream, documentInfo);

        // Assert: Verify that the XMP metadata was created correctly.
        XMPMeta xmpMeta = xmpWriter.getXmpMeta();

        // 1. Check that the valid "Author" property was processed and added as "dc:creator".
        String creator = xmpMeta.getArrayItem(XMPConst.NS_DC, "creator", 1).getValue();
        assertEquals("Test Author", creator);

        // 2. Check that the "Subject" property, which had a null value, was ignored and not added as "dc:description".
        boolean subjectExists = xmpMeta.doesPropertyExist(XMPConst.NS_DC, "description");
        assertFalse("The 'description' property should not exist for a null 'Subject' value.", subjectExists);
    }
}