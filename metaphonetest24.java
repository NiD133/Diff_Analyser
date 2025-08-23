package org.apache.commons.codec.language;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.commons.codec.AbstractStringEncoderTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests the Metaphone algorithm for specific encoding rules.
 * This class verifies the rule where "TCH" is encoded as "X".
 */
public class MetaphoneTest extends AbstractStringEncoderTest<Metaphone> {

    @Override
    protected Metaphone createStringEncoder() {
        return new Metaphone();
    }

    @Test
    @DisplayName("Metaphone should encode 'TCH' as 'X'")
    void shouldEncodeTchAsX() {
        // This test verifies that the Metaphone algorithm correctly handles the "TCH"
        // combination, which should be encoded to "X".
        assertEquals("RX", getStringEncoder().metaphone("RETCH"), "Encoding 'RETCH' should result in 'RX'");
        assertEquals("WX", getStringEncoder().metaphone("WATCH"), "Encoding 'WATCH' should result in 'WX'");
    }
}