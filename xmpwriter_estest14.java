package com.itextpdf.text.xml.xmp;

import com.itextpdf.xmp.XMPException;
import org.junit.Test;

import java.io.IOException;
import java.io.OutputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * This test class verifies the input validation and exception handling
 * of the {@link XmpWriter} class.
 */
public class XmpWriter_ESTestTest14 extends XmpWriter_ESTest_scaffolding {

    /**
     * Verifies that calling {@link XmpWriter#appendArrayItem(String, String, String)}
     * with a null schema namespace throws an XMPException.
     * <p>
     * The underlying XMP library requires a valid, non-empty schema namespace URI
     * to correctly structure the metadata. This test ensures that this constraint is enforced.
     * </p>
     */
    @Test
    public void appendArrayItem_withNullSchemaNamespace_throwsException() throws IOException {
        // Arrange
        // The OutputStream is not used when just manipulating the XMP data structure in memory.
        // Therefore, passing null is acceptable for this test's scope.
        XmpWriter xmpWriter = new XmpWriter((OutputStream) null);
        String nullSchemaNamespace = null;
        String anyArrayName = "dc:subject";
        String anyValue = "test";

        // Act & Assert
        try {
            // Attempt to append an array item with a null schema namespace, which is invalid.
            xmpWriter.appendArrayItem(nullSchemaNamespace, anyArrayName, anyValue);
            fail("Expected an XMPException to be thrown due to the null schema namespace, but no exception occurred.");
        } catch (XMPException e) {
            // Verify that the exception message is the one expected from the XMP library's validation.
            assertEquals("Empty schema namespace URI", e.getMessage());
        }
    }
}