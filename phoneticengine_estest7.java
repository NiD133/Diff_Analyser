package org.apache.commons.codec.language.bm;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class PhoneticEngine_ESTestTest7 extends PhoneticEngine_ESTest_scaffolding {

    /**
     * Tests that the encode() method throws an IllegalArgumentException if the
     * PhoneticEngine was constructed with a negative value for maxPhonemes.
     *
     * The constructor itself does not validate the parameter, so the failure
     * is expected during the encoding process.
     */
    @Test
    public void encodeShouldThrowExceptionWhenEngineConfiguredWithNegativeMaxPhonemes() {
        // Arrange: Create a PhoneticEngine with an invalid, negative maxPhonemes value.
        final int negativeMaxPhonemes = -1;
        final PhoneticEngine engine = new PhoneticEngine(NameType.ASHKENAZI,
                                                         RuleType.APPROX,
                                                         false,
                                                         negativeMaxPhonemes);

        // Act & Assert: Attempting to encode should trigger the exception.
        try {
            engine.encode("test");
            fail("Expected an IllegalArgumentException because maxPhonemes is negative.");
        } catch (final IllegalArgumentException e) {
            // Verify that the exception is the one we expect, which is thrown when
            // attempting to create a collection with a negative initial capacity.
            final String expectedMessage = "Illegal initial capacity: " + negativeMaxPhonemes;
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}