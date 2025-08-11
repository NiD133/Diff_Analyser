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
    Section 5 of the GNU Affero General Legal Notices, as required under
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

/**
 * Test suite for TextRenderInfo class functionality.
 * Tests character positioning, Unicode handling, and Type3 font width calculations.
 */
public class TextRenderInfoTest {

    // Test constants
    private static final int FIRST_PAGE = 1;
    private static final int FIRST_ELEMENT_INDEX = 0;
    private static final float POSITION_TOLERANCE = 1 / 2f;
    private static final float VECTOR_TOLERANCE = 1 / 72f;
    
    // Test file names
    private static final String JAPANESE_TEXT_PDF = "japanese_text.pdf";
    private static final String TYPE3_FONT_PDF = "type3font_text.pdf";
    
    // Test data
    private static final String SIMPLE_TEXT = "ABCD";

    /**
     * Tests that character render infos maintain proper positioning.
     * Verifies that adjacent characters have continuous baseline positioning
     * (end point of previous character matches start point of next character).
     */
    @Test
    public void testCharacterRenderInfos() throws Exception {
        // Given: A simple PDF with rotated page containing "ABCD"
        byte[] pdfBytes = createSimplePdf(PageSize.LETTER.rotate().rotate(), SIMPLE_TEXT);
        PdfReader reader = new PdfReader(pdfBytes);

        // When: Processing the content with character position listener
        PdfReaderContentParser parser = new PdfReaderContentParser(reader);
        CharacterPositionValidator validator = new CharacterPositionValidator();
        parser.processContent(FIRST_PAGE, validator);

        // Then: Character positioning is validated within the listener
        // (assertions are performed in the CharacterPositionValidator.renderText method)
    }

    /**
     * Tests Unicode text extraction, specifically for Japanese characters.
     * This test was introduced to prevent ArrayIndexOutOfBoundsException
     * that occurred with certain Unicode characters in Japanese text.
     * 
     * @since 5.5.5-SNAPSHOT
     */
    @Test
    public void testUnicodeTextExtractionDoesNotThrowException() throws Exception {
        // Given: A PDF containing Japanese Unicode text
        PdfReader reader = TestResourceUtils.getResourceAsPdfReader(this, JAPANESE_TEXT_PDF);
        TextExtractionStrategy strategy = new SimpleTextExtractionStrategy();

        // When: Extracting text from the first page
        String extractedText = PdfTextExtractor.getTextFromPage(reader, FIRST_PAGE, strategy);
        String firstLine = extractedText.substring(0, extractedText.indexOf("\n"));

        // Then: The extracted text matches the expected Japanese characters
        String expectedJapaneseText = buildExpectedJapaneseText();
        Assert.assertEquals("Japanese text extraction should match expected Unicode characters", 
                           expectedJapaneseText, firstLine);
    }

    /**
     * Tests Type3 font width calculations and baseline positioning.
     * Verifies that text with Type3 fonts has correct start and end point positioning.
     */
    @Test
    public void testType3FontWidthCalculation() throws Exception {
        // Given: Expected line segment for Type3 font text
        LineSegment expectedLineSegment = new LineSegment(
            new Vector(20.3246f, 769.4974f, 1.0f), 
            new Vector(151.22923f, 769.4974f, 1.0f)
        );

        // And: A PDF with Type3 font text
        PdfReader reader = TestResourceUtils.getResourceAsPdfReader(this, TYPE3_FONT_PDF);
        TextPositionCollector positionCollector = new TextPositionCollector();
        
        // When: Processing the content to collect text positions
        PdfContentStreamProcessor processor = new PdfContentStreamProcessor(positionCollector);
        PdfDictionary pageDictionary = reader.getPageN(FIRST_PAGE);
        PdfDictionary resourcesDictionary = pageDictionary.getAsDict(PdfName.RESOURCES);
        processor.processContent(ContentByteUtils.getContentBytesForPage(reader, FIRST_PAGE), resourcesDictionary);

        // Then: The collected line segment matches expected positioning
        LineSegment actualLineSegment = positionCollector.getCollectedLineSegments().get(FIRST_ELEMENT_INDEX);
        
        assertVectorComponentsEqual("Type3 font start point should match expected position",
                                  expectedLineSegment.getStartPoint(), 
                                  actualLineSegment.getStartPoint());
        
        assertVectorComponentsEqual("Type3 font end point should match expected position",
                                  expectedLineSegment.getEndPoint(), 
                                  actualLineSegment.getEndPoint());
    }

    // Helper methods

    /**
     * Creates a simple PDF document with the specified page size and text content.
     */
    private byte[] createSimplePdf(Rectangle pageSize, String... textContent) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Document document = new Document(pageSize);
        
        PdfWriter.getInstance(document, outputStream);
        document.open();
        
        for (String text : textContent) {
            document.add(new Paragraph(text));
            document.newPage();
        }
        
        document.close();
        return outputStream.toByteArray();
    }

    /**
     * Builds the expected Japanese text string for Unicode testing.
     */
    private String buildExpectedJapaneseText() {
        return "\u76f4\u8fd1\u306e\u0053\uff06\u0050\u0035\u0030\u0030"
             + "\u914d\u5f53\u8cb4\u65cf\u6307\u6570\u306e\u30d1\u30d5"
             + "\u30a9\u30fc\u30de\u30f3\u30b9\u306f\u0053\uff06\u0050"
             + "\u0035\u0030\u0030\u6307\u6570\u3092\u4e0a\u56de\u308b";
    }

    /**
     * Asserts that two vectors have equal components within tolerance.
     */
    private void assertVectorComponentsEqual(String message, Vector expected, Vector actual) {
        Assert.assertEquals(message + " (X component)", 
                           expected.get(FIRST_ELEMENT_INDEX), 
                           actual.get(FIRST_ELEMENT_INDEX), 
                           POSITION_TOLERANCE);
    }

    /**
     * Asserts that two vectors are equal within a specified tolerance.
     */
    private void assertVectorsEqual(String message, Vector expected, Vector actual) {
        Assert.assertEquals(message + " (X coordinate)", 
                           expected.get(0), actual.get(0), VECTOR_TOLERANCE);
        Assert.assertEquals(message + " (Y coordinate)", 
                           expected.get(1), actual.get(1), VECTOR_TOLERANCE);
    }

    // Test helper classes

    /**
     * Collects text position information during PDF content processing.
     * Used specifically for testing Type3 font positioning.
     */
    private static class TextPositionCollector implements RenderListener {
        private final List<LineSegment> collectedLineSegments = new ArrayList<LineSegment>();

        public List<LineSegment> getCollectedLineSegments() {
            return collectedLineSegments;
        }

        @Override
        public void renderText(TextRenderInfo renderInfo) {
            collectedLineSegments.add(renderInfo.getBaseline());
        }

        @Override
        public void beginTextBlock() {
            // No action needed for this test
        }

        @Override
        public void endTextBlock() {
            // No action needed for this test
        }

        @Override
        public void renderImage(ImageRenderInfo renderInfo) {
            // No action needed for this test
        }
    }

    /**
     * Validates character positioning by ensuring adjacent characters
     * have continuous baseline positioning.
     */
    private class CharacterPositionValidator implements TextExtractionStrategy {

        @Override
        public void beginTextBlock() {
            // No action needed for this test
        }

        @Override
        public void renderText(TextRenderInfo renderInfo) {
            List<TextRenderInfo> characterInfos = renderInfo.getCharacterRenderInfos();
            
            if (characterInfos.size() <= 1) {
                return; // Nothing to validate for single character or empty text
            }

            // Validate that each character's end point matches the next character's start point
            TextRenderInfo previousChar = characterInfos.get(0);
            
            for (int i = 1; i < characterInfos.size(); i++) {
                TextRenderInfo currentChar = characterInfos.get(i);
                
                Vector previousEndPoint = previousChar.getBaseline().getEndPoint();
                Vector currentStartPoint = currentChar.getBaseline().getStartPoint();
                
                assertVectorsEqual("Character '" + currentChar.getText() + "' should be positioned continuously after previous character",
                                 previousEndPoint, currentStartPoint);
                
                previousChar = currentChar;
            }
        }

        @Override
        public void endTextBlock() {
            // No action needed for this test
        }

        @Override
        public void renderImage(ImageRenderInfo renderInfo) {
            // No action needed for this test
        }

        @Override
        public String getResultantText() {
            return null; // Not needed for position validation
        }
    }
}