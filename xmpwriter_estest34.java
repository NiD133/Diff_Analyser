package com.itextpdf.text.xml.xmp;

import org.junit.Test;

import java.io.IOException;
import java.io.OutputStream;

import static org.junit.Assert.assertNotNull;

/**
 * Contains tests for the {@link XmpWriter} constructor.
 */
public class XmpWriterConstructorTest {

    /**
     * Verifies that the XmpWriter constructor can be instantiated with a null
     * OutputStream and a negative padding value without throwing an exception.
     *
     * This test ensures the constructor is robust against these specific edge-case inputs.
     */
    @Test
    public void constructorShouldSucceedWithNullStreamAndNegativePadding() throws IOException {
        // Arrange: Define edge-case parameters for the constructor.
        OutputStream nullOutputStream = null;
        int negativePadding = -4;
        String encoding = "UTF-16BE";

        // Act: Attempt to create an XmpWriter instance with the specified parameters.
        // The test will fail if the constructor throws an unexpected exception.
        XmpWriter xmpWriter = new XmpWriter(nullOutputStream, encoding, negativePadding);

        // Assert: Confirm that the object was created successfully.
        assertNotNull("XmpWriter instance should be successfully created even with a null stream and negative padding.", xmpWriter);
    }
}