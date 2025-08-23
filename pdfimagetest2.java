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
 * Tests the creation of a PDF containing a single image.
 * This test suite verifies that different image types are correctly embedded into a PDF document.
 */
public class PdfImageTest {

    private static final String OUTPUT_FOLDER = "./target/com/itextpdf/test/pdf/PdfImageTest/";
    private static final String RESOURCE_FOLDER = "./src/test/resources/com/itextpdf/text/pdf/PdfImageTest/";

    @BeforeClass
    public static void setUp() {
        new File(OUTPUT_FOLDER).mkdirs();
    }

    /**
     * Verifies that a PNG image with a color profile and palette is correctly added to a PDF.
     */
    @Test
    public void testAddPngWithColorProfileAndPalette() throws DocumentException, IOException, InterruptedException {
        // Arrange
        String outputPdfFileName = "pngWithColorProfileAndPalette.pdf";
        String inputImageName = "test_icc_pallet.png";
        String expectedPdfFileName = "cmp_pngColorProfilePalletImage.pdf";

        // Act
        createPdfWithSingleImage(outputPdfFileName, inputImageName);

        // Assert
        assertPdfEquals(outputPdfFileName, expectedPdfFileName);
    }

    /**
     * Creates a PDF document containing a single, page-sized image.
     *
     * @param outputPdfFileName The name for the generated PDF file.
     * @param inputImageName    The name of the image file from the resource folder to add to the PDF.
     */
    private void createPdfWithSingleImage(String outputPdfFileName, String inputImageName) throws IOException, DocumentException {
        String outputPdfPath = OUTPUT_FOLDER + outputPdfFileName;
        String imagePath = RESOURCE_FOLDER + inputImageName;

        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(outputPdfPath));
        document.open();

        Image image = Image.getInstance(imagePath);
        // Scale the image to fit the entire page
        image.scaleToFit(new Rectangle(document.left(), document.bottom(), document.right(), document.top()));
        document.add(image);

        document.close();
        writer.close();
    }

    /**
     * Asserts that a generated PDF is identical to an expected reference PDF.
     *
     * @param actualPdfFileName   The file name of the generated PDF in the output folder.
     * @param expectedPdfFileName The file name of the reference PDF in the resource folder.
     */
    private void assertPdfEquals(String actualPdfFileName, String expectedPdfFileName) throws IOException, InterruptedException {
        String actualPdfPath = OUTPUT_FOLDER + actualPdfFileName;
        String expectedPdfPath = RESOURCE_FOLDER + expectedPdfFileName;
        String diffPrefix = "diff_" + actualPdfFileName + "_";

        CompareTool compareTool = new CompareTool();
        String comparisonResult = compareTool.compareByContent(actualPdfPath, expectedPdfPath, OUTPUT_FOLDER, diffPrefix);

        Assert.assertNull(
                "The generated PDF does not match the expected PDF. Comparison result: " + comparisonResult,
                comparisonResult
        );
    }
}