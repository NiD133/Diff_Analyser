/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2022 iText Group NV
    Authors: iText Software.

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License version 3
    as published by the Free Software Foundation with the addition of the
    following permission added to Section 15 as permitted in Section 7(a):
    FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY
    ITEXT GROUP. ITEXT GROUP DISCLAIMS THE WARRANTY OF NON INFRINGEMENT
    OF THIRD PARTY RIGHTS

    This program is distributed in the hope that it will be useful, but
    WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
    or FITNESS FOR A PARTICULAR PURPOSE.
    See the GNU Affero General Public License for more details.
    You should have received a copy of the GNU Affero General Public License
    along with this program; if not, see http://www.gnu.org/licenses or write to
    the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
    Boston, MA, 02110-1301 USA, or download the license from the following URL:
    http://itextpdf.com/terms-of-use/

    The interactive user interfaces in modified source and object code versions
    of this program must display Appropriate Legal Notices, as required under
    Section 5 of the GNU Affero General Public License.

    In accordance with Section 7(b) of the GNU Affero General Public License,
    a covered work must retain the producer line in every PDF that is created
    or manipulated using iText.

    You can be released from the requirements of the license by purchasing
    a commercial license. Buying such a license is mandatory as soon as you
    develop commercial activities involving the iText software without
    disclosing the source code of your own applications.
    These activities include: offering paid services to customers as an ASP,
    serving PDFs on the fly in a web application, shipping iText with a closed
    source product.

    For more information, please contact iText Software Corp. at this
    address: sales@itextpdf.com
 */
package com.itextpdf.text.pdf.parser;

import com.itextpdf.testutils.TestResourceUtils;
import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Tests for the {@link TextRenderInfo} class, focusing on text position,
 * character rendering, and font handling.
 */
public class TextRenderInfoTest {

    private static final int FIRST_PAGE = 1;
    private static final int FIRST_ELEMENT_INDEX = 0;

    /**
     * Verifies that getCharacterRenderInfos() correctly splits a text string
     * and that the baseline of each character connects seamlessly to the next.
     */
    @Test
    public void getCharacterRenderInfos_shouldReturnContiguousCharacterBaselines() throws Exception {
        // Arrange
        byte[] pdfBytes = createSimplePdf(PageSize.LETTER, "ABCD");
        PdfReader reader = new PdfReader(pdfBytes);
        PdfReaderContentParser parser = new PdfReaderContentParser(reader);
        // The listener will perform assertions internally as it processes the content.
        RenderListener listener = new CharacterPositionRenderListener();

        // Act & Assert
        // The processContent method triggers the listener, which contains the assertions.
        // An AssertionError will be thrown if character baselines are not contiguous.
        parser.processContent(FIRST_PAGE, listener);
    }

    /**
     * Verifies that text extraction correctly handles Japanese characters.
     * This test was introduced to fix a bug where certain Unicode characters
     * caused an ArrayIndexOutOfBoundsException during text processing.
     */
    @Test
    public void getText_shouldCorrectlyExtractJapaneseText() throws Exception {
        // Arrange
        String resourceName = "japanese_text.pdf";
        PdfReader reader = TestResourceUtils.getResourceAsPdfReader(this, resourceName);

        // The expected text is the first line from the source PDF file.
        // It translates to: "Recent S&P 500 Dividend Aristocrats Index performance beats S&P 500 Index"
        String expectedFirstLine = "\u76f4\u8fd1\u306e\u0053\uff06\u0050\u0035\u0030\u0030\u914d\u5f53\u8cb4\u65cf\u6307\u6570\u306e\u30d1\u30d5\u30a9\u30fc\u30de\u30f3\u30b9\u306f\u0053\uff06\u0050\u0035\u0030\u0030\u6307\u6570\u3092\u4e0a\u56de\u308b";

        // Act
        String extractedText = PdfTextExtractor.getTextFromPage(reader, FIRST_PAGE, new SimpleTextExtractionStrategy());
        String actualFirstLine = extractedText.substring(0, extractedText.indexOf('\n'));

        // Assert
        Assert.assertEquals(expectedFirstLine, actualFirstLine);
    }

    /**
     * Verifies that the baseline of text rendered with a Type 3 font is calculated correctly.
     */
    @Test
    public void getBaseline_shouldReturnCorrectCoordinates_forType3Font() throws Exception {
        // Arrange
        String resourceName = "type3font_text.pdf";
        PdfReader reader = TestResourceUtils.getResourceAsPdfReader(this, resourceName);

        // Expected coordinates are determined by inspecting the source PDF.
        Vector expectedStartPoint = new Vector(20.3246f, 769.4974f, 1.0f);
        Vector expectedEndPoint = new Vector(151.22923f, 769.4974f, 1.0f);
        // A tolerance of 0.5 points is used due to potential floating-point inaccuracies.
        float delta = 0.5f;

        TextPositionRenderListener renderListener = new TextPositionRenderListener();
        PdfContentStreamProcessor processor = new PdfContentStreamProcessor(renderListener);
        PdfDictionary pageDic = reader.getPageN(FIRST_PAGE);
        PdfDictionary resourcesDic = pageDic.getAsDict(PdfName.RESOURCES);

        // Act
        processor.processContent(ContentByteUtils.getContentBytesForPage(reader, FIRST_PAGE), resourcesDic);
        LineSegment actualBaseline = renderListener.getLineSegments().get(FIRST_ELEMENT_INDEX);
        Vector actualStartPoint = actualBaseline.getStartPoint();
        Vector actualEndPoint = actualBaseline.getEndPoint();

        // Assert
        Assert.assertEquals("Start point X-coordinate mismatch.", expectedStartPoint.get(Vector.I1), actualStartPoint.get(Vector.I1), delta);
        Assert.assertEquals("Start point Y-coordinate mismatch.", expectedStartPoint.get(Vector.I2), actualStartPoint.get(Vector.I2), delta);
        Assert.assertEquals("End point X-coordinate mismatch.", expectedEndPoint.get(Vector.I1), actualEndPoint.get(Vector.I1), delta);
        Assert.assertEquals("End point Y-coordinate mismatch.", expectedEndPoint.get(Vector.I2), actualEndPoint.get(Vector.I2), delta);
    }

    /**
     * A render listener that captures the baseline of each text object.
     */
    private static class TextPositionRenderListener implements RenderListener {
        private final List<LineSegment> lineSegments = new ArrayList<>();

        public List<LineSegment> getLineSegments() {
            return lineSegments;
        }

        @Override
        public void renderText(TextRenderInfo renderInfo) {
            lineSegments.add(renderInfo.getBaseline());
        }

        @Override
        public void beginTextBlock() {}

        @Override
        public void endTextBlock() {}

        @Override
        public void renderImage(ImageRenderInfo renderInfo) {}
    }

    /**
     * A text extraction strategy that verifies the contiguity of character positions.
     * It asserts that the end of one character's baseline is the start of the next.
     */
    private static class CharacterPositionRenderListener implements TextExtractionStrategy {
        @Override
        public void renderText(TextRenderInfo renderInfo) {
            List<TextRenderInfo> characterRenderInfos = renderInfo.getCharacterRenderInfos();
            if (characterRenderInfos.size() <= 1) {
                return;
            }

            TextRenderInfo previousCharInfo = characterRenderInfos.get(0);
            for (int i = 1; i < characterRenderInfos.size(); i++) {
                TextRenderInfo currentCharInfo = characterRenderInfos.get(i);
                Vector previousEndPoint = previousCharInfo.getBaseline().getEndPoint();
                Vector currentStartPoint = currentCharInfo.getBaseline().getStartPoint();

                assertVectorsEqual(currentCharInfo.getText(), previousEndPoint, currentStartPoint);
                previousCharInfo = currentCharInfo;
            }
        }

        private void assertVectorsEqual(String message, Vector v1, Vector v2) {
            // A tolerance of 1/72nd of an inch (1 point) is acceptable for character placement.
            float delta = 1 / 72f;
            Assert.assertEquals("X-coordinate mismatch for character: " + message, v1.get(Vector.I1), v2.get(Vector.I1), delta);
            Assert.assertEquals("Y-coordinate mismatch for character: " + message, v1.get(Vector.I2), v2.get(Vector.I2), delta);
        }

        @Override
        public void beginTextBlock() {}

        @Override
        public void endTextBlock() {}

        @Override
        public void renderImage(ImageRenderInfo renderInfo) {}

        @Override
        public String getResultantText() {
            return null;
        }
    }

    /**
     * Creates a simple PDF document in memory containing the specified text.
     *
     * @param pageSize The page size for the document.
     * @param text     One or more strings to add to the document, each on a new page.
     * @return A byte array representing the PDF file.
     * @throws Exception if document creation fails.
     */
    private byte[] createSimplePdf(Rectangle pageSize, final String... text) throws Exception {
        try (ByteArrayOutputStream byteStream = new ByteArrayOutputStream()) {
            Document document = new Document(pageSize);
            PdfWriter.getInstance(document, byteStream);
            document.open();
            for (String line : text) {
                document.add(new Paragraph(line));
                document.newPage();
            }
            document.close();
            return byteStream.toByteArray();
        }
    }
}