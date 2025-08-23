package org.joda.time.chrono;

import org.joda.time.DateTimeField;
import org.joda.time.DateTimeZone;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Tests for the CopticChronology class.
 */
public class CopticChronologyTest {

    /**
     * Tests that the era field for the Coptic chronology is correctly configured
     * as 'Anno Martyrum' (AM), which has a value of 1.
     */
    @Test
    public void eraField_shouldBeCorrectlyConfiguredAsAnnoMartyrum() {
        // Arrange: Get an instance of the CopticChronology. Using a fixed time zone
        // like UTC makes the test more deterministic than using the system default.
        CopticChronology copticChronology = CopticChronology.getInstance(DateTimeZone.UTC);

        // Act: Access the era field through the public API.
        // This internally triggers the assembly of all chronology fields.
        DateTimeField eraField = copticChronology.era();

        // Assert: Verify that the era field is correctly set up.
        assertNotNull("The era field should not be null", eraField);

        // The Coptic era is 'Anno Martyrum' (AM), which corresponds to CE.
        // We verify its name, its constant value, and the expected numeric value.
        assertEquals("AM", eraField.getAsText());
        assertEquals(1, eraField.get(0L)); // The era value is constant for any instant.
        assertEquals("The AM constant should be 1", 1, CopticChronology.AM);
    }
}