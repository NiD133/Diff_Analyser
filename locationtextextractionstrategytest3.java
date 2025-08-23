package com.itextpdf.text.pdf.parser;

import com.itextpdf.awt.geom.AffineTransform;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * A test suite for LocationTextExtractionStrategy, focusing on advanced and edge-case PDF layouts.
 * This includes scenarios with rotated pages, rotated XObjects, overlapping text,
 * superscripts, and complex character spacing to ensure the strategy extracts text
 * in a logically sorted and readable order.
 */
public class LocationTextExtractionStrategyAdvancedScenariosTest {

    private static final float DEFAULT_FONT_SIZE = 12f;

    /**
     * The strategy under test. This is instantiated for each text extraction operation.
     */
    private TextExtractionStrategy createExtractionStrategy() {
        return new LocationTextExtractionStrategy();
    }

    // --- Test Cases ---

    @Test
    public void testExtractsTextFromRotatedPageCorrectly() throws Exception {
        // Arrange: Create a PDF with a 90-degree rotated page.
        byte[] pdfBytes = createPdfWithRotatedPage("Line 1\nLine 2\nLine 3");

        // Act: Extract text from the page.
        String extractedText = extractText(pdfBytes);

        // Assert: The text should be extracted in the correct reading order.
        Assert.assertEquals("Line 1\nLine 2\nLine 3", extractedText);
    }

    @Test
    public void testExtractsTextFromRotatedXObjectInCorrectOrder() throws Exception {
        // Arrange: Create a PDF with text, a rotated XObject containing text, and more text.
        byte[] pdfBytes = createPdfWithRotatedXObject("Rotated XObject Text");

        // Act: Extract text.
        String extractedText = extractText(pdfBytes);

        // Assert: The strategy should correctly order the text from the main content and the XObject.
        Assert.assertEquals("A\nB\nRotated XObject Text\nC", extractedText);
    }

    @Test
    public void testExtractsHorizontallyOverlappingTextInCorrectOrder() throws Exception {
        // Arrange: Create a PDF where two sets of text chunks are on the same line and overlap.
        // The second set of chunks is slightly offset to start between the first set's chunks.
        String[] textSet1 = {"TextA", "TextC"};
        String[] textSet2 = {"TextB", "TextD"};
        byte[] pdfBytes = createPdfWithHorizontallyOverlappingText(textSet1, textSet2);

        // Act: Extract text.
        String extractedText = extractText(pdfBytes);

        // Assert: The strategy should sort the text chunks by their horizontal position.
        Assert.assertEquals("TextA TextB TextC TextD", extractedText);
    }

    @Test
    public void testExtractsVerticallyOverlappingTextInCorrectOrder() throws Exception {
        // Arrange: Create a PDF where lines of text from two sets are interleaved.
        String[] textSet1 = {"Line 1", "Line 3"};
        String[] textSet2 = {"Line 2", "Line 4"};
        byte[] pdfBytes = createPdfWithVerticallyOverlappingText(textSet1, textSet2);

        // Act: Extract text.
        String extractedText = extractText(pdfBytes);

        // Assert: The strategy should sort the text chunks by their vertical position, preserving line order.
        Assert.assertEquals("Line 1\nLine 2\nLine 3\nLine 4", extractedText);
    }

    @Test
    public void testExtractsTextWithNegativeCharacterSpacingAsSingleWord() throws Exception {
        // Arrange: Create a PDF that uses negative character spacing to join two parts of a word.
        byte[] pdfBytes = createPdfWithNegativeCharSpacing("Hel", -200, "lo");

        // Act: Extract text.
        String extractedText = extractText(pdfBytes);

        // Assert: The strategy should merge the text chunks into a single word.
        Assert.assertEquals("Hello", extractedText);
    }

    @Test
    public void testExtractsSuperscriptOnSameLineAsRegularText() throws Exception {
        // Arrange: Create a PDF with regular text followed by a superscript.
        byte[] pdfBytes = createPdfWithSuperscript("E=mc", "2");

        // Act: Extract text.
        String extractedText = extractText(pdfBytes);

        // Assert: The superscript should be appended to the regular text on the same line.
        Assert.assertEquals("E=mc2", extractedText);
    }

    @Test
    public void testExtractsTextWithComplexKerningAdjustments() throws Exception {
        // Arrange: Create a PDF with complex text adjustments (kerning) using a PdfTextArray.
        byte[] pdfBytes = createPdfWithComplexKerning();

        // Act: Extract text.
        String extractedText = extractText(pdfBytes);

        // Assert: The strategy should correctly assemble the word despite the adjustments.
        Assert.assertEquals("Preface", extractedText);
    }

    @Test
    public void testExtractsTextWithVerySmallFontSize() throws Exception {
        // Arrange: Create a PDF containing text with a very small font size, followed by a normal font size.
        byte[] pdfBytes = createPdfWithVerySmallFontSize();

        // Act: Extract text.
        String extractedText = extractText(pdfBytes);

        // Assert: The strategy should handle both font sizes and extract the text correctly.
        Assert.assertEquals("Preface Preface", extractedText);
    }


    // --- Helper Methods for PDF Creation and Text Extraction ---

    /**
     * A generic utility to extract text from the first page of a PDF byte array.
     */
    private String extractText(byte[] pdfBytes) throws IOException {
        PdfReader reader = new PdfReader(pdfBytes);
        return PdfTextExtractor.getTextFromPage(reader, 1, createExtractionStrategy());
    }

    /**
     * Creates a simple PDF with one page containing the given text. The page is rotated 90 degrees.
     */
    private byte[] createPdfWithRotatedPage(final String... text) throws DocumentException, IOException {
        final ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        // Use PageSize.LETTER.rotate() to create a landscape page.
        final Document document = new Document(PageSize.LETTER.rotate());
        PdfWriter.getInstance(document, byteStream);
        document.open();
        for (String string : text) {
            document.add(new Paragraph(string));
        }
        document.close();
        return byteStream.toByteArray();
    }

    /**
     * Creates a PDF with text on the main content stream and additional text inside a rotated XObject.
     * This tests if the strategy correctly integrates and orders text from different sources.
     */
    private byte[] createPdfWithRotatedXObject(String xobjectText) throws DocumentException, IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document doc = new Document();
        PdfWriter writer = PdfWriter.getInstance(doc, baos);
        doc.open();
        doc.add(new Paragraph("A"));
        doc.add(new Paragraph("B"));

        // Create a template (XObject) that will contain text.
        PdfTemplate template = writer.getDirectContent().createTemplate(20, 100);
        AffineTransform tx = new AffineTransform();
        tx.translate(0, template.getHeight());
        tx.rotate(-Math.PI / 2.0); // Rotate -90 degrees
        template.transform(tx);

        template.beginText();
        template.setFontAndSize(BaseFont.createFont(), DEFAULT_FONT_SIZE);
        template.moveText(0, template.getWidth() - DEFAULT_FONT_SIZE);
        template.showText(xobjectText);
        template.endText();

        // Add the template to the document as a rotated image.
        Image xobjectImage = Image.getInstance(template);
        xobjectImage.setRotationDegrees(90);
        doc.add(xobjectImage);

        doc.add(new Paragraph("C"));
        doc.close();
        return baos.toByteArray();
    }

    /**
     * Creates a PDF where text chunks are placed on the same line with horizontal overlap.
     * This tests the strategy's ability to sort text chunks based on their starting X-coordinate.
     */
    private byte[] createPdfWithHorizontallyOverlappingText(String[] text1, String[] text2) throws DocumentException, IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document doc = new Document();
        PdfWriter writer = PdfWriter.getInstance(doc, baos);
        doc.open();
        PdfContentByte canvas = writer.getDirectContent();

        final float Y_POS = 500f;
        final float X_START = 50f;
        final float X_INCREMENT = 70f;
        final float X_OFFSET = 12f; // Offset for the second set of text to create overlap.

        canvas.beginText();
        canvas.setFontAndSize(BaseFont.createFont(), DEFAULT_FONT_SIZE);

        // Draw the first set of text.
        float currentX = X_START;
        for (String text : text1) {
            canvas.showTextAligned(PdfContentByte.ALIGN_LEFT, text, currentX, Y_POS, 0);
            currentX += X_INCREMENT;
        }

        // Draw the second set of text, interleaved with the first.
        currentX = X_START + X_OFFSET;
        for (String text : text2) {
            canvas.showTextAligned(PdfContentByte.ALIGN_LEFT, text, currentX, Y_POS, 0);
            currentX += X_INCREMENT;
        }

        canvas.endText();
        doc.close();
        return baos.toByteArray();
    }

    /**
     * Creates a PDF where lines of text are placed with vertical overlap.
     * This tests the strategy's ability to sort text chunks based on their Y-coordinate to form correct lines.
     */
    private byte[] createPdfWithVerticallyOverlappingText(String[] text1, String[] text2) throws DocumentException, IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document doc = new Document();
        PdfWriter writer = PdfWriter.getInstance(doc, baos);
        doc.open();
        PdfContentByte canvas = writer.getDirectContent();

        final float X_POS = 50f;
        final float Y_START = 500f;
        final float Y_INCREMENT = 25f;
        final float Y_OFFSET = 13f; // Offset to interleave the lines.

        canvas.beginText();
        canvas.setFontAndSize(BaseFont.createFont(), DEFAULT_FONT_SIZE);

        // Draw the first set of lines.
        float currentY = Y_START;
        for (String text : text1) {
            canvas.showTextAligned(PdfContentByte.ALIGN_LEFT, text, X_POS, currentY, 0);
            currentY -= Y_INCREMENT;
        }

        // Draw the second set of lines, interleaved with the first.
        currentY = Y_START - Y_OFFSET;
        for (String text : text2) {
            canvas.showTextAligned(PdfContentByte.ALIGN_LEFT, text, X_POS, currentY, 0);
            currentY -= Y_INCREMENT;
        }

        canvas.endText();
        doc.close();
        return baos.toByteArray();
    }

    /**
     * Creates a PDF using the TJ operator with a negative value to pull text chunks together.
     * This simulates kerning or manual adjustments that should result in a single word.
     */
    private byte[] createPdfWithNegativeCharSpacing(String str1, float charSpacing, String str2) throws DocumentException, IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document doc = new Document();
        PdfWriter writer = PdfWriter.getInstance(doc, baos);
        doc.open();
        PdfContentByte canvas = writer.getDirectContent();
        canvas.beginText();
        canvas.setFontAndSize(BaseFont.createFont(), DEFAULT_FONT_SIZE);
        canvas.moveText(45, doc.getPageSize().getHeight() - 45);

        PdfTextArray textArray = new PdfTextArray();
        textArray.add(str1);
        textArray.add(charSpacing); // Negative value moves the next string to the left.
        textArray.add(str2);
        canvas.showText(textArray);

        canvas.endText();
        doc.close();
        return baos.toByteArray();
    }

    /**
     * Creates a PDF with a superscript character. This tests if the strategy keeps text with
     * a different vertical baseline (`setTextRise`) on the same logical line.
     */
    private byte[] createPdfWithSuperscript(String regularText, String superscriptText) throws DocumentException, IOException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, byteStream);
        document.open();

        document.add(new Chunk(regularText));
        Chunk superscriptChunk = new Chunk(superscriptText);
        superscriptChunk.setTextRise(7.0f); // Raise the text to make it a superscript.
        document.add(superscriptChunk);

        document.close();
        return byteStream.toByteArray();
    }

    /**
     * Creates a PDF using a complex PdfTextArray with multiple kerning adjustments.
     * This is an advanced test for text chunk assembly.
     */
    private byte[] createPdfWithComplexKerning() throws DocumentException, IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document doc = new Document();
        PdfWriter writer = PdfWriter.getInstance(doc, baos);
        doc.open();
        BaseFont font = BaseFont.createFont();
        PdfContentByte canvas = writer.getDirectContent();
        canvas.beginText();
        canvas.setFontAndSize(font, DEFAULT_FONT_SIZE);
        canvas.moveText(45, doc.getPageSize().getHeight() - 45);

        // This array manually adjusts the position after almost every character.
        PdfTextArray textArray = new PdfTextArray();
        textArray.add("P");
        textArray.add(-226.2f);
        textArray.add("r");
        textArray.add(-231.8f);
        textArray.add("e");
        textArray.add(-230.8f);
        textArray.add("f");
        textArray.add(-238);
        textArray.add("a");
        textArray.add(-238.9f);
        textArray.add("c");
        textArray.add(-228.9f);
        textArray.add("e");
        canvas.showText(textArray);

        canvas.endText();
        doc.close();
        return baos.toByteArray();
    }

    /**
     * Creates a PDF with text rendered at a very small font size (0.2pt).
     * This tests the strategy's robustness with unusual font metrics.
     */
    private byte[] createPdfWithVerySmallFontSize() throws IOException, DocumentException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document doc = new Document();
        PdfWriter writer = PdfWriter.getInstance(doc, baos);
        doc.open();
        BaseFont font = BaseFont.createFont();
        PdfContentByte canvas = writer.getDirectContent();
        canvas.beginText();
        canvas.moveText(45, doc.getPageSize().getHeight() - 45);

        // Write "Preface " with a tiny font
        canvas.setFontAndSize(font, 0.2f);
        canvas.showText("Preface ");

        // Write "Preface " again with a normal font
        canvas.setFontAndSize(font, 10f);
        canvas.showText("Preface");

        canvas.endText();
        doc.close();
        return baos.toByteArray();
    }
}