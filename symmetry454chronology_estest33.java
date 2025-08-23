package org.threeten.extra.chrono;

import static org.junit.Assert.fail;

import java.time.Instant;
import java.time.ZoneId;
import org.junit.Test;

/**
 * This class contains tests for the Symmetry454Chronology.
 * The original test was auto-generated and has been refactored for clarity.
 */
public class Symmetry454Chronology_ESTestTest33 extends Symmetry454Chronology_ESTest_scaffolding {

    /**
     * Tests that calling zonedDateTime(Instant, ZoneId) with a null ZoneId
     * throws a NullPointerException, as expected by the contract of similar
     * methods in java.time.
     */
    @Test(timeout = 4000)
    public void zonedDateTime_withNullZoneId_throwsNullPointerException() {
        // Arrange: Set up the test data.
        Symmetry454Chronology chronology = Symmetry454Chronology.INSTANCE;
        Instant anyInstant = Instant.EPOCH; // The specific instant does not affect this test's outcome.
        ZoneId nullZoneId = null;

        // Act & Assert: Call the method and verify it throws the expected exception.
        try {
            chronology.zonedDateTime(anyInstant, nullZoneId);
            fail("Expected a NullPointerException to be thrown, but no exception occurred.");
        } catch (NullPointerException expected) {
            // This is the expected behavior. The test passes.
        }
    }
}