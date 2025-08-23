package com.itextpdf.text.error_messages;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * This test focuses on the argument validation of the 
 * {@link MessageLocalization#setLanguage(String, String)} method.
 */
// The original class name and inheritance are preserved as they might be part of a larger, generated test suite.
public class MessageLocalization_ESTestTest10 extends MessageLocalization_ESTest_scaffolding {

    /**
     * Verifies that calling setLanguage() with a null language argument 
     * throws an IllegalArgumentException.
     */
    @Test
    public void setLanguage_withNullLanguage_throwsIllegalArgumentException() throws IOException {
        // Arrange
        String expectedErrorMessage = "The language cannot be null.";
        String country = "US"; // A dummy value, as it's not relevant for this test.

        try {
            // Act
            MessageLocalization.setLanguage(null, country);
            
            // Assert
            fail("Expected an IllegalArgumentException to be thrown, but the method completed successfully.");
        } catch (IllegalArgumentException e) {
            // Assert
            assertEquals("The exception message did not match the expected value.",
                         expectedErrorMessage, e.getMessage());
        }
    }
}