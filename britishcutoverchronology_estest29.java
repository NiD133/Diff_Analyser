package org.threeten.extra.chrono;

import org.junit.Test;
import org.threeten.extra.chrono.JulianEra;
import java.time.chrono.Era;
import java.time.chrono.ThaiBuddhistEra;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

// The test class name and inheritance are kept from the original generated code.
public class BritishCutoverChronology_ESTestTest29 extends BritishCutoverChronology_ESTest_scaffolding {

    /**
     * Tests that prolepticYear() throws a ClassCastException when the provided era
     * is not a JulianEra, which is the only era type supported by this chronology.
     */
    @Test(timeout = 4000)
    public void prolepticYear_withInvalidEraType_throwsClassCastException() {
        // Arrange: Create the chronology and an era of an unsupported type.
        // The BritishCutoverChronology's prolepticYear method expects a JulianEra.
        BritishCutoverChronology chronology = new BritishCutoverChronology();
        Era invalidEra = ThaiBuddhistEra.BE;
        int yearOfEra = 2023; // The specific year is not relevant for this type check.

        // Act & Assert: Call prolepticYear and verify that the correct exception is thrown.
        ClassCastException exception = assertThrows(
                "Calling prolepticYear with a non-JulianEra should throw a ClassCastException.",
                ClassCastException.class,
                () -> chronology.prolepticYear(invalidEra, yearOfEra)
        );

        // Assert: Verify the exception message is as expected.
        assertEquals("Era must be JulianEra", exception.getMessage());
    }
}