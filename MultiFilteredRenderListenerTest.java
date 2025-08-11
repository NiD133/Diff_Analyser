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
    Section 5 of the GNU Affero General Notices, as required under
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
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfReader;
import junit.framework.Assert;
import org.junit.Test;

import java.io.IOException;

/**
 * Tests for MultiFilteredRenderListener functionality.
 * This class tests the ability to extract text from specific regions of a PDF
 * using multiple filters simultaneously.
 */
public class MultiFilteredRenderListenerTest {

    private static final String TEST_PDF_FILENAME = "test.pdf";
    private static final int PAGE_NUMBER = 1;

    @Test
    public void testTextExtractionFromMultipleRegions() throws IOException {
        // Given: A PDF document and specific regions to extract text from
        PdfReader pdfReader = TestResourceUtils.getResourceAsPdfReader(this, TEST_PDF_FILENAME);
        
        TestRegion[] testRegions = createTestRegions();
        
        // When: Extract text from multiple regions using MultiFilteredRenderListener
        String[] actualTexts = extractTextFromRegions(pdfReader, testRegions);
        
        // Then: Verify extracted text matches expected content
        verifyExtractedTexts(actualTexts, testRegions);
    }

    @Test
    public void testMultipleFiltersForSingleExtractionStrategy() throws IOException {
        // Given: A PDF document and multiple overlapping regions
        PdfReader pdfReader = TestResourceUtils.getResourceAsPdfReader(this, TEST_PDF_FILENAME);
        Rectangle[] overlappingRegions = createOverlappingRegions();
        
        // When: Apply multiple filters to a single extraction strategy
        String actualText = extractTextWithMultipleFilters(pdfReader, overlappingRegions);
        
        // Then: Compare with reference implementation using FilteredTextRenderListener
        String expectedText = extractTextUsingReferenceImplementation(pdfReader, overlappingRegions);
        
        Assert.assertEquals("Text extracted with multiple filters should match reference implementation", 
                           expectedText, actualText);
    }

    /**
     * Creates test regions with their expected text content.
     */
    private TestRegion[] createTestRegions() {
        return new TestRegion[] {
            new TestRegion(
                new Rectangle(90, 605, 220, 581),
                "PostScript Compatibility"
            ),
            new TestRegion(
                new Rectangle(80, 578, 450, 486),
                "Because the PostScript language does not support the transparent imaging \n" +
                "model, PDF 1.4 consumer applications must have some means for converting the \n" +
                "appearance of a document that uses transparency to a purely opaque description \n" +
                "for printing on PostScript output devices. Similar techniques can also be used to \n" +
                "convert such documents to a form that can be correctly viewed by PDF 1.3 and \n" +
                "earlier consumers. "
            ),
            new TestRegion(
                new Rectangle(103, 196, 460, 143),
                "Otherwise, flatten the colors to some assumed device color space with pre-\n" +
                "determined calibration. In the generated PostScript output, paint the flattened \n" +
                "colors in a CIE-based color space having that calibration. "
            )
        };
    }

    /**
     * Creates multiple overlapping regions for testing multiple filters on single strategy.
     */
    private Rectangle[] createOverlappingRegions() {
        return new Rectangle[] {
            new Rectangle(0, 0, 500, 650),    // Full page region
            new Rectangle(0, 0, 400, 400),    // Top-left quadrant
            new Rectangle(200, 200, 500, 600), // Bottom-right area
            new Rectangle(100, 100, 450, 400)  // Center area
        };
    }

    /**
     * Extracts text from multiple regions using MultiFilteredRenderListener.
     */
    private String[] extractTextFromRegions(PdfReader pdfReader, TestRegion[] testRegions) throws IOException {
        MultiFilteredRenderListener multiListener = new MultiFilteredRenderListener();
        LocationTextExtractionStrategy[] extractionStrategies = new LocationTextExtractionStrategy[testRegions.length];
        
        // Attach extraction strategy for each region
        for (int i = 0; i < testRegions.length; i++) {
            RegionTextRenderFilter regionFilter = new RegionTextRenderFilter(testRegions[i].getRegion());
            extractionStrategies[i] = (LocationTextExtractionStrategy) 
                multiListener.attachRenderListener(new LocationTextExtractionStrategy(), regionFilter);
        }
        
        // Process the PDF content
        new PdfReaderContentParser(pdfReader).processContent(PAGE_NUMBER, multiListener);
        
        // Collect results
        String[] results = new String[testRegions.length];
        for (int i = 0; i < testRegions.length; i++) {
            results[i] = extractionStrategies[i].getResultantText();
        }
        
        return results;
    }

    /**
     * Extracts text using multiple filters applied to a single extraction strategy.
     */
    private String extractTextWithMultipleFilters(PdfReader pdfReader, Rectangle[] regions) throws IOException {
        RegionTextRenderFilter[] regionFilters = createRegionFilters(regions);
        
        MultiFilteredRenderListener multiListener = new MultiFilteredRenderListener();
        LocationTextExtractionStrategy extractionStrategy = (LocationTextExtractionStrategy) 
            multiListener.attachRenderListener(new LocationTextExtractionStrategy(), regionFilters);
        
        new PdfReaderContentParser(pdfReader).processContent(PAGE_NUMBER, multiListener);
        
        return extractionStrategy.getResultantText();
    }

    /**
     * Extracts text using the reference FilteredTextRenderListener for comparison.
     */
    private String extractTextUsingReferenceImplementation(PdfReader pdfReader, Rectangle[] regions) throws IOException {
        RegionTextRenderFilter[] regionFilters = createRegionFilters(regions);
        return PdfTextExtractor.getTextFromPage(pdfReader, PAGE_NUMBER, 
            new FilteredTextRenderListener(new LocationTextExtractionStrategy(), regionFilters));
    }

    /**
     * Creates region filters from rectangles.
     */
    private RegionTextRenderFilter[] createRegionFilters(Rectangle[] regions) {
        RegionTextRenderFilter[] filters = new RegionTextRenderFilter[regions.length];
        for (int i = 0; i < regions.length; i++) {
            filters[i] = new RegionTextRenderFilter(regions[i]);
        }
        return filters;
    }

    /**
     * Verifies that extracted texts match expected content.
     */
    private void verifyExtractedTexts(String[] actualTexts, TestRegion[] testRegions) {
        Assert.assertEquals("Number of extracted texts should match number of test regions", 
                           testRegions.length, actualTexts.length);
        
        for (int i = 0; i < testRegions.length; i++) {
            Assert.assertEquals(
                String.format("Text extracted from region %d should match expected content", i),
                testRegions[i].getExpectedText(), 
                actualTexts[i]
            );
        }
    }

    /**
     * Helper class to encapsulate test region data and expected results.
     */
    private static class TestRegion {
        private final Rectangle region;
        private final String expectedText;

        public TestRegion(Rectangle region, String expectedText) {
            this.region = region;
            this.expectedText = expectedText;
        }

        public Rectangle getRegion() {
            return region;
        }

        public String getExpectedText() {
            return expectedText;
        }
    }
}