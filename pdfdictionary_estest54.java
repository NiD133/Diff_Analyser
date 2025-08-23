package com.itextpdf.text.pdf;

import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the {@link PdfDictionary} class, focusing on the isPages() method.
 */
public class PdfDictionaryTest {

    /**
     * Verifies that isPages() returns false for a dictionary that does not have
     * its /Type entry set to /Pages.
     * <p>
     * A newly created, empty dictionary is used as the test case.
     */
    @Test
    public void isPages_returnsFalse_forDictionaryWithoutPagesType() {
        // Arrange: Create a new, empty dictionary. By default, it has no /Type entry.
        PdfDictionary dictionary = new PdfDictionary();

        // Act: Check if the dictionary is of type /Pages.
        boolean isPages = dictionary.isPages();

        // Assert: The result should be false because the /Type is not /Pages.
        assertFalse("A dictionary without a /Type should not be identified as a Pages dictionary.", isPages);
    }

    /**
     * Verifies that isPages() returns true for a dictionary that has its /Type
     * entry explicitly set to /Pages.
     */
    @Test
    public void isPages_returnsTrue_forDictionaryWithPagesType() {
        // Arrange: Create a dictionary with its type explicitly set to PAGES.
        PdfDictionary pagesDictionary = new PdfDictionary(PdfDictionary.PAGES);

        // Act: Check if the dictionary is of type /Pages.
        boolean isPages = pagesDictionary.isPages();

        // Assert: The result should be true.
        assertTrue("A dictionary with /Type /Pages should be identified as a Pages dictionary.", isPages);
    }
}