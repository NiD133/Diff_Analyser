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
 * Verifies that PNG images with different ICC profile configurations are embedded correctly
 * by rendering a single-image PDF and comparing it against a reference PDF.
 */
public class PdfImageTest {

    private static final String OUTPUT_DIR = "./target/com/itextpdf/test/pdf/PdfImageTest/";
    private static final String RESOURCE_DIR = "./src/test/resources/com/itextpdf/text/pdf/PdfImageTest/";
    private static final String CMP_PREFIX = "cmp_";
    private static final String DIFF_PREFIX = "diff_";

    private static final CompareTool COMPARE_TOOL = new CompareTool();

    @BeforeClass
    public static void setUp() {
        new File(OUTPUT_DIR).mkdirs();
    }

    @Test
    public void pngColorProfileTest() throws DocumentException, InterruptedException, IOException {
        renderAndCompareSingleImagePdf("pngColorProfileImage.pdf", "test_icc.png");
    }

    @Test
    public void pngColorProfilePalletTest() throws DocumentException, InterruptedException, IOException {
        renderAndCompareSingleImagePdf("pngColorProfilePalletImage.pdf", "test_icc_pallet.png");
    }

    @Test
    public void pngIncorrectColorProfileTest() throws DocumentException, InterruptedException, IOException {
        renderAndCompareSingleImagePdf("pngIncorrectProfileImage.pdf", "test_incorrect_icc.png");
    }

    /**
     * Renders a PDF that contains a single image scaled to the document's content area,
     * then compares the output PDF with the reference PDF.
     */
    private void renderAndCompareSingleImagePdf(String outputFileName, String imageFileName)
            throws IOException, DocumentException, InterruptedException {

        final File outFile = new File(OUTPUT_DIR, outputFileName);
        final File cmpFile = new File(RESOURCE_DIR, CMP_PREFIX + outputFileName);
        final File imageFile = new File(RESOURCE_DIR, imageFileName);

        Assert.assertTrue("Test resource missing: " + imageFile.getAbsolutePath(), imageFile.exists());
        Assert.assertTrue("Reference PDF missing: " + cmpFile.getAbsolutePath(), cmpFile.exists());

        Document document = new Document();
        PdfWriter writer = null;
        try {
            writer = PdfWriter.getInstance(document, new FileOutputStream(outFile));
            document.open();

            Image image = Image.getInstance(imageFile.getAbsolutePath());
            scaleToContentArea(image, document);
            document.add(image);
        } finally {
            if (document.isOpen()) {
                document.close();
            }
            if (writer != null) {
                writer.close();
            }
        }

        String diffPrefix = DIFF_PREFIX + outputFileName + "_";
        String compareResult = COMPARE_TOOL.compareByContent(
                outFile.getAbsolutePath(),
                cmpFile.getAbsolutePath(),
                OUTPUT_DIR,
                diffPrefix);

        Assert.assertNull("Generated PDF differs from reference: " + compareResult, compareResult);
    }

    /**
     * Scales the image to fit within the document's content area (inside margins).
     */
    private static void scaleToContentArea(Image image, Document document) {
        image.scaleToFit(contentAreaOf(document));
    }

    /**
     * Returns a rectangle representing the content area of the document (page minus margins).
     */
    private static Rectangle contentAreaOf(Document document) {
        return new Rectangle(document.left(), document.bottom(), document.right(), document.top());
    }
}