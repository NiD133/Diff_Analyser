package com.itextpdf.text.pdf.parser;

import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;

/**
 * Tests the text extraction capabilities of {@link SimpleTextExtractionStrategy}
 * when dealing with text inside XObjects.
 */
public class SimpleTextExtractionStrategyXObjectTest {

    /**
     * Verifies that text placed inside a PdfTemplate (which becomes an XObject in the PDF)
     * is correctly extracted in its proper sequence along with other text on the page.
     */
    @Test
    public void shouldExtractTextFromWithinAnXObject() throws Exception {
        // Arrange
        final String textInXObject = "This is inside the XObject";
        // The strategy should extract text from paragraphs and the XObject, separated by newlines.
        final String expectedText = "Before XObject\n" + textInXObject + "\nAfter XObject";

        byte[] pdfBytes = createPdfWithTextInXObject(textInXObject);
        PdfReader reader = new PdfReader(pdfBytes);

        // Act
        TextExtractionStrategy strategy = new SimpleTextExtractionStrategy();
        String extractedText = PdfTextExtractor.getTextFromPage(reader, 1, strategy);

        // Assert
        Assert.assertEquals(expectedText, extractedText);
    }

    /**
     * Creates a PDF document containing text within a PdfTemplate (XObject).
     * The structure is a paragraph, followed by the XObject, followed by another paragraph.
     *
     * @param xobjectText The text to be placed inside the XObject.
     * @return A byte array representing the generated PDF file.
     */
    private byte[] createPdfWithTextInXObject(String xobjectText) throws Exception {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            Document document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, baos);
            document.open();

            document.add(new Paragraph("Before XObject"));

            // Create a template (XObject) and add text to it
            PdfTemplate template = writer.getDirectContent().createTemplate(250, 20);
            template.beginText();
            template.setFontAndSize(BaseFont.createFont(), 12);
            template.showText(xobjectText);
            template.endText();

            // Add the template to the document as an image
            Image xobjectImage = Image.getInstance(template);
            document.add(xobjectImage);

            document.add(new Paragraph("After XObject"));

            document.close();
            return baos.toByteArray();
        }
    }
}