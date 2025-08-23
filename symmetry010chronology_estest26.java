package org.threeten.extra.chrono;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.time.Clock;
import org.junit.Test;

/**
 * This test class focuses on verifying the behavior of the Symmetry010Chronology class.
 * The original test class name and inheritance are preserved as they might be
 * required by the test runner or test suite configuration.
 */
public class Symmetry010Chronology_ESTestTest26 extends Symmetry010Chronology_ESTest_scaffolding {

    /**
     * Tests that calling dateNow() with a null Clock throws a NullPointerException.
     * This is the expected behavior, as the method should not accept null arguments.
     */
    @Test(timeout = 4000)
    public void dateNow_withNullClock_throwsNullPointerException() {
        // Arrange: Get an instance of the chronology to test.
        Symmetry010Chronology chronology = Symmetry010Chronology.INSTANCE;

        // Act & Assert: Attempt to call the method with a null argument and verify the exception.
        try {
            chronology.dateNow((Clock) null);
            fail("Expected a NullPointerException to be thrown, but no exception was thrown.");
        } catch (NullPointerException e) {
            // The underlying implementation is expected to use Objects.requireNonNull(clock, "clock"),
            // so we verify the exception message for robustness.
            assertEquals("clock", e.getMessage());
        }
    }
}