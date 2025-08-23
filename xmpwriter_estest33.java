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
     * <p>
     * This test ensures the constructor is robust and can handle potentially
     * invalid or unusual inputs gracefully during object initialization.
     */
    @Test
    public void constructor_withNullStreamAndNegativePadding_shouldCreateInstanceSuccessfully() throws IOException {
        // Arrange
        OutputStream nullOutputStream = null;
        String encoding = "UTF-16";
        int negativePadding = -1;

        // Act
        // Attempt to create an XmpWriter with a null stream and negative padding.
        XmpWriter xmpWriter = new XmpWriter(nullOutputStream, encoding, negativePadding);

        // Assert
        // The primary goal is to ensure no exception was thrown during construction.
        // Asserting non-nullity makes this success condition explicit.
        assertNotNull("The XmpWriter instance should be created successfully.", xmpWriter);
    }
}