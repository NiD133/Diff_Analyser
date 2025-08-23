package org.threeten.extra.chrono;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.time.chrono.Era;
import java.time.chrono.JapaneseEra;
import org.junit.Test;

/*
 * Note: The class name "BritishCutoverChronology_ESTestTest43" is auto-generated.
 * In a real-world scenario, this would be part of a well-named test suite,
 * for example, "BritishCutoverChronologyTest".
 */
public class BritishCutoverChronology_ESTestTest43 extends BritishCutoverChronology_ESTest_scaffolding {

    /**
     * Tests that dateYearDay() throws a ClassCastException when an incorrect Era type is provided.
     *
     * The BritishCutoverChronology is designed to work specifically with JulianEra. This test
     * verifies that providing an era from a different calendar system, such as JapaneseEra,
     * results in the expected exception, ensuring type safety.
     */
    @Test
    public void dateYearDay_withIncorrectEraType_throwsClassCastException() {
        // Arrange: Create the chronology and an era from an incompatible calendar system.
        BritishCutoverChronology chronology = new BritishCutoverChronology();
        Era incorrectEra = JapaneseEra.SHOWA;
        // The specific year and day values are not relevant for this type-check failure.
        int yearOfEra = 1;
        int dayOfYear = 1;

        // Act & Assert: Attempting to create a date with an incompatible era should fail.
        try {
            chronology.dateYearDay(incorrectEra, yearOfEra, dayOfYear);
            fail("Expected a ClassCastException to be thrown due to incorrect Era type.");
        } catch (ClassCastException e) {
            // Verify that the exception message clearly communicates the requirement.
            assertEquals("Era must be JulianEra", e.getMessage());
        }
    }
}