package org.threeten.extra.scale;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the TaiInstant class.
 */
public class TaiInstantTest {

    /**
     * Tests that calling {@link TaiInstant#withTaiSeconds(long)} with the current
     * value returns an equal instance.
     * <p>
     * This confirms that the method correctly handles no-op updates, which is
     * important for immutable classes.
     */
    @Test
    public void withTaiSeconds_whenValueIsUnchanged_returnsAnEqualInstance() {
        // Arrange: Create a base instant at the TAI epoch.
        TaiInstant baseInstant = TaiInstant.ofTaiSeconds(0L, 0L);
        long sameSecondsValue = 0L;

        // Act: Call the 'with' method using the same seconds value.
        TaiInstant resultInstant = baseInstant.withTaiSeconds(sameSecondsValue);

        // Assert: The resulting instant should be equal to the base instant.
        assertEquals(baseInstant, resultInstant);
    }
}