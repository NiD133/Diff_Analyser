package org.threeten.extra.scale;

import org.junit.Test;

/**
 * Test class for the {@link TaiInstant} factory methods.
 */
public class TaiInstantTest {

    /**
     * Tests that the of(UtcInstant) factory method throws a NullPointerException
     * when passed a null argument, as required by its contract.
     */
    @Test(expected = NullPointerException.class)
    public void of_utcInstant_withNullInput_throwsNullPointerException() {
        // The cast to UtcInstant is necessary to resolve method ambiguity.
        TaiInstant.of((UtcInstant) null);
    }
}