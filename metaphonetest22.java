package org.apache.commons.codec.language;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.commons.codec.AbstractStringEncoderTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link Metaphone} class, focusing on specific behaviors.
 */
public class MetaphoneTest extends AbstractStringEncoderTest<Metaphone> {

    @Override
    protected Metaphone createStringEncoder() {
        return new Metaphone();
    }

    @Test
    @DisplayName("The metaphone algorithm should truncate the encoded string to the specified maximum length")
    void metaphoneShouldBeTruncatedToMaxLength() {
        // Arrange
        final Metaphone metaphone = getStringEncoder();
        final int maxLength = 6;
        metaphone.setMaxCodeLen(maxLength);

        // The full metaphone encoding for "AXEAXEAXE" is "AKSKSKS".
        // This test verifies that the output is correctly truncated to the max length.
        final String input = "AXEAXEAXE";
        final String expectedTruncatedCode = "AKSKSK";

        // Act
        final String actualCode = metaphone.metaphone(input);

        // Assert
        assertEquals(expectedTruncatedCode, actualCode,
            "The metaphone code should be truncated to the max length of " + maxLength);
    }
}