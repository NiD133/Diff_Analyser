package org.apache.commons.codec.language;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.commons.codec.AbstractStringEncoderTest;
import org.junit.jupiter.api.Test;

/**
 * Tests the {@link Metaphone} encoder.
 * <p>
 * This class focuses on specific encoding rules and behaviors of the Metaphone algorithm.
 * </p>
 */
public class MetaphoneTest extends AbstractStringEncoderTest<Metaphone> {

    @Override
    protected Metaphone createStringEncoder() {
        return new Metaphone();
    }

    /**
     * Tests that the string "PHISH" is encoded to "FX".
     * This test case covers two specific Metaphone rules:
     * <ul>
     *     <li>'PH' is transformed to 'F'</li>
     *     <li>'SH' is transformed to 'X'</li>
     * </ul>
     */
    @Test
    void shouldEncodePhishAsFx() {
        // Arrange
        final String input = "PHISH";
        final String expectedEncoding = "FX";

        // Act
        final String actualEncoding = getStringEncoder().metaphone(input);

        // Assert
        assertEquals(expectedEncoding, actualEncoding,
            "Encoding 'PHISH' should correctly apply 'PH'->'F' and 'SH'->'X' rules.");
    }
}