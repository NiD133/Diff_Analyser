package com.itextpdf.text.pdf.parser;

import com.itextpdf.awt.geom.AffineTransform;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Tests the ability of LocationTextExtractionStrategy to correctly extract text
 * from a rotated XObject, preserving the reading order of the surrounding content.
 */
public class LocationTextExtractionStrategyRotatedXObjectTest {

    /**
     * This test verifies that text within a rotated XObject (a reusable content block,
     * often used for images or templates) is extracted in the correct order relative
     * to other text on the page.
     *
     * The PDF is constructed with:
     * 1. Paragraph "A"
     * 2. Paragraph "B"
     * 3. An XObject containing the text "X", which is internally rotated.
     * 4. Paragraph "C"
     *
     * The strategy should correctly process the transformations and produce the text in
     * the logical reading order: "A", "B", "X", "C".
     */
    @Test
    public void shouldExtractTextInCorrectOrderFromPageWithRotatedXObject() throws Exception {
        // ARRANGE
        final String textInXObject = "X";
        final String expectedText = "A\nB\n" + textInXObject + "\nC";

        byte[] pdfBytes = createPdfWithTextInsideRotatedXObject(textInXObject);
        PdfReader reader = new PdfReader(pdfBytes);
        TextExtractionStrategy strategy = new LocationTextExtractionStrategy();

        // ACT
        String extractedText = PdfTextExtractor.getTextFromPage(reader, 1, strategy);

        // ASSERT
        Assert.assertEquals(expectedText, extractedText);
    }

    /**
     * Creates a PDF document containing text paragraphs and an Image created from a
     * PdfTemplate (an XObject). The text inside the XObject is subject to rotation.
     *
     * @param xobjectText The text to be placed inside the rotated XObject.
     * @return A byte array representing the generated PDF file.
     */
    private byte[] createPdfWithTextInsideRotatedXObject(String xobjectText) throws DocumentException, IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document doc = new Document();
        PdfWriter writer = PdfWriter.getInstance(doc, baos);
        doc.open();

        // Add text before the XObject
        doc.add(new Paragraph("A"));
        doc.add(new Paragraph("B"));

        // Create a template (XObject) that will contain rotated text
        PdfTemplate template = writer.getDirectContent().createTemplate(20, 100);

        // Define a transformation to rotate the content within the template by -90 degrees.
        AffineTransform tx = new AffineTransform();
        tx.translate(0, template.getHeight());
        tx.rotate(-Math.PI / 2.0); // -90 degrees in radians
        template.transform(tx);

        // Add text to the now-rotated coordinate system of the template.
        template.beginText();
        template.setFontAndSize(BaseFont.createFont(), 12);
        template.moveText(0, template.getWidth() - 12);
        template.showText(xobjectText);
        template.endText();

        // Convert the template into an Image and rotate the image itself by +90 degrees.
        // The text extraction engine must correctly handle this nested transformation.
        Image xobjectImage = Image.getInstance(template);
        xobjectImage.setRotationDegrees(90);
        doc.add(xobjectImage);

        // Add text after the XObject
        doc.add(new Paragraph("C"));

        doc.close();
        return baos.toByteArray();
    }
}