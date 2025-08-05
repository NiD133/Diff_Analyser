/*
 *
 * This file is part of the iText (R) project.
    Copyright (c) 1998-2022 iText Group NV
 * Authors: Bruno Lowagie, Paulo Soares, Kevin Day, et al.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation with the addition of the
 * following permission added to Section 15 as permitted in Section 7(a):
 * FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY
 * ITEXT GROUP. ITEXT GROUP DISCLAIMS THE WARRANTY OF NON INFRINGEMENT
 * OF THIRD PARTY RIGHTS
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program; if not, see http://www.gnu.org/licenses or write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA, 02110-1301 USA, or download the license from the following URL:
 * http://itextpdf.com/terms-of-use/
 *
 * The interactive user interfaces in modified source and object code versions
 * of this program must display Appropriate Legal Notices, as required under
 * Section 5 of the GNU Affero General Public License.
 *
 * In accordance with Section 7(b) of the GNU Affero General Public License,
 * a covered work must retain the producer line in every PDF that is created
 * or manipulated using iText.
 *
 * You can be released from the requirements of the license by purchasing
 * a commercial license. Buying such a license is mandatory as soon as you
 * develop commercial activities involving the iText software without
 * disclosing the source code of your own applications.
 * These activities include: offering paid services to customers as an ASP,
 * serving PDFs on the fly in a web application, shipping iText with a closed
 * source product.
 *
 * For more information, please contact iText Software Corp. at this
 * address: sales@itextpdf.com
 */
package com.itextpdf.text.pdf.parser;

import com.itextpdf.awt.geom.AffineTransform;
import com.itextpdf.testutils.TestResourceUtils;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
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
import java.io.InputStream;

/**
 * Tests for {@link SimpleTextExtractionStrategy}.
 *
 * @author kevin
 */
public class SimpleTextExtractionStrategyTest {

    private static final String TEXT_CHUNK_1 = "TEXT1 TEXT1";
    private static final String TEXT_CHUNK_2 = "TEXT2 TEXT2";

    private TextExtractionStrategy createStrategy() {
        return new SimpleTextExtractionStrategy();
    }

    // --- Tests for Collinear Text ---

    @Test
    public void should_concatenateCollinearText_when_renderedOnSameLineWithoutGap() throws Exception {
        // Arrange: Create a PDF with two text chunks on the same line with no space between them.
        byte[] pdfBytes = createPdfWithTwoTextChunks(TEXT_CHUNK_1, TEXT_CHUNK_2, 0, false, 0);
        String expectedText = TEXT_CHUNK_1 + TEXT_CHUNK_2;

        // Act
        String extractedText = PdfTextExtractor.getTextFromPage(new PdfReader(pdfBytes), 1, createStrategy());

        // Assert
        Assert.assertEquals(expectedText, extractedText);
    }

    @Test
    public void should_insertSpace_when_collinearTextHasSufficientGap() throws Exception {
        // Arrange: Create a PDF with two text chunks on the same line with a 2-point gap.
        byte[] pdfBytes = createPdfWithTwoTextChunks(TEXT_CHUNK_1, TEXT_CHUNK_2, 0, false, 2);
        String expectedText = TEXT_CHUNK_1 + " " + TEXT_CHUNK_2;

        // Act
        String extractedText = PdfTextExtractor.getTextFromPage(new PdfReader(pdfBytes), 1, createStrategy());

        // Assert
        Assert.assertEquals(expectedText, extractedText);
    }

    @Test
    public void should_notInsertExtraSpace_when_firstTextChunkEndsWithSpace() throws Exception {
        // Arrange: Create a PDF where the first text chunk already ends with a space.
        // The strategy should not add a second, redundant space.
        String text1WithSpace = TEXT_CHUNK_1 + " ";
        byte[] pdfBytes = createPdfWithTwoTextChunks(text1WithSpace, TEXT_CHUNK_2, 0, false, 2);
        String expectedText = text1WithSpace + TEXT_CHUNK_2;

        // Act
        String extractedText = PdfTextExtractor.getTextFromPage(new PdfReader(pdfBytes), 1, createStrategy());

        // Assert
        Assert.assertEquals(expectedText, extractedText);
    }

    @Test
    public void should_handleTrailingSpacesCorrectly() throws Exception {
        // Arrange
        byte[] pdfBytes = createPdfWithTwoTextChunks(TEXT_CHUNK_1 + " ", TEXT_CHUNK_2, 0, false, 6);
        String expectedText = TEXT_CHUNK_1 + " " + TEXT_CHUNK_2;

        // Act
        String extractedText = PdfTextExtractor.getTextFromPage(new PdfReader(pdfBytes), 1, createStrategy());

        // Assert
        Assert.assertEquals(expectedText, extractedText);
    }

    @Test
    public void should_handleLeadingSpacesCorrectly() throws Exception {
        // Arrange
        byte[] pdfBytes = createPdfWithTwoTextChunks(TEXT_CHUNK_1, " " + TEXT_CHUNK_2, 0, false, 6);
        String expectedText = TEXT_CHUNK_1 + " " + TEXT_CHUNK_2;

        // Act
        String extractedText = PdfTextExtractor.getTextFromPage(new PdfReader(pdfBytes), 1, createStrategy());

        // Assert
        Assert.assertEquals(expectedText, extractedText);
    }

    // --- Tests for Multi-line and Rotated Text ---

    @Test
    public void should_insertNewline_when_textIsOnDifferentLines() throws Exception {
        // Arrange: Create a PDF where the second text chunk is on a new line below the first.
        byte[] pdfBytes = createPdfWithTwoTextChunks(TEXT_CHUNK_1, TEXT_CHUNK_2, 0, true, -20);
        String expectedText = TEXT_CHUNK_1 + "\n" + TEXT_CHUNK_2;

        // Act
        String extractedText = PdfTextExtractor.getTextFromPage(new PdfReader(pdfBytes), 1, createStrategy());

        // Assert
        Assert.assertEquals(expectedText, extractedText);
    }

    @Test
    public void should_insertNewline_when_textIsOnDifferentLines_withNegative90DegreeRotation() throws Exception {
        // Arrange
        byte[] pdfBytes = createPdfWithTwoTextChunks(TEXT_CHUNK_1, TEXT_CHUNK_2, -90, true, -20);
        String expectedText = TEXT_CHUNK_1 + "\n" + TEXT_CHUNK_2;

        // Act
        String extractedText = PdfTextExtractor.getTextFromPage(new PdfReader(pdfBytes), 1, createStrategy());

        // Assert
        Assert.assertEquals(expectedText, extractedText);
    }

    @Test
    public void should_insertNewline_when_textIsOnDifferentLines_withPositive90DegreeRotation() throws Exception {
        // Arrange
        byte[] pdfBytes = createPdfWithTwoTextChunks(TEXT_CHUNK_1, TEXT_CHUNK_2, 90, true, -20);
        String expectedText = TEXT_CHUNK_1 + "\n" + TEXT_CHUNK_2;

        // Act
        String extractedText = PdfTextExtractor.getTextFromPage(new PdfReader(pdfBytes), 1, createStrategy());

        // Assert
        Assert.assertEquals(expectedText, extractedText);
    }

    @Test
    public void should_insertNewline_when_textIsOnDifferentLines_withPartialRotation() throws Exception {
        // Arrange
        byte[] pdfBytes = createPdfWithTwoTextChunks(TEXT_CHUNK_1, TEXT_CHUNK_2, 33, true, -20);
        String expectedText = TEXT_CHUNK_1 + "\n" + TEXT_CHUNK_2;

        // Act
        String extractedText = PdfTextExtractor.getTextFromPage(new PdfReader(pdfBytes), 1, createStrategy());

        // Assert
        Assert.assertEquals(expectedText, extractedText);
    }

    // --- Tests for TJ Operator (Glyph Positioning) ---

    @Test
    public void should_insertSpace_when_wordSpacingIsCreatedByTjOperator() throws Exception {
        // Arrange: Create a PDF using the TJ operator to position two text chunks with a space.
        byte[] pdfBytes = createPdfWithTjOperator(TEXT_CHUNK_1, TEXT_CHUNK_2, 250);
        String expectedText = TEXT_CHUNK_1 + " " + TEXT_CHUNK_2;

        // Act
        String extractedText = PdfTextExtractor.getTextFromPage(new PdfReader(pdfBytes), 1, createStrategy());

        // Assert
        Assert.assertEquals(expectedText, extractedText);
    }

    @Test
    public void should_correctlyExtractText_when_usingComplexGlyphPositioning() throws Exception {
        // Arrange: Create a PDF with complex character spacing via the TJ operator.
        String tjCommand = "[(S)3.2(an)-255.0(D)13.0(i)8.3(e)-10.1(g)1.6(o)-247.5(C)2.4(h)5.8(ap)3.0(t)10.7(er)]TJ";
        byte[] pdfBytes = createPdfWithTjOperator(tjCommand);
        String expectedText = "San Diego Chapter";

        // Act
        String extractedText = PdfTextExtractor.getTextFromPage(new PdfReader(pdfBytes), 1, createStrategy());

        // Assert
        Assert.assertEquals(expectedText, extractedText);
    }

    // --- Tests for Special PDF Structures ---

    @Test
    public void should_extractTextFromXObject() throws Exception {
        // Arrange
        String xObjectText = "This is text inside an XObject";
        byte[] pdfBytes = createPdfWithXObject(xObjectText);

        // Act
        String extractedText = PdfTextExtractor.getTextFromPage(new PdfReader(pdfBytes), 1, createStrategy());

        // Assert
        Assert.assertTrue("Extracted text should contain the XObject text.", extractedText.contains(xObjectText));
    }

    // --- Regression Tests from PDF Files ---

    @Test
    public void should_produceSameOutputAsCharByCharStrategy_forPage229RegressionPdf() throws IOException {
        // This regression test uses a PDF where text is rendered character by character.
        // It ensures that the standard strategy (processing whole text chunks) produces the
        // same result as a strategy that processes each character individually. This validates
        // the logic for handling character-based render information.
        try (
                InputStream is = TestResourceUtils.getResourceAsStream(this, "page229.pdf");
                PdfReader reader = new PdfReader(is)
        ) {
            // Act
            String textFromStandardStrategy = PdfTextExtractor.getTextFromPage(reader, 1, new SimpleTextExtractionStrategy());
            String textFromCharByCharStrategy = PdfTextExtractor.getTextFromPage(reader, 1, new SingleCharacterSimpleTextExtractionStrategy());

            // Assert
            Assert.assertEquals("Results from standard and char-by-char strategies should match.",
                    textFromStandardStrategy, textFromCharByCharStrategy);
        }
    }

    @Test
    public void should_produceSameOutputAsCharByCharStrategy_forIsoRegressionPdf() throws IOException {
        // This is another regression test to ensure consistency between chunk-based and
        // character-based text processing for a more complex, real-world document.
        try (
                InputStream is = TestResourceUtils.getResourceAsStream(this, "ISO-TC171-SC2_N0896_SC2WG5_Edinburgh_Agenda.pdf");
                PdfReader reader = new PdfReader(is)
        ) {
            // Act
            String textFromPage1 = PdfTextExtractor.getTextFromPage(reader, 1, new SimpleTextExtractionStrategy());
            String textFromPage2 = PdfTextExtractor.getTextFromPage(reader, 2, new SimpleTextExtractionStrategy());
            String textFromStandardStrategy = textFromPage1 + "\n" + textFromPage2;

            String textFromPage1Char = PdfTextExtractor.getTextFromPage(reader, 1, new SingleCharacterSimpleTextExtractionStrategy());
            String textFromPage2Char = PdfTextExtractor.getTextFromPage(reader, 2, new SingleCharacterSimpleTextExtractionStrategy());
            String textFromCharByCharStrategy = textFromPage1Char + "\n" + textFromPage2Char;

            // Assert
            Assert.assertEquals("Results from standard and char-by-char strategies should match.",
                    textFromStandardStrategy, textFromCharByCharStrategy);
        }
    }

    // ===================================================================================
    //                                 HELPER METHODS
    // ===================================================================================

    /**
     * Creates a PDF document with two text chunks, allowing for control over their relative position and rotation.
     * This is the primary helper method for testing how the strategy handles different text layouts.
     *
     * @param text1              The first string of text.
     * @param text2              The second string of text.
     * @param rotation           The rotation in degrees to apply to the text.
     * @param moveTextToNextLine If true, uses the 'moveText' operator (Td) to create a new line. If false,
     *                           uses a translation transform (Tm) to move horizontally.
     * @param moveTextDelta      The distance to move before drawing the second text chunk. The unit and direction
     *                           depend on the 'moveTextToNextLine' parameter.
     * @return A byte array containing the generated PDF.
     */
    private static byte[] createPdfWithTwoTextChunks(String text1, String text2, float rotation, boolean moveTextToNextLine, float moveTextDelta) throws Exception {
        try (ByteArrayOutputStream byteStream = new ByteArrayOutputStream()) {
            Document document = new Document(PageSize.LETTER);
            PdfWriter writer = PdfWriter.getInstance(document, byteStream);
            document.open();

            PdfContentByte cb = writer.getDirectContent();
            BaseFont font = BaseFont.createFont();

            float x = document.getPageSize().getWidth() / 2;
            float y = document.getPageSize().getHeight() / 2;

            cb.saveState();
            cb.transform(AffineTransform.getTranslateInstance(x, y));
            cb.beginText();
            cb.setFontAndSize(font, 12);
            cb.transform(AffineTransform.getRotateInstance(Math.toRadians(rotation)));
            cb.showText(text1);
            if (moveTextToNextLine) {
                cb.moveText(0, moveTextDelta);
            } else {
                cb.transform(AffineTransform.getTranslateInstance(moveTextDelta, 0));
            }
            cb.showText(text2);
            cb.endText();
            cb.restoreState();

            document.close();
            return byteStream.toByteArray();
        }
    }

    /**
     * Creates a PDF with text positioned using the TJ operator with an explicit spacing value.
     *
     * @param text1         The first text chunk.
     * @param text2         The second text chunk.
     * @param spaceInPoints The spacing value (kerning) to insert between chunks.
     * @return A byte array containing the generated PDF.
     */
    private static byte[] createPdfWithTjOperator(String text1, String text2, int spaceInPoints) throws Exception {
        String tjCommand = String.format("[ (%s) %d (%s) ]TJ", text1, -spaceInPoints, text2);
        return createPdfWithTjOperator(tjCommand);
    }

    /**
     * Creates a PDF with text positioned using a raw TJ operator command.
     *
     * @param directContentTj The full TJ command string.
     * @return A byte array containing the generated PDF.
     */
    private static byte[] createPdfWithTjOperator(String directContentTj) throws Exception {
        try (ByteArrayOutputStream byteStream = new ByteArrayOutputStream()) {
            Document document = new Document(PageSize.LETTER);
            PdfWriter writer = PdfWriter.getInstance(document, byteStream);
            document.open();

            PdfContentByte cb = writer.getDirectContent();
            BaseFont font = BaseFont.createFont();

            cb.beginText();
            cb.setFontAndSize(font, 12);
            cb.moveText(100, 500);
            cb.getInternalBuffer().append(directContentTj).append("\n");
            cb.endText();

            document.close();
            return byteStream.toByteArray();
        }
    }

    /**
     * Creates a PDF containing an XObject (a reusable graphic object), which in turn contains text.
     *
     * @param xobjectText The text to place inside the XObject.
     * @return A byte array containing the generated PDF.
     */
    private static byte[] createPdfWithXObject(String xobjectText) throws Exception {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            Document doc = new Document();
            PdfWriter writer = PdfWriter.getInstance(doc, baos);
            doc.open();

            doc.add(new Paragraph("Before XObject"));

            PdfTemplate template = writer.getDirectContent().createTemplate(200, 100);
            template.beginText();
            template.setFontAndSize(BaseFont.createFont(), 12);
            template.moveText(5, 50);
            template.showText(xobjectText);
            template.endText();

            Image xobjectImage = Image.getInstance(template);
            doc.add(xobjectImage);

            doc.add(new Paragraph("After XObject"));

            doc.close();
            return baos.toByteArray();
        }
    }

    /**
     * A test-specific strategy that renders text character by character.
     * Used to verify that chunk-based processing is equivalent to char-based processing.
     */
    private static class SingleCharacterSimpleTextExtractionStrategy extends SimpleTextExtractionStrategy {
        @Override
        public void renderText(TextRenderInfo renderInfo) {
            for (TextRenderInfo characterRenderInfo : renderInfo.getCharacterRenderInfos()) {
                super.renderText(characterRenderInfo);
            }
        }
    }
}