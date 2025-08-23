package com.itextpdf.text.xml.xmp;

import com.itextpdf.xmp.XMPException;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertTrue;

/**
 * Test suite for the {@link XmpWriter} class.
 */
public class XmpWriterTest {

    /**
     * Verifies that appendOrderedArrayItem correctly adds an item to an ordered array
     * and that the item is present in the final serialized XMP output.
     */
    @Test
    public void appendOrderedArrayItem_shouldAddItemToSerializedXmp() throws IOException, XMPException {
        // Arrange
        // 1. Define clear, named constants for test data instead of using magic strings.
        final String testNamespace = "http://ns.itextpdf.com/test/";
        final String arrayName = "Authors";
        final String itemValue = "John Doe";

        // 2. Use ByteArrayOutputStream for in-memory testing. It's self-contained,
        // reliable, and allows easy inspection of the output.
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        XmpWriter xmpWriter = new XmpWriter(outputStream, XmpWriter.UTF8, 2000);

        // Act
        // 3. Call the method under test to add an item to an ordered array.
        xmpWriter.appendOrderedArrayItem(testNamespace, arrayName, itemValue);

        // 4. Close the writer to ensure all XMP data is flushed to the output stream.
        xmpWriter.close();

        // Assert
        // 5. Verify the outcome by checking the content of the output stream.
        String xmpOutput = outputStream.toString(StandardCharsets.UTF_8.name());
        String expectedRdfListItem = "<rdf:li>" + itemValue + "</rdf:li>";

        assertTrue("The XMP output should contain the array name.", xmpOutput.contains(arrayName));
        assertTrue("The XMP output should contain the added ordered array item as an rdf:li element.", xmpOutput.contains(expectedRdfListItem));
    }
}