package org.threeten.extra.scale;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * A test suite for the {@link TaiInstant} class, focusing on its immutability
 * and the behavior of its 'with' methods.
 */
public class TaiInstantTest {

    /**
     * Tests that {@code withTaiSeconds()} and {@code withNano()} create new instances
     * with the updated values, while leaving the original instances unchanged.
     */
    @Test
    public void withMethodsShouldReturnNewInstancesWithUpdatedValues() {
        // === Arrange: Create an initial TaiInstant ===
        // The factory method normalizes seconds and nanoseconds.
        // 124,934,400 seconds and -1,909 nanos becomes:
        // Seconds: 124,934,400 - 1 = 124,934,399
        // Nanos:   1,000,000,000 - 1,909 = 999,998,091
        long initialRawSeconds = 124934400L;
        long nanoAdjustment = -1909L;
        TaiInstant initialInstant = TaiInstant.ofTaiSeconds(initialRawSeconds, nanoAdjustment);

        long expectedInitialSeconds = 124934399L;
        int expectedInitialNanos = 999_998_091;
        assertEquals("Initial seconds should be correctly normalized", expectedInitialSeconds, initialInstant.getTaiSeconds());
        assertEquals("Initial nanos should be correctly normalized", expectedInitialNanos, initialInstant.getNano());

        // === Act & Assert 1: Test withTaiSeconds() ===
        long newSecondsValue = -1909L;
        TaiInstant instantWithNewSeconds = initialInstant.withTaiSeconds(newSecondsValue);

        // Verify the new instance has the updated seconds and original nanos
        assertEquals("Seconds should be updated to the new value", newSecondsValue, instantWithNewSeconds.getTaiSeconds());
        assertEquals("Nanos should be unchanged after updating seconds", expectedInitialNanos, instantWithNewSeconds.getNano());

        // Verify the original instance remains unchanged (immutability)
        assertEquals("Original instant's seconds should not change", expectedInitialSeconds, initialInstant.getTaiSeconds());
        assertEquals("Original instant's nanos should not change", expectedInitialNanos, initialInstant.getNano());

        // === Act & Assert 2: Test withNano() on the previously created instance ===
        int newNanosValue = 331;
        TaiInstant finalInstant = instantWithNewSeconds.withNano(newNanosValue);

        // Verify the final instance has the updated nanos and previous seconds
        assertEquals("Seconds should be unchanged after updating nanos", newSecondsValue, finalInstant.getTaiSeconds());
        assertEquals("Nanos should be updated to the new value", newNanosValue, finalInstant.getNano());

        // Verify the intermediate instance also remains unchanged (immutability)
        assertEquals("Intermediate instant's seconds should not change", newSecondsValue, instantWithNewSeconds.getTaiSeconds());
        assertEquals("Intermediate instant's nanos should not change", expectedInitialNanos, instantWithNewSeconds.getNano());
    }
}