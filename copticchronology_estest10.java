package org.joda.time.chrono;

import org.joda.time.DateTimeConstants;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link CopticChronology} class.
 */
public class CopticChronologyTest {

    /**
     * Tests that the constant for the Coptic era, 'Anno Martyrum' (AM),
     * has the correct value, which should be equivalent to the Common Era (CE).
     */
    @Test
    public void testAmEraConstantIsCorrect() {
        // The CopticChronology.AM constant represents the 'Anno Martyrum' era.
        // According to the class documentation, this is equivalent to the
        // Common Era (CE) constant defined in DateTimeConstants.
        assertEquals("The AM era constant should be equal to CE.",
                DateTimeConstants.CE, CopticChronology.AM);
    }
}