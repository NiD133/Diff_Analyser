package org.threeten.extra.scale;

import static org.junit.Assert.assertThrows;

import java.time.Instant;
import org.junit.Test;

/**
 * Test class for {@link UtcInstant}.
 */
public class UtcInstantTest {

    /**
     * Tests that the factory method {@code UtcInstant.of(Instant)} throws
     * a NullPointerException when given a null argument, as per its contract.
     */
    @Test
    public void ofInstant_whenArgumentIsNull_thenThrowsNullPointerException() {
        // The cast to Instant is necessary to resolve method overload ambiguity for the null literal.
        assertThrows(NullPointerException.class, () -> UtcInstant.of((Instant) null));
    }
}