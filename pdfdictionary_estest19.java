package com.itextpdf.text.pdf;

import org.junit.Test;
import static org.junit.Assert.fail;

/**
 * Contains unit tests for the {@link PdfDictionary} class, focusing on its
 * robustness and error handling in edge-case scenarios.
 */
public class PdfDictionaryTest {

    /**
     * Verifies that getAsNumber() throws a NullPointerException if the dictionary's
     * internal map is null.
     *
     * <p>This is a white-box test that simulates an invalid internal state. While this
     * state should not occur during normal operation, this test ensures that the method
     * fails predictably with a clear exception rather than causing a more obscure error
     * downstream. This guards against potential regressions or unexpected state corruption.
     */
    @Test
    public void getAsNumber_whenInternalMapIsNull_shouldThrowNullPointerException() {
        // Arrange: Create a PdfDictionary and force its internal map to be null to
        // simulate an invalid, corrupted state.
        PdfDictionary dictionary = new PdfDictionary();
        dictionary.hashMap = null; // This is the specific condition under test.

        // Act & Assert: Attempt to retrieve a value and verify that the expected exception is thrown.
        try {
            dictionary.getAsNumber(PdfName.FONT);
            fail("A NullPointerException was expected but was not thrown.");
        } catch (NullPointerException expected) {
            // Success: The expected exception was caught, confirming the method's behavior.
        }
    }
}