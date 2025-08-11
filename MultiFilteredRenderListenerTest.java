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
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfReader;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * Tests the {@link MultiFilteredRenderListener} to ensure it correctly delegates
 * rendering events to multiple listeners based on specified filters.
 */
public class MultiFilteredRenderListenerTest {

    private static final String TEST_PDF = "test.pdf";
    private PdfReader pdfReader;

    @Before
    public void setUp() throws IOException {
        pdfReader = TestResourceUtils.getResourceAsPdfReader(this, TEST_PDF);
    }

    @After
    public void tearDown() {
        if (pdfReader != null) {
            pdfReader.close();
        }
    }

    /**
     * Tests that a MultiFilteredRenderListener can process a PDF page once and delegate
     * content from different, non-overlapping regions to separate, dedicated listeners.
     */
    @Test
    public void extractsTextFromDistinctRegionsIntoSeparateStrategies() throws IOException {
        // Arrange
        // Define the specific regions of the PDF to extract text from.
        final Rectangle titleRegion = new Rectangle(90, 605, 220, 581);
        final Rectangle firstParagraphRegion = new Rectangle(80, 578, 450, 486);
        final Rectangle secondParagraphRegion = new Rectangle(103, 196, 460, 143);

        // Define the expected text content for each region.
        final String expectedTitleText = "PostScript Compatibility";
        final String expectedFirstParagraphText = "Because the PostScript language does not support the transparent imaging \n" +
                "model, PDF 1.4 consumer applications must have some means for converting the \n" +
                "appearance of a document that uses transparency to a purely opaque description \n" +
                "for printing on PostScript output devices. Similar techniques can also be used to \n" +
                "convert such documents to a form that can be correctly viewed by PDF 1.3 and \n" +
                "earlier consumers. ";
        final String expectedSecondParagraphText = "Otherwise, flatten the colors to some assumed device color space with pre-\n" +
                "determined calibration. In the generated PostScript output, paint the flattened \n" +
                "colors in a CIE-based color space having that calibration. ";

        MultiFilteredRenderListener multiListener = new MultiFilteredRenderListener();

        // Attach a separate extraction strategy for each region of interest.
        LocationTextExtractionStrategy titleStrategy = multiListener.attachRenderListener(
                new LocationTextExtractionStrategy(), new RegionTextRenderFilter(titleRegion));
        LocationTextExtractionStrategy firstParagraphStrategy = multiListener.attachRenderListener(
                new LocationTextExtractionStrategy(), new RegionTextRenderFilter(firstParagraphRegion));
        LocationTextExtractionStrategy secondParagraphStrategy = multiListener.attachRenderListener(
                new LocationTextExtractionStrategy(), new RegionTextRenderFilter(secondParagraphRegion));

        // Act
        // Process the first page of the PDF, which will trigger the appropriate listeners.
        new PdfReaderContentParser(pdfReader).processContent(1, multiListener);

        // Assert
        // Verify that each strategy has captured the correct text from its designated region.
        assertEquals("Title text should be extracted correctly.",
                expectedTitleText, titleStrategy.getResultantText());
        assertEquals("First paragraph text should be extracted correctly.",
                expectedFirstParagraphText, firstParagraphStrategy.getResultantText());
        assertEquals("Second paragraph text should be extracted correctly.",
                expectedSecondParagraphText, secondParagraphStrategy.getResultantText());
    }

    /**
     * Tests that a single listener attached with multiple filters is only invoked
     * when ALL filters are satisfied (i.e., for content in the intersection of all filter regions).
     * This behavior is verified by comparing the result with FilteredTextRenderListener,
     * which serves as the baseline for this type of filtering.
     */
    @Test
    public void extractsTextFromIntersectionOfMultipleOverlappingFilters() throws IOException {
        // Arrange
        // Define several overlapping regions. Text should only be extracted from their intersection.
        final Rectangle[] regions = {
                new Rectangle(0, 0, 500, 650),
                new Rectangle(0, 0, 400, 400),
                new Rectangle(200, 200, 500, 600),
                new Rectangle(100, 100, 450, 400)
        };

        RegionTextRenderFilter[] regionFilters = new RegionTextRenderFilter[regions.length];
        for (int i = 0; i < regions.length; i++) {
            regionFilters[i] = new RegionTextRenderFilter(regions[i]);
        }

        // Create the listener and attach a single strategy with ALL the filters.
        MultiFilteredRenderListener multiListener = new MultiFilteredRenderListener();
        LocationTextExtractionStrategy extractionStrategy = multiListener.attachRenderListener(
                new LocationTextExtractionStrategy(), regionFilters);

        // Act
        // Process the content; the listener will only be triggered for text inside all specified regions.
        new PdfReaderContentParser(pdfReader).processContent(1, multiListener);
        String actualText = extractionStrategy.getResultantText();

        // Assert
        // The result must be identical to using the standard FilteredTextRenderListener with the same set of filters.
        String expectedText = PdfTextExtractor.getTextFromPage(pdfReader, 1,
                new FilteredTextRenderListener(new LocationTextExtractionStrategy(), regionFilters));

        assertEquals("Text from the intersection of filters should match the baseline.",
                expectedText, actualText);
    }
}