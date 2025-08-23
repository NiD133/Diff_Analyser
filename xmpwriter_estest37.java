package com.itextpdf.text.xml.xmp;

import com.itextpdf.xmp.XMPException;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Test suite for the {@link XmpWriter} class, focusing on property validation.
 */
public class XmpWriterPropertyTest {

    /**
     * Verifies that calling setProperty() with a null schema namespace URI
     * throws an XMPException.
     *
     * The schema namespace is a mandatory parameter for identifying a property,
     * and providing a null value should be rejected.
     */
    @Test
    public void setPropertyWithNullSchemaNamespaceShouldThrowException() throws IOException {
        // Arrange: Create an XmpWriter instance. A ByteArrayOutputStream is a
        // suitable dummy stream for this test's purpose.
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        XmpWriter xmpWriter = new XmpWriter(outputStream);

        String nullSchemaNamespace = null;
        String propertyName = "dc:title";
        String propertyValue = "My Document Title";

        // Act & Assert: Call setProperty with a null namespace and verify that
        // an XMPException is thrown. The modern assertThrows is used for clarity.
        XMPException thrownException = assertThrows(
                XMPException.class,
                () -> xmpWriter.setProperty(nullSchemaNamespace, propertyName, propertyValue)
        );

        // Further assert on the exception message to ensure it's the expected error.
        assertEquals("Empty schema namespace URI", thrownException.getMessage());
    }
}