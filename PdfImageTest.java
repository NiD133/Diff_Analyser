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
package com.itextpdf.text.pdf;

import com.itextpdf.testutils.CompareTool;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Test suite for PdfImage functionality, specifically testing PNG images with various color profiles.
 * These tests verify that PNG images with different color profile configurations are correctly
 * embedded in PDF documents and produce the expected output.
 */
public class PdfImageTest {
    
    // Test output directory where generated PDFs will be saved
    private static final String OUTPUT_DIRECTORY = "./target/com/itextpdf/test/pdf/PdfImageTest/";
    
    // Test resources directory containing reference PDFs and test images
    private static final String RESOURCES_DIRECTORY = "./src/test/resources/com/itextpdf/text/pdf/PdfImageTest/";
    
    // Test image file names
    private static final String PNG_WITH_ICC_PROFILE = "test_icc.png";
    private static final String PNG_WITH_ICC_PALETTE_PROFILE = "test_icc_pallet.png";
    private static final String PNG_WITH_INCORRECT_ICC_PROFILE = "test_incorrect_icc.png";
    
    // Expected output PDF file names
    private static final String PNG_ICC_PROFILE_OUTPUT = "pngColorProfileImage.pdf";
    private static final String PNG_ICC_PALETTE_OUTPUT = "pngColorProfilePalletImage.pdf";
    private static final String PNG_INCORRECT_ICC_OUTPUT = "pngIncorrectProfileImage.pdf";

    @BeforeClass
    public static void createOutputDirectory() {
        new File(OUTPUT_DIRECTORY).mkdirs();
    }

    /**
     * Tests that a PNG image with an ICC color profile is correctly embedded in a PDF.
     * This verifies that color profile information is preserved during PDF generation.
     */
    @Test
    public void shouldEmbedPngWithIccColorProfile() throws DocumentException, InterruptedException, IOException {
        createPdfWithImageAndCompareToExpected(PNG_ICC_PROFILE_OUTPUT, PNG_WITH_ICC_PROFILE);
    }

    /**
     * Tests that a PNG image with an ICC palette color profile is correctly embedded in a PDF.
     * This verifies that palette-based color profiles are handled properly.
     */
    @Test
    public void shouldEmbedPngWithIccPaletteColorProfile() throws DocumentException, InterruptedException, IOException {
        createPdfWithImageAndCompareToExpected(PNG_ICC_PALETTE_OUTPUT, PNG_WITH_ICC_PALETTE_PROFILE);
    }

    /**
     * Tests that a PNG image with an incorrect/malformed ICC color profile is handled gracefully.
     * This verifies that the PDF generation doesn't fail when encountering invalid color profiles.
     */
    @Test
    public void shouldHandlePngWithIncorrectIccColorProfile() throws DocumentException, InterruptedException, IOException {
        createPdfWithImageAndCompareToExpected(PNG_INCORRECT_ICC_OUTPUT, PNG_WITH_INCORRECT_ICC_PROFILE);
    }

    /**
     * Creates a PDF document containing the specified image and compares it to the expected output.
     * 
     * @param outputFileName The name of the PDF file to generate
     * @param imageFileName The name of the image file to embed in the PDF
     * @throws IOException If file operations fail
     * @throws DocumentException If PDF document creation fails
     * @throws InterruptedException If the comparison process is interrupted
     */
    private void createPdfWithImageAndCompareToExpected(String outputFileName, String imageFileName) 
            throws IOException, DocumentException, InterruptedException {
        
        // Setup file paths
        String generatedPdfPath = OUTPUT_DIRECTORY + outputFileName;
        String expectedPdfPath = RESOURCES_DIRECTORY + "cmp_" + outputFileName;
        String imagePath = RESOURCES_DIRECTORY + imageFileName;
        String differenceFilePrefix = "diff_" + outputFileName + "_";

        // Create PDF document with the test image
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(generatedPdfPath));

        document.open();
        
        // Load and scale the image to fit the document
        Image testImage = Image.getInstance(imagePath);
        Rectangle documentBounds = new Rectangle(
            document.left(), 
            document.bottom(), 
            document.right(), 
            document.top()
        );
        testImage.scaleToFit(documentBounds);
        
        document.add(testImage);
        document.close();
        writer.close();

        // Compare generated PDF with expected reference PDF
        CompareTool compareTool = new CompareTool();
        String comparisonResult = compareTool.compareByContent(
            generatedPdfPath, 
            expectedPdfPath, 
            OUTPUT_DIRECTORY, 
            differenceFilePrefix
        );
        
        // Assert that the PDFs are identical (null result means no differences found)
        Assert.assertNull(
            "Generated PDF does not match expected output. Check difference files for details.", 
            comparisonResult
        );
    }
}