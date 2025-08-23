package org.apache.commons.codec.language.bm;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.CoreMatchers.is;

/**
 * Test suite for the PhoneticEngine class, focusing on edge cases and invalid inputs.
 */
public class PhoneticEngineTest {

    // A JUnit 4 rule for asserting that a method throws an exception.
    // This is a modern alternative to the try-catch-fail pattern.
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    /**
     * Tests that the encode() method throws an IllegalArgumentException when the PhoneticEngine
     * is constructed with a negative 'maxPhonemes' value.
     *
     * The constructor itself does not validate this parameter, but the exception is triggered
     * later during the encoding process when a collection is initialized with the negative capacity.
     */
    @Test
    public void encodeShouldThrowExceptionWhenMaxPhonemesIsNegative() {
        // Arrange: Create a PhoneticEngine with an invalid maxPhonemes value.
        final int negativeMaxPhonemes = -1;
        final PhoneticEngine engine = new PhoneticEngine(
                NameType.GENERIC,
                RuleType.APPROX,
                true, // concat
                negativeMaxPhonemes
        );

        // Arrange: Define the expected exception details.
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(is("Illegal initial capacity: -1"));

        // Act: Call the method that is expected to trigger the exception.
        // The specific input string and language set do not matter for this test.
        engine.encode("any name", Languages.NO_LANGUAGES);
    }
}