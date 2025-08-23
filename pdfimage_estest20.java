package com.itextpdf.text.pdf;

import com.itextpdf.text.BadPdfFormatException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PdfTemplate;
import org.junit.Test;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

/**
 * Test suite for the {@link PdfImage} class.
 */
public class PdfImageTest {

    /**
     * Verifies that the PdfImage constructor throws a NullPointerException
     * when initialized with an Image created from a PdfTemplate.
     * <p>
     * An Image created from a template lacks a URL or raw data. The PdfImage
     * constructor attempts to access one of these, resulting in an expected NPE.
     */
    @Test
    public void constructor_whenImageIsFromTemplate_throwsNullPointerException() {
        // Arrange: Create an Image from a template, which has no raw data or URL.
        PdfTemplate template = new PdfPSXObject();
        Image imageFromTemplate = Image.getInstance(template);

        // A dummy reference is needed for the constructor parameter.
        PdfIndirectReference dummyMaskReference = new PdfIndirectReference(0, 0);

        // Act & Assert
        try {
            new PdfImage(imageFromTemplate, "anyName", dummyMaskReference);
            fail("Expected a NullPointerException to be thrown, but no exception occurred.");
        } catch (NullPointerException expectedException) {
            // This is the expected outcome.
            // The exception has no message because it's triggered by dereferencing a null object.
            assertNull("The NullPointerException should not have a message.", expectedException.getMessage());
        } catch (BadPdfFormatException e) {
            fail("A BadPdfFormatException was thrown, but a NullPointerException was expected. Error: " + e.getMessage());
        }
    }
}