package com.itextpdf.text.xml.xmp;

import com.itextpdf.xmp.XMPException;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Unit tests for the {@link XmpWriter} class.
 */
public class XmpWriterTest {

    /**
     * Verifies that attempting to append an item to an alternate array using an
     * unregistered schema namespace URI correctly throws an XMPException.
     */
    @Test
    public void appendAlternateArrayItem_withUnregisteredSchema_shouldThrowException() throws IOException {
        // Arrange: Create an XmpWriter and define test data with an invalid namespace.
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        XmpWriter xmpWriter = new XmpWriter(outputStream);

        String unregisteredSchemaNS = "http://my.invalid.schema/ns/";
        String arrayName = "MyAlternateArray";
        String itemValue = "An example value";

        // Act & Assert: Expect an XMPException when the method is called.
        try {
            xmpWriter.appendAlternateArrayItem(unregisteredSchemaNS, arrayName, itemValue);
            fail("Expected an XMPException to be thrown due to the unregistered schema namespace.");
        } catch (XMPException e) {
            // Verify that the exception message is the one expected from the underlying XMP library.
            assertEquals("Unregistered schema namespace URI", e.getMessage());
        }
    }
}