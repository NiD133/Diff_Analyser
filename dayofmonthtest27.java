package org.threeten.extra;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.time.Clock;
import org.evosuite.runtime.mock.java.time.MockClock;

public class DayOfMonth_ESTestTest27 {

    /**
     * Verifies that an instance of DayOfMonth created using now(Clock) is equal to itself.
     *
     * This test confirms two behaviors:
     * 1. The `now(Clock)` factory method correctly creates a DayOfMonth instance from the provided clock.
     * 2. The `equals()` method is reflexive (an object is always equal to itself).
     */
    @Test
    public void nowFromClock_createsInstanceThatIsEqualToItself() {
        // Arrange: Set up a fixed clock and create a DayOfMonth instance from it.
        // The mock clock from EvoSuite consistently returns a date where the day is 14.
        final int EXPECTED_DAY = 14;
        Clock fixedClock = MockClock.systemDefaultZone();
        DayOfMonth dayOfMonth = DayOfMonth.now(fixedClock);

        // Act: The primary actions are the creation of the object in the Arrange step
        // and the `equals` comparison within the Assert step.

        // Assert: Verify the created object's state and its equality to itself.
        assertEquals("The day of month should be correctly extracted from the clock.",
                EXPECTED_DAY, dayOfMonth.getValue());
        assertEquals("An instance should always be equal to itself.",
                dayOfMonth, dayOfMonth);
    }
}