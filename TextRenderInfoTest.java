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
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import org.junit.Assert;
import org.junit.Test;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;

public class TextRenderInfoTest {
    private static final int FIRST_PAGE = 1;
    private static final int FIRST_ELEMENT_INDEX = 0;
    private static final float FLOAT_TOLERANCE = 1 / 72f; // Tolerance for floating point comparisons

    @Test
    public void consecutiveCharacters_ShouldHaveMatchingEndAndStartPoints() throws Exception {
        String testContent = "ABCD";
        byte[] pdfBytes = createSimplePdf(PageSize.LETTER.rotate().rotate(), testContent);

        PdfReader pdfReader = new PdfReader(pdfBytes);
        PdfReaderContentParser parser = new PdfReaderContentParser(pdfReader);
        
        parser.processContent(FIRST_PAGE, new CharacterPositionValidator());
    }

    @Test
    public void extractJapaneseText_ShouldMatchExpected() throws Exception {
        String japanesePdf = "japanese_text.pdf";
        String expectedFirstLine = 
            "\u76f4\u8fd1\u306e\u0053\uff06\u0050\u0035\u0030\u0030" +
            "\u914d\u5f53\u8cb4\u65cf\u6307\u6570\u306e\u30d1\u30d5" +
            "\u30a9\u30fc\u30de\u30f3\u30b9\u306f\u0053\uff06\u0050" +
            "\u0035\u0030\u0030\u6307\u6570\u3092\u4e0a\u56de\u308b";

        PdfReader reader = TestResourceUtils.getResourceAsPdfReader(this, japanesePdf);
        String extractedText = PdfTextExtractor.getTextFromPage(reader, FIRST_PAGE, new SimpleTextExtractionStrategy());
        
        String actualFirstLine = extractedText.substring(0, extractedText.indexOf("\n"));
        Assert.assertEquals("Extracted Japanese text should match expected", expectedFirstLine, actualFirstLine);
    }

    @Test
    public void type3FontRendering_ShouldHaveCorrectBaselinePositions() throws Exception {
        String testPdf = "type3font_text.pdf";
        float expectedStartX = 20.3246f;
        float expectedEndX = 151.22923f;
        float tolerance = 0.5f; // 1/2 point tolerance

        PdfReader reader = TestResourceUtils.getResourceAsPdfReader(this, testPdf);
        TextPositionRecorder positionRecorder = new TextPositionRecorder();
        PdfContentStreamProcessor processor = new PdfContentStreamProcessor(positionRecorder);

        PdfDictionary pageDict = reader.getPageN(FIRST_PAGE);
        PdfDictionary resources = pageDict.getAsDict(PdfName.RESOURCES);
        processor.processContent(ContentByteUtils.getContentBytesForPage(reader, FIRST_PAGE), resources);

        LineSegment firstBaseline = positionRecorder.getLineSegments().get(FIRST_ELEMENT_INDEX);
        Assert.assertEquals("Baseline start X", expectedStartX, firstBaseline.getStartPoint().get(0), tolerance);
        Assert.assertEquals("Baseline end X", expectedEndX, firstBaseline.getEndPoint().get(0), tolerance);
    }

    // Helper class to validate character positioning
    private static class CharacterPositionValidator implements TextExtractionStrategy {
        @Override
        public void renderText(TextRenderInfo renderInfo) {
            List<TextRenderInfo> characters = renderInfo.getCharacterRenderInfos();
            TextRenderInfo previousChar = characters.get(0);

            for (int i = 1; i < characters.size(); i++) {
                TextRenderInfo currentChar = characters.get(i);
                Vector prevEnd = previousChar.getBaseline().getEndPoint();
                Vector currentStart = currentChar.getBaseline().getStartPoint();
                
                assertPointsMatch(currentChar.getText(), prevEnd, currentStart);
                previousChar = currentChar;
            }
        }

        private void assertPointsMatch(String context, Vector v1, Vector v2) {
            Assert.assertEquals(context + ": X coordinate mismatch", v1.get(0), v2.get(0), FLOAT_TOLERANCE);
            Assert.assertEquals(context + ": Y coordinate mismatch", v1.get(1), v2.get(1), FLOAT_TOLERANCE);
        }

        // Required interface methods - no action needed for this test
        @Override public void beginTextBlock() {}
        @Override public void endTextBlock() {}
        @Override public void renderImage(ImageRenderInfo renderInfo) {}
        @Override public String getResultantText() { return null; }
    }

    // Helper class to record text positions
    private static class TextPositionRecorder implements RenderListener {
        private final List<LineSegment> lineSegments = new ArrayList<>();

        public List<LineSegment> getLineSegments() {
            return lineSegments;
        }

        @Override
        public void renderText(TextRenderInfo renderInfo) {
            lineSegments.add(renderInfo.getBaseline());
        }

        // Required interface methods - no action needed for this test
        @Override public void beginTextBlock() {}
        @Override public void endTextBlock() {}
        @Override public void renderImage(ImageRenderInfo renderInfo) {}
    }

    private byte[] createSimplePdf(Rectangle pageSize, String... content) throws Exception {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        Document document = new Document(pageSize);
        PdfWriter.getInstance(document, byteStream);
        
        document.open();
        for (String text : content) {
            document.add(new Paragraph(text));
            document.newPage();
        }
        document.close();
        
        return byteStream.toByteArray();
    }
}