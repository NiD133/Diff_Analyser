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
 * Tests functionality related to adding images to a PDF document,
 * focusing on the underlying PdfImage object creation.
 */
public class PdfImageTest {

    private static final String OUTPUT_FOLDER = "./target/com/itextpdf/test/pdf/PdfImageTest/";
    private static final String RESOURCES_FOLDER = "./src/test/resources/com/itextpdf/text/pdf/PdfImageTest/";

    @BeforeClass
    public static void setUp() {
        new File(OUTPUT_FOLDER).mkdirs();
    }

    /**
     * Tests that a PNG image with an embedded ICC color profile is correctly
     * processed and added to a PDF document.
     *
     * @throws DocumentException      if a PDF document error occurs.
     * @throws InterruptedException   if the comparison process is interrupted.
     * @throws IOException            if an I/O error occurs.
     */
    @Test
    public void testAddPngWithIccProfile_GeneratesCorrectPdf() throws DocumentException, InterruptedException, IOException {
        // Define the source image and the expected output PDF for this test case
        final String sourceImageFileName = "test_icc.png";
        final String outputPdfFileName = "pngColorProfileImage.pdf";

        createPdfFromImageAndCompare(outputPdfFileName, sourceImageFileName);
    }

    /**
     * A helper method that performs a standard test routine:
     * 1. Creates a PDF from a single source image.
     * 2. The image is scaled to fit the page.
     * 3. The generated PDF is compared against a pre-existing reference file.
     *
     * @param outputPdfFileName The file name for the generated PDF.
     * @param sourceImageFileName The file name of the image to add to the PDF.
     */
    private void createPdfFromImageAndCompare(String outputPdfFileName, String sourceImageFileName)
            throws IOException, DocumentException, InterruptedException {
        // --- ARRANGE ---
        // Define paths for the files involved in the test.
        final String actualPdfPath = OUTPUT_FOLDER + outputPdfFileName;
        final String referencePdfPath = RESOURCES_FOLDER + "cmp_" + outputPdfFileName;
        final String sourceImagePath = RESOURCES_FOLDER + sourceImageFileName;
        final String diffFilePrefix = "diff_" + outputPdfFileName + "_";

        // --- ACT ---
        // Create a new PDF document, add the scaled image, and save the file.
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(actualPdfPath));
        document.open();

        Image image = Image.getInstance(sourceImagePath);
        image.scaleToFit(new Rectangle(document.left(), document.bottom(), document.right(), document.top()));
        document.add(image);

        document.close(); // This also closes the writer and the underlying stream.

        // --- ASSERT ---
        // Compare the generated PDF with the reference PDF.
        // The CompareTool returns null if the files are identical.
        CompareTool compareTool = new CompareTool();
        String comparisonResult = compareTool.compareByContent(actualPdfPath, referencePdfPath, OUTPUT_FOLDER, diffFilePrefix);

        Assert.assertNull(
                "The generated PDF is not identical to the reference PDF. See diff file with prefix: " + diffFilePrefix,
                comparisonResult
        );
    }
}