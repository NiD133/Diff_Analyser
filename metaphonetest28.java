package org.apache.commons.codec.language;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.commons.codec.AbstractStringEncoderTest;
import org.junit.jupiter.api.Test;

/**
 * Tests the {@link Metaphone} encoder.
 *
 * This test class has been refactored for clarity from an original version
 * that had an unconventional name (MetaphoneTestTest28) and included several
 * helper methods that were not used by the test case in this file.
 */
public class MetaphoneTest extends AbstractStringEncoderTest<Metaphone> {

    @Override
    protected Metaphone createStringEncoder() {
        return new Metaphone();
    }

    /**
     * Tests that the Metaphone encoding of "WHY" results in an empty string.
     * This behavior is specific to the original Metaphone algorithm and differs
     * from other implementations (e.g., PHP's, which returns "H").
     *
     * @see <a href="https://issues.apache.org/jira/browse/CODEC-57">CODEC-57</a>
     */
    @Test
    void shouldEncodeWhyAsEmptyString() {
        // The original Metaphone algorithm returns an empty string for "WHY".
        // This test verifies compliance with that specific behavior.
        assertEquals("", getStringEncoder().metaphone("WHY"),
                     "Metaphone of 'WHY' should be an empty string");
    }
}