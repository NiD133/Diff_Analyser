package com.itextpdf.text.pdf;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Contains tests for the {@link PdfDictionary} class.
 */
public class PdfDictionaryTest {

    /**
     * Verifies that the checkType() method returns true when the dictionary's type
     * matches the type it was initialized with in the constructor.
     */
    @Test
    public void checkType_shouldReturnTrue_whenTypeMatchesConstructorArgument() {
        // Arrange: Create a PdfDictionary with a specific type. The constructor
        // is expected to set the "/Type" key within the dictionary.
        PdfName expectedType = PdfName.CFM;
        PdfDictionary dictionary = new PdfDictionary(expectedType);

        // Act: Call the method under test to check if the dictionary's type
        // matches the one it was created with.
        boolean typeMatches = dictionary.checkType(expectedType);

        // Assert: The result should be true, confirming the type was set and checked correctly.
        assertTrue("The dictionary type should match the one provided to its constructor.", typeMatches);
    }
}