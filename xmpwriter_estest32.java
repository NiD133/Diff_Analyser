package com.itextpdf.text.xml.xmp;

import org.junit.Test;

import java.io.IOException;
import java.io.OutputStream;

import static org.junit.Assert.assertNotNull;

/**
 * Contains tests for the {@link XmpWriter} constructor.
 */
// The original test class name and inheritance are preserved as per the request.
// In a real-world scenario, this class would likely be named XmpWriterTest.
public class XmpWriter_ESTestTest32 extends XmpWriter_ESTest_scaffolding {

    /**
     * Verifies that the XmpWriter constructor does not throw an exception when provided
     * with a null output stream and a negative padding value. This tests the constructor's
     * robustness with potentially invalid inputs.
     */
    @Test(timeout = 4000)
    public void constructorShouldSucceedWithNullStreamAndNegativePadding() throws IOException {
        // Arrange
        OutputStream nullOutputStream = null;
        String encoding = "UTF-16LE";
        int negativePadding = -1;

        // Act
        XmpWriter xmpWriter = new XmpWriter(nullOutputStream, encoding, negativePadding);

        // Assert
        // The primary test is that the constructor call above does not throw an exception.
        // This explicit assertion confirms that the object was created successfully.
        assertNotNull("The XmpWriter instance should not be null.", xmpWriter);
    }
}