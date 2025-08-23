package com.itextpdf.text.pdf;

import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import static org.junit.Assert.assertTrue;

/**
 * Test suite for the PdfDictionary class.
 */
public class PdfDictionaryTest {

    /**
     * Tests that the toPdf() method correctly serializes a PdfDictionary
     * (using a PdfAction subclass) into its PDF string representation.
     */
    @Test
    public void toPdfForLaunchActionWritesCorrectPdfRepresentation() throws IOException {
        // ARRANGE
        // A PdfAction is a specific type of PdfDictionary. We create a "Launch" action
        // to have a dictionary with predictable content to test against.
        String application = "notepad.exe";
        String parameters = "log.txt";
        PdfAction launchAction = new PdfAction(application, parameters, "open", "C:/temp");

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // ACT
        // The toPdf method writes the dictionary's PDF representation to the stream.
        // A null PdfWriter is acceptable for this dictionary's serialization.
        launchAction.toPdf(null, outputStream);
        String actualPdfOutput = outputStream.toString();

        // ASSERT
        // Verify that the output string contains the essential elements of the
        // PDF dictionary for a "Launch" action. This is more robust than an
        // exact string match, which could fail on minor whitespace changes.
        assertTrue("Output should start with the dictionary delimiter '<<'", actualPdfOutput.startsWith("<<"));
        assertTrue("Output should be a valid dictionary structure", actualPdfOutput.contains("/S /Launch"));
        assertTrue("Output should contain the application file specification", actualPdfOutput.contains("/F (" + application + ")"));
        assertTrue("Output should contain the application parameters", actualPdfOutput.contains("/P (" + parameters + ")"));
        assertTrue("Output should end with the dictionary delimiter '>>'", actualPdfOutput.trim().endsWith(">>"));
    }
}