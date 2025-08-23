package org.threeten.extra.scale;

import org.junit.Test;
import java.time.Instant;

/**
 * Unit tests for the {@link TaiInstant} class.
 */
public class TaiInstantTest {

    /**
     * Tests that the {@code of(Instant)} factory method throws a NullPointerException
     * when given a null input.
     */
    @Test(expected = NullPointerException.class)
    public void of_withNullInstant_shouldThrowNullPointerException() {
        // The explicit cast to Instant is necessary to resolve method overload ambiguity
        // between of(Instant) and of(UtcInstant).
        TaiInstant.of((Instant) null);
    }
}