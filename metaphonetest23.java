package org.apache.commons.codec.language;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.commons.codec.AbstractStringEncoderTest;
import org.junit.jupiter.api.Test;

/**
 * Tests the {@link Metaphone} encoder for specific encoding rules.
 */
public class MetaphoneTest extends AbstractStringEncoderTest<Metaphone> {

    @Override
    protected Metaphone createStringEncoder() {
        return new Metaphone();
    }

    /**
     * Tests the Metaphone rule where the sequences 'SH', 'SIO', and 'SIA' are encoded as 'X'.
     * This test verifies that the encoder correctly handles these specific cases.
     */
    @Test
    void shSioSiaShouldBeEncodedAsX() {
        // The metaphone() method is called directly to test specific rule transformations.
        assertEquals("XT", getStringEncoder().metaphone("SHOT"), "Rule: 'SH' -> 'X'");
        assertEquals("OTXN", getStringEncoder().metaphone("ODSIAN"), "Rule: 'SIA' -> 'X'");
        assertEquals("PLXN", getStringEncoder().metaphone("PULSION"), "Rule: 'SIO' -> 'X'");
    }
}