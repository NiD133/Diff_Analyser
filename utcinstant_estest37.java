package org.threeten.extra.scale;

import org.junit.Test;

/**
 * Tests for the {@link UtcInstant} class, focusing on factory methods.
 */
public class UtcInstantTest {

    /**
     * Tests that the {@code UtcInstant.of(TaiInstant)} factory method throws
     * a {@code NullPointerException} when given a null input.
     * <p>
     * The method contract requires a non-null argument, and this test verifies
     * that this precondition is enforced.
     */
    @Test(expected = NullPointerException.class)
    public void of_taiInstant_throwsNullPointerException_forNullInput() {
        // The cast to (TaiInstant) is necessary to resolve method overload ambiguity
        // between of(TaiInstant) and of(Instant).
        UtcInstant.of((TaiInstant) null);
    }
}