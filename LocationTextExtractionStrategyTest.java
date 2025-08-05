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
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfTextArray;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests the {@link LocationTextExtractionStrategy} to ensure it correctly extracts and orders text
 * based on its position, orientation, and styling on the PDF page.
 */
public class LocationTextExtractionStrategyTest extends SimpleTextExtractionStrategyTest {

    @Override
    @Before
    public void setUp() throws Exception {
    }

    @Override
    @After
    public void tearDown() throws Exception {
    }

    @Override
    public TextExtractionStrategy createRenderListenerForTest() {
        return new LocationTextExtractionStrategy();
    }

    /**
     * Tests that text positioned vertically is extracted in the correct top-to-bottom order,
     * separated by newlines.
     */
    @Test
    public void testExtractsTextInCorrectVerticalOrder() throws Exception {
        // Arrange
        String[] mainText = {"A", "B", "C", "D"};
        String[] overlappingText = {"AA", "BB", "CC", "DD"};
        PdfReader reader = createPdfWithOverlappingTextVertical(mainText, overlappingText);
        String expectedText = "A\nAA\nB\nBB\nC\nCC\nD\nDD";

        // Act
        String actualText = PdfTextExtractor.getTextFromPage(reader, 1, createRenderListenerForTest());

        // Assert
        Assert.assertEquals("Text should be extracted in top-to-bottom order.", expectedText, actualText);
    }

    /**
     * Tests that text positioned horizontally is extracted in the correct left-to-right order,
     * separated by spaces.
     */
    @Test
    public void testExtractsTextInCorrectHorizontalOrderWithSpaces() throws Exception {
        // Arrange
        String[] text1 = {"A", "B", "C", "D"};
        String[] text2 = {"AA", "BB", "CC", "DD"};
        byte[] content = createPdfWithOverlappingTextHorizontal(text1, text2);
        PdfReader reader = new PdfReader(content);
        String expectedText = "A AA B BB C CC D DD";

        // Act
        String actualText = PdfTextExtractor.getTextFromPage(reader, 1, createRenderListenerForTest());

        // Assert
        Assert.assertEquals("Text should be extracted in left-to-right order with spaces.", expectedText, actualText);
    }

    /**
     * Verifies that text is extracted correctly from a page that is rotated 90 degrees.
     */
    @Test
    public void testExtractsTextFromPageRotated90Degrees() throws Exception {
        // Arrange
        byte[] bytes = createSimplePdf(PageSize.LETTER.rotate(), "A\nB\nC\nD");
        PdfReader reader = new PdfReader(bytes);
        String expectedText = "A\nB\nC\nD";

        // Act
        String actualText = PdfTextExtractor.getTextFromPage(reader, 1, createRenderListenerForTest());

        // Assert
        Assert.assertEquals("Text extraction should be correct on a 90-degree rotated page.", expectedText, actualText);
    }

    /**
     * Verifies that text is extracted correctly from a page that is rotated 180 degrees.
     */
    @Test
    public void testExtractsTextFromPageRotated180Degrees() throws Exception {
        // Arrange
        byte[] bytes = createSimplePdf(PageSize.LETTER.rotate().rotate(), "A\nB\nC\nD");
        PdfReader reader = new PdfReader(bytes);
        String expectedText = "A\nB\nC\nD";

        // Act
        String actualText = PdfTextExtractor.getTextFromPage(reader, 1, createRenderListenerForTest());

        // Assert
        Assert.assertEquals("Text extraction should be correct on a 180-degree rotated page.", expectedText, actualText);
    }

    /**
     * Verifies that text is extracted correctly from a page that is rotated 270 degrees.
     */
    @Test
    public void testExtractsTextFromPageRotated270Degrees() throws Exception {
        // Arrange
        byte[] bytes = createSimplePdf(PageSize.LETTER.rotate().rotate().rotate(), "A\nB\nC\nD");
        PdfReader reader = new PdfReader(bytes);
        String expectedText = "A\nB\nC\nD";

        // Act
        String actualText = PdfTextExtractor.getTextFromPage(reader, 1, createRenderListenerForTest());

        // Assert
        Assert.assertEquals("Text extraction should be correct on a 270-degree rotated page.", expectedText, actualText);
    }

    /**
     * Tests that text within a rotated XObject is extracted and placed in the correct
     * reading order relative to the surrounding text on the page.
     */
    @Test
    public void testExtractsTextFromRotatedXObjectInCorrectOrder() throws Exception {
        // Arrange
        byte[] content = createPdfWithRotatedXObject("X");
        PdfReader reader = new PdfReader(content);
        String expectedText = "A\nB\nX\nC";

        // Act
        String actualText = PdfTextExtractor.getTextFromPage(reader, 1, createRenderListenerForTest());

        // Assert
        Assert.assertEquals("Text from the rotated XObject should be inserted in the correct order.", expectedText, actualText);
    }

    /**
     * Tests that text with negative character spacing, which causes characters to overlap,
     * is still extracted as a single, coherent word.
     */
    @Test
    public void testExtractsTextWithNegativeCharacterSpacingAsSingleWord() throws Exception {
        // Arrange
        byte[] content = createPdfWithNegativeCharSpacing("W", 200, "A");
        PdfReader reader = new PdfReader(content);
        String expectedText = "WA";

        // Act
        String actualText = PdfTextExtractor.getTextFromPage(reader, 1, createRenderListenerForTest());

        // Assert
        Assert.assertEquals("Text with negative character spacing should be merged into one word.", expectedText, actualText);
    }

    /**
     * This is a sanity check to ensure the underlying vector math, used by the strategy
     * to determine the spatial relationship between text chunks, is functioning as expected.
     * It verifies the dot product calculation for parallel and anti-parallel vectors.
     */
    @Test
    public void testVectorMathForChunkComparison_IsCorrect() {
        // Arrange
        Vector start = new Vector(0, 0, 1);
        Vector end = new Vector(1, 0, 1);
        Vector normalizedDirection = end.subtract(start).normalize();

        Vector antiparallelStart = new Vector(0.9f, 0, 1);
        Vector parallelStart = new Vector(1.1f, 0, 1);

        // Act
        float rsltAntiParallel = antiparallelStart.subtract(end).dot(normalizedDirection);
        float rsltParallel = parallelStart.subtract(end).dot(normalizedDirection);

        // Assert
        Assert.assertEquals("Anti-parallel vector projection should be negative.", -0.1f, rsltAntiParallel, 0.0001);
        Assert.assertEquals("Parallel vector projection should be positive.", 0.1f, rsltParallel, 0.0001);
    }

    /**
     * Tests that text formatted as a superscript is extracted as part of the same word,
     * without any extra spaces or newlines.
     */
    @Test
    public void testExtractsSuperscriptTextAsPartOfSameWord() throws Exception {
        // Arrange
        byte[] content = createPdfWithSupescript("Hel", "lo");
        PdfReader reader = new PdfReader(content);
        String expectedText = "Hello";

        // Act
        String actualText = PdfTextExtractor.getTextFromPage(reader, 1, createRenderListenerForTest());

        // Assert
        Assert.assertEquals("Superscript text should be appended to the preceding text.", expectedText, actualText);
    }

    /**
     * Tests text extraction in a complex scenario where character spacing adjustments
     * are used, which can be challenging for extraction algorithms.
     */
    @Test
    public void testExtractsTextWithComplexSpacingCorrectly() throws Exception {
        // Arrange
        byte[] content = createPdfWithFontSpacingEqualsCharSpacing();
        PdfReader reader = new PdfReader(content);
        String expectedText = "Preface";

        // Act
        String actualText = PdfTextExtractor.getTextFromPage(reader, 1, createRenderListenerForTest());

        // Assert
        Assert.assertEquals("Text with complex character spacing should be extracted correctly.", expectedText, actualText);
    }

    /**
     * Tests that text written with a very small font size is still extracted correctly,
     * along with other text of a normal font size.
     */
    @Test
    public void testExtractsTextWithVerySmallAndNormalFontSizes() throws Exception {
        // Arrange
        byte[] content = createPdfWithLittleFontSize();
        PdfReader reader = new PdfReader(content);
        String expectedText = "Preface Preface ";

        // Act
        String actualText = PdfTextExtractor.getTextFromPage(reader, 1, createRenderListenerForTest());

        // Assert
        Assert.assertEquals("Text should be extracted correctly regardless of font size.", expectedText, actualText);
    }

    // region PDF Creation Helper Methods

    private byte[] createPdfWithNegativeCharSpacing(String str1, float charSpacing, String str2) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document doc = new Document();
        PdfWriter writer = PdfWriter.getInstance(doc, baos);
        writer.setCompressionLevel(0);
        doc.open();

        PdfContentByte canvas = writer.getDirectContent();
        canvas.beginText();
        canvas.setFontAndSize(BaseFont.createFont(), 12);
        canvas.moveText(45, doc.getPageSize().getHeight() - 45);
        PdfTextArray ta = new PdfTextArray();
        ta.add(str1);
        ta.add(charSpacing);
        ta.add(str2);
        canvas.showText(ta);
        canvas.endText();

        doc.close();

        return baos.toByteArray();
    }

    private byte[] createPdfWithRotatedXObject(String xobjectText) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document doc = new Document();
        PdfWriter writer = PdfWriter.getInstance(doc, baos);
        writer.setCompressionLevel(0);
        doc.open();

        doc.add(new Paragraph("A"));
        doc.add(new Paragraph("B"));

        boolean rotate = true;

        PdfTemplate template = writer.getDirectContent().createTemplate(20, 100);
        template.setColorStroke(BaseColor.GREEN);
        template.rectangle(0, 0, template.getWidth(), template.getHeight());
        template.stroke();
        AffineTransform tx = new AffineTransform();
        if (rotate) {
            tx.translate(0, template.getHeight());
            tx.rotate(-90 / 180f * Math.PI);
        }
        template.transform(tx);
        template.beginText();
        template.setFontAndSize(BaseFont.createFont(), 12);
        if (rotate)
            template.moveText(0, template.getWidth() - 12);
        else
            template.moveText(0, template.getHeight() - 12);
        template.showText(xobjectText);

        template.endText();

        Image xobjectImage = Image.getInstance(template);
        if (rotate)
            xobjectImage.setRotationDegrees(90);
        doc.add(xobjectImage);

        doc.add(new Paragraph("C"));

        doc.close();

        return baos.toByteArray();
    }

    private byte[] createSimplePdf(Rectangle pageSize, final String... text) throws Exception {
        final ByteArrayOutputStream byteStream = new ByteArrayOutputStream();

        final Document document = new Document(pageSize);
        PdfWriter.getInstance(document, byteStream);
        document.open();
        for (String string : text) {
            document.add(new Paragraph(string));
            document.newPage();
        }

        document.close();

        return byteStream.toByteArray();
    }

    protected byte[] createPdfWithOverlappingTextHorizontal(String[] text1, String[] text2) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document doc = new Document();
        PdfWriter writer = PdfWriter.getInstance(doc, baos);
        writer.setCompressionLevel(0);
        doc.open();

        PdfContentByte canvas = writer.getDirectContent();
        float ystart = 500;
        float xstart = 50;

        canvas.beginText();
        canvas.setFontAndSize(BaseFont.createFont(), 12);
        float x = xstart;
        float y = ystart;
        for (String text : text1) {
            canvas.showTextAligned(PdfContentByte.ALIGN_LEFT, text, x, y, 0);
            x += 70.0;
        }

        x = xstart + 12;
        y = ystart;
        for (String text : text2) {
            canvas.showTextAligned(PdfContentByte.ALIGN_LEFT, text, x, y, 0);
            x += 70.0;
        }
        canvas.endText();

        doc.close();

        return baos.toByteArray();
    }

    private PdfReader createPdfWithOverlappingTextVertical(String[] text1, String[] text2) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document doc = new Document();
        PdfWriter writer = PdfWriter.getInstance(doc, baos);
        writer.setCompressionLevel(0);
        doc.open();

        PdfContentByte canvas = writer.getDirectContent();
        float ystart = 500;

        canvas.beginText();
        canvas.setFontAndSize(BaseFont.createFont(), 12);
        float x = 50;
        float y = ystart;
        for (String text : text1) {
            canvas.showTextAligned(PdfContentByte.ALIGN_LEFT, text, x, y, 0);
            y -= 25.0;
        }

        y = ystart - 13;
        for (String text : text2) {
            canvas.showTextAligned(PdfContentByte.ALIGN_LEFT, text, x, y, 0);
            y -= 25.0;
        }
        canvas.endText();

        doc.close();

        return new PdfReader(baos.toByteArray());
    }

    private byte[] createPdfWithSupescript(String regularText, String superscriptText) throws Exception {
        final ByteArrayOutputStream byteStream = new ByteArrayOutputStream();

        final Document document = new Document();
        PdfWriter.getInstance(document, byteStream);
        document.open();
        document.add(new Chunk(regularText));
        Chunk c2 = new Chunk(superscriptText);
        c2.setTextRise(7.0f);
        document.add(c2);

        document.close();

        return byteStream.toByteArray();
    }

    private byte[] createPdfWithFontSpacingEqualsCharSpacing() throws DocumentException, IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document doc = new Document();
        PdfWriter writer = PdfWriter.getInstance(doc, baos);

        writer.setCompressionLevel(0);
        doc.open();

        BaseFont font = BaseFont.createFont();
        int fontSize = 12;
        float charSpace = font.getWidth(' ') / 1000.0f;

        PdfContentByte canvas = writer.getDirectContent();
        canvas.beginText();
        canvas.setFontAndSize(font, fontSize);
        canvas.moveText(45, doc.getPageSize().getHeight() - 45);
        canvas.setCharacterSpacing(-charSpace * fontSize);

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

    private byte[] createPdfWithLittleFontSize() throws IOException, DocumentException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document doc = new Document();
        PdfWriter writer = PdfWriter.getInstance(doc, baos);
        writer.setCompressionLevel(0);
        doc.open();

        BaseFont font = BaseFont.createFont();
        PdfContentByte canvas = writer.getDirectContent();
        canvas.beginText();
        canvas.setFontAndSize(font, 0.2f);
        canvas.moveText(45, doc.getPageSize().getHeight() - 45);

        PdfTextArray textArray = new PdfTextArray();
        textArray.add("P");
        textArray.add("r");
        textArray.add("e");
        textArray.add("f");
        textArray.add("a");
        textArray.add("c");
        textArray.add("e");
        textArray.add(" ");

        canvas.showText(textArray);
        canvas.setFontAndSize(font, 10);
        canvas.showText(textArray);

        canvas.endText();

        doc.close();

        return baos.toByteArray();
    }
    // endregion
}