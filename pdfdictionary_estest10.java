package com.itextpdf.text.pdf;

import org.junit.Test;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Contains an improved test for the PdfDictionary class, focusing on the basic
 * functionality of adding and retrieving objects.
 */
public class PdfDictionary_ESTestTest10 extends PdfDictionary_ESTest_scaffolding {

    /**
     * Verifies that an object retrieved from a PdfDictionary using get()
     * is the same instance that was previously added using putEx().
     *
     * This test uses a PdfName object, which is a type that can be stored
     * in a PDF Object Stream. The test also confirms this property on the
     * retrieved object, preserving the original test's intent.
     */
    @Test
    public void getAfterPutExShouldReturnSameObjectInstance() {
        // Arrange
        // A PdfDictionary stores key-value pairs. PdfResources is a concrete subclass
        // used here, matching the original test's setup.
        PdfDictionary dictionary = new PdfResources();
        PdfName keyAndValue = PdfWriter.PDF_VERSION_1_6;

        // Act
        dictionary.putEx(keyAndValue, keyAndValue);
        PdfObject retrievedObject = dictionary.get(keyAndValue);

        // Assert
        // 1. The retrieved object must be the exact same instance as the one put in.
        // This is a more direct and robust check than the original assertion.
        assertSame("The object retrieved from the dictionary should be the same instance that was added.",
                keyAndValue, retrievedObject);

        // 2. The object should have the expected properties. A PdfName can be in an object stream.
        // This assertion is preserved from the original test to maintain its specific check.
        assertTrue("A PdfName object should be classifiable as being allowed in an object stream.",
                retrievedObject.canBeInObjStm());
    }
}