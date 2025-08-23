package org.joda.time.chrono;

import org.joda.time.DateTimeConstants;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the EthiopicChronology class.
 */
// The original test class name `EthiopicChronology_ESTestTest9` and scaffolding are kept for context.
public class EthiopicChronology_ESTestTest9 extends EthiopicChronology_ESTest_scaffolding {

    /**
     * Tests that the public constant for the Ethiopic Era (EE) is correctly
     * defined as being equivalent to the Common Era (CE).
     */
    @Test
    public void ethiopianEraConstant_shouldBeEqualToCommonEra() {
        // The source code defines EthiopicChronology.EE as being equivalent to DateTimeConstants.CE.
        // This test verifies that this fundamental constant has the expected value.
        assertEquals("The Ethiopic Era constant (EE) should be equal to the Common Era constant (CE).",
                DateTimeConstants.CE, EthiopicChronology.EE);
    }
}