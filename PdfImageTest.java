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
 * Test suite for verifying PDF image handling in iText.
 */
public class PdfImageTest {

    private static final String TARGET_DIRECTORY = "./target/com/itextpdf/test/pdf/PdfImageTest/";
    private static final String SOURCE_DIRECTORY = "./src/test/resources/com/itextpdf/text/pdf/PdfImageTest/";

    /**
     * Sets up the test environment by creating necessary directories.
     */
    @BeforeClass
    public static void setUp() {
        new File(TARGET_DIRECTORY).mkdirs();
    }

    /**
     * Test for verifying PNG images with a color profile.
     */
    @Test
    public void testPngColorProfile() throws DocumentException, InterruptedException, IOException {
        verifyImageProcessing("pngColorProfileImage.pdf", "test_icc.png");
    }

    /**
     * Test for verifying PNG images with a color profile and palette.
     */
    @Test
    public void testPngColorProfilePalette() throws DocumentException, InterruptedException, IOException {
        verifyImageProcessing("pngColorProfilePalletImage.pdf", "test_icc_pallet.png");
    }

    /**
     * Test for verifying PNG images with an incorrect color profile.
     */
    @Test
    public void testPngIncorrectColorProfile() throws DocumentException, InterruptedException, IOException {
        verifyImageProcessing("pngIncorrectProfileImage.pdf", "test_incorrect_icc.png");
    }

    /**
     * Helper method to perform image processing and comparison.
     *
     * @param outputFileName Name of the output PDF file.
     * @param imageFileName  Name of the image file to be processed.
     */
    private void verifyImageProcessing(String outputFileName, String imageFileName) throws IOException, DocumentException, InterruptedException {
        String outputPath = TARGET_DIRECTORY + outputFileName;
        String comparisonPath = SOURCE_DIRECTORY + "cmp_" + outputFileName;
        String imagePath = SOURCE_DIRECTORY + imageFileName;
        String diffPrefix = "diff_" + outputFileName + "_";

        createPdfWithImage(outputPath, imagePath);
        comparePdfWithExpected(outputPath, comparisonPath, diffPrefix);
    }

    /**
     * Creates a PDF document with the specified image.
     *
     * @param outputPath Path where the PDF will be saved.
     * @param imagePath  Path of the image to be added to the PDF.
     */
    private void createPdfWithImage(String outputPath, String imagePath) throws DocumentException, IOException {
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(outputPath));

        document.open();
        Image image = Image.getInstance(imagePath);
        image.scaleToFit(new Rectangle(document.left(), document.bottom(), document.right(), document.top()));
        document.add(image);
        document.close();
        writer.close();
    }

    /**
     * Compares the generated PDF with the expected PDF.
     *
     * @param outputPath      Path of the generated PDF.
     * @param comparisonPath  Path of the expected PDF.
     * @param diffPrefix      Prefix for the diff file.
     */
    private void comparePdfWithExpected(String outputPath, String comparisonPath, String diffPrefix) throws IOException, InterruptedException {
        CompareTool compareTool = new CompareTool();
        String errorMessage = compareTool.compareByContent(outputPath, comparisonPath, TARGET_DIRECTORY, diffPrefix);
        Assert.assertNull("PDF content does not match expected output.", errorMessage);
    }
}