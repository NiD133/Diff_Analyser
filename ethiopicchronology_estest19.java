package org.joda.time.chrono;

import org.joda.time.DateTimeConstants;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the EthiopicChronology class.
 */
public class EthiopicChronologyTest {

    /**
     * Tests that the Ethiopic Era (EE) constant has the correct value,
     * which should be equivalent to the Christian Era (CE).
     */
    @Test
    public void testEEConstant_isEquivalentToCE() {
        // The source code for EthiopicChronology defines its era constant 'EE'
        // as being equivalent to the standard Christian Era 'CE'.
        // This test verifies that this relationship holds.
        assertEquals("The Ethiopic Era (EE) should be equal to the Christian Era (CE)",
                DateTimeConstants.CE, EthiopicChronology.EE);
    }
}