package org.apache.commons.codec.language;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.commons.codec.AbstractStringEncoderTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link Metaphone} encoder.
 */
public class MetaphoneTest extends AbstractStringEncoderTest<Metaphone> {

    @Override
    protected Metaphone createStringEncoder() {
        return new Metaphone();
    }

    @Test
    void metaphoneShouldTruncateCodeExceedingDefaultMaxLength() {
        // The Metaphone class has a default maximum code length of 4.
        // The full, untruncated Metaphone code for "AXEAXE" is "AKSKS".
        // This test verifies that the output is correctly truncated to "AKSK".

        // Arrange
        final String input = "AXEAXE";
        final String expectedCode = "AKSK";

        // Act
        final String actualCode = getStringEncoder().metaphone(input);

        // Assert
        assertEquals(expectedCode, actualCode,
            "Metaphone code should be truncated to the default max length of 4.");
    }
}