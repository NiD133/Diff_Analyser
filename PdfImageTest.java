/*
    This file is part of the iText (R) project.
    (License header omitted for brevity)
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
 * Tests the creation of PDFs containing PNG images with different types of color profiles.
 * The tests verify that iText correctly processes these images by comparing the output
 * against pre-defined reference PDFs.
 */
public class PdfImageTest {

    private static final String TARGET_DIR = "./target/com/itextpdf/test/pdf/PdfImageTest/";
    private static final String RESOURCES_DIR = "./src/test/resources/com/itextpdf/text/pdf/PdfImageTest/";

    @BeforeClass
    public static void setUp() {
        new File(TARGET_DIR).mkdirs();
    }

    @Test
    public void whenAddingPngWithValidColorProfile_thenPdfIsCorrect() throws DocumentException, InterruptedException, IOException {
        createPdfFromImageAndAssertCorrectness("pngColorProfileImage.pdf", "test_icc.png");
    }

    @Test
    public void whenAddingPngWithPalletizedColorProfile_thenPdfIsCorrect() throws DocumentException, InterruptedException, IOException {
        createPdfFromImageAndAssertCorrectness("pngColorProfilePalletImage.pdf", "test_icc_pallet.png");
    }

    @Test
    public void whenAddingPngWithIncorrectColorProfile_thenPdfIsCorrect() throws DocumentException, InterruptedException, IOException {
        createPdfFromImageAndAssertCorrectness("pngIncorrectProfileImage.pdf", "test_incorrect_icc.png");
    }

    /**
     * Creates a PDF document with a single, scaled image and compares it against a reference PDF.
     * This helper method encapsulates the common test logic of PDF generation and comparison.
     *
     * @param outputPdfName   The file name for the generated PDF.
     * @param sourceImageName The file name of the image to add to the PDF.
     * @throws IOException          If an I/O error occurs.
     * @throws DocumentException    If a PDF document error occurs.
     * @throws InterruptedException If the comparison process is interrupted.
     */
    private void createPdfFromImageAndAssertCorrectness(String outputPdfName, String sourceImageName)
            throws IOException, DocumentException, InterruptedException {
        // ARRANGE: Define paths for input image, output PDF, and reference PDF.
        String actualPdfPath = TARGET_DIR + outputPdfName;
        String expectedPdfPath = RESOURCES_DIR + "cmp_" + outputPdfName;
        String sourceImagePath = RESOURCES_DIR + sourceImageName;

        // ACT: Create a new PDF document and add the image to it.
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(actualPdfPath));
        document.open();

        Image image = Image.getInstance(sourceImagePath);
        image.scaleToFit(new Rectangle(document.left(), document.bottom(), document.right(), document.top()));
        document.add(image);

        document.close();
        writer.close();

        // ASSERT: Verify that the generated PDF's content matches the expected reference PDF.
        String diffFilesPrefix = "diff_" + outputPdfName + "_";
        CompareTool compareTool = new CompareTool();
        String errorMessage = compareTool.compareByContent(actualPdfPath, expectedPdfPath, TARGET_DIR, diffFilesPrefix);

        Assert.assertNull("The generated PDF '" + outputPdfName + "' does not match the expected reference PDF.", errorMessage);
    }
}