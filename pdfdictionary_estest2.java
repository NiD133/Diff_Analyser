package com.itextpdf.text.pdf;

import org.junit.Test;
import java.io.IOException;
import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the PdfDictionary class, focusing on serialization behavior.
 */
public class PdfDictionary_ESTestTest2 extends PdfDictionary_ESTest_scaffolding {

    /**
     * Tests that the toPdf() method correctly serializes a dictionary
     * containing a PdfName value whose type has been manually modified.
     *
     * This edge case is triggered by setting the 'type' field of a PdfName object
     * to INDIRECT, which tests how the serialization logic handles an object
     * that claims to be an indirect reference but is not an instance of
     * PdfIndirectReference.
     */
    @Test(timeout = 4000)
    public void toPdf_withPdfNameValueModifiedAsIndirect_serializesToCorrectString() throws IOException {
        // Arrange
        // The expected PDF output for a dictionary with one key-value pair: /Sect -> /Sect
        // <<      (start dictionary)
        // /Sect   (key)
        // /Sect   (value)
        // >>      (end dictionary)
        // The expected string is "<</Sect /Sect>>".
        String expectedPdfOutput = "<</Sect /Sect>>";

        PdfName name = PdfName.SECT;
        PdfDictionary dictionary = new PdfDictionary();
        dictionary.put(name, name);

        // This is the core of the test's scenario: we manually modify the internal
        // type of the PdfName *value* to simulate an indirect object reference.
        // This is done to test a specific path within the toPdf serialization logic.
        name.type = PdfObject.INDIRECT; // The integer value for INDIRECT is 8.

        ByteBuffer outputBuffer = new ByteBuffer();

        // Act
        // The PdfWriter parameter is not used in this specific implementation of toPdf,
        // so passing null is acceptable for this test.
        dictionary.toPdf(null, outputBuffer);

        // Assert
        assertEquals(expectedPdfOutput, outputBuffer.toString());
    }
}