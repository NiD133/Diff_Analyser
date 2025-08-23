package org.joda.time.chrono;

import org.joda.time.DateTimeConstants;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

// The original test class structure is preserved.
public class EthiopicChronology_ESTestTest2 extends EthiopicChronology_ESTest_scaffolding {

    /**
     * Tests that the Ethiopic Era (EE) constant is correctly defined as
     * equivalent to the Common Era (CE).
     */
    @Test
    public void testEEConstantIsEqualToCE() {
        // The EthiopicChronology.EE constant is documented to be equivalent to CE.
        // This test verifies that its value matches DateTimeConstants.CE.
        assertEquals(DateTimeConstants.CE, EthiopicChronology.EE);
    }
}