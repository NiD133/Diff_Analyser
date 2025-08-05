package com.itextpdf.text.pdf;

import com.itextpdf.testutils.CompareTool;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;

public class PdfImageTest {
    private static final String RESOURCE_DIR = "com/itextpdf/text/pdf/PdfImageTest/";

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    @Test
    public void pngColorProfileTest() throws Exception {
        runImageTest("test_icc.png", "cmp_pngColorProfileImage.pdf");
    }

    @Test
    public void pngColorProfilePalletTest() throws Exception {
        runImageTest("test_icc_pallet.png", "cmp_pngColorProfilePalletImage.pdf");
    }

    @Test
    public void pngIncorrectColorProfileTest() throws Exception {
        runImageTest("test_incorrect_icc.png", "cmp_pngIncorrectProfileImage.pdf");
    }

    private void runImageTest(String imageName, String cmpFileName) 
            throws IOException, DocumentException {
        // Setup
        File outputFile = tempFolder.newFile();
        Image testImage = loadTestImage(imageName);
        
        // Execution
        createPdfWithImage(outputFile, testImage);
        
        // Verification
        assertPdfMatchesReference(outputFile, cmpFileName);
    }

    private Image loadTestImage(String imageName) throws IOException {
        String imagePath = RESOURCE_DIR + imageName;
        InputStream resource = getClass().getClassLoader().getResourceAsStream(imagePath);
        if (resource == null) {
            throw new IOException("Test image not found: " + imagePath);
        }
        return Image.getInstance(resource.readAllBytes());
    }

    private void createPdfWithImage(File outputFile, Image image) 
            throws DocumentException, IOException {
        try (Document document = new Document();
             FileOutputStream fos = new FileOutputStream(outputFile)) {
            PdfWriter.getInstance(document, fos);
            document.open();
            
            // Scale image to fit within page margins
            Rectangle pageBounds = new Rectangle(
                document.left(), document.bottom(), 
                document.right(), document.top()
            );
            image.scaleToFit(pageBounds);
            
            document.add(image);
        }
    }

    private void assertPdfMatchesReference(File actualPdf, String cmpFileName) 
            throws IOException, InterruptedException {
        String cmpPath = RESOURCE_DIR + cmpFileName;
        File referenceFile = getResourceFile(cmpPath);
        
        String diffPrefix = "diff_" + cmpFileName.replace(".pdf", "_");
        String differences = new CompareTool().compareByContent(
            actualPdf.getAbsolutePath(), 
            referenceFile.getAbsolutePath(), 
            tempFolder.getRoot().getAbsolutePath(), 
            diffPrefix
        );
        
        Assert.assertNull(
            "PDF differences found:\n" + differences,
            differences
        );
    }

    private File getResourceFile(String resourcePath) throws IOException {
        return new File(
            getClass().getClassLoader().getResource(resourcePath).getFile()
        );
    }
}