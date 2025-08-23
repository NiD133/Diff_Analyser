package com.itextpdf.text.pdf;

import org.junit.Test;

/**
 * Test suite focusing on the behavior of the PdfDictionary class under exceptional conditions.
 */
public class PdfDictionaryTest {

    /**
     * Verifies that getKeys() throws a NullPointerException if the internal
     * map has been set to null.
     *
     * <p>This is a white-box test that simulates an invalid internal state.
     * The public API of PdfDictionary ensures its internal map is always initialized,
     * but this test ensures robustness against unexpected states that could arise,
     * for example, through reflection.</p>
     */
    @Test(expected = NullPointerException.class)
    public void getKeysShouldThrowNullPointerExceptionWhenInternalMapIsNull() {
        // Arrange: Create a PdfDictionary and manually set its internal state to be invalid.
        // The 'hashMap' field is protected, allowing this direct manipulation in the test.
        PdfDictionary dictionary = new PdfDictionary();
        dictionary.hashMap = null;

        // Act: Call the method under test. This is expected to throw the exception.
        dictionary.getKeys();

        // Assert: The test succeeds if a NullPointerException is thrown, as declared
        // by the @Test(expected = ...) annotation. No further assertions are needed.
    }
}