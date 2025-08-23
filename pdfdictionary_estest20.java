package com.itextpdf.text.pdf;

import org.junit.Test;

/**
 * This test suite contains tests for the PdfDictionary class.
 * The original test was auto-generated and has been refactored for clarity.
 */
// Note: The original test class name, inheritance, and EvoSuite runner annotations are preserved
// as they may be required by the project's existing test infrastructure.
public class PdfDictionary_ESTestTest20 extends PdfDictionary_ESTest_scaffolding {

    /**
     * Verifies that getAsName() throws a NullPointerException if the dictionary's internal map is null.
     *
     * This test simulates a corrupted object state to ensure the method fails predictably.
     * This state should not be reachable through normal public API usage, but testing it
     * guards against unexpected behavior if the object's internal invariants are violated.
     */
    @Test(timeout = 4000, expected = NullPointerException.class)
    public void getAsNameShouldThrowNullPointerExceptionWhenInternalMapIsNull() {
        // Arrange: Create a PdfDictionary and corrupt its internal state by nullifying its hash map.
        PdfDictionary dictionary = new PdfDictionary();
        dictionary.hashMap = null;

        // Act: Attempt to retrieve a value using getAsName().
        // This is expected to throw a NullPointerException because the method will try to
        // access the null internal map. The key passed as an argument is irrelevant to this failure.
        dictionary.getAsName(PdfName.A);
    }
}