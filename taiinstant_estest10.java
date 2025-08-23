package org.threeten.extra.scale;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link TaiInstant}.
 */
public class TaiInstantTest {

    @Test
    public void withNano_should_updateNanoValue_whileKeepingSecondsUnchanged() {
        // Arrange: Create an instant at the TAI epoch (0 seconds, 0 nanoseconds).
        TaiInstant initialInstant = TaiInstant.ofTaiSeconds(0L, 0L);
        int newNanoValue = 91;

        // Act: Create a new instant by updating the nanosecond value.
        TaiInstant updatedInstant = initialInstant.withNano(newNanoValue);

        // Assert: The new instant should have the updated nanosecond value,
        // and the seconds part should remain unchanged, confirming immutability.
        assertEquals("Seconds should not be modified", 0L, updatedInstant.getTaiSeconds());
        assertEquals("Nanos should be updated to the new value", newNanoValue, updatedInstant.getNano());
    }
}