package com.itextpdf.text.xml.xmp;

import com.itextpdf.xmp.XMPConst;
import com.itextpdf.xmp.XMPException;
import com.itextpdf.xmp.XMPMeta;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Contains tests for the {@link XmpWriter} class, focusing on array manipulation.
 */
public class XmpWriterTest {

    /**
     * Verifies that appendArrayItem successfully adds a new item to an unordered array
     * in the XMP metadata.
     */
    @Test
    public void appendArrayItem_shouldAddItemToUnorderedArray() throws IOException, XMPException {
        // Arrange: Set up the XmpWriter and define the data to be added.
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        XmpWriter xmpWriter = new XmpWriter(outputStream, new HashMap<>());

        final String pdfNamespace = XMPConst.NS_PDF;
        final String arrayPropertyName = "Keywords";
        final String keywordValue = "Test Keyword";

        // Act: Append an item to an array property.
        xmpWriter.appendArrayItem(pdfNamespace, arrayPropertyName, keywordValue);

        // Assert: Verify that the item was added correctly to the underlying XMP metadata.
        XMPMeta xmpMeta = xmpWriter.getXmpMeta();

        // Verify that the property exists and is an array.
        assertTrue("The array property 'Keywords' should exist.",
                xmpMeta.doesPropertyExist(pdfNamespace, arrayPropertyName));
        assertTrue("The 'Keywords' property should be an array.",
                xmpMeta.getProperty(pdfNamespace, arrayPropertyName).getOptions().isArray());

        // Verify the array has exactly one item.
        int itemCount = xmpMeta.countArrayItems(pdfNamespace, arrayPropertyName);
        assertEquals("The array should contain one item after appending.", 1, itemCount);

        // Verify the content of the appended item (XMP arrays are 1-indexed).
        String retrievedValue = xmpMeta.getArrayItem(pdfNamespace, arrayPropertyName, 1).getValue();
        assertEquals("The appended item's value is incorrect.", keywordValue, retrievedValue);
    }
}