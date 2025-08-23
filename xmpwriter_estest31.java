package com.itextpdf.text.xml.xmp;

import com.itextpdf.text.pdf.PdfDictionary;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static org.junit.Assert.assertNotNull;

/**
 * Test suite for the {@link XmpWriter} class.
 */
public class XmpWriterTest {

    /**
     * Verifies that the XmpWriter can be constructed successfully with a null
     * PdfDictionary, as it should gracefully handle this input without
     * throwing a NullPointerException.
     */
    @Test
    public void constructorWithNullPdfDictionaryShouldCreateInstance() throws IOException {
        // Arrange: Create a dummy output stream and a null PdfDictionary.
        OutputStream dummyOutputStream = new ByteArrayOutputStream();
        PdfDictionary nullInfoDictionary = null;

        // Act: Attempt to create an XmpWriter instance with the null dictionary.
        XmpWriter xmpWriter = new XmpWriter(dummyOutputStream, nullInfoDictionary);

        // Assert: The constructor should complete successfully, and the resulting object should not be null.
        assertNotNull("XmpWriter instance should be successfully created even with a null info dictionary.", xmpWriter);
    }
}