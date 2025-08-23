package com.itextpdf.text.pdf;

import com.itextpdf.text.BaseColor;
import org.junit.Test;

import java.io.OutputStream;

import static org.junit.Assert.assertFalse;

/**
 * Note: The original test class was likely auto-generated for the PdfDictionary class.
 * This specific test case verifies the default state of a PdfShading object,
 * which is a specialized type of PdfDictionary (via its parent class, PdfStream).
 */
public class PdfDictionary_ESTestTest51 extends PdfDictionary_ESTest_scaffolding {

    /**
     * Tests that a PdfShading object created via the simpleRadial factory method
     * has the 'AntiAlias' property set to false by default.
     */
    @Test
    public void simpleRadialShading_shouldHaveAntiAliasDisabledByDefault() {
        // ARRANGE
        // A mock PdfWriter is needed for the factory method, but its internal state is not used.
        // FdfWriter.Wrt is a convenient, accessible subclass of PdfWriter for this purpose.
        FdfWriter.Wrt mockWriter = new FdfWriter.Wrt((OutputStream) null, (FdfWriter) null);

        // Define the geometric and color parameters for the radial shading.
        BaseColor color = BaseColor.MAGENTA;
        float startX = 512f;
        float startY = 1f;
        float startRadius = 32f;
        float endX = 8f;
        float endY = 8192f;
        float endRadius = 663.0f;

        // ACT
        // Create a simple radial shading object. This object contains a PdfDictionary
        // that stores its properties.
        PdfShading radialShading = PdfShading.simpleRadial(
                mockWriter,
                startX, startY, startRadius,
                endX, endY, endRadius,
                color, color);

        // ASSERT
        // Verify that the AntiAlias flag is false by default. This flag is a boolean
        // value stored within the underlying PdfDictionary.
        assertFalse("The AntiAlias property should be false by default for a new radial shading.",
                radialShading.isAntiAlias());
    }
}